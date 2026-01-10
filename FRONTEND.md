# Frontend-Dokumentation - FoodRescue

## Übersicht

Das FoodRescue-Frontend ist eine modulare JavaScript-Anwendung, die Benutzerauthentifizierung (Login und Registrierung) sowie die Hauptfunktionen der Lebensmittelrettungs-Plattform handhabt. Die Architektur folgt einer klaren Trennung der Verantwortlichkeiten mit ES6-Modulen, Backend-gesteuerter Validierung und benutzerfreundlichem Fehler-Feedback über Toast-Benachrichtigungen.

**Kernprinzip**: Die Validierung erfolgt im **Backend**, um Datenintegrität und Sicherheit zu gewährleisten. Die Rolle des Frontends ist es, Daten zu erfassen, zu übertragen und Ergebnisse anzuzeigen.

---

## Dateistruktur

### HTML-Dateien

```
resources/static/
├── index.html              # Hauptseite (Login/Registrierung)
├── dashboard.html          # Dashboard nach Login
└── register.html           # Anbieter-Registrierung
```

### JavaScript-Module

```
resources/static/js/
├── main.js                 # Einstiegspunkt - initialisiert App und Event-Listener
├── domElements.js          # Speichert DOM-Element-Referenzen
├── authModeToggle.js       # Wechsel zwischen Login ↔ Registrierung
├── authActions.js          # Formular-Übermittlung & API-Kommunikation ⭐
├── toastNotifications.js   # Anzeige von Fehler-/Erfolgsmeldungen
├── dashboardInit.js        # Dashboard-Initialisierungslogik
├── loadAngebot.js          # Laden und Anzeigen von Angeboten
├── handleCreateAngebot.js  # Angebotserstellung für Anbieter
└── register.js             # Anbieter-Registrierungslogik
```

### CSS-Dateien

```
resources/static/css/
├── styles.css              # Styles für Authentifizierungsseiten
└── dashboard.css           # Dashboard-Styles
```

### Modul-Verantwortlichkeiten

| Modul                     | Zweck                                                             |
|---------------------------| ----------------------------------------------------------------- |
| `main.js`                 | Application Bootstrapper - verbindet alle Module                  |
| `domElements.js`          | Zentraler DOM-Element-Cache (Performance-Optimierung)             |
| `authModeToggle.js`       | Verwaltet UI-Zustand zwischen Login- und Registrierungsmodus      |
| `authActions.js`          | Kernlogik: erfasst Daten, ruft Backend auf, verarbeitet Antworten |
| `toastNotifications.js`   | Erstellt animierte Toast-Benachrichtigungen für Benutzer-Feedback |
| `dashboardInit.js`        | Dashboard-Initialisierung mit rollenbasierter UI                  |
| `loadAngebot.js`          | Lädt und zeigt verfügbare Angebote an                             |
| `handleCreateAngebot.js`  | Erstellt neue Angebote (nur für Anbieter)                         |
| `register.js`             | Erweiterte Anbieter-Registrierung mit Profilinformationen         |

---

## Datenfluss: Von Benutzereingabe zur Backend-Validierung

### High-Level-Flow

```
Benutzereingabe (Formular)
    ↓
Frontend sammelt Daten (authActions.js)
    ↓
HTTP POST zu /api/users
    ↓
Backend validiert (UserController.java)
    ↓
Erfolgsantwort (201) ODER Fehlerantwort (400)
    ↓
Frontend zeigt Ergebnis (Toast-Benachrichtigung)
    ↓
Weiterleitung zum Dashboard ODER Verbleib im Formular
```

---

## Detaillierte Datenerfassung & -übertragung

### 1. Benutzer füllt Formular aus

Im **Registrierungsmodus** zeigt das Formular:

- **Namensfeld** (Texteingabe)
- **E-Mail-Feld** (E-Mail-Eingabe)
- **Rollenfeld** (Dropdown: ABHOLER oder ANBIETER)
- **weitere Felder** (nur für Anbieter, z.B. Firmenname, Adresse)

### 2. Frontend erfasst Daten

Bei Formularübermittlung sammelt `authActions.js` die Werte:

