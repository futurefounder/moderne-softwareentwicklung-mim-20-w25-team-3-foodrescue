import { showError, showSuccess } from "./toastNotifications.js";

export async function handleCreateAngebot(event) {
    event.preventDefault();

    const titel = document.getElementById("angebot-titel")?.value?.trim() || "";
    const beschreibung = document.getElementById("angebot-beschreibung")?.value?.trim() || "";
    const tagsRaw = document.getElementById("angebot-tags")?.value || "";

    const tags = tagsRaw
        .split(",")
        .map((t) => t.trim())
        .filter((t) => t.length > 0);

    const von = document.getElementById("abhol-von")?.value;
    const bis = document.getElementById("abhol-bis")?.value;

    const userId = localStorage.getItem("userId");
    const role = localStorage.getItem("userRole");

    if (role !== "ANBIETER") {
        showError("Nur Anbieter können Angebote erstellen.");
        return false;
    }
    if (!userId || !titel || !von || !bis) {
        showError("Bitte fülle alle Pflichtfelder aus.");
        return false;
    }
    if (new Date(von) >= new Date(bis)) {
        showError("Ungültiges Zeitfenster: 'Von' muss vor 'Bis' liegen.");
        return false;
    }

    try {
        const response = await fetch("/api/angebote", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Accept": "application/json",
            },
            body: JSON.stringify({
                anbieterId: userId,
                titel,
                beschreibung,
                tags,
                zeitfenster: { von, bis },
            }),
        });

        if (response.ok) {
            showSuccess("Angebot erstellt (noch nicht veröffentlicht).");
            return true;
        }

        const text = await response.text().catch(() => "");
        console.error("Create Angebot failed:", response.status, text);
        showError(text || `Fehler beim Erstellen (Status ${response.status}).`);
        return false;
    } catch (e) {
        console.error("Create Angebot network error:", e);
        showError("Netzwerkfehler beim Erstellen des Angebots.");
        return false;
    }
}
