function loadTimetableTab(idAccount, thisObj) {
    selectCurrent(thisObj);
    $.ajax({
        type: "get",
        url: "/chikaboom/personality/" + idAccount + "/timetable",
        contentType: "application/text",
        dataType: "text",
        data: {},
        success: function (data) {
            $("#content-placeholder").html(data);
        },
        error: function () {
            $("#popup-message-text")[0].innerText = "Невозможно загрузить вкладку график!"
            $(".message-popup > .popup-title > #popup-message-header")[0].innerText = "ОШИБКА!";
            openPopup('message-popup');
        }
    });
}

function loadServicesTab(idAccount, thisObj) {
    selectCurrent(thisObj)

    $.ajax({
        type: "get",
        url: "/chikaboom/personality/" + idAccount + "/services",
        contentType: "application/text",
        dataType: "text",
        success: function (data) {
            $("#content-placeholder").html(data);
        },
        error: function () {
            $("#popup-message-text")[0].innerText = "Невозможно загрузить услуги!"
            $(".message-popup > .popup-title > #popup-message-header")[0].innerText = "ОШИБКА!";
            openPopup('message-popup');
        }
    });
}

function loadSettingsTab(idAccount, thisObj) {
    selectCurrent(thisObj);

    $.ajax({
        type: "get",
        url: "/chikaboom/personality/" + idAccount + "/settings",
        contentType: "application/text",
        dataType: "text",
        success: function (data) {
            $("#content-placeholder").html(data);
        },
        error: function () {
            $("#popup-message-text")[0].innerText = "Невозможно загрузить настройки!"
            $(".message-popup > .popup-title > #popup-message-header")[0].innerText = "ОШИБКА!";
            openPopup('message-popup');
        }
    });
}

function loadAppointmentTab(idAccount, thisObj) {
    selectCurrent(thisObj);

    $.ajax({
        type: "get",
        url: "/chikaboom/personality/" + idAccount + "/appointment",
        contentType: "application/text",
        dataType: "text",
        data: {},
        success: function (data) {
            $("#content-placeholder").html(data);
        },
        error: function () {
            $("#popup-message-text")[0].innerText = "Невозможно загрузить вкладку записи!"
            $(".message-popup > .popup-title > #popup-message-header")[0].innerText = "ОШИБКА!";
            openPopup('message-popup');
        }
    });
}


function loadStatistic(idAccount, thisObj) {
    selectCurrent(thisObj);
    underConstruction();
}

function loadClients(idAccount, thisObj) {
    selectCurrent(thisObj)
    underConstruction();
}

function loadMessages(idAccount, thisObj) {
    selectCurrent(thisObj)
    underConstruction();
}

function loadReviews(idAccount, thisObj) {
    selectCurrent(thisObj)
    underConstruction();
}

function underConstruction() {
    $.ajax({
        type: "get",
        url: "/chikaboom/under_construction",
        contentType: "application/text",
        dataType: "text",
        data: {},
        success: function (data) {
            $("#content-placeholder").html(data);
        },
        error: function () {
            console.error("ERROR")
        }
    });
}

function selectCurrent(thisObj) {
    Array.from($(".menu-box > div")).forEach(function (elem) {
        elem.setAttribute("selected", "false");
    });

    thisObj.setAttribute("selected", "true");
}
