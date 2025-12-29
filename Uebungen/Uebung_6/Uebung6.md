# Übung :6 Aspect Orientierte Programmierung

## 1. Backend-Weiterentwicklung

### 1.1 Implementierte Domain-Events und Geschäftsprozesse

Im Rahmen der Backend-Weiterentwicklung wurde die Reservierung von Angeboten umgesetzt. Außerdem wurden die DDD-Prinzipien versucht vollständig umzusetzen.

#### Domain-Event 1: AngebotReserviertEvent

**Geschäftsprozess:** Ein Abholer reserviert ein verfügbares Lebensmittelangebot.

**Implementierung:**

1. **Domain Model (Angebotsmanagement)**:
   - `Angebot.reservieren()`: Business Logic für Reservierung mit Invarianten-Schutz
   - Prüfung: Angebot muss im Status VERFUEGBAR sein
   - Business Rule: Anbieter kann sein eigenes Angebot nicht reservieren
   - Statuswechsel: VERFUEGBAR → RESERVIERT
   - Event-Erzeugung: `AngebotReserviertEvent(angebotId, abholerId, abholcode)`

2. **Event Handler (Reservierungsmanagement)**:
   - `AngebotReserviertEventHandler` reagiert auf das Event
   - Erstellt automatisch ein neues `Reservierung`-Aggregate im eigenen Bounded Context
   - Respektiert Aggregate Boundaries (keine direkte Referenz zwischen Aggregates)

3. **Repository-Anbindung**:
   - `InMemoryAngebotRepository`: Speichert Aggregate und published Domain Events automatisch
   - `InMemoryReservierungRepository`: Persistiert die neue Reservierung
   - Event Publishing über Spring's `ApplicationEventPublisher`

4. **Service-Layer**:
   - `ReservierungsApplicationService.reserviereAngebot()`: Orchestriert den Use Case
   - Lädt Angebot, generiert Abholcode, delegiert an Domain-Methode
   - Transaktionsgrenze mit `@Transactional`

**Code-Beispiel (Domain Logic)**:
```java
public class Angebot implements AggregateRoot {
    public AngebotReserviertEvent reservieren(String abholerId, Abholcode abholcode) {
        // Invariante: Nur verfügbare Angebote
        if (status != Status.VERFUEGBAR) {
            throw new DomainException("Angebot ist nicht verfügbar");
        }
        
        // Business Rule: Anbieter ≠ Abholer
        if (this.anbieterId.getValue().toString().equals(abholerId)) {
            throw new DomainException("Anbieter kann sein eigenes Angebot nicht reservieren");
        }
        
        status = Status.RESERVIERT;
        var event = new AngebotReserviertEvent(id.value(), abholerId, abholcode.value());
        domainEvents.add(event);
        return event;
    }
}
```

## 2. Frontend-Weiterentwicklung

### Rollenbasierte UI

- unterschiedliche Ansichten für Anbieter vs. Abholer
- Navigation zwischen verschiedenen Views (Angebote, Reservierungen)
- Formular zum Erstellen neuer Angebote (`handleCreateAngebot.js`)
- Liste geplanter Abholungen
- Anzeige von Abholcode
- Status-Tracking (AKTIV, ABGEHOLT, STORNIERT)
- Prozesssteuerung über DOM und localStorage
- Fehlerhandling erweitert


### Responsive Design (Bonus)

**Implementiert mit CSS Media Queries**:

```css
/* Mobile First Approach */
.pickup-item {
    display: flex;
    flex-direction: column;
    padding: 1rem;
}

/* Tablet */
@media (min-width: 768px) {
    .pickup-item {
        flex-direction: row;
        justify-content: space-between;
    }
}

/* Desktop */
@media (min-width: 1024px) {
    .dashboard-content {
        max-width: 1200px;
        margin: 0 auto;
    }
}
```

**Responsive Features**:
- Flexible Layouts (Flexbox/Grid)
- Touch-friendly Button-Größen (min. 44x44px)
- Optimierte Navigation für Mobile (Hamburger-Menü könnte ergänzt werden)
- Anpassbare Schriftgrößen

---

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


### AOP-Analyse

#### (a) Querschnittsbelange (Cross-Cutting Concerns)

