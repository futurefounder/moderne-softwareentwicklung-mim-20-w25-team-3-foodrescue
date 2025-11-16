# Clean Code Developement

Inhaltsverzeichnis

**1. Was versteht man unter Clean Code ?**

**2. Was versteht man unter technical debt und wie denken Manager und Entwickler darüber ?**

**3. Motivation Clean Code anzuwenden**

**4. Best practices für Clean Code**

**5. Clean Code und Generative KI**

**6. Schlussfolgerungen für Git-Anfänger**

**7. Fazit**


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

