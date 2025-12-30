## 1. Review Implementierungsstrategie

Die bisherige Implementierungsstrategie orientiert sich bereits an DDD, Domain-Driven-Development und seinen Prinzipien.
Eine klare Trennung der Domänenlogik in Bounded Contexts ist enthalten.
Nachdem die Implementierungsstrategie überprüft wurde, ist eine kleine Anpassung erforderlich.
Es wird ein Use-Case-Layer empfohlen, um zu verhindern, dass ein Overengineering stattfindet und die Domain-Objekte an den Use Cases vorbei designed werden.
Daher sollten die Domains schrittweise pro Use Case entwickelt werden.
Hier für sollten neue Aggregate nzw Entitäten nur angelegt werden die ein konkreter Use Case benötigt und Attribute bzw Methoden erst hinzugefügt werden,
wenn ein Use Case benötigt wird.
Eine weitere Schwachstelle in der Implementierungsstrategie ist, dass die Tests an letzter Stelle aufgelistet sind. Dies suggeriert,
dass die erst am Ende geschrieben werden. Diese sollten jedoch nach dem Test-Driven-Developement noch vor der implementierung der Implementierung der Klassen usw. erfolgen.
Hierfür sollte eine Teststrategie genauer definiert werden. Diese Vorschläge wurden übernommen.
Weitere Vorschläge die nicht übernommen wurden sind folgende.
Dass GitHub Pages nur für das Frontend geeignet ist. Diese Aussage ist, sowie andere Aussagen der LLM korrekt und muss daher nicht übernommen werden, da es bereits umgesetzt wird.

### Ausgewählte Domain-Events

Aufgrund des Vorschlags der LLM dass die Benennung konsistent sein sollte und zwar in folgendem Stil, <DomänenobjektL><Vergangenheitsform>, sind dies unsere ausgewählten Domain-Events.

- **AngebotVeröffentlicht**
- **ReservierungErstellt**
- **AbholungAbgeschlossen**

## 2. TDD Schritt 1: Testfälle mit LLM generieren und validieren

### Vorgehen und Dokumentation

**2.1.** Wir haben das LLM verwendet, um zunächst für unsere Entität `Angebot` Testfälle zu generieren, die sich an der im DDD definierten Domäne „Angebotsmanagement“ orientieren.  
Das LLM wurde mit der Beschreibung unserer Domänenlogik und Validierungsanforderungen (Angebot veröffentlichen, reservieren, Abholcode-Validierung, E-Mail und Titel-Formate) gefüttert.

### Generierte Tests
**2.2. Das LLM hat Happy-Path-, Edge-Case- und Negative-Tests vorgeschlagen:**

* **Happy Path:**
    * `veroeffentlichen_setztStatusVerfuegbar_und_emittiertEvent()`
    * `reservieren_imHappyPath_markiertAngebotUndErzeugtReservierung()`

* **Edge Cases:**
    * doppelte Veröffentlichung nicht erlaubt
    * Zeitfenster muss chronologisch sein
    * `getDomainEvents()` liefert unveränderbare Kopie

* **Negative Tests:**
    * Reservieren ohne Veröffentlichung
    * Ungültige IDs (`null`) und falsche Zustände
    * Regex-Validierungen für Titel, Beschreibung, Tags, E-Mail und Abholcode

### Kritische Bewertung
**2.3.**
* Die LLM-generierten Tests waren weitgehend brauchbar; einige wurden vereinfacht (z. B. Wegfall unnötiger Mocking-Konstrukte).
* Die Regexe wurden auf realistische Eingaben validiert und leicht angepasst (Umlaute und deutsche Zeichen hinzugefügt).
* Fehlende Tests für Kleinbuchstaben bei Abholcode wurden ergänzt.

### Regex-Validierung

**2.4. Zur Validierung wurden folgende Reguläre Ausdrücke verwendet:**

| Attribut | Regex | Bedeutung |
|-----------|--------|-----------|
| Titel | `^[A-Za-zÄÖÜäöüß0-9][A-Za-zÄÖÜäöüß0-9 \-/]{1,49}$` | alphanumerisch, Bindestrich erlaubt, max. 50 Zeichen |
| Beschreibung | `^(?!.*<(script|iframe|object)).{0,500}$` | kein HTML-Injection, max. 500 Zeichen |
| Tags | `^[a-zäöüß0-9]{1,20}(?:,[a-zäöüß0-9]{1,20})*$` | komma-separiert, nur Kleinbuchstaben |
| E-Mail | `^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$` | RFC-konformes Pattern |
| Abholcode | `^[A-Z0-9]{4,8}$` | Domänenregel für FoodRescue |

**2.5. Kritische Bewertung der Tests:** Die LLM-generierten Tests decken für den Start gute Szenarien ab. Die Aufteilung der Tests war übersichtlich und fokussiert. Allerdings gibt es Raum für Verbesserungen: Zunächst wurden zu wenig Tests generiert. Durch immer mehr prompts und Spezifizierung dieser, konnte jedoch ein vollständiges Testszenario erstellt werden. 

**2.6. Implementierung und Testergebnisse:** Die JUnit-Tests wurden vollständig implementiert und ausgeführt. Wie im TDD-Ansatz erwartet, befinden wir uns in der RED-Phase: Von 5 Tests schlagen 4 fehl (alle Validierungs-Tests), während 1 Happy-Path-Test durchläuft (der keine Validierung erwartet). Die Validierungslogik wurde bewusst nicht implementiert, dies ist Aufgabe von TDD Schritt 2. Die erste Test-Suite ist an dieser Stelle vollständig lauffähig und bereit für die Implementierung der Domänenlogik in der nächsten Aufgabe.


