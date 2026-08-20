# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Location

The frontend source lives in `frontend/` at the root of the mono-repo (`main/frontend/`). All commands below must be run from that directory.

## Commands

```bash
# From main/frontend/
npm run dev -- --host   # Start dev server (Vite, localhost:5173)
npm run build           # Production build
npm run lint            # Run ESLint
npm run preview         # Preview production build
```

There are no tests configured.

## Architecture

This is a React 19 + Vite SPA for an education management platform (EduManage). It communicates with a Spring Boot backend at `http://localhost:8082/api`.

### Request flow

`src/api/axios.js` — central Axios instance that:
- Auto-attaches the JWT from `localStorage.accessToken` as a Bearer token on every request.
- Intercepts 401 responses: clears auth data and redirects to `/login` (except on `/users/login` itself).

All API modules (`src/api/*Api.js`) call that instance and use helpers in `src/utils/apiResponse.js` to unpack the backend's envelope format: { status, message, payload, metadata }


- `extractPayload(response)` → `response.data.payload`
- `extractMetadata(response)` → normalized pagination object `{ number, totalElements, size, totalPages }`

### Auth

- `AuthContext` (context/AuthContext.jsx) holds the current user and exposes `login` / `logout` via `useAuth()`.
- `utils/auth.js` persists `accessToken`, `expiresIn`, and `user` to `localStorage`.
- `ProtectedRoute` (components/common/ProtectedRoute.jsx) guards routes using `useAuth()` — **not** `localStorage` directly.

### Role system

Four roles: `ADMINISTRATEUR`, `GESTIONNAIRE`, `FORMATEUR`, `APPRENANT` (defined in `utils/roles.js`).

Use `hasRole(user, role)` or `hasAnyRole(user, roles[])` to gate UI actions. The `user` object comes from `useAuth()`.

**Convention** : role strings have no `ROLE_` prefix — use `hasAnyRole(user, ['ADMINISTRATEUR'])`, never `hasRole(user, 'ROLE_ADMINISTRATEUR')`.

### Page pattern

Every page follows the same structure:
1. Wrap content in `<AppLayout>` (renders Sidebar + Topbar).
2. Open with `<PageHeader title subtitle action>`.
3. Use shared components from `components/common/` (`DataTable`, `Pagination`, `SearchBar`, `Modal`, `FormInput`, `FormSelect`, `StatusBadge`, `LoadingMessage`, `ErrorMessage`).
4. Inline styles object at the bottom of the file (no CSS modules, no Tailwind).

### Routes

All routes are in `src/routes/AppRouter.jsx`. Every route except `/login` and `/forbidden` is wrapped in `<ProtectedRoute allowedRoles={[...]}>`.

| Path | Page | allowedRoles |
|---|---|---|
| `/dashboard` | DashboardPage | ADMINISTRATEUR, GESTIONNAIRE, FORMATEUR, APPRENANT |
| `/users` | UsersPage | ADMINISTRATEUR |
| `/formations` | FormationsPage | ADMINISTRATEUR, GESTIONNAIRE, FORMATEUR, APPRENANT |
| `/inscriptions` | InscriptionsPage | ADMINISTRATEUR, GESTIONNAIRE, FORMATEUR |
| `/paiements` | PaiementsPage | ADMINISTRATEUR, GESTIONNAIRE |
| `/audit-logs` | AuditLogsPage | ADMINISTRATEUR |
| `/forbidden` | ForbiddenPage | Tout authentifié |