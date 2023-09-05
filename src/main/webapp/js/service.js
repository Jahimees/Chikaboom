{
    let servicesJson;
    let serviceSubtypes;

    function loadConcreteServiceTab(tabName, idAccount) {
        $.ajax({
            type: "get",
            url: "/chikaboom/personality/" + idAccount + "/services/" + tabName,
            contentType: "application/text",
            dataType: "text",
            success: function (data) {
                setCurrentTabName(tabName);
                loadServiceSubtypes();
                $("#service-type-content-placeholder").html(data);
            },
            error: function () {
                loadUnderConstruction();
            }
        });
    }

    function loadServiceSubtypes() {
        $.ajax({
            contentType: "application/json",
            dataType: "json",
            method: "get",
            url: "/service-subtypes",
            async: false,
            success: function (data) {
                serviceSubtypes = data;
            },
            error: function () {
                repairDefaultMessagePopup();
                $("#popup-message-text")[0].innerText = "Что-то пошло не так. Невозможно загрузить подтипы услуг!"
                $(".message-popup > .popup-title > #popup-message-header")[0].innerText = "Невозможно загрузить подтипы услуг!";
                openPopup('message-popup');
            }
        })
    }

    function loadServiceTab(idAccount) {
        $.ajax({
            type: "get",
            url: "/accounts/" + idAccount + "/services",
            contentType: "application/text",
            dataType: "text",
            success: function (data) {
                servicesJson = JSON.parse(data);
                fillServiceTable(servicesJson);
                loadServiceTypeSelectOptions();
            },
            error: function () {
                loadUnderConstruction();
            }
        });
    }

    function fillServiceTable(servicesJson, isAccountPage) {
        if (servicesJson == null) {
            return;
        }

        let serviceTypeSet = new Set();
        let serviceSubtypeSet = new Set();

        $("#service-placeholder")[0].innerHTML = "";

        servicesJson.forEach(function (service) {
            if (!isObjectInSet(service.serviceSubtype.serviceType, serviceTypeSet, "serviceType")) {
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
                orderTag.setAttribute("data-bs-target", "#appointmentModal");
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

        let serviceTypes = new Set();
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
        let serviceName = $("#service-name-input").val();
        let servicePrice = $("#service-price-input").val();
        let serviceTime = $("#service-time-select").val();
        let idService = $("#service-id-input").val();

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

        let serviceSubtypeToSend;

        serviceSubtypes.forEach(function (serviceSubtype) {
            if (serviceSubtype.idServiceSubtype === idServiceSubtype) {
                serviceSubtypeToSend = serviceSubtype;
            }
        })

        obj = {
            account: accountJson,
            serviceSubtype: serviceSubtypeToSend,
            price: servicePrice,
            time: serviceTime,
            name: serviceName
        };

        let method;
        let url = "/services";
        if (idService != null && idService !== "") {
            method = "PUT";
            url += "/" + idService;
        } else {
            method = "POST";
        }

        repairDefaultMessagePopup();
        $.ajax({
            method: method,
            url: url,
            contentType: "application/json",
            dataType: "json",
            data: JSON.stringify(obj),
            success: function () {
                $("#popup-message-text")[0].innerText = "Данные успешно сохранены!"
                $(".message-popup > .popup-title > #popup-message-header")[0].innerText = "Данные сохранены";
                openPopup('message-popup');
                loadServiceTab(accountJson.idAccount);
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
            url: "/services/" + idService,
            success: function () {
                $("#popup-message-text")[0].innerText = "Удаление прошло успешно!";
                $(".message-popup > .popup-title > #popup-message-header")[0].innerText = "Услуга удалена!";
                openPopup("message-popup");
                loadServiceTab(accountJson.idAccount);
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

    function fillWorkingDays(accountJson) {
        let workingDaySelect = $("#working-day-select")[0];

        let workingDays = JSON.parse(accountJson.workingDays.workingDays);

        workingDays.forEach(function (workingDay) {
            let today = new Date();
            let workingDayObj = new Date(workingDay);
            if ((today.getFullYear() < workingDayObj.getFullYear())
                || (today.getFullYear() === workingDayObj.getFullYear()
                    && today.getMonth() < workingDayObj.getMonth())
                || (today.getFullYear() === workingDayObj.getFullYear()
                    && today.getMonth() === workingDayObj.getMonth()
                    && today.getDate() <= workingDayObj.getDate())) {

                let option = document.createElement("option");
                option.value = workingDay;
                option.innerHTML = workingDayObj.getDate() + "." + (1 + workingDayObj.getMonth()) + "." + workingDayObj.getFullYear();

                workingDaySelect.appendChild(option);
            }
        });
    }


}