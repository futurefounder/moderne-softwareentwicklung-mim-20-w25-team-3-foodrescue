# FoodRescue - Lebensmittelrettungs-Plattform - Projektübersicht - README

## Projektübersicht

FoodRescue ist eine webbasierte Plattform zur Rettung von Lebensmitteln, die Anbieter (Restaurants, Bäckereien, Supermärkte) mit Abholern (Privatpersonen, Tafeln, soziale Einrichtungen) verbindet. Das Projekt folgt Domain-Driven Design (DDD) und Clean Architecture Prinzipien.

## Hauptfunktionen

- **Angebotserstellung**: Anbieter können überschüssige Lebensmittel als Angebote einstellen
- **Angebotssuche**: Abholer können verfügbare Angebote durchsuchen und reservieren
- **Reservierungssystem**: Zeitfenster-basierte Reservierungen mit Abholcodes
- **Abholungsverwaltung**: Bestätigung der Abholung mittels generierten Codes
- **Rollensystem**: Unterschiedliche Berechtigungen für Anbieter und Abholer

## Architektur

### Backend: Domain-Driven Design mit 5 Bounded Contexts

```
FoodRescue
├── Angebotsmanagement       # Verwaltung von Lebensmittelangeboten
├── Reservierungsmanagement  # Reservierungen und Rettungslogik
├── Abholungsmanagement      # Abholprozess mit Codes und Zeitfenstern
├── Userverwaltung          # Benutzer, Rollen, Anbieterprofile
└── Shared                  # Gemeinsame Komponenten (Events, Exceptions, AOP)
```

### Frontend: Modulares JavaScript

```
Frontend
├── Authentifizierung        # Login & Registrierung
├── Dashboard               # Rollenbasierte Hauptansicht
├── Angebotsverwaltung      # Erstellen, Anzeigen, Bearbeiten
└── Reservierungssystem     # Angebote reservieren und abholen
```

## Technologie-Stack

### Backend
- **Java 21** - Moderne Java-Features
- **Spring Boot 3.3.5** - Application Framework
- **Clean Architecture** - Layered Design (Domain, Application, Infrastructure)
- **DDD Pattern** - Aggregate Roots, Value Objects, Domain Events
- **In-Memory Persistence** - ConcurrentHashMap (produktionsbereit für JPA-Migration)

### Frontend
- **Vanilla JavaScript (ES6 Modules)** - Keine Framework-Abhängigkeiten
- **HTML5 & CSS3** - Semantic HTML, Responsive Design
- **Toast Notifications** - Benutzerfreundliches Feedback-System
- **LocalStorage** - Session Management

### Build & Deployment
- **Maven** - Dependency Management & Build
- **GitHub Actions** - CI/CD Pipeline
- **GitHub Pages** - Automatisches Deployment

## Projektstruktur

```
foodrescue/
├── java/com/foodrescue/
│   ├── angebotsmanagement/
│   │   ├── domain/          # Angebot, Status, Events
│   │   ├── application/     # Services, Commands
│   │   └── infrastructure/  # REST Controller, Repositories
│   ├── reservierungsmanagement/
│   │   ├── domain/          # Reservierung, Events
│   │   ├── application/     # RescueService, Commands
│   │   └── infrastructure/  # REST, Event Handlers
│   ├── abholungsmanagement/
│   │   ├── domain/          # Abholung, Abholcode, Zeitfenster
│   │   ├── application/     # Abholungsservice
│   │   └── infrastructure/  # REST Controller
│   ├── userverwaltung/
│   │   ├── domain/          # User, Rolle, EmailAdresse, AnbieterProfil
│   │   ├── application/     # User Services, Queries
│   │   └── infrastructure/  # REST, Repositories
│   └── shared/
│       ├── domain/          # AggregateRoot, DomainEvent
│       ├── exception/       # GlobalExceptionHandler
│       └── aop/             # LoggingAspect
└── resources/static/
    ├── index.html           # Login/Registrierung
    ├── dashboard.html       # Hauptanwendung
    ├── register.html        # Anbieter-Registrierung
    ├── js/                  # JavaScript Module
    │   ├── main.js          # Entry Point
    │   ├── authActions.js   # API-Kommunikation
    │   ├── dashboardInit.js # Dashboard-Logik
    │   ├── loadAngebot.js   # Angebote laden
    │   └── ...
    ├── css/                 # Stylesheets
    └── img/                 # Bilder & Assets
```

## Installation & Start

### Voraussetzungen
- Java 21+
- Maven 3.8+
- Git

### Projekt klonen
```bash
git https://github.com/futurefounder/moderne-softwareentwicklung-mim-20-w25-team-3-foodrescue.git
cd foodrescue
```

### Backend starten
```bash
# Mit Maven
mvn spring-boot:run

# Oder als JAR
mvn clean package
java -jar target/foodrescue-1.0.0.jar
```

Die Anwendung ist dann unter **http://localhost:8080/index.html** erreichbar.

### Tests ausführen
```bash
# Alle Tests
mvn test

# Mit Coverage Report
mvn clean test jacoco:report
```

## 📖 API-Dokumentation

### User Management (`/api/users`)

**POST /api/users** - Neuen Benutzer registrieren
```json
{
  "name": "Max Mustermann",
  "email": "max@example.com",
  "rolle": "ABHOLER"
}
```

**GET /api/users/by-email?email={email}** - Benutzer per E-Mail abrufen

### Angebotsmanagement (`/api/angebote`)

