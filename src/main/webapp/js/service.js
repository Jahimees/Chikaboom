function fillServiceTable(userServicesJson, isAccountPage) {
    let servicesSet = new Set();
    let subservicesSet = new Set();

    $("#user-service-placeholder")[0].innerHTML = "";

    //TODO проверка на null
    userServicesJson.forEach(function (userService) {
        if (!isObjectInSet(userService.subservice.service, servicesSet, "service")) {
            servicesSet.add(userService.subservice.service);
        }
        if (!isObjectInSet(userService.subservice, subservicesSet, "subservice")) {
            subservicesSet.add(userService.subservice);
        }
    })

    servicesSet.forEach(function (service) {
        let userServiceBlock = $("#user-service-placeholder")[0];

        let serviceTypeBlock = document.createElement("div");
        serviceTypeBlock.setAttribute("class", "service-type-block");
        serviceTypeBlock.setAttribute("id", "service-" + service.idService);

        let serviceTypeBlockHeader = document.createElement("div");
        serviceTypeBlockHeader.setAttribute("class", "service-header medium-text");
        serviceTypeBlockHeader.innerHTML = service.serviceName.toUpperCase();

        let serviceUnderline = document.createElement("div");
        serviceUnderline.setAttribute("class", "horizontal-blue-line");

        userServiceBlock.appendChild(serviceTypeBlock);
        serviceTypeBlock.appendChild(serviceTypeBlockHeader);
        serviceTypeBlock.appendChild(serviceUnderline);
    })

    subservicesSet.forEach(function (subservice) {
        let service = subservice.service;

        let serviceTag = $("#service-" + service.idService)[0];
        let subserviceTypeBlockHeader = document.createElement("div");
        subserviceTypeBlockHeader.setAttribute("class", "service-header medium-text");
        subserviceTypeBlockHeader.innerHTML = subservice.subserviceName;

        serviceTag.appendChild(subserviceTypeBlockHeader);
    })

    userServicesJson.forEach(function (userService) {
        let idService = userService.subservice.service.idService;
        let serviceTag = $("#service-" + idService)[0];

        let rowTag = document.createElement("div");
        rowTag.setAttribute("class", "service-row row medium-text");

        let nameTag = document.createElement("div");
        nameTag.setAttribute("class", "col-5");
        nameTag.innerHTML = userService.userServiceName;

        let timeTag = document.createElement("div");
        timeTag.setAttribute("class", "col-3");
        timeTag.innerHTML = userService.time;

        let priceTag = document.createElement("div");
        priceTag.setAttribute("class", "col-2");
        priceTag.innerHTML = userService.price + " BYN";

        rowTag.appendChild(nameTag);
        rowTag.appendChild(timeTag);
        rowTag.appendChild(priceTag);

        if (!isAccountPage) {

            let editTag = document.createElement("div");
            let editIcon = document.createElement("img");
            editIcon.setAttribute("src", "/image/icon/edit_icon.svg");

            editTag.setAttribute("class", "edit-button col-1");
            editTag.setAttribute("idUserService", userService.idUserService)
            editTag.setAttribute("onclick", "editUserService(this.getAttribute('idUserService'))");
            editTag.appendChild(editIcon);

            let deleteTag = document.createElement("div");
            let crossIcon = document.createElement("img");
            crossIcon.setAttribute("src", "/image/icon/cross_icon.svg");
            crossIcon.setAttribute("width", "22px");

            deleteTag.setAttribute("idUserService", userService.idUserService);
            deleteTag.setAttribute("class", "edit-button col-1");
            deleteTag.setAttribute("onclick", "callConfirmDeletePopup(this.getAttribute('idUserService'))");
            deleteTag.appendChild(crossIcon);

            rowTag.appendChild(editTag);
            rowTag.appendChild(deleteTag);
        } else {
            let orderTag = document.createElement("div");
            orderTag.setAttribute("class", "purple-button col-2");
            orderTag.setAttribute("style", "font-size: 20px; padding: 0 0;");
            orderTag.setAttribute("data-bs-toggle", "modal");
            orderTag.setAttribute("data-bs-target", "#exampleModal");
            orderTag.onclick = function () {
                $("#services-select")[0].value = userService.idUserService;
            }
            orderTag.innerHTML = "Записаться";

            rowTag.appendChild(orderTag);
        }


        serviceTag.appendChild(rowTag);
    })
}

