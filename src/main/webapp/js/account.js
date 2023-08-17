function initializePage(idAccount) {
    accountJson = loadAccount(idAccount);

    if (isMaster(accountJson)) {
        servicesJson = loadMastersServices(accountJson.idAccount);
        fillServiceTable(servicesJson, true);
        fillServicesModal(servicesJson);
        fillWorkingDays(accountJson);

        masterAppointmentsJson = loadMastersAppointments(accountJson.idAccount);
        let addressData = accountJson.address != null ? accountJson.address : "";
        $("#address-placeholder").text("Адрес: " + addressData);
        $("#profession-placeholder").text(accountJson.about !== null ? accountJson.about.profession : "");
        $("#about-text-placeholder").text(accountJson.about !== null ? accountJson.about.text : "");
    } else {
        $(".master-only").remove();
        $(".main-information").css("height", "auto");
    }

    $("#username-placeholder").text(accountJson.username);
    if (accountJson.phoneVisible) {
        $("#phone-placeholder").text("Телефон: " + "+" + accountJson.phoneCode.phoneCode + " " + accountJson.phone);
    }
}

function loadAccount(idAccount) {
    var accountJson

    $.ajax({
        contentType: "application/json",
        dataType: "json",
        method: "get",
        async: false,
        url: "/accounts/" + idAccount,
        success: function (data) {
            return accountJson = data;
        },
        error: function () {
            location.href = "/chikaboom/404";
        }
    });

    return accountJson;
}

function isMaster(accountJson) {
    var result = false;

    accountJson.roles.forEach(function (role) {
        if (role.name === "ROLE_MASTER") {
            result = true;
        }
    });

    return result;
}

function loadMastersServices(idAccount) {
    var servicesJson;

    $.ajax({
        contentType: "application/json",
        dataType: "json",
        method: "get",
        async: false,
        url: "/accounts/" + idAccount + "/services",
        success: function (data) {
            servicesJson = data;
        },
        error: function () {
            repairDefaultMessagePopup();
            $("#popup-message-text")[0].innerText = "Что-то пошло не так. Невозможно загрузить услуги!"
            $(".message-popup > .popup-title > #popup-message-header")[0].innerText = "Невозможно загрузить услуги!";
            openPopup('message-popup');
        }
    });

    return servicesJson;
}

function loadMastersAppointments(idAccount) {
    let masterAppointmentsJson;

    $.ajax({
        type: "get",
        url: "/accounts/" + idAccount + "/income-appointments",
        contentType: "application/json",
        dataType: "json",
        async: false,
        success: function (data) {
            masterAppointmentsJson = data;
        },
        error: function () {
            repairDefaultMessagePopup();
            $("#popup-message-text")[0].innerText = "Что-то пошло не так. Невозможно загрузить записи!"
            $(".message-popup > .popup-title > #popup-message-header")[0].innerText = "Невозможно загрузить записи!";
            openPopup('message-popup');
        }
    })

    return masterAppointmentsJson;
}

$("#make-appointment-btn").on("click", function () {
    masterAppointmentsJson = loadMastersAppointments(accountJson.idAccount);
})
