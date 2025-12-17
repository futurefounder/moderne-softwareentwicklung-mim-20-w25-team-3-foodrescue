import { initAngeboteUi } from "./loadAngebot.js";

function checkAuth() {
    const isLoggedIn = localStorage.getItem("isLoggedIn");
    if (!isLoggedIn || isLoggedIn !== "true") {
        window.location.href = "./index.html";
        return false;
    }
    return true;
}

function loadUserInfo() {
    const userName = localStorage.getItem("userName");
    const greetingNameElement = document.getElementById("user-greeting-name");
    if (greetingNameElement) {
        greetingNameElement.textContent = userName || "Benutzer";
    }
}

function handleLogout() {
    localStorage.removeItem("isLoggedIn");
    localStorage.removeItem("userName");
    localStorage.removeItem("userEmail");
    localStorage.removeItem("userRole");
    localStorage.removeItem("userId");
    window.location.href = "/index.html";
}

function setActiveNav(activeId) {
    const overview = document.getElementById("nav-overview-link");
    const action = document.getElementById("nav-action-link");

    if (overview) overview.classList.toggle("active", activeId === "overview");
    if (action) action.classList.toggle("active", activeId === "action");
}

function applyRoleLayout() {
    const role = localStorage.getItem("userRole");

    const angeboteSection = document.getElementById("angebote-section");
    const pickupsSection = document.getElementById("pickups-section");

    const navOverviewLink = document.getElementById("nav-overview-link");
    const navActionText = document.getElementById("nav-action-text");
    const navActionLink = document.getElementById("nav-action-link");

    const findDonationsBtn = document.getElementById("btn-find-donations");

    const createPanel = document.getElementById("angebot-create-panel");
    const angebotForm = document.getElementById("angebot-form");

    // ganz oben im applyRoleLayout() zusätzlich holen:
    const listEl = document.getElementById("angebote-list");
    const angeboteTitle = document.getElementById("angebote-title");
    const angeboteSubtitle = document.getElementById("angebote-subtitle");

// Helper:


    const showCreate = () => {
        setActiveNav("action");
        if (listEl) listEl.style.display = "none";
        if (createPanel) createPanel.style.display = "block";
        if (angebotForm) angebotForm.style.display = "block";

        if (angeboteTitle) angeboteTitle.textContent = "Neues Angebot";
        if (angeboteSubtitle) angeboteSubtitle.textContent = "Entwurf erstellen und später veröffentlichen";
    };


    if (!angeboteSection || !pickupsSection || !navOverviewLink || !navActionText || !navActionLink) {
        console.error(
            "Dashboard IDs fehlen. Prüfe: angebote-section, pickups-section, nav-overview-link, nav-action-link, nav-action-text"
        );
        return;
    }

    const showOverview = () => {
        setActiveNav("overview");
        if (listEl) listEl.style.display = "block";
        if (createPanel) createPanel.style.display = "none";
        if (angebotForm) angebotForm.style.display = "none";

        if (angeboteTitle) angeboteTitle.textContent = "Meine Angebote";
        if (angeboteSubtitle) angeboteSubtitle.textContent = "Ihre veröffentlichten und gespeicherten Angebote";
    };

    // ---------- ANBIETER ----------


    if (role === "ANBIETER") {
        angeboteSection.style.display = "block";
        pickupsSection.style.display = "none";

        if (findDonationsBtn) findDonationsBtn.style.display = "none";

        navActionText.textContent = "Neues Angebot anlegen";

        navOverviewLink.onclick = () => {
            showOverview();
            angeboteSection.scrollIntoView({ behavior: "smooth", block: "start" });
        };

        navActionLink.onclick = () => {
                showCreate();
                angeboteSection.scrollIntoView({ behavior: "smooth", block: "start" });

                const titel = document.getElementById("angebot-titel");
                if (titel) titel.focus();

        };

        showOverview();
        initAngeboteUi();
        return;
    }

    // ---------- ABHOLER (Default) ----------
    pickupsSection.style.display = "block";
    angeboteSection.style.display = "none";

// Für Abholer: Titel/Subtitel der Angebote-Sektion passend setzen (wenn Suche aktiv)
    const setSearchHeader = () => {
        if (angeboteTitle) angeboteTitle.textContent = "Essensspenden suchen";
        if (angeboteSubtitle) angeboteSubtitle.textContent = "Verfügbare Angebote anzeigen und reservieren";
    };

// Überblick: Abholungen sichtbar, Angebote versteckt, Button sichtbar
    const showAbholerOverview = () => {
        setActiveNav("overview");
        pickupsSection.style.display = "block";
        angeboteSection.style.display = "none";

        if (angeboteTitle) angeboteTitle.textContent = "Essensspenden";
        if (angeboteSubtitle) angeboteSubtitle.textContent = "";

        // Button nur im Überblick
        if (findDonationsBtn) findDonationsBtn.style.display = "inline-flex";

        pickupsSection.scrollIntoView({ behavior: "smooth", block: "start" });

        // geplante Abholungen laden
        loadPlannedPickups();
    };

// Suche: Angebote sichtbar, Abholungen versteckt, Button versteckt
    const showAbholerSearch = () => {
        setActiveNav("action");
        pickupsSection.style.display = "none";
        angeboteSection.style.display = "block";

        // Button nur im Überblick
        if (findDonationsBtn) findDonationsBtn.style.display = "none";

        setSearchHeader();
        angeboteSection.scrollIntoView({ behavior: "smooth", block: "start" });

        // verfügbare Angebote laden (Abholer-Pfad in loadAngebot.js)
        initAngeboteUi();
    };

    navActionText.textContent = "Essensspenden suchen";

    navOverviewLink.onclick = showAbholerOverview;
    navActionLink.onclick = showAbholerSearch;

// Button führt ebenfalls in die Suche, ist aber nur im Überblick sichtbar
    if (findDonationsBtn) {
        findDonationsBtn.onclick = showAbholerSearch;
    }

// Initial: Overview
    showAbholerOverview();

}

