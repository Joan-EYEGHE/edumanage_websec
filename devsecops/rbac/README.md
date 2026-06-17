# Tests manuels RBAC / JWT — Procédure de reproduction (P3)

Ce dossier accompagne [`analyse-rbac.md`](./analyse-rbac.md). Il décrit comment **reproduire**
les tests qui démontrent l'absence de contrôle d'accès par rôle, captures d'écran à l'appui.

> Les commandes ci-dessous sont en **`curl.exe`** (le vrai curl, pas l'alias PowerShell `Invoke-WebRequest`)
> et sont **prêtes à coller dans PowerShell**. Le JSON est entre guillemets simples PowerShell `'...'`
> pour préserver les guillemets doubles internes.

---

## Pré-requis

| Élément | Valeur |
|---|---|
| Backend démarré | profil `dev`, port **8082** |
| URL de base | `http://localhost:8082/api` |
| Compte admin de test | `joanarcher26@gmail.com` / `joan@admin2` (rôle `ADMINISTRATEUR`) |
| Outil | PowerShell + `curl.exe` (inclus dans Windows 10/11) |

> Le préfixe `/api` vient du *context-path* du serveur. Les chemins déclarés dans `SecurityConfig`
> (ex. `/users/login`) deviennent donc `http://localhost:8082/api/users/login` côté client.

---

## Étape 0 — Authentification (récupérer un token admin)

Affichage brut de la réponse (pour voir la structure) :

```powershell
curl.exe -s -X POST "http://localhost:8082/api/users/login" -H "Content-Type: application/json" -d '{"email":"joanarcher26@gmail.com","password":"joan@admin2"}'
```

Extraction automatique du token dans une variable réutilisable :

```powershell
$login = curl.exe -s -X POST "http://localhost:8082/api/users/login" -H "Content-Type: application/json" -d '{"email":"joanarcher26@gmail.com","password":"joan@admin2"}' | ConvertFrom-Json
$tokenAdmin = $login.payload.accessToken
$tokenAdmin   # doit afficher un long JWT
```

📸 *Screenshot attendu* : `screenshots/00-login-admin.png`

---

## Test A — Accès SANS token → 401 attendu

But : vérifier que l'authentification est bien exigée.

```powershell
curl.exe -i "http://localhost:8082/api/audit-logs/all"
```

✅ Résultat attendu : **401 Unauthorized**.
📸 `screenshots/A-sans-token-401.png`

---

## Test B — Accès AVEC token admin → 200

```powershell
curl.exe -i "http://localhost:8082/api/audit-logs/all" -H "Authorization: Bearer $tokenAdmin"
```

✅ Résultat attendu : **200 OK** (l'admin accède aux journaux d'audit, comportement normal).
📸 `screenshots/B-admin-200.png`

---

## Test C — Création de compte SANS authentification *(faille É2)*

But : démontrer que `/users/create` est public — n'importe qui peut créer un compte.

```powershell
curl.exe -i -X POST "http://localhost:8082/api/users/create" -H "Content-Type: application/json" -d '{"nom":"Test","prenom":"Apprenant","email":"apprenant.test@example.com","telephone":"+221770000000","roles":["APPRENANT"],"actif":true,"statut":"ACTIF","password":"apprenant@2026"}'
```

✅ Résultat attendu : **compte créé** alors qu'aucun token n'a été fourni.
📸 `screenshots/C-create-sans-auth.png`

---

## Test D — Élévation de privilège : créer un ADMIN sans authentification *(faille É2, critique)*

But : démontrer qu'on peut s'auto-octroyer le rôle `ADMINISTRATEUR` sans être connecté.

```powershell
curl.exe -i -X POST "http://localhost:8082/api/users/create" -H "Content-Type: application/json" -d '{"nom":"Pirate","prenom":"Anonyme","email":"pirate.admin@example.com","telephone":"+221770000001","roles":["ADMINISTRATEUR"],"actif":true,"statut":"ACTIF","password":"pirate@2026"}'
```

⚠️ Résultat attendu : **compte ADMINISTRATEUR créé sans aucune authentification**.
On peut ensuite se connecter avec ce compte (`pirate.admin@example.com` / `pirate@2026`).
📸 `screenshots/D-elevation-privilege.png`

---

## Test E — Preuve que le RBAC n'est PAS appliqué *(faille É1, le test clé du P3)*

But : un compte **APPRENANT** (le rôle le moins privilégié) accède à une route censée être
réservée aux admins (journaux d'audit). En présence d'un RBAC, on attendrait **403 Forbidden**.

```powershell
# 1) Se connecter avec le compte APPRENANT créé au Test C
$loginApp = curl.exe -s -X POST "http://localhost:8082/api/users/login" -H "Content-Type: application/json" -d '{"email":"apprenant.test@example.com","password":"apprenant@2026"}' | ConvertFrom-Json
$tokenApprenant = $loginApp.payload.accessToken

# 2) Accéder aux journaux d'audit avec ce token APPRENANT
curl.exe -i "http://localhost:8082/api/audit-logs/all" -H "Authorization: Bearer $tokenApprenant"
```

❌ Résultat attendu en l'état : **200 OK** → c'est la **preuve** que le rôle n'est pas vérifié.
✅ Résultat souhaité après correctif : **403 Forbidden**.
📸 `screenshots/E-apprenant-acces-admin-200.png`

---

## Récapitulatif des résultats attendus

| Test | Requête | Attendu (actuel) | Attendu (après correctif RBAC) |
|---|---|---|---|
| A | audit-logs sans token | 401 | 401 |
| B | audit-logs token admin | 200 | 200 |
| C | create sans auth (apprenant) | créé | refusé / rôle forcé |
| D | create sans auth (admin) | créé ⚠️ | refusé |
| E | audit-logs token apprenant | **200** ❌ | **403** ✅ |

Les écarts E et D constituent les preuves principales du rapport
(*Broken Access Control*, OWASP A01:2021). Détails et recommandations dans [`analyse-rbac.md`](./analyse-rbac.md).
