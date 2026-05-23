# P6 — Scan DAST avec OWASP ZAP

## Qu'est-ce que OWASP ZAP ?

OWASP ZAP (Zed Attack Proxy) est un outil open source de test de sécurité dynamique (DAST).
Contrairement à SonarCloud qui analyse le code source sans l'exécuter, ZAP attaque
l'application en cours d'exécution comme le ferait un attaquant réel.
Il envoie des requêtes malformées, teste les injections, vérifie les headers de sécurité, etc.

## Environnement de test

| Élément        | Valeur                          |
|----------------|---------------------------------|
| Application    | EduManage Secure (Spring Boot)  |
| URL cible      | http://localhost:8080/api       |
| Version ZAP    | 2.x                             |
| Date du scan   | 23 mai 2026                     |
| Type de scan   | Scan actif authentifié          |

## Méthode utilisée

### 1. Authentification
L'API EduManage est protégée par JWT. Un token a été obtenu via la route publique
`POST /api/users/login` avec un compte administrateur.

Le token a été configuré dans le module **Replacer** de ZAP :
- Type : Nom de l'en-tête de la requête
- En-tête : `Authorization`
- Valeur injectée : `Bearer <token>`

Cela permet à ZAP d'accéder aux routes protégées pendant le scan.

### 2. Exploration des routes
Les routes de l'API ont été explorées manuellement via le Requêteur ZAP :

| Route                        | Méthode | Code obtenu |
|------------------------------|---------|-------------|
| /api/users/login             | POST    | 200         |
| /api/users/me                | GET     | 200         |
| /api/users/all               | GET     | 200         |
| /api/users/all-list          | GET     | 200         |
| /api/users/count-all         | GET     | 200         |
| /api/formations/all          | GET     | 200         |
| /api/formations/all-list     | GET     | 200         |
| /api/formations/count-all    | GET     | 200         |
| /api/paiements/all           | GET     | 200         |
| /api/paiements/all-list      | GET     | 200         |
| /api/inscriptions/all        | GET     | 200         |
| /api/inscriptions/all-list   | GET     | 200         |
| /api/audit-logs/all          | GET     | 200         |
| /api/audit-logs/all-list     | GET     | 200         |

### 3. Scan actif
Le scan actif a été lancé sur `http://localhost:8080`.
ZAP a envoyé **323 requêtes** au total en testant les injections,
les manipulations de paramètres et les headers de sécurité.

## Résultats

### Alertes détectées

| Alerte | Niveau | Confiance | Routes concernées |
|--------|--------|-----------|-------------------|
| Content Security Policy (CSP) Header Not Set | Medium | High | /api/users/me |

### Interprétation

**Content Security Policy (CSP) Header Not Set — Medium**

Le backend Spring Boot ne renvoie pas le header HTTP `Content-Security-Policy`
dans ses réponses. Ce header indique au navigateur quelles sources de contenu
(scripts, images, styles) sont autorisées à se charger.

Sans ce header, un attaquant qui réussit à injecter du JavaScript malveillant
dans une page (attaque XSS) peut exécuter n'importe quel script.
Le navigateur n'a aucune règle pour bloquer ce contenu.

**Correction recommandée :** ajouter la configuration suivante dans Spring Security :

```java
http.headers(headers -> headers
    .contentSecurityPolicy(csp -> csp
        .policyDirectives("default-src 'self'")
    )
);
```

## Limites du scan

ZAP détecte les vulnérabilités techniques visibles dans les échanges HTTP.
Il ne détecte pas :
- Les failles logiques (ex : un utilisateur accédant aux données d'un autre)
- Les problèmes de gestion des rôles (RBAC)
- Les failles dans la logique métier

Ces aspects nécessitent des tests manuels ou des tests fonctionnels automatisés (Selenium).

## Fichiers produits

| Fichier | Description |
|---------|-------------|
| `zap-report.html` | Rapport complet généré par ZAP |
| `README.md` | Ce fichier — explication et interprétation |
| `screenshots/` | Captures d'écran de la session ZAP |