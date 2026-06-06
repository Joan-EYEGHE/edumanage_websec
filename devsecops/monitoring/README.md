# P9 — Monitoring : Prometheus + Grafana

## Objectif

Mettre en place un système de monitoring pour l'application EduManage Secure.
Prometheus collecte les métriques exposées par le backend Spring Boot.
Grafana les visualise sous forme de dashboards.

## Architecture

```
Backend Spring Boot (port 8082)
|
| /api/actuator/prometheus
v
Prometheus (port 9091) — scrape toutes les 15s
|
v
Grafana (port 3000) — visualisation des métriques
```

## Prérequis

- Docker installé
- Backend Spring Boot démarré sur le port 8082

## Lancer le monitoring

```powershell
cd devsecops/monitoring
docker compose up -d
```

## Accès

| Service    | URL                        | Identifiants       |
|------------|----------------------------|--------------------|
| Prometheus | http://localhost:9091      | aucun              |
| Grafana    | http://localhost:3000      | admin / admin      |

## Métriques exposées

Le backend expose ses métriques via Spring Boot Actuator + Micrometer à l'endpoint :

```
GET /api/actuator/prometheus
```

Exemples de métriques disponibles :
- jvm_memory_used_bytes — mémoire JVM utilisée
- http_server_requests_seconds — temps de réponse des requêtes HTTP
- hikaricp_connections — état du pool de connexions PostgreSQL
- process_cpu_usage — utilisation CPU du processus

## Configuration Prometheus

Le fichier prometheus.yml configure Prometheus pour scraper le backend toutes les 15 secondes.
L adresse host.docker.internal permet à Prometheus (dans Docker) d atteindre le backend sur la machine hôte.

## Screenshots

| Fichier | Description |
|--------|-------------|
| 01_prometheus_targets.png | Backend visible par Prometheus avec statut UP |
| 02_prometheus_graph.png | Métrique jvm_memory_used_bytes affichée dans Prometheus |
| 03_grafana_login.png | Page de connexion Grafana |
| 04_grafana_datasource.png | Connexion Grafana → Prometheus validée |
| 05_grafana_dashboard.png | Dashboard EduManage avec métriques en temps réel |
```
