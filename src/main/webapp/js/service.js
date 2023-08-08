function fillServiceTable(servicesJson, isAccountPage) {
    let serviceTypeSet = new Set();
    let serviceSubtypeSet = new Set();

    $("#service-placeholder")[0].innerHTML = "";

    //TODO проверка на null
    servicesJson.forEach(function (service) {
        if (!isObjectInSet(service.serviceSubtype.serviceType, serviceTypeSet, "service")) {
            serviceTypeSet.add(service.serviceSubtype.serviceType);
        }
        if (!isObjectInSet(service.serviceSubtype, serviceSubtypeSet, "serviceSubtype")) {
            serviceSubtypeSet.add(service.serviceSubtype);
        }
    })

    serviceTypeSet.forEach(function (serviceType) {
        let serviceBlock = $("#service-placeholder")[0];

        let serviceTypeBlock = document.createElement("div");
        serviceTypeBlock.setAttribute("class", "service-type-block");
        serviceTypeBlock.setAttribute("id", "service-type-" + serviceType.idServiceType);

        let serviceTypeBlockHeader = document.createElement("div");
        serviceTypeBlockHeader.setAttribute("class", "service-type-header medium-text");
        serviceTypeBlockHeader.innerHTML = serviceType.name.toUpperCase();

        let serviceUnderline = document.createElement("div");
        serviceUnderline.setAttribute("class", "horizontal-blue-line");

        serviceBlock.appendChild(serviceTypeBlock);
        serviceTypeBlock.appendChild(serviceTypeBlockHeader);
        serviceTypeBlock.appendChild(serviceUnderline);
    })

    serviceSubtypeSet.forEach(function (serviceSubtype) {
        let serviceType = serviceSubtype.serviceType;

        let serviceTypeTag = $("#service-type-" + serviceType.idServiceType)[0];
        let serviceSubtypeBlockHeader = document.createElement("div");
        serviceSubtypeBlockHeader.setAttribute("id", "service-subtype-" + serviceSubtype.idServiceSubtype);
        serviceSubtypeBlockHeader.setAttribute("class", "service-type-header medium-text");
        serviceSubtypeBlockHeader.innerHTML = serviceSubtype.name;

        serviceTypeTag.appendChild(serviceSubtypeBlockHeader);
    })

    servicesJson.forEach(function (service) {
        let idServiceSubtype = service.serviceSubtype.idServiceSubtype;
        let serviceSubtypeTag = $("#service-subtype-" + idServiceSubtype)[0];

        let rowTag = document.createElement("div");
        rowTag.setAttribute("class", "service-row row medium-text");

        let nameTag = document.createElement("div");
        nameTag.setAttribute("class", "col-5");
        nameTag.innerHTML = service.name;

        let timeTag = document.createElement("div");
        timeTag.setAttribute("class", "col-3");
        timeTag.innerHTML = service.time;

        let priceTag = document.createElement("div");
        priceTag.setAttribute("class", "col-2");
        priceTag.innerHTML = service.price + " BYN";

        rowTag.appendChild(nameTag);
        rowTag.appendChild(timeTag);
        rowTag.appendChild(priceTag);

        if (!isAccountPage) {

            let editTag = document.createElement("div");
            let editIcon = document.createElement("img");
            editIcon.setAttribute("src", "/image/icon/edit_icon.svg");

            editTag.setAttribute("class", "edit-button col-1");
            editTag.setAttribute("idService", service.idService)
            editTag.setAttribute("onclick", "editService(this.getAttribute('idService'))");
            editTag.appendChild(editIcon);

            let deleteTag = document.createElement("div");
            let crossIcon = document.createElement("img");
            crossIcon.setAttribute("src", "/image/icon/cross_icon.svg");
            crossIcon.setAttribute("width", "22px");

            deleteTag.setAttribute("idService", service.idService);
            deleteTag.setAttribute("class", "edit-button col-1");
            deleteTag.setAttribute("onclick", "callConfirmDeletePopup(this.getAttribute('idService'))");
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
                $("#services-select")[0].value = service.idService;
            }
            orderTag.innerHTML = "Записаться";

            rowTag.appendChild(orderTag);
        }


        serviceSubtypeTag.appendChild(rowTag);
    })
}

