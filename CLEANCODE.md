# Clean Code Developement

Inhaltsverzeichnis

**1. Was versteht man unter Clean Code ?**

**2. Was versteht man unter technical debt und wie denken Manager und Entwickler darüber ?**

**3. Motivation Clean Code anzuwenden**

**4. Grundlegende Prinzipien der Programmierung**

**5. Best practices für Clean Code und Beispiele aus dem Projekt**

**6. Clean Code und Generative KI**

**6. Fazit**


## 1. Was versteht man unter Clean Code ?

**Clean Code** ist der verantwortungsvolle Umgang mit Code, der als Gestaltung von gutem code beziehungsweise guter Software zu verstehen ist.
Hierbei ist nicht nur der Code an sich relevant, sondern auch die Architektur und der gesamte Aufbau des Softwareprojektes.

## 2. Was versteht man unter technical debt und wie denken Manger und Entwickler darüber ?

**Technical debt** bezeichnet die Gefahr, die Probleme und die Folgen, welche aus schlecht geschrieben Code entstehen.
- Manager, Unternehmen und Entwickler sind sich der Bedeutung eines hohen technical debt(technische Schuld) nicht bewusst.

### Manager
- durch den Lock-In-Effekt sind Kunden 10 Jahre oder länger an das Produkt gebunden.

Daher werden Entscheidungen auf Grund von folgender Einordnung gegen gutes Design getroffen.

TODO(image einfügen)

- schnelles Geld verdienen und bewusst rücksichtslos sein
- Unwissende, die  Qualität, Codequalität, Design und technical debt nicht verstehen
- der Gefahr von technical debt nicht bewusst sein und das Problem verschieben
- Qualität ist von anfang an im blick und Fehler werden erkannt, *aber* es wird gehofft später die Zeit dafür zu finden.

### Entwickler

Entwickler arbeiten jedoch mit dem folgenden Wissen:

- sie müssen Manager erst davon Überzeugen, dass ein geringer technical debt ein Vorteil ist
- das Wissen, dass Code zu 90% gelesen wird und nicht nur von einer Person
- das Wissen, dass die Wartung von Code und die Erweiterung mehr als die Gesamtkosten von Software ausmachen

## 3. Motivation Clean Code anzuwenden

### Das Ziel von Clean Code

Das Ziel ist die **Entwicklung von guten, veränderbaren, nachhaltigen und lesbaren Code** durch die Umsetzung von **Best Practices, Coding Guidelines** und Regeln.
Aber auch die Entwickler zu ermutigen, durch die Anwendung von Clean Code den **Code so zu entwickeln**, dass dessen **Qualität so gut ist**, dass der **technical debt minimal** bleibt.

Das Ziel ist ein gutes Design statt kein Design

### Die Motivation Clean Code anzuwenden

Jeder kennt das Problem von schlechten Code, aber keiner bekämpft es an der Wurzel, da das Wissen nicht kohärent, sondern selektiv existiert. 

Die Probleme sind dabei folgende:

- die **Unwissenheit der Entwickler** 
- der **Marktdruck schnell "irgendetwas" zu liefern**
- die **Wartbarkeit, Lesbarkeit, Testbarkeit** und **Änderbarkeit** von Code nimmt zu

Dies macht den Code nicht mehr kontrollierbar und **80% der Gesamtkosten** sind für das **Debuggen** und die **Wartung**.

Der Aufwand für das Einführen neuer Features steigt.

Der Break-Even-Point, an dem es sich nicht mehr lohnt den technical debt zu erhöhen, weil man mit dem Produkt kein Geld mehr verdienen kann, ist schneller erreicht.

Die Vorteile & Nachteile von der Verwendung von gutem Design oder keinem Design sind folgende:

kein Design:
- zu Beginn schneller mehr Funktionalitäten
- im weiteren Verlauf sinkt die Produktivität aufgrund schlechter Wartbarkeit
- der Aufwand für neue Features steigt
- die Projekte scheitern dadurch


gutes Design:
- zu Beginn weniger Funktionalitäten
- die Produktivität bleibt erhalten
- neue Features können besser implementiert werden

### Dies gelingt durch Prinzipen die in Form von Best Practices und Smells, dem wissen was passiert, wenn man die Prinzipen und Regeln nicht einhält, gelebt werden müssen.

## 4. Grundlegende Prinzipien der Programmierung

### Die bekanntesten Prinzipien unter Softwareentwicklern sind:
- DRY = Don't repeat yourself.
- KISS = Keep it simple, stupid.

Diese besagen folgendes:

- Copy and Paste sollte vermieden werden, da dies eine der häufigsten Fehlerquellen ist
- sowohl die Struktur als auch die Menge an Code ist einfach zu halten und sollte später erweiterbar sein 

### Weitere Prinzipien sind: 

- zu frühe Optimierungen vermeiden
- Doing vs. Calling Code
- konfigurierbare Daten auf hoher Ebene
- Vorsicht bei Konfigurationen
- versteckte zeitliche Kopplung vermeiden
- nicht willkürlich sein

