document.getElementById('check').addEventListener('click', async () => {
    const out = document.getElementById('out');
    try {
        const res = await fetch('/api/health');
        out.textContent = res.ok ? await res.text() : `Fehler: ${res.status}`;
    } catch (e) {
        out.textContent = `Netzwerkfehler: ${e.message}`;
    }
});
