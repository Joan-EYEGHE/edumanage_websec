# EduManage Secure — Synthèse DevSecOps

> Joan EYEGHE BOULINGUI — M2 CDSD, ISM Dakar  
> Branche : `websec` | Repo : [Joan-EYEGHE/edumanage_websec](https://github.com/Joan-EYEGHE/edumanage_websec)

---

## 1. Présentation du projet

EduManage Secure est une application web de gestion de centre de formation (Spring Boot 3.5 + React 19 + PostgreSQL 16). Ce dossier regroupe l'ensemble des livrables DevSecOps produits dans le cadre du module M13 — Cybersécurité applicative.

**Stack technique :**

| Couche | Technologie |
|---|---|
| Backend | Spring Boot 3.5 / Java 21 / JWT HS256 |
| Frontend | React 19 + Vite / axios |
| Base de données | PostgreSQL 16 (Windows natif) |
| Secrets | HashiCorp Vault 1.17.6 (Docker) |
| Infra | NGINX (Docker) / Prometheus + Grafana (Docker) |
| CI/CD | GitHub Actions — branche `websec` |

---

## 2. Architecture de sécurité globale

```
                        ┌─────────────────────────────────┐
                        │        GitHub Actions CI         │
                        │  SonarCloud · Trivy · Gitleaks   │
                        └──────────────┬──────────────────┘
                                       │ push websec
                        ┌──────────────▼──────────────────┐
                        │           NGINX (443)            │
                        │  TLS · CSP · X-Frame · HSTS     │
                        └──────┬───────────────┬──────────┘
                               │               │
               ┌───────────────▼──┐     ┌──────▼──────────────┐
               │  React Frontend  │     │  Spring Boot :8082   │
               │  RBAC côté UI    │     │  Spring Security     │
               │  ProtectedRoute  │     │  @PreAuthorize RBAC  │
               └──────────────────┘     └──────┬──────────────┘
                                               │
                        ┌──────────────────────▼──────────┐
                        │         PostgreSQL 16            │
                        │  BCrypt · audit_logs · JPA       │
                        └─────────────────────────────────┘
                                       │
                        ┌──────────────▼──────────────────┐
                        │      HashiCorp Vault :8200       │
                        │  KV v2 · AppRole · Dynamic DB    │
                        └─────────────────────────────────┘
```

---

## 3. Vue d'ensemble des points DevSecOps

| Point | Intitulé | Statut | Dossier |
|---|---|---|---|
| P2 | CI/CD & SAST Pipeline | ✅ Terminé | [sonar/](./sonar/) |
| P3 | RBAC & JWT | ✅ Terminé | [rbac/](./rbac/) |
| P4 | Tests automatisés Selenium | ✅ Terminé | [selenium/](./selenium/) |
| P5 | HashiCorp Vault | ✅ Terminé | [vault/](./vault/) |
| P6 | OWASP ZAP — DAST | ✅ Terminé | [zap/](./zap/) |
| P9 | Observabilité Microservices | ✅ Terminé | [monitoring/](./monitoring/) |
| P10 | Audit de Sécurité OWASP ASVS | ✅ Terminé | [asvs/](./asvs/) |
| P11 | NGINX Reverse Proxy | ✅ Terminé | [nginx/](./nginx/) |

---

## 4. Détail par point

### Stage 1 — DevSecOps & Pipeline

#### P2 — CI/CD & SAST Pipeline

**Objectif :** automatiser l'analyse de sécurité à chaque push sur `websec`.

**Ce qui a été implémenté :**

- **SonarCloud** : analyse statique (SAST) déclenchée automatiquement via GitHub Actions. 54 issues détectées (52 code smells, 2 rejections de Promise sans objet Error, 1 contexte React recréé à chaque rendu). Aucune issue Critical ou Blocker.
- **Trivy** : scan des dépendances Maven à la recherche de CVE CRITICAL/HIGH. Cache Maven pré-peuplé pour contourner le rate limit de Maven Central.
- **Gitleaks** : détection de secrets exposés dans l'historique Git. Résultat : aucun secret détecté sur la branche `websec` (une clé JWT compromise détectée et purgée avec `git filter-repo` en amont).

**Pipeline GitHub Actions :** 3 jobs parallèles, tous verts depuis le commit `75d2de6`.

**Résultats :** → [sonar/README.md](./sonar/README.md)

---

#### P4 — Tests automatisés Selenium

**Objectif :** valider automatiquement le comportement RBAC frontend dans un vrai navigateur.

**Ce qui a été implémenté :**

3 classes de test JUnit 5 + Selenium WebDriver (Chrome headless) :

| Test | Scénario | Résultat |
|---|---|---|
| `AdminSidebarTest` | Admin voit 6 items de menu | ✅ Pass |
| `ApprenantSidebarTest` | Apprenant voit Dashboard + Formations uniquement | ✅ Pass |
| `ApprenantForbiddenTest` | Apprenant redirigé vers `/forbidden` sur `/paiements` | ✅ Pass |

Les tests sont tagués `@Tag("selenium")` et exclus du pipeline CI (Chrome + frontend incompatibles avec l'environnement headless GitHub Actions). Lancement local : `mvn test -Dgroups=selenium`.

**Résultats :** → [selenium/README.md](./selenium/README.md)

---

#### P6 — OWASP ZAP (DAST)

**Objectif :** tester l'application en cours d'exécution comme le ferait un attaquant réel.

**Ce qui a été implémenté :**

Scan actif authentifié (token JWT injecté via le module Replacer de ZAP). 14 routes explorées. 323 requêtes envoyées.

**Alerte détectée :**

| Alerte | Niveau | Correction appliquée |
|---|---|---|
| Content Security Policy Header Not Set | Medium | ✅ Corrigée en P11 (NGINX) |

Aucune injection SQL, aucune XSS, aucune faille d'authentification détectée.

**Résultats :** → [zap/README.md](./zap/README.md) · [zap/zap-report.html](./zap/zap-report.html)

---

### Stage 2 — IAM & Secrets

#### P3 — RBAC & JWT

**Objectif :** contrôler l'accès aux ressources selon le rôle de l'utilisateur, côté backend et frontend.

**Failles démontrées avant implémentation :**

- É1 : un compte APPRENANT accédait aux journaux d'audit (réponse 200 au lieu de 403)
- É2 : création de compte ADMINISTRATEUR sans authentification (élévation de privilège)

**Ce qui a été implémenté :**

*Backend :*
- `enum Role` avec `Role.Constants` comme source de vérité unique
- `@PreAuthorize` sur les implémentations concrètes (jamais sur les interfaces — CVE-2025-41248/22223)
- Convention `hasAuthority()` sans préfixe `ROLE_`
- `/users/create` fermé aux non-authentifiés
- `TestDataFixtures` avec `@Profile("dev")` pour les comptes de test idempotents

*Frontend :*
- `ProtectedRoute` role-aware consommant `useAuth()`
- Intercepteur axios 401 pour les tokens expirés
- Matrice RBAC sur `AppRouter`

**Périmètre non couvert :** OAuth2 / Keycloak non intégré — JWT maison fonctionnel retenu. L'intégration OAuth2 aurait nécessité de faire cohabiter deux mécanismes d'authentification sur le même backend, avec un risque de régression sur le RBAC existant non acceptable en fin de projet.

**Résultats :** → [rbac/README.md](./rbac/README.md) · [rbac/analyse-rbac.md](./rbac/analyse-rbac.md)

---

#### P5 — HashiCorp Vault

**Objectif :** remplacer la gestion statique des secrets par un coffre-fort centralisé.

**Ce qui a été implémenté :**

| Mécanisme | Description |
|---|---|
| KV v2 | Secrets statiques versionnés (`edumanage/database`, `edumanage/jwt`) |
| AppRole | Authentification machine-à-machine — Spring Boot s'authentifie avec `role_id` + `secret_id` |
| Dynamic secrets | Credentials PostgreSQL temporaires générés à la demande (TTL 1h) |
| Rotation automatique | Renouvellement du mot de passe database toutes les 24h |

Spring Boot lit les secrets depuis Vault au démarrage via `bootstrap.yml` (Spring Cloud 2025.0.3).

**Limites assumées (contexte académique) :**

- Vault en mode `dev` Docker : données en mémoire, perdues au redémarrage (S22)
- `role_id` + `secret_id` en clair dans `bootstrap.yml` gitignor (S25)
- Token root non révoqué après configuration (S23)
- Connexion database engine via utilisateur `postgres` superutilisateur (S27)

**Résultats :** → [vault/README.md](./vault/README.md) · [vault/analyse-vault.md](./vault/analyse-vault.md)

---

### Stage 3 — Infra, Observabilité & Audit

#### P9 — Observabilité Microservices

**Objectif :** monitorer l'application Spring Boot en temps réel.

**Ce qui a été implémenté :**

```
Backend :8082/api/actuator/prometheus
        ↓ scrape toutes les 15s
Prometheus :9091
        ↓
Grafana :3000
```

Métriques exposées via Spring Boot Actuator + Micrometer : `jvm_memory_used_bytes`, `http_server_requests_seconds`, `hikaricp_connections`, `process_cpu_usage`.

**Limites assumées :** Loki (centralisation des logs) et alerting SLA non implémentés — hors scope compte tenu du périmètre retenu.

**Résultats :** → [monitoring/README.md](./monitoring/README.md)

---

#### P10 — Audit OWASP ASVS L1

**Objectif :** évaluer la conformité de l'application aux contrôles de sécurité de base (niveau L1).

**Résultats globaux :**

| Statut | Nombre | % |
|---|---|---|
| ✅ Conforme | 14 | 54% |
| 🟡 Partiel | 8 | 31% |
| ❌ Non conforme | 4 | 15% |

**Points forts :** BCrypt, JPA anti-injection SQL, JWT avec expiration, pipeline CI/CD, NGINX headers, audit logs.

**Failles identifiées :**

| Faille | Gravité | Traitement |
|---|---|---|
| Credentials Cloudinary exposés dans l'historique Git | Haute | ✅ Révoqués et purgés (`git filter-repo`) |
| JWT stocké en localStorage (vulnérable XSS) | Moyenne | Documenté — migration cookie HttpOnly hors scope |
| Pas d'invalidation de token à la déconnexion | Moyenne | Documenté — blacklist Redis hors scope |
| Swagger UI exposé sans authentification | Faible | Documenté — à désactiver en profil `prod` |

**Résultats :** → [asvs/README.md](./asvs/README.md) · [asvs/audit-asvs.md](./asvs/audit-asvs.md)

---

#### P11 — NGINX Reverse Proxy

**Objectif :** point d'entrée unique HTTPS avec headers de sécurité centralisés.

**Ce qui a été implémenté :**

- Reverse proxy HTTPS (certificat auto-signé) — ports internes 8082 et 5173 masqués
- Redirection HTTP → HTTPS (port 80 → 443)
- Headers de sécurité : `Content-Security-Policy`, `X-Frame-Options`, `X-Content-Type-Options`, `Strict-Transport-Security`
- TLS 1.2 / 1.3 uniquement (TLS 1.0 et 1.1 désactivés)
- Correction de l'alerte ZAP Medium (CSP manquant)

**Limites assumées :** ModSecurity (WAF) non implémenté — intégration nécessitant une image Docker dédiée avec risque de casser la configuration NGINX existante, écarté en fin de projet.

**Résultats :** → [nginx/README.md](./nginx/README.md)

---

## 5. Matrice de couverture OWASP Top 10

| OWASP Top 10 (2021) | Couverture | Point DevSecOps |
|---|---|---|
| A01 — Broken Access Control | ✅ Couverte | P3 RBAC backend + frontend |
| A02 — Cryptographic Failures | 🟡 Partielle | P10 ASVS (JWT localStorage documenté) |
| A03 — Injection | ✅ Couverte | JPA/Hibernate natif + P6 ZAP |
| A04 — Insecure Design | 🟡 Partielle | P10 ASVS |
| A05 — Security Misconfiguration | ✅ Couverte | P11 NGINX headers + P2 Gitleaks |
| A06 — Vulnerable Components | ✅ Couverte | P2 Trivy |
| A07 — Auth & Session Failures | 🟡 Partielle | JWT implémenté, invalidation manquante |
| A08 — Software Integrity Failures | ✅ Couverte | P2 Gitleaks + P5 Vault |
| A09 — Logging & Monitoring | ✅ Couverte | P9 Prometheus/Grafana + audit_logs |
| A10 — SSRF | ➖ Non applicable | Architecture sans appels serveur-serveur externes |

---

## 6. Dette technique assumée

| # | Description | Décision |
|---|---|---|
| S10 | `UserFixtures` sans `@Profile` | Risque accepté — projet jamais déployé |
| S15 | Backend retourne HTTP 200 sur login échoué | Documenté — correction hors scope |
| S19 | `application-dev.yml` dans l'historique remote avant purge | Risque accepté — credentials révoqués |
| S22 | Vault mode `dev` — données perdues au redémarrage | Acceptable — contexte académique |
| S23 | Token root Vault non révoqué | Acceptable — contexte académique |
| S25 | `role_id` + `secret_id` en clair dans `bootstrap.yml` gitignor | Gitignor — risque local |
| S27 | Database engine Vault via superutilisateur `postgres` | Documenté — hors scope |

---

## 7. Conclusion

Le projet EduManage Secure couvre l'ensemble du périmètre DevSecOps du module M13 : sécurisation du pipeline CI/CD (P2), contrôle d'accès par rôle backend et frontend (P3/P4), gestion centralisée des secrets (P5), test dynamique d'intrusion (P6), monitoring (P9), audit de conformité (P10) et sécurisation de l'infrastructure réseau (P11).

Les limites assumées (OAuth2, ModSecurity, Loki, Vault persistant) sont documentées et justifiées — elles reflètent des choix de périmètre délibérés dans un contexte académique, non des oublis.
