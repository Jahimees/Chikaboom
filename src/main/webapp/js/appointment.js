function fillAppointmentsTable(appointmentsJSON, isIncomeAppointment, idCurrentAccount, tableId) {
    let tableName = tableId ? tableId : "default";
    let $dataTable = $("#" + tableName + "_table");

    if (!$.fn.DataTable.isDataTable('#' + tableName)) {
        $dataTable.DataTable().data().clear();
        $dataTable.DataTable().destroy();
    }

    initDataTable(tableName);

    let timeOptions = {
        hour: 'numeric',
        minute: 'numeric'
    }

    appointmentsJSON.forEach(function (appointment) {
        let nameText, phoneText;

        if (isIncomeAppointment) {
            nameText = (appointment.userDetailsClient.firstName ? appointment.userDetailsClient.firstName + " " : " ") +
                (appointment.userDetailsClient.lastName ? appointment.userDetailsClient.lastName : "");
            phoneText = appointment.userDetailsClient.phone ? appointment.userDetailsClient.phone : " ";
        } else {
            nameText = "<a href='/chikaboom/account/" + appointment.masterAccount.idAccount + "'>" + (appointment.masterAccount.userDetails.firstName ? appointment.masterAccount.userDetails.firstName + " " : " ") +
                (appointment.masterAccount.userDetails.lastName ? appointment.masterAccount.userDetails.lastName : "") + "</a>";
            phoneText = appointment.masterAccount.userDetails.phone ? appointment.masterAccount.userDetails.phone : " ";
        }

        let rowNode = $dataTable.DataTable().row.add([
            appointment.service.name,
            new Date(appointment.appointmentDateTime).toLocaleDateString('ru'),
            new Date(appointment.appointmentDateTime).toLocaleTimeString("ru", timeOptions),
            appointment.service.price,
            appointment.service.time,
            phoneText,
            nameText,
            isIncomeAppointment ?
                "<img onclick='callConfirmDeleteIncomeAppointmentPopup(" + appointment.masterAccount.idAccount
                + "," + appointment.idAppointment + ")' src='/image/icon/cross_icon.svg' style='cursor: pointer; width: 15px' >"

                : "<img onclick='callConfirmDeleteOutcomeAppointmentPopup(" + idCurrentAccount
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
                fillAppointmentsTable(json, isIncomeAppointments, idAccount, "appointment")
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
                "onclick", "closePopup('message-popup'), loadAppointmentsData(" + idAccountMaster + ", true)");
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
            $("#confirm-message-btn")[0].setAttribute("onclick", "closePopup('message-popup'), loadAppointmentsData(" + idAccountClient + ", false)");
        },
        error: function () {
            repairDefaultMessagePopup();
            $("#popup-message-text")[0].innerText = "Невозможно удалить запись!"
            $(".message-popup > .popup-title > #popup-message-header")[0].innerText = "ОШИБКА!";
            openPopup('message-popup');

        }
    })
}

function doMakeAppointment(clientId, accountMasterJson, client) {
    let masterId = accountMasterJson.idAccount;

    let workingDayVal = $("#working-day-select").val();
    let workingTimeVal = $("#working-time-select").val();


    if (workingTimeVal === '') {
        $("#appointment-warn").css("display", "block");
    } else if ($("#working-day-select").val() === null) {
        $("#close-modal-btn").click();

        repairDefaultMessagePopup();
        $("#popup-message-text")[0].innerText = "Не выбрана дата записи! Или, возможно, мастер ещё не настроил свой график работы!"
        $(".message-popup > .popup-title > #popup-message-header")[0].innerText = "Запись отклонена!";
        openPopup('message-popup');
    } else if (typeof clientId === 'undefined' || clientId === 0) {
        $("#close-modal-btn").click();

        repairDefaultMessagePopup();
        $("#popup-message-text")[0].innerText = "Сначала необходимо авторизоваться!"
        $(".message-popup > .popup-title > #popup-message-header")[0].innerText = "Запись отклонена!";
        openPopup('message-popup');
    } else if (clientId === masterId) {
        $("#close-modal-btn").click();

        repairDefaultMessagePopup();
        $("#popup-message-text")[0].innerText = "Нельзя записываться самому к себе на услуги!"
        $(".message-popup > .popup-title > #popup-message-header")[0].innerText = "Запись отклонена!";
        openPopup('message-popup');
    } else {
        let appointmentDateTime = new Date(workingDayVal);
        let splittedTime = workingTimeVal.split(":");
        appointmentDateTime.setHours(splittedTime[0]);
        appointmentDateTime.setMinutes(splittedTime[1]);

        $("#appointment-warn").css("display", "none");
        let master = accountMasterJson;

        let idService = parseInt($("#services-select").val());
        let service;
        let servicesJson = loadMastersServices(masterId);
        servicesJson.forEach(function (serv) {
            if (serv.idService === idService) {
                service = serv
            }
        })

        let appointmentToSend = {
            clientAccount: client,
            masterAccount: master,
            userDetailsClient: client.userDetails,
            service: service,
            appointmentDateTime: appointmentDateTime
        }

        $.ajax({
            method: "post",
            url: "/appointments",
            contentType: "application/json",
            dataType: "json",
            data: JSON.stringify(appointmentToSend),
            success: function () {
                $("#close-modal-btn").click();

                repairDefaultMessagePopup();
                $("#popup-message-text")[0].innerText = "Вы успешно записались на услугу!"
                $(".message-popup > .popup-title > #popup-message-header")[0].innerText = "Запись оформлена!";
                openPopup('message-popup');

                masterAppointmentsJson = loadMastersAppointments(accountMasterJson.idAccount);
                calculateServiceTime();
            },
            error: function () {
                repairDefaultMessagePopup();
                $("#popup-message-text")[0].innerText = "Не удалось записаться на услугу!"
                $(".message-popup > .popup-title > #popup-message-header")[0].innerText = "Произошла ошибка!";
                openPopup('message-popup');
            }
        })
    }
}
