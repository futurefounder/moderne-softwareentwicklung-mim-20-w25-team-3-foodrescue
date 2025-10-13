# Inhaltsverzeichnis
**1. Git: Programminformationen und Vorteile bei der Nutzung**

**2. Grundlegende Git-Befehle**

**3. Branches und ihre Nutzung und Umgang mit Merge-Konflikten**

**4. Kombination von Git mit IntelliJ/PyCharm: Local Repository und Remote Repository**

**5. Nützliche Git-Tools und Plattformen**

**6. Schlussfolgerungen für Git-Anfänger**

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

## 4. Git mit IntelliJ/PyCharm benutzen: Local Repository und Remote Repository

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

## Aufgabenverteilung

| Aufgaben                                                                                | Bearbeiter |
|-----------------------------------------------------------------------------------------|------------|
| Git Repository auf GitHub anlegen, Team einladen, .gitignore erstellen, Readme Struktur | Jesse      |
| Was ist Git und warum sollte es verwendet werden?                                       | Thomas     |
| Grundlegende Git-Befehle (z. B. git init, git add, git commit, git push)                |            |
| Branches und ihre Nutzung, Umgang mit Merge-Konflikten                                  |            |
| Git mit IntelliJ/PyCharm benutzen: Local Repository und Remote Repository               |            |
| Nützliche Git-Tools und Plattformen (z. B. GitHub)                                      | Stephan    |