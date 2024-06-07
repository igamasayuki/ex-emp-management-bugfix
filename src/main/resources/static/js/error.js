"use strict";

window.addEventListener("DOMContentLoaded", () => {
    setTimeout(() => transiton(), 5000);
})

const transiton = () => {
    const url = document.querySelector(".url").getAttribute("href");
    window.location.href = url;
}