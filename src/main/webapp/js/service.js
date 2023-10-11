{
    let servicesJson;
    let serviceSubtypesFacade;

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
                serviceSubtypesFacade = data;
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

    function fillServiceTable(servicesFacadeJson, isAccountPage) {
        if (servicesFacadeJson == null) {
            return;
        }

        let serviceTypeFacadeSet = new Set();
        let serviceSubtypeFacadeSet = new Set();

        $("#service-placeholder").html("");

        servicesFacadeJson.forEach(function (serviceFacade) {
            if (!isObjectInSet(serviceFacade.serviceSubtypeFacade.serviceTypeFacade,
                serviceTypeFacadeSet, "serviceTypeFacade")) {
                serviceTypeFacadeSet.add(serviceFacade.serviceSubtypeFacade.serviceTypeFacade);
            }
            if (!isObjectInSet(serviceFacade.serviceSubtypeFacade,
                serviceSubtypeFacadeSet, "serviceSubtypeFacade")) {
                serviceSubtypeFacadeSet.add(serviceFacade.serviceSubtypeFacade);
            }
        })

        serviceTypeFacadeSet.forEach(function (serviceTypeFacade) {
            let serviceBlock = $("#service-placeholder");

            let idServiceType = "service-type-" + serviceTypeFacade.idServiceType

            let serviceTypeBlock = $("<div class='service-type-block' id='" + idServiceType + "'></div>");
            let serviceTypeBlockHeader = $("<div class='service-type-header medium-text'></div>").text(serviceTypeFacade.name.toUpperCase());
            let serviceUnderLine = $("<div class='horizontal-blue-line'></div>")
            serviceTypeBlock.append(serviceTypeBlockHeader).append(serviceUnderLine);

            serviceBlock.append(serviceTypeBlock);
        })

        serviceSubtypeFacadeSet.forEach(function (serviceSubtypeFacade) {
            let serviceTypeFacade = serviceSubtypeFacade.serviceTypeFacade;

            let serviceTypeTag = $("#service-type-" + serviceTypeFacade.idServiceType);
            let serviceSubtypeBlockHeader = $("<div class='service-type-header medium-text' id='service-subtype-" +
                serviceSubtypeFacade.idServiceSubtype + "'></div>").text(serviceTypeFacade.name);

            serviceTypeTag.append(serviceSubtypeBlockHeader);
        })

        servicesFacadeJson.forEach(function (serviceFacade) {
            let idServiceSubtype = serviceFacade.serviceSubtypeFacade.idServiceSubtype;
            let serviceSubtypeTag = $("#service-subtype-" + idServiceSubtype)

            let rowTag = $("<div class='service-row row medium-text'></div>")
            let nameTag = $("<div class='col-5'></div>").text(serviceFacade.name);
            let timeTag = $("<div class='col-3'></div>").text(serviceFacade.time);
            let priceTag = $("<div class='col-2'></div>").text(serviceFacade.price + " BYN");

            rowTag.append(nameTag, timeTag, priceTag);

            if (!isAccountPage) {

                let editTag = $("<div class='edit-button col-1' idService='" +
                    serviceFacade.idService + "' onclick='editService(this.getAttribute(\"idService\"))'></div>")
                let editIcon = $("<img src='/image/icon/edit_icon.svg'>")
                editTag.append(editIcon);

                let deleteTag = $("<div class='edit-button col-1' onclick='callConfirmDeletePopup(" +
                    "this.getAttribute(\"idService\"))' idService='" + serviceFacade.idService + "'></div>")
                let crossIcon = $("<img src='/image/icon/cross_icon.svg' width='22px'>")
                deleteTag.append(crossIcon);

                rowTag.append(editTag, deleteTag);
            } else {
                let orderTag = $("<div class='purple-button col-2' style='font-size: 20px; padding: 0' " +
                    "data-bs-toggle='modal' data-bs-target='#appointmentModal'></div>").text("Записаться")

                orderTag.onclick = function () {
                    $(".services-select").val(serviceFacade.idService);
                }
                rowTag.append(orderTag);
            }

            serviceSubtypeTag.append(rowTag);
        })
    }

    function loadServiceTypeSelectOptions() {
        clearServiceTypeOptions();

        let serviceTypesFacade = new Set();
        let serviceTypeSelect = $("#service-type-select");

        serviceSubtypesFacade.forEach(function (serviceSubtypeFacade) {
            if (!isObjectInSet(serviceSubtypeFacade.serviceTypeFacade, serviceTypesFacade, "serviceTypeFacade")) {
                serviceTypesFacade.add(serviceSubtypeFacade.serviceTypeFacade);
            }
        })

        serviceTypesFacade.forEach(function (serviceTypeFacade) {
            let option = $("<option></option>").val(serviceTypeFacade.idServiceType).text(serviceTypeFacade.name);

            serviceTypeSelect.append(option);
        })

        reloadServiceSubtypeSelectOptions();
    }

    function reloadServiceSubtypeSelectOptions() {
        clearServiceSubtypeOptions()

        let serviceSubtypeFacadeSet = new Set();
        let $serviceSubtypeSelect = $("#service-subtype-select");

        serviceSubtypesFacade.forEach(function (serviceSubtypeFacade) {
            if (serviceSubtypeFacade.serviceTypeFacade.idServiceType === Number($("#service-type-select").val())) {
                serviceSubtypeFacadeSet.add(serviceSubtypeFacade);
            }
        })

        serviceSubtypeFacadeSet.forEach(function (serviceSubtypeFacade) {
            let option = $("<option></option>").val(serviceSubtypeFacade.idServiceSubtype).text(serviceSubtypeFacade.name);

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

        if (objectType === "serviceTypeFacade") {
            objectSet.forEach(function (serviceTypeFacade) {
                if (object.idServiceType == serviceTypeFacade.idServiceType) {
                    flag = true;
                }
            });
        } else if (objectType === "serviceSubtypeFacade") {

            objectSet.forEach(function (serviceSubtypeFacade) {
                if (object.idServiceSubtype == serviceSubtypeFacade.idServiceSubtype) {
                    flag = true;
                }
            });
        }

        return flag;
    }

    function editService(idService) {
        showEditRow();

        let serviceFacade = searchService(idService);

        $("#service-id-input").val(serviceFacade.idService);
        $("#service-type-select").val(serviceFacade.serviceSubtypeFacade.serviceTypeFacade.idServiceType);
        reloadServiceSubtypeSelectOptions();
        $("#service-subtype-select").val(serviceFacade.serviceSubtypeFacade.idServiceSubtype);
        $("#service-name-input").val(serviceFacade.name);
        $("#service-time-select").val(serviceFacade.time);
        $("#service-price-input").val(serviceFacade.price);
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

        serviceSubtypesFacade.forEach(function (serviceSubtypeFacade) {
            if (serviceSubtypeFacade.idServiceSubtype === idServiceSubtype) {
                serviceSubtypeToSend = serviceSubtypeFacade;
            }
        })

        let serviceFacadeToSend = {
            accountFacade: accountFacadeJson,
            serviceSubtypeFacade: serviceSubtypeToSend,
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
            data: JSON.stringify(serviceFacadeToSend),
            success: function () {
                callMessagePopup("Данные сохранены", "Данные успешно сохранены!");
                loadServiceTab(accountFacadeJson.idAccount);
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
                loadServiceTab(accountFacadeJson.idAccount);
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
        let servicesFacadeJson;

        $.ajax({
            contentType: "application/json",
            dataType: "json",
            method: "get",
            async: false,
            url: "/accounts/" + idAccount + "/services",
            success: function (data) {
                servicesFacadeJson = data;
            },
            error: function () {
                callMessagePopup("Невозможно загрузить услуги!", "Что-то пошло не так. Невозможно загрузить услуги!")
            }
        });

        return servicesFacadeJson;
    }
}
