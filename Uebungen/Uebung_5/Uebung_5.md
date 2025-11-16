# Übung 5: Software- und Architekturmetriken für Codequalität und Architekturoptimierung

## 1. Überblick und Anwendung einfacher Metriken

Für die Aufgabe wurde aufgrund des Vortrages des Teams 8 SonarQube gewählt, da dieses Tool auf in der Aufgabe 3 benötigt wird.
Die Installation verlief in meinem Umfeld nicht reibungsfrei und die endgültige Lösung des Problems, dass sich SonarQube nicht mit IntelliJ IDEA verbinden ließ, wurde erst durch eine VM mit kompletter, sauberer Installation gelöst.
Nachdem ich hier SonarQube lauffähig bekommen habe, konnte ich dies auch in meine eigentliche Umgebung lauffähig bekommen.
Die Analyse des Codes mit SonarQube zeigte einige interessante Metriken auf.
Die wichtigsten Metriken sind:

### McCabe-Metrik (Zyklomatische Komplexität)

| Wert    |                Bedeutung                |
| :------ | :-------------------------------------: |
| 1–10    |   Einfacher Eingriff, geringes Risiko   |
| 11–20   |       Komplexes, mittleres Risiko       |
| 21–50   |          Komplex, hohes Risiko          |
| &gt; 50 | Nicht testbarer Code, sehr hohes Risiko |

Daher wurden einige Klassen näher betrachtet, welche einen Wert größer als 10 hatten, und im Rahmen der Übung hierzu Empfehlungen via LLM eingeholt.

#### Klasse Abholung.java – Wert 13

Empfehlung zur Reduzierung der Komplexität:

1. Guard Clauses auslagern in private Validierungsmethoden.
2. Statuswechsel und Fehlermeldungen trennen
3. (Optional) State Pattern für Statuslogik

![img_3.png](img_3.png)

#### Klasse Angebot.java – Wert 16

Die Klasse Angebot hat aktuell keine sehr komplexen Methoden – die zyklomatische Komplexität entsteht primär durch die if-Bedingungen in veroeffentlichen() und reservieren().

Empfehlung zur Reduzierung der Komplexität:

1. Guard Clauses / Validierungs-Methoden auslagern
2. Statuslogik in ein State Pattern auslagern (bei wachsender Domain)
3. Statuswechsel in eigene Methoden kapseln
4. Validierung und Erstellung trennen

![img_4.png](img_4.png)

#### Klasse Reservierung.java – Wert 15

Empfehlung zur Reduzierung der Komplexität:

1. Guard Clauses auslagern in eigene Validierungsmethoden
2. Statusprüfung zentralisieren
3. (Optional) Status-Logik ins State-Pattern auslagern

![img_5.png](img_5.png)

### Kognitive Komplexität

Die kognitive Komplexität misst, wie schwer es für einen Entwickler ist, den Code zu verstehen. Sie berücksichtigt dabei nicht nur die Anzahl der Verzweigungen, sondern auch deren Verschachtelungstiefe und die Art der Kontrollstrukturen.
Als Faustregel gilt, dass die Werte für die kognitive Komplexität unter 15 liegen sollten.

Der höchste Wert von 7 wurde in der Klasse GeoStandort.java erzielt. Alle anderen Klassen lagen darunter.

![img_6.png](img_6.png)

Als Möglichkeiten zur Reduzierung wurde vom LLM folgendes vorgeschlagen:

1. Validierung in eigene Methoden auslagern
2. Optional: equals und hashCode mit Objects.equals vereinfachen

![img_8.png](img_8.png)

## 2. Test Coverage erweitern und Code Coverage verbessern

## 3. Technical Debt und Regelverletzungen mit LLM analysieren

## 4. Frontend-Entwicklung und Erweiterung der Anwendung

Das Frontend der FoodRescue-Anwendung besteht aus zwei Hauptseiten

- `index.html` für Login/Registrierung und
- `dashboard.html` für eingeloggte User

Es wird eine modulare JavaScript-Architektur mit separaten Modulen für Authentifizierung, DOM-Manipulation und Benachrichtigungen eingesetzt:

- `main.js` - Haupteinstiegspunkt der Anwendung
- `authActions.js` - Authentifizierungs-Logik für Login/Registrierung
- `authMode.js` - Verwaltung des Authentifizierungsmodus (Login/Registrierung)
- `dom.js` - DOM-Manipulation und Element-Referenzen
- `notifications.js` - Anzeige von Fehler- und Erfolgsmeldungen
- `dashboard.js` - Dashboard-Funktionalität und Logout

### Zusammenspiel Frontend und Backend

Die zentrale Backend-Funktionalität der Userverwaltung wird über die REST-API `/api/users` (POST-Methode) angesprochen, damit eingegebene Daten serverseitig validiert werden können, für Testzwecke werden die Daten jedoch noch nicht persistent gespeichert, sondern im `localStorage` abgelegt. Das Dashboard zeigt aktuell statische Informationen (Abholtermine, Navigationspunkte) an, um später weitere Backend-Services wie Angebotsmanagement und Reservierungsverwaltung über entsprechende API-Endpunkte anzubinden.

## 5. Reflexion zum Einsatz von Metriken und LLM

### LLM & Frontendentwicklung

Beim Einsatz des LLM für die Frontendentwicklung zeigte sich, dass ohne klare Instruktionen zunächst eine einzige `script.js`-Datei mit mehreren hundert Zeilen entstand, die kaum wartbar war und zudem keinerlei Verzeichnisstruktur für CSS oder Bildressourcen vorsah. Erst durch gezieltes Prompting hin zu einer modularen Struktur mit klaren Verantwortlichkeiten, separaten Ordnern (`css/`, `img/`) und erläuternden Kommentaren wurde der Code übersichtlicher und besser erweiterbar. Die Erfahrung unterstreicht, wie wichtig klare Vorgaben an das LLM sind, um maintainable Ergebnisse zu erhalten, die keine technischen Schulden erzeugen.
