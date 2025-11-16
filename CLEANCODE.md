# Clean Code Developement

Inhaltsverzeichnis

**1. Was versteht man unter Clean Code ?**

**2. Was versteht man unter technical debt und wie denken Manager und Entwickler darüber ?**

**3. Motivation Clean Code anzuwenden**

**4. Best practices für Clean Code und Beispiele aus dem Projekt**

**5. Clean Code und Generative KI**

**6. Fazit**


## 1. Was versteht man unter Clean Code ?

**Clean Code** ist der verantwortungsvolle Umgang mit Code, der als Gestaltung von gutem code beziehungsweise guter Software zu verstehen ist.
Hierbei ist nicht nur der Code an sich relevant sondern auch die Architektur und der gesamte aufbau des Softwareprojektes.

## 2. Was versteht man unter technical debt und wie denken Manger und Entwickler darüber ?

**Technical debt** bezeichnet die Gefahr, die Probleme und die Folgen, welche aus schlecht geschrieben Code entstehen.
- Manager, Unternehmen und Entwickler sind sich der Bedeutung eines hohen technical debt( technische Schuld) nicht bewusst.

### Manager
- durch den Lock-In-Effekt sind Kunden 10 Jahre oder länger an Produkt gebunden.

Daher sind Entscheidungen auf folgenden Grundlagen gegen gutes Design.

- schnelles Geld verdienen und bewusst rücksichtslos
- Unwissende, die  Qualität, Code Qualität, Design und technical debt nicht verstehen
- der Gefahr von technical debt nicht bewusst sein und das Problem verschieben
- Qualität ist von anfang an im blick und Fehler werden erkannt, *aber* es wird gehofft später die Zeit dafür zu finden.

### Entwickler

Entwickler arbeiten jedoch mit folgendem wissen

- sie müssen Manager erst davon Überzeugen, dass ein geringer technical debt ein Vorteil ist
- das Wissen, dass Code zu 90% gelesen wird und das nicht nur von einer Person
- das Wissen, dass die Wartung von Code und die ERweiterung mehr als die Gesamtkosten von software ausmachen

## 3. Motivation Clean Code anzuwenden

### Das Ziel von Clean Code

Die **Entwicklung von guten, veränderbaren, nachhaltigen und lesbaren Code** durch die Umsetzung von **Best Practices, Coding Guidelines** und Regeln.
Aber auch die entwickler zu ermutigen durch die Anwendung von Clean Code den **Code so zu entwickeln**, dass dessen **Qualität so gut ist**, dass die **technical debt minimal** bleibt

Das Ziel ist ein gutes Design statt kein Design

### Die Motivation Clean Code anzuwenden

Jeder kennt das Problem von schlechten Code, aber keiner bekämpft es an der Wurzel, da das wissem nicht kohärent sondern selektiv existiert. 

Die Probleme sind:

- **Unwissenheit der Entwickler** 
- **Marktdruck schnell "irgendetwas" zu liefern**
- die **Wartbarkeit, Lesbarkeit, Testbarkeit** und **Änderbarkeit** von code nimmt zu

dies macht den Code nicht mehr kontrollierbar und **80% der Gesamtkosten** sind für das **Debuggen** und die **Wartung**.

Der Aufwand für das Einführen neuer Features steigt.

Der Break-Even-Point, an dem es sich nicht mehr lohnt die technical debt zu erhöhen, weil man mit dem Produkt kein Geld mehr verdienen kann, ist schneller erreicht.

Vorteile & Nachteile von der Verwendung von gutem Design oder keinem Design

kein Design:
- zu Beginn schneller mehr Funktionalitäten
- im weiteren Verlauf sinkt die Produktivität aufgrund schlechter Wartbarkeit
- Aufwand für neue Features steigt
- Projekte scheitern dadurch


gutes Design:
- zu Beginn langsamer mehr Funktionalitäten
- Produktivität bleibt erhalten
- neue Features können besser implementiert werden

### Dies gelingt durch Prinzipen die gelebt werden müssen in Form von Best Practices und Smells, dem wissen was passiert wenn man die Prinzipen und Regeln nicht einhält.

## 4. Best practices für Clean Code

Im folgenden werden Bestpractices und dazugehörige Smells für folgende Bereiche erläutert

- Programmierprinzipien
- Grundlagen
- Code Basics
- Architektur und Klassendesign
- Packages
- Produktivität
- Management

## Grundlagen

### Nutzlosen code entfernen

**BP** = Wenn Code tatsächlich zur späteren Aufgabe gehört wird dieser mit TODO() versehen und in die Backlogliste eingetragen

**Smell** = ungenutzer & zweckloser Code der setehen bleibt verringert die Code Coverage

### Nutzlose kommentare entfernen

