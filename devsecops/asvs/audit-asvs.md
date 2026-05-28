# Audit OWASP ASVS L1 — EduManage Secure

> Réalisé par : Joan Archer (DevSecOps)
> Date : Mai 2026
> Niveau cible : ASVS L1 (contrôles de base)
> Application : EduManage Secure — Spring Boot + React + PostgreSQL + JWT

---

## Légende

| Symbole | Signification |
|---------|---------------|
| ✅ | Conforme — preuve disponible |
| 🟡 | Partiel — mesure incomplète ou non totalement vérifiable |
| ❌ | Non conforme — faille identifiée |
| N/A | Non applicable au contexte du projet |

---

## V1 — Architecture & Design

| ID | Contrôle ASVS L1 | Statut | Justification | Preuve |
|----|------------------|--------|---------------|--------|
| V1.1.1 | Le cycle de développement intègre des pratiques de sécurité (analyse statique, revue de code) | ✅ Conforme | Pipeline CI/CD P2 — SonarCloud SAST actif sur chaque push, 54 issues détectées et documentées | `screenshots/sonarcloud-jobs-verts.png` |
| V1.1.2 | Les menaces sont identifiées et documentées | 🟡 Partiel | P6 — ZAP identifie 1 alerte Medium (CSP Header Not Set, corrigée en P11). Pas de modèle de menaces formel — acceptable au niveau L1 | `screenshots/zap-alerte-medium.png` |
| V1.9.1 | Les communications entre composants sont chiffrées | ✅ Conforme | P11 — NGINX HTTPS actif avec certificat auto-signé. Toutes les requêtes transitent via TLS | `screenshots/nginx-tls-connection.png` |

---

## V2 — Authentification

| ID | Contrôle ASVS L1 | Statut | Justification | Preuve |
|----|------------------|--------|---------------|--------|
| V2.1.1 | Longueur minimale de mot de passe (12 caractères recommandés) | 🟡 Partiel | Aucune contrainte de longueur explicite trouvée dans les DTOs ou le code de validation. BCrypt est utilisé pour le hashage, mais la politique de mot de passe n'est pas enforced côté serveur | — |
| V2.2.1 | Les contrôles d'authentification sont effectués côté serveur | ✅ Conforme | Spring Boot — JWT validé côté serveur via `JwtAuthFilter`. Toutes les routes protégées passent par ce filtre avant d'atteindre les controllers | `screenshots/sonarcloud-jobs-verts.png` |
| V2.2.2 | Pas d'identifiants par défaut (admin/admin, etc.) | ✅ Conforme | P6 — scan ZAP authentifié avec identifiants custom. Aucun compte par défaut détecté | `screenshots/zap-scan-authentifie.png` |
| V2.8.1 | Les tokens d'authentification ont une durée de vie limitée | ✅ Conforme | JWT configuré avec expiration de 18 000 000 ms (5 heures) dans `TokenServiceImpl.java`. Clé persistée dans un fichier, non regénérée à chaque démarrage | — |

---

## V3 — Gestion de session

| ID | Contrôle ASVS L1 | Statut | Justification | Preuve |
|----|------------------|--------|---------------|--------|
| V3.2.1 | Les tokens de session sont générés côté serveur et non prévisibles | ✅ Conforme | JWT signé avec clé HS256 persistée dans `TokenServiceImpl`. Clé non exposée, non régénérée aléatoirement à chaque démarrage | — |
| V3.2.3 | Les tokens sont invalidés côté serveur à la déconnexion | ❌ Non conforme | Architecture JWT stateless — aucune blacklist de tokens implémentée. Un token reste valide jusqu'à expiration même après déconnexion. Point faible connu de l'architecture JWT sans état | — |
| V3.4.1 | Les cookies de session utilisent l'attribut `HttpOnly` | ❌ Non conforme | Le JWT est stocké dans le `localStorage` du navigateur, pas dans un cookie HttpOnly. Accessible par JavaScript — exposition au risque XSS | `screenshots/jwt-localstorage.png` |
| V3.4.2 | Les cookies de session utilisent l'attribut `Secure` | ❌ Non conforme | Même raison que V3.4.1 — stockage localStorage, pas de cookie Secure | `screenshots/jwt-localstorage.png` |

