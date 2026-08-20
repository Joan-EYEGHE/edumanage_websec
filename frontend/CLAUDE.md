# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Location

The actual frontend source lives in `edumanage_websec/frontend/`. All commands below must be run from that directory.

## Commands

```bash
# From edumanage_websec/frontend/
pnpm dev          # Start dev server (Vite, localhost:5173)
pnpm build        # Production build
pnpm lint         # Run ESLint
pnpm preview      # Preview production build
```

There are no tests configured.

## Architecture

This is a React 19 + Vite SPA for an education management platform (EduManage). It communicates with a Spring Boot backend at `http://localhost:8080/api`.

### Request flow

`src/api/axios.js` — central Axios instance that auto-attaches the JWT from `localStorage.accessToken` as a Bearer token on every request.

All API modules (`src/api/*Api.js`) call that instance and use helpers in `src/utils/apiResponse.js` to unpack the backend's envelope format:

```
{ status, message, payload, metadata }
```

- `extractPayload(response)` → `response.data.payload`
- `extractMetadata(response)` → normalized pagination object `{ number, totalElements, size, totalPages }`

### Auth

- `AuthContext` (context/AuthContext.jsx) holds the current user and exposes `login` / `logout`.
- `utils/auth.js` persists `accessToken`, `expiresIn`, and `user` to `localStorage`.
- `ProtectedRoute` (components/common/ProtectedRoute.jsx) guards routes by checking `localStorage.accessToken` directly (does not use AuthContext).

### Role system

Four roles: `ADMINISTRATEUR`, `GESTIONNAIRE`, `FORMATEUR`, `APPRENANT` (defined in `utils/roles.js`).

Use `hasRole(user, role)` or `hasAnyRole(user, roles[])` to gate UI actions. The `user` object comes from `useAuth()`.

### Page pattern

Every page follows the same structure:
1. Wrap content in `<AppLayout>` (renders Sidebar + Topbar).
2. Open with `<PageHeader title subtitle action>`.
3. Use shared components from `components/common/` (`DataTable`, `Pagination`, `SearchBar`, `Modal`, `FormInput`, `FormSelect`, `StatusBadge`, `LoadingMessage`, `ErrorMessage`).
4. Inline styles object at the bottom of the file (no CSS modules, no Tailwind).

### Routes

All routes are in `src/routes/AppRouter.jsx`. Every route except `/login` is wrapped in `<ProtectedRoute>`.

| Path | Page |
|---|---|
| `/dashboard` | DashboardPage |
| `/users` | UsersPage |
| `/formations` | FormationsPage |
| `/inscriptions` | InscriptionsPage |
| `/paiements` | PaiementsPage |
| `/audit-logs` | AuditLogsPage |
