/**
 * Authentication Actions Module
 * Handles form submission, validation, and API calls for login/signup
 */

import { elements } from "./domElements.js";
import { getIsSignupMode } from "./authModeToggle.js";
import { showError, showSuccess } from "./toastNotifications.js";

async function handleFormSubmit(event) {
    event.preventDefault();

    if (getIsSignupMode()) {
        await handleSignup();
    } else {
        await handleLogin();
    }
}


async function handleLogin() {
    const email = elements.emailInput.value.trim();
    if (!email) {
        showError("Bitte E-Mail eingeben.");
        return;
    }

    try {
        // 1) Übliche Endpunkte ausprobieren
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
            showError("Login fehlgeschlagen: Nutzer nicht gefunden (E-Mail unbekannt).");
            return;
        }

        // 2) Login-State setzen
        localStorage.setItem("isLoggedIn", "true");
        localStorage.setItem("userEmail", userData.email);
        localStorage.setItem("userName", userData.name);
        localStorage.setItem("userRole", userData.rolle);
        localStorage.setItem("userId", userData.id);

        showSuccess(`Willkommen zurück, ${userData.name}!`);

        window.location.href = "./dashboard.html";
    } catch (e) {
        console.error(e);
        showError("Verbindungsfehler beim Login.");
    }
}


async function handleSignup() {
  const name = elements.nameInput.value.trim();
  const email = elements.emailInput.value.trim();
  const role = elements.roleSelect.value || null;

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
        window.location.href = "./dashboard.html";
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

export { handleFormSubmit };
