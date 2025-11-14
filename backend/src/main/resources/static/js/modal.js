/**
 * Modal Module
 * Handles the "Forgot Password" modal dialog
 */

import { elements } from "./dom.js";

function openModal() {
  if (!elements.modal) {
    return;
  }

  elements.modal.classList.add("active");
  document.body.style.overflow = "hidden";
}

function closeModal() {
  if (!elements.modal) {
    return;
  }

  elements.modal.classList.remove("active");
  document.body.style.overflow = "auto";
}

function handleForgotPassword(event) {
  event.preventDefault();
  openModal();
}

// Initialize modal event listeners
function initModal() {
  if (elements.forgotPassword) {
    elements.forgotPassword.addEventListener("click", handleForgotPassword);
  }

  if (elements.closeModal) {
    elements.closeModal.addEventListener("click", closeModal);
  }

  if (elements.modalOk) {
    elements.modalOk.addEventListener("click", closeModal);
  }

  // Close modal when clicking outside
  if (elements.modal) {
    elements.modal.addEventListener("click", (event) => {
      if (event.target === elements.modal) {
        closeModal();
      }
    });
  }

  // Close modal with Escape key
  document.addEventListener("keydown", (event) => {
    if (
      event.key === "Escape" &&
      elements.modal &&
      elements.modal.classList.contains("active")
    ) {
      closeModal();
    }
  });
}

export { initModal, openModal, closeModal };