## 5. Best practices und Prinzipien für Clean Code und besseres Entwicklen von Code

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

**BP** = Wenn Code tatsächlich zur späteren Aufgabe gehört, wird dieser mit einem TODO() versehen und in die Backlogliste eingetragen.

**Smell** = Ungenutzer & zweckloser Code, der setehen bleibt, verringert die Code Coverage.

### Nutzlose Kommentare entfernen

**BP** = Der Code sollte mindestens auf Paket- oder Klassenebene ausreichend dokumentiert sein.

Wobei es zwei Meinungen gibt: 
- alles muss Dokumentiert werden
- der Code muss klar lesbar geschrieben sein

**Smell** = Dies dient für den abstrakten Einstieg in den Quellcode.

### Präzise Bennenungen

**BP** = Es sollten selbsterklärende Namen verwendet werden, die nicht zu lang sind und es sollte eine konsistente Benennung beibehalten werden.

- Klasse: beschreibe die Art der Implementierung
- Methode: beschreibe was sie macht und niemals wie sie es macht
- Interface: beschreibe die Funktionalität die sie abstrahiert
- Nebenwirkungen: der Name muss die Nebenwirkung ausdrücken
- kein Typ/ kein Scope: dies muss im Namen stehen

**Smell** = Der Code sollte sich wie Prosa lesen.

### Magic Numbers vermeiden

**BP** = Es sollte genau deutlich gemacht werden, was die Zahlen bedeuten.

**Smell** = Es wird viel Zeit verloren, um Magic Numbers zu verstehen.

### Keine Seiteneffekte erzeugen

**BP** = Eine Funktion sollte keine Seiteneffekte erzeugen.

- andere Module verlassen sich auf Variablen, diese sollten nicht plötzlich negative Zahlen haben und für Fehler sorgen

### Felder sollen den Zustand definieren

**BP** = Felder sollten einen stabilen Zustand eines Objektes repräsentieren.

- durch lokale Variablen
- durch Extrahieren einer Klasse, die temporäre Zustände berechnet oder verwendet

### Korrekte Behandlung von Exceptions verwenden

**BP** = Exceptions sollten folgendes beachten:  

- so spezifisch wie möglich abgefangen werden
- auf einer Ebene sein, auf der man sinvoll damit umgehen kann
- sauber verarbeitet werden
- nicht für den normalen Kontrollfluss verwendet werden

### Duplizierten Code vermeiden

**BP** = DRY steht für "Don't repeat yourself" und bedeutet, dass man selbst geschrieben Code nicht wiederholen sollte.

**Smell** =  Dies bläht die Codebasis auf und sorgt für erhöhten Wartungsaufwand und Inkonsistenz. Es wird ebenfalls die Verständlichkeit verschlechtert und macht Fehler wahrscheinlicher.

### Refactoring

**BP** = Man sollte den Refactoring Katalog kennen und die entsprechenden Menüs in der IDE beherrschen.

## Code und dessen Qualität

### Lokale Deklarationen verwenden

**BP** = Es sollte dort deklariert werden, wo die Variable verwendet wird und eine Sammlungen von Deklarationen am Blockanfang vermieden werden.

### Selbsterklärende Variablen verwenden

**BP** = Es sollte eine ausreichende Anzahl an selbsterklärenden Variablen verwendet werden und nicht zu viele die überflüssig sind.

**Smell** = Dadurch sind die einzelnen Schritte verständlich, gut lesbar und besser wartbar

### Korrekte Verwendung von Verschachtelungen

**BP** = Verschachtelter Code sollte zunehmend spezifische Aufgaben übernehmen und auf höherer Ebene sollte die Abstraktion oder Wahrscheinlichkeit eines Aufrufes höher sein.

### Multi-Thread Code trennen

**BP** = Multi- Thread Code sollte nicht mit normalem Code vermischt werden und ein Thread sollte in eine eigene Klasse gekapselt werden.

### Conditionals kapseln

**BP** = Es sollte geprüft werden, ob es sinnvoll ist komplexe Logik zu kapseln.

### Negative Bedingungen Vermeiden

**BP** = Es sollten negative Ausdrücke vermieden werden.

**Smell** = Der Code ist schwer zu lesen und zu verstehen.

### Randbedingungen kapseln

**BP** = Randbedingungen für Schleifen usw. sollte man deutlich erkennbar machen und an einer Stelle gebündelt implementieren.

**Smell** = Dies erschwert unnötig das Refactoring.


### Architektur und Klassendesign

### stärkere Kopplung bei Vererbung als bei Instanzen

**BP** = Es sollte Besser instantiiert werden als zu vererben.

**Smell** = Dies erschwert die Testbarkeit und Austauschbarkeit von Komponenten.

### Single Responsibility Principle

**BP** = Eine Klasse bzw. Modul sollte genau eine Verantwortung haben.

