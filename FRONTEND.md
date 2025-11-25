# Frontend Documentation - FoodRescue Authentication System

## 📋 Overview

The FoodRescue frontend is a modular JavaScript application that handles user authentication (login and signup). The architecture follows a clean separation of concerns with ES6 modules, backend-driven validation, and user-friendly error feedback via toast notifications.

**Key Principle**: Validation happens on the **backend**, ensuring data integrity and security. The frontend's role is to capture, transmit, and display results.

---

## 📁 File Structure

### HTML

```
backend/src/main/resources/static/
├── index.html              # Main authentication page (login/signup form)
└── dashboard.html          # Post-login dashboard
```

### JavaScript Modules

```
backend/src/main/resources/static/js/
├── main.js                 # Entry point - initializes app and event listeners
├── domElements.js          # Caches DOM element references
├── authModeToggle.js       # Handles login ↔ signup mode switching
├── authActions.js          # Form submission & API communication ⭐
├── toastNotifications.js   # Error/success message display
└── dashboardInit.js        # Dashboard initialization logic
```

### CSS

```
backend/src/main/resources/static/css/
├── styles.css              # Authentication page styles
└── dashboard.css           # Dashboard styles
```

### Module Responsibilities

| Module                  | Purpose                                                         |
| ----------------------- | --------------------------------------------------------------- |
| `main.js`               | Application bootstrapper - wires up all modules                 |
| `domElements.js`        | Centralized DOM element cache (performance optimization)        |
| `authModeToggle.js`     | Manages UI state between login and signup modes                 |
| **`authActions.js`**    | **Core logic: captures data, calls backend, handles responses** |
| `toastNotifications.js` | Creates animated toast notifications for user feedback          |

---

## Data Flow: From User Input to Backend Validation

### High-Level Flow

```
User Input (Form)
    ↓
Frontend Collects Data (authActions.js)
    ↓
HTTP POST to /api/users
    ↓
Backend Validates (UserController.java)
    ↓
Success Response (201) OR Error Response (400)
    ↓
Frontend Displays Result (Toast Notification)
    ↓
Redirect to Dashboard OR Stay on Form
```

---

## Detailed Data Capture & Transmission

### 1. User Fills Form

When a user is in **signup mode**, the form displays:

- **Name field** (text input)
- **Email field** (email input)
- **Role field** (dropdown: ABHOLER or ANBIETER)

### 2. Frontend Captures Data

When the form is submitted, `authActions.js` collects the values:

```javascript
// From authActions.js - handleSignup() function
async function handleSignup() {
  const name = elements.nameInput.value.trim(); // Gets name, removes whitespace
  const email = elements.emailInput.value.trim(); // Gets email, removes whitespace
  const role = elements.roleSelect.value || null; // Gets role, converts "" to null

  const requestData = {
    name,
    email,
    rolle: role, // Note: Backend expects 'rolle' (German)
  };

  // ... continues to send data
}
```

**Key Detail**: Empty role (`""`) is converted to `null` to match backend expectations.

### 3. HTTP Request to Backend

The frontend sends a POST request to the backend API:

```javascript
const response = await fetch("/api/users", {
  method: "POST",
  headers: {
    "Content-Type": "application/json",
  },
  body: JSON.stringify(requestData),
});
```

**Request Example**:

```json
POST /api/users
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john@example.com",
  "rolle": "ABHOLER"
}
```

---

## Backend Validation Process

### Validation Order

The backend (`UserController.java`) validates fields in this specific order:

1. **Name validation** - Check if empty
2. **Email validation** - Check if empty, then validate format
3. **Role validation** - Check if null

