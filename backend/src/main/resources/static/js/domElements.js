/**
 * DOM Elements Module
 * Caches all DOM element references used throughout the application
 */

// Cache all DOM elements once to avoid repeated queries
export const elements = {
  form: document.getElementById("auth-form"),
  formTitle: document.getElementById("form-title"),
  formSubtitle: document.getElementById("form-subtitle"),
  submitBtn: document.getElementById("submit-btn"),
  toggleText: document.getElementById("toggle-text"),
  nameGroup: document.getElementById("name-group"),
  nameInput: document.getElementById("name"),
  emailInput: document.getElementById("email"),
  roleGroup: document.getElementById("role-group"),
  roleSelect: document.getElementById("role"),
};

// Toggle link is dynamically recreated, so we query it on demand
export function getToggleLink() {
  return document.getElementById("toggle-mode");
}
