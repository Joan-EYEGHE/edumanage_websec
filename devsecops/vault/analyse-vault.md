# Analyse P5 — HashiCorp Vault : Gestion Centralisée des Secrets

> **Projet** : EduManage Secure — M2 CDSD, ISM Dakar  
> **Branche** : `websec`  
> **Date** : 21/08/2026  
> **Environnement** : Windows 11, Docker Desktop 29.7.2, Spring Boot 3.5.14-SNAPSHOT

---

## 1. Problème résolu

### 1.1 Situation avant P5

Les secrets de l'application — mot de passe PostgreSQL, clé JWT HS256 — étaient stockés
dans `application-dev.yml` et `application-prod.yml` sur le disque local. Ces fichiers
sont gitignorés, ce qui évite leur exposition dans le dépôt public. Mais plusieurs
problèmes subsistaient :

- Les secrets existent **en clair sur le disque** — accessible à tout process local
- **Aucune traçabilité** : impossible de savoir qui a lu un secret, quand, depuis où
- **Gestion manuelle** : en cas de compromission, retrouver et changer tous les endroits
  d'utilisation est une opération manuelle et risquée
- **Secrets statiques** : ils ne changent jamais automatiquement
- Le token root Vault utilisé pendant la configuration a **tous les droits sans restriction**

### 1.2 Ce que Vault apporte

Vault est un coffre-fort centralisé. Au lieu que chaque composant gère ses propres
fichiers de configuration avec des secrets en clair, tous les composants demandent
leurs secrets à Vault au démarrage, via une API authentifiée.

```
AVANT                               APRÈS
-----                               -----
Spring Boot                         Spring Boot
  └── application-dev.yml             └── bootstrap.yml (config Vault)
        └── password=xxx                    └── "Vault, donne-moi le mot de passe"
              (fichier sur disque)                └── Vault vérifie l'identité
                                                        └── retourne le secret
                                                              (jamais stocké côté app)
```

---

## 2. Architecture de déploiement

### 2.1 Choix d'infrastructure

**Vault tourne dans Docker Desktop (Windows natif)**, pas dans WSL2.

| Composant | Localisation | Justification |
|---|---|---|
| Vault | Conteneur Docker (`vault-edumanage`) | Isolation, pas d'installation système |
| PostgreSQL | Windows natif (service `postgresql-x64-16`) | Déjà en place, pas migré |
| Spring Boot | Windows natif (JVM locale) | Déjà en place |

**Problème réseau identifié et résolu** (voir section 5) : Vault dans Docker ne peut
pas atteindre PostgreSQL sur Windows via `localhost` ou `127.0.0.1`. Ces adresses
pointent vers l'intérieur du conteneur, pas vers l'hôte Windows.

### 2.2 Réseau Docker Desktop

Docker Desktop sur Windows tourne dans une VM légère (Hyper-V). Les adresses réseau
importantes :

| Adresse | Ce qu'elle désigne |
|---|---|
| `127.0.0.1` | Intérieur du conteneur uniquement |
| `172.17.0.1` | Gateway réseau bridge Docker — **ne joint PAS Windows directement** |
| `192.168.65.254` | IP réelle de l'hôte Windows vue depuis Docker Desktop ✅ |
| `host.docker.internal` | DNS Docker → résout en IPv6 (`fdc4:f303:...`) dans ce cas — **non fiable** |

**Découverte clé** : `host.docker.internal` résolvait en IPv6, provoquant
`network is unreachable`. La solution a été d'utiliser directement l'IP `192.168.65.254`.

---

## 3. Configuration Vault — étapes complètes

### 3.1 Lancement du conteneur

```powershell
docker run -d `
  --name vault-edumanage `
  --cap-add=IPC_LOCK `
  -p 8200:8200 `
  -e VAULT_DEV_ROOT_TOKEN_ID=root-token-edumanage `
  -e VAULT_DEV_LISTEN_ADDRESS=0.0.0.0:8200 `
  hashicorp/vault:1.17
```