---

## V5 — Validation des entrées

| ID | Contrôle ASVS L1 | Statut | Justification | Preuve |
|----|------------------|--------|---------------|--------|
| V5.1.1 | Les paramètres HTTP ne sont pas utilisés directement sans validation | 🟡 Partiel | P2 — SonarCloud a détecté 54 issues incluant des problèmes de qualité de code. Validation présente dans certains controllers mais non systématique | `screenshots/sonarcloud-54-issues.png` |
| V5.1.3 | Les données structurées (JSON) sont validées par schéma | 🟡 Partiel | Spring Boot utilise les annotations `@Valid` sur certains DTOs — validation partielle, non vérifiée sur l'ensemble des endpoints | — |
| V5.3.4 | Pas d'injection SQL possible | ✅ Conforme | Spring Data JPA / Hibernate utilisé sur l'ensemble du projet — toutes les requêtes sont paramétrées par défaut. Pas de requêtes SQL natives non sécurisées identifiées | — |

---

## V6 — Cryptographie

| ID | Contrôle ASVS L1 | Statut | Justification | Preuve |
|----|------------------|--------|---------------|--------|
| V6.2.1 | Les modules cryptographiques échouent de manière sécurisée | ✅ Conforme | JWT — un échec de validation (token expiré, signature invalide) provoque le rejet immédiat de la requête avec HTTP 401. Comportement géré par `JwtAuthFilter` | — |
| V6.2.2 | Algorithmes cryptographiques non obsolètes pour le hashage des mots de passe | ✅ Conforme | `PasswordConfig.java` — `BCryptPasswordEncoder` configuré comme bean Spring. BCrypt est l'algorithme recommandé pour le hashage de mots de passe | `screenshots/bcrypt-config.png` |

---

## V7 — Gestion des erreurs et logs

