$("#settings-btn").on("click", function () {
    selectCurrent(this);
    loadSettings();
});

$("#profile-btn").on("click", function () {
    selectCurrent(this);
    loadProfile();
});

$("#services-btn").on("click", function () {
    selectCurrent(this);
    loadServices();
});

$("#statistic-btn").on("click", function () {
    selectCurrent(this);
    loadStatistic();
});

$("#orders-btn").on("click", function () {
    selectCurrent(this);
    loadOrders()
});

$("#timetable-btn").on("click", function () {
    selectCurrent(this);
    loadTimetable();
});

$("#clients-btn").on("click", function () {
    selectCurrent(this);
    loadClients();
});

$("#messages-btn").on("click", function () {
    selectCurrent(this);
    loadMessages();
});

$("#reviews-btn").on("click", function () {
    selectCurrent(this);
    loadReviews();
});

function selectCurrent(thisObj) {
    Array.from($(".menu-box > div")).forEach(function (elem) {
        elem.setAttribute("selected", "false");
    });

    thisObj.setAttribute("selected", "true");
}

function loadProfile() {
    document.location.replace("/chikaboom/account");
}

function loadServices() {
    underConstruction();
}

function loadStatistic() {
    underConstruction();
}

function loadOrders() {
    underConstruction();
}

function loadTimetable() {
    underConstruction();
}

function loadClients() {
    underConstruction();
}

function loadMessages() {
    underConstruction();
}

function loadReviews() {
    underConstruction();
}
