import { showError, showSuccess } from "./toastNotifications.js";

const form = document.getElementById("register-form");
const submitBtn = document.getElementById("register-submit-btn");

const nameInput = document.getElementById("name");
const emailInput = document.getElementById("email");
const roleSelect = document.getElementById("role");

const anbieterFieldsContainer = document.getElementById("anbieter-fields");
const businessNameInput = document.getElementById("business-name");
const businessTypeSelect = document.getElementById("business-type");

// Legacy: ein Feld "address"
const addressInput = document.getElementById("address");

// Optional: strukturierte Adresse (falls du sie in register.html ergänzt hast)
const strasseInput = document.getElementById("strasse");
const plzInput = document.getElementById("plz");
const ortInput = document.getElementById("ort");
const landInput = document.getElementById("land");

// Geo
const latitudeInput = document.getElementById("latitude");
const longitudeInput = document.getElementById("longitude");

/**
 * Mapping von UI-Werten (z.B. "Abholer", "Anbieter") auf Enum-Namen im Backend
 */
function mapRoleToEnum(roleValue) {
    switch (roleValue) {
        case "Abholer":
            return "ABHOLER";
        case "Anbieter":
            return "ANBIETER";
        default:
            // falls das <option>-value schon "ABHOLER"/"ANBIETER" ist
            return roleValue;
    }
}

/**
 * Mapping von UI-Werten (z.B. "Bäckerei") auf Enum-Namen im Backend
 */
function mapBusinessTypeToEnum(typeValue) {
    switch (typeValue) {
        case "Bäckerei":
            return "BAECKEREI";
        case "Supermarkt":
            return "SUPERMARKT";
        case "Restaurant":
            return "RESTAURANT";
        case "Imbiss":
            return "IMBISS";
        case "Sonstiges":
            return "SONSTIGES";
        default:
            // falls das <option>-value schon Enum-Name ist oder nichts gewählt wurde
            return typeValue || "SONSTIGES";
    }
}

function isBlank(s) {
    return !s || s.trim() === "";
}

/**
 * Liest JSON-Fehlerantworten robust:
 * - unterstützt JSON {error, message}
 * - fallback auf Text
 */
async function readErrorMessage(response, fallback) {
    try {
        const contentType = response.headers.get("content-type") || "";
        if (contentType.includes("application/json")) {
            const data = await response.json();
            return data?.message || data?.error || fallback;
        }
    } catch (e) {
        // ignorieren, fallback unten
    }

    try {
        const text = await response.text();
        return text || fallback;
    } catch {
        return fallback;
    }
}

function readNumberOrNull(inputEl) {
    if (!inputEl) return null;
    const raw = (inputEl.value ?? "").trim();
    if (raw === "") return null;
    const n = Number(raw);
    return Number.isFinite(n) ? n : null;
}

/**
 * Versucht, eine Legacy-Adresse "Straße Hausnr, PLZ Ort, Land" zu parsen.
 * Beispiel: "Ahornstraße 11, 12345 Berlin, DE"
 */
function parseLegacyAdresse(raw) {
    const s = (raw ?? "").trim();
    if (s === "") return null;

    const re = /^\s*(.+?)\s*,\s*(\d{4,6})\s+(.+?)\s*,\s*([A-Za-z]{2,})\s*$/;
    const m = s.match(re);
    if (!m) return null;

    return {
        strasse: m[1].trim(),
        plz: m[2].trim(),
        ort: m[3].trim(),
        land: m[4].trim(),
    };
}

/**
 * Ermittelt Adresse für Backend:
 * 1) bevorzugt strukturierte Inputs strasse/plz/ort/land, falls vorhanden
 * 2) fallback: parse aus addressInput im Format "Straße Hausnr, PLZ Ort, Land"
 */
function buildAdressePayloadOrThrow() {
    const hasStructured =
        !!strasseInput && !!plzInput && !!ortInput && !!landInput;

    if (hasStructured) {
        const strasse = strasseInput.value.trim();
        const plz = plzInput.value.trim();
        const ort = ortInput.value.trim();
        const land = landInput.value.trim();

        if (isBlank(strasse) || isBlank(plz) || isBlank(ort) || isBlank(land)) {
            throw new Error("Bitte Adresse vollständig ausfüllen (Straße, PLZ, Ort, Land).");
        }

        return { strasse, plz, ort, land, adresseLegacy: null };
    }

    // Legacy-Feld vorhanden?
    const legacy = addressInput ? addressInput.value.trim() : "";
    const parsed = parseLegacyAdresse(legacy);

    if (!parsed) {
        throw new Error(
            "Adresse ungültig. Bitte im Format 'Straße Hausnr, PLZ Ort, Land' eingeben (z.B. 'Ahornstraße 11, 12345 Berlin, DE')."
        );
    }

    return { ...parsed, adresseLegacy: legacy };
}