function loadServiceTypeSelectOptions() {
    clearServiceTypeOptions();

    serviceTypes = new Set();
    let serviceTypeSelect = $("#service-type-select")[0];

    serviceSubtypes.forEach(function (serviceSubtype) {
        if (!isObjectInSet(serviceSubtype.serviceType, serviceTypes, "serviceType")) {
            serviceTypes.add(serviceSubtype.serviceType);
        }
    })

    serviceTypes.forEach(function (serviceType) {
        let option = document.createElement("option");
        option.value = serviceType.idServiceType;
        option.innerHTML = serviceType.name;

        serviceTypeSelect.appendChild(option);
    })

    reloadServiceSubtypeSelectOptions();
}

function reloadServiceSubtypeSelectOptions() {
    clearServiceSubtypeOptions()

    let idServiceType = $("#service-type-select")[0].value;
    let serviceSubtypeSet = new Set();
    let serviceSubtypeSelect = $("#service-subtype-select")[0];

    serviceSubtypes.forEach(function (serviceSubtype) {
        if (serviceSubtype.serviceType.idServiceType === Number(idServiceType)) {
            serviceSubtypeSet.add(serviceSubtype);
        }
    })

    serviceSubtypeSet.forEach(function (serviceSubtype) {
        let option = document.createElement("option");
        option.value = serviceSubtype.idServiceSubtype;
        option.innerHTML = serviceSubtype.name;

        serviceSubtypeSelect.appendChild(option);
    })
}

function clearServiceTypeOptions() {
    $("#service-type-select")[0].innerHTML = "";
}

function clearServiceSubtypeOptions() {
    $("#service-subtype-select")[0].innerHTML = "";
}

function isObjectInSet(object, objectSet, objectType) {
    let flag = false;

    if (objectType === "serviceType") {

        objectSet.forEach(function (serviceType) {
            if (object.idServiceType == serviceType.idServiceType) {
                flag = true;
            }
        });
    } else if (objectType === "serviceSubtype") {

        objectSet.forEach(function (serviceSubtype) {
            if (object.idServiceSubtype == serviceSubtype.idServiceSubtype) {
                flag = true;
            }
        });
    }

    return flag;
}

function editService(idService) {
    showEditRow();

    let service;

    service = searchService(idService);

    $("#service-id-input")[0].value = service.idService;
    $("#service-type-select")[0].value = service.serviceSubtype.serviceType.idServiceType;
    reloadServiceSubtypeSelectOptions();
    $("#service-subtype-select")[0].value = service.serviceSubtype.idServiceSubtype;
    $("#service-name-input")[0].value = service.name;
    $("#service-time-select")[0].value = service.time;
    $("#service-price-input")[0].value = service.price;
}

