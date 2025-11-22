/**
 * Main Entry Point
 * Initializes all modules and wires up the application
 */

import { elements } from "./domElements.js";
import { initAuthMode } from "./authModeToggle.js";
import { handleFormSubmit } from "./authActions.js";

function initEventListeners() {
  if (elements.form) {
    elements.form.addEventListener("submit", handleFormSubmit);
  }
}

function init() {
  initAuthMode();
  initEventListeners();

  console.log("FoodRescue Login/Signup Page Initialized");
}

init();
