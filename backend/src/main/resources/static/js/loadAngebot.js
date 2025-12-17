import { handleCreateAngebot } from "./handleCreateAngebot.js";
import { showError, showSuccess } from "./toastNotifications.js";

export function initAngeboteUi() {
    const role = localStorage.getItem("userRole");
    const userId = localStorage.getItem("userId");

    if (role === "ANBIETER") {
        const form = document.getElementById("angebot-form");
        if (form) {
            // Formular beim Überblick NICHT automatisch anzeigen
            form.style.display = "none";

            // Submit-Handler nur einmal binden (verhindert doppelte Listener)
            form.addEventListener("submit", async (e) => {
                const ok = await handleCreateAngebot(e);
                if (!ok) return;

                // Eingaben zurücksetzen
                form.reset();

                // optional: Panel schließen und zur Übersicht wechseln
                await loadMeineAngebote();
                document.getElementById("nav-overview-link")?.click();
            });

        }

        loadMeineAngebote();
        return;
    }

    // Abholer (optional): verfügbare Angebote anzeigen
    loadVerfuegbareAngebote();
}

async function loadMeineAngebote() {
    const userId = localStorage.getItem("userId");
    const listEl = document.getElementById("angebote-list");
    if (!listEl) return;

    if (!userId) {
        listEl.innerHTML = "";
        showError("Nicht eingeloggt (userId fehlt).");
        return;
    }

    listEl.innerHTML = "<div class='pickup-item'>Lade Angebote...</div>";

    try {
        const res = await fetch(`/api/angebote/anbieter/${encodeURIComponent(userId)}`);
        if (!res.ok) {
            listEl.innerHTML = "";
            showError(`Angebote konnten nicht geladen werden (Status ${res.status}).`);
            return;
        }

        const angebote = await res.json();
        renderMeineAngebote(listEl, angebote);
    } catch (e) {
        console.error(e);
        listEl.innerHTML = "";
        showError("Verbindungsfehler beim Laden der Angebote.");
    }
}

function renderMeineAngebote(listEl, angebote) {
    if (!Array.isArray(angebote) || angebote.length === 0) {
        listEl.innerHTML = "<div class='pickup-item'>Noch keine Angebote vorhanden.</div>";
        return;
    }

    listEl.innerHTML = angebote
        .map((a) => {
            const status = a.status || "UNBEKANNT";
            const tags = Array.isArray(a.tags) ? a.tags.join(", ") : "";
            const von = a.zeitfenster?.von || "";
            const bis = a.zeitfenster?.bis || "";
            const zeitfensterText = (von && bis) ? `${von} – ${bis}` : "";
            const canPublish = status === "ENTWURF"; // Draft / noch nicht veröffentlicht

            return `
        <div class="pickup-item" data-angebot-id="${a.id}">
          <div class="pickup-left">
            <div class="pickup-info">
              <h3 class="pickup-name">${escapeHtml(a.titel || "")}</h3>
              <p class="pickup-address">${escapeHtml(a.beschreibung || "")}</p>
              <p class="pickup-address"><strong>Status:</strong> ${escapeHtml(status)}</p>
              ${tags ? `<p class="pickup-address"><strong>Tags:</strong> ${escapeHtml(tags)}</p>` : ""}
              ${zeitfensterText ? `<p class="pickup-address"><strong>Abholfenster:</strong> ${escapeHtml(zeitfensterText)}</p>` : ""}
            </div>
          </div>

          <div class="pickup-time" style="display:flex; gap:8px; align-items:center;">
            ${
                canPublish
                    ? `<button class="btn-primary btn-publish" type="button">Veröffentlichen</button>`
                    : ""
            }
          </div>
        </div>
      `;
        })
        .join("");

    // Button-Handler (Event Delegation)
    if (!listEl.dataset.publishBound) {
        listEl.dataset.publishBound = "true";

        listEl.addEventListener("click", async (ev) => {
            const btn = ev.target.closest(".btn-publish");
            if (!btn) return;

            const item = ev.target.closest("[data-angebot-id]");
            const angebotId = item?.getAttribute("data-angebot-id");
            if (!angebotId) return;

            await publishAngebot(angebotId);
            await loadMeineAngebote();
        });
    }

}

async function publishAngebot(angebotId) {
    try {
        const res = await fetch(`/api/angebote/${encodeURIComponent(angebotId)}/veroeffentlichen`, {
            method: "POST",
        });

        if (!res.ok) {
            showError(`Veröffentlichen fehlgeschlagen (Status ${res.status}).`);
            return;
        }

        showSuccess("Angebot veröffentlicht.");
    } catch (e) {
        console.error(e);
        showError("Verbindungsfehler beim Veröffentlichen.");
    }
}