**1. Logging** (Hauptpriorität):
- **Vorkommen**: 15+ Service-Klassen, 8+ Controller, 4+ Repositories
- **Problem**: Jede Klasse deklariert eigenen Logger, manuelle Entry/Exit-Logs
- **Beispiel**:
```java
// Vor AOP in jeder Klasse:
private static final Logger log = LoggerFactory.getLogger(AngebotApplicationService.class);

public AngebotsId erstelleAngebot(ErstelleAngebotCommand cmd) {
    log.info("Erstelle Angebot für Anbieter: {}", cmd.getAnbieterId());
    // Business Logic
    log.info("Angebot erfolgreich erstellt: {}", id);
}
```

**2. Performance-Monitoring**:
- **Vorkommen**: Aktuell nicht implementiert, wäre in ~10+ kritischen Methoden sinnvoll
- **Problem**: Keine Metriken für Geschäftsvorgänge (z.B. Reservierungsdauer)
- **Potenzial**: Identifikation von Bottlenecks, KPI-Tracking

**3. Exception-Handling**:
- **Vorkommen**: GlobalExceptionHandler vorhanden, aber keine automatische Fehlerprotokollierung
- **Problem**: Keine Context-Informationen bei Fehlern (welche Parameter führten zum Fehler?)

**4. Event-Publishing**:
- **Vorkommen**: 4 Repository-Implementierungen
- **Pattern**: Immer gleicher Code zum Event-Publishing

**5. Transaktionsverwaltung**:
- **Vorkommen**: @Transactional manuell an ~10 Service-Methoden
- **Problem**: Leicht zu vergessen, keine zentrale Kontrolle

#### (b) Wiederholende Funktionalitäten

**1. Method Entry/Exit Logging** (10+ Stellen):
```java
log.info("Empfange AngebotReserviertEvent für Angebot: {}", event.getAngebotId());
// ... Methoden-Logik ...
log.info("Reservierung erfolgreich erstellt: {}", reservierungsId.value());
```

**2. Exception Logging mit Try-Catch** (15+ Methoden):
```java
try {
    // Business Logic
} catch (Exception e) {
    log.error("Fehler in Methode X", e);
    throw e;
}
```

**3. Null-Checks und Validierung** (50+ Stellen):
```java
Objects.requireNonNull(id, "ID darf nicht null sein");
if (titel == null || titel.trim().isEmpty()) {
    throw new DomainException("Titel darf nicht leer sein");
}
```

**4. DTO-Mapping** (20+ Mapping-Methoden):
```java
response.setId(angebot.getId());
response.setAnbieterId(angebot.getAnbieterId().getValue().toString());
response.setTitel(angebot.getTitel());
// ... weitere Felder ...
```

#### (c) Potenzielle AOP-Anwendungsfälle

**1. Logging Aspect** ✅ IMPLEMENTIERT:
- Automatisches Method Entry/Exit Logging
- Performance-Messung integriert
- Exception-Logging mit Context
- **Nutzen**: Eliminiert ~100+ manuelle Log-Statements

**2. Performance Monitoring Aspect** (Zukunft):
- Automatische Ausführungszeit-Messung
- Warnung bei Überschreitung von Schwellwerten
- Statistiken (min, max, avg, count)
- **Nutzen**: Identifikation von Bottlenecks

**3. Audit Logging Aspect** (Zukunft):
- Protokollierung kritischer Geschäftsvorgänge (CREATE_OFFER, RESERVE_OFFER)
- Compliance (DSGVO, Nachvollziehbarkeit)
- **Nutzen**: Security, Business Intelligence

**4. Validation Aspect** (Zukunft):
- Automatische Parameter-Validierung
- Zentrale Validierungsregeln
- **Nutzen**: Reduziert Boilerplate-Validierungscode

### 3.3 AOP-Integration: LoggingAspect

#### Implementierung

**Package-Struktur**:
```
com.foodrescue.shared.aop/
└── LoggingAspect.java
```

**Vollständige Implementierung**:

