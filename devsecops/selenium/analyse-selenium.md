# Audit Selenium — EduManage Secure (Point P4)

> **Auteur** : Joan Eyeghe — **Date** : 20/08/2026 — **Branche** : `websec`
> **Périmètre** : tests d'interface automatisés sur le RBAC frontend (React Vite, port 5173).

---

## 1. Contexte & méthode (QQOQCP)

| Question | Réponse |
|---|---|
| **Quoi ?** | Vérifier automatiquement que le RBAC frontend est correctement appliqué côté UI. |
| **Qui ?** | Utilisateurs avec rôles APPRENANT et ADMINISTRATEUR. |
| **Où ?** | Frontend React Vite (`localhost:5173`), piloté par ChromeDriver via Selenium WebDriver. |
| **Quand ?** | À chaque exécution manuelle (`mvn test -Dgroups=selenium`). Exclus du pipeline CI (nécessitent Chrome + frontend démarré). |
| **Comment ?** | 3 tests JUnit 5 héritant de `SeleniumBaseTest`, tagués `@Tag("selenium")`, utilisant `WebDriverWait` pour la synchronisation asynchrone. |
| **Pourquoi ?** | Les tests backend (P3) valident le RBAC au niveau API. P4 valide que le frontend traduit correctement ces règles en expérience utilisateur : menu filtré, redirections, page Forbidden. |

---

## 2. Architecture des tests

### Classe de base — `SeleniumBaseTest`

| Élément | Valeur |
|---|---|
| `BASE_URL` | `http://localhost:5173` |
| Driver | `ChromeDriver` (WebDriverManager gère le téléchargement automatique) |
| Timeout | 10 secondes (`WebDriverWait`) |
| Cycle | `@BeforeEach` instancie le driver — `@AfterEach` appelle `driver.quit()` |
| Options | `ChromeOptions` avec `detach: true` (sans effet avec `quit()` explicite — voir note) |

> **Note technique** : l'option `detach` ne conserve pas la fenêtre Chrome ouverte
> lorsque `driver.quit()` est appelé explicitement. `detach` protège uniquement contre
> la mort du processus chromedriver — pas contre un `quit()` de session. Comportement
> confirmé par mesure : 0 fenêtre Chrome résiduelle après chaque run.

---

## 3. Tableau des tests

| Classe | Scénario | Compte utilisé | Assertions |
|---|---|---|---|
| `ApprenantSidebarTest` | Un apprenant ne voit que Dashboard et Formations | `test.apprenant@edumanage.local` | Dashboard présent ✅ — Formations présent ✅ — Paiements absent ✅ — Utilisateurs absent ✅ |
| `ApprenantForbiddenTest` | Un apprenant accédant à `/paiements` est redirigé vers `/forbidden` | `test.apprenant@edumanage.local` | URL contient `/forbidden` après navigation vers `/paiements` ✅ |
| `AdminSidebarTest` | Un administrateur voit les 6 entrées de menu | `joanarcher26@gmail.com` | Dashboard ✅ — Utilisateurs ✅ — Formations ✅ — Inscriptions ✅ — Paiements ✅ — Audit Logs ✅ |

---

## 4. Résultats

### Run individuel (validation par test)

| Test | Durée | Statut |
|---|---|---|
| `ApprenantSidebarTest` | ~14 s | ✅ BUILD SUCCESS |
| `ApprenantForbiddenTest` | ~10 s | ✅ BUILD SUCCESS |
| `AdminSidebarTest` | ~10 s | ✅ BUILD SUCCESS |

### Run groupé (suite complète)

```
Tests run: 3, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
Total time: 46 s
```

---

## 5. Lien avec P3 RBAC

P4 est la validation bout en bout de P3. Les règles testées sont :

| Règle RBAC (P3) | Validée par (P4) |
|---|---|
| APPRENANT voit Dashboard + Formations uniquement | `ApprenantSidebarTest` |
| APPRENANT ne peut pas accéder à `/paiements` | `ApprenantForbiddenTest` |
| ADMINISTRATEUR voit les 6 items de menu | `AdminSidebarTest` |

Les sélecteurs CSS (`a.sidebar-link[href='...']`) ciblent les ancres de la `Sidebar`
React — ils sont robustes tant que la classe `sidebar-link` et les `href` ne changent pas.

---

## 6. Limites et dette

| Limite | Impact | Décision |
|---|---|---|
| Tests exclus du pipeline CI | Pas de régression automatique sur PR | Accepté — nécessitent Chrome + frontend démarré, incompatible avec CI headless sans configuration dédiée |
| Pas de test pour GESTIONNAIRE et FORMATEUR | Couverture partielle des rôles | Hors scope P4 — couverture backend suffisante pour le livrable académique |
| `Thread.sleep()` non ajouté | Chrome ferme immédiatement après assertion | Décision volontaire — ne pas ralentir le pipeline même pour les runs locaux |
| Sélecteur `button[type='submit']` | Fragile si plusieurs boutons submit sur la page | Acceptable — `LoginPage` n'a qu'un seul bouton submit |

---

## 7. Captures d'écran

| Fichier | Contenu |
|---|---|
| `01_admin_sidebar_complete.png` | Sidebar admin — 6 items visibles |
| `02_apprenant_sidebar_reduite.png` | Sidebar apprenant — Dashboard + Formations uniquement |
| `03_apprenant_acces_interdit.png` | Page `/forbidden` après tentative d'accès à `/paiements` |
| `04_maven_3_tests_build_success.png` | Sortie Maven — 3 tests verts, BUILD SUCCESS |
