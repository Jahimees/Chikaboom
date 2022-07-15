$("#settings-btn").on("click", function () {
    unselectAll(this);
});

$("#profile-btn").on("click", function () {
    unselectAll(this);
});

$("#services-btn").on("click", function () {
    unselectAll(this);
});

$("#statistic-btn").on("click", function () {
    unselectAll(this);
});

$("#orders-btn").on("click", function () {
    unselectAll(this);
});

$("#chart-btn").on("click", function () {
    unselectAll(this);
});

$("#clients-btn").on("click", function () {
    unselectAll(this);
});

$("#messages-btn").on("click", function () {
    unselectAll(this);
});

$("#reviews-btn").on("click", function () {
    unselectAll(this);
});

function unselectAll(thisObj) {
    Array.from($(".menu-box > div")).forEach(function(elem) {
        elem.setAttribute("selected", "false");
    });

    thisObj.setAttribute("selected", "true");
}


