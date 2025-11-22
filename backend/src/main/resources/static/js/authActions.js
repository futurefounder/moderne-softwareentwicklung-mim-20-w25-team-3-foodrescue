/**
 * Authentication Actions Module
 * Handles form submission, validation, and API calls for login/signup
 */

import { elements } from "./domElements.js";
import { getIsSignupMode, switchToLoginMode } from "./authModeToggle.js";
import { showError, showSuccess } from "./toastNotifications.js";

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
    showError("Bitte E-Mail-Adresse eingeben");
    return;
  }

  if (!isValidEmail(email)) {
    showError(
      "Bitte eine korrekte E-Mail eingeben (Format z.B. name@email.de)"
    );
    return;
  }

  console.log("Login attempt:", { email });

  // Store login state in localStorage (simplified login without backend validation)
  localStorage.setItem("isLoggedIn", "true");
  localStorage.setItem("userEmail", email);
  localStorage.setItem("userName", "Benutzer"); // Default name for login
  localStorage.setItem("userRole", "ABHOLER"); // Default role

  // Redirect to dashboard
  window.location.href = "/dashboard.html";
}

async function handleSignup() {
  const name = elements.nameInput.value.trim();
  const email = elements.emailInput.value.trim();
  const role = elements.roleSelect.value;

  // Validate each field individually for better error messages
  if (!name) {
    showError("Bitte gib deinen vollständigen Namen ein");
    return;
  }

  if (!email) {
    showError("Bitte gib deine E-Mail-Adresse ein");
    return;
  }

  if (!isValidEmail(email)) {
    showError(
      "Bitte eine korrekte E-Mail eingeben (Format z.B. name@email.de)"
    );
    return;
  }

  if (!role) {
    showError("Bitte wähle deine Rolle");
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

      // Store user data in localStorage
      localStorage.setItem("isLoggedIn", "true");
      localStorage.setItem("userName", userData.name);
      localStorage.setItem("userEmail", userData.email);
      localStorage.setItem("userRole", userData.rolle);
      localStorage.setItem("userId", userData.id);

      // Redirect to dashboard after success
      setTimeout(() => {
        window.location.href = "/dashboard.html";
      }, 1500);
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