```java
@Aspect
@Component
public class LoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    // ========== Pointcut Definitions ==========
    
    @Pointcut("execution(* com.foodrescue.*.application.services.*.*(..))")
    public void applicationServiceMethods() {}

    @Pointcut("execution(* com.foodrescue.*.infrastructure.web.rest.*.*(..))")
    public void controllerMethods() {}

    @Pointcut("execution(* com.foodrescue.*.infrastructure.repositories.*.*(..)) || " +
              "execution(* com.foodrescue.*.infrastructure.persistence.*.*(..))")
    public void repositoryMethods() {}

    @Pointcut("execution(* com.foodrescue.*.infrastructure.eventhandlers.*.*(..))")
    public void eventHandlerMethods() {}

    @Pointcut("applicationServiceMethods() || controllerMethods() || repositoryMethods() || eventHandlerMethods()")
    public void loggableMethods() {}

    // ========== Around Advice (Method Entry/Exit + Performance) ==========
    
    @Around("applicationServiceMethods() || repositoryMethods()")
    public Object logMethodExecutionWithPerformance(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getSignature().getDeclaringType().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        // Method Entry Logging
        if (log.isDebugEnabled()) {
            log.debug("→ {}.{}() called with: {}", className, methodName, formatArguments(args));
        }

        // Performance-Messung
        Instant start = Instant.now();
        try {
            // Methoden-Ausführung
            Object result = joinPoint.proceed();

            // Performance berechnen
            Duration duration = Duration.between(start, Instant.now());

            // Method Exit Logging mit Performance
            if (log.isDebugEnabled()) {
                log.debug("← {}.{}() returned: {} [Execution time: {}ms]",
                    className, methodName, formatReturnValue(result), duration.toMillis());
            }

            return result;
        } catch (Throwable throwable) {
            throw throwable; // Exception wird in @AfterThrowing behandelt
        }
    }

    // ========== Before Advice (Controller Entry Logging) ==========
    
    @Before("controllerMethods()")
    public void logControllerEntry(JoinPoint joinPoint) {
        String className = joinPoint.getSignature().getDeclaringType().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        log.info("HTTP → {}.{}() | Request: {}", className, methodName, formatArguments(args));
    }

    // ========== AfterReturning Advice (Controller Exit Logging) ==========
    
    @AfterReturning(pointcut = "controllerMethods()", returning = "result")
    public void logControllerExit(JoinPoint joinPoint, Object result) {
        String className = joinPoint.getSignature().getDeclaringType().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        log.info("HTTP ← {}.{}() | Response: {}", className, methodName, formatReturnValue(result));
    }

    // ========== AfterThrowing Advice (Exception Logging) ==========
    
    @AfterThrowing(pointcut = "loggableMethods()", throwing = "exception")
    public void logException(JoinPoint joinPoint, Throwable exception) {
        String className = joinPoint.getSignature().getDeclaringType().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        log.error("✗ {}.{}() threw {} with message: '{}' | Arguments: {}",
            className, methodName, exception.getClass().getSimpleName(),
            exception.getMessage(), formatArguments(args), exception);
    }

    // ========== Before Advice für Event Handlers ==========
    
    @Before("eventHandlerMethods()")
    public void logEventHandling(JoinPoint joinPoint) {
        String className = joinPoint.getSignature().getDeclaringType().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        String eventInfo = "Unknown Event";
        if (args.length > 0 && args[0] != null) {
            eventInfo = args[0].getClass().getSimpleName();
        }

        log.info("📨 Event Received: {} | Handler: {}.{}()", eventInfo, className, methodName);
    }

    // ========== Helper Methods ==========
    
    private String formatArguments(Object[] args) {
        if (args == null || args.length == 0) return "[]";
        return Arrays.toString(Arrays.stream(args).map(this::formatObject).toArray());
    }

    private String formatReturnValue(Object returnValue) {
        if (returnValue == null) return "null";
        return formatObject(returnValue);
    }

    private String formatObject(Object obj) {
        if (obj == null) return "null";
        String str = obj.toString();
        final int MAX_LENGTH = 100;
        if (str.length() > MAX_LENGTH) {
            return str.substring(0, MAX_LENGTH) + "... [truncated]";
        }
        return str;
    }
}
```

#### Funktionsweise

**1. Around Advice für Services & Repositories**:
- Fängt Methodenaufruf ab
- Loggt Entry mit Parametern
- Misst Ausführungszeit
- Loggt Exit mit Rückgabewert und Performance
- Exception-Handling delegiert an @AfterThrowing

**2. Before/AfterReturning für Controller**:
- Loggt HTTP-Requests (Before)
- Loggt HTTP-Responses (AfterReturning)
- Separates Logging für bessere Lesbarkeit