```javascript
// Aus authActions.js - handleSignup() Funktion
async function handleSignup() {
  const name = elements.nameInput.value.trim(); // Holt Namen, entfernt Whitespace
  const email = elements.emailInput.value.trim(); // Holt E-Mail, entfernt Whitespace
  const role = elements.roleSelect.value || null; // Holt Rolle, konvertiert "" zu null

  const requestData = {
    name,
    email,
    rolle: role, // Hinweis: Backend erwartet 'rolle' (deutsch)
  };

  // ... sendet Daten weiter
}
```

**Wichtiges Detail**: Leere Rolle (`""`) wird zu `null` konvertiert, um Backend-Erwartungen zu entsprechen.

### 3. HTTP-Request zum Backend

Das Frontend sendet einen POST-Request zur Backend-API:

```javascript
const response = await fetch("/api/users", {
  method: "POST",
  headers: {
    "Content-Type": "application/json",
  },
  body: JSON.stringify(requestData),
});
```

**Request-Beispiel**:

```json
POST /api/users
Content-Type: application/json

{
  "name": "Max Mustermann",
  "email": "max@example.com",
  "rolle": "ABHOLER"
}
```

---

## Backend-Validierungsprozess

### Validierungsreihenfolge

Das Backend (`UserController.java`) validiert Felder in dieser spezifischen Reihenfolge:

1. **Namensvalidierung** - Prüft, ob leer
2. **E-Mail-Validierung** - Prüft, ob leer, dann Formatvalidierung
3. **Rollenvalidierung** - Prüft, ob null

```java
// Aus UserController.java
@PostMapping
public ResponseEntity<UserResponse> registriereUser(@RequestBody RegistriereUserRequest request) {
  // 1. Name validieren
  if (request.getName() == null || request.getName().trim().isEmpty()) {
    throw new IllegalArgumentException("Bitte geben Sie einen Namen ein");
  }

  // 2. E-Mail validieren (Leer-Prüfung)
  if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
    throw new IllegalArgumentException("Bitte geben Sie eine E-Mail-Adresse ein");
  }

  // 3. E-Mail-Format validieren
  new EmailAdresse(request.getEmail());  // Wirft Exception bei ungültigem Format

  // 4. Rolle validieren
  if (request.getRolle() == null) {
    throw new IllegalArgumentException("Bitte wählen Sie eine Rolle aus");
  }

  // Registrierung fortsetzen...
}
```

### Fehlerantwort-Format

Wenn Validierung fehlschlägt, gibt das Backend zurück:

```json
HTTP 400 Bad Request
Content-Type: application/json

{
  "error": "Validation Error",
  "message": "Bitte geben Sie einen Namen ein"
}
```

### Erfolgsantwort-Format

Wenn Validierung erfolgreich ist und Benutzer erstellt wurde:

```json
HTTP 201 Created
Content-Type: application/json

{
  "id": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
  "name": "Max Mustermann",
  "email": "max@example.com",
  "rolle": "ABHOLER"
}
```

---

## Fehleranzeige im Frontend

### Antwortverarbeitung

Das Frontend prüft den HTTP-Antwortstatus:

```javascript
// Aus authActions.js
if (response.ok) {
  // ERFOLGSPFAD (Status 200-299)
  const userData = await response.json();
  showSuccess(`Registrierung erfolgreich! Willkommen ${userData.name}!`);

  // Benutzerdaten speichern und weiterleiten
  localStorage.setItem("isLoggedIn", "true");
  localStorage.setItem("userName", userData.name);
  localStorage.setItem("userEmail", userData.email);
  localStorage.setItem("userRole", userData.rolle);
  localStorage.setItem("userId", userData.id);

  setTimeout(() => {
    window.location.href = "/dashboard.html";
  }, 1500);
} else {
  // FEHLERPFAD (Status 400, 500, etc.)
  let errorMessage = "Registrierung fehlgeschlagen.";

  try {
    const errorData = await response.json();
    errorMessage = errorData.message || errorMessage; // Extrahiere 'message' Feld
  } catch (parseError) {
    const errorText = await response.text();
    if (errorText) {
      errorMessage = errorText;
    }
  }

  showError(errorMessage); // Zeige rote Toast-Benachrichtigung
}
```

### Toast-Benachrichtigungssystem

Fehler werden über animierte Toast-Benachrichtigungen angezeigt:

