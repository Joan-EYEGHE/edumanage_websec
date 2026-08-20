# Audit RBAC / JWT — EduManage Secure (Point P3)

> **Auteur** : Joan Eyeghe — **Date initiale** : 16/06/2026 — **Mise à jour** : 20/08/2026
> **Branche** : `websec`
> **Périmètre** : audit backend Spring Boot (`src/main/java/**`) + frontend React Vite (`frontend/src/**`).

---

## 1. Contexte & méthode (QQOQCP)

| Question | Réponse |
|---|---|
| **Quoi ?** | Vérifier si le contrôle d'accès basé sur les rôles (RBAC) est réellement appliqué sur les routes de l'API et les pages du frontend. |
| **Qui ?** | Les utilisateurs de EduManage, avec 4 rôles distincts : ADMINISTRATEUR, GESTIONNAIRE, FORMATEUR, APPRENANT. |
| **Où ?** | Backend Spring Boot (port `8082`, préfixe global `/api`) + frontend React Vite (port `5173`). |
| **Quand ?** | À chaque requête HTTP (backend) et à chaque navigation (frontend), lors des phases d'autorisation. |
| **Comment ?** | Audit initial en lecture seule (16/06/2026), puis application des correctifs en Sessions 1–4 (juillet–août 2026). |
| **Pourquoi ?** | Un RBAC déclaré « sur le papier » mais non appliqué = faille **Broken Access Control** (OWASP A01:2021), la faille n°1 du Top 10 OWASP. |

**Définitions rapides :**
- **RBAC** (*Role-Based Access Control*) : restreindre chaque action selon le rôle de l'utilisateur.
- **Enforced / appliqué** : la règle est vérifiée *à l'exécution*. Un rôle stocké en base mais jamais testé n'est **pas** appliqué.
- **Authority / autorité** : la représentation interne d'un droit dans Spring Security (ex. `ADMINISTRATEUR`).

---

## 2. Verdict global

> ✅ **Le RBAC est appliqué — backend et frontend.**
>
> À l'issue des Sessions 1 à 4, le RBAC est opérationnel sur l'ensemble du périmètre :
> - **Backend** : toutes les routes sensibles sont protégées par `@PreAuthorize` sur les implémentations concrètes.
> - **Frontend** : toutes les pages sont protégées par `ProtectedRoute` avec contrôle de rôle ; la sidebar est filtrée par rôle.
> - **Tests** : 10/10 tests RBAC backend validés ; 3 scénarios UI validés (admin, apprenant, page forbidden).

**Situation initiale (16/06/2026) :** zéro occurrence de `@PreAuthorize`, `@Secured`, `hasRole`, `hasAuthority` dans `src/main/java`. Tout utilisateur authentifié pouvait appeler n'importe quelle route.

**Situation actuelle (20/08/2026) :** RBAC appliqué sur toutes les routes et pages du périmètre défini.

---

## 3. Matrice des rôles — Définition vs Application

| Rôle | Existe (enum) | Comptes test | Vérifié backend | Vérifié frontend |
|---|:---:|:---:|:---:|:---:|
| `ADMINISTRATEUR` | ✅ | ✅ (UserFixtures) | ✅ | ✅ |
| `GESTIONNAIRE` | ✅ | ✅ (TestDataFixtures, profil dev) | ✅ | ✅ |
| `FORMATEUR` | ✅ | ✅ (TestDataFixtures, profil dev) | ✅ | ✅ |
| `APPRENANT` | ✅ | ✅ (TestDataFixtures, profil dev) | ✅ | ✅ |

**Correctifs appliqués sur la définition des rôles (Session 1) :**
- Création de `enum Role` avec `Role.Constants` — les rôles sont désormais figés dans le code, plus de risque de faute de frappe silencieuse.
- Convention retenue : `hasAuthority()` (pas `hasRole()`) — les autorités Spring Security ne portent pas de préfixe `ROLE_`.
- Commit : `5d92c9a`

---

## 4. Matrice Routes × Protection réelle

Préfixe global : `/api`.

### Backend

