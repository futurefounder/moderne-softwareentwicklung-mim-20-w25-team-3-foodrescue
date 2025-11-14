// ===================================
// STATE MANAGEMENT
// ===================================
let isSignupMode = false;

// ===================================
// DOM ELEMENTS
// ===================================
const elements = {
  form: document.getElementById("auth-form"),
  formTitle: document.getElementById("form-title"),
  formSubtitle: document.getElementById("form-subtitle"),
  submitBtn: document.getElementById("submit-btn"),
  toggleText: document.getElementById("toggle-text"),
  toggleMode: document.getElementById("toggle-mode"),

  // Form fields
  nameGroup: document.getElementById("name-group"),
  nameInput: document.getElementById("name"),
  emailInput: document.getElementById("email"),
  roleGroup: document.getElementById("role-group"),
  roleSelect: document.getElementById("role"),

  // Links
  forgotPasswordLink: document.getElementById("forgot-password-link"),
  forgotPassword: document.getElementById("forgot-password"),

  // Modal
  modal: document.getElementById("forgot-password-modal"),
  closeModal: document.getElementById("close-modal"),
  modalOk: document.getElementById("modal-ok"),
};

// ===================================
// MODE SWITCHING
// ===================================
function switchToSignupMode() {
  isSignupMode = true;

  // Update header
  elements.formTitle.textContent = "Werde FoodRescuer/in!";
  elements.formSubtitle.textContent = "Erstelle dein FoodRescue Konto";

  // Show signup-specific fields
  elements.nameGroup.style.display = "flex";
  elements.roleGroup.style.display = "flex";

  // Set required attributes
  elements.nameInput.setAttribute("required", "required");
  elements.roleSelect.setAttribute("required", "required");

  // Update button text
  elements.submitBtn.textContent = "Konto erstellen";

  // Update toggle text
  elements.toggleText.innerHTML =
    'Bereits ein Konto? <a href="#" class="link-primary" id="toggle-mode">Anmelden</a>';

  // Reattach event listener to new link
  document
    .getElementById("toggle-mode")
    .addEventListener("click", handleToggleMode);
}

function switchToLoginMode() {
  isSignupMode = false;

  // Update header
  elements.formTitle.textContent = "Save Food, Fight Waste";
  elements.formSubtitle.textContent = "Bitte melde dich an";

  // Hide signup-specific fields
  elements.nameGroup.style.display = "none";
  elements.roleGroup.style.display = "none";

  // Remove required attributes
  elements.nameInput.removeAttribute("required");
  elements.roleSelect.removeAttribute("required");

  // Update button text
  elements.submitBtn.textContent = "Anmelden";

  // Update toggle text
  elements.toggleText.innerHTML =
    'Noch kein Konto? <a href="#" class="link-primary" id="toggle-mode">Registrieren</a>';

  // Reattach event listener to new link
  document
    .getElementById("toggle-mode")
    .addEventListener("click", handleToggleMode);

  // Clear form
  elements.form.reset();
}

// ===================================
// EVENT HANDLERS
// ===================================
function handleToggleMode(e) {
  e.preventDefault();

  if (isSignupMode) {
    switchToLoginMode();
  } else {
    switchToSignupMode();
  }
}

function handleFormSubmit(e) {
  e.preventDefault();

  if (isSignupMode) {
    handleSignup();
  } else {
    handleLogin();
  }
}

function handleLogin() {
  const email = elements.emailInput.value.trim();

  // Basic validation
  if (!email) {
    showError("Bitte E-Mail-Adresse eingeben.");
    return;
  }

  if (!isValidEmail(email)) {
    showError("Bitte geben Sie eine gültige E-Mail-Adresse ein.");
    return;
  }

  // Log for now (login functionality not yet implemented)
  console.log("Login attempt:", { email });

  // Show info message
  showError("Login-Funktionalität ist noch nicht implementiert.");
}