function loadServiceSelectOptions() {
    clearServiceOptions();

    services = new Set();
    let serviceSelect = $("#service-select")[0];

    subservices.forEach(function (subservice) {
        if (!isObjectInSet(subservice.service, services, "service")) {
            services.add(subservice.service);
        }
    })

    services.forEach(function (service) {
        let option = document.createElement("option");
        option.value = service.idService;
        option.innerHTML = service.serviceName;

        serviceSelect.appendChild(option);
    })

    reloadSubserviceSelectOptions();
}

function reloadSubserviceSelectOptions() {
    clearSubserviceOptions()

    let idService = $("#service-select")[0].value;
    let subserviceSet = new Set();
    let subserviceSelect = $("#subservice-select")[0];

    subservices.forEach(function (subservice) {
        if (subservice.service.idService === Number(idService)) {
            subserviceSet.add(subservice);
        }
    })

    subserviceSet.forEach(function (subservice) {
        let option = document.createElement("option");
        option.value = subservice.idSubservice;
        option.innerHTML = subservice.subserviceName;

        subserviceSelect.appendChild(option);
    })
}

function clearServiceOptions() {
    $("#service-select")[0].innerHTML = "";
}

function clearSubserviceOptions() {
    $("#subservice-select")[0].innerHTML = "";
}

function isObjectInSet(object, objectSet, objectType) {
    let flag = false;

    if (objectType === "service") {

        objectSet.forEach(function (service) {
            if (object.idService == service.idService) {
                flag = true;
            }
        });
    } else if (objectType === "subservice") {

        objectSet.forEach(function (subservice) {
            if (object.idSubservice == subservice.idSubservice) {
                flag = true;
            }
        });
    }

    return flag;
}

function editUserService(idUserService) {
    showEditRow();

    let userService;

    userService = searchUserService(idUserService);

    $("#userservice-id-input")[0].value = userService.idUserService;
    $("#service-select")[0].value = userService.subservice.service.idService;
    reloadSubserviceSelectOptions();
    $("#subservice-select")[0].value = userService.subservice.idSubservice;
    $("#userservice-name-input")[0].value = userService.userServiceName;
    $("#userservice-time-select")[0].value = userService.time;
    $("#userservice-price-input")[0].value = userService.price;
}

function saveUserService() {
    let idSubservice = Number($("#subservice-select")[0].value);
    let userServiceName = $("#userservice-name-input")[0].value;
    let userServicePrice = $("#userservice-price-input")[0].value;
    let userServiceTime = $("#userservice-time-select")[0].value;
    let idUserService = $("#userservice-id-input")[0].value;

    if (userServiceName == null || userServiceName === "") {
        repairDefaultMessagePopup();
        $("#popup-message-text")[0].innerText = "Поле с названием должно быть заполнено!"
        $(".message-popup > .popup-title > #popup-message-header")[0].innerText = "Не все поля заполнены!";
        openPopup('message-popup');
        return;
    }

    if (userServicePrice == null || userServicePrice === "") {
        repairDefaultMessagePopup();
        $("#popup-message-text")[0].innerText = "Поле с ценой должно быть заполнено!"
        $(".message-popup > .popup-title > #popup-message-header")[0].innerText = "Не все поля заполнены!";
        openPopup('message-popup');
        return;
    }

    idUserService = idUserService == null || idUserService === "" ? 0 : idUserService;

    let subserviceToSend;

    subservices.forEach(function (subservice) {
        if (subservice.idSubservice === idSubservice) {
            subserviceToSend = subservice;
        }
    })

    obj = {};
    obj.account = accountJson;
    obj.subservice = subserviceToSend;
    obj.idUserService = idUserService;
    obj.price = userServicePrice;
    obj.time = userServiceTime;
    obj.userServiceName = userServiceName;

    let url = "/chikaboom/personality/" + accountJson.idAccount + "/services"
    repairDefaultMessagePopup();
    $.ajax({
        type: "POST",
        url: url,
        contentType: "application/json",
        dataType: "json",
        data: JSON.stringify(obj),
        success: function () {
            $("#popup-message-text")[0].innerText = "Данные успешно сохранены!"
            $(".message-popup > .popup-title > #popup-message-header")[0].innerText = "Данные сохранены";
            openPopup('message-popup');
            loadServiceTab();
        },
        error: function () {
            $("#popup-message-text")[0].innerText = "Произошла ошибка! Данные не были сохранены!"
            $(".message-popup > .popup-title > #popup-message-header")[0].innerText = "Данные не сохранены";
            openPopup('message-popup');
        }
    })
}

