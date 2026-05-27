# P11 — NGINX Reverse Proxy

## Qu'est-ce qu'un reverse proxy ?

NGINX est placé en entrée du système. Il reçoit toutes les requêtes de l'utilisateur
et les redirige vers le bon service en arrière-plan.

Sans NGINX :
- Frontend accessible sur http://localhost:5173
- Backend accessible sur http://localhost:8082

Avec NGINX :
- Tout passe par https://localhost
- NGINX redirige /api/... vers le backend (port 8082)
- NGINX redirige / vers le frontend (port 5173)
- Les ports internes ne sont plus exposés directement

## Pourquoi c'est utile en sécurité ?

- Point d'entrée unique : réduit la surface d'attaque
- HTTPS activé : les données sont chiffrées entre le navigateur et le serveur
- Headers de sécurité centralisés : CSP, X-Frame-Options, X-Content-Type-Options
- Ports internes cachés : l'utilisateur ne voit jamais 8082 ou 5173

## Lien avec P6 — OWASP ZAP

Le scan ZAP avait détecté une alerte Medium : Content Security Policy (CSP) Header Not Set.
NGINX corrige cette alerte en ajoutant le header CSP sur toutes les réponses.

## Structure des fichiers

```
devsecops/nginx/
├── nginx.conf            — configuration NGINX (proxy, HTTPS, headers)
├── docker-compose.yml    — lancement de NGINX via Docker
├── certs/
│   ├── nginx.crt         — certificat SSL auto-signé
│   └── nginx.key         — clé privée
├── screenshots/          — preuves de fonctionnement
└── README.md             — ce fichier
```

## Comment lancer NGINX

### Prérequis
- Docker installé
- Backend Spring Boot démarré sur le port 8082
- Frontend React démarré avec : npm run dev -- --host

### Démarrage
```powershell
cd devsecops/nginx
docker-compose up -d
```

### Vérification
```powershell
docker ps
docker logs edumanage-nginx
```

Puis ouvrir https://localhost dans le navigateur.

## Résultats obtenus

| Test | Résultat |
|------|----------|
| https://localhost/login | ✅ Frontend React affiché |
| https://localhost/api/users/login | ✅ Backend Spring Boot répond (405 Method Not Allowed = normal pour un GET) |
| Certificat auto-signé | ✅ Avertissement navigateur attendu |
| Header CSP ajouté | ✅ Alerte ZAP corrigée |
| Redirection HTTP → HTTPS | ✅ Port 80 redirige vers 443 |

## Difficultés rencontrées

- Port 443 occupé par Apache (httpd) → processus arrêté manuellement
- Port 8080 occupé par Keycloak → backend déplacé sur le port 8082
- Frontend Vite en écoute IPv6 uniquement → relancé avec --host pour écouter sur 0.0.0.0
- CSP trop restrictif bloquant React → directive assouplie pour autoriser les scripts inline et WebSockets
```
