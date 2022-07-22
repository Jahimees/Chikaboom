$("#settings-btn").on("click", function () {
    selectCurrent(this);
    loadSettings();
});

$("#profile-btn").on("click", function () {
    selectCurrent(this);
});

$("#services-btn").on("click", function () {
    selectCurrent(this);
});

$("#statistic-btn").on("click", function () {
    selectCurrent(this);
});

$("#orders-btn").on("click", function () {
    selectCurrent(this);
});

$("#chart-btn").on("click", function () {
    selectCurrent(this);
});

$("#clients-btn").on("click", function () {
    selectCurrent(this);
});

$("#messages-btn").on("click", function () {
    selectCurrent(this);
});

$("#reviews-btn").on("click", function () {
    selectCurrent(this);
});

function selectCurrent(thisObj) {
    Array.from($(".menu-box > div")).forEach(function(elem) {
        elem.setAttribute("selected", "false");
    });

    thisObj.setAttribute("selected", "true");
}

function loadSettings() {

}

