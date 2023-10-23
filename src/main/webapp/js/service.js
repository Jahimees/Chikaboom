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
        if (typeof serviceSubtypesFacade !== "undefined") {
            return serviceSubtypesFacade;
        }
        $.ajax({
            contentType: "application/json",
            dataType: "json",
            method: "get",
            url: "/service-subtypes",
            async: false,
            success: function (data) {
                console.log("Endpoint 15 done::: ");
                serviceSubtypesFacade = data;
            },
            error: function () {
                callMessagePopup("Невозможно загрузить подтипы услуг!", "Что-то пошло не так. Невозможно загрузить подтипы услуг!")
            }
        })
    }

    function loadServiceData(idAccount, reload) {
        if (typeof servicesJson === "undefined" || reload) {
            reloadServiceData(idAccount);
        }
    }

    function reloadServiceData(idAccount) {
        $.ajax({
            type: "get",
            url: "/accounts/" + idAccount + "/services",
            contentType: "application/text",
            dataType: "text",
            async: false,
            success: function (data) {
                console.log("Endpoint 16 done::: ");
                servicesJson = JSON.parse(data);
            },
            error: function () {
                loadUnderConstruction();
            }
        });
    }

    function loadServiceTab(idAccount, reload) {
        loadServiceData(idAccount, reload)
        fillServiceTable(servicesJson);
        loadServiceTypeSelectOptions();
    }

    function fillServiceTable(servicesFacadeJson, isAccountPage) {
        if (servicesFacadeJson == null) {
            return;
        }

        const serviceTypeFacadeSet = new Set();
        const serviceSubtypeFacadeSet = new Set();

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
            const serviceBlock = $("#service-placeholder");

            const idServiceType = "service-type-" + serviceTypeFacade.idServiceType

            const serviceTypeBlock = $("<div class='service-type-block' id='" + idServiceType + "'></div>");
            const serviceTypeBlockHeader = $("<div class='service-type-header medium-text'></div>").text(serviceTypeFacade.name.toUpperCase());
            const serviceUnderLine = $("<div class='horizontal-blue-line'></div>")
            serviceTypeBlock.append(serviceTypeBlockHeader).append(serviceUnderLine);

            serviceBlock.append(serviceTypeBlock);
        })

        serviceSubtypeFacadeSet.forEach(function (serviceSubtypeFacade) {
            const serviceTypeFacade = serviceSubtypeFacade.serviceTypeFacade;

            const serviceTypeTag = $("#service-type-" + serviceTypeFacade.idServiceType);
            const serviceSubtypeBlockHeader = $("<div class='service-type-header medium-text' id='service-subtype-" +
                serviceSubtypeFacade.idServiceSubtype + "'></div>").text(serviceTypeFacade.name);

            serviceTypeTag.append(serviceSubtypeBlockHeader);
        })

        servicesFacadeJson.forEach(function (serviceFacade) {
            const idServiceSubtype = serviceFacade.serviceSubtypeFacade.idServiceSubtype;
            const serviceSubtypeTag = $("#service-subtype-" + idServiceSubtype)

            const rowTag = $("<div class='service-row row medium-text'></div>")
            const nameTag = $("<div class='col-5'></div>").text(serviceFacade.name);
            const timeTag = $("<div class='col-3'></div>").text(serviceFacade.time);
            const priceTag = $("<div class='col-2'></div>").text(serviceFacade.price + " BYN");

            rowTag.append(nameTag, timeTag, priceTag);

            if (!isAccountPage) {

                const editTag = $("<div class='edit-button col-1' idService='" +
                    serviceFacade.idService + "' onclick='editService(this.getAttribute(\"idService\"))'></div>")
                const editIcon = $("<img src='/image/icon/edit_icon.svg'>")
                editTag.append(editIcon);

                const deleteTag = $("<div class='edit-button col-1' onclick='callConfirmDeletePopup(" +
                    "this.getAttribute(\"idService\"))' idService='" + serviceFacade.idService + "'></div>")
                const crossIcon = $("<img src='/image/icon/cross_icon.svg' width='22px'>")
                deleteTag.append(crossIcon);

                rowTag.append(editTag, deleteTag);
            } else {
                const orderTag = $("<div class='purple-button col-2' style='font-size: 20px; padding: 0' " +
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

        const serviceTypesFacade = new Set();
        const $serviceTypeSelect = $("#service-type-select");

        serviceSubtypesFacade.forEach(function (serviceSubtypeFacade) {
            if (!isObjectInSet(serviceSubtypeFacade.serviceTypeFacade, serviceTypesFacade, "serviceTypeFacade")) {
                serviceTypesFacade.add(serviceSubtypeFacade.serviceTypeFacade);
            }
        })

        serviceTypesFacade.forEach(function (serviceTypeFacade) {
            const option = $("<option></option>").val(serviceTypeFacade.idServiceType).text(serviceTypeFacade.name);

            $serviceTypeSelect.append(option);
        })

        reloadServiceSubtypeSelectOptions();
    }

    function reloadServiceSubtypeSelectOptions() {
        clearServiceSubtypeOptions()

        const serviceSubtypeFacadeSet = new Set();
        const $serviceSubtypeSelect = $("#service-subtype-select");

        serviceSubtypesFacade.forEach(function (serviceSubtypeFacade) {
            if (serviceSubtypeFacade.serviceTypeFacade.idServiceType === Number($("#service-type-select").val())) {
                serviceSubtypeFacadeSet.add(serviceSubtypeFacade);
            }
        })

        serviceSubtypeFacadeSet.forEach(function (serviceSubtypeFacade) {
            const option = $("<option></option>").val(serviceSubtypeFacade.idServiceSubtype).text(serviceSubtypeFacade.name);

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

        const serviceFacade = searchService(idService);

        $("#service-id-input").val(serviceFacade.idService);
        $("#service-type-select").val(serviceFacade.serviceSubtypeFacade.serviceTypeFacade.idServiceType);
        reloadServiceSubtypeSelectOptions();
        $("#service-subtype-select").val(serviceFacade.serviceSubtypeFacade.idServiceSubtype);
        $("#service-name-input").val(serviceFacade.name);
        $("#service-time-select").val(serviceFacade.time);
        $("#service-price-input").val(serviceFacade.price);
    }

    function saveService() {
        const idServiceSubtype = Number($("#service-subtype-select").val());
        const serviceName = $("#service-name-input").val();
        const servicePrice = $("#service-price-input").val();
        const serviceTime = $("#service-time-select").val();
        const idService = $("#service-id-input").val();

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

        const serviceFacadeToSend = {
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
                loadServiceTab(accountFacadeJson.idAccount, true);
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
                loadServiceTab(accountFacadeJson.idAccount, true);
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
                console.log("Endpoint 17 done::: ");
                servicesFacadeJson = data;
            },
            error: function () {
                callMessagePopup("Невозможно загрузить услуги!", "Что-то пошло не так. Невозможно загрузить услуги!")
            }
        });

        return servicesFacadeJson;
    }
}