function callConfirmDeletePopup(idUserService) {
    let userServiceName = searchUserService(idUserService).userServiceName;


    repairDefaultMessagePopup();
    $("#decline-message-btn")[0].style.display = "block";
    $("#confirm-message-btn")[0].setAttribute("onclick", "deleteUserService(" + idUserService + ")");

    $("#popup-message-text")[0].innerText = "Вы действительно хотите удалить услугу \"" + userServiceName + "\"?";
    $(".message-popup > .popup-title > #popup-message-header")[0].innerText = "Удаление";
    openPopup("message-popup");

}

function deleteUserService(idUserService) {
    repairDefaultMessagePopup();

    $.ajax({
        type: "DELETE",
        url: "/chikaboom/personality/" + accountJson.idAccount + "/services/" + idUserService,
        success: function () {
            $("#popup-message-text")[0].innerText = "Удаление прошло успешно!";
            $(".message-popup > .popup-title > #popup-message-header")[0].innerText = "Услуга удалена!";
            openPopup("message-popup");
            loadServiceTab();
        },
        error: function () {
            $("#popup-message-text")[0].innerText = "Услуга не удалена! Произошла неизвестная ошибка"
            $(".message-popup > .popup-title > #popup-message-header")[0].innerText = "Услуга не удалена!";
            openPopup("message-popup");
        }
    })
}

function showEditRow() {
    $("#user-service-edit")[0].style.display = "flex"
}

function clearEditRow() {
    $("#userservice-id-input")[0].value = "";
    $("#service-select")[0].value = 1;
    reloadSubserviceSelectOptions();
    $("#userservice-name-input")[0].value = "";
    $("#userservice-time-select")[0].value = "30 минут";
    $("#userservice-price-input")[0].value = "";
}

function prepareToCreateNewService() {
    clearEditRow();
    showEditRow();
}

function hideEditRow() {
    $("#user-service-edit")[0].style.display = "none"
}

function searchUserService(idUserService) {
    let result;
    userServicesJson.forEach(function (userService) {
        if (userService.idUserService === Number(idUserService)) {
            result = userService

        }
    })

    return result;
}


// Модальное окно записи

function fillUserServicesModal(userServicesJson) {
    let servicesSelect = $("#services-select")[0];

    userServicesJson.forEach(function (userService) {
        let option = document.createElement("option");
        option.value = userService.idUserService;
        option.innerHTML = userService.userServiceName;

        servicesSelect.appendChild(option);
    })
}

function fillWorkingDays() {
    let workingDaySelect = $("#working-day-select")[0];

    let workingDays = JSON.parse(accountJson.workingDays.workingDays);

    workingDays.forEach(function (workingDay) {
        let workingDayObj = new Date(workingDay);
        let option = document.createElement("option");
        option.value = workingDay;
        option.innerHTML = workingDayObj.getDate() + "." + (1 + workingDayObj.getMonth()) + "." + workingDayObj.getFullYear();

        workingDaySelect.appendChild(option);
    })
}

$("#services-select").on("click", function (ev) {
    calculateServiceTime();
    let chosenIdUserService = ev.currentTarget.value;
    let chosenUserService;
    userServicesJson.forEach(function (userService) {
        if (userService.idUserService === parseInt(chosenIdUserService)) {
            chosenUserService = userService
        }
    })
    $("#service-cost-placeholder")[0].innerHTML = "Стоимость услуги: " + chosenUserService.price + " р.";
    $("#service-time-placeholder")[0].innerHTML = "Время услуги: " + chosenUserService.time;
})

$("#working-day-select").on("click", function () {
    calculateServiceTime();
})

