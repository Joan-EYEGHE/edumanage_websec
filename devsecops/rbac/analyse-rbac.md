# Audit RBAC / JWT — EduManage Secure (Point P3)

> **Auteur** : Joan Eyeghe — **Date** : 16/06/2026 — **Branche** : `websec`
> **Périmètre** : audit en **lecture seule** du backend Spring Boot (`src/main/java/**`).
> Aucun fichier du code applicatif n'a été modifié pour produire ce document.

---

## 1. Contexte & méthode (QQOQCP)

| Question | Réponse |
|---|---|
| **Quoi ?** | Vérifier si le contrôle d'accès basé sur les rôles (RBAC) est réellement appliqué sur les routes de l'API. |
| **Qui ?** | Les utilisateurs de EduManage, censés avoir des rôles différents (ADMIN, GESTIONNAIRE, FORMATEUR, APPRENANT). |
| **Où ?** | Backend Spring Boot, package `ism.dakar.edumanage`, port `8082`, préfixe global `/api`. |
| **Quand ?** | À chaque requête HTTP, lors de la phase d'autorisation de Spring Security. |
| **Comment ?** | Lecture exhaustive de `SecurityConfig`, des controllers, du filtre JWT et de la couche service. |
| **Pourquoi ?** | Un RBAC déclaré « sur le papier » mais non appliqué = faille **Broken Access Control** (OWASP A01:2021), la faille n°1 du Top 10 OWASP. |

**Définitions rapides (pour le rapport) :**
- **RBAC** (*Role-Based Access Control*) : restreindre chaque action selon le rôle de l'utilisateur.
- **Enforced / appliqué** : la règle est vérifiée *à l'exécution*. Un rôle stocké en base mais jamais testé n'est **pas** appliqué.
- **Authority / autorité** : la représentation interne d'un droit dans Spring Security (ex. `ADMINISTRATEUR`).

---

## 2. Verdict global

