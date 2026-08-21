# P5 — HashiCorp Vault : Gestion Centralisée des Secrets

## Objectif

Remplacer la gestion statique des secrets (fichiers `application-dev.yml` sur disque)
par un coffre-fort centralisé. Vault stocke, contrôle l'accès, génère et fait tourner
les secrets de l'application EduManage de façon automatisée.

---

## Stack utilisée

| Composant | Version | Rôle |
|---|---|---|
| HashiCorp Vault | 1.17.6 | Coffre-fort de secrets |
| Docker Desktop | 29.7.2 | Conteneur Vault |
| Spring Cloud Vault | 2025.0.3 (BOM) | Intégration Spring Boot ↔ Vault |
| spring-cloud-starter-bootstrap | 2025.0.3 | Chargement `bootstrap.yml` avant `application.yml` |
| PostgreSQL | 16 (Windows natif) | Cible des dynamic secrets |

---

## Ce que couvre P5

| Mécanisme | Description |
|---|---|
| **KV v2** | Moteur clé-valeur versionné — secrets statiques stockés avec historique |
| **AppRole** | Authentification machine-à-machine — Spring Boot s'authentifie avec `role_id` + `secret_id` |
| **Dynamic secrets** | Vault génère des credentials PostgreSQL temporaires à la demande (TTL 1h) |
| **Rotation automatique** | Vault renouvelle le mot de passe de l'utilisateur database toutes les 24h |

---

## Lancer Vault (à chaque session)

Vault tourne en mode `dev` dans Docker. Les données sont en mémoire — **un redémarrage
du conteneur efface tout**. Pour une démo, relancer le conteneur et reconfigurer.

```powershell
# Démarrer le conteneur Vault
docker run -d `
  --name vault-edumanage `
  --cap-add=IPC_LOCK `
  -p 8200:8200 `
  -e VAULT_DEV_ROOT_TOKEN_ID=root-token-edumanage `
  -e VAULT_DEV_LISTEN_ADDRESS=0.0.0.0:8200 `
  hashicorp/vault:1.17

# Vérifier que Vault est opérationnel
docker exec -e VAULT_ADDR=http://127.0.0.1:8200 vault-edumanage vault status
```

Token root de développement : `root-token-edumanage` (ne jamais utiliser en production).

---

## Démarrer le backend avec Vault

```powershell
# Depuis main/
./mvnw spring-boot:run
```

Le backend contacte Vault au démarrage via `bootstrap.yml`. Log attendu :
```
Fetching config from Vault at: secret/edumanage/dev
Scheduling Token renewal
```

---

## Arborescence des livrables

```
devsecops/vault/
├── README.md                       ← ce fichier
├── analyse-vault.md                ← analyse complète : configuration, obstacles, résultats
└── screenshots/                    ← preuves visuelles
    ├── vault-status.png            ← vault status (Sealed: false)
    ├── kv-v2-versioning.png        ← vault kv get avec metadata version
    ├── dynamic-secret-rotation.png ← génération credential dynamique + rotation
    └── spring-boot-vault-start.png ← démarrage Spring Boot avec Vault
```

---

## Contacts et ressources

- Documentation Vault : https://developer.hashicorp.com/vault/docs
- Spring Cloud Vault : https://docs.spring.io/spring-cloud-vault/docs/current/reference/html/
- Compatibilité Spring Boot 3.5.x → Spring Cloud `2025.0.3`