**POST /api/angebote** - Neues Angebot erstellen
```json
{
  "anbieterId": "uuid",
  "titel": "Frisches Brot",
  "beschreibung": "5 Brote vom Vortag",
  "menge": 5,
  "mengenEinheit": "Stück",
  "abholfensterStart": "2024-11-17T08:00:00",
  "abholfensterEnde": "2024-11-17T10:00:00"
}
```

**GET /api/angebote/verfuegbar** - Alle verfügbaren Angebote

**GET /api/angebote/anbieter/{anbieterId}** - Angebote eines Anbieters

### Reservierungsmanagement (`/api/reservierungen`)

**POST /api/reservierungen** - Angebot reservieren
```json
{
  "angebotId": "uuid",
  "abholerId": "uuid"
}
```

**GET /api/reservierungen/abholer/{abholerId}** - Reservierungen eines Abholers

### Abholungsmanagement (`/api/abholungen`)

**POST /api/abholungen/{id}/bestaetigen** - Abholung bestätigen
```json
{
  "abholcode": "A1B2C3"
}
```

## Authentifizierung

Das System verwendet ein einfaches E-Mail-basiertes Authentifizierungssystem:

1. **Registrierung**: Benutzer wählen Rolle (ANBIETER oder ABHOLER)
2. **Login**: E-Mail-basierte Anmeldung
3. **Session**: LocalStorage-basierte Session-Verwaltung
4. **Rollensystem**: UI passt sich der Benutzerrolle an

## Frontend-Module

### Hauptmodule

- **`main.js`** - Einstiegspunkt, initialisiert alle Module
- **`authActions.js`** - Login/Signup-Logik, API-Kommunikation
- **`dashboardInit.js`** - Dashboard-Initialisierung, rollenbasierte UI
- **`loadAngebot.js`** - Angebote laden und anzeigen
- **`handleCreateAngebot.js`** - Angebotserstellung für Anbieter
- **`toastNotifications.js`** - Benutzer-Feedback-System

### UI-Besonderheiten

- **Rollenbasierte Ansichten**: Unterschiedliche UI für Anbieter und Abholer
- **Responsive Design**: Mobile-First Ansatz
- **Toast Notifications**: Nicht-blockierendes Feedback
- **Kein Framework**: Pure JavaScript für maximale Performance

## Testing

Das Projekt verfügt über umfassende Tests:

- **Unit Tests**: Domain-Logik und Services
- **Integration Tests**: API-Endpoints
- **Web Tests**: Controller mit @WebMvcTest

Test-Coverage wird mit JaCoCo gemessen und ist im CI/CD-Bericht verfügbar.

## Code-Qualität

- **Spotless**: Code-Formatierung (Google Java Format)
- **SonarQube-ready**: Konfiguration vorhanden
- **LoggingAspect**: AOP-basiertes automatisches Logging
- **Exception Handling**: Globaler Exception Handler

## Domain Events

Das System nutzt Domain Events für lose Kopplung zwischen Bounded Contexts:

- **AngebotErstelltEvent** → Logging, Benachrichtigungen
- **AngebotReserviertEvent** → Status-Updates, Abholungserstellung
- **ReservierungErstellt** → Event-Handler für Cross-Context-Logik

## Validierung

### Backend-Validierung (Domain Layer)

- **EmailAdresse**: Format-Validierung mit Regex
- **Name**: Nicht leer, Whitespace-Trimming
- **Rolle**: Enum-Validierung (ANBIETER, ABHOLER)
- **Abholcode**: 6-stelliger alphanumerischer Code

### Frontend-Validierung

- Minimal (Trimming, Null-Checks)
- Hauptvalidierung erfolgt im Backend
- Fehler werden als Toast-Nachrichten angezeigt

## Deployment

Das Projekt nutzt GitHub Actions für automatisches Deployment:

- **Trigger**: Push zu `main` oder `dev` Branch
- **Pipeline**: Build → Test → Package → Deploy
- **Target**: GitHub Pages
- **Artefakte**: Frontend + Maven Site Documentation

Siehe [DEPLOYMENT.md](DEPLOYMENT.md) für Details.

## Weitere Dokumentation

- **[FRONTEND.md](FRONTEND.md)** - Detaillierte Frontend-Dokumentation
- **[DEPLOYMENT.md](DEPLOYMENT.md)** - Deployment-Setup und CI/CD
- **JavaDocs** - Im Code verfügbar, generierbar mit `mvn javadoc:javadoc`

## Entwicklungsprozess

### Branch-Strategie
- `main` - Produktiv-Branch
- `dev` - Entwicklungs-Branch
- Feature-Branches für neue Features

### Code-Style
- Google Java Format (via Spotless)
- ESLint für JavaScript (konfigurierbar)
- Semantic HTML

## Bekannte Einschränkungen

- **Keine Persistenz**: Daten gehen bei Server-Neustart verloren (In-Memory)
- **Keine echte Authentifizierung**: Kein Password-System
- **Keine E-Mail-Benachrichtigungen**: Events werden nur geloggt
- **Keine Bilduploads**: Angebote haben keine Fotos
- **Kein Bewertungssystem**: Keine Bewertungen für Anbieter/Abholer
- **Kein Mobile App**: Nur responsive Web-App
- **Kein Admin-Dashboard**: Keine Admin-Funktionen
- **Eingeschränkte Tests**: Fokus auf Backend, Frontend-Tests fehlen


## Roadmap

- [ ] JPA/Hibernate Integration für Datenpersistenz
- [ ] Spring Security Integration
- [ ] E-Mail-Benachrichtigungen
- [ ] Bildupload für Angebote
- [ ] Bewertungssystem
- [ ] Mobile App (Progressive Web App)
- [ ] Admin-Dashboard


---

**Entwickelt mit ❤️ für die Rettung von Lebensmitteln**