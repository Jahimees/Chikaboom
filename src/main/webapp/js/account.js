{
    let servicesJson;

    function initializePage(idAccount) {
        accountJson = loadAccount(idAccount);

        let nameText;
        if (accountJson.userDetails != null) {
            nameText = (accountJson.userDetails.firstName ? accountJson.userDetails.firstName + " " : "")
                + (accountJson.userDetails.lastName ? accountJson.userDetails.lastName + " " : "")
        }
        $("#username-placeholder").text(nameText ? nameText : "@" + accountJson.username);
        if (accountJson.phoneVisible) {
            let phoneText = "Телефон: " + accountJson.userDetails.displayedPhone

            $("#phone-placeholder").text(phoneText)
        }
        if (isMaster(accountJson)) {
            servicesJson = loadMastersServices(accountJson.idAccount);
            fillServiceTable(servicesJson, true);
            fillServicesModal(servicesJson);

            masterAppointmentsJson = loadMastersAppointments(accountJson.idAccount);
            let addressData = accountJson.address != null ? accountJson.address : "";
            $("#address-placeholder").text("Адрес: " + addressData);
            $("#profession-placeholder").text(accountJson.userDetails.about !== null ? accountJson.userDetails.about.profession : "");
            $("#about-text-placeholder").text(accountJson.userDetails.about !== null ? accountJson.userDetails.about.text : "");
            fillWorkingDays(accountJson);
        } else {
            $(".master-only").remove();
            $(".main-information").css("height", "auto");
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
                callMessagePopup("Невозможно загрузить услуги!", "Что-то пошло не так. Невозможно загрузить услуги!")
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
                callMessagePopup("Невозможно загрузить записи!", "Что-то пошло не так. Невозможно загрузить записи!")
            }
        })

        return masterAppointmentsJson;
    }

    $("#make-appointment-btn").on("click", function () {
        masterAppointmentsJson = loadMastersAppointments(accountJson.idAccount);
    })

    $("#services-select").on("click", function (ev) {
        calculateServiceTime();
        let chosenIdService = ev.currentTarget.value;
        let chosenService;
        servicesJson.forEach(function (service) {
            if (service.idService === parseInt(chosenIdService)) {
                chosenService = service
            }
        })
        $("#service-cost-placeholder").text("Стоимость услуги: " + chosenService.price + " р.");
        $("#service-time-placeholder").text("Время услуги: " + chosenService.time);
    })

    $("#working-day-select").on("click", (e) => {
        calculateServiceTime(
            e.target.selectedOptions[0].getAttribute("start-time"),
            e.target.selectedOptions[0].getAttribute("end-time"));
    })

    let prevWorkingDayStart
    let prevWorkingDayEnd

    function calculateServiceTime(workingDayStartTime, workingDayEndTime) {
        if (typeof workingDayStartTime !== "undefined") {
            prevWorkingDayStart = new Date(workingDayStartTime);
        }

        if (typeof workingDayEndTime !== "undefined") {
            prevWorkingDayEnd = new Date(workingDayEndTime);
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

        let chosenDate = new Date($("#working-day-select").val());
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

        masterAppointmentsJson.forEach(function (masterAppointment) {
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
                return;
            }
        })

        let idService = parseInt($("#services-select").val());
        let currentService;

        servicesJson.forEach(function (service) {
            if (service.idService === idService) {
                currentService = service;
            }
        })

        let serviceDurationTime = currentService.time.replace(' минут', '').split(' час');
        let serviceDurationNumber;

        $("#service-cost-placeholder").text("Стоимость услуги: " + currentService.price + " р.");
        $("#service-time-placeholder").text("Время услуги: " + currentService.time);

        if (serviceDurationTime.length === 1) {
            serviceDurationNumber = 1;
        } else {
            serviceDurationTime[1] = serviceDurationTime[1].replace('а', '');

            serviceDurationNumber = serviceDurationTime[0] * 2;
            serviceDurationNumber += serviceDurationTime[1] === '' ? 0 : 1;
        }

        let $workingTimeSelect = $("#working-time-select");
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
}