$("#settings-btn").on("click", function () {
    selectCurrent(this);
    loadSettingsTab()
});

$("#services-btn").on("click", function () {
    selectCurrent(this);
    loadServicesTab();
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

function loadStatistic() {
    underConstruction();
}

function loadOrders() {
    underConstruction();
}

function loadTimetable() {
    loadTimetableTab();
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
