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