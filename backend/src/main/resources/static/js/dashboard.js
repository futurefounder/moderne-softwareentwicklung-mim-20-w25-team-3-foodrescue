/**
 * Dashboard Module
 * Handles dashboard initialization, user info display, and logout
 */

// Check if user is logged in
function checkAuth() {
  const isLoggedIn = localStorage.getItem("isLoggedIn");
  if (!isLoggedIn || isLoggedIn !== "true") {
    // Redirect to login page if not logged in
    window.location.href = "/index.html";
    return false;
  }
  return true;
}

// Load user info from localStorage
function loadUserInfo() {
  const userName = localStorage.getItem("userName");
  const userEmail = localStorage.getItem("userEmail");
  const userRole = localStorage.getItem("userRole");

  // Update DOM elements
  const nameElement = document.getElementById("user-name");
  const emailElement = document.getElementById("user-email");
  const roleElement = document.getElementById("user-role");

  if (nameElement) {
    nameElement.textContent = userName || "Nicht verfügbar";
  }

  if (emailElement) {
    emailElement.textContent = userEmail || "Nicht verfügbar";
  }

  if (roleElement) {
    // Format role for display
    let roleDisplay = userRole || "Nicht verfügbar";
    if (userRole === "ABHOLER") {
      roleDisplay = "Abholer";
    } else if (userRole === "ANBIETER") {
      roleDisplay = "Anbieter";
    }
    roleElement.textContent = roleDisplay;
  }
}

// Handle logout
function handleLogout() {
  // Clear all user data from localStorage
  localStorage.removeItem("isLoggedIn");
  localStorage.removeItem("userName");
  localStorage.removeItem("userEmail");
  localStorage.removeItem("userRole");
  localStorage.removeItem("userId");

  // Redirect to login page
  window.location.href = "/index.html";
}

// Initialize dashboard
function init() {
  // Check authentication first
  if (!checkAuth()) {
    return;
  }

  // Load user information
  loadUserInfo();

  // Setup logout button
  const logoutBtn = document.getElementById("logout-btn");
  if (logoutBtn) {
    logoutBtn.addEventListener("click", handleLogout);
  }

  console.log("Dashboard initialized");
}

// Run initialization when DOM is ready
init();

