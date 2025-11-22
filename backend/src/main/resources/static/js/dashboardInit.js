/**
 * Dashboard Module
 * Handles dashboard initialization, user info display, and logout
 */

// Check if user is logged in
function checkAuth() {
  const isLoggedIn = localStorage.getItem("isLoggedIn");
  if (!isLoggedIn || isLoggedIn !== "true") {
    // Redirect to login page if not logged in
    window.location.href = "./index.html";
    return false;
  }
  return true;
}

// Load user info from localStorage
function loadUserInfo() {
  const userName = localStorage.getItem("userName");

  // Update greeting with user name
  const greetingNameElement = document.getElementById("user-greeting-name");
  if (greetingNameElement) {
    greetingNameElement.textContent = userName || "Benutzer";
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