```javascript
// Aus toastNotifications.js
function showError(message) {
  createToast(message, "#ef4444"); // Roter Hintergrund
}

function showSuccess(message) {
  createToast(message, "#10b981"); // Grüner Hintergrund
}

function createToast(message, backgroundColor) {
  const toast = document.createElement("div");
  toast.style.cssText = `
    position: fixed;
    top: 20px;
    right: 20px;
    background-color: ${backgroundColor};
    color: white;
    padding: 1rem 1.5rem;
    border-radius: 8px;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
    z-index: 2000;
    animation: slideIn 0.3s ease;
  `;
  toast.textContent = message;
  document.body.appendChild(toast);

  // Automatisches Ausblenden nach 4 Sekunden
  setTimeout(() => {
    toast.style.animation = "fadeOut 0.3s ease";
    setTimeout(() => toast.remove(), 300);
  }, 4000);
}
```

**Visuelles Ergebnis**:

- **Roter Toast** erscheint in der oberen rechten Ecke mit Fehlermeldung
- Gleitet von rechts mit sanfter Animation ein
- Verschwindet automatisch nach 4 Sekunden
- Benutzer bleibt im Formular, um Fehler zu korrigieren

---

## Beispiel-Szenarien

### Szenario 1: Leeres Namensfeld

**Benutzereingabe**:

- Name: `""` (leer)
- E-Mail: `max@example.com`
- Rolle: `ABHOLER`

**Ablauf**:

1. Frontend sendet Request mit leerem Namen
2. Backend validiert Namen zuerst → schlägt fehl
3. Gibt zurück: `{"error": "Validation Error", "message": "Bitte geben Sie einen Namen ein"}`
4. Frontend zeigt **roten Toast**: "Bitte geben Sie einen Namen ein"
5. Benutzer bleibt im Formular

---

### Szenario 2: Ungültiges E-Mail-Format

**Benutzereingabe**:

- Name: `Max Mustermann`
- E-Mail: `test` (kein @ oder Domain)
- Rolle: `ABHOLER`

**Ablauf**:

1. Frontend sendet Request
2. Backend validiert Name → erfolgreich
3. Backend validiert E-Mail leer → erfolgreich
4. Backend validiert E-Mail-Format → **schlägt fehl**
5. Gibt zurück: `{"error": "Validation Error", "message": "Email hat ein ungültiges Format: test"}`
6. Frontend zeigt **roten Toast**: "Email hat ein ungültiges Format: test"
7. Benutzer bleibt im Formular

---

### Szenario 3: Keine Rolle ausgewählt

**Benutzereingabe**:

- Name: `Max Mustermann`
- E-Mail: `max@example.com`
- Rolle: `null` (nicht ausgewählt)

**Ablauf**:

1. Frontend sendet Request mit `rolle: null`
2. Backend validiert Name → erfolgreich
3. Backend validiert E-Mail → erfolgreich
4. Backend validiert Rolle → **schlägt fehl**
5. Gibt zurück: `{"error": "Validation Error", "message": "Bitte wählen Sie eine Rolle aus"}`
6. Frontend zeigt **roten Toast**: "Bitte wählen Sie eine Rolle aus"
7. Benutzer bleibt im Formular

---

### Szenario 4: Alle Daten gültig

**Benutzereingabe**:

- Name: `Max Mustermann`
- E-Mail: `max@example.com`
- Rolle: `ABHOLER`

**Ablauf**:

1. Frontend sendet Request
2. Backend validiert alle Felder → **alle erfolgreich**
3. Benutzer wird mit UUID erstellt
4. Gibt zurück: `{"id": "...", "name": "Max Mustermann", "email": "max@example.com", "rolle": "ABHOLER"}`
5. Frontend zeigt **grünen Toast**: "Registrierung erfolgreich! Willkommen Max Mustermann!"
6. Benutzerdaten in localStorage gespeichert
7. Nach 1,5 Sekunden → **Weiterleitung zu `/dashboard.html`**

---

## Login-Prozess

### E-Mail-basierte Anmeldung

```javascript
async function handleLogin() {
    const email = elements.emailInput.value.trim();
    
    // Prüfe mehrere Endpoint-Kandidaten
    const candidates = [
        `/api/users/by-email?email=${encodeURIComponent(email)}`,
        `/api/users?email=${encodeURIComponent(email)}`
    ];

    let userData = null;
    for (const url of candidates) {
        const res = await fetch(url);
        if (res.ok) {
            userData = await res.json();
            break;
        }
    }

    if (!userData || !userData.id) {
        showError("Login fehlgeschlagen: Nutzer nicht gefunden");
        return;
    }

    // Speichere Login-Status
    localStorage.setItem("isLoggedIn", "true");
    localStorage.setItem("userEmail", userData.email);
    localStorage.setItem("userName", userData.name);
    localStorage.setItem("userRole", userData.rolle);
    localStorage.setItem("userId", userData.id);

    showSuccess(`Willkommen zurück, ${userData.name}!`);
    window.location.href = "./dashboard.html";
}
```