```java
// From UserController.java
@PostMapping
public ResponseEntity<UserResponse> registriereUser(@RequestBody RegistriereUserRequest request) {
  // 1. Validate name
  if (request.getName() == null || request.getName().trim().isEmpty()) {
    throw new IllegalArgumentException("Bitte geben Sie einen Namen ein");
  }

  // 2. Validate email (empty check)
  if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
    throw new IllegalArgumentException("Bitte geben Sie eine E-Mail-Adresse ein");
  }

  // 3. Validate email format
  new EmailAdresse(request.getEmail());  // Throws if invalid format

  // 4. Validate role
  if (request.getRolle() == null) {
    throw new IllegalArgumentException("Bitte wählen Sie eine Rolle aus");
  }

  // Continue with registration...
}
```

### Error Response Format

When validation fails, the backend returns:

```json
HTTP 400 Bad Request
Content-Type: application/json

{
  "error": "Validation Error",
  "message": "Bitte geben Sie einen Namen ein"
}
```

### Success Response Format

When validation passes and user is created:

```json
HTTP 201 Created
Content-Type: application/json

{
  "id": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
  "name": "John Doe",
  "email": "john@example.com",
  "rolle": "ABHOLER"
}
```

---

## Error Display on Frontend

### Response Handling

The frontend checks the HTTP response status:

```javascript
// From authActions.js
if (response.ok) {
  // SUCCESS PATH (status 200-299)
  const userData = await response.json();
  showSuccess(`Registrierung erfolgreich! Willkommen ${userData.name}!`);

  // Store user data and redirect
  localStorage.setItem("isLoggedIn", "true");
  localStorage.setItem("userName", userData.name);
  localStorage.setItem("userEmail", userData.email);
  localStorage.setItem("userRole", userData.rolle);
  localStorage.setItem("userId", userData.id);

  setTimeout(() => {
    window.location.href = "/dashboard.html";
  }, 1500);
} else {
  // ERROR PATH (status 400, 500, etc.)
  let errorMessage = "Registrierung fehlgeschlagen.";

  try {
    const errorData = await response.json();
    errorMessage = errorData.message || errorMessage; // Extract 'message' field
  } catch (parseError) {
    const errorText = await response.text();
    if (errorText) {
      errorMessage = errorText;
    }
  }

  showError(errorMessage); // Display red toast notification
}
```

### Toast Notification System

Errors are displayed using animated toast notifications:

```javascript
// From toastNotifications.js
function showError(message) {
  createToast(message, "#ef4444"); // Red background
}

function showSuccess(message) {
  createToast(message, "#10b981"); // Green background
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

  // Auto-dismiss after 4 seconds
  setTimeout(() => {
    toast.style.animation = "fadeOut 0.3s ease";
    setTimeout(() => toast.remove(), 300);
  }, 4000);
}
```

**Visual Result**:

- **Red toast** appears at top-right corner with error message
- Slides in from right with smooth animation
- Auto-disappears after 4 seconds
- User remains on form to correct the error

---

## 📝 Example Scenarios

### Scenario 1: Empty Name Field

**User Input**:

- Name: `""` (empty)
- Email: `john@example.com`
- Role: `ABHOLER`

**Flow**:

1. Frontend sends request with empty name
2. Backend validates name first → fails
3. Returns: `{"error": "Validation Error", "message": "Bitte geben Sie einen Namen ein"}`
4. Frontend displays **red toast**: "Bitte geben Sie einen Namen ein"
5. User stays on form

---

### Scenario 2: Invalid Email Format

**User Input**:

- Name: `John Doe`
- Email: `test` (no @ or domain)
- Role: `ABHOLER`

**Flow**:

1. Frontend sends request
2. Backend validates name → passes
3. Backend validates email empty → passes
4. Backend validates email format → **fails**
5. Returns: `{"error": "Validation Error", "message": "Email hat ein ungültiges Format: test"}`
6. Frontend displays **red toast**: "Email hat ein ungültiges Format: test"
7. User stays on form

---

### Scenario 3: No Role Selected

**User Input**:

- Name: `John Doe`
- Email: `john@example.com`
- Role: `null` (not selected)

**Flow**:

