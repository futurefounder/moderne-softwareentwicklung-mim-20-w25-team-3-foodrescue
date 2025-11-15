/**
 * Notifications Module
 * Displays toast notifications for success and error messages
 */

let animationsInjected = false;

// Inject CSS animations once on first use
function ensureAnimations() {
  if (animationsInjected) {
    return;
  }

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
  animationsInjected = true;
}

// Create a toast notification that auto-dismisses after 4 seconds
function createToast(message, backgroundColor) {
  ensureAnimations();

  const toast = document.createElement("div");
  toast.style.cssText = `
    position: fixed;
    top: 20px;
    right: 20px;
    background-color: ${backgroundColor};
    color: white;
    padding: 1rem 1.5rem;
    border-radius: 8px;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
    z-index: 2000;
    animation: slideIn 0.3s ease;
    max-width: 400px;
  `;
  toast.textContent = message;

  document.body.appendChild(toast);

  setTimeout(() => {
    toast.style.animation = "fadeOut 0.3s ease";
    setTimeout(() => toast.remove(), 300);
  }, 4000);
}

function showError(message) {
  createToast(message, "#ef4444");
}

function showSuccess(message) {
  createToast(message, "#10b981");
}

export { showError, showSuccess };
