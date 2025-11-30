// js/register.js
import { showError, showSuccess } from "./toastNotifications.js"; // :contentReference[oaicite:2]{index=2}

const form = document.getElementById("register-form");
const submitBtn = document.getElementById("register-submit-btn");

const nameInput = document.getElementById("name");
const emailInput = document.getElementById("email");
const roleSelect = document.getElementById("role");

const anbieterFieldsContainer = document.getElementById("anbieter-fields");
const businessNameInput = document.getElementById("business-name");
const businessTypeSelect = document.getElementById("business-type");
const addressInput = document.getElementById("address");
const latitudeInput = document.getElementById("latitude");
const longitudeInput = document.getElementById("longitude");

// Rolle -> Anbieter-Felder ein-/ausblenden
function updateRoleSpecificFields() {
    const role = roleSelect.value;

    if (role === "ANBIETER") {
        anbieterFieldsContainer.style.display = "flex";

        businessNameInput.required = true;
        businessTypeSelect.required = true;
        addressInput.required = true;
    } else {
        anbieterFieldsContainer.style.display = "none";

        businessNameInput.required = false;
        businessTypeSelect.required = false;
        addressInput.required = false;
    }
}

roleSelect.addEventListener("change", updateRoleSpecificFields);

form.addEventListener("submit", async (event) => {
    event.preventDefault();

    const name = nameInput.value.trim();
    const email = emailInput.value.trim();
    const role = roleSelect.value;

    if (!name || !email || !role) {
        showError("Bitte fülle alle Pflichtfelder aus.");
        return;
    }

    // 1. User registrieren
    const userRequest = {
        name,
        email,
        rolle: role,
    };

    try {
        submitBtn.disabled = true;
        submitBtn.textContent = "Wird registriert...";

        // entspricht deinem bisherigen Signup-Call in authActions.js :contentReference[oaicite:3]{index=3}
        const userResponse = await fetch("/api/users", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(userRequest),
        });

        if (!userResponse.ok) {
            let errorMessage = "Registrierung fehlgeschlagen.";

            try {
                const errorData = await userResponse.json();
                if (errorData && errorData.message) {
                    errorMessage = errorData.message;
                }
            } catch {
                const errorText = await userResponse.text();
                if (errorText) {
                    errorMessage = errorText;
                }
            }

            showError(errorMessage);
            return;
        }

        const userData = await userResponse.json();
        console.log("User registered:", userData);

        // 2. Wenn Rolle ANBIETER: AnbieterProfil anlegen
        if (role === "ANBIETER") {
            const geschaeftsname = businessNameInput.value.trim();
            const geschaeftstyp = businessTypeSelect.value || "SONSTIGES";
            const adresse = addressInput.value.trim();
            const breitengrad = latitudeInput.value ? Number(latitudeInput.value) : null;
            const laengengrad = longitudeInput.value ? Number(longitudeInput.value) : null;

            const profilRequest = {
                userId: userData.id,
                geschaeftsname,
                geschaeftstyp,
                adresse,
                breitengrad,
                laengengrad,
            };

            const profilResponse = await fetch("/api/anbieterprofile", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(profilRequest),
            });

            if (!profilResponse.ok) {
                console.error("Fehler beim Erstellen des AnbieterProfils", profilResponse.status);
                showError(
                    "Konto wurde erstellt, aber das Anbieter-Profil konnte nicht angelegt werden."
                );
            } else {
                const profilData = await profilResponse.json();
                console.log("AnbieterProfil erstellt:", profilData);
            }
        }

        // 3. Login-State & Redirect
        showSuccess(`Registrierung erfolgreich! Willkommen ${userData.name}!`);

        localStorage.setItem("isLoggedIn", "true");
        localStorage.setItem("userName", userData.name);
        localStorage.setItem("userEmail", userData.email);
        localStorage.setItem("userRole", userData.rolle);
        localStorage.setItem("userId", userData.id);

        setTimeout(() => {
            window.location.href = "./dashboard.html";
        }, 1500);
    } catch (error) {
        console.error("Error during registration:", error);
        showError("Verbindungsfehler. Bitte versuchen Sie es später erneut.");
    } finally {
        submitBtn.disabled = false;
        submitBtn.textContent = "Konto erstellen";
    }
});

// initialer Zustand
updateRoleSpecificFields();
