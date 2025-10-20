# Inhaltsverzeichnis
**1. Git: Programminformationen und Vorteile bei der Nutzung**

**2. Grundlegende Git-Befehle**

**3. Branches und ihre Nutzung und Umgang mit Merge-Konflikten**

**4. Kombination von Git mit IntelliJ/PyCharm: Local Repository und Remote Repository**

**5. Nützliche Git-Tools und Plattformen**

**6. Schlussfolgerungen für Git-Anfänger**

**7. CI/CD Pipeline**

## 1. Git: Programminformationen und Vorteile bei der Nutzung

**Git** ist ein verteiltes Versionskontrollsystem, das ursprünglich von *Linus Torvalds* im Jahr 2005 für die Entwicklung des Linux-Kernels entworfen wurde. Es dient dazu, Änderungen an Dateien – insbesondere an Quellcode – effizient zu verfolgen, frühere Zustände wiederherzustellen und die Zusammenarbeit in Softwareprojekten zu erleichtern.

Im Gegensatz zu zentralisierten Systemen wie *Subversion (SVN)* oder *CVS* arbeitet Git **dezentral**:  
Jede Entwicklerin und jeder Entwickler besitzt eine vollständige Kopie des Repositories, inklusive Historie. Dadurch können Commits, Branches und Merges lokal ausgeführt werden, ohne eine dauerhafte Serververbindung zu benötigen. Erst wenn man Änderungen teilen oder integrieren möchte, erfolgt eine Synchronisation mit einem **Remote-Repository** (z. B. auf GitHub oder GitLab).

### Hauptmerkmale von Git
- **Verteiltes Arbeiten:** Jeder hat eine vollständige Projektkopie und kann unabhängig entwickeln.
- **Hohe Geschwindigkeit:** Operationen wie Commit, Diff oder Branchwechsel erfolgen lokal und dadurch sehr schnell.
- **Zuverlässigkeit:** Git verwendet Prüfsummen (SHA-1-Hashes), um Datenintegrität sicherzustellen.
- **Flexibles Branching-Modell:** Branches sind leichtgewichtig und fördern parallele Entwicklung.
- **Rückverfolgbarkeit:** Jede Änderung wird mit Autor, Datum und Commit-Nachricht protokolliert.
- **Open Source:** Git ist frei verfügbar und läuft plattformübergreifend (Linux, macOS, Windows).

### Vorteile für Teams und Einzelentwickler
- **Effiziente Zusammenarbeit:** Mehrere Personen können gleichzeitig an verschiedenen Funktionen arbeiten, ohne sich gegenseitig zu behindern.
- **Transparente Versionshistorie:** Änderungen lassen sich jederzeit nachvollziehen, vergleichen oder rückgängig machen.
- **Experimentieren ohne Risiko:** Durch Branches und Tags können neue Ideen getestet werden, ohne den Hauptzweig zu gefährden.
- **Integration mit Entwicklungsplattformen:** Git lässt sich in IDEs wie IntelliJ, Eclipse oder VS Code einbinden und arbeitet nahtlos mit Plattformen wie GitHub, GitLab oder Bitbucket zusammen.
- **Nachvollziehbares Arbeiten:** Durch Pull Requests und Code Reviews bleibt die Codequalität auch bei größeren Teams erhalten.

### Fazit
Git ist heute der De-facto-Standard in der Softwareentwicklung.  
Es ermöglicht eine strukturierte, nachvollziehbare und effiziente Zusammenarbeit in Projekten jeder Größe.  
Ob für Open-Source-Projekte, Unternehmen oder das Studium – die Nutzung von Git ist ein zentraler Bestandteil moderner Softwareentwicklung.

## 2. Grundlegende Git-Befehle
Git-Befehle steuern und organisieren spezifische Aktionen innerhalb des Versionskontrollsystems **Git**. Sie ermöglichen Entwicklern, den gesamten Entwicklungsprozess von Projekten zu verwalten, indem sie verschiedene Funktionen wie das Verfolgen von Änderungen, die Zusammenarbeit mit anderen und die Verwaltung von Versionen bereitstellen.  
Hier sind einige grundlegende Git-Befehle, ihre Rollen, jeweils mit einem Beispiel:


