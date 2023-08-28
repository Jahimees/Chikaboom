function fillAppointmentsTable(appointmentsJSON, isIncomeAppointment) {

    if (typeof $("#default_table").DataTable() !== 'undefined') {
        $("#default_table").DataTable().data().clear();
    }

    let timeOptions = {
        hour: 'numeric',
        minute: 'numeric'
    }

    appointmentsJSON.forEach(function (appointment) {

        var rowNode = $("#default_table").DataTable().row.add([
            appointment.service.name,
            new Date(appointment.appointmentDateTime).toLocaleDateString('ru'),
            new Date(appointment.appointmentDateTime).toLocaleTimeString("ru", timeOptions),
            appointment.service.price,
            appointment.service.time,
            isIncomeAppointment ? appointment.clientAccount.phoneCode.phoneCode + " "
                + appointment.clientAccount.phone : appointment.masterAccount.phoneCode.phoneCode + " "
                + appointment.masterAccount.phone,
            isIncomeAppointment ? appointment.clientAccount.username : appointment.masterAccount.username,
            isIncomeAppointment ?
                "<img onclick='callConfirmDeleteIncomeAppointmentPopup(" + appointment.masterAccount.idAccount
                + "," + appointment.idAppointment + ")' src='/image/icon/cross_icon.svg' style='cursor: pointer; width: 15px' >"

                : "<img onclick='callConfirmDeleteOutcomeAppointmentPopup(" + appointment.clientAccount.idAccount
                + "," + appointment.idAppointment + ")' src='/image/icon/cross_icon.svg' style='cursor: pointer; width: 15px'>",
        ])
            .draw()
            .node();

        if (new Date(appointment.appointmentDateTime).getTime() <= new Date().getTime()) {
            $(rowNode).addClass("past-appointment");
        }
    })
}

function loadAppointmentsData(idAccount, isIncomeAppointments) {
    if (typeof isIncomeAppointments !== 'undefined' && isIncomeAppointments !== null) {
        $.ajax({
            method: "get",
            url: "/accounts/" + idAccount + (isIncomeAppointments ? "/income-appointments" : "/outcome-appointments"),
            contentType: "application/json",
            dataType: "json",
            async: false,
            success: function (json) {
                fillAppointmentsTable(json, isIncomeAppointments)
            }
        })
    } else {
        repairDefaultMessagePopup();
        $("#popup-message-text")[0].innerText = "Невозможно загрузить записи на услуги";
        $("#popup-message-header")[0].innerText = "Ошибка!";
        openPopup('message-popup');
    }
}

function callConfirmDeleteIncomeAppointmentPopup(idAccountMaster, idAppointment) {
    repairDefaultMessagePopup();
    $("#decline-message-btn")[0].style.display = "block";
    $("#confirm-message-btn")[0].setAttribute("onclick", "deleteIncomeAppointment(" + idAccountMaster + "," + idAppointment + ")");

    $("#popup-message-text")[0].innerText = "Вы действительно хотите удалить запись на услугу?";
    $(".message-popup > .popup-title > #popup-message-header")[0].innerText = "Удаление";
    openPopup("message-popup");
}

function deleteIncomeAppointment(idAccountMaster, idAppointment) {
    $.ajax({
        type: "delete",
        url: "/accounts/" + idAccountMaster + "/income-appointments/" + idAppointment,
        contentType: "application/text",
        dataType: "text",
        success: function () {
            repairDefaultMessagePopup();
            $("#popup-message-text")[0].innerText = "Запись успешно удалена!"
            $(".message-popup > .popup-title > #popup-message-header")[0].innerText = "Удалено";
            openPopup('message-popup');
            $("#confirm-message-btn")[0].setAttribute(
                "onclick", "closePopup('message-popup'), loadIncomeAppointmentsData(" + idAccountMaster + ")");
        },
        error: function () {
            repairDefaultMessagePopup();
            $("#popup-message-text")[0].innerText = "Невозможно удалить запись!"
            $(".message-popup > .popup-title > #popup-message-header")[0].innerText = "ОШИБКА!";
            openPopup('message-popup');

        }
    })
}

function callConfirmDeleteOutcomeAppointmentPopup(idAccountClient, idAppointment) {
    repairDefaultMessagePopup();
    $("#decline-message-btn")[0].style.display = "block";
    $("#confirm-message-btn")[0].setAttribute(
        "onclick", "deleteOutcomeAppointment(" + idAccountClient + "," + idAppointment + ")");

    $("#popup-message-text")[0].innerText = "Вы действительно хотите удалить запись на услугу?";
    $(".message-popup > .popup-title > #popup-message-header")[0].innerText = "Удаление";
    openPopup("message-popup");
}

function deleteOutcomeAppointment(idAccountClient, idAppointment) {
    $.ajax({
        type: "delete",
        url: "/accounts/" + idAccountClient + "/outcome-appointments/" + idAppointment,
        contentType: "application/text",
        dataType: "text",
        success: function () {
            repairDefaultMessagePopup();
            $("#popup-message-text")[0].innerText = "Запись успешно удалена!"
            $(".message-popup > .popup-title > #popup-message-header")[0].innerText = "Удалено";
            openPopup('message-popup');
            $("#confirm-message-btn")[0].setAttribute("onclick", "closePopup('message-popup'), loadOutcomeAppointmentsData(" + idAccountClient + ")");
        },
        error: function () {
            repairDefaultMessagePopup();
            $("#popup-message-text")[0].innerText = "Невозможно удалить запись!"
            $(".message-popup > .popup-title > #popup-message-header")[0].innerText = "ОШИБКА!";
            openPopup('message-popup');

        }
    })
}


