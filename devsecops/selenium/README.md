# P4 — Tests automatisés RBAC avec Selenium

## Objectif

Vérifier automatiquement que le RBAC frontend fonctionne correctement via des tests
d'interface pilotés par Selenium WebDriver. Ces tests complètent les tests unitaires
backend (P3) en validant le comportement réel de l'UI dans un navigateur Chrome.

---

## Pré-requis

| Pré-requis | Valeur |
|---|---|
| Backend démarré | `http://localhost:8082/api` |
| Frontend démarré | `http://localhost:5173` |
| Profil Spring actif | `dev` (actif par défaut dans `application.yaml`) |
| Comptes test en base | Créés automatiquement par `TestDataFixtures` au démarrage |
| Chrome installé | Version récente (ChromeDriver géré automatiquement par WebDriverManager) |

### Démarrer le backend

```bash
# Depuis main/
./mvnw spring-boot:run
```

### Démarrer le frontend

```bash
# Depuis main/frontend/
npm run dev -- --host
```

---

## Comptes de test utilisés

| Compte | Mot de passe | Rôle |
|---|---|---|
| `test.apprenant@edumanage.local` | `Test1234!` | APPRENANT |
| `joanarcher26@gmail.com` | *(voir application-dev.yml)* | ADMINISTRATEUR |

---

## Lancer les tests

### Tous les tests Selenium

```bash
mvn test -Dgroups=selenium
```

### Un test spécifique

```bash
mvn test -Dgroups=selenium -Dtest=ApprenantSidebarTest
mvn test -Dgroups=selenium -Dtest=ApprenantForbiddenTest
mvn test -Dgroups=selenium -Dtest=AdminSidebarTest
```

### Tests unitaires seuls (exclut Selenium)

```bash
mvn test
```

Le tag `@Tag("selenium")` garantit que les tests Selenium ne s'exécutent **jamais**
lors d'un `mvn test` standard. Ils nécessitent Chrome et un frontend démarré —
ils sont exclus du pipeline CI pour cette raison.

---

## Structure des tests

```
src/test/java/ism/dakar/edumanage/selenium/
├── SeleniumBaseTest.java        ← classe de base (@BeforeEach/@AfterEach, ChromeDriver, wait)
├── AdminSidebarTest.java        ← admin voit les 6 items de menu
├── ApprenantSidebarTest.java    ← apprenant voit Dashboard + Formations uniquement
└── ApprenantForbiddenTest.java  ← apprenant redirigé vers /forbidden sur /paiements
```

---

## Résultats attendus

| Test | Scénario | Résultat attendu |
|---|---|---|
| `ApprenantSidebarTest` | Login apprenant → sidebar | Dashboard + Formations visibles ; Paiements + Utilisateurs absents |
| `ApprenantForbiddenTest` | Login apprenant → accès `/paiements` | Redirection vers `/forbidden` |
| `AdminSidebarTest` | Login admin → sidebar | 6 items visibles (Dashboard, Utilisateurs, Formations, Inscriptions, Paiements, Audit Logs) |

---

## Mécanisme d'exclusion par tag

La classe de base `SeleniumBaseTest` ne porte pas de `@Tag`. Chaque test porte
`@Tag("selenium")`. Le `pom.xml` configure Surefire pour exclure ce tag par défaut :

```xml
<excludedGroups>selenium</excludedGroups>
```

Pour inclure les tests Selenium, passer `-Dgroups=selenium` explicitement.