**3. AfterThrowing für alle Methoden**:
- Fängt alle Exceptions ab
- Loggt mit vollständigem Context (Klasse, Methode, Parameter, Exception-Typ)
- Stack Trace wird automatisch mitgeloggt

**4. Before für Event Handler**:
- Spezielle Formatierung für Domain Events
- Extrahiert Event-Typ aus erstem Parameter

## 4. LLM-Einsatz für AOP

### 4.1 Dokumentation des LLM-Einsatzes

#### Identifikation von Cross-Cutting Concerns

**Prompt an Claude AI**:
> "Analysiere mein FoodRescue Spring Boot Projekt auf Cross-Cutting Concerns. Identifiziere wiederholende Funktionalitäten und potenzielle AOP-Anwendungsfälle. Das Projekt verwendet DDD mit Bounded Contexts für Angebotsmanagement, Reservierungsmanagement, Abholungsmanagement und Userverwaltung."

**LLM-Antwort** (zusammengefasst):
Claude identifizierte 6 Hauptkategorien von Cross-Cutting Concerns:
1. **Logging** (höchste Priorität) - ~100+ manuelle Log-Statements
2. **Performance-Monitoring** - Keine Metriken vorhanden, wäre sinnvoll
3. **Exception-Handling** - Try-Catch-Boilerplate in ~15+ Methoden
4. **Event-Publishing** - Redundanter Code in 4 Repositories
5. **Transaktionsverwaltung** - @Transactional an ~10 Stellen
6. **Validation** - Null-Checks an ~50+ Stellen

**Bewertung**: Die Analyse war äußerst präzise. Claude erkannte nicht nur die offensichtlichen Concerns (Logging), sondern auch subtilere Patterns wie das Event-Publishing-Pattern in Repositories. Die Priorisierung (Logging als höchste Priorität) war ebenfalls nachvollziehbar.

### 4.2 Reflexion über LLM-Einsatz

#### Nützlichkeit des LLM-Einsatzes

**Sehr nützlich bei**:
1. ✅ **Initiale Code-Generierung**: Claude lieferte eine 90% fertige Implementierung
2. ✅ **Best Practices**: Code folgte automatisch Best Practices (Log-Level-Checks, Performance-Messung)
3. ✅ **Dokumentation**: JavaDoc-Kommentare waren ausführlich und präzise
4. ✅ **Problemanalyse**: Identifikation von Cross-Cutting Concerns war sehr gründlich
5. ✅ **Konzept-Erklärungen**: Erklärung von Join Points, Pointcuts, Weaving-Prozess war verständlich

**Weniger nützlich bei**:
1. ⚠️ **Projekt-spezifische Anpassungen**: Package-Namen mussten manuell angepasst werden
2. ⚠️ **Testing**: Test-Code war generisch und musste stark angepasst werden
3. ⚠️ **Integration-Details**: Konkrete Spring Boot Konfiguration (application.properties) fehlte teilweise

#### Herausforderungen und Lösungen

**Herausforderung 1: Zu generischer Code**
- **Problem**: Initiale Implementierung verwendete generische Package-Namen
- **Lösung**: Prompt präzisiert mit konkreten Package-Strukturen aus dem Projekt
- **Learning**: LLM braucht Kontext über konkrete Projekt-Struktur

**Herausforderung 2: Über-Engineering**
- **Problem**: Claude schlug zunächst auch Performance-Monitoring-Aspect, Audit-Aspect, Security-Aspect vor
- **Lösung**: Fokussierung auf einen Aspekt (Logging) für Übung 6
- **Learning**: Klare Scope-Definition im Prompt wichtig

**Herausforderung 3: Pointcut-Expression-Debugging**
- **Problem**: Initiale Pointcut-Expression matchte nicht alle gewünschten Methoden
- **Lösung**: Iterative Verfeinerung mit Claude ("Warum matcht diese Expression nicht XYZ?")
- **Learning**: LLM kann gut bei Debugging helfen, wenn man konkrete Beispiele gibt

**Herausforderung 4: Character-Encoding-Probleme**
- **Problem**: Emojis in Log-Ausgaben (📨, ✗) führten zu Encoding-Problemen
- **Lösung**: Emojis durch ASCII-Zeichen ersetzt (→, ←, X)
- **Learning**: LLM-generierter Code muss auf Kompatibilität geprüft werden

