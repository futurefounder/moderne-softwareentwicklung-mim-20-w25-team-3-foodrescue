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

## 4. Best practices und Prinzipien für Clean Code und besseres entwicklen von Code

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

### Felder sollen den Zustand definieren

**BP** = Felder sollten einen stabilen Zustand eines Objektes repräsentieren

- durch lokale Variablen
- durch Extrahieren einer Klasse, die temporäre Zustände berechnet oder verwendet

### eine korrekte Behandlung von Exceptions verwenden

**BP** = Exceptions sollten :  

- so spezifisch wie möglich abgefangen werden
- auf Ebene auf der man sinvoll damit umgehen kann
- sauber verarbeiten werden
- nicht für den normalen Kontrollfluss verwendet werden

### Duplizierten Code vermeiden

**BP** = Don't repeat yourself

**Smell** = bläht die Codebasis auf und sorgt für erhöhten Wartungsaufwand und Inkonsistenz. 
Dies verschlechtert die Verständlichkeit und macht Fehler wahrscheinlicher

### Refactoring

**BP** = den Refactoring Katalog kennen und die entsprechenden Menüs in der IDE kennen, sowie anwenden können

## Code und dessen Qualität

### Lokale Deklarationen verwenden

**BP** = dort deklarieren wo die Variable verwendet wird und Sammlungen von Deklarationen am Blockanfang vermeiden

### selbsterklärende Variablen verwenden

**BP** = eine ausreichende Anzahl an selbsterklärenden Variablem verwenden

**Smell** = dadurch sind einzelen Schritte verständlich, gut lesbar und besser wartbar

### korrekte Verwendung von Verschachtelungen

**BP** = verschachtelter Code sollte zunehmend spezifische Aufgaben übernehmen und auf höherer Ebene soltle die Abstraktion oder Wahrscheinlichkeit eines Aufrufes höher sein

### Multi-Thread Code trennen

**BP** = Multi- Thread code sollte nicht mit normalen code vermischt werden und ein Thread sollte in eine eigene Klasse gekapselt werden

### Conditionals kapseln

**BP** = prüfen ob es sinnvoll ist komplexe Logik zu kapseln

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


### Tennung von Interfaces

**BP** = Die Abhängigkeiten von Interfaces sollte minimal sein und Interfaces sollten getrennt und gezielt zugeschnitten werden

- besser kleines und stabiles Interface ableiten, welches verwendet werden kann


### Versteckte Informationen

**BP** = Klassen sollten nur das exponieren, was andere unbedingt wissen müssen, außer unsichtbare Attribute und interne Details sind ausdrücklich gewünscht

### Inversion of Control

**BP** = die Steuerung der Ablauflogik  sollte umgedreht ablaufen

- das Framework ruft die Komponente auf


### Dependency Injection

**BP** = Abhängigkeiten sollten von außen in die Klasse gegeben werden anstatt von der Klasse selbst erzeugt zu werden

### Open Colsed Principle

**BP** = Komponenten sollten offen für Erweiterungen sein, aber geschlossen für Modifikationen

### Einfaches und komplexes Refactoring

**BP** = der Code wird so umgestaltet, dass sein Verhalten unverändert bleibt indem er durch Tests abgesichert ist und 
leicht Rückgängig zu machen ist. 


### Alle Basic und Enterprise Patterns prüfen

- Strategy
- Factory
- Adapter
- Strukturpattern
- Kontrollfluss
- Aufteilung der komponenten
- DAtenbankzugriff
- 
### Die Implementierung sollte den Entwurf wiederspiegeln

**BP** = Implementierung und Entwurf wie UML-Diagramm oder DDD-Darstellung dürfen nicht auseinander driften

## Packages

### Common Closure und Common Resue
- Packages bzw. eine Klasse sollte so gestaltet werden, dass sie nicht geändert werden muss, aber erweritert werden kann
- Klassen die gemeinsam geändert uoder gemeinsam wiederverwendet werden, gehören in das selbe Package

### Visualisierung von Paketabhängigkeiten
- Packages sollten nur nach rechts oder unten zugreifen, aber nie nach oben oder links.

### Paketabhängigkeiten müssen Zyklusfrei sein
- bereits wenige Zyklen reduzieren die Andelbarkeit und Evolierbarkeit großer Softwaresysteme dramatisch

### Stabile Abhängigkeiten
- instabile Pakete sollten von stabileren Paketen abhängig sein

### Stabile Abstraktionen
- je stabiler ein Paket desto abstrakter sollte es sein

## Produktivität

### Beachten der Pfadregel

**BP** = jeder Code wird besser hinterlassen wie man ihn zu Beginn vorgefunden hat

### Die Ursache bekämpfen und nicht die Wirkung

**BP** = Es ist effektiver nach der Ursache zu suchen anstatt die Nebenwirkungen zu beseitigen

### Komponentenorientiertes denken

**BP** = Komponenten bzw. Domains auf kleiner Ebene bietet lose Kopplung und hohe kohäsion

### keine goldenen Wasserhähne

**BP** = Funktionalitäten sollten dem Kernziel dienen, sonst wird das System aufgeblasen

### Prinzip der kleinstmöglichen Überraschung

### System für die Versionskontrolle verwenden

### Issues und Fehler werden öffentlich verwaltet

### Effektives Buildmanagement verwenden

### CI/CD mit kurzer Cycle-Time 

## Management

### Ablenkungen ausschalten

- für konzentriertes Arbeiten Ablenkungen ausschalten und beiseite legen

### Fast Launcher
- imemr einen schnellen Launcher für Anwendungen und Weblinks zur Hand haben

### Tastenkombinationen und IDE shortcuts beherrschen
- ständiger wechsel zwischen Maus und Tastatur stört den Arbeitsfluss und kostet Zeit

### mehrere Bildschrime oder Virtuelle Desktops

### Scripting Profi
- schreiben von Macros und Shellskripts
- 
### UNIX nutzen

### den besten Editor und die beste IDE nutzen

### Das Rad nicht neu erfinden
- die Bibliotheken kennen

### Iterative Entwicklung wie Scrum verwenden

### lesen, öesen, lesen und sich Weiterbilden

### Events besuchen und von den Meistern lernen

### Sein Wissen teilen