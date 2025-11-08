### 1. Review Implementierungsstrategie

Die bisherige Implementierungsstrategie orientiert sich bereits an DDD, Domain-Driven-Development und seinen Prinzipien.
Eine klare Trennung der Domänenlogik in Bounded Contexts ist enthalten.
Nachdem die Implementierungsstrategie überprüft wurde, ist eine kleine Anpassung erforderlich.
Es wird ein Use-Case- Layer empfohlen, um zu verhindern, dass ein Overengineering stattfindet und die Domain-Objekte an den Use Cases vorbei designed werden.
Daher sollten die Domains schrittweise pro Use Case entwickelt werden.
Hier für sollten neue Aggregate nzw Entitäten nur angelegt werden die ein konkreter Use Case benötigt und Attribute bzw Methoden erst hinzugefügt werden,
wenn ein Use Case benötigt wird.
Eine weitere Schwachstelle in der Implementierungsstrategie ist, dass die Tests an letzter Stelle aufgelistet sind. Dies suggeriert,
dass die erst am Ende geschrieben werden. Diese sollten jedoch nach dem Test-Driven-Developement noch vor der implementierung der Implementierung der Klassen usw. erfolgen.
Hierfür sollte eine Teststrategie genauer definiert werden. Diese Vorschläge wurden übernommen.
Weitere Vorschläge die nicht übernommen wurden sind folgende.
Dass GitHub Pages nur für das Frontend geeignet ist. Diese Aussage wirkt frei erfunden aus den Informationen die gegeben waren.

### Ausgewählte Domain-Events

Aufgrund des Vorschlags der LLM dass die Bennenung konsistent sein sollte und zwar in folgendem Stil, <DomänenobjektL><Vergangenheitsform>, sind dies unsere ausgewählten Domain-Events.

- **AngebotVeröffentlicht**
- **ReservierungErstellt**
- **AbholungAbgeschlossen**

## 2. TDD Schritt 1: Testfälle mit LLM generieren und validieren

### Vorgehen und Dokumentation

**2.1. LLM-Prompt Erstellung:** Für die Domänenlogik des Angebot-Aggregates wurde ein Prompt erstellt, der die Validierung von **Anbieter-Daten** beschreibt. Die zu testenden Felder waren:

- AnbieterName
- AnbieterEmail
- AnbieterTelefon
- Beschreibung und Menge

Das LLM erhielt Anweisungen, Test-Cases für Happy-Path, Edge-Cases und negative Tests zu generieren.

**2.2. Test-Cases Generierung:** Das LLM generierte insgesamt 5 Test-Cases, aufgeteilt in drei Kategorien. Der Happy-Path-Test (1 Test) überprüft die grundlegende Objekterstellung und ob die Getter-Methoden die übergebenen Werte korrekt zurückgeben, ohne Validierung zu erwarten. Die Edge-Case-Tests (2 Tests) prüfen Grenzwerte: Name zu kurz (unter 2 Zeichen) und Beschreibung zu lang (über 500 Zeichen). Die negativen Tests (2 Tests) erwarten, dass ungültige Eingaben abgelehnt werden: Email ohne @ und null-Werte.

**2.3. Kritische Bewertung:** Das LLM hat zum Start zu viele (41!) Tests erstellt und sich dabei z.T. nicht an die gepromptete Struktur gehalten. Beispielsweise wurde dieser Test als Happy-Path Test deklariert

```java
@Test
void sollteAngebotMitUmlautenUndBindestrichErstellen() {
  // Happy Path mit Sonderzeichen (Umlaute, Bindestrich)
  Angebot angebot =
      Angebot.erstellen(
          "Müller-Lüdenscheidt",
          "mueller@example.de",
          "030 987654",
          "Verschiedene Backwaren vom Vortag",
          "10 Stück");

  assertNotNull(angebot);
  assertEquals("Müller-Lüdenscheidt", angebot.getAnbieterName());
}
```

Dieser Test ist kein Happy-Path-Test, sondern testet bereits Validierungslogik (ob Umlaute und Bindestriche akzeptiert werden).

**Positive Aspekte:** Die Aufteilung in Happy Path, Edge Cases und Negative Tests wurde eingehalten. Der Basis-Happy-Path-Test ist vollständig, und die Edge-Case-Tests für Grenzwerte (Name min. 2 Zeichen, Beschreibung max. 500 Zeichen) führen korrekterweise zu fails (RED-Phase).

**2.4. Regex-Validierung:** Das LLM generierte fünf Regex-Patterns für die Validierungslogik: EMAIL_PATTERN (mit Lokalteil, @, Domain und TLD), NAME_PATTERN (Buchstaben inkl. Umlaute, min. 2 Zeichen, Bindestriche/Leerzeichen erlaubt), TELEFON_PATTERN (deutsche Formate mit +49 oder 0, keine 0 nach Vorwahl), BESCHREIBUNG_PATTERN (10-500 Zeichen, kein reiner Whitespace) und MENGE_PATTERN (positive Dezimalzahl mit optionaler Einheit wie kg, g, Stück). Diese Patterns wurden als Kommentare in der Angebot-Klasse dokumentiert.

**2.5. Kritische Bewertung der Tests:** Die LLM-generierten Tests decken die wichtigsten Szenarien kompakt ab. Besonders hilfreich waren die Edge-Cases für konkrete Grenzwerte (Name mindestens 2 Zeichen, Beschreibung maximal 500 Zeichen). Die Aufteilung in 1+2+2 Tests war übersichtlich und fokussiert. Allerdings gibt es Raum für Verbesserungen: Der Test sollteNullWerteAbweisen prüft nur den Namen-Parameter auf null, während andere Felder nicht getestet werden - dieser Test ist damit unvollständig und täuscht Vollständigkeit vor. Es fehlen Tests für wichtige Fälle wie ungültige Telefonnummern (ohne Vorwahl), ungültige Mengenangaben (Text statt Zahl) oder zu kurze Beschreibungen. Die Test-Suite ist bewusst minimalistisch gehalten, was für eine Übung akzeptabel ist, aber in einem echten Projekt unzureichend wäre.

**2.6. Implementierung und Testergebnisse:** Die JUnit-Tests wurden vollständig implementiert und ausgeführt. Wie im TDD-Ansatz erwartet, befinden wir uns in der RED-Phase: Von 5 Tests schlagen 4 fehl (alle Validierungs-Tests), während 1 Happy-Path-Test durchläuft (der keine Validierung erwartet). Die Validierungslogik wurde bewusst NICHT implementiert - dies ist Aufgabe von TDD Schritt 2. Die kompakte Test-Suite ist vollständig lauffähig und bereit für die Implementierung der Domänenlogik in der nächsten Übung.