| Route | Méthode | Protection | Rôles autorisés |
|---|---|---|---|
| `/api/users/login` | POST | Public | Tous |
| `/api/users/create` | POST | Authentifié + rôle | ADMINISTRATEUR uniquement |
| `/api/users/me` | GET | Authentifié | Tout connecté |
| `/api/users/all`, `/all-list`, `/count-all`, `/{id}/get` | GET | Authentifié + rôle | ADMINISTRATEUR |
| `/api/users/{id}/update`, `/{id}/delete` | PUT/DELETE | Authentifié + rôle | ADMINISTRATEUR |
| `/api/formations/**` | CRUD | Authentifié + rôle | ADMINISTRATEUR, GESTIONNAIRE, FORMATEUR (écriture) ; APPRENANT (lecture) |
| `/api/inscriptions/**` | CRUD | Authentifié + rôle | ADMINISTRATEUR, GESTIONNAIRE, FORMATEUR |
| `/api/paiements/**` | CRUD | Authentifié + rôle | ADMINISTRATEUR, GESTIONNAIRE |
| `/api/audit-logs/**` | GET | Authentifié + rôle | ADMINISTRATEUR |

### Frontend

| Route | `allowedRoles` | Comportement si non autorisé |
|---|---|---|
| `/dashboard` | ADMINISTRATEUR, GESTIONNAIRE, FORMATEUR, APPRENANT | — |
| `/users` | ADMINISTRATEUR | Redirection `/forbidden` |
| `/formations` | ADMINISTRATEUR, GESTIONNAIRE, FORMATEUR, APPRENANT | — |
| `/inscriptions` | ADMINISTRATEUR, GESTIONNAIRE, FORMATEUR | Redirection `/forbidden` |
| `/paiements` | ADMINISTRATEUR, GESTIONNAIRE | Redirection `/forbidden` |
| `/audit-logs` | ADMINISTRATEUR | Redirection `/forbidden` |
| `/forbidden` | Tout authentifié | — |

### Sidebar (cohérente avec AppRouter)

| Item | Visible pour |
|---|---|
| Dashboard | Tous |
| Utilisateurs | ADMINISTRATEUR |
| Formations | Tous |
| Inscriptions | ADMINISTRATEUR, GESTIONNAIRE, FORMATEUR |
| Paiements | ADMINISTRATEUR, GESTIONNAIRE |
| Audit Logs | ADMINISTRATEUR |

---

## 5. Écarts initiaux — Statut après correctifs

### É1 — RBAC totalement absent à l'exécution *(critique)*
**Statut : ✅ RÉSOLU — Sessions 3**

`@PreAuthorize` appliqué sur toutes les implémentations concrètes des controllers.
Convention : `hasAuthority()` sur les implémentations (jamais sur les interfaces — best practice confirmée par CVE-2025-41248/22223).
Commit : `6c2367b`

### É2 — Création de compte non authentifiée *(critique)*
**Statut : ✅ RÉSOLU — Session 3**

`/users/create` retiré de `permitAll` et restreint à `ADMINISTRATEUR` via `@PreAuthorize`.
Commit : `6c2367b`

> **Note dette résiduelle S12** : `UserServiceImpl.create()` applique encore les rôles fournis par le client sans contrôle ABAC. Ce point est volontairement hors scope (projet académique, RBAC pur retenu — décision D1). Documenté comme dette S12.

### É3 — Rôles définis comme données, non typés *(moyen)*
**Statut : ✅ RÉSOLU — Session 1**

`enum Role` créée avec `Role.Constants` pour utilisation dans les annotations.
Commit : `5d92c9a`

### É4 — Authorities sans préfixe `ROLE_` *(piège technique)*
**Statut : ✅ GÉRÉ — Convention appliquée**

`UserAuthenticationProvider.validateToken()` charge les autorités depuis la base sans préfixe `ROLE_`.
Convention retenue en cohérence : `hasAuthority('ADMINISTRATEUR')` partout. Aucune occurrence de `hasRole()` dans le code.

### É5 — Faiblesses JWT *(moyen)*
**Statut : ⚠️ PARTIELLEMENT TRAITÉ**

- Clé JWT compromise (ancienne clé dans `public/token/token.json`) → ✅ purgée via `git filter-repo`, nouvelle clé dans `application-dev.yml` / `application-prod.yml` (gitignorés).
- Durée de vie 5h → ⚠️ inchangée (hors scope, projet académique).
- Pas de denylist logout → ⚠️ inchangé (hors scope).
- `localStorage` pour le token → ⚠️ inchangé (décision D4 — migration cookie HttpOnly hors scope).

---

## 6. Correctifs appliqués — Sessions 1 à 4

### Session 1 — Source de vérité des rôles
- Création `enum Role` + `Role.Constants` + `RoleConstantsTest`
- Commit `5d92c9a` sur `websec`