### 1. `git init`
**Rolle**: Initialisiert ein neues Git-Repository in einem bestehenden Verzeichnis. Es erstellt ein `.git`-Verzeichnis, das alle Informationen über das Repository enthält, sodass Dateien verfolgt werden können.

**Beispiel**:
```bash
cd mein-projekt
git init
```
Dies erstellt ein neues Git-Repository im Ordner `mein-projekt`.


### 2. `git clone`
**Rolle**: Erstellt eine lokale Kopie eines bestehenden Git-Repositories.

**Beispiel**:
```bash
git clone https://github.com/username/repository.git
```
Dies kopiert das Repository "repository" aus GitHub in ein neues Verzeichnis auf dem Rechner.


### 3. `git status`
**Rolle**: Zeigt, welche Dateien geändert wurden und welche Änderungen zum Commit vorgemerkt sind.

**Beispiel**:
```bash
git status
```
Dies zeigt die aktuellen Änderungen, die noch nicht zum Commit vorgemerkt sind.


### 4. `git add`
**Rolle**: Fügt Änderungen zur Staging-Area hinzu, sodass sie für den nächsten Commit bereit sind.

**Beispiel**:
```bash
git add datei.txt
```
Dies fügt die Datei "datei.txt" zur Staging-Area hinzu


### 5. `git commit`
**Rolle**: Speichert die Änderungen, die in der Staging-Area vorgemerkt sind, dauerhaft im Repository.

**Beispiel**:
```bash
git commit -m "Füge neue Funktionen hinzu"
```
Dies erstellt einen neuen Commit mit der Nachricht "Füge neue Funktionen hinzu".


### 6. `git push`
**Rolle**: Überträgt lokale Commits auf ein Remote-Repository.

**Beispiel**:
```bash
git push origin main
```
Dies überträgt die Commits auf den `main`-Branch des Remote-Repositories.


### 7. `git pull`
**Rolle**: Holt und integriert Änderungen von einem Remote-Repository in das lokale Repository.

**Beispiel**:
```bash
git pull origin main
```
Dies zieht die Änderungen vom `main`-Branch des Remote-Repositories und integriert sie lokal.


### 8. `git branch`
**Rolle**: Listet alle lokalen Branches auf, erstellt oder löscht Branches.

**Beispiel**:
```bash
git branch feature-branch
```
Dies erstellt einen neuen Branch namens `feature-branch`.


### 9. `git checkout`
**Rolle**: Wechselt zwischen Branches oder setzt den Zustand von Dateien zurück.

**Beispiel**:
```bash
git checkout feature-branch
```
Dies wechselt zum Branch `feature-branch`.


### 10. `git merge`
**Rolle**: Integriert Änderungen von einem Branch in einen anderen.

**Beispiel**:
```bash
git merge feature-branch
```
Dies integriert die Änderungen von `feature-branch` in den aktuellen Branch.

### 11. `git log`
**Rolle**: Dient zur Anzeige der Commit-Historie, die nacheinander vorgenommen wurden.

**Beispiel**:
```bash
git log
```
Zeigt eine Liste der letzten Commits mit Details wie Autor, Datum und Commit-Nachricht.

### 12. `git remote`
**Rolle**: Zeigt alle Remote-Verbindungen an oder erlaubt das Hinzufügen/Entfernen von Remotes.

**Beispiel**:
```bash
git remote add origin https://github.com/username/repository.git
```
Dies fügt ein Remote-Repository namens `origin` hinzu.

### 13. `git reset`
**Rolle**: Kann verwendet werden, um Commits zurückzusetzen oder die Staging-Area und Arbeitsverzeichnis zu ändern.

**Beispiel**:
```bash
git reset --hard HEAD~1
```
Dies setzt das Repository auf den Zustand des vorherigen Commits zurück und verwirft alle Änderungen.

### 14. `git diff`
**Rolle**: Vergleicht Änderungen in Dateien oder zwischen Commits, um Unterschiede anzuzeigen.