> ⚠️ **Le RBAC n'est PAS appliqué.** Les rôles existent en base de données mais ne sont
> vérifiés sur **aucune** route. Tout utilisateur **authentifié** peut appeler **n'importe quelle**
> route (créer/supprimer des utilisateurs, lister les paiements, consulter les journaux d'audit),
> quel que soit son rôle.

Preuve factuelle : recherche exhaustive sur tout `src/main/java` →
**zéro** occurrence de `@PreAuthorize`, `@Secured`, `@RolesAllowed`, `hasRole`, `hasAuthority`.

---

## 3. Matrice des rôles — Définition vs Application

Les rôles sont des **codes d'accès** (`AccesEntity`) seedés en base, et non une `enum` typée.
Source du seed : [`AccessHelperService.java:47-50`](../../src/main/java/ism/dakar/edumanage/security/helpers/AccessHelperService.java#L47-L50).

| Rôle (code) | Annoncé dans le projet | Existe en base (seedé) | Porté par un compte seedé | Vérifié sur une route ? |
|---|:---:|:---:|:---:|:---:|
| `ADMINISTRATEUR` | ~ (annoncé sous le nom `ADMIN`) | ✅ | ✅ 3 comptes | ❌ |
| `GESTIONNAIRE` | ✅ | ✅ | ❌ aucun | ❌ |
| `FORMATEUR` | ✅ | ✅ | ❌ aucun | ❌ |
| `APPRENANT` | ✅ | ✅ | ❌ aucun | ❌ |

**Constat** : il y a **3 désalignements** :
1. **Nom** : le rôle annoncé `ADMIN` s'appelle en réalité `ADMINISTRATEUR` en base.
2. **Peuplement** : seul `ADMINISTRATEUR` est porté par des comptes (les 3 fixtures de démo).
   Aucun compte `GESTIONNAIRE`/`FORMATEUR`/`APPRENANT` n'est créé par défaut.
   → réf. [`UserFixtures.java:40,51,62`](../../src/main/java/ism/dakar/edumanage/datas/fixtures/UserFixtures.java#L40)
3. **Application** : aucun rôle n'est **vérifié** au moment de servir une requête (le cœur du problème).

> Les rôles étant de simples chaînes stockées en base (pas une `enum`), une faute de frappe
> dans une future annotation (`@PreAuthorize("hasAuthority('ADMINISTATEUR')")`) ne lèverait
> aucune erreur de compilation et créerait un trou de sécurité silencieux.

---

## 4. Matrice Routes × Protection réelle

Préfixe global : `/api`. Protection lue dans
[`SecurityConfig.java:35-46`](../../src/main/java/ism/dakar/edumanage/security/configs/SecurityConfig.java#L35-L46).

| Route | Méthode | Protection actuelle | Protégé par annotation ? | Rôle attendu (théorie) |
|---|---|---|:---:|---|
| `/api/users/login` | POST | **Public** | ❌ | Tous |
| `/api/users/create` | POST | **Public** ⚠️ | ❌ | ADMIN uniquement |
| `/api/users/me` | GET | Authentifié | ❌ | Tout connecté |
| `/api/users/all` `/all-list` `/count-all` `/{id}/get` | GET | Authentifié, **aucun rôle** | ❌ | ADMIN |
| `/api/users/{id}/update` `/{id}/delete` | PUT/DELETE | Authentifié, **aucun rôle** | ❌ | ADMIN |
| `/api/formations/**` | CRUD | Authentifié, **aucun rôle** | ❌ | FORMATEUR/ADMIN (écriture) |
| `/api/inscriptions/**` | CRUD | Authentifié, **aucun rôle** | ❌ | GESTIONNAIRE/ADMIN |
| `/api/paiements/**` | CRUD | Authentifié, **aucun rôle** | ❌ | GESTIONNAIRE/ADMIN |
| `/api/audit-logs/**` | GET | Authentifié, **aucun rôle** | ❌ | ADMIN uniquement |

> La colonne « Protégé par annotation ? » est **NON partout** : c'est le cœur du problème.

---

## 5. Écarts identifiés (du plus grave au moins grave)

### É1 — RBAC totalement absent à l'exécution *(critique)*
Aucune route ne distingue les rôles. `SecurityConfig` s'arrête à `.anyRequest().authenticated()`
([ligne 46](../../src/main/java/ism/dakar/edumanage/security/configs/SecurityConfig.java#L46)).
→ **Broken Access Control (OWASP A01:2021)**.

### É2 — Création de compte ADMIN non authentifiée *(critique, confirmé)*
`/api/users/create` est `permitAll` ([`SecurityConfig.java:41`](../../src/main/java/ism/dakar/edumanage/security/configs/SecurityConfig.java#L41)),
et `UserServiceImpl.create` applique **les rôles fournis par le client** sans contrôle
([`UserServiceImpl.java:72-76`](../../src/main/java/ism/dakar/edumanage/services/impls/UserServiceImpl.java#L72-L76)) :

```java
if (!dto.getRoles().isEmpty()){
    dto.getRoles().forEach(code -> {
        entity.getAccess().add(accessRepository.findByCode(code)); // rôle client appliqué tel quel
    });
}
```
→ **N'importe qui, sans token, peut se créer un compte `ADMINISTRATEUR`.**
Élévation de privilège verticale (le test D du README le démontre).

### É3 — Rôles définis comme données, non typés *(moyen)*
Les 4 rôles existent en base (seedés) mais sous forme de **chaînes de caractères**, pas d'`enum`.
Le nom annoncé `ADMIN` est en réalité `ADMINISTRATEUR`, et aucun compte non-admin n'existe par défaut.
→ risque de faute de frappe silencieuse dans les futures annotations ; difficile d'écrire un RBAC fiable
tant que les rôles ne sont pas figés dans le code (idéalement une `enum`).

### É4 — Authorities sans préfixe `ROLE_` *(piège technique)*
[`UserAuthenticationProvider.java:53-55`](../../src/main/java/ism/dakar/edumanage/security/configs/UserAuthenticationProvider.java#L53-L55)
crée `new SimpleGrantedAuthority(role)` sans préfixe. Conséquence : `hasRole('ADMINISTRATEUR')`
**échouera silencieusement** (il cherche `ROLE_ADMINISTRATEUR`). Il faudra `hasAuthority('ADMINISTRATEUR')`,
ou normaliser les rôles avec le préfixe `ROLE_`.

### É5 — Faiblesses JWT *(moyen — recoupe le point P10)*
- Le token ne contient **pas** de claim de rôle ([`UserAuthenticationProvider.java:31-43`](../../src/main/java/ism/dakar/edumanage/security/configs/UserAuthenticationProvider.java#L31-L43)).
  *Note : recharger les rôles depuis la base à chaque requête est en soi une bonne chose pour la révocation.*
- Durée de vie = 5 h (`18000000` ms) — longue.
- Aucune liste de révocation (denylist) → un logout ne peut pas invalider le token côté serveur.
- Clé HS256 stockée dans `public/token/token.json` ([`TokenServiceImpl.java:24`](../../src/main/java/ism/dakar/edumanage/security/services/impls/TokenServiceImpl.java#L24)) — nom « public » à vérifier (ne doit jamais être exposé statiquement).
- `@CrossOrigin("*")` sur tous les controllers — CORS ouvert à toute origine.

---

## 6. Recommandations sur les 2 failles JWT du point P10

### Faille P10-1 — JWT stocké dans `localStorage` (frontend React)
**Problème** : `localStorage` est lisible par tout JavaScript de la page. Une faille XSS permet
de voler le token. OWASP : *« Do not store session identifiers in local storage as the data is
always accessible by JavaScript. »*

**Recommandation 2026** :
- Stocker le token dans un **cookie `HttpOnly` + `Secure` + `SameSite=Strict`** (inaccessible au JS),
  accompagné d'une **protection anti-CSRF** (le cookie étant envoyé automatiquement).
- Variante recommandée : **access token court** (≈ 15 min) + **refresh token** long en cookie `HttpOnly`.

### Faille P10-2 — Pas d'invalidation au logout
**Problème** : un JWT signé reste valide jusqu'à expiration (ici 5 h), même après un « logout ».
Le logout actuel ne fait que supprimer le token côté client ; le token reste accepté par le serveur.

**Recommandation 2026** :
- Mettre en place une **denylist serveur** : à chaque logout, stocker un **digest SHA-256 du token**
  + sa date de révocation ; le filtre JWT rejette tout token présent dans la denylist.
- Réduire la durée de vie de l'access token et s'appuyer sur le refresh token pour le renouvellement.

---

## 7. Proposition de correctif RBAC (NON appliquée — pour décision de Joan/Steeve)

> ⚠️ Ceci est une **proposition** illustrative. Aucune modification n'a été faite dans `src/**`.
> La méthode de sécurité par annotations (`@EnableMethodSecurity`) est déjà activée
> ([`SecurityConfig.java:19`](../../src/main/java/ism/dakar/edumanage/security/configs/SecurityConfig.java#L19)),
> il « suffit » donc d'ajouter les annotations.

**Option A — protéger les routes via annotations sur les controllers** (rapide à poser) :

```diff
// Exemple sur AuditLogRestControllerImpl (journaux = ADMIN uniquement)
+ import org.springframework.security.access.prepost.PreAuthorize;

+ @PreAuthorize("hasAuthority('ADMINISTRATEUR')")   // hasAuthority car pas de préfixe ROLE_
  public Response<Object> getAll(Map<String,String> searchParams, Pageable pageable) { ... }
```

**Option B — fermer la création de compte ouverte (faille É2)** :

```diff
// SecurityConfig.java — retirer /users/create des routes publiques
  .requestMatchers(
      "/swagger-ui/**", "/swagger-ui.html/**", "/v1/api-docs/**",
      "/users/login",
-     "/users/create",
      "/actuator/prometheus", "/actuator/health"
  ).permitAll()
```
+ côté `UserServiceImpl.create`, ignorer le champ `roles` venant du client pour toute création
  non effectuée par un ADMINISTRATEUR (forcer un rôle par défaut, ex. `APPRENANT`).

**Pré-requis indispensable** : figer les rôles (idéalement une `enum`) et choisir la convention
(`hasAuthority('ADMINISTRATEUR')` *ou* normaliser en `ROLE_…` + `hasRole`).

---

## 8. Sources (bonnes pratiques 2026)

**Spring Security — Method Security & rôles :**
- [Method Security — documentation officielle Spring Security](https://docs.enterprise.spring.io/spring-security/reference/servlet/authorization/method-security.html)
- [Introduction to Spring Method Security — Baeldung](https://www.baeldung.com/spring-security-method-security)
- [Difference Between hasRole() and hasAuthority() — GeeksforGeeks](https://www.geeksforgeeks.org/advance-java/difference-between-hasrole-and-hasauthority-in-spring-security/)
- [Spring Method Security with @PreAuthorize — Okta Developer](https://developer.okta.com/blog/2019/06/20/spring-preauthorize)

**OWASP — Stockage & révocation des JWT :**
- [JSON Web Token for Java Cheat Sheet — OWASP](https://cheatsheetseries.owasp.org/cheatsheets/JSON_Web_Token_for_Java_Cheat_Sheet.html)
- [LocalStorage vs Cookies : storing JWT securely — Cyber Chief](https://www.cyberchief.ai/2023/05/secure-jwt-token-storage.html)