async function loadPlannedPickups() {
    const userId = localStorage.getItem("userId");
    const list = document.getElementById("pickups-list");
    if (list) list.innerHTML = "";

    if (!list) return;

    if (!userId) {
        list.innerHTML = "<div class='pickup-item'>Nicht eingeloggt (userId fehlt).</div>";
        return;
    }

    list.innerHTML = "<div class='pickup-item'>Lade Abholungen...</div>";

    try {
        const res = await fetch(`/api/reservierungen/user/${encodeURIComponent(userId)}`, {
            headers: { "Accept": "application/json" }
        });


        if (!res.ok) {
            const text = await res.text().catch(() => "");
            console.error("Pickups failed:", res.status, text);
            list.innerHTML = `<div class='pickup-item'>Fehler beim Laden (Status ${res.status}).</div>`;
            return;
        }


        const data = await res.json();

        if (!Array.isArray(data) || data.length === 0) {
            list.innerHTML = "<div class='pickup-item'>Noch keine geplanten Abholungen vorhanden.</div>";
            return;
        }

        list.innerHTML = data.map((r) => `
  <div class="pickup-item">
    <div class="pickup-left">
      <div class="pickup-info">
        <h3 class="pickup-name">${escapeHtml(r.angebotTitel || "Unbekanntes Angebot")}</h3>
        ${r.angebotBeschreibung ? `<p class="pickup-address">${escapeHtml(r.angebotBeschreibung)}</p>` : ""}
        <p class="pickup-address"><strong>Status:</strong> ${escapeHtml(r.status || "")}</p>
        ${r.abholcode ? `<p class="pickup-address"><strong>Abholcode:</strong> ${escapeHtml(r.abholcode)}</p>` : ""}
        ${(r.zeitfensterVon && r.zeitfensterBis)
            ? `<p class="pickup-address"><strong>Abholfenster:</strong> ${escapeHtml(r.zeitfensterVon)} – ${escapeHtml(r.zeitfensterBis)}</p>`
            : ""}
      </div>
    </div>
  </div>
`).join("");



    } catch (e) {
        console.error(e);
        list.innerHTML = "<div class='pickup-item'>Verbindungsfehler beim Laden der Abholungen.</div>";
    }
}


// Hilfsfunktion (wie in loadAngebot.js)
function escapeHtml(str) {
    return String(str)
        .replaceAll("&", "&amp;")
        .replaceAll("<", "&lt;")
        .replaceAll(">", "&gt;")
        .replaceAll('"', "&quot;")
        .replaceAll("'", "&#039;");
}


function init() {
    if (!checkAuth()) return;

    loadUserInfo();

    const logoutBtn = document.getElementById("logout-btn");
    if (logoutBtn) logoutBtn.addEventListener("click", handleLogout);

    applyRoleLayout();
    console.log("Dashboard initialized");
}

init();
