/**
 * Modal Dialog Controller
 * This script manages modal dialog functionality for confirmation dialogs
 */

// Event listeners registered when the DOM is loaded
document.addEventListener('DOMContentLoaded', function() {
    // Logout button opens the modal
    const logoutBtn = document.getElementById('logoutBtn');
    if (logoutBtn) {
        logoutBtn.addEventListener('click', openLogoutModal);
    }

    // Cancel button closes the modal
    const cancelBtn = document.getElementById('cancelBtn');
    if (cancelBtn) {
        cancelBtn.addEventListener('click', closeLogoutModal);
    }

    // Close the modal when clicking outside it
    const modal = document.getElementById("logoutModal");
    if (modal) {
        window.addEventListener('click', function(event) {
            if (event.target === modal) {
                closeLogoutModal();
            }
        });
    }

    // Support an Escape key to close the modal
    document.addEventListener('keydown', function(event) {
        if (event.key === "Escape" && modal && modal.style.display === "block") {
            closeLogoutModal();
        }
    });
});

/**
 * Opens the logout confirmation modal
 */
function openLogoutModal() {
    const modal = document.getElementById("logoutModal");
    if (modal) {
        modal.style.display = "block";
        // Small delay to allow CSS transition to work
        setTimeout(() => {
            modal.style.opacity = "1";
        }, 10);
    }
}

/**
 * Closes the logout confirmation modal
 */
function closeLogoutModal() {
    const modal = document.getElementById("logoutModal");
    if (modal) {
        modal.style.opacity = "0";
        // Wait for the transition to complete before hiding
        setTimeout(() => {
            modal.style.display = "none";
        }, 300);
    }
}