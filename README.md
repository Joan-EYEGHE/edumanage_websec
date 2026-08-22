# EduManage Secure

Application web de gestion de centre de formation (inscriptions, paiements, contrôle d'accès par rôle) construite comme projet académique de cybersécurité (M2 CDSD, ISM Dakar) pour démontrer la sécurité *by design* : CIA, STRIDE, OWASP Top 10, DevSecOps.

**Auteur :** Joan EYEGHE BOULINGUI ([Joan-EYEGHE](https://github.com/Joan-EYEGHE)) — M2 CDSD, ISM Dakar
**Repo :** [Joan-EYEGHE/edumanage_websec](https://github.com/Joan-EYEGHE/edumanage_websec) — branche `websec`

---

## En 30 secondes

| Couche | Technologie | Détail |
|---|---|---|
| Backend | Spring Boot 3.5 / Java 21 | Port **8082**, context-path `/api/` |
| Frontend | React 19 + Vite | Port 5173 (`npm run dev -- --host`) |
| Base de données | PostgreSQL 16 | **Native Windows**, pas de conteneur |
| Secrets | HashiCorp Vault 1.17.6 | Docker, mode dev, `http://localhost:8200` |
| Reverse proxy | NGINX | Docker, HTTPS, headers de sécurité |
| Monitoring | Prometheus + Grafana | Docker |
| CI/CD | GitHub Actions | SonarCloud (SAST) · Trivy · Gitleaks |
| Tests DAST | OWASP ZAP | Scan actif authentifié |
| Tests UI | Selenium WebDriver | 3 scénarios RBAC (hors pipeline CI) |

Quatre rôles : `ADMINISTRATEUR`, `GESTIONNAIRE`, `FORMATEUR`, `APPRENANT`.

Pour l'architecture de sécurité détaillée (DevSecOps, matrice OWASP Top 10, audit ASVS, analyse RBAC/Vault) : voir **[devsecops/README.md](devsecops/README.md)**.

---

## Prérequis

- **Java 21** (Temurin recommandé)
- **Maven** (le wrapper `mvnw`/`mvnw.cmd` est inclus, pas besoin d'installer Maven globalement)
- **Node.js 20 LTS** + npm
- **PostgreSQL 16** installé en natif sur la machine (pas de conteneur — voir section dédiée)
- **Docker Desktop** (pour Vault, NGINX, Prometheus/Grafana)
- **Git**

> ⚠️ Le backend écoute sur le port **8082**, pas 8080 (souvent occupé par Keycloak ou un autre service local). Vérifiez ce port avant de démarrer.

---

## 1. Cloner le repo

```bash
git clone https://github.com/Joan-EYEGHE/edumanage_websec.git
cd edumanage_websec
```

---

## 2. Configuration — fichiers à créer manuellement

Trois fichiers sont **gitignorés** (ils contiennent des secrets) et n'existent pas après un clone. Créez-les avec le contenu ci-dessous, en remplaçant les placeholders par vos propres valeurs.

### `src/main/resources/application-dev.yml`

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/edumanage
    username: postgres
    password: <votre_mot_de_passe_postgres>

jwt:
  secret: <une_chaine_secrete_longue_et_aleatoire>
  expiration: 3600000
```

> Adaptez les clés `jwt.*` et `spring.datasource.*` à ce que consomme réellement votre configuration de sécurité (`TokenServiceImpl`) — ce squelette couvre le strict minimum pour démarrer.

### `src/main/resources/application-prod.yml`

Même structure que `application-dev.yml`, avec des valeurs de production. Non nécessaire pour un lancement en local avec le profil `dev` (actif par défaut).

### `src/main/resources/bootstrap.yml`

Requis uniquement si vous démarrez Vault (section 4). Sans Vault, le backend démarre quand même mais ne consomme aucun secret centralisé.

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
        role-id: <role-id-genere-par-vault>
        secret-id: <secret-id-genere-par-vault>
      kv:
        enabled: true
        backend: secret
        default-context: edumanage
      config:
        lifecycle:
          enabled: false
```

`role-id` et `secret-id` sont générés lors de la configuration AppRole de Vault — voir **[devsecops/vault/README.md](devsecops/vault/README.md)** pour la procédure complète.

### Variables d'environnement (Cloudinary)

Le backend lit ces trois variables au démarrage (`CloudinaryConfig`) — **l'application ne démarre pas sans elles**, même si aucun endpoint n'appelle actuellement le service d'upload associé :

```bash
CLOUDINARY_CLOUD_NAME=<votre_cloud_name>
CLOUDINARY_API_KEY=<votre_api_key>
CLOUDINARY_API_SECRET=<votre_api_secret>
```

> Si vous ne souhaitez pas configurer Cloudinary,
> renseignez des valeurs non vides quelconques —
> le service ne sera pas appelé si aucun endpoint
> d'upload n'est sollicité.

Un compte gratuit sur [cloudinary.com](https://cloudinary.com) suffit pour obtenir ces valeurs.

---

## 3. Initialiser PostgreSQL

PostgreSQL tourne en **natif sur Windows**, pas dans Docker. Une fois installé :

```sql
CREATE DATABASE edumanage;
```

Le schéma est généré automatiquement au démarrage (`spring.jpa.hibernate.ddl-auto: update`). Renseignez l'utilisateur/mot de passe correspondants dans `application-dev.yml` (section 2).

---

## 4. Lancer les services Docker (ordre important)

### Vault — à démarrer **avant** le backend

```powershell
docker run -d `
  --name vault-edumanage `
  --cap-add=IPC_LOCK `
  -p 8200:8200 `
  -e VAULT_DEV_ROOT_TOKEN_ID=root-token-edumanage `
  -e VAULT_DEV_LISTEN_ADDRESS=0.0.0.0:8200 `
  hashicorp/vault:1.17
```

UI Vault : `http://localhost:8200` — token root de dev : `root-token-edumanage` (ne jamais utiliser en production). Procédure complète de configuration KV/AppRole : **[devsecops/vault/README.md](devsecops/vault/README.md)**.

> Vault tourne en mode `dev` : les données sont en mémoire et perdues à chaque redémarrage du conteneur. C'est une limite assumée pour ce projet académique.

### NGINX (reverse proxy HTTPS)

```powershell
cd devsecops/nginx
docker compose up -d
```

Accès : `https://localhost` (certificat auto-signé, avertissement navigateur normal).

### Monitoring (Prometheus + Grafana)

```powershell
cd devsecops/monitoring
docker compose up -d
```

| Service | URL | Identifiants |
|---|---|---|
| Prometheus | http://localhost:9091 | aucun |
| Grafana | http://localhost:3000 | admin / admin |

---

## 5. Démarrer le backend

```bash
./mvnw spring-boot:run
```

Le profil `dev` est actif par défaut (`application.yaml`) — pas besoin de l'activer manuellement. Le backend écoute sur `http://localhost:8082/api/`.

- Swagger UI : http://localhost:8082/api/swagger-ui.html
- Actuator Prometheus : http://localhost:8082/api/actuator/prometheus

---

## 6. Démarrer le frontend

```bash
cd frontend
npm install
npm run dev -- --host
```

Le frontend démarre sur `http://localhost:5173`.

---

## 7. Se connecter

Des comptes de test sont créés automatiquement au démarrage (profil `dev`) :

| Compte | Email | Mot de passe | Rôle |
|---|---|---|---|
| Admin | *(compte configuré dans application-dev.yml)* | *(voir application-dev.yml)* | ADMINISTRATEUR |
| Test gestionnaire | `test.gestionnaire@edumanage.local` | `Test1234!` | GESTIONNAIRE |
| Test formateur | `test.formateur@edumanage.local` | `Test1234!` | FORMATEUR |
| Test apprenant | `test.apprenant@edumanage.local` | `Test1234!` | APPRENANT |

---

## 8. Lancer les tests

### Tests unitaires

```bash
mvn test
```

### Tests Selenium (optionnel)

Nécessitent Chrome installé, le backend **et** le frontend démarrés. Exclus du pipeline CI (incompatibles avec l'environnement headless GitHub Actions).

```bash
mvn test -Dgroups=selenium
```

Détail des scénarios couverts : **[devsecops/selenium/README.md](devsecops/selenium/README.md)**.

---

## Arrêter les services

```powershell
# Vault
docker stop vault-edumanage

# NGINX
cd devsecops/nginx && docker compose down

# Monitoring
cd devsecops/monitoring && docker compose down
```

---

## Sécurité & DevSecOps

Ce projet couvre l'ensemble du périmètre DevSecOps du module M13 : pipeline CI/CD sécurisé (SonarCloud, Trivy, Gitleaks), RBAC backend/frontend, tests Selenium, gestion centralisée des secrets (Vault), DAST (OWASP ZAP), monitoring (Prometheus/Grafana), audit de conformité OWASP ASVS L1, et reverse proxy NGINX durci.

L'architecture de sécurité complète, la matrice de couverture OWASP Top 10 et le détail de chaque point (P2 à P11) sont documentés dans **[devsecops/README.md](devsecops/README.md)**.

---

## Dette technique connue

Quelques limites sont assumées dans un contexte académique (Vault en mode dev, JWT stocké en `localStorage`, pas d'invalidation de token à la déconnexion, etc.). Liste complète et justifications : voir la section *Dette technique assumée* de **[devsecops/README.md](devsecops/README.md)**.

---

## Structure du repo

```
.
├── src/                    ← backend Spring Boot
├── frontend/                ← frontend React + Vite
├── devsecops/                ← documentation et livrables DevSecOps (Module 13)
│   ├── asvs/ monitoring/ nginx/ rbac/ selenium/ sonar/ vault/ zap/
│   └── README.md            ← synthèse sécurité globale
├── pom.xml                  ← build Maven backend
└── .github/workflows/        ← pipeline CI/CD (SonarCloud, Trivy, Gitleaks)
```

