# Deployment-Setup

Diese Dokumentation erklärt, wie das automatische Deployment zu GitHub Pages konfiguriert ist.

## Übersicht

Das Projekt nutzt GitHub Actions, um sowohl die Frontend-Anwendung als auch die von Maven generierte Dokumentation automatisch zu GitHub Pages zu deployen, sobald Änderungen in den `main`- oder `dev`-Branch gepusht werden.

## Was wird deployed

1. **Frontend-Anwendung** - Die FoodRescue-Webanwendung
    - Login/Registrierungsseite
    - Dashboard mit rollenbasierter UI
    - Angebots- und Reservierungsverwaltung

2. **Maven Site Dokumentation** - Automatisch generierte Projektdokumentation mit:
    - Projektinformationen und Reports
    - Dependency-Analyse
    - Test-Coverage-Reports (JaCoCo)
    - Plugin-Informationen
    - JavaDocs

## Deployment-Struktur

```
GitHub Pages Site Root
├── index.html              # Frontend Login-Seite
├── dashboard.html          # Frontend Dashboard
├── register.html           # Anbieter-Registrierung
├── css/                    # Stylesheets
│   ├── styles.css
│   └── dashboard.css
├── js/                     # JavaScript-Module
│   ├── main.js
│   ├── authActions.js
│   ├── dashboardInit.js
│   ├── loadAngebot.js
│   ├── handleCreateAngebot.js
│   └── ...
├── img/                    # Bilder und Assets
│   ├── food-rescue-hero-login.jpg
│   └── kawaii-food.png
├── video/                  # Video-Assets
│   └── login-hero.mp4
└── reports/                # Maven-generierte Dokumentation
    ├── index.html          # Projekt-Dokumentations-Startseite
    ├── dependencies.html   # Dependencies-Report
    ├── jacoco/            # Test-Coverage-Reports
    │   ├── index.html
    │   └── ...
    ├── surefire-report.html # Test-Ergebnisse
    └── ...                # Weitere Maven-Reports
```

## GitHub Actions Workflow

Das Deployment wird durch `.github/workflows/ci.yml` gesteuert, welche:

1. **Backend bauen und testen**
    - Maven Build mit Java 21
    - Spotless Code-Formatierungsprüfung
    - JUnit-Tests ausführen
    - JaCoCo Test-Coverage generieren

2. **Frontend linten**
    - ESLint-Prüfung der JavaScript-Module
    - Code-Qualitätsprüfung

3. **Statisches Frontend packen**
    - Kopiert alle Dateien aus `resources/static/`
    - HTML, CSS, JavaScript, Bilder, Videos

4. **Maven Site Dokumentation generieren**
    - Erstellt umfassende Projektdokumentation
    - Generiert Test-Reports
    - Erstellt Dependency-Analysen
    - Baut JavaDocs

5. **Alles kombinieren**
    - Frontend und Reports in ein Deployment-Paket zusammenführen
    - Struktur für GitHub Pages vorbereiten

6. **Zu GitHub Pages deployen**
    - Automatisches Deployment bei Push zu `main` oder `dev`
    - Setzt `.nojekyll` für korrekte Darstellung

## Setup-Anforderungen

### 1. GitHub Pages aktivieren

1. Gehe zu den Repository-Einstellungen
2. Navigiere zum Abschnitt "Pages"
3. Unter "Source" wähle "GitHub Actions"

### 2. Repository-Berechtigungen

Der Workflow benötigt diese Berechtigungen (bereits konfiguriert):

```yaml
permissions:
  contents: read      # Zum Checkout des Codes
  pages: write        # Zum Deployen zu GitHub Pages
  id-token: write     # Für sicheres Deployment
  checks: write       # Für Test-Reports
  pull-requests: write # Für PR-Kommentare
```

### 3. Secrets und Variablen

Keine zusätzlichen Secrets erforderlich - GitHub stellt automatisch `GITHUB_TOKEN` bereit.

## CI/CD Pipeline im Detail

### Workflow-Jobs

```yaml
jobs:
  backend:
    # 1. Backend bauen und testen
    - Checkout Code
    - Setup Java 21
    - Maven Cache
    - Maven Build (mit Spotless-Check)
    - Tests ausführen
    - JaCoCo Coverage Report
    - Test-Ergebnisse zu PR posten

  package-frontend:
    # 2. Frontend packen
    - Statische Dateien aus resources/static/ kopieren
    - Als Artifact hochladen

  publish:
    # 3. Maven Site generieren & deployen
    - Nur bei Push zu main/dev
    - Maven Site generieren
    - Frontend und Reports kombinieren
    - Zu GitHub Pages deployen
```

