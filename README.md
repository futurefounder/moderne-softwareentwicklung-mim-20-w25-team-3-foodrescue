# Inhaltsverzeichnis
**1. Git: Programminformationen und Vorteile bei der Nutzung**

**2. Grundlegende Git-Befehle**

**3. Branches und ihre Nutzung und Umgang mit Merge-Konflikten**

**4. Kombination von Git mit IntelliJ/PyCharm: Local Repository und Remote Repository**

**5. Nützliche Git-Tools und Plattformen**

**6. Schlussfolgerungen für Git-Anfänger**

## 1. Git: Programminformationen und Vorteile bei der Nutzung

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


## 3. Branches und ihre Nutzung und Umgang mit Merge-Konflikten

## 4. Git mit IntelliJ/PyCharm benutzen: Local Repository und Remote Repository

## 5. Nützliche Git-Tools und Plattformen

## 6. Wichtige Erkenntnisse für Git-Anfänger

## Aufgabenverteilung

| Aufgaben                                                                                | Bearbeiter |
|-----------------------------------------------------------------------------------------|------------|
| Git Repository auf GitHub anlegen, Team einladen, .gitignore erstellen, Readme Struktur | Jesse      |
| Was ist Git und warum sollte es verwendet werden?                                       |            |
| Grundlegende Git-Befehle (z. B. git init, git add, git commit, git push)                |            |
| Branches und ihre Nutzung, Umgang mit Merge-Konflikten                                  |            |
| Git mit IntelliJ/PyCharm benutzen: Local Repository und Remote Repository               |            |
| Nützliche Git-Tools und Plattformen (z. B. GitHub)                                      |            |