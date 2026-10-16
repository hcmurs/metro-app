// Theme switch for rendered illustrations: ?theme=dark selects the dark tokens in the stylesheet.
document.documentElement.dataset.theme = new URLSearchParams(location.search).get("theme") === "dark" ? "dark" : "light";
