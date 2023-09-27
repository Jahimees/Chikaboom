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
                callMessagePopup("Невозможно загрузить подтипы услуг!", "Что-то пошло не так. Невозможно загрузить подтипы услуг!")
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

        $("#service-placeholder").html("");

        servicesJson.forEach(function (service) {
            if (!isObjectInSet(service.serviceSubtype.serviceType, serviceTypeSet, "serviceType")) {
                serviceTypeSet.add(service.serviceSubtype.serviceType);
            }
            if (!isObjectInSet(service.serviceSubtype, serviceSubtypeSet, "serviceSubtype")) {
                serviceSubtypeSet.add(service.serviceSubtype);
            }
        })

        serviceTypeSet.forEach(function (serviceType) {
            let serviceBlock = $("#service-placeholder");

            let idServiceType = "service-type-" + serviceType.idServiceType

            let serviceTypeBlock = $("<div class='service-type-block' id='" + idServiceType + "'></div>");
            let serviceTypeBlockHeader = $("<div class='service-type-header medium-text'></div>").text(serviceType.name.toUpperCase());
            let serviceUnderLine = $("<div class='horizontal-blue-line'></div>")
            serviceTypeBlock.append(serviceTypeBlockHeader).append(serviceUnderLine);

            serviceBlock.append(serviceTypeBlock);
        })

        serviceSubtypeSet.forEach(function (serviceSubtype) {
            let serviceType = serviceSubtype.serviceType;

            let serviceTypeTag = $("#service-type-" + serviceType.idServiceType);
            let serviceSubtypeBlockHeader = $("<div class='service-type-header medium-text' id='service-subtype-" +
                serviceSubtype.idServiceSubtype + "'></div>").text(serviceType.name);

            serviceTypeTag.append(serviceSubtypeBlockHeader);
        })

        servicesJson.forEach(function (service) {
            let idServiceSubtype = service.serviceSubtype.idServiceSubtype;
            let serviceSubtypeTag = $("#service-subtype-" + idServiceSubtype)

            let rowTag = $("<div class='service-row row medium-text'></div>")
            let nameTag = $("<div class='col-5'></div>").text(service.name);
            let timeTag = $("<div class='col-3'></div>").text(service.time);
            let priceTag = $("<div class='col-2'></div>").text(service.price + " BYN");

            rowTag.append(nameTag, timeTag, priceTag);

            if (!isAccountPage) {

                let editTag = $("<div class='edit-button col-1' idService='" +
                    service.idService + "' onclick='editService(this.getAttribute(\"idService\"))'></div>")
                let editIcon = $("<img src='/image/icon/edit_icon.svg'>")
                editTag.append(editIcon);

                let deleteTag = $("<div class='edit-button col-1' onclick='callConfirmDeletePopup(" +
                    "this.getAttribute(\"idService\"))' idService='" + service.idService + "'></div>")
                let crossIcon = $("<img src='/image/icon/cross_icon.svg' width='22px'>")
                deleteTag.append(crossIcon);

                rowTag.append(editTag, deleteTag);
            } else {
                let orderTag = $("<div class='purple-button col-2' style='font-size: 20px; padding: 0' " +
                    "data-bs-toggle='modal' data-bs-target='#appointmentModal'></div>").text("Записаться")

                orderTag.onclick = function () {
                    $("#services-select").val(service.idService);
                }
                rowTag.append(orderTag);
            }

            serviceSubtypeTag.append(rowTag);
        })
    }

    function loadServiceTypeSelectOptions() {
        clearServiceTypeOptions();

        let serviceTypes = new Set();
        let serviceTypeSelect = $("#service-type-select");

        serviceSubtypes.forEach(function (serviceSubtype) {
            if (!isObjectInSet(serviceSubtype.serviceType, serviceTypes, "serviceType")) {
                serviceTypes.add(serviceSubtype.serviceType);
            }
        })

        serviceTypes.forEach(function (serviceType) {
            let option = $("<option></option>").val(serviceType.idServiceType).text(serviceType.name);

            serviceTypeSelect.append(option);
        })

        reloadServiceSubtypeSelectOptions();
    }

    function reloadServiceSubtypeSelectOptions() {
        clearServiceSubtypeOptions()

        let serviceSubtypeSet = new Set();
        let $serviceSubtypeSelect = $("#service-subtype-select");

        serviceSubtypes.forEach(function (serviceSubtype) {
            if (serviceSubtype.serviceType.idServiceType === Number($("#service-type-select").val())) {
                serviceSubtypeSet.add(serviceSubtype);
            }
        })

        serviceSubtypeSet.forEach(function (serviceSubtype) {
            let option = $("<option></option>").val(serviceSubtype.idServiceSubtype).text(serviceSubtype.name);

            $serviceSubtypeSelect.append(option);
        })
    }

    function clearServiceTypeOptions() {
        $("#service-type-select").html("");
    }

    function clearServiceSubtypeOptions() {
        $("#service-subtype-select").html("");
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

        $("#service-id-input").val(service.idService);
        $("#service-type-select").val(service.serviceSubtype.serviceType.idServiceType);
        reloadServiceSubtypeSelectOptions();
        $("#service-subtype-select").val(service.serviceSubtype.idServiceSubtype);
        $("#service-name-input").val(service.name);
        $("#service-time-select").val(service.time);
        $("#service-price-input").val(service.price);
    }

    function saveService() {
        let idServiceSubtype = Number($("#service-subtype-select").val());
        let serviceName = $("#service-name-input").val();
        let servicePrice = $("#service-price-input").val();
        let serviceTime = $("#service-time-select").val();
        let idService = $("#service-id-input").val();

        if (serviceName == null || serviceName === "") {
            callMessagePopup("Не все поля заполнены!", "Поле с названием должно быть заполнено!");
            return;
        }

        if (servicePrice == null || servicePrice === "") {
            callMessagePopup("Не все поля заполнены!", "Поле с ценой должно быть заполнено!");
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
                callMessagePopup("Данные сохранены", "Данные успешно сохранены!");
                loadServiceTab(accountJson.idAccount);
            },
            error: function () {
                callMessagePopup("Данные не сохранены", "Произошла ошибка! Данные не были сохранены!")
            }
        })
    }

    function callConfirmDeletePopup(idService) {
        let serviceName = searchService(idService).name;
        callMessagePopup("Удаление", "Вы действительно хотите удалить услугу \"" + serviceName + "\"?")

        $("#decline-message-btn").css("display", "block");
        $("#confirm-message-btn").attr("onclick", "deleteService(" + idService + ")");
    }

    function deleteService(idService) {
        $.ajax({
            type: "DELETE",
            url: "/services/" + idService,
            success: function () {
                callMessagePopup("Услуга удалена!", "Удаление прошло успешно!")
                loadServiceTab(accountJson.idAccount);
            },
            error: function () {
                callMessagePopup("Услуга не удалена!", "Услуга не удалена! Произошла неизвестная ошибка")
            }
        })
    }

    function showEditRow() {
        $("#service-edit").css("display", "flex");
    }

    function clearEditRow() {
        $("#service-id-input").val("");
        $("#service-type-select").val(1);
        reloadServiceSubtypeSelectOptions();
        $("#service-name-input").val("");
        $("#service-time-select").val("30 минут");
        $("#service-price-input").val("");
    }

    function prepareToCreateNewService() {
        clearEditRow();
        showEditRow();
    }

    function hideEditRow() {
        $("#service-edit").css("display", "none");
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
}