**Paramètres expliqués :**
- `--cap-add=IPC_LOCK` : empêche les secrets d'être swappés sur disque (sécurité mémoire)
- `VAULT_DEV_ROOT_TOKEN_ID` : token root prédéfini pour la configuration initiale
- `VAULT_DEV_LISTEN_ADDRESS=0.0.0.0:8200` : écoute sur toutes les interfaces (nécessaire
  pour que Spring Boot sur Windows puisse joindre Vault dans Docker via `localhost:8200`)
- Mode `dev` : Vault démarre déjà déverrouillé (`Sealed: false`), stockage en mémoire

**Vérification :**
```powershell
docker exec -e VAULT_ADDR=http://127.0.0.1:8200 vault-edumanage vault status
```

Résultat attendu : `Sealed: false`, `Storage Type: inmem`.

> ⚠ Note : le client Vault à l'intérieur du conteneur tente par défaut HTTPS.
> Il faut passer `VAULT_ADDR=http://...` explicitement dans chaque commande `docker exec`.

**Capture :** `screenshots/vault-status.png`

---

### 3.2 Moteur KV v2 — secrets statiques versionnés

**Pourquoi KV v2 et pas v1 ?**  
KV v2 maintient un historique de versions pour chaque secret. Si un secret est
corrompu ou compromis, on peut identifier quand le changement s'est produit et
revenir à la version précédente. KV v1 n'a pas cette capacité.

En mode `dev`, Vault active automatiquement KV v2 sur le chemin `secret/`. Vérification :

```powershell
docker exec -e VAULT_ADDR=http://127.0.0.1:8200 -e VAULT_TOKEN=root-token-edumanage `
  vault-edumanage vault secrets list -detailed
```

Colonne `Options` de la ligne `secret/` : `map[version:2]` ✅

**Stockage des secrets applicatifs :**

```powershell
# Mot de passe PostgreSQL
docker exec -e VAULT_ADDR=http://127.0.0.1:8200 -e VAULT_TOKEN=root-token-edumanage `
  vault-edumanage vault kv put secret/edumanage/database `
  password="[CONFIDENTIEL]" url="jdbc:postgresql://localhost:5432/edumanage" username="edumanage"

# Clé JWT
docker exec -e VAULT_ADDR=http://127.0.0.1:8200 -e VAULT_TOKEN=root-token-edumanage `
  vault-edumanage vault kv put secret/edumanage/jwt `
  key="[CONFIDENTIEL]" time="18000"
```

**Vérification (extrait) :**
```
Secret Path : secret/data/edumanage/database
version     : 1
created_time: 2026-08-21T13:56:36Z
```

**Capture :** `screenshots/kv-v2-versioning.png`

---

### 3.3 AppRole — authentification machine-à-machine

**Pourquoi AppRole ?**  
Le token root a tous les droits sur Vault. Le donner à Spring Boot serait une
faille de sécurité majeure. AppRole crée une identité spécifique pour l'application
avec uniquement les permissions nécessaires (principe du moindre privilège).

**Mécanisme :**
1. L'application présente un `role_id` (identifiant public) et un `secret_id` (mot de passe jetable)
2. Vault valide la paire et retourne un token limité (TTL 1h, max 4h)
3. L'application utilise ce token pour lire ses secrets

**Activation AppRole :**
```powershell
docker exec -e VAULT_ADDR=http://127.0.0.1:8200 -e VAULT_TOKEN=root-token-edumanage `
  vault-edumanage vault auth enable approle