**Beispiel**:
```bash
git diff
```
Zeigt die Unterschiede zwischen dem aktuellen Arbeitsverzeichnis und der Staging-Area.

## 3. Branches und ihre Nutzung und Umgang mit Merge-Konflikten

### Was sind Branches?

Ein **Branch** (Zweig) in Git ist eine unabhängige Entwicklungslinie innerhalb eines Repositories.  
Branches ermöglichen es, **parallel an verschiedenen Features, Bugfixes oder Experimenten** zu arbeiten, ohne den Hauptcode (z. B. den `main`- oder `master`-Branch) zu beeinträchtigen.

### Vorteile von Branches
- Unabhängige Entwicklung neuer Funktionen
- Sicheres Testen ohne Risiko für den Hauptzweig
- Einfaches Zusammenführen (Merge) nach Abschluss der Arbeit
- Verbesserte Zusammenarbeit im Team

### Branches erstellen und verwalten

#### Neuen Branch erstellen
```bash
git branch feature-login
```

#### In den Branch wechseln
```bash
git checkout feature-login
```

#### Alle Branches anzeigen
```bash
git branch
```

#### Branch löschen
```bash
git branch -d feature-login
```

#### Branch umbenennen
```bash
git branch -m alter-name neuer-name
```

---

### Arbeiten mit Branches

Typischer Workflow:

1. Neuen Branch erstellen (z. B. für ein neues Feature)
2. Änderungen vornehmen und committen
3. Branch in das Hauptprojekt integrieren (per Merge oder Rebase)
4. Branch löschen, wenn er nicht mehr benötigt wird

Beispiel:
```bash
git switch -c feature-neues-ui
# Code ändern, committen
git add .
git commit -m "Neues UI erstellt"
# Zurück zu main
git switch main
# Merge
git merge feature-neues-ui
```

---

### Merge-Konflikte verstehen und lösen

#### Was ist ein Merge-Konflikt?

Ein **Merge-Konflikt** entsteht, wenn Git zwei Änderungen an derselben Datei und an derselben Stelle nicht automatisch zusammenführen kann.

Beispiel:
- Entwickler A ändert Zeile 10 in `main`
- Entwickler B ändert dieselbe Zeile in `feature-branch`
- Beim Merge kann Git nicht entscheiden, welche Version „richtig“ ist

---

#### Merge-Konflikt erkennen

Beim Versuch zu mergen:
```bash
git merge feature-branch
```

Git meldet:
```
CONFLICT (content): Merge conflict in datei.txt
```

Die betroffene Datei enthält Konfliktmarker:

```plaintext
Version aus aktuellem Branch
```

---

#### Merge-Konflikt lösen

1. Datei öffnen
2. Konfliktstellen manuell bearbeiten
    - Entscheiden, welche Version behalten oder kombinieren
3. Konfliktmarker (`<<<<<<<`, `=======`, `>>>>>>>`) entfernen
4. Änderungen als gelöst markieren:
   ```bash
   git add datei.txt
   ```
5. Merge abschließen:
   ```bash
   git commit
   ```

---

### Tipps zum Vermeiden von Konflikten

- Regelmäßig **pullen** (z. B. `git pull origin main`), um Änderungen frühzeitig zu integrieren
- Kleine, häufige Commits statt großer auf einmal
- Kommunikation im Team über geänderte Dateien
- **Rebase** statt Merge für lineare Historien (wenn sinnvoll):
  ```bash
  git rebase main
  ```

---

### Fazit

Branches sind das Herzstück von Git-Workflows und ermöglichen parallele, saubere Entwicklung.  
Merge-Konflikte sind ein natürlicher Teil dieser Arbeit – mit etwas Sorgfalt und Routine lassen sie sich jedoch leicht erkennen und lösen.


## 4. Git mit IntelliJ/PyCharm benutzen: Local Repository und Remote Repository

Nachdem die grundlegenden Git-Befehle und der Umgang mit Branches erklärt wurden, wird in diesem Abschnitt die Nutzung von Git in Verbindung mit den Entwicklungsumgebungen IntelliJ und PyCharm behandelt.
Dies ermöglicht eine effiziente Verwaltung lokaler und entfernter Repositories direkt aus der IDE.

