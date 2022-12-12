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

$("#appointments-btn").on("click", function () {
    selectCurrent(this);
    loadAppointmentTab()
});

$("#my-appointments-btn").on("click", function () {
    selectCurrent(this);
    loadMyAppointmentTab();
});

$("#timetable-btn").on("click", function () {
    selectCurrent(this);
    loadTimetableTab();
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

function loadClients() {
    underConstruction();
}

function loadMessages() {
    underConstruction();
}

function loadReviews() {
    underConstruction();
}
