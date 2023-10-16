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

    function fillAppointmentsTable(appointmentsFacadeJson, isIncomeAppointment, idCurrentAccount, tableId) {
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

        appointmentsFacadeJson.forEach(function (appointmentFacade) {
            let nameText, phoneText;

            if (isIncomeAppointment) {
                nameText = (appointmentFacade.userDetailsFacadeClient.firstName ?
                        appointmentFacade.userDetailsFacadeClient.firstName + " " : " ") +
                    (appointmentFacade.userDetailsFacadeClient.lastName ?
                        appointmentFacade.userDetailsFacadeClient.lastName : "");
                phoneText = appointmentFacade.userDetailsFacadeClient.displayedPhone ?
                    appointmentFacade.userDetailsFacadeClient.displayedPhone : " ";
            } else {
                let firstName = secureCleanValue(appointmentFacade.masterAccountFacade.userDetailsFacade.firstName ?
                    appointmentFacade.masterAccountFacade.userDetailsFacade.firstName + " " : " ")
                let lastName = secureCleanValue(appointmentFacade.masterAccountFacade.userDetailsFacade.lastName ?
                    appointmentFacade.masterAccountFacade.userDetailsFacade.lastName : "")
                let visibleName = (firstName + " " + lastName).trim() ?
                    (firstName + " " + lastName).trim() : appointmentFacade.masterAccountFacade.username;
                nameText = "<a href='/chikaboom/account/" + appointmentFacade.masterAccountFacade.idAccount + "'>" + visibleName + "</a>";
                phoneText = appointmentFacade.masterAccountFacade.userDetailsFacade.displayedPhone ?
                    appointmentFacade.masterAccountFacade.userDetailsFacade.displayedPhone : " ";
            }

            let rowNode = $dataTable.DataTable().row.add([
                secureCleanValue(appointmentFacade.serviceFacade.name),
                new Date(appointmentFacade.appointmentDateTime).toLocaleDateString('ru'),
                new Date(appointmentFacade.appointmentDateTime).toLocaleTimeString("ru", timeOptions),
                secureCleanValue(appointmentFacade.serviceFacade.price),
                secureCleanValue(appointmentFacade.serviceFacade.time),
                secureCleanValue(phoneText),
                nameText,
                isIncomeAppointment ?
                    "<img onclick='callConfirmDeleteIncomeAppointmentPopup(" + appointmentFacade.masterAccountFacade.idAccount
                    + "," + appointmentFacade.idAppointment + ")' src='/image/icon/cross_icon.svg' style='cursor: pointer; width: 15px' >"

                    : "<img onclick='callConfirmDeleteOutcomeAppointmentPopup(" + idCurrentAccount
                    + "," + appointmentFacade.idAppointment + ")' src='/image/icon/cross_icon.svg' style='cursor: pointer; width: 15px'>",
            ])
                .draw()
                .node();

            if (new Date(appointmentFacade.appointmentDateTime).getTime() <= new Date().getTime()) {
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
                    // TODO optimize. Не нужны прошлые записи. Их догружать отдельно
                    console.log("Endpoint 2 done::: ");
                    masterAppointmentsFacadeCache = json;
                    fillAppointmentsTable(
                        json,
                        isIncomeAppointments,
                        idAccount,
                        "appointment")
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
    let masterAppointmentsFacadeCache

    $("#appointmentModal").on("show.bs.modal", function () {
        initAppointmentModal(false)
    })

    $("#createIncomeAppointmentModal").on("show.bs.modal", function () {
        initAppointmentModalWithClient(false);
    })

    function initAppointmentModalWithClient(isCalendar, masterServicesFacade, masterWorkingDaysFacade, masterAppointments, clientsDataFacade) {
        initAppointmentModal(isCalendar, masterServicesFacade, masterWorkingDaysFacade, masterAppointments);
        if (typeof clientsDataCache === "undefined") {
            if (typeof clientsDataFacade === "undefined" || clientsDataFacade === null) {
                clientsDataCache = loadClients(accountFacadeJson.idAccount)
            } else {
                clientsDataCache = clientsDataFacade;
            }
        }

        fillClientsToModal(clientsDataCache, isCalendar);
    }

    function initAppointmentModal(isCalendar, masterServicesFacade, masterWorkingDaysFacade, masterAppointmentsFacade) {
        if (typeof masterServicesCache === "undefined") {
            if (typeof masterServicesFacade === "undefined") {
                masterServicesCache = loadMastersServices(accountFacadeJson.idAccount);
            } else {
                masterServicesCache = masterServicesFacade;
            }
        }

        if (typeof masterWorkingDaysCache === "undefined") {
            if (typeof masterWorkingDaysFacade === "undefined") {
                masterWorkingDaysCache = loadWorkingDaysData(accountFacadeJson.idAccount);
            } else {
                masterWorkingDaysCache = masterWorkingDaysFacade;
            }
        }
        fillServicesToModal(masterServicesCache, isCalendar);

        if (typeof masterAppointmentsFacadeCache === "undefined") {
            if (masterAppointmentsFacade === null || typeof masterAppointmentsFacade === "undefined") {
                masterAppointmentsFacadeCache = loadMastersAppointments(accountFacadeJson.idAccount)
            } else {
                if (typeof masterAppointmentsFacade === "undefined") {
                    masterAppointmentsFacadeCache = masterAppointmentsFacade;
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

    function fillWorkingDaysToModal(workingDaysFacade) {
        $workingDaySelect.html('');

        workingDaysFacade.forEach(function (workingDay) {
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
        clientSelect.html('')

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
        masterAppointmentsFacadeCache.forEach(function (masterAppointmentFacade) {
            let appointmentDateTime = new Date(masterAppointmentFacade.appointmentDateTime);

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

                        let serviceTime = masterAppointmentFacade.serviceFacade.time;
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
        let currentServiceFacade;

        masterServicesCache.forEach(function (serviceFacade) {
            if (serviceFacade.idService === idService) {
                currentServiceFacade = serviceFacade;
            }
        })

        let serviceDurationTime = currentServiceFacade.time.replace(' минут', '').split(' час');
        let serviceDurationNumber;

        $serviceCostPlaceholder.text("Стоимость услуги: " + currentServiceFacade.price + " р.");
        $serviceTimePlaceholder.text("Время услуги: " + currentServiceFacade.time);

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
        let workingDaysFacadeFromServer
        $.ajax({
            method: "get",
            url: "/accounts/" + idAccount + "/working-days",
            contentType: "application/json",
            dataType: "json",
            async: false,
            success: function (data) {
                console.log("Endpoint 3 done::: ");
                workingDaysFacadeFromServer = data;
            },
            error: function () {
                callMessagePopup("Что-то пошло не так!", "Невозможно загрузить расписание!");
            }
        })

        return workingDaysFacadeFromServer ? workingDaysFacadeFromServer : []
    }

    function loadMastersAppointments(idAccount) {
        let masterAppointmentsFacadeJson;

        $.ajax({
            type: "get",
            url: "/accounts/" + idAccount + "/income-appointments",
            contentType: "application/json",
            dataType: "json",
            async: false,
            success: function (data) {
                console.log("Endpoint 4 error::: ")
                console.log(data);
                masterAppointmentsFacadeJson = data;
            },
            error: function () {
                callMessagePopup("Невозможно загрузить записи!", "Что-то пошло не так. Невозможно загрузить записи!")
            }
        })

        return masterAppointmentsFacadeJson;
    }

    /////////////////////////////DO MAKE APPOINTMENT////////////////////////
    function doMakeAppointment(clientPrincipal, accountMasterFacadeJson) {
        let masterId = accountMasterFacadeJson.idAccount;

        let workingDayVal = $workingDaySelect.val();
        let workingTimeVal = $workingTimeSelect.val();

        if (workingTimeVal === '' || workingTimeVal === null) {
            $appointmentWarn.css("display", "block");
        } else if ($workingDaySelect.val() === null) {
            $closeModalBtn.click();

            callMessagePopup("Запись отклонена!", "Не выбрана дата записи! Или, возможно, мастер ещё не настроил свой график работы!");
        } else if (typeof clientPrincipal === 'undefined' || clientPrincipal === null || clientPrincipal.idAccount === 0) {
            $closeModalBtn.click();
            callMessagePopup("Запись отклонена!", "Сначала необходимо авторизоваться!")
        } else if (clientPrincipal.idAccount === masterId) {
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
            let master = accountMasterFacadeJson;

            let idService = parseInt($serviceSelectApp.val());
            let service;
            let servicesJson = loadMastersServices(masterId);
            servicesJson.forEach(function (serv) {
                if (serv.idService === idService) {
                    service = serv
                }
            })

            let appointmentToSend = {
                masterAccountFacade: master,
                userDetailsFacadeClient: {
                    idUserDetails: clientPrincipal.idUserDetails
                },
                serviceFacade: service,
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
                    masterAppointmentsFacadeCache = loadMastersAppointments(accountMasterFacadeJson.idAccount);

                    if (typeof $.fn.DataTable != "undefined") {
                        fillAppointmentsTable(masterAppointmentsFacadeCache, true, accountMasterFacadeJson.idAccount, "appointment");
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
        let clientUserDetailsFacade = {
            idUserDetails: $clientSelectApp.val()
        }

        doMakeAppointment(clientUserDetailsFacade, accountFacadeJson);
    }
}
