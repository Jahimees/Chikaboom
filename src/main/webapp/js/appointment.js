{
    let $serviceSelectApp = $("#service-select-app");
    let $clientSelectApp = $("#client-select-app");
    let $serviceSelectEv = $("#service-select-ev");
    let $clientSelectEv = $("#client-select-ev");
    let $workingDaySelect = $("#working-day-select");
    let $workingTimeSelect = $("#working-time-select");
    let $declineMessageBtn = $("#decline-message-btn");
    let $confirmMessageBtn = $("#confirm-message-btn");
    let $closeModalBtn = $("#close-modal-btn");
    let $appointmentWarn = $("#appointment-warn");
    let $serviceCostPlaceholder = $("#service-cost-placeholder");
    let $serviceTimePlaceholder = $("#service-time-placeholder");

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
                let firstName = secureCleanValue(appointment.masterAccount.userDetails.firstName ? appointment.masterAccount.userDetails.firstName + " " : " ")
                let lastName = secureCleanValue(appointment.masterAccount.userDetails.lastName ? appointment.masterAccount.userDetails.lastName : "")
                let visibleName = (firstName + " " + lastName).trim() ? (firstName + " " + lastName).trim() : appointment.masterAccount.username;
                nameText = "<a href='/chikaboom/account/" + appointment.masterAccount.idAccount + "'>" + visibleName + "</a>";
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
                    masterAppointmentsCache = json;
                    fillAppointmentsTable(json, isIncomeAppointments, idAccount, "appointment")
                }
            })
        } else {
            callMessagePopup("Ошибка!", "Невозможно загрузить записи на услуги")
        }
    }

    function callConfirmDeleteIncomeAppointmentPopup(idAccountMaster, idAppointment) {
        callMessagePopup("Удаление", "Вы действительно хотите удалить запись на услугу?");
        $declineMessageBtn.css("display", "block");
        $confirmMessageBtn.attr("onclick", "deleteIncomeAppointment(" + idAccountMaster + "," + idAppointment + ")");
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

        $declineMessageBtn.css("display", "block");
        $confirmMessageBtn.attr("onclick", "deleteOutcomeAppointment(" + idAccountClient + "," + idAppointment + ")");
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

    //////////////////////////////////////////////////////APPOINTMENTS MODALS

    let clientsDataCache;
    let masterServicesCache
    let masterWorkingDaysCache
    let masterAppointmentsCache

    $("#appointmentModal").on("show.bs.modal", function () {
        initAppointmentModal(false)
    })

    $("#createIncomeAppointmentModal").on("show.bs.modal", function () {
       initAppointmentModalWithClient(false);
    })

    function initAppointmentModalWithClient(isCalendar, masterServices, masterWorkingDays, masterAppointments, clientsData) {
        initAppointmentModal(isCalendar, masterServices, masterWorkingDays, masterAppointments);
        if (typeof clientsDataCache === "undefined") {
            if (typeof clientsData === "undefined" || clientsData === null) {
                clientsDataCache = loadClients(accountJson.idAccount)
            } else {
                if (typeof clientsData === "undefined") {
                    clientsDataCache = clientsData;
                }
            }
        }

        fillClientsToModal(clientsDataCache, isCalendar);
    }

    function initAppointmentModal(isCalendar, masterServices, masterWorkingDays, masterAppointments) {
        if (typeof masterServicesCache === "undefined") {
            if (typeof masterServices === "undefined") {
                masterServicesCache = loadMastersServices(accountJson.idAccount);
            } else {
                masterServicesCache = masterServices;
            }
        }

        if (typeof masterWorkingDaysCache === "undefined") {
            if (typeof masterWorkingDays === "undefined") {
                masterWorkingDaysCache = loadWorkingDaysData(accountJson.idAccount);
            } else {
                masterWorkingDaysCache = masterWorkingDays;
            }
        }
        fillServicesToModal(masterServicesCache, isCalendar);

        if (typeof masterAppointmentsCache === "undefined") {
            if (masterAppointments === null || typeof masterAppointments === "undefined") {
                masterAppointmentsCache = loadMastersAppointments(accountJson.idAccount)
            } else {
                if (typeof masterAppointments === "undefined") {
                    masterAppointmentsCache = masterAppointments;
                }
            }
        }
        fillWorkingDaysToModal(masterWorkingDaysCache);

        $workingTimeSelect.html('');
    }

    /////////////////////////////////////FILLING MODAL////////////////////////
    function fillServicesToModal(servicesJson, isCalendar) {
        let serviceSelect;
        if (isCalendar) {
            serviceSelect = $serviceSelectEv;
        } else {
            serviceSelect = $serviceSelectApp;
        }
        serviceSelect.html('')

        servicesJson.forEach(function (service) {
            let option = $("<option></option>").val(service.idService).text(service.name);

            serviceSelect.append(option);
        })
    }

    function fillWorkingDaysToModal(workingDays) {
        $workingDaySelect.html('');

        workingDays.forEach(function (workingDay) {
            let today = new Date();
            let workingDayObj = new Date(workingDay.date);
            if ((today.getFullYear() < workingDayObj.getFullYear())
                || (today.getFullYear() === workingDayObj.getFullYear()
                    && today.getMonth() < workingDayObj.getMonth())
                || (today.getFullYear() === workingDayObj.getFullYear()
                    && today.getMonth() === workingDayObj.getMonth()
                    && today.getDate() <= workingDayObj.getDate())) {

                let option = $("<option></option>")
                    .attr("start-time", workingDay.workingDayStart)
                    .attr("end-time", workingDay.workingDayEnd)
                    .text(workingDayObj.getDate() +
                        "." + (1 + workingDayObj.getMonth()) +
                        "." + workingDayObj.getFullYear())

                $workingDaySelect.append(option);
            }
        });
    }

    function fillClientsToModal(clientsJSON, isCalendar) {
        let clientSelect;
        if (isCalendar) {
            clientSelect = $("#client-select-ev");
        } else {
            clientSelect = $clientSelectApp;

        }

        clientsJSON.forEach(client => {
            clientSelect.append($("<option></option>")
                .val(client.idUserDetails)
                .text(client.firstName + " " + client.lastName));
        })
    }

    $workingDaySelect.on("click", (e) => {
        calculateServiceTime(
            e.target.selectedOptions[0].getAttribute("start-time"),
            e.target.selectedOptions[0].getAttribute("end-time"));
    })

    let prevWorkingDayStart
    let prevWorkingDayEnd

    function calculateServiceTime(workingDayStartTime, workingDayEndTime) {
        if (typeof workingDayStartTime !== "undefined") {
            prevWorkingDayStart = new Date("2000-09-09 " + workingDayStartTime);
        }

        if (typeof workingDayEndTime !== "undefined") {
            prevWorkingDayEnd = new Date("2000-09-09 " + workingDayEndTime);
        }

        if (typeof prevWorkingDayEnd === "undefined" || typeof prevWorkingDayStart === "undefined") {
            return
        }

        let workingCellsCount = (prevWorkingDayEnd.getHours() - prevWorkingDayStart.getHours()) * 2;

        if (prevWorkingDayEnd.getMinutes() > prevWorkingDayStart.getMinutes()) {
            workingCellsCount++;
        } else if (prevWorkingDayEnd.getMinutes() < prevWorkingDayStart.getMinutes()) {
            workingCellsCount--;
        }

        let workingCells = [];
        let iterator = new Date();
        iterator.setHours(prevWorkingDayStart.getHours());
        iterator.setMinutes(prevWorkingDayStart.getMinutes());

        let splittedDate = $workingDaySelect.val().split('.')
        let chosenDate = new Date(splittedDate[1] + "." + splittedDate[0] + "." + splittedDate[2]);
        let todayIsChosenDay = false;
        let today = new Date();

        if (today.getDate() === chosenDate.getDate()
            && today.getMonth() === chosenDate.getMonth()
            && today.getFullYear() === chosenDate.getFullYear()) {
            todayIsChosenDay = true;
        }

        for (let i = 0; i < workingCellsCount; i++) {
            workingCells[i] = {
                hours: iterator.getHours(),
                minutes: iterator.getMinutes(),
                time: iterator.getHours() + ":" + (iterator.getMinutes() === 0 ? '00' : iterator.getMinutes()), //deprecated
                value: true
            };

            if (todayIsChosenDay) {
                let todayTimeValue = today.getHours() * 60 + today.getMinutes();
                let iteratorTimeValue = iterator.getHours() * 60 + iterator.getMinutes();
                if (todayTimeValue >= iteratorTimeValue - 60) {
                    workingCells[i].value = false;
                }
            }

            iterator.setMinutes(iterator.getMinutes() + 30);
        }

        // masterAppointmentsJson
        masterAppointmentsCache.forEach(function (masterAppointment) {
            let appointmentDateTime = new Date(masterAppointment.appointmentDateTime);

            if (appointmentDateTime.getDate() === chosenDate.getDate()
                && appointmentDateTime.getMonth() === chosenDate.getMonth()
                && appointmentDateTime.getFullYear() === chosenDate.getFullYear()) {

                let trueTimer = 0;

                for (let i = 0; i < workingCellsCount; i++) {
                    if (trueTimer > 0) {
                        // Установка флага, что клеточка с конкретным временем занята
                        workingCells[i].value = false;
                        trueTimer--;
                    }
                    if (appointmentDateTime.getHours() === workingCells[i].hours
                        && appointmentDateTime.getMinutes() === workingCells[i].minutes) {

                        // Подсчёт количества клеточек со свободным временем (каждая клетка - полчаса)
                        workingCells[i].value = false;

                        let serviceTime = masterAppointment.service.time;
                        let serviceDurationTime = serviceTime.replace(' минут', '').split(' час');

                        if (serviceDurationTime.length === 1) {
                            trueTimer = 1;
                        } else {
                            serviceDurationTime[1] = serviceDurationTime[1].replace('а', '');

                            trueTimer = serviceDurationTime[0] * 2;
                            trueTimer += serviceDurationTime[1] === '' ? 0 : 1;
                        }

                        trueTimer--;
                    }
                }
            }
        })

        let idService = parseInt($serviceSelectApp.val());
        let currentService;

        masterServicesCache.forEach(function (service) {
            if (service.idService === idService) {
                currentService = service;
            }
        })

        let serviceDurationTime = currentService.time.replace(' минут', '').split(' час');
        let serviceDurationNumber;

        $serviceCostPlaceholder.text("Стоимость услуги: " + currentService.price + " р.");
        $serviceTimePlaceholder.text("Время услуги: " + currentService.time);

        if (serviceDurationTime.length === 1) {
            serviceDurationNumber = 1;
        } else {
            serviceDurationTime[1] = serviceDurationTime[1].replace('а', '');

            serviceDurationNumber = serviceDurationTime[0] * 2;
            serviceDurationNumber += serviceDurationTime[1] === '' ? 0 : 1;
        }

        $workingTimeSelect.html("");
        //запоминает позицию, с который начался просчёт ячеек для услуги
        let posReminder = -1;

        //количество "успешных ячеек". Например. услуга оказывается час. Значит нужно 2 ячейки по полчаса
        //значит, нужно проверить, будут ли следующие две ячейки свободными (без записи)
        let durationCounter = 0;
        for (let i = 0; i < workingCells.length; i++) {
            if (workingCells[i].value) {
                durationCounter++;
                if (durationCounter === 1) {
                    posReminder = i;
                }
                if (durationCounter === serviceDurationNumber) {
                    let option = document.createElement("option");
                    option.value = workingCells[posReminder].time;
                    option.innerHTML = workingCells[posReminder].time;

                    $workingTimeSelect.append(option);
                    durationCounter = 0;
                    i = posReminder;
                    posReminder = -1;
                }
            } else {
                durationCounter = 0;
                if (posReminder !== -1) {
                    i = posReminder;
                }
                posReminder = -1;
            }
        }
    }

    //////////////////////////////// LOADING DATA/////////////////////
    function loadWorkingDaysData(idAccount) {
        let workingDaysFromServer
        $.ajax({
            method: "get",
            url: "/accounts/" + idAccount + "/working-days",
            contentType: "application/json",
            dataType: "json",
            async: false,
            success: function (data) {
                workingDaysFromServer = data;
            },
            error: function () {
                callMessagePopup("Что-то пошло не так!", "Невозможно загрузить расписание!");
            }
        })

        return workingDaysFromServer ? workingDaysFromServer : []
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
                callMessagePopup("Невозможно загрузить записи!", "Что-то пошло не так. Невозможно загрузить записи!")
            }
        })

        return masterAppointmentsJson;
    }

    /////////////////////////////DO MAKE APPOINTMENT////////////////////////
    function doMakeAppointment(client, accountMasterJson) {
        let masterId = accountMasterJson.idAccount;

        let workingDayVal = $workingDaySelect.val();
        let workingTimeVal = $workingTimeSelect.val();

        if (workingTimeVal === '' || workingTimeVal === null) {
            $appointmentWarn.css("display", "block");
        } else if ($workingDaySelect.val() === null) {
            $closeModalBtn.click();

            callMessagePopup("Запись отклонена!", "Не выбрана дата записи! Или, возможно, мастер ещё не настроил свой график работы!");
        } else if (typeof client === 'undefined' || client === null || client.idAccount === 0) {
            $closeModalBtn.click();
            callMessagePopup("Запись отклонена!", "Сначала необходимо авторизоваться!")
        } else if (client.idAccount === masterId) {
            $closeModalBtn.click();
            callMessagePopup("Запись отклонена!", "Нельзя записываться самому к себе на услуги!")
        } else {
            let splittedWorkingDayVal = workingDayVal.split('.');
            let reversedWorkingDayVal = splittedWorkingDayVal[1] + "." + splittedWorkingDayVal[0] + "." + splittedWorkingDayVal[2]
            let appointmentDateTime = new Date(reversedWorkingDayVal);
            let splittedTime = workingTimeVal.split(":");

            appointmentDateTime.setHours(splittedTime[0]);
            appointmentDateTime.setMinutes(splittedTime[1]);

            $appointmentWarn.css("display", "none");
            let master = accountMasterJson;

            let idService = parseInt($serviceSelectApp.val());
            let service;
            let servicesJson = loadMastersServices(masterId);
            servicesJson.forEach(function (serv) {
                if (serv.idService === idService) {
                    service = serv
                }
            })

            let appointmentToSend = {
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
                    $('#appointmentModal').modal('hide');
                    $('#createIncomeAppointmentModal').modal('hide');
                    callMessagePopup("Запись оформлена!", "Вы успешно записались на услугу!")
                    masterAppointmentsCache = loadMastersAppointments(accountMasterJson.idAccount);

                    if (typeof $.fn.DataTable != "undefined") {
                        fillAppointmentsTable(masterAppointmentsCache, true, accountMasterJson.idAccount, "appointment");
                    }

                    calculateServiceTime();
                },
                error: function () {
                    $('#createIncomeAppointmentModal').modal('hide');
                    callMessagePopup("Произошла ошибка!", "Не удалось записаться на услугу!")
                }
            })
        }
    }

    function makeIncomeAppointment() {
        let clientUserDetails = {
            idUserDetails: $clientSelectApp.val()
        }

        doMakeAppointment(clientUserDetails, accountJson);
    }
}