### Session 2 — Comptes test par rôle
- `TestDataFixtures` avec `@Profile("dev")` : seed idempotent de 3 comptes test (GESTIONNAIRE, FORMATEUR, APPRENANT)
- Commit `63f5533` sur `websec`

### Session 3 — RBAC backend
- `@PreAuthorize` sur toutes les implémentations concrètes
- `/users/create` restreint à ADMINISTRATEUR
- 10/10 tests RBAC validés (captures `01`–`04`)
- Commit `6c2367b` sur `websec`

### Session 4 — RBAC frontend
- `ProtectedRoute` role-aware consommant `useAuth()` (décision D12)
- `ForbiddenPage` sur `/forbidden`
- Intercepteur axios 401 (garde-fou token expiré, décision D10)
- Matrice RBAC appliquée dans `AppRouter`
- Sidebar filtrée par rôle (résolution dette S13)
- Intercepteur axios 403 global retiré — pages gèrent leurs 403 localement (décision D13)
- Captures UI `05`–`07` (menus admin, menus apprenant, page forbidden)
- Commits `99f6513`, `a8222da`, `359f73e` sur `frontend-integration` ; `244ff41` sur `websec`

> **Note S16** : le commit `359f73e` porte le message "ajouter intercepteur axios de réponse pour gérer les erreurs 401 et 403" alors qu'il contient en réalité la Phase 3 (matrice AppRouter, corrections Sidebar) et le retrait de l'intercepteur 403 global. Message trompeur détecté — documenté ici par transparence. Aucun force-push effectué (commit déjà pushé, risque inutile).

---

## 7. Décisions d'architecture RBAC

| # | Décision | Justification |
|---|---|---|
| D1 | RBAC pur, pas ABAC | Scope projet académique |
| D3 | Claim `roles` JWT abandonné | `validateToken()` charge déjà les autorités depuis DB — annotation `@PreAuthorize` fonctionne sans modification JWT |
| D4 | Pas de migration cookie HttpOnly | Hors scope, projet académique |
| D10 | Pas d'appel `/api/users/me` | Payload login retourne user complet + roles ; intercepteur 401 gère token expiré |
| D11 | APPRENANT voit Formations en lecture seule | Alignement frontend sur matrice backend |
| D12 | `ProtectedRoute` consomme `useAuth()` | Source de vérité unique, pas `localStorage` directement |
| D13 | Intercepteur axios 403 global retiré | Un 403 API ne doit pas rediriger l'app entière ; pages gèrent leurs 403 localement |

---

## 8. Dette résiduelle

| # | Description | Gravité | Décision |
|---|---|---|---|
| S10 | `UserFixtures` sans `@Profile` — comptes admin créés en prod si déployé | Faible | Risque accepté — projet académique, jamais déployé |
| S11 | Endpoint `/users/apprenants` manquant pour GESTIONNAIRE | Moyenne | Hors scope |
| S12 | `UserServiceImpl.create()` applique les rôles fournis par le client sans contrôle ABAC | Moyenne | Hors scope — RBAC pur retenu (D1) |
| S14 | `UserDto` redéclare `email` hérité de `AppUserDto` — risque conflit Jackson | Faible | Hors scope |
| S15 | Backend retourne HTTP 200 sur login échoué (erreur dans le payload) | Moyenne | Hors scope |
| S16 | Commit `359f73e` message trompeur — voir section 6 | Cosmétique | Documenté, pas de force-push |
| S18 | Intercepteur axios 403 global retiré | Info | Décision D13 — comportement voulu |

---

## 9. Sources

**Spring Security — Method Security & rôles :**
- [Method Security — documentation officielle Spring Security](https://docs.enterprise.spring.io/spring-security/reference/servlet/authorization/method-security.html)
- [Introduction to Spring Method Security — Baeldung](https://www.baeldung.com/spring-security-method-security)
- [Difference Between hasRole() and hasAuthority() — GeeksforGeeks](https://www.geeksforgeeks.org/advance-java/difference-between-hasrole-and-hasauthority-in-spring-security/)

**OWASP — Broken Access Control :**
- [OWASP Top 10 A01:2021 — Broken Access Control](https://owasp.org/Top10/A01_2021-Broken_Access_Control/)
- [JSON Web Token for Java Cheat Sheet — OWASP](https://cheatsheetseries.owasp.org/cheatsheets/JSON_Web_Token_for_Java_Cheat_Sheet.html)