---

## Dashboard-Module

### Dashboard-Initialisierung

Das Dashboard passt sich der Benutzerrolle an:

```javascript
// Aus dashboardInit.js
function applyRoleLayout() {
    const role = localStorage.getItem("userRole");

    if (role === "ANBIETER") {
        // Zeige Angebotserstellung
        // Zeige "Meine Angebote"
        // Navigation: "Angebote erstellen"
    } else if (role === "ABHOLER") {
        // Zeige "Angebote finden"
        // Zeige "Meine Reservierungen"
        // Navigation: "Angebote suchen"
    }
}
```

### Angebotsverwaltung

**Für Anbieter** (`handleCreateAngebot.js`):
- Formular zur Angebotserstellung
- Felder: Titel, Beschreibung, Menge, Abholzeitfenster
- POST zu `/api/angebote`

**Für Abholer** (`loadAngebot.js`):
- Anzeige verfügbarer Angebote
- GET von `/api/angebote/verfuegbar`
- Reservierungs-Button für jedes Angebot

---

## Backend-Integrationspunkte

### Backend-Dateien

| Datei                             | Zweck                                              |
| --------------------------------- | -------------------------------------------------- |
| `UserController.java`             | REST-Endpoint `/api/users` - verarbeitet POST-Requests |
| `RegistriereUserRequest.java`     | DTO für eingehende JSON-Daten                      |
| `EmailAdresse.java`               | Value Object mit E-Mail-Formatvalidierung          |
| `Name.java`                       | Value Object mit Namensvalidierung                 |
| `User.java`                       | Domain-Entity                                      |
| `GlobalExceptionHandler.java`     | Fängt Exceptions ab und formatiert Fehlerantworten |

### Validierungsebenen

Die Validierung erfolgt in mehreren Schichten:

1. **Controller-Schicht** (`UserController.java`):
    - Null/Leer-Prüfungen
    - Feldpräsenz-Validierung
    - E-Mail-Formatvalidierung (via `EmailAdresse`-Konstruktor)

2. **Value Objects** (`EmailAdresse.java`, `Name.java`):
    - Formatvalidierung
    - Durchsetzung von Geschäftsregeln
    - Wirft `IllegalArgumentException` bei ungültigen Daten

3. **Exception Handler** (`GlobalExceptionHandler.java`):
    - Fängt `IllegalArgumentException` → gibt 400 mit Nachricht zurück
    - Fängt `HttpMessageNotReadableException` → gibt 400 mit generischer Nachricht zurück
    - Fängt andere Exceptions → gibt entsprechende Fehlerantwort zurück

### Fehlermeldungs-Mapping

| Backend-Validierung   | Fehlermeldung (Deutsch)                                       |
| --------------------- | ------------------------------------------------------------- |
| Leerer Name           | "Bitte geben Sie einen Namen ein"                             |
| Leere E-Mail          | "Bitte geben Sie eine E-Mail-Adresse ein"                     |
| Ungültiges E-Mail-Format | "Email hat ein ungültiges Format: [Eingabe]"               |
| Fehlende Rolle        | "Bitte wählen Sie eine Rolle aus"                             |
| JSON-Parse-Fehler     | "Ungültige Eingabedaten. Bitte überprüfen Sie Ihre Angaben." |

---

## Wichtige Architekturentscheidungen

### 1. Backend-gesteuerte Validierung

**Warum**: Gewährleistet konsistente Validierungslogik, verhindert clientseitiges Umgehen, Single Source of Truth.

### 2. Spezifische Fehlermeldungen

**Warum**: Bessere UX - Benutzer wissen genau, was sie korrigieren müssen, statt generischer "Ungültige Eingabe"-Nachrichten.

### 3. Validierungsreihenfolge ist wichtig

**Warum**: Benutzer sehen den ersten Fehler, den sie beheben müssen, verhindert Verwirrung durch mehrere Fehler gleichzeitig.

### 4. Modulare JavaScript-Architektur

**Warum**: Wartbarkeit, Testbarkeit, klare Trennung der Verantwortlichkeiten.

