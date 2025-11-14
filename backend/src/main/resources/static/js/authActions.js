/**
 * Authentication Actions Module
 * Handles form submission, validation, and API calls for login/signup
 */

import { elements } from "./dom.js";
import { getIsSignupMode, switchToLoginMode } from "./authMode.js";
import { showError, showSuccess } from "./notifications.js";

function handleFormSubmit(event) {
  event.preventDefault();

  if (getIsSignupMode()) {
    handleSignup();
  } else {
    handleLogin();
  }
}

function handleLogin() {
  const email = elements.emailInput.value.trim();

  if (!email) {
    showError("Bitte E-Mail-Adresse eingeben.");
    return;
  }

  if (!isValidEmail(email)) {
    showError(
      "Bitte eine korrekte E-Mail eingeben (Format z.B. name@email.de)"
    );
    return;
  }

  console.log("Login attempt:", { email });
  showError("Login-Funktionalität ist noch nicht implementiert.");
}

async function handleSignup() {
  const name = elements.nameInput.value.trim();
  const email = elements.emailInput.value.trim();
  const role = elements.roleSelect.value;

  // Validate each field individually for better error messages
  if (!name) {
    showError("Bitte gib deinen vollständigen Namen ein.");
    return;
  }

  if (!email) {
    showError("Bitte gib deine E-Mail-Adresse ein.");
    return;
  }

  if (!isValidEmail(email)) {
    showError(
      "Bitte eine korrekte E-Mail eingeben (Format z.B. name@email.de)"
    );
    return;
  }

  if (!role) {
    showError("Bitte wähle deine Rolle (Abholen oder Anbieten).");
    return;
  }

  const requestData = {
    name,
    email,
    rolle: role,
  };

  try {
    // Disable button to prevent double submission
    elements.submitBtn.disabled = true;
    elements.submitBtn.textContent = "Wird registriert...";

    const response = await fetch("/api/users", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(requestData),
    });

    if (response.ok) {
      const userData = await response.json();
      console.log("User registered successfully:", userData);
      showSuccess(`Registrierung erfolgreich! Willkommen ${userData.name}!`);

      // Switch to login mode after success
      setTimeout(() => {
        switchToLoginMode();
      }, 2000);
    } else {
      // Handle backend validation errors
      let errorMessage = "Registrierung fehlgeschlagen.";

      try {
        const errorData = await response.json();
        errorMessage = errorData.message || errorMessage;
      } catch (parseError) {
        const errorText = await response.text();
        if (errorText) {
          errorMessage = errorText;
        }
      }

      console.error("Registration failed:", response.status, errorMessage);
      showError(errorMessage);
    }
  } catch (error) {
    console.error("Error during registration:", error);
    showError("Verbindungsfehler. Bitte versuchen Sie es später erneut.");
  } finally {
    elements.submitBtn.disabled = false;
    elements.submitBtn.textContent = "Konto erstellen";
  }
}

function isValidEmail(email) {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return emailRegex.test(email);
}

export { handleFormSubmit };