| ID | Contrôle ASVS L1 | Statut | Justification | Preuve |
|----|------------------|--------|---------------|--------|
| V7.1.1 | Pas d'informations sensibles dans les logs (mots de passe, tokens) | 🟡 Partiel | P2 — Gitleaks vérifie les secrets dans le code source. Le contenu des logs runtime n'a pas été audité exhaustivement | `screenshots/gitleaks-job-vert.png` |
| V7.1.2 | Les logs contiennent les événements de sécurité (connexions, échecs d'auth) | ✅ Conforme | Table `audit_logs` identifiée lors du scan ZAP P6 — endpoint `/api/audit-logs/` accessible et actif | `screenshots/zap-routes-explorees.png` |
| V7.4.1 | Les messages d'erreur ne révèlent pas d'informations internes | 🟡 Partiel | ZAP n'a pas signalé de fuite d'information dans les réponses d'erreur. Non testé exhaustivement sur tous les endpoints | — |

---

## V9 — Communications

| ID | Contrôle ASVS L1 | Statut | Justification | Preuve |
|----|------------------|--------|---------------|--------|
| V9.1.1 | TLS utilisé pour toutes les communications | ✅ Conforme | P11 — NGINX HTTPS actif sur port 443. HTTP non exposé | `screenshots/nginx-tls-connection.png` |
| V9.1.2 | Versions TLS obsolètes désactivées (TLS 1.0 et 1.1 exclus) | ✅ Conforme | `nginx.conf` mis à jour — `ssl_protocols TLSv1.2 TLSv1.3` configuré explicitement. TLS 1.0 et 1.1 désactivés | `screenshots/nginx-ssl-protocols.png` |
| V9.2.1 | Les headers de sécurité HTTP sont présents | ✅ Conforme | P11 — NGINX ajoute sur toutes les réponses : `Content-Security-Policy`, `X-Frame-Options`, `X-Content-Type-Options` | `screenshots/nginx-headers-securite.png` |

---

## V10 — Code malveillant

| ID | Contrôle ASVS L1 | Statut | Justification | Preuve |
|----|------------------|--------|---------------|--------|
| V10.2.1 | Le code source ne contient pas de backdoors ou credentials hardcodés | ❌ Non conforme | **Faille identifiée** : `application.yaml` contient `api-secret: RGqqismZtqoHpZ4i6FJ_WamEUfc` (clé Cloudinary) commité sur le repo GitHub public `snguem/edumanage`. De plus, Gitleaks ne scanne pas la branche frontend React. Clé révoquée suite à l'audit. | — |
| V10.3.2 | Les dépendances sont à jour et sans vulnérabilités connues | ✅ Conforme | P2 — Trivy scan actif sur `pom.xml` à chaque push sur la branche `websec` | `screenshots/trivy-job-vert.png` |

---

## V14 — Configuration

| ID | Contrôle ASVS L1 | Statut | Justification | Preuve |
|----|------------------|--------|---------------|--------|
| V14.2.1 | Les composants non utilisés sont désactivés | 🟡 Partiel | Spring Boot auto-configure de nombreux composants. Swagger UI activé en production (`springdoc.swagger-ui.enabled: true`) — expose la documentation API publiquement | — |
| V14.4.1 | Les headers HTTP de sécurité sont configurés sur toutes les réponses | ✅ Conforme | P11 — NGINX applique les headers de sécurité sur l'ensemble des réponses HTTP | `screenshots/nginx-headers-securite.png` |
| V14.4.6 | Le header `Content-Security-Policy` est présent et configuré | ✅ Conforme | P11 — CSP configuré dans NGINX après l'alerte ZAP P6 (CSP Header Not Set). Alerte corrigée. | `screenshots/nginx-headers-securite.png` |

---

## Résumé global

| Statut | Nombre | Pourcentage |
|--------|--------|-------------|
| ✅ Conforme | 14 | 54% |
| 🟡 Partiel | 8 | 31% |
| ❌ Non conforme | 4 | 15% |
| **Total** | **26** | **100%** |

---

## Failles identifiées — synthèse

### ❌ Faille 1 — Credentials Cloudinary exposés (V10.2.1)
**Gravité : Haute**
La clé API Cloudinary (`api-secret`) était commitée en clair dans `application.yaml` sur un repo GitHub public. Toute personne ayant accès au repo pouvait lire et utiliser ces credentials.
**Action corrective** : clé révoquée et regénérée sur Cloudinary suite à l'audit. Le fichier `application.yaml` doit être ajouté au `.gitignore` ou les secrets doivent être gérés via des variables d'environnement (ex. HashiCorp Vault — P5).

### ❌ Faille 2 — JWT stocké en localStorage (V3.4.1 / V3.4.2)
**Gravité : Moyenne**
Le token JWT est stocké dans le `localStorage` du navigateur. En cas de faille XSS, un attaquant peut lire ce token et usurper l'identité de l'utilisateur.
**Recommandation** : stocker le JWT dans un cookie avec les attributs `HttpOnly` et `Secure`.

### ❌ Faille 3 — Invalidation de token à la déconnexion (V3.2.3)
**Gravité : Moyenne**
Architecture JWT stateless sans blacklist — un token reste valide jusqu'à expiration (5h) même après déconnexion explicite.
**Recommandation** : implémenter une blacklist en mémoire (Redis) ou réduire la durée d'expiration.

### ❌ Faille 4 — Swagger UI exposé (V14.2.1)
**Gravité : Faible**
`springdoc.swagger-ui.enabled: true` dans `application.properties` expose la documentation complète de l'API en production. Un attaquant peut cartographier tous les endpoints sans authentification.
**Recommandation** : désactiver Swagger UI en profil `prod`.