## ⚙️ Aufgabe 3 – Implementierung der Domänenlogik (TDD Schritt 2)

### Vorgehen
**3.1.** Nach den Tests wurde die Domänenlogik für die Kern-Entitäten `Angebot`, `Reservierung` und `Abholung` implementiert.  
Das LLM diente hier als „Pair-Programming-Partner“ – es generierte den ersten Code-Entwurf, den wir anschließend iterativ überarbeitet haben.

### Wichtige Klassen
**3.2.**
* `Angebot` – zuständig für Lebenszyklus eines Angebots (`neu`, `veroeffentlichen`, `reservieren`)
* `Reservierung` – erzeugt bei Reservierung, prüft Abholcode, liefert DomainEvents
* `Abholung` – separates Aggregat für Übergabeprozess
* Value Objects – `AbholZeitfenster`, `Abholcode`
* Events – `AngebotVeröffentlicht`, `ReservierungErstellt`, `AbholungAbgeschlossen`
* Exceptions – `DomainException`, `DomainError`

### Beispiel Implementierung (Auszug)
```java
public class Angebot {
    public enum Status { VERFUEGBAR, RESERVIERT, ABGEHOLT, ENTFERNT }
    private Status status = Status.ENTFERNT;
    public List<DomainEvent> veroeffentlichen() {
        if (status != Status.ENTFERNT) throw DomainException.raise(DomainError.ANGEBOT_BEREITS_VEROEFFENTLICHT);
        status = Status.VERFUEGBAR;
        var evt = new AngebotVeröffentlicht(id.value());
        domainEvents.add(evt);
        return List.of(evt);
    }
}
```
**3.3. und 3.4.**
### Pair-Programming Erfahrungen
* Das LLM half vor allem bei der Initialstruktur (Events, Value Objects, Exceptions).
* Wir haben bewusst auf Streams und Optionals verzichtet, um Klarheit in der Domänenlogik zu bewahren.
* Das LLM schlug immutable Entities vor – wurde teilweise übernommen (ID, Fenster final), Statusfelder blieben mutable.
* Fehlerbehandlung mit `DomainException` statt `IllegalArgumentException` war ein hilfreicher Hinweis.
* DDD-Prinzipien wurden konsequent eingehalten: Angebot ist Aggregate Root, Reservierung Aggregate Boundary.
* Alle Tests blieben grün – Ergebnis: eine vollständige, saubere Domänenschicht.


## 🔁 Aufgabe 4 – Tests erweitern und Refactoring (TDD Schritt 3)

**4.1 Erweiterte Tests**
Neue Tests für Randfälle und Fehlerbedingungen:

* **Edge Cases:** doppelte Veröffentlichung, ungültiges Zeitfenster
* **Negative:** falscher Abholcode, mehrfache Abholung, ungültige Statuswechsel
* **Regex-Tests:** nur für Angebotsattribute (Titel, Beschreibung, Tags, E-Mail, Abholcode)

**4.2 Refactorings (schrittweise umgesetzt)**

1. **Value Objects für IDs** (`AngebotsId`, `AnbieterId`, `ReservierungsId`, `NutzerId`)  
   → Bessere Typensicherheit und Vermeidung von String-Fehlern.

2. **DomainEvents ziehen und leeren**  
   → `pullDomainEvents()` zur Outbox-Integration (Events werden nach Verarbeitung entfernt).

3. **Statuswechsel zentralisieren**  
   → Methode `wechselStatus(Status alt, Status neu, DomainError)` ersetzt doppelte If-Blöcke.

4. **Fehler-Enum `DomainError`**  
   → Einheitliche Fehlerquelle für Lesbarkeit und Tests.

5. **Zeit injektionsfähig machen**  
   → `AbholZeitfenster.istNochAktuell(Clock)` für deterministische Tests.

6. **Sichtbarkeiten anpassen**  
   → Entitäten `package-private`, Value Objects und Events `public`.

**4.3 Refactoring-Bewertung**
* **Umgesetzt:** 1–4 waren einfach und verbessern Testbarkeit & Lesbarkeit.
* **Nicht umgesetzt:** vollständige Immutabilität – nicht sinnvoll für Statuswechsel.
* **Ergebnis:** Alle Tests nach Refactoring grün, Code sauber, DDD beibehalten.
* Im Rahmen des dritten TDD-Schritts wurde die Testsuite gezielt um Randfälle und Fehlerszenarien erweitert, um die Robustheit der Domänenlogik zu erhöhen.
  Dazu gehören doppelte Veröffentlichungen, ungültige Zeitfenster sowie fehlerhafte Statuswechsel und Abholcodes.
  Auf Basis der LLM-Vorschläge wurden anschließend Refactorings umgesetzt, die Redundanzen beseitigten und die Lesbarkeit des Codes deutlich verbesserten.
  Besonders hilfreich waren die Einführung von Value Objects für IDs, die zentrale Methode für Statuswechsel und die klare Fehlerstruktur über das DomainError-Enum.
  Nach jedem Refactoring-Schritt wurden alle Tests erneut ausgeführt – sie blieben konsistent grün.
  Das Ergebnis ist eine saubere, testbare und leicht erweiterbare Domänenschicht, die sowohl funktional als auch strukturell den DDD-Prinzipien entspricht.

---

## 🧾 Beispiel Regex-Tests (aus AngebotRegexValidationTest)
```java
@Test
void titel_regex_happyPath() {
    assertTrue(TITEL_PATTERN.matcher("Tomatensuppe").matches());
    assertTrue(TITEL_PATTERN.matcher("Salat-Box 2").matches());
}
@Test
void titel_regex_negative() {
    assertFalse(TITEL_PATTERN.matcher("Pizza🍕").matches());
}