async function loadVerfuegbareAngebote() {
    const listEl = document.getElementById("angebote-list");
    if (!listEl) return;

    listEl.innerHTML = "<div class='pickup-item'>Lade verfügbare Angebote...</div>";

    try {
        const res = await fetch("/api/angebote/verfuegbar", {
            headers: { "Accept": "application/json" }
        });

        if (!res.ok) {
            listEl.innerHTML = "";
            showError(`Angebote konnten nicht geladen werden (Status ${res.status}).`);
            return;
        }

        const angebote = await res.json();

        if (!Array.isArray(angebote) || angebote.length === 0) {
            listEl.innerHTML = "<div class='pickup-item'>Keine verfügbaren Angebote gefunden.</div>";
            return;
        }

        listEl.innerHTML = angebote.map((a) => {
            const tags = Array.isArray(a.tags) ? a.tags.join(", ") : "";
            const von = a.zeitfenster?.von || "";
            const bis = a.zeitfenster?.bis || "";
            const zeitfensterText = (von && bis) ? `${von} – ${bis}` : "";

            return `
        <div class="pickup-item" data-angebot-id="${a.id}">
          <div class="pickup-left">
            <div class="pickup-info">
              <h3 class="pickup-name">${escapeHtml(a.titel || "")}</h3>
              <p class="pickup-address">${escapeHtml(a.beschreibung || "")}</p>
              ${tags ? `<p class="pickup-address"><strong>Tags:</strong> ${escapeHtml(tags)}</p>` : ""}
              ${zeitfensterText ? `<p class="pickup-address"><strong>Abholfenster:</strong> ${escapeHtml(zeitfensterText)}</p>` : ""}
            </div>
          </div>

          <div class="pickup-time" style="display:flex; gap:8px; align-items:center;">
            <button class="btn-primary btn-open" type="button">Öffnen</button>
            <button class="btn-primary btn-reserve" type="button">Reservieren</button>
          </div>
        </div>
      `;
        }).join("");

        if (!listEl.dataset.abholerBound) {
            listEl.dataset.abholerBound = "true";

            listEl.addEventListener("click", async (ev) => {
                const item = ev.target.closest("[data-angebot-id]");
                if (!item) return;
                const angebotId = item.getAttribute("data-angebot-id");

                if (ev.target.closest(".btn-open")) {
                    openOfferDetails(item);
                    return;
                }
                if (ev.target.closest(".btn-reserve")) {
                    await reserveOffer(angebotId);
                    await loadVerfuegbareAngebote();
                    return;
                }
            });
        }


    } catch (e) {
        console.error(e);
        listEl.innerHTML = "";
        showError("Verbindungsfehler beim Laden der Angebote.");
    }
}

async function openOfferDetails(item) {
    const angebotId = item.getAttribute("data-angebot-id");
    if (!angebotId) return;

    const modal = document.getElementById("angebot-modal");
    const closeBtn = document.getElementById("angebot-modal-close");
    const titleEl = document.getElementById("angebot-modal-title");
    const descEl = document.getElementById("angebot-modal-desc");
    const tagsEl = document.getElementById("angebot-modal-tags");
    const zeitEl = document.getElementById("angebot-modal-zeitfenster");
    const actionsEl = document.getElementById("angebot-modal-actions");

    if (!modal || !closeBtn || !titleEl || !descEl || !tagsEl || !zeitEl || !actionsEl) {
        // Fallback: bisheriges Verhalten, wenn Modal noch nicht eingebaut wurde
        item.scrollIntoView({ behavior: "smooth", block: "center" });
        item.style.outline = "2px solid rgba(0,0,0,0.2)";
        setTimeout(() => (item.style.outline = "none"), 1200);
        return;
    }

    // Close
    closeBtn.onclick = () => (modal.style.display = "none");
    modal.onclick = (e) => { if (e.target === modal) modal.style.display = "none"; };

    // Laden
    titleEl.textContent = "Lade...";
    descEl.textContent = "";
    tagsEl.textContent = "";
    zeitEl.textContent = "";
    actionsEl.innerHTML = "";

    modal.style.display = "flex";

    try {
        const res = await fetch(`/api/angebote/${encodeURIComponent(angebotId)}`, {
            headers: { "Accept": "application/json" }
        });

        if (!res.ok) {
            titleEl.textContent = "Angebot";
            descEl.textContent = `Details konnten nicht geladen werden (Status ${res.status}).`;
            return;
        }

        const a = await res.json();

        const tags = Array.isArray(a.tags) ? a.tags.join(", ") : "";
        const von = a.zeitfenster?.von || "";
        const bis = a.zeitfenster?.bis || "";

        titleEl.textContent = a.titel || "Angebot";
        descEl.textContent = a.beschreibung || "";
        tagsEl.textContent = tags ? `Tags: ${tags}` : "";
        zeitEl.textContent = (von && bis) ? `Abholfenster: ${von} – ${bis}` : "";

        // Abholer-Aktion im Modal: reservieren
        const role = localStorage.getItem("userRole");
        if (role !== "ANBIETER") {
            const btn = document.createElement("button");
            btn.className = "btn-primary";
            btn.type = "button";
            btn.textContent = "Reservieren";
            btn.onclick = async () => {
                await reserveOffer(angebotId);
                modal.style.display = "none";
                await loadVerfuegbareAngebote();
            };
            actionsEl.appendChild(btn);
        }
    } catch (e) {
        titleEl.textContent = "Angebot";
        descEl.textContent = "Verbindungsfehler beim Laden der Details.";
    }
}


async function reserveOffer(angebotId) {
    const abholerId = localStorage.getItem("userId");
    if (!abholerId) {
        showError("Nicht eingeloggt (userId fehlt).");
        return;
    }

    const res = await fetch("/api/reservierungen", {
        method: "POST",
        headers: { "Content-Type": "application/json", "Accept": "application/json" },
        body: JSON.stringify({ angebotId, abholerId: localStorage.getItem("userId") }),
    });


    if (res.ok) {
        showSuccess("Angebot reserviert.");
        return;
    }

    const text = await res.text().catch(() => "");
    console.error("Reservierung fehlgeschlagen:", res.status, text);
    showError(text || `Reservierung fehlgeschlagen (Status ${res.status}).`);
}



function escapeHtml(str) {
    return String(str)
        .replaceAll("&", "&amp;")
        .replaceAll("<", "&lt;")
        .replaceAll(">", "&gt;")
        .replaceAll('"', "&quot;")
        .replaceAll("'", "&#039;");
}