function calculateServiceTime() {
    let workingDayStart = new Date();
    let workingDayEnd = new Date();

    if (accountJson.workingDays.workingDayStart.toString().length === 3) {
        workingDayStart.setHours(accountJson.workingDays.workingDayStart.toString()[0]);
        workingDayStart.setMinutes(accountJson.workingDays.workingDayStart.toString().substring(1, 3));
    } else {
        workingDayStart.setHours(accountJson.workingDays.workingDayStart.toString().substring(0, 2));
        workingDayStart.setMinutes(accountJson.workingDays.workingDayStart.toString().substring(2, 4));
    }

    if (accountJson.workingDays.workingDayEnd.toString().length === 3) {
        workingDayEnd.setHours(accountJson.workingDays.workingDayEnd.toString()[0]);
        workingDayEnd.setMinutes(accountJson.workingDays.workingDayEnd.toString().substring(1, 3));
    } else {
        workingDayEnd.setHours(accountJson.workingDays.workingDayEnd.toString().substring(0, 2));
        workingDayEnd.setMinutes(accountJson.workingDays.workingDayEnd.toString().substring(2, 4));
    }

    let workingCellsCount = (workingDayEnd.getHours() - workingDayStart.getHours()) * 2;

    if (workingDayEnd.getMinutes() > workingDayStart.getMinutes()) {
        workingCellsCount++;
    } else if (workingDayEnd.getMinutes() < workingDayStart.getMinutes()) {
        workingCellsCount--;
    }

    let workingCells = [];
    let iterator = new Date();
    iterator.setHours(workingDayStart.getHours());
    iterator.setMinutes(workingDayStart.getMinutes());

    for (let i = 0; i < workingCellsCount; i++) {
        workingCells[i] = {
            time: iterator.getHours() + ":" + (iterator.getMinutes() === 0 ? '00' : iterator.getMinutes()),
            value: true
        };

        iterator.setMinutes(iterator.getMinutes() + 30);
    }

    let chosenDate = $("#working-day-select")[0].value;

    masterAppointmentsJson.forEach(function (masterAppointment) {
        if (masterAppointment.appointmentDate === chosenDate) {
            let trueTimer = 0;

            for (let i = 0; i < workingCellsCount; i++) {
                if (trueTimer > 0) {
                    workingCells[i].value = false;
                    trueTimer--;
                }
                if (masterAppointment.appointmentTime === workingCells[i].time) {
                    workingCells[i].value = false;

                    let userServiceTime = masterAppointment.userService.time;
                    let userServiceDurationTime = userServiceTime.replace(' минут', '').split(' час');

                    if (userServiceDurationTime.length === 1) {
                        trueTimer = 1;
                    } else {
                        userServiceDurationTime[1] = userServiceDurationTime[1].replace('а', '');

                        trueTimer = userServiceDurationTime[0] * 2;
                        trueTimer += userServiceDurationTime[1] === '' ? 0 : 1;
                    }

                    trueTimer--;
                }
            }
        }
    })

    let idUserService = parseInt($("#services-select")[0].value);
    let currentUserService;

    userServicesJson.forEach(function (userService) {
        if (userService.idUserService === idUserService) {
            currentUserService = userService;
        }
    })

    let userServiceDurationTime = currentUserService.time.replace(' минут', '').split(' час');
    let userServiceDurationNumber;

    if (userServiceDurationTime.length === 1) {
        userServiceDurationNumber = 1;
    } else {
        userServiceDurationTime[1] = userServiceDurationTime[1].replace('а', '');

        userServiceDurationNumber = userServiceDurationTime[0] * 2;
        userServiceDurationNumber += userServiceDurationTime[1] === '' ? 0 : 1;
    }

    for (let i = workingCells.length - 1; i > workingCells.length - userServiceDurationNumber; i--) {
        workingCells[i].value = false;
    }

    let workingTimeSelect = $("#working-time-select")[0];
    workingTimeSelect.innerHTML = "";

    let timePosition = -1;
    let durationCounter = 0;
    for (let i = 0; i < workingCells.length; i++) {
        if (workingCells[i].value) {
            durationCounter++;
            if (durationCounter === 1) {
                timePosition = i;
            }
            if (durationCounter === userServiceDurationNumber) {
                let option = document.createElement("option");
                option.value = workingCells[timePosition].time;
                option.innerHTML = workingCells[timePosition].time;

                workingTimeSelect.appendChild(option);
                durationCounter = 0;
                i = timePosition;
                timePosition = -1;
            }
        } else {
            durationCounter = 0;
            if (timePosition !== -1) {
                i = timePosition;
            }
            timePosition = -1;
        }
    }
}

function loadMasterAppointments(idAccount) {

    $.ajax({
        type: "get",
        url: "/chikaboom/appointment/" + idAccount,
        contentType: "application/json",
        dataType: "json",
        data: {},
        success: function (data) {
            masterAppointmentsJson = data;
        },
        error: function () {
            //TODO ERROR
        }
    })
}

function loadUserServices(idAccount) {
    $.ajax({
        type: "get",
        url: "/chikaboom/personality/" + idAccount + "/services/info",
        contentType: "application/json",
        dataType: "json",
        data: {},
        success: function (data) {
            userServicesJson = data;
            fillServiceTable(userServicesJson, true);
            fillUserServicesModal(userServicesJson);
            fillWorkingDays();
        },
        error: function () {
            //TODO ERROR
        }
    });
}