**Smell** = Viele fachliche Abhängigkeiten durch unterschiedliche Aufgaben in einer Klasse machen die Änderungen komplex und fehleranfällig.

### einzelne Querschnittsaufgaben

**BP** = Eine fachliche Methode sollte nicht mit Querschnittsaufgaben wie Persistenz, Logging, Sicherheit und Monitoring zugemüllt werden.


### Tennung von Interfaces

**BP** = Die Abhängigkeiten von einem Interfaces sollte minimal sein und Interfaces sollten getrennt und gezielt zugeschnitten werden.

- besser kleines und stabiles Interface ableiten, welches verwendet werden kann


### Versteckte Informationen

**BP** = Klassen sollten nur das exponieren, was andere unbedingt wissen müssen, außer unsichtbare Attribute und interne Details sind ausdrücklich gewünscht.

### Inversion of Control

**BP** = Die Steuerung der Ablauflogik sollte umgedreht ablaufen.

- das Framework ruft die Komponente auf


### Dependency Injection

**BP** = Abhängigkeiten sollten von außen in die Klasse gegeben werden anstatt von der Klasse selbst erzeugt zu werden.

### Open Colsed Principle

**BP** = Komponenten sollten offen für Erweiterungen sein, aber geschlossen für Modifikationen.

### Einfaches und komplexes Refactoring

**BP** = Der Code sollte so umgestaltet werden, dass sein Verhalten unverändert bleibt indem er durch Tests abgesichert ist und 
leicht Rückgängig zu machen ist. 


### Alle Basic und Enterprise Patterns prüfen

- Strategy
- Factory
- Adapter
- Strukturpattern
- Kontrollfluss
- Aufteilung der komponenten
- DAtenbankzugriff


### Die Implementierung sollte den Entwurf wiederspiegeln

**BP** = Die Implementierung und der Entwurf wie UML-Diagramme oder DDD-Darstellungen dürfen nicht auseinander driften.

## Packages

### Common Closure und Common Resue
- Packages bzw. eine Klasse sollte so gestaltet werden, dass sie nicht geändert werden muss, aber erweritert werden kann
- Klassen die gemeinsam geändert oder gemeinsam wiederverwendet werden, gehören in das selbe Package

### Visualisierung von Paketabhängigkeiten
- Packages sollten nur nach rechts oder unten zugreifen, aber nie nach oben oder links.

### Paketabhängigkeiten müssen Zyklusfrei sein
- bereits wenige Zyklen reduzieren die Wandelbarkeit und Evolierbarkeit großer Softwaresysteme dramatisch

### Stabile Abhängigkeiten
- instabile Pakete sollten von stabileren Paketen abhängig sein

### Stabile Abstraktionen
- je stabiler ein Paket desto abstrakter sollte es sein

## Produktivität

### Beachten der Pfadregel

**BP** = Jeder Code sollte besser hinterlassen werden, wie man ihn zu Beginn vorgefunden hat.

### Die Ursache bekämpfen und nicht die Wirkung

**BP** = Es ist effektiver nach der Ursache zu suchen, anstatt die Nebenwirkungen zu beseitigen.

### Komponentenorientiertes denken

**BP** = Komponenten bzw. Domains auf kleiner Ebene bieten eine lose Kopplung und eine hohe Kohäsion.

### keine goldenen Wasserhähne

**BP** = Funktionalitäten sollten dem Kernziel dienen, sonst wird das System aufgeblasen.

### Prinzip der kleinstmöglichen Überraschung

### System für die Versionskontrolle verwenden

### Issues und Fehler werden öffentlich verwaltet

### Effektives Buildmanagement verwenden

### CI/CD mit kurzer Cycle-Time 

## Management

### Ablenkungen ausschalten

- für konzentriertes Arbeiten sollten Ablenkungen ausgeschaltet und beiseite gelegt werden

### Fast Launcher
- immer einen schnellen Launcher für Anwendungen und Weblinks zur Hand haben

### Tastenkombinationen und IDE shortcuts beherrschen
- ständiger Wechsel zwischen Maus und Tastatur stört den Arbeitsfluss und kostet Zeit

### mehrere Bildschrime oder Virtuelle Desktops

### Scripting Profi
- schreiben von Macros und Shellskripts

### UNIX nutzen

### den besten Editor und die beste IDE nutzen

### Das Rad nicht neu erfinden
- die Bibliotheken kennen und nutzen

### Iterative Entwicklung wie Scrum verwenden

### lesen, lesen, lesen und sich Weiterbilden

### Events besuchen und von den Meistern lernen

### Sein Wissen teilen


## 6. Clean Code und Generative KI

Es gibt 4 Bereiche wo generative KI aktiv genutz werden kann

**1. zur Überwachung von Code**

**2. sich spontane Vorschläge geben lassen**

**3. auf Anfrage zum Druchführen einer Code-Analyse**

**4. zum Durchführen eines Refactoring**