### Trigger-Bedingungen

```yaml
on:
  push:
    branches: [ main, dev ]
  pull_request:
    branches: [ main, dev ]
```

**Behavior**:
- **Pull Requests**: Build und Tests werden ausgeführt, aber kein Deployment
- **Push zu main/dev**: Vollständiger Build, Tests, und Deployment zu GitHub Pages

## 🔧 Lokales Deployment-Testing

### Maven Site lokal generieren

```bash
# Im Projektverzeichnis
mvn clean site

# Dokumentation ist dann verfügbar unter:
# target/site/index.html
```

### Frontend lokal testen

```bash
# Mit Spring Boot starten
mvn spring-boot:run

# Oder mit Java
mvn clean package
java -jar target/foodrescue-1.0.0.jar

# Dann öffnen: http://localhost:8080
```

## Zugriff auf die deployete Site

Nach erfolgreichem Deployment ist die Site verfügbar unter:

- **Hauptanwendung**: `https://[username].github.io/[repository-name]/`
- **Maven Reports & Dokumentation**: `https://[username].github.io/[repository-name]/reports/`
    - Test-Coverage: `https://[username].github.io/[repository-name]/reports/jacoco/`
    - Projekt-Informationen: `https://[username].github.io/[repository-name]/reports/project-info.html`
    - Test-Ergebnisse: `https://[username].github.io/[repository-name]/reports/surefire-report.html`

### Beispiel-URLs

```
https://futurefounder.github.io/moderne-softwareentwicklung-mim-20-w25-team-3-foodrescue/
https://futurefounder.github.io/moderne-softwareentwicklung-mim-20-w25-team-3-foodrescue/dashboard.html
https://futurefounder.github.io/moderne-softwareentwicklung-mim-20-w25-team-3-foodrescue/reports/
https://futurefounder.github.io/moderne-softwareentwicklung-mim-20-w25-team-3-foodrescue/reports/jacoco/
```

## Troubleshooting

### Build-Fehler

Überprüfe die GitHub Actions-Logs im "Actions"-Tab des Repositories.

**Häufige Probleme**:

#### Maven Build schlägt fehl
```bash
# Lokale Prüfung
mvn clean verify

# Spotless-Fehler beheben
mvn spotless:apply
```

#### Java-Version-Inkompatibilität
- Stelle sicher, dass lokal Java 21 verwendet wird
- Prüfe `pom.xml` auf korrekte Java-Version

#### Tests schlagen fehl
```bash
# Einzelnen Test ausführen
mvn test -Dtest=ClassName#methodName

# Mit Debugging
mvn test -X
```

#### Node.js-Probleme (Frontend-Linting)
```bash
# ESLint lokal ausführen
npm install
npm run lint
```

### Deployment schlägt fehl

#### GitHub Pages-Einstellungen prüfen
- Navigiere zu Settings → Pages
- Quelle muss "GitHub Actions" sein
- Branch sollte nicht "None" sein

#### Berechtigungen prüfen
- Workflow benötigt `pages: write` und `id-token: write`
- Überprüfe Repository Settings → Actions → General → Workflow permissions

#### Cache-Probleme
```bash
# GitHub Actions Cache löschen
# Settings → Actions → Caches → Delete all caches
```

### Dokumentation wird nicht aktualisiert

#### Maven Site prüfen
```bash
# Lokal generieren und Fehler prüfen
mvn clean site

# Site-Descriptor validieren
cat src/site/site.xml
```

#### Reports fehlen
Stelle sicher, dass `pom.xml` alle benötigten Reporting-Plugins enthält:
```xml
<reporting>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-surefire-report-plugin</artifactId>
        </plugin>
        <plugin>
            <groupId>org.jacoco</groupId>
            <artifactId>jacoco-maven-plugin</artifactId>
        </plugin>
    </plugins>
</reporting>
```

## Anpassung

### Weitere Reports hinzufügen

Bearbeite `pom.xml` im `<reporting>`-Bereich:

```xml
<reporting>
    <plugins>
        <!-- Bestehende Plugins... -->
        
        <!-- Dependency-Updates-Report -->
        <plugin>
            <groupId>org.codehaus.mojo</groupId>
            <artifactId>versions-maven-plugin</artifactId>
            <version>2.16.2</version>
        </plugin>
        
        <!-- Checkstyle-Report -->
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-checkstyle-plugin</artifactId>
            <version>3.3.1</version>
        </plugin>
    </plugins>
</reporting>
```