## Voraussetzungen

Bevor du beginnst, stelle sicher, dass du Folgendes installiert und eingerichtet hast:

- **Git**: Lade Git von [git-scm.com](https://git-scm.com/) herunter und installiere es.
- **IntelliJ** oder **PyCharm**: Wähle die für deine Programmiersprache geeignete IDE von [JetBrains](https://www.jetbrains.com/) und installiere sie.
- **Git-Integration**: Die JetBrains IDEs haben in der Regel Git bereits integriert. Überprüfe in den Einstellungen unter "Version Control > Git", ob der Pfad zur Git-ausführbaren Datei korrekt ist.

## Lokales Repository erstellen

1. **Neues Projekt anlegen**:
- Öffne IntelliJ IDEA oder PyCharm.
- Wähle "File > New > Project" aus dem Hauptmenü.
- Konfiguriere dein Projekt nach Bedarf und klicke auf "Create".

![Beispiel Neues Projekt](https://i.ibb.co/61fYYCp/IJ-Neues-Projekt-small.webp)

*Beispiel: Neues Projekt anlegen*

2. **Git-Initialisierung**:
- Navigiere zu "VCS > Enable Version Control Integration" im Menü.
- Wähle "Git" aus dem Dropdown-Menü und bestätige.
- Alternativ kannst du im Terminal-Fenster der IDE `git init` eingeben.

![Beispiel Git](https://i.ibb.co/HNv3VPX/IJ-VCS-Git-s.webp)

*Beispiel: Menüpunkt "VCS > Enable Version Control Integration > Git"*

3. **Erste Dateien hinzufügen und committen**:
- Erstelle oder bearbeite Dateien in deinem Projekt.
- Du hast nun zwei Möglichkeiten zum Committen:

  a) Über die IDE:
    - Öffne das "Commit"-Fenster (Alt + 0 oder ⌘0).
    - Wähle die zu committenden Dateien aus.
    - Gib eine aussagekräftige Commit-Nachricht ein.
    - Klicke auf "Commit" oder "Commit and Push".

  b) Über das Terminal in der IDE:
    - Öffne das Terminal-Fenster in der IDE.
    - Füge Dateien zum Staging-Bereich hinzu mit:
        - `git add .` (für alle Änderungen) oder
        - `git add <dateiname>` (für spezifische Dateien).
    - Führe den Commit aus mit:
        - `git commit -m "Deine aussagekräftige Commit-Nachricht"`.
    - Optional: Pushe die Änderungen mit:
        - `git push`.

![Beispiel Commit](https://i.ibb.co/rH4n2RH/IJ-commit.webp)

*Beispiel: Commit-Möglichkeit via GUI*

## Remote Repository verbinden

1. **Remote Repository erstellen**:
- Gehe zu GitHub, GitLab oder Bitbucket und erstelle ein neues Repository.
- Kopiere die URL des Remote-Repositories.

![Beispiel Github Copy Remote](https://i.ibb.co/YfMgDG3/GH-copy-URL.webp)

*Beispiel: Copy GitHub URL*

2. **Remote Repository zum lokalen Projekt hinzufügen**:
- In IntelliJ/PyCharm, gehe zu "Git > Manage Remotes".
- Klicke auf das "+"-Symbol, um ein neues Remote hinzuzufügen.
- Gib "origin" als Name ein und füge die kopierte URL ein.
- Bestätige mit "OK".

![Beispiel IntelliJ Manage Remotes](https://i.ibb.co/BfGH1Wd/IJ-manage-remotes.webp)

*Beispiel: Remote Repository hinzufügen*

3. **Änderungen pushen**:
- Wähle "Git > Push" aus dem Menü oder nutze das "Commit"-Fenster.
- Wähle den Branch aus, den du pushen möchtest (meist "master" oder "main").
- Klicke auf "Push", um deine Änderungen zum Remote-Repository zu senden.


## Abschlussbemerkung zu Git mit IntelliJ/PyCharm

Die Integration von Git in IntelliJ/PyCharm bietet eine verknüpfte Versionskontrolle direkt in deiner Entwicklungsumgebung.
Diese Tools vereinfachen den Git-Workflow, indem sie visuelle Unterstützung für Commits, Merges und die Verwaltung von Branches bieten.
Durch die Nutzung der integrierten Funktionen von IntelliJ/PyCharm kannst du effizienter arbeiten und dich besser auf den Code konzentrieren, während du gleichzeitig von den Vorteilen der Versionskontrolle profitierst.


## 5. Nützliche Git-Tools und Plattformen

Git ist ein weit verbreitetes Versionskontrollsystem, das von Entwicklern auf der ganzen Welt genutzt wird, um Projekte zu verfolgen und kollaborativ zu arbeiten. Um die Arbeit mit Git zu erleichtern und zu erweitern, gibt es zahlreiche Tools und Plattformen, die Git und GitHub in verschiedene Bereiche wie Automatisierung, Projektmanagement und Sicherheit integrieren.

Git-Tools und Git-Plattformen sind essenzielle Hilfsmittel für Entwickler, die Git zur Versionskontrolle nutzen. Während Git das zugrunde liegende System für das Verwalten von Codeänderungen darstellt, helfen Tools und Plattformen dabei, die Arbeit mit Git zu vereinfachen und zu erweitern.

Git-Tools sind spezialisierte Anwendungen, die Entwicklern helfen, spezifische Aufgaben mit Git schneller und effizienter zu erledigen. Sie können grafische Benutzeroberflächen bieten (wie GitHub Desktop), die Arbeit über die Kommandozeile vereinfachen (wie die GitHub CLI), oder Automatisierungen und Sicherheitsüberprüfungen einführen (wie Dependabot). Tools sind häufig auf bestimmte Funktionen fokussiert und ergänzen die Git-Arbeit, ohne den gesamten Workflow zu beeinflussen.

Git-Plattformen hingegen bieten eine umfassendere Infrastruktur für die Entwicklung und Zusammenarbeit. Plattformen wie GitHub oder GitLab integrieren Versionskontrolle, Projektmanagement und Automatisierung in einer einzigen Umgebung. Diese Plattformen ermöglichen es Teams, komplexe Entwicklungsprojekte zu verwalten, CI/CD (Continuous Integration/Continuous Deployment) Pipelines zu automatisieren und Software sicher und effizient zu entwickeln.

### Beispiele für Git-Tools

#### GitHub CLI (Command Line Interface)

Die GitHub CLI ermöglicht es Entwicklern, direkt über die Kommandozeile auf GitHub zuzugreifen und Aktionen wie das Erstellen von Pull Requests, das Verwalten von Issues und das Klonen von Repositories durchzuführen. Es ist besonders nützlich für Entwickler, die bevorzugt in der Kommandozeile arbeiten und nahtlos GitHub-Workflows ausführen möchten.

#### GitHub Desktop ist eine Open-Source Anwendung, die dabei hilft mit Code zu arbeiten, der auf GitHub oder anderen Git-Diensten gehostet wird. Die Benutzeroberfläche ermöglicht es, Git-Operationen wie Commit, Branching, Pull Requests und Merges über eine einfache grafische Oberfläche zu steuern. Es ist ideal für GitHub-Nutzer, die nicht auf die Kommandozeile angewiesen sein möchten.

####  GitKraken

GitKraken ist ein visueller Git-Client, der für seine intuitive Benutzeroberfläche bekannt ist. Es unterstützt Git-Operationen wie das Erstellen von Branches, das Durchführen von Merges und das Verwalten von Pull Requests. GitKraken bietet zudem eine einfache Verwaltung von Git-Flow und Integration mit Plattformen wie GitHub, GitLab und Bitbucket.

####  Sourcetree

Sourcetree ist ein weiterer beliebter Git-Client, der von Atlassian entwickelt wurde. Es bietet eine visuelle Benutzeroberfläche zur Verwaltung von Git-Repositories und erleichtert das Verfolgen von Änderungen, das Erstellen von Branches sowie das Beheben von Merge-Konflikten. Sourcetree ist besonders bei Entwicklern beliebt, die auch mit Bitbucket arbeiten, da es nahtlos integriert ist.

#### Fork

Fork ist ein moderner Git-Client für Mac und Windows, der für seine Benutzerfreundlichkeit und Geschwindigkeit bekannt ist. Es unterstützt fortgeschrittene Funktionen wie interaktive Rebase, Stash-Management und visuelle Merge-Konfliktauflösung. Fork ist bei Entwicklern beliebt, die nach einem schnellen und zuverlässigen Git-Client suchen.
Beispiele für Git-Plattformen

#### GitHub

GitHub ist eine der bekanntesten Plattformen für Versionskontrolle und Softwareentwicklung, die auf Git basiert. Sie wird weltweit von Entwicklern verwendet, um Quellcode zu hosten, Änderungen nachzuverfolgen und kollaborativ an Projekten zu arbeiten. GitHub bietet Funktionen wie Pull Requests, Issues zur Fehlerverfolgung und Projektmanagement-Tools. Die Plattform ist bekannt für ihre weit verbreitete Nutzung in der Open-Source-Community und eignet sich sowohl für private als auch öffentliche Projekte. GitHub unterstützt darüber hinaus Continuous Integration/Continuous Deployment (CI/CD) über GitHub Actions, wodurch Arbeitsabläufe automatisiert werden können.

#### GitLab

GitLab ist eine umfassende Plattform für DevOps, die den gesamten Softwareentwicklungsprozess von der Planung bis zur Bereitstellung abdeckt. Neben der Versionskontrolle bietet GitLab Funktionen wie Continuous Integration/Continuous Deployment (CI/CD), die vollständig in die Plattform integriert sind. Dadurch können Entwickler automatisierte Pipelines zur Durchführung von Tests und zur Bereitstellung von Software einrichten. GitLab eignet sich besonders für Teams, die eine zentrale Lösung für alle Phasen der Softwareentwicklung suchen.

#### Bitbucket

Bitbucket ist eine Plattform für die Versionskontrolle, die von Atlassian betrieben wird. Sie ist eng mit anderen Atlassian-Produkten wie Jira und Confluence integriert, was sie zu einer guten Wahl für Teams macht, die bereits in der Atlassian-Umgebung arbeiten. Bitbucket unterstützt Git und ermöglicht es Entwicklern, Pull Requests und Code-Reviews durchzuführen sowie CI/CD-Pipelines (Continuous Integration, Continuous Deployment) direkt in der Plattform zu verwalten. Es ist besonders bei Unternehmen und Teams beliebt, die ihre Softwareentwicklungsprozesse mit anderen Atlassian-Tools koordinieren wollen.

## 6. Wichtige Erkenntnisse für Git-Anfänger

Die Verwendung von Git in der Softwareentwicklung ist unerlässlich, um eine effektive Zusammenarbeit und Versionierung zu gewährleisten. 
Mit den richtigen Werkzeugen und Plattformen können Entwickler ihre Arbeitsabläufe optimieren und die Qualität ihrer Projekte steigern. 
Für Anfänger mag Git zunächst überwältigend erscheinen, doch mit der Zeit wird es einfacher und intuitiver. 
Git ist ein wesentlicher Bestandteil moderner Softwareentwicklung, der es ermöglicht, effektiver im Team zu arbeiten und den Überblick über den Codeverlauf zu behalten.

## 7. CI/CD Pipeline

In diesem Abschnitt erläutern wir das Grundgerüst unserer CI/CD-Pipeline für das **FoodRescue-Projekt**, dabei beleuchten wir den Aufbau, die Konfiguration und welche Tests konkret ausgeführt werden.

### Projekt-Übersicht
- **Backend**: Spring Boot 3.3.4 mit Java 21
- **Build-Tool**: Maven
- **Package**: JAR-Artefakt (`foodrescue-0.0.1-SNAPSHOT.jar`)
- **Projektstruktur**: `backend/` enthält die Spring Boot Anwendung

### Konfigurierte Maven-Plugins
Das Projekt nutzt mehrere Plugins für Qualitätssicherung:

1. **Spring Boot Maven Plugin** - Erstellt ausführbare JAR-Dateien
2. **Spotless** (Version 2.45.0) - Automatische Code-Formatierung mit Google Java Format
3. **JaCoCo** (Version 0.8.13) - Code-Coverage-Analyse während der Testausführung
4. **Maven Surefire** (Version 3.2.5) - Test-Ausführung mit Mockito-Unterstützung

### Unsere Test-Suiten

Das FoodRescue-Backend verfügt über drei verschiedene Test-Kategorien:

#### 1. **ContextLoadsTest** - Integration/Smoke Test
```java
@SpringBootTest
class ContextLoadsTest {
  @Test void contextLoads() {}
}
```
- **Zweck**: Stellt sicher, dass der Spring Application Context erfolgreich startet
- **Testtyp**: Integrationstest

#### 2. **HealthControllerTest** - Controller/API Test
```java
@WebMvcTest(controllers = HealthController.class)
class HealthControllerTest {
  @Test
  void healthEndpointReturnsOk() throws Exception {
    // Testet GET /api/health Endpoint
  }
}
```
- **Zweck**: Testet den Health-Endpoint des REST-Controllers
- **Testtyp**: Controller-Test (mit MockMvc)
- **Technologie**: Verwendet `@WebMvcTest` für Web-Layer-Tests
- **Mocking**: RescueService wird gemockt mit Mockito
- **Validierung**: HTTP-Status 200 und Response-Content "OK"

#### 3. **RescueServiceTest** - Unit Test
```java
class RescueServiceTest {
  @Test
  void filtersNonPerishables() {
    var input = List.of("frische Milch", "Konserven", "Pasta", "frische Beeren");
    var result = service.filterNonPerishables(input);
    assertThat(result).containsExactlyInAnyOrder("Konserven", "Pasta");
  }
}
```
- **Zweck**: Testet die Kern-Business-Logik der `RescueService`-Klasse
- **Testtyp**: Unit Test (ohne Spring-Kontext)

### CI/CD Pipeline-Ablauf/Stufen

#### 1. **Trigger**
   - Der Trigger wird automatisch ausgelöst bei Push/Pull Request auf `main` oder `develop` Branches
   - Kann auch manuell über GitHub Actions ausgelöst werden

#### 2. **Checkout**
   - Repository wird ausgecheckt
   - Alle Quellcode-Dateien werden bereitgestellt

#### 3. **Setup**
   - Java 21 JDK wird installiert
   - Maven Dependencies werden aus dem Cache geladen (falls vorhanden)

#### 4. **Build & Tests**
   - Maven-Build mit allen Plugins wird ausgeführt:
     ```bash
     mvn -B clean verify
     ```
   - **Ausgeführte Tests**:
     - Unit-Tests: `RescueServiceTest` (schnelle Logik-Tests)
     - Controller-Tests: `HealthControllerTest` (API-Layer-Tests mit MockMvc)
     - Integrationstests: `ContextLoadsTest` (Spring-Kontext-Validierung)
   
   - **Code-Formatierung**: Spotless überprüft Google Java Format-Konformität
   - **Coverage-Report**: JaCoCo generiert Coverage-Bericht in `target/site/jacoco/`

#### 5. **Code-Qualität**
   - **JaCoCo Coverage**: Misst Testabdeckung des Codes
     - Report verfügbar unter: `backend/target/site/jacoco/index.html`
   - **Spotless**: Validiert Code-Formatierung
     - Bei Fehlern: `mvn spotless:apply` zum Beheben

#### 6. **Package & Artefakte**
   - Erzeugung des ausführbaren JARs: `foodrescue-0.0.1-SNAPSHOT.jar`
   - Artefakt-Location: `backend/target/`

#### 7. **Zukünftige Deployment Gedanken** 
   - Automatisches Deployment in Test-/Staging-Umgebung
   - Produktion-Deployment nach manueller Freigabe
