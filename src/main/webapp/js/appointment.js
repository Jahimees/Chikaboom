{
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
                phoneText = appointment.userDetailsClient.displayedPhone ? appointment.userDetailsClient.displayedPhone : " ";
            } else {
                nameText = "<a href='/chikaboom/account/" + appointment.masterAccount.idAccount + "'>" + secureCleanValue(appointment.masterAccount.userDetails.firstName ? appointment.masterAccount.userDetails.firstName + " " : " ") +
                    secureCleanValue(appointment.masterAccount.userDetails.lastName ? appointment.masterAccount.userDetails.lastName : "") + "</a>";
                phoneText = appointment.masterAccount.userDetails.displayedPhone ? appointment.masterAccount.userDetails.displayedPhone : " ";
            }

            let rowNode = $dataTable.DataTable().row.add([
                secureCleanValue(appointment.service.name),
                new Date(appointment.appointmentDateTime).toLocaleDateString('ru'),
                new Date(appointment.appointmentDateTime).toLocaleTimeString("ru", timeOptions),
                secureCleanValue(appointment.service.price),
                secureCleanValue(appointment.service.time),
                secureCleanValue(phoneText),
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
            callMessagePopup("Ошибка!", "Невозможно загрузить записи на услуги")
        }
    }

    function callConfirmDeleteIncomeAppointmentPopup(idAccountMaster, idAppointment) {
        repairDefaultMessagePopup();
        $("#decline-message-btn").css("display", "block");
        $("#confirm-message-btn").attr("onclick", "deleteIncomeAppointment(" + idAccountMaster + "," + idAppointment + ")");
        callMessagePopup("Удаление", "Вы действительно хотите удалить запись на услугу?");
    }

    function deleteIncomeAppointment(idAccountMaster, idAppointment) {
        $.ajax({
            type: "delete",
            url: "/accounts/" + idAccountMaster + "/income-appointments/" + idAppointment,
            contentType: "application/text",
            dataType: "text",
            success: function () {
                callMessagePopup("Удалено", "Запись успешно удалена");
                loadAppointmentsData(idAccountMaster, true);
            },
            error: function () {
                callMessagePopup("Ошибка!", "Невозможно удалить запись!");
            }
        })
    }

    function callConfirmDeleteOutcomeAppointmentPopup(idAccountClient, idAppointment) {
        callMessagePopup("Удаление", "Вы действительно хотите удалить запись на услугу?");

        $("#decline-message-btn").css("display", "block");
        $("#confirm-message-btn").attr("onclick", "deleteOutcomeAppointment(" + idAccountClient + "," + idAppointment + ")");
    }

    function deleteOutcomeAppointment(idAccountClient, idAppointment) {
        $.ajax({
            type: "delete",
            url: "/accounts/" + idAccountClient + "/outcome-appointments/" + idAppointment,
            contentType: "application/text",
            dataType: "text",
            success: function () {
                callMessagePopup("Удалено", "Запись успешно удалена!")
                loadAppointmentsData(idAccountClient, false)
            },
            error: function () {
                callMessagePopup("Ошибка!", "Невозможно удалить запись!")
            }
        })
    }

    function doMakeAppointment(client, accountMasterJson) {
        let masterId = accountMasterJson.idAccount;

        let workingDayVal = $("#working-day-select").val();
        let workingTimeVal = $("#working-time-select").val();


        if (workingTimeVal === '') {
            $("#appointment-warn").css("display", "block");
        } else if ($("#working-day-select").val() === null) {
            $("#close-modal-btn").click();

            callMessagePopup("Запись отклонена!", "Не выбрана дата записи! Или, возможно, мастер ещё не настроил свой график работы!");
        } else if (typeof client === 'undefined' || client === null || client.idAccount === 0) {
            $("#close-modal-btn").click();
            callMessagePopup("Запись отклонена!", "Сначала необходимо авторизоваться!")
        } else if (client.idAccount === masterId) {
            $("#close-modal-btn").click();
            callMessagePopup("Запись отклонена!", "Нельзя записываться самому к себе на услуги!")
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
                clientAccount: {
                    idAccount: client.idAccount
                },
                masterAccount: master,
                userDetailsClient: {
                    idUserDetails: client.idUserDetails
                },
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
                    callMessagePopup("Запись оформлена!", "Вы успешно записались на услугу!")
                    masterAppointmentsJson = loadMastersAppointments(accountMasterJson.idAccount);
                    calculateServiceTime();
                },
                error: function () {
                    callMessagePopup("Произошла ошибка!", "Не удалось записаться на услугу!")
                }
            })
        }
    }
}