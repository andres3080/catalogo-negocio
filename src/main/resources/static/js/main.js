const revealElements = document.querySelectorAll(".reveal");
const dropdownToggle = document.querySelector(".nav-dropdown-toggle");
const dropdown = document.querySelector(".nav-dropdown");
const dropdownLinks = document.querySelectorAll(".nav-dropdown-menu a");

const observer = new IntersectionObserver((entries) => {
    entries.forEach((entry) => {
        if (entry.isIntersecting) {
            entry.target.classList.add("visible");
        }
    });
}, { threshold: 0.15 });

revealElements.forEach((el) => observer.observe(el));

if (dropdownToggle && dropdown) {
    dropdownToggle.addEventListener("click", (event) => {
        event.preventDefault();
        const expanded = dropdown.classList.toggle("is-open");
        dropdownToggle.setAttribute("aria-expanded", expanded ? "true" : "false");
    });

    document.addEventListener("click", (event) => {
        if (!dropdown.contains(event.target)) {
            dropdown.classList.remove("is-open");
            dropdownToggle.setAttribute("aria-expanded", "false");
        }
    });

    dropdownLinks.forEach((link) => {
        link.addEventListener("click", () => {
            dropdown.classList.remove("is-open");
            dropdownToggle.setAttribute("aria-expanded", "false");
        });
    });
}
