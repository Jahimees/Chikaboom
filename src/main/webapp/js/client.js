{
    let loadedClientsDetails = [];

    function addRowToDataTable(clientDetails, tableId) {
        let tableName = tableId ? tableId : "default";

        loadedClientsDetails.push(clientDetails);
        let phoneText = clientDetails.displayedPhone ? clientDetails.displayedPhone : "Номер не указан";

        let name = (clientDetails.firstName ? clientDetails.firstName + " " : "")
            + (clientDetails.lastName ? clientDetails.lastName : "");
        name = name ? name : "Безымянный";

        let nameDiv = "<div class='light-button btn m-2 master-only' data-bs-toggle='modal' user-details-id='"
            + clientDetails.idUserDetails + "' data-bs-target='#clientInfoModal'>" + secureCleanValue(name) + "</div>";

        let deleteBtn = "";
        if (typeof clientDetails.masterOwnerFacade !== "undefined" &&
            clientDetails.masterOwnerFacade.idAccount === accountFacadeJson.idAccount) {
            deleteBtn = "<div class='edit-button col1' " +
                "onclick='callConfirmDeleteClientUserDetails(" +
                clientDetails.idUserDetails + "," + clientDetails.masterOwnerFacade.idAccount + ")' " +
                "idUserDetails='" + clientDetails.idUserDetails + "'>" +
                "<img src='/image/icon/cross_icon.svg' width='22px'>" +
                "</div>"
        }


        $("#" + tableName + "_table").DataTable().row.add([
            nameDiv,
            secureCleanValue(phoneText),
            clientDetails.visitCount,
            clientDetails.lastVisitDate != null ? new Date(clientDetails.lastVisitDate).toLocaleDateString('ru') : "",
            deleteBtn
        ]).draw();
    }

    function callConfirmDeleteClientUserDetails(idUserDetails, idAccountMaster) {
        callMessagePopup("Удаление", "Вы действительно хотите удалить клиента? Также будут удалены все его записи")
        $("#decline-message-btn").css("display", "block");
        $("#confirm-message-btn").attr("onclick", "deleteClientUserDetails(" + idUserDetails + "," + idAccountMaster + ")");
    }

    function deleteClientUserDetails(idUserDetails, idAccountMaster) {
        $.ajax({
            method: "delete",
            url: "/accounts/" + idAccountMaster + "/clients/" + idUserDetails,
            contentType: "application/json",
            dataType: "text",
            success: () => {
                let clientsJSON = loadClients(idAccountMaster)
                fillClientsTable(clientsJSON, 'client');

                $("#messageModal").on("hidden.bs.modal", function () {
                    callMessagePopup("Клиент удалён!", "Клиент и его записи успешно удалены!");
                    $("#messageModal").unbind();
                });
            },
            error: () => {
                $("#messageModal").on("hidden.bs.modal", function () {
                    callMessagePopup("Ошибка!", "Невозможно удалить клиента!");
                    $("#messageModal").unbind();
                });
            }
        })
    }

    function createClient(idAccount) {
        let firstNameVal = $("#client-first-name-input").val();
        let lastNameVal = $("#client-last-name-input").val();
        let phoneVal = $("#client-phone-input").val();
        let aboutVal = $("#client-about-input").val();

        let selectedCountryData = window.intlTelInputGlobals.getInstance(
            document.querySelector("#client-phone-input")).getSelectedCountryData();
        let phoneCode = selectedCountryData.dialCode;
        let countryCut = selectedCountryData.iso2;

        if (validateFields(firstNameVal, lastNameVal, phoneVal, aboutVal)) {
            let userDetailsObject = {
                displayedPhone: phoneVal,
                firstName: firstNameVal,
                lastName: lastNameVal,
                aboutFacade: {
                    text: aboutVal,
                },
                phoneCodeFacade: {
                    phoneCode: phoneCode,
                    countryCut: countryCut
                },
                masterOwnerFacade: {
                    idAccount: idAccount
                }
            }

            $('#createClientModal').modal('hide');

            $.ajax({
                method: "post",
                url: "/user-details",
                contentType: "application/json",
                dataType: "json",
                data: JSON.stringify(userDetailsObject),
                success: function (clientDetailsFacade) {
                    addRowToDataTable(clientDetailsFacade, "client");
                    callMessagePopup("Клиент создан", "Клиент успешно создан!")
                },
                error: function () {
                    callMessagePopup("Ошибка!", "Невозможно создать клиента!")
                }
            })
        }
    }

    function validateFields(firstNameVal, lastNameVal, phoneVal, aboutVal) {
        let flag = true;
        if (!isValid(firstNameVal, 'firstName')) {
            $("#first-name-invalid-label").css("display", "block");
            flag = false;
        } else {
            $("#first-name-invalid-label").css("display", "none");
        }

        if (!isValid(lastNameVal, 'lastName')) {
            $("#last-name-invalid-label").css("display", "block");
            flag = false;
        } else {
            $("#last-name-invalid-label").css("display", "none");
        }

        if (!window.intlTelInputGlobals.getInstance(
                document.querySelector("#client-phone-input")).isValidNumber() &&
            $("#client-phone-input").val() !== "") {
            flag = false;
        }

        if (!isValid(aboutVal, 'about')) {
            $("#about-invalid-label").css("display", "block");
            flag = false;
        } else {
            $("#about-invalid-label").css("display", "none");
        }

        return flag;
    }

    function isValid(value, type) {
        switch (type) {
            case 'firstName': {
                return /^[a-zA-Zа-яА-ЯёЁ]+$/.test(value);
            }
            case 'lastName': {
                return (/^[a-zA-Zа-яА-ЯёЁ]+$/.test(value) || value.length === 0);
            }
            case 'about': {
                return value.length <= 300
            }
        }
    }

///////////////////////////////////////////CLIENTS DATATABLE///////////////////////////////////////

    function loadClients(idMasterAccount) {
        let response;
        $.ajax({
            type: "get",
            url: "/accounts/" + idMasterAccount + "/clients",
            contentType: "application/json",
            dataType: "json",
            async: false,
            success: function (data) {
                response = data;
            },
            error: function () {
                callMessagePopup("Ошибка", "Невозможно загрузить информацию о клиентах!")
            }
        })

        return response;
    }

    function fillClientsTable(clientsJSON, tableId) {
        let tableName = tableId ? tableId : "default"
        let $dataTable = $("#" + tableName + "_table");
        loadedClientsDetails = [];

        if (!$.fn.DataTable.isDataTable('#' + tableName)) {
            $dataTable.DataTable().data().clear();
            $dataTable.DataTable().destroy();
        }

        initDataTable(tableName);

        clientsJSON.forEach(function (clientDetailsFacade) {
            addRowToDataTable(clientDetailsFacade, 'client')
        })
    }

///////////////////////////////////////////MODAL CLIENT APPOINTMENTS///////////////////////////////////////
    let shownUserDetails;

    $(document).ready(() => {
        let $clientInfoModal = $("#clientInfoModal");
        let $clientAppointmentsTable = $("#client_appointments_table")

        //on open client info modal
        $clientInfoModal.unbind();
        $clientInfoModal.on("show.bs.modal", function (event) {
            let idUserDetails = event.relatedTarget.getAttribute("user-details-id");
            let $saveClientInfoBtn = $("#save-client-info-btn");
            $saveClientInfoBtn.remove();

            let $clientFirstNameInput = $("#client-first-name-input-upd");
            let $clientLastNameInput = $("#client-last-name-input-upd");
            let $clintAboutInput = $("#client-about-input-upd");
            let $clientPhoneInput = $("#client-phone-input-upd");

            loadedClientsDetails.forEach(function (detailsFacade) {
                if (detailsFacade.idUserDetails == idUserDetails) {

                    $clientFirstNameInput.val(detailsFacade.firstName);
                    $clientLastNameInput.val(detailsFacade.lastName);

                    console.log(detailsFacade)
                    $clintAboutInput.val(detailsFacade.aboutFacade ? detailsFacade.aboutFacade.text : "");
                    $clientPhoneInput.val(detailsFacade.displayedPhone);

                    if (detailsFacade.phoneCodeFacade !== null && typeof detailsFacade.phoneCodeFacade !== "undefined") {
                        window.intlTelInputGlobals.getInstance(
                            document.querySelector("#client-phone-input-upd"))
                            .setCountry(detailsFacade.phoneCodeFacade.countryCut);
                    }

                    let lastVisitDate = detailsFacade.lastVisitDate ? new Date(detailsFacade.lastVisitDate).toLocaleDateString('ru') : "-"
                    $("#client-visit-count-upd").text("Количество посещений: " + detailsFacade.visitCount);
                    $("#client-last-visit-date-upd").text("Последнее посещение: " + lastVisitDate);

                    loadClientAppointmentsInfoForModal(idUserDetails);

                    if (detailsFacade.masterOwnerFacade != null && typeof detailsFacade.masterOwnerFacade != 'undefined') {
                        shownUserDetails = detailsFacade;
                        let $closeClientInfoBtn = $("#close-client-info-btn");

                        $closeClientInfoBtn.before("<button id='save-client-info-btn' class='popup-btn' type='button' " +
                            "onClick='saveUserDetails()' " +
                            "className='popup-btn'>Сохранить</button>")
                        validateClientInfoFields();
                    }
                }
            })

            $("#client-first-name-input-upd, " +
                "#client-last-name-input-upd, " +
                "#client-about-input-upd, " +
                "#client-phone-input-upd").on("keyup", () => {
                validateClientInfoFields();
            })
        })

        $clientInfoModal.on("hide.bs.modal", function () {
            if (!$.fn.DataTable.isDataTable('#client_appointments')) {
                $clientAppointmentsTable.DataTable().data().clear();
                $clientAppointmentsTable.DataTable().destroy();
            }
        });
    })

    function saveUserDetails() {
        if (validateClientInfoFields()) {
            let selectedCountryData = window.intlTelInputGlobals.getInstance(
                document.querySelector("#client-phone-input-upd")).getSelectedCountryData();

            let phoneCode = selectedCountryData.dialCode;
            let countryCut = selectedCountryData.iso2;
            let phone = $("#client-phone-input-upd").val();
            let firstName = $("#client-first-name-input-upd").val();
            let lastName = $("#client-last-name-input-upd").val();
            let aboutText = $("#client-about-input-upd").val();

            let updatedUserDetails = {
                firstName: firstName,
                lastName: lastName,
                displayedPhone: phone,
                phoneCode: {
                    phoneCode: phoneCode,
                    countryCut: countryCut
                },
                aboutFacade: {
                    text: aboutText
                },
                masterOwnerFacade: shownUserDetails.masterOwnerFacade
            }

            $.ajax({
                method: "patch",
                url: "/user-details/" + shownUserDetails.idUserDetails,
                contentType: "application/json",
                dataType: "json",
                data: JSON.stringify(updatedUserDetails),
                success: () => {
                    $("#close-client-info-btn").click();
                    callMessagePopup("Данные сохранены", "Данные клиента успешно сохранены!");
                    let clientsJSON = loadClients(shownUserDetails.masterOwnerFacade.idAccount);
                    fillClientsTable(clientsJSON, 'client');
                },
                error: () => {
                    $("#close-client-info-btn").click();
                    callMessagePopup("Ошибка!", "Невозможно сохранить данные о записях клиента")
                }
            })
        }
    }

    function validateClientInfoFields() {

        let flag = true;
        if (!isValid($("#client-first-name-input-upd").val(), "firstName")) {
            flag = false;
            $("#first-name-invalid-label-upd").css("display", "block");
        } else {
            $("#first-name-invalid-label-upd").css("display", "none");
        }

        if (!isValid($("#client-last-name-input-upd").val(), "lastName")) {
            flag = false;
            $("#last-name-invalid-label-upd").css("display", "block");
        } else {
            $("#last-name-invalid-label-upd").css("display", "none");
        }

        if (!isValid($("#client-about-input-upd").val(), "about")) {
            flag = false;
            $("#about-invalid-label-upd").css("display", "block");
        } else {
            $("#about-invalid-label-upd").css("display", "none");
        }

        if (!window.intlTelInputGlobals.getInstance(
                document.querySelector("#client-phone-input-upd")).isValidNumber()
            && $("#client-phone-input-upd").val() !== '') {
            flag = false;
        }

        return flag;
    }

    function loadClientAppointmentsInfoForModal(idUserDetails) {
        $.ajax({
            method: "get",
            url: "/accounts/" + accountFacadeJson.idAccount + "/appointments",
            async: false,
            contentType: "application/text",
            dataType: "json",
            data: {
                idUserDetails: idUserDetails
            },
            success: function (data) {
                fillClientAppointmentsTableForModal(data, 'client_appointments')
            },
            error: function () {
                callMessagePopup("Ошибка!", "Невозможно загрузить данные о записях клиента");
            }
        })
    }

    function fillClientAppointmentsTableForModal(appointmentsJSON, tableId) {
        let tableName = tableId ? tableId : "default";
        let $dataTable = $("#" + tableName + "_table");

        if (!$.fn.DataTable.isDataTable('#' + tableName)) {
            $dataTable.DataTable().data().clear();
            $dataTable.DataTable().destroy();
        }

        initDataTable(tableName);

        appointmentsJSON.forEach(function (appointmentFacade) {
            let serviceNameVal = appointmentFacade.serviceFacade.name;
            let appointmentDateVal = new Date(appointmentFacade.appointmentDateTime).toLocaleDateString('ru');
            let appointmentTimeVal = new Date(appointmentFacade.appointmentDateTime).toLocaleTimeString('ru');
            let durationTimeVal = appointmentFacade.serviceFacade.time;
            let priceVal = appointmentFacade.serviceFacade.price;

            $dataTable.DataTable().row.add([
                serviceNameVal,
                appointmentDateVal,
                appointmentTimeVal,
                durationTimeVal,
                priceVal
            ]).draw();
        })
    }
}