// Rolle -> Anbieter-Felder ein-/ausblenden
function updateRoleSpecificFields() {
    const roleValue = roleSelect.value;
    const roleEnum = mapRoleToEnum(roleValue);

    if (roleEnum === "ANBIETER") {
        anbieterFieldsContainer.style.display = "flex";

        businessNameInput.required = true;
        businessTypeSelect.required = true;

        // Falls structured Felder existieren, diese required, sonst legacy address
        const hasStructured =
            !!strasseInput && !!plzInput && !!ortInput && !!landInput;

        if (hasStructured) {
            strasseInput.required = true;
            plzInput.required = true;
            ortInput.required = true;
            landInput.required = true;
            if (addressInput) addressInput.required = false;
        } else {
            if (addressInput) addressInput.required = true;
        }
    } else {
        anbieterFieldsContainer.style.display = "none";

        businessNameInput.required = false;
        businessTypeSelect.required = false;

        if (addressInput) addressInput.required = false;
        if (strasseInput) strasseInput.required = false;
        if (plzInput) plzInput.required = false;
        if (ortInput) ortInput.required = false;
        if (landInput) landInput.required = false;
    }
}

roleSelect.addEventListener("change", updateRoleSpecificFields);

form.addEventListener("submit", async (event) => {
    event.preventDefault();

    const name = nameInput.value.trim();
    const email = emailInput.value.trim();
    const roleValue = roleSelect.value;
    const roleEnum = mapRoleToEnum(roleValue);

    if (!name || !email || !roleValue) {
        showError("Bitte fülle alle Pflichtfelder aus.");
        return;
    }

    // 1. User registrieren
    const userRequest = {
        name,
        email,
        rolle: roleEnum, // Enum-Name für das Backend
    };

    try {
        submitBtn.disabled = true;
        submitBtn.textContent = "Wird registriert...";

        const userResponse = await fetch("/api/users", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(userRequest),
        });

        if (!userResponse.ok) {
            const msg = await readErrorMessage(
                userResponse,
                `Registrierung fehlgeschlagen (Status ${userResponse.status}).`
            );
            console.error("User registration failed:", userResponse.status, msg);
            showError(msg);
            return;
        }

        const userData = await userResponse.json();
        console.log("User registered:", userData);

        const userId = userData.id ?? userData.userId;
        if (!userId) {
            showError("Registrierung fehlgeschlagen: Keine User-ID vom Backend erhalten.");
            return;
        }

        // 2. Wenn Rolle ANBIETER: AnbieterProfil anlegen
        if (roleEnum === "ANBIETER") {
            const geschaeftsname = businessNameInput.value.trim();
            const geschaeftstypEnum = mapBusinessTypeToEnum(businessTypeSelect.value);

            if (isBlank(geschaeftsname)) {
                showError("Bitte einen Geschäftsnamen angeben.");
                return;
            }

            // Adresse bestimmen (structured oder parse legacy)
            let adressePayload;
            try {
                adressePayload = buildAdressePayloadOrThrow();
            } catch (e) {
                showError(e.message || "Adresse ungültig.");
                return;
            }

            // Geo: optional, aber wenn eins gesetzt ist -> beide + Range
            const lat = readNumberOrNull(latitudeInput);
            const lon = readNumberOrNull(longitudeInput);

            if (lat !== null || lon !== null) {
                if (lat === null || lon === null) {
                    showError("Bitte Breitengrad und Längengrad entweder beide ausfüllen oder beide leer lassen.");
                    return;
                }
                if (lat < -90 || lat > 90) {
                    showError("Breitengrad muss zwischen -90 und 90 liegen.");
                    return;
                }
                if (lon < -180 || lon > 180) {
                    showError("Längengrad muss zwischen -180 und 180 liegen.");
                    return;
                }
            }

            const profilRequest = {
                userId,
                geschaeftsname,
                geschaeftstyp: geschaeftstypEnum,
                // strukturiert (Backend bevorzugt das)
                strasse: adressePayload.strasse,
                plz: adressePayload.plz,
                ort: adressePayload.ort,
                land: adressePayload.land,
                // optional legacy-Feld mitsenden (schadet nicht, hilft Debugging)
                adresse: adressePayload.adresseLegacy ?? undefined,
                breitengrad: lat,
                laengengrad: lon,
            };

            const profilResponse = await fetch("/api/anbieter-profile", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(profilRequest),
            });

            if (!profilResponse.ok) {
                const msg = await readErrorMessage(
                    profilResponse,
                    `Anbieterprofil konnte nicht erstellt werden (Status ${profilResponse.status}).`
                );

                console.error("Fehler beim Erstellen des AnbieterProfils", profilResponse.status, msg);
                showError("Konto wurde erstellt, aber das Anbieter-Profil konnte nicht angelegt werden: " + msg);
                return; // wichtig: nicht “erfolgreich” weitermachen
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
        localStorage.setItem("userId", userId);

        setTimeout(() => {
            window.location.href = "./dashboard.html";
        }, 1500);
    } catch (error) {
        console.error("Error during registration:", error);
        showError(error?.message || "Verbindungsfehler. Bitte versuchen Sie es später erneut.");
    } finally {
        submitBtn.disabled = false;
        submitBtn.textContent = "Konto erstellen";
    }
});

// initialer Zustand
updateRoleSpecificFields();
