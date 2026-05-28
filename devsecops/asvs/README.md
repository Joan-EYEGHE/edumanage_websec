# P10 — Audit OWASP ASVS — EduManage Secure

## Qu'est-ce que l'OWASP ASVS ?

L'OWASP ASVS (Application Security Verification Standard) est une norme internationale qui définit les contrôles de sécurité qu'une application web devrait respecter. Elle est organisée en trois niveaux :

- **L1** : contrôles de base, applicables à toute application web
- **L2** : contrôles intermédiaires, pour les applications manipulant des données sensibles
- **L3** : contrôles avancés, pour les applications critiques (banques, santé, défense)

Cet audit cible le **niveau L1**, adapté au contexte académique du projet EduManage Secure.

---

## Périmètre de l'audit

| Élément | Valeur |
|---------|--------|
| Application | EduManage Secure — gestion de centre de formation |
| Stack technique | Spring Boot (Java 21) + React + PostgreSQL + JWT |
| Nombre de contrôles audités | 26 |
| Niveau ASVS ciblé | L1 |
| Auditeur | Joan Archer (DevSecOps) |
| Date | Mai 2026 |

---

## Lien avec les autres points DevSecOps

L'audit ASVS ne repart pas de zéro. Il s'appuie sur les travaux déjà réalisés :

| Contrôle ASVS | Point DevSecOps associé |
|---------------|------------------------|
| Secrets non exposés dans le code | P2 — Gitleaks |
| Dépendances sans vulnérabilités connues | P2 — Trivy |
| Qualité et sécurité du code | P2 — SonarCloud |
| Headers de sécurité HTTP | P11 — NGINX |
| HTTPS activé | P11 — NGINX (certificat auto-signé) |
| TLS 1.0 / 1.1 désactivés | P11 — nginx.conf mis à jour |
| Authentification JWT | Backend Steeve (Spring Security) |
| Scan dynamique des routes | P6 — OWASP ZAP |

---

## Résultats

| Statut | Nombre | Pourcentage |
|--------|--------|-------------|
| ✅ Conforme | 14 | 54% |
| 🟡 Partiel | 8 | 31% |
| ❌ Non conforme | 4 | 15% |
| **Total** | **26** | **100%** |

---

## Points forts identifiés

- **Pipeline CI/CD** (P2) : analyse statique automatisée à chaque push, détection de secrets et de vulnérabilités dans les dépendances
- **BCrypt** : hashage des mots de passe avec algorithme recommandé (`PasswordConfig.java`)
- **JWT avec expiration** : tokens valides 5 heures, clé persistée entre les redémarrages
- **JPA / Hibernate** : protection native contre les injections SQL
- **NGINX** (P11) : HTTPS, headers de sécurité, TLS 1.2/1.3 uniquement
- **Audit logs** : table `audit_logs` active, traçabilité des événements de sécurité

---

## Failles identifiées

### 🔴 Credentials Cloudinary exposés — Gravité Haute
La clé API Cloudinary était commitée en clair dans `application.yaml` sur un repo GitHub public. Découverte pendant l'audit. Clé révoquée et regénérée immédiatement.
**Recommandation** : utiliser des variables d'environnement ou HashiCorp Vault (P5) pour tous les secrets.

### 🟠 JWT stocké en localStorage — Gravité Moyenne
Le token JWT est stocké dans le localStorage du navigateur, accessible par JavaScript. En cas de faille XSS, le token peut être volé.
**Recommandation** : stocker le JWT dans un cookie avec les attributs `HttpOnly` et `Secure`.

### 🟠 Pas d'invalidation de token à la déconnexion — Gravité Moyenne
Architecture JWT stateless — un token reste valide 5 heures après déconnexion explicite.
**Recommandation** : implémenter une blacklist (Redis) ou réduire la durée d'expiration.

### 🟡 Swagger UI exposé en production — Gravité Faible
La documentation complète de l'API est accessible sans authentification.
**Recommandation** : désactiver Swagger UI en profil `prod` (`springdoc.swagger-ui.enabled: false`).

---

## Structure des fichiers

```
devsecops/asvs/
├── audit-asvs.md          ← checklist complète avec justifications
├── screenshots/           ← preuves visuelles
│   ├── bcrypt-config.png
│   ├── gitleaks-job-vert.png
│   ├── trivy-job-vert.png
│   ├── sonarcloud-jobs-verts.png
│   ├── sonarcloud-54-issues.png
│   ├── zap-alerte-medium.png
│   ├── zap-scan-authentifie.png
│   ├── zap-routes-explorees.png
│   ├── nginx-headers-securite.png
│   ├── nginx-tls-connection.png
│   ├── nginx-ssl-protocols.png
│   └── jwt-localstorage.png
└── README.md              ← ce fichier
```