```

**Création de la politique d'accès** (accès limité à `secret/edumanage/*` en lecture) :

Contenu du fichier de politique (`/tmp/edumanage-policy.hcl`) :
```hcl
path "secret/data/edumanage" {
  capabilities = ["read"]
}
path "secret/data/edumanage/*" {
  capabilities = ["read"]
}
```

> ⚠ Les deux chemins sont nécessaires : `secret/data/edumanage` (contexte racine
> que Spring Cloud Vault lit par défaut) ET `secret/data/edumanage/*` (sous-chemins).
> Oublier le premier provoque une erreur `403 permission denied` au démarrage.

```powershell
# Écriture de la politique dans le conteneur
docker exec vault-edumanage sh -c 'printf "path \"secret/data/edumanage\" {\n  capabilities = [\"read\"]\n}\npath \"secret/data/edumanage/*\" {\n  capabilities = [\"read\"]\n}\n" > /tmp/edumanage-policy.hcl'

# Application de la politique
docker exec -e VAULT_ADDR=http://127.0.0.1:8200 -e VAULT_TOKEN=root-token-edumanage `
  vault-edumanage vault policy write edumanage-policy /tmp/edumanage-policy.hcl
```

> ⚠ La syntaxe heredoc (`<< 'EOF'`) ne fonctionne pas avec `docker exec` sur Windows
> (Git Bash ou PowerShell). Contournement : écrire la politique dans un fichier
> à l'intérieur du conteneur via `sh -c 'printf ...'`.

**Création du rôle EduManage :**
```powershell
docker exec -e VAULT_ADDR=http://127.0.0.1:8200 -e VAULT_TOKEN=root-token-edumanage `
  vault-edumanage vault write auth/approle/role/edumanage-role `
  token_policies="edumanage-policy" token_ttl=1h token_max_ttl=4h
```

**Récupération des credentials AppRole :**
```powershell
# role_id (identifiant public, stable)
docker exec -e VAULT_ADDR=http://127.0.0.1:8200 -e VAULT_TOKEN=root-token-edumanage `
  vault-edumanage vault read auth/approle/role/edumanage-role/role-id

# secret_id (mot de passe jetable, généré à la demande)
docker exec -e VAULT_ADDR=http://127.0.0.1:8200 -e VAULT_TOKEN=root-token-edumanage `
  vault-edumanage vault write -f auth/approle/role/edumanage-role/secret-id
```

**Test d'authentification AppRole :**
```powershell
docker exec -e VAULT_ADDR=http://127.0.0.1:8200 vault-edumanage `
  vault write auth/approle/login role_id="<ROLE_ID>" secret_id="<SECRET_ID>"
```

Résultat obtenu :
```
token_duration          1h
token_policies          ["default" "edumanage-policy"]
token_meta_role_name    edumanage-role
```

Token limité, durée 1h, politique `edumanage-policy` uniquement ✅

---

### 3.4 Intégration Spring Boot

**Dépendances ajoutées au `pom.xml` :**

```xml
<!-- BOM Spring Cloud 2025.0.3 — compatible Spring Boot 3.5.x -->
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-dependencies</artifactId>
            <version>2025.0.3</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>

<!-- Intégration Vault -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-vault-config</artifactId>
</dependency>

<!-- Support bootstrap.yml — obligatoire depuis Spring Cloud 2020.0.x -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-bootstrap</artifactId>
</dependency>
```

> ⚠ **Décision de version** : la version `2023.0.3` initialement envisagée est
> incompatible avec Spring Boot 3.5.x (elle cible Spring Boot 3.1.x). La version
> correcte est `2025.0.3` — première version compatible Spring Boot 3.5.

> ⚠ **`spring-cloud-starter-bootstrap` obligatoire** : depuis Spring Cloud 2020.0.x,
> le support de `bootstrap.yml` a été extrait dans une dépendance séparée. Sans elle,
> `bootstrap.yml` est silencieusement ignoré et Vault tente une auth TOKEN par défaut,
> provoquant : `Cannot create authentication mechanism for TOKEN`.

**Fichier `bootstrap.yml`** (chargé avant `application.yml`) :

```yaml
spring:
  application:
    name: edumanage
  profiles:
    active: dev
  cloud:
    vault:
      uri: http://localhost:8200
      authentication: APPROLE
      app-role:
        role-id: [CONFIDENTIEL — voir gestionnaire de secrets]
        secret-id: [CONFIDENTIEL — voir gestionnaire de secrets]
      kv:
        enabled: true
        backend: secret
        default-context: edumanage
      config:
        lifecycle:
          enabled: false
```

**Pourquoi `bootstrap.yml` et pas `application.yml` ?**  
`bootstrap.yml` est chargé en tout premier dans le cycle Spring Boot, avant même
`application.yml`. C'est obligatoire pour Vault : si la config Vault était dans
`application.yml`, Spring Boot essaierait de charger ses propriétés avant de savoir
où trouver Vault.

**Pourquoi `profiles.active: dev` dans `bootstrap.yml` ?**  
Sans cette ligne, le profil `dev` n'est pas activé au moment où Spring Cloud Vault
charge le contexte bootstrap. Résultat : `application-dev.yml` (qui contient les
propriétés datasource) n'est pas chargé → erreur `Failed to determine a suitable driver class`.

**Log de démarrage confirmant l'intégration :**
```
Fetching config from Vault at: secret/edumanage/dev
Scheduling Token renewal
Located property source: [BootstrapPropertySource {name='bootstrapProperties-secret/edumanage'}]
The following 1 profile is active: "dev"
Tomcat started on port 8082 (http) with context path '/api'
```

**Capture :** `screenshots/spring-boot-vault-start.png`

**Test de bout en bout — login admin :**
```powershell
curl -X POST http://localhost:8082/api/users/login `
  -H "Content-Type: application/json" `
  -d "{`"email`":`"joanarcher26@gmail.com`",`"password`":`"joan@admin2`"}"
```

Réponse : token JWT valide, rôle `ADMINISTRATEUR` ✅

---

### 3.5 Dynamic secrets PostgreSQL

**Principe :** au lieu d'un mot de passe PostgreSQL fixe stocké dans Vault, Vault
génère à la demande un utilisateur PostgreSQL temporaire avec un mot de passe
aléatoire. Cet utilisateur expire automatiquement après le TTL défini (1h).

**Problème réseau résolu :** Vault dans Docker ne peut pas atteindre PostgreSQL
sur Windows via `localhost`. Voir section 5 pour le détail complet du diagnostic.
Solution : utiliser l'IP `192.168.65.254` (IP de l'hôte Windows vue depuis Docker Desktop)
et l'utilisateur `postgres` (superutilisateur PostgreSQL, pas `edumanage`).

**Étapes de configuration :**

```powershell
# 1. Activer le moteur database
docker exec -e VAULT_ADDR=http://127.0.0.1:8200 -e VAULT_TOKEN=root-token-edumanage `
  vault-edumanage vault secrets enable database

# 2. Configurer la connexion PostgreSQL
# Note : utilisateur postgres (superutilisateur) — seul avec droits de CREATE ROLE
# Note : IP 192.168.65.254 = hôte Windows vu depuis Docker Desktop
# Note : pg_hba.conf doit autoriser 192.168.65.0/24
docker exec -e VAULT_ADDR=http://127.0.0.1:8200 -e VAULT_TOKEN=root-token-edumanage `
  vault-edumanage vault write database/config/edumanage-db `
  plugin_name=postgresql-database-plugin `
  allowed_roles="edumanage-dynamic" `
  connection_url="postgresql://{{username}}:{{password}}@192.168.65.254:5432/edumanage?sslmode=disable" `
  username="postgres" `
  password="[CONFIDENTIEL]"
```

**Création du rôle dynamic (depuis Git Bash — échappement PowerShell incompatible) :**
```bash
docker exec -e VAULT_ADDR=http://127.0.0.1:8200 -e VAULT_TOKEN=root-token-edumanage \
  vault-edumanage vault write database/roles/edumanage-dynamic \
  db_name=edumanage-db \
  creation_statements="CREATE ROLE \"{{name}}\" WITH LOGIN PASSWORD '{{password}}' VALID UNTIL '{{expiration}}'; GRANT CONNECT ON DATABASE edumanage TO \"{{name}}\";" \
  default_ttl="1h" max_ttl="24h"
```

**Génération d'un credential dynamique :**
```powershell
docker exec -e VAULT_ADDR=http://127.0.0.1:8200 -e VAULT_TOKEN=root-token-edumanage `
  vault-edumanage vault read database/creds/edumanage-dynamic
```

Résultat obtenu :
```
lease_id           database/creds/edumanage-dynamic/klKvvf0dIwAFjCFeQSknnwum
lease_duration     1h
lease_renewable    true
password           [MASQUÉ]
username           v-token-edumanag-3lHr35Fvt8QFPLCBBmbN-1787329188
```

L'utilisateur `v-token-edumanag-3lHr35Fvt8QFPLCBBmbN-...` n'existait pas avant cette
commande. Il sera automatiquement supprimé de PostgreSQL après 1h. ✅

**Capture :** `screenshots/dynamic-secret-rotation.png`

---

### 3.6 Rotation automatique

**Configuration de la rotation sur la connexion database :**
```powershell
docker exec -e VAULT_ADDR=http://127.0.0.1:8200 -e VAULT_TOKEN=root-token-edumanage `
  vault-edumanage vault write database/config/edumanage-db rotation_period=86400
```

`rotation_period=86400` = 86400 secondes = 24h. Vault changera automatiquement
le mot de passe de l'utilisateur `postgres` dans PostgreSQL toutes les 24h, sans
intervention manuelle.

**Capture :** `screenshots/dynamic-secret-rotation.png`

---

## 4. Résultats et preuves

| Mécanisme | Preuve | Capture |
|---|---|---|
| Vault opérationnel | `Sealed: false`, `Version: 1.17.6` | `screenshots/vault-status.png` |
| KV v2 | `version: 1` sur les secrets stockés | `screenshots/kv-v2-versioning.png` |
| AppRole | Token retourné avec `token_policies: ["edumanage-policy"]` | — |
| Intégration Spring Boot | Log `Fetching config from Vault`, démarrage port 8082 | `screenshots/spring-boot-vault-start.png` |
| Dynamic secrets | Username temporaire généré `v-token-edumanag-...`, TTL 1h | `screenshots/dynamic-secret-rotation.png` |
| Rotation automatique | `rotation_period=86400` configuré | `screenshots/dynamic-secret-rotation.png` |

---

## 5. Obstacles rencontrés et solutions

### O1 — `bootstrap.yml` ignoré silencieusement

**Symptôme :** `Cannot create authentication mechanism for TOKEN. This method requires
either a Token (spring.cloud.vault.token) or a token file at ~/.vault-token.`

**Cause :** depuis Spring Cloud 2020.0.x, le support de `bootstrap.yml` nécessite
la dépendance `spring-cloud-starter-bootstrap`. Sans elle, le fichier est ignoré
et Vault tente une auth TOKEN par défaut.

**Solution :** ajouter `spring-cloud-starter-bootstrap` au `pom.xml`.

---

### O2 — Profil `dev` non activé avec bootstrap

**Symptôme :** `Failed to determine a suitable driver class` — datasource PostgreSQL
introuvable au démarrage.

**Cause :** `bootstrap.yml` court-circuite le mécanisme normal d'activation des profils.
Le profil `dev` n'était pas activé au moment où le contexte bootstrap chargeait,
donc `application-dev.yml` n'était pas lu.

**Solution :** ajouter `spring.profiles.active: dev` directement dans `bootstrap.yml`.

---

### O3 — Politique Vault `403 permission denied`

**Symptôme :** `Could not locate PropertySource: Status 403 Forbidden [secret/data/edumanage]`

**Cause :** la politique initiale couvrait `secret/data/edumanage/*` (avec wildcard)
mais pas `secret/data/edumanage` (chemin racine). Spring Cloud Vault lit le contexte
racine en premier.

**Solution :** ajouter les deux chemins dans la politique :
```hcl
path "secret/data/edumanage" { capabilities = ["read"] }
path "secret/data/edumanage/*" { capabilities = ["read"] }
```

---

### O4 — Heredoc incompatible avec `docker exec` sur Windows

**Symptôme :** `'policy' parameter not supplied or empty` malgré un heredoc valide.

**Cause :** Git Bash sur Windows ne transmet pas correctement le stdin via `docker exec`
avec la syntaxe `<< 'EOF'`.

**Solution :** écrire le contenu dans un fichier à l'intérieur du conteneur via
`sh -c 'printf ...'`, puis le référencer par chemin.

---

### O5 — PostgreSQL Windows inaccessible depuis Docker

**Symptôme :** `dial tcp 172.17.0.1:5432: connect: connection refused`

**Cause :** Docker Desktop sur Windows tourne dans une VM. `172.17.0.1` est la
gateway du réseau bridge Docker, pas l'IP de l'hôte Windows. `host.docker.internal`
résolvait en IPv6 (`fdc4:f303:...`) — non routé.

**Diagnostic :**
```powershell
docker exec vault-edumanage sh -c "nc -zv host.docker.internal 5432"
# → host.docker.internal (192.168.65.254:5432) open
```

L'IP réelle de l'hôte Windows vue depuis Docker Desktop est `192.168.65.254`.

**Actions nécessaires :**
1. Ajouter la règle `pg_hba.conf` : `host all edumanage 192.168.65.0/24 scram-sha-256`
2. Ajouter règle pare-feu Windows pour `192.168.65.0/24` sur port 5432
3. Redémarrer PostgreSQL (en PowerShell administrateur)
4. Utiliser l'IP `192.168.65.254` dans la connection_url Vault

---

### O6 — Utilisateur PostgreSQL insuffisant

**Symptôme :** même avec la bonne IP, `authentication failed for user "edumanage"`.

**Cause :** l'utilisateur `edumanage` n'est pas le propriétaire PostgreSQL —
il ne dispose pas des droits `CREATE ROLE` nécessaires pour que Vault génère
des dynamic secrets.

**Solution :** utiliser l'utilisateur `postgres` (superutilisateur PostgreSQL)
pour la connexion Vault → database engine.

---

### O7 — Échappement PowerShell incompatible pour `creation_statements`

**Symptôme :** `The command parameter was already specified.`

**Cause :** PowerShell interprète les guillemets échappés dans `creation_statements`
différemment. La commande `docker exec` reçoit des arguments malformés.

**Solution :** exécuter la commande depuis Git Bash, qui gère correctement
l'échappement dans ce contexte.

---

## 6. Dette technique

| # | Description | Gravité | Statut |
|---|---|---|---|
| S22 | Vault en mode `dev` — données en mémoire, perdues au redémarrage du conteneur | Moyenne | Acceptable — projet académique, jamais déployé |
| S23 | Token root `root-token-edumanage` non révoqué après configuration | Moyenne | Acceptable — projet académique |
| S24 | `secret_id` AppRole sans TTL ni limite d'utilisation (`secret_id_ttl: 0s`, `secret_id_num_uses: 0`) | Faible | Documenter dans rapport |
| S25 | `role_id` et `secret_id` en clair dans `bootstrap.yml` | Moyenne | En production : injecter via variables d'environnement ou fichier gitignored |
| S26 | Pas de persistance Vault (volume Docker) — reconfiguration complète à chaque redémarrage conteneur | Moyenne | Acceptable — projet académique |
| S27 | Connexion database engine utilise `postgres` (superutilisateur) au lieu d'un utilisateur dédié avec droits minimaux | Faible | Hors scope académique |

---

## 7. Décisions d'architecture

| # | Décision | Justification |
|---|---|---|
| D19 | Vault en mode `dev` Docker | Zéro installation système, reproductible, suffisant pour la démo académique |
| D20 | Spring Cloud `2025.0.3` (pas `2023.0.3`) | Seule version compatible Spring Boot 3.5.x — `2023.0.3` cible Spring Boot 3.1.x |
| D21 | IP `192.168.65.254` et non `host.docker.internal` | `host.docker.internal` résolvait en IPv6 non routé dans cet environnement |
| D22 | Utilisateur `postgres` pour database engine | Seul utilisateur avec droits `CREATE ROLE` pour les dynamic secrets |
| D23 | `creation_statements` exécuté depuis Git Bash | PowerShell incompatible avec l'échappement de guillemets imbriqués |
| D24 | `spring.profiles.active: dev` dans `bootstrap.yml` | Nécessaire pour que `application-dev.yml` soit chargé dans le contexte bootstrap |