### Site-Erscheinungsbild ändern

Modifiziere `src/site/site.xml`:

```xml
<project>
    <skin>
        <groupId>org.apache.maven.skins</groupId>
        <artifactId>maven-fluido-skin</artifactId>
        <version>1.11.2</version>
    </skin>
    
    <body>
        <menu name="FoodRescue">
            <item name="Startseite" href="index.html"/>
            <item name="Tests" href="surefire-report.html"/>
            <item name="Coverage" href="jacoco/index.html"/>
        </menu>
    </body>
</project>
```

### Frontend-Build-Prozess erweitern

Für komplexere Frontend-Builds:

```yaml
# In .github/workflows/ci.yml
- name: Build Frontend
  run: |
    npm install
    npm run build
    
- name: Copy Built Files
  run: |
    cp -r dist/* frontend-artifact/
```

### Deployment-Branch ändern

```yaml
# In .github/workflows/ci.yml
on:
  push:
    branches: [ main, production ]  # production statt dev
```

## Deployment-Metriken

### Build-Zeiten (typisch)

- Backend Build & Tests: ~2-3 Minuten
- Frontend Packaging: ~30 Sekunden
- Maven Site Generation: ~1-2 Minuten
- GitHub Pages Deployment: ~1 Minute

**Gesamt**: ~5-7 Minuten pro Deployment

### Artifact-Größen

- Frontend-Static-Files: ~5-10 MB (mit Bildern/Videos)
- Maven Site Documentation: ~2-5 MB
- JaCoCo Reports: ~500 KB - 2 MB

## Sicherheit

### GitHub Token

Der Workflow nutzt das automatisch bereitgestellte `GITHUB_TOKEN`:
- Begrenzte Berechtigungen
- Automatisch von GitHub verwaltet
- Keine manuellen Secrets erforderlich

### Dependency-Scanning

Erwäge die Aktivierung von:
- **Dependabot**: Automatische Dependency-Updates
- **Code Scanning**: Sicherheitslücken-Erkennung
- **Secret Scanning**: Verhindert versehentliches Commit von Secrets

## Checkliste vor dem ersten Deployment

- [ ] GitHub Pages aktiviert (Settings → Pages → Source: GitHub Actions)
- [ ] `pom.xml` enthält korrekte Maven-Plugins
- [ ] `src/site/site.xml` existiert und ist valide
- [ ] Alle Tests laufen lokal erfolgreich (`mvn test`)
- [ ] Maven Site generiert lokal ohne Fehler (`mvn site`)
- [ ] Frontend-Dateien sind in `resources/static/`
- [ ] `.github/workflows/ci.yml` existiert
- [ ] Repository-Berechtigungen sind korrekt gesetzt

## Deployment-Status prüfen

### In GitHub

1. Gehe zum Repository
2. Klicke auf "Actions"-Tab
3. Sieh dir den neuesten Workflow-Run an
4. Grüner Haken = Erfolg, Rotes X = Fehler

### Logs einsehen

```
Actions → [Workflow Run auswählen] → [Job auswählen] → Logs anzeigen
```

### Deployment-URL testen

Nach erfolgreichem Deployment:
```bash
# Hauptseite prüfen
curl -I https://github.com/futurefounder/moderne-softwareentwicklung-mim-20-w25-team-3-foodrescue.git/

# Reports prüfen
curl -I https://github.com/futurefounder/moderne-softwareentwicklung-mim-20-w25-team-3-foodrescue.git/reports/
```

## Weiterführende Ressourcen

- [GitHub Actions Dokumentation](https://docs.github.com/en/actions)
- [GitHub Pages Dokumentation](https://docs.github.com/en/pages)
- [Maven Site Plugin](https://maven.apache.org/plugins/maven-site-plugin/)
- [JaCoCo Maven Plugin](https://www.jacoco.org/jacoco/trunk/doc/maven.html)

## Support

Bei Problemen:
1. Prüfe GitHub Actions-Logs
2. Teste lokal mit `mvn clean site`
3. Öffne ein Issue im Repository
4. Überprüfe GitHub Status: [githubstatus.com](https://www.githubstatus.com/)

---

**Letzte Aktualisierung**: 10. Januar 2026