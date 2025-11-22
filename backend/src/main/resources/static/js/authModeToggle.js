/**
 * Authentication Mode Module
 * Handles switching between login and signup modes
 */

import { elements, getToggleLink } from "./domElements.js";

let isSignupMode = false;

// Rebind the toggle link after it's recreated in the DOM
function bindToggleLink() {
  const toggleLink = getToggleLink();
  if (!toggleLink) {
    return;
  }

  toggleLink.addEventListener("click", handleToggleMode);
}

// Switch to signup mode: show name and role fields
function switchToSignupMode() {
  isSignupMode = true;

  elements.formTitle.textContent = "Werde FoodRescuer/in!";
  elements.formSubtitle.textContent = "Erstelle dein FoodRescue Konto";

  elements.nameGroup.style.display = "flex";
  elements.roleGroup.style.display = "flex";

  elements.nameInput.setAttribute("required", "required");
  elements.roleSelect.setAttribute("required", "required");

  elements.submitBtn.textContent = "Konto erstellen";

  elements.toggleText.innerHTML =
    'Bereits ein Konto? <a href="#" class="link-primary" id="toggle-mode">Anmelden</a>';

  bindToggleLink();
}

// Switch to login mode: hide signup-specific fields
function switchToLoginMode() {
  isSignupMode = false;

  elements.formTitle.textContent = "Save Food, Fight Waste";
  elements.formSubtitle.textContent = "Bitte melde dich an";

  elements.nameGroup.style.display = "none";
  elements.roleGroup.style.display = "none";

  elements.nameInput.removeAttribute("required");
  elements.roleSelect.removeAttribute("required");

  elements.submitBtn.textContent = "Anmelden";

  elements.toggleText.innerHTML =
    'Noch kein Konto? <a href="#" class="link-primary" id="toggle-mode">Registrieren</a>';

  bindToggleLink();
  elements.form.reset();
}

function handleToggleMode(event) {
  event.preventDefault();

  if (isSignupMode) {
    switchToLoginMode();
  } else {
    switchToSignupMode();
  }
}

function initAuthMode() {
  bindToggleLink();
}

function getIsSignupMode() {
  return isSignupMode;
}

export {
  initAuthMode,
  handleToggleMode,
  switchToSignupMode,
  switchToLoginMode,
  getIsSignupMode,
};
