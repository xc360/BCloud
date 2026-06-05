const showAll = document.getElementById("backTop");
const returnTop = document.getElementById("returnTop");
returnTop.onclick = function () {
    showAll.scrollTop = 0;
};
returnTop.style.display = "none";
showAll.onscroll = function () {
    if (showAll.scrollTop <= 500) {
        returnTop.style.display = "none";
    } else {
        returnTop.style.display = "inline";
    }
};