### 5. Toast-Benachrichtigungen

**Warum**: Nicht-blockierendes Feedback, erfordert kein Neuladen der Seite, modernes UX-Pattern.

### 6. Rollenbasierte UI

**Warum**: Benutzer sehen nur relevante Funktionen, reduziert Komplexität, verbessert Usability.

---

## Zusätzliche Frontend-Features

### Angebotserstellung (nur Anbieter)

```javascript
// Aus handleCreateAngebot.js
async function handleCreateAngebot(event) {
    event.preventDefault();
    
    const angebotData = {
        anbieterId: localStorage.getItem("userId"),
        titel: document.getElementById("titel").value,
        beschreibung: document.getElementById("beschreibung").value,
        menge: parseInt(document.getElementById("menge").value),
        mengenEinheit: document.getElementById("einheit").value,
        abholfensterStart: document.getElementById("start").value,
        abholfensterEnde: document.getElementById("ende").value
    };

    const response = await fetch("/api/angebote", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(angebotData)
    });

    if (response.ok) {
        showSuccess("Angebot erfolgreich erstellt!");
        // Formular zurücksetzen und Liste neu laden
    }
}
```

### Angebote laden (für Abholer)

```javascript
// Aus loadAngebot.js
async function loadVerfuegbareAngebote() {
    const response = await fetch("/api/angebote/verfuegbar");
    const angebote = await response.json();
    
    const container = document.getElementById("angebote-container");
    container.innerHTML = angebote.map(angebot => `
        <div class="angebot-card">
            <h3>${angebot.titel}</h3>
            <p>${angebot.beschreibung}</p>
            <p>Menge: ${angebot.menge} ${angebot.mengenEinheit}</p>
            <button onclick="reservieren('${angebot.id}')">
                Reservieren
            </button>
        </div>
    `).join('');
}
```

---

## Session-Management

### LocalStorage-basiert

```javascript
// Login
localStorage.setItem("isLoggedIn", "true");
localStorage.setItem("userName", userData.name);
localStorage.setItem("userEmail", userData.email);
localStorage.setItem("userRole", userData.rolle);
localStorage.setItem("userId", userData.id);

// Auth-Check (in dashboardInit.js)
function checkAuth() {
    const isLoggedIn = localStorage.getItem("isLoggedIn");
    if (!isLoggedIn || isLoggedIn !== "true") {
        window.location.href = "./index.html";
        return false;
    }
    return true;
}

// Logout
function handleLogout() {
    localStorage.clear();
    window.location.href = "/index.html";
}
```

---

## Performance-Optimierungen

### DOM-Element-Caching

```javascript
// domElements.js
export const elements = {
    form: document.getElementById("auth-form"),
    nameInput: document.getElementById("name"),
    emailInput: document.getElementById("email"),
    roleSelect: document.getElementById("role"),
    submitBtn: document.getElementById("submit-btn"),
    // ... weitere Elemente
};
```

Verhindert wiederholte DOM-Queries und verbessert Performance.

---

## Testing-Ansatz

### Manuelles Testing

- Browser-DevTools für Netzwerk-Requests
- Console-Logging für Debugging
- Toast-Benachrichtigungen für User-Feedback

### Automatisiertes Testing (erweiterbar)

- Jest für Unit-Tests
- Cypress für E2E-Tests
- Testing Library für DOM-Testing

---

## Zusammenfassung

Das FoodRescue-Frontend folgt einem sauberen **Erfassen → Übertragen → Validieren → Anzeigen** Muster:

1. **Erfassen**: Formulareingaben werden via `authActions.js` gesammelt
2. **Übertragen**: JSON-Daten werden an `/api/users` Endpoint gesendet
3. **Validieren**: Backend validiert in Reihenfolge (Name → E-Mail → Rolle)
4. **Anzeigen**: Frontend zeigt spezifische Fehlermeldungen via Toast-Benachrichtigungen

Diese Architektur gewährleistet:

- Datenintegrität (Backend-Validierung)
- Klares Benutzer-Feedback (spezifische Fehlermeldungen)
- Gute UX (Toast-Benachrichtigungen, keine Seitenneuladungen)
- Wartbarer Code (modulare Struktur)
- Rollenbasierte Funktionalität (ANBIETER vs. ABHOLER)

---


**Letzte Aktualisierung**: 10. Januar 2026