async function handleSignup() {
  const name = elements.nameInput.value.trim();
  const email = elements.emailInput.value.trim();
  const role = elements.roleSelect.value;

  // Basic validation
  if (!name || !email || !role) {
    showError("Bitte füllen Sie alle Felder aus.");
    return;
  }

  if (!isValidEmail(email)) {
    showError("Bitte geben Sie eine gültige E-Mail-Adresse ein.");
    return;
  }

  // Prepare request data matching backend's RegistriereUserRequest
  const requestData = {
    name: name,
    email: email,
    rolle: role, // ABHOLER or ANBIETER
  };

  try {
    // Disable submit button to prevent double submission
    elements.submitBtn.disabled = true;
    elements.submitBtn.textContent = "Wird registriert...";

    // Make POST request to backend
    const response = await fetch("/api/users", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(requestData),
    });

    if (response.ok) {
      // Success - status 201 (CREATED)
      const userData = await response.json();
      console.log("User registered successfully:", userData);
      showSuccess(`Registrierung erfolgreich! Willkommen ${userData.name}!`);

      // Switch back to login mode after short delay
      setTimeout(() => {
        switchToLoginMode();
      }, 2000);
    } else {
      // Backend validation failed
      let errorMessage = "Registrierung fehlgeschlagen.";

      try {
        const errorData = await response.json();
        errorMessage = errorData.message || errorMessage;
      } catch (e) {
        // If response is not JSON, try to parse as text
        const errorText = await response.text();
        if (errorText) {
          errorMessage = errorText;
        }
      }

      console.error("Registration failed:", response.status, errorMessage);
      showError(errorMessage);
    }
  } catch (error) {
    // Network error or other issues
    console.error("Error during registration:", error);
    showError("Verbindungsfehler. Bitte versuchen Sie es später erneut.");
  } finally {
    // Re-enable submit button
    elements.submitBtn.disabled = false;
    elements.submitBtn.textContent = "Konto erstellen";
  }
}

function handleForgotPassword(e) {
  e.preventDefault();
  openModal();
}

// ===================================
// MODAL FUNCTIONS
// ===================================
function openModal() {
  elements.modal.classList.add("active");
  document.body.style.overflow = "hidden";
}

function closeModal() {
  elements.modal.classList.remove("active");
  document.body.style.overflow = "auto";
}

// ===================================
// VALIDATION HELPERS
// ===================================
function isValidEmail(email) {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return emailRegex.test(email);
}

// ===================================
// UI FEEDBACK
// ===================================
function showError(message) {
  // Create temporary error message
  const errorDiv = document.createElement("div");
  errorDiv.style.cssText = `
        position: fixed;
        top: 20px;
        right: 20px;
        background-color: #ef4444;
        color: white;
        padding: 1rem 1.5rem;
        border-radius: 8px;
        box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        z-index: 2000;
        animation: slideIn 0.3s ease;
        max-width: 400px;
    `;
  errorDiv.textContent = message;

  document.body.appendChild(errorDiv);

  // Remove after 4 seconds
  setTimeout(() => {
    errorDiv.style.animation = "fadeOut 0.3s ease";
    setTimeout(() => errorDiv.remove(), 300);
  }, 4000);
}

function showSuccess(message) {
  // Create temporary success message
  const successDiv = document.createElement("div");
  successDiv.style.cssText = `
        position: fixed;
        top: 20px;
        right: 20px;
        background-color: #10b981;
        color: white;
        padding: 1rem 1.5rem;
        border-radius: 8px;
        box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        z-index: 2000;
        animation: slideIn 0.3s ease;
        max-width: 400px;
    `;
  successDiv.textContent = message;

  document.body.appendChild(successDiv);

  // Remove after 4 seconds
  setTimeout(() => {
    successDiv.style.animation = "fadeOut 0.3s ease";
    setTimeout(() => successDiv.remove(), 300);
  }, 4000);
}

// Add animations to CSS via JavaScript
const style = document.createElement("style");
style.textContent = `
    @keyframes slideIn {
        from {
            transform: translateX(400px);
            opacity: 0;
        }
        to {
            transform: translateX(0);
            opacity: 1;
        }
    }
    
    @keyframes fadeOut {
        from {
            opacity: 1;
        }
        to {
            opacity: 0;
        }
    }
`;
document.head.appendChild(style);

// ===================================
// EVENT LISTENERS
// ===================================
elements.toggleMode.addEventListener("click", handleToggleMode);
elements.form.addEventListener("submit", handleFormSubmit);
elements.forgotPassword.addEventListener("click", handleForgotPassword);
elements.closeModal.addEventListener("click", closeModal);
elements.modalOk.addEventListener("click", closeModal);

// Close modal when clicking outside
elements.modal.addEventListener("click", (e) => {
  if (e.target === elements.modal) {
    closeModal();
  }
});

// Close modal with Escape key
document.addEventListener("keydown", (e) => {
  if (e.key === "Escape" && elements.modal.classList.contains("active")) {
    closeModal();
  }
});

// ===================================
// INITIALIZATION
// ===================================
console.log("FoodRescue Login/Signup Page Initialized");
console.log("Mode:", isSignupMode ? "Signup" : "Login");
