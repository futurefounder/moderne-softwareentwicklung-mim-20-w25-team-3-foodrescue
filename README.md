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
<<<<<<< HEAD
Version aus aktuellem Branch
=======
Version aus feature-branch
>>>>>>> feature-branch
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

## 5. Nützliche Git-Tools und Plattformen

## 6. Wichtige Erkenntnisse für Git-Anfänger

## Aufgabenverteilung

| Aufgaben                                                                                | Bearbeiter |
|-----------------------------------------------------------------------------------------|------------|
| Git Repository auf GitHub anlegen, Team einladen, .gitignore erstellen, Readme Struktur | Jesse      |
| Was ist Git und warum sollte es verwendet werden?                                       | Thomas     |
| Grundlegende Git-Befehle (z. B. git init, git add, git commit, git push)                |            |
| Branches und ihre Nutzung, Umgang mit Merge-Konflikten                                  | Thomas     |
| Git mit IntelliJ/PyCharm benutzen: Local Repository und Remote Repository               |            |
| Nützliche Git-Tools und Plattformen (z. B. GitHub)                                      |            |