function saveService() {
    let idServiceSubtype = Number($("#service-subtype-select")[0].value);
    let serviceName = $("#service-name-input")[0].value;
    let servicePrice = $("#service-price-input")[0].value;
    let serviceTime = $("#service-time-select")[0].value;
    let idService = $("#service-id-input")[0].value;

    if (serviceName == null || serviceName === "") {
        repairDefaultMessagePopup();
        $("#popup-message-text")[0].innerText = "Поле с названием должно быть заполнено!"
        $(".message-popup > .popup-title > #popup-message-header")[0].innerText = "Не все поля заполнены!";
        openPopup('message-popup');
        return;
    }

    if (servicePrice == null || servicePrice === "") {
        repairDefaultMessagePopup();
        $("#popup-message-text")[0].innerText = "Поле с ценой должно быть заполнено!"
        $(".message-popup > .popup-title > #popup-message-header")[0].innerText = "Не все поля заполнены!";
        openPopup('message-popup');
        return;
    }

    idService = idService == null || idService === "" ? 0 : idService;

    let serviceSubtypeToSend;

    serviceSubtypes.forEach(function (serviceSubtype) {
        if (serviceSubtype.idServiceSubtype === idServiceSubtype) {
            serviceSubtypeToSend = serviceSubtype;
        }
    })

    obj = {};
    obj.account = accountJson;
    obj.serviceSubtype = serviceSubtypeToSend;
    obj.idService = idService;
    obj.price = servicePrice;
    obj.time = serviceTime;
    obj.name = serviceName;

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

function callConfirmDeletePopup(idService) {
    let serviceName = searchService(idService).name;


    repairDefaultMessagePopup();
    $("#decline-message-btn")[0].style.display = "block";
    $("#confirm-message-btn")[0].setAttribute("onclick", "deleteService(" + idService + ")");

    $("#popup-message-text")[0].innerText = "Вы действительно хотите удалить услугу \"" + serviceName + "\"?";
    $(".message-popup > .popup-title > #popup-message-header")[0].innerText = "Удаление";
    openPopup("message-popup");

}

function deleteService(idService) {
    repairDefaultMessagePopup();

    $.ajax({
        type: "DELETE",
        url: "/chikaboom/personality/" + accountJson.idAccount + "/services/" + idService,
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
    $("#service-edit")[0].style.display = "flex"
}

function clearEditRow() {
    $("#service-id-input")[0].value = "";
    $("#service-type-select")[0].value = 1;
    reloadServiceSubtypeSelectOptions();
    $("#service-name-input")[0].value = "";
    $("#service-time-select")[0].value = "30 минут";
    $("#service-price-input")[0].value = "";
}

function prepareToCreateNewService() {
    clearEditRow();
    showEditRow();
}

function hideEditRow() {
    $("#service-edit")[0].style.display = "none"
}

function searchService(idService) {
    let result;
    servicesJson.forEach(function (service) {
        if (service.idService === Number(idService)) {
            result = service
        }
    })

    return result;
}


// Модальное окно записи
function fillServicesModal(servicesJson) {
    let servicesSelect = $("#services-select")[0];

    servicesJson.forEach(function (service) {
        let option = document.createElement("option");
        option.value = service.idService;
        option.innerHTML = service.name;

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
    let chosenIdService = ev.currentTarget.value;
    let chosenService;
    servicesJson.forEach(function (service) {
        if (service.idService === parseInt(chosenIdService)) {
            chosenService = service
        }
    })
    $("#service-cost-placeholder")[0].innerHTML = "Стоимость услуги: " + chosenService.price + " р.";
    $("#service-time-placeholder")[0].innerHTML = "Время услуги: " + chosenService.time;
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

    let idService = parseInt($("#services-select")[0].value);
    let currentService;

    servicesJson.forEach(function (service) {
        if (service.idService === idService) {
            currentService = service;
        }
    })

    let serviceDurationTime = currentService.time.replace(' минут', '').split(' час');
    let serviceDurationNumber;

    if (serviceDurationTime.length === 1) {
        serviceDurationNumber = 1;
    } else {
        serviceDurationTime[1] = serviceDurationTime[1].replace('а', '');

        serviceDurationNumber = serviceDurationTime[0] * 2;
        serviceDurationNumber += serviceDurationTime[1] === '' ? 0 : 1;
    }

    for (let i = workingCells.length - 1; i > workingCells.length - serviceDurationNumber; i--) {
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
            if (durationCounter === serviceDurationNumber) {
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

function loadServices(idAccount) {
    $.ajax({
        type: "get",
        url: "/chikaboom/personality/" + idAccount + "/services/info",
        contentType: "application/json",
        dataType: "json",
        data: {},
        success: function (data) {
            servicesJson = data;
            fillServiceTable(servicesJson, true);
            fillServicesModal(servicesJson);
            fillWorkingDays();
        },
        error: function () {
            //TODO ERROR
        }
    });
}