**BP** = Code mindestens auf Paket- oder Klassenebene ausreichend dokumentieren

Wobei es zwei Meinungen gibt: 
- alles muss Dokumentiert werden
- der Code muss klar geschrieben sein

**Smell** = für abstrakten einstieg in den Quellcode

### Präzise Bennenungen

**BP** = selbsterklärende Namen verwenden die nicht zu lang sind und eine konsistente Benennung beibehalten

- Klasse: beschreibe die Art der Implementierung
- Methode: beschreibe was sie macht und niemals wie sie es macht
- Interface: beschreibe die Funktionalität die sie abstrahiert
- Nebenwirkungen: der Name muss diese ausdrücken
- kein Typ/ kein Scope: muss im Namen stehen

**Smell** = der code sollte sich wie Prosa lesen

### Magic Numbers vermeiden

**BP** = genau deutlich machen was Zahlen bedeuten

**Smell** = viel Zeit wird verloren um Magic numbers zu verstehen

### Keine Seiteneffekte erzeugen

**BP** = eine Funktion darf keine Seiteneffekte erzeugen

- andere Module verlassen sich auf Variablen, die plötzlich negative Zahlen haben und für Fehler sorgen

**Smell** = 

### Felder sollen den Zustand definieren

**BP** = Felder sollten einen stabilen Zustand eines Objektes repräsentieren

- durch lokale Variablen
- durch Extrahieren einer Klasse, die temporäre Zustände berechnet oder verwendet

**Smell** =

### eine korrekte Behandlung von Exceptions verwenden

**BP** = Exceptions sollten :  

- so spezifisch wie möglich abgefangen werden
- auf Ebene auf der man sinvoll damit umgehen kann
- sauber verarbeiten werden
- nicht für den normalen Kontrollfluss verwendet werden

**Smell** =

### Duplizierten Code vermeiden

**BP** = Don't repeat yourself

**Smell** = bläht die Codebasis auf und sorgt für erhöhten Wartungsaufwand und Inkonsistenz. 
Dies verschlechtert die Verständlichkeit und macht Fehler wahrscheinlicher

### Refactoring

**BP** = den Refactoring Katalog kennen und die entsprechenden Menüs in der IDE kennen, sowie anwenden können

**Smell** =

## Code und dessen Qualität

### Lokale Deklarationen verwenden

**BP** = dort deklarieren wo die Variable verwendet wird und Sammlungen von Deklarationen am Blockanfang vermeiden

**Smell** =

### selbsterklärende Variablen verwenden

**BP** = eine ausreichende Anzahl an selbsterklärenden Variablem verwenden

**Smell** = dadurch sind einzelen Schritte verständlich, gut lesbar und besser wartbar

### korrekte Verwendung von Verschachtelungen

**BP** = verschachtelter Code sollte zunehmend spezifische Aufgaben übernehmen und auf höherer Ebene soltle die Abstraktion oder Wahrscheinlichkeit eines Aufrufes höher sein

**Smell** =

### Multi-Thread Code trennen

**BP** = Multi- Thread code sollte nicht mit normalen code vermischt werden und ein Thread sollte in eine eigene Klasse gekapselt werden

**Smell** =

### Conditionals kapseln

**BP** = prüfen ob es sinnvoll ist komplexe Logik zu kapseln

**Smell** =

### Negative Bedingungen Vermeiden

**BP** = negative ausdrücke vermeiden

**Smell** = schwer zu lesen und zu verstehen

### Randbedingungen kapseln

**BP** = Randbedingungen für Schleifen usw. deutlich erkennbar machen und an einer Stelle gebündelt implementieren

**Smell** = erschwert unnötig das Refactoring


### Architektur und Klassendesign

### stärkere Kopplung bei Vererbung als bei Instanzen

**BP** = Besser instantiieren als vererben

**Smell** = erschwert Testbarkeit und Austauschbarkeit von Komponenten

### Single Responsibility Principle

**BP** = eine Klasse / Modul soll genau eine Verantwortung haben

**Smell** = viele fachliche Abhängigkeiten durch unterschiedliche Aufgaben in einer Klasse machen Änderungen komplex und fehleranfällig

### einzelne Querschnittsaufgaben

**BP** = eine fachliche Methode sollte nicht mit Querschnittsaufgaben wie Persistenz, Logging, Sicherheit und Monitoring zugemüllt werden

**Smell** =

### Tennung von Interfaces

**BP** = Die Abhängigkeiten von Interfaces sollte minimal sein und Interfaces sollten getrennt und gezielt zugeschnitten werden

- besser kleines und stabiles Interface ableiten, welches verwendet werden kann

**Smell** = 

### Conditionals kapseln

**BP** =

**Smell** =

### Packages

### Produktivität

### Management