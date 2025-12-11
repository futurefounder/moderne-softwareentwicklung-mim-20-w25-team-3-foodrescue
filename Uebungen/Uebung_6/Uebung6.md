# Übung :6 Aspect Orientierte Programmierung

## 1. Backend-Weiterentwicklung

## 2. Frontend-Weiterentwicklung

## 3. Aspect Oriented Programming(AOP)

### AOP = Aspektorientierte Programmierung

-	Querschnitssfunktionen aus dem normalen Code auslagern und zentral kapseln
-	statt in jeder Methode Logging, Security-Checks oder Transaktionen einzubauen werdenn Aspekte definiert die automatisch dazwischen funken wenn bestimmte  Methoden aufgerufen werden


### Cross-Cutting-Concerns

-	Teile einer Software die viele Bereiche gleichzeitig betreffen, aber nicht direkt zur Fachlogik einzelner Klassen oder Funktionen gehören. (zb. Logging, Exception Handling, Security/Authentifizierung, Datenbankanwendungen für ganze Use Cases, Caching, Monitoring/Metriken)


### Join Points &

-	der konkrete Punkt (im  Code) an dem das Aufrufereignis stattfindet bzw ein Punkt im Programmablauf den man abfangen kann zb( Methodenaufruf, Konstruktoraufruf oder Zugriff auf ein Feld)

### Pointcuts

-	Definition der Orte an denen tatsächlich hineingewoben wird (Anwendung kann bestimmt oder eingeschränkt werden)
-	eine eEgel die beschreibt welche Join Points betroffen sind (zb alle Methoden im Paket service)


### Advice-Typen (Bevor,After,Around)

-	der auszuführende Code, der an einem Join Point ausgeführt wird und in die Core-Level Methode eingewoben wird
-	before = wird vor der Methode ausgeführt
-	after = wird nach der Methode ausgeführt
-	around = ersetzt die Methode und sie gegebenfalls selbst auf

### Weaving Prozess

-	der technische Prozess des Hineinwebens der fachfremden Concerns (Aspekte?) in den Zielcode. Vorgenommen vom Weaver ( Programm welches die.class Datei neu verdrahtet / ändern kann)


### Codebeispiele

-   Cross-Cutting Concerns


-   Wiederholende Funktionalitäten


-   Potentielle AOP-Anwendungsfälle



(300 Wörter)


### AOP-Integration



## 4. LLM-Einsatz für AOP