1. Frontend sends request with `rolle: null`
2. Backend validates name → passes
3. Backend validates email → passes
4. Backend validates role → **fails**
5. Returns: `{"error": "Validation Error", "message": "Bitte wählen Sie eine Rolle aus"}`
6. Frontend displays **red toast**: "Bitte wählen Sie eine Rolle aus"
7. User stays on form

---

### Scenario 4: All Valid Data

**User Input**:

- Name: `John Doe`
- Email: `john@example.com`
- Role: `ABHOLER`

**Flow**:

1. Frontend sends request
2. Backend validates all fields → **all pass**
3. User is created with UUID
4. Returns: `{"id": "...", "name": "John Doe", "email": "john@example.com", "rolle": "ABHOLER"}`
5. Frontend displays **green toast**: "Registrierung erfolgreich! Willkommen John Doe!"
6. User data stored in localStorage
7. After 1.5 seconds → **redirect to `/dashboard.html`**

---

## Backend Integration Points

### Backend Files

| File                          | Purpose                                            |
| ----------------------------- | -------------------------------------------------- |
| `UserController.java`         | REST endpoint `/api/users` - handles POST requests |
| `RegistriereUserRequest.java` | DTO for incoming JSON data                         |
| `EmailAdresse.java`           | Value object with email format validation          |
| `Name.java`                   | Value object with name validation                  |
| `User.java`                   | Domain entity                                      |
| `GlobalExceptionHandler.java` | Catches exceptions and formats error responses     |

### Validation Layer

The validation happens in multiple layers:

1. **Controller Layer** (`UserController.java`):

   - Null/empty checks
   - Field presence validation
   - Email format validation (via `EmailAdresse` constructor)

2. **Value Objects** (`EmailAdresse.java`, `Name.java`):

   - Format validation
   - Business rules enforcement
   - Throws `IllegalArgumentException` on invalid data

3. **Exception Handler** (`GlobalExceptionHandler.java`):
   - Catches `IllegalArgumentException` → returns 400 with message
   - Catches `HttpMessageNotReadableException` → returns 400 with generic message
   - Catches other exceptions → returns appropriate error response

### Error Message Mapping

| Backend Validation   | Error Message (German)                                       |
| -------------------- | ------------------------------------------------------------ |
| Empty name           | "Bitte geben Sie einen Namen ein"                            |
| Empty email          | "Bitte geben Sie eine E-Mail-Adresse ein"                    |
| Invalid email format | "Email hat ein ungültiges Format: [input]"                   |
| Missing role         | "Bitte wählen Sie eine Rolle aus"                            |
| JSON parse error     | "Ungültige Eingabedaten. Bitte überprüfen Sie Ihre Angaben." |

---

## Key Architectural Decisions

### 1. Backend-Driven Validation

**Why**: Ensures consistent validation logic, prevents client-side bypass, single source of truth.

### 2. Specific Error Messages

**Why**: Better UX - users know exactly what to fix instead of generic "Invalid input" messages.

### 3. Validation Order Matters

**Why**: Users see the first error they need to fix, preventing confusion from multiple errors at once.

### 4. Modular JavaScript Architecture

**Why**: Maintainability, testability, clear separation of concerns.

### 5. Toast Notifications

**Why**: Non-blocking feedback, doesn't require page reload, modern UX pattern.

---

## Summary

The FoodRescue frontend follows a clean **capture → transmit → validate → display** pattern:

1. **Capture**: Form inputs are collected via `authActions.js`
2. **Transmit**: JSON data sent to `/api/users` endpoint
3. **Validate**: Backend validates in order (name → email → role)
4. **Display**: Frontend shows specific error messages via toast notifications

This architecture ensures:

- Data integrity (backend validation)
- Clear user feedback (specific error messages)
- Good UX (toast notifications, no page reloads)
- Maintainable code (modular structure)

---

**Last Updated**: November 23, 2025
