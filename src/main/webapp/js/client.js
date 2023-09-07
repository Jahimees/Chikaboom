{
    let loadedClientsDetails = [];

    function addRowToDataTable(clientDetails, tableId) {
        let tableName = tableId ? tableId : "default";

        loadedClientsDetails.push(clientDetails);
        let phoneText = "";
        if (clientDetails.userDetails !== null) {
            phoneText = clientDetails.displayedPhone ? clientDetails.displayedPhone : "Номер не указан";
        }

        let name = (clientDetails.firstName ? clientDetails.firstName + " " : "")
            + (clientDetails.lastName ? clientDetails.lastName : "");
        name = name ? name : "Безымянный";

        let nameDiv = "<div class='light-button btn m-2 master-only' data-bs-toggle='modal' user-details-id='"
            + clientDetails.idUserDetails + "' data-bs-target='#clientInfoModal'>" + secureCleanValue(name) + "</div>";

        let deleteBtn = "";
        if (clientDetails.masterOwner !== null && clientDetails.masterOwner.idAccount === accountJson.idAccount) {
            deleteBtn = "<div class='edit-button col1' " +
                "onclick='callConfirmDeleteClientUserDetails(" +
                clientDetails.idUserDetails + "," + clientDetails.masterOwner.idAccount + ")' " +
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
        repairDefaultMessagePopup();
        $("#decline-message-btn").css("display", "block");
        $("#confirm-message-btn").attr("onclick", "deleteClientUserDetails(" + idUserDetails + "," + idAccountMaster + ")");

        $("#popup-message-text").text("Вы действительно хотите удалить клиента? Также будут удалены все его записи");
        $(".message-popup > .popup-title > #popup-message-header").text("Удаление");
        openPopup("message-popup");
    }

    function deleteClientUserDetails(idUserDetails, idAccountMaster) {
        closePopup('message-popup');

        $.ajax({
            method: "delete",
            url: "/accounts/" + idAccountMaster + "/clients/" + idUserDetails,
            contentType: "application/json",
            dataType: "text",
            success: () => {
                repairDefaultMessagePopup();
                $("#popup-message-text").text("Клиент и его записи успешно удалены!");
                $(".message-popup > .popup-title > #popup-message-header").text("Клиент удалён!");
                openPopup('message-popup');

                $("#confirm-message-btn").attr(
                    "onclick", "closePopup('message-popup'),  loadClientsAndShowTable(" + idAccountMaster + ")");
            },
            error: () => {
                repairDefaultMessagePopup();
                $("#popup-message-text").text("Невозможно удалить клиента!");
                $(".message-popup > .popup-title > #popup-message-header").text("ОШИБКА!");
                openPopup('message-popup');
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
                about: {
                    text: aboutVal,
                },
                phoneCode: {
                    phoneCode: phoneCode,
                    countryCut: countryCut
                },
                masterOwner: {
                    idAccount: idAccount
                }
            }

            $.ajax({
                method: "post",
                url: "/user-details",
                contentType: "application/json",
                dataType: "json",
                data: JSON.stringify(userDetailsObject),
                success: function (clientDetails) {
                    addRowToDataTable(clientDetails, "client");
                    $('#createClientModal').modal('hide');
                    repairDefaultMessagePopup();
                    $("#popup-message-text").text("Клиент успешно создан!");
                    $(".message-popup > .popup-title > #popup-message-header").text("Клиент создан");
                    openPopup('message-popup');
                },
                error: function () {
                    $('#createClientModal').modal('hide');
                    repairDefaultMessagePopup();
                    $("#popup-message-text").text("Невозможно создать клиента!");
                    $(".message-popup > .popup-title > #popup-message-header").text("ОШИБКА!");
                    openPopup('message-popup');
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
                return /^[a-zA-Zа-яА-Я]+$/.test(value);
            }
            case 'lastName': {
                return (/^[a-zA-Zа-яА-Я]+$/.test(value) || value.length === 0);
            }
            case 'about': {
                return value.length <= 300
            }
        }
    }

///////////////////////////////////////////CLIENTS DATATABLE///////////////////////////////////////

    function loadClientsAndShowTable(idMasterAccount) {
        $.ajax({
            type: "get",
            url: "/accounts/" + idMasterAccount + "/clients",
            contentType: "application/json",
            dataType: "json",
            async: false,
            success: function (data) {
                fillClientsTable(data, 'client');
            },
            error: function () {
                repairDefaultMessagePopup();
                $("#popup-message-text").text("Невозможно загрузить информацию о клиентах!");
                $(".message-popup > .popup-title > #popup-message-header").text("ОШИБКА!");
                openPopup('message-popup');
            }
        })
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

        clientsJSON.forEach(function (clientDetails) {
            addRowToDataTable(clientDetails, 'client')
        })
    }

///////////////////////////////////////////MODAL CLIENT APPOINTMENTS///////////////////////////////////////
    let shownUserDetails;

    $(document).ready(function () {
        let $clientInfoModal = $("#clientInfoModal");
        let $clientAppointmentsTable = $("#client_appointments_table")

        $clientInfoModal.on("hidden.bs.modal", function (event) {
            if (!$.fn.DataTable.isDataTable('#client_appointments')) {
                $clientAppointmentsTable.DataTable().data().clear();
                $clientAppointmentsTable.DataTable().destroy();
            }
        });

        //on open client info modal
        $clientInfoModal.on("show.bs.modal", function (event) {
            let idUserDetails = event.relatedTarget.getAttribute("user-details-id");
            let $saveClientInfoBtn = $("#save-client-info-btn");
            $saveClientInfoBtn.remove();

            let $clientFirstNameInput = $("#client-first-name-input-upd");
            let $clientLastNameInput = $("#client-last-name-input-upd");
            let $clintAboutInput = $("#client-about-input-upd");
            let $clientPhoneInput = $("#client-phone-input-upd");

            loadedClientsDetails.forEach(function (details) {
                if (details.idUserDetails == idUserDetails) {

                    $clientFirstNameInput.val(details.firstName);
                    $clientLastNameInput.val(details.lastName);

                    $clintAboutInput.val(details.about ? details.about.text : "");
                    $clientPhoneInput.val(details.displayedPhone);

                    if (details.phoneCode !== null && typeof details.phoneCode !== "undefined") {
                        window.intlTelInputGlobals.getInstance(
                            document.querySelector("#client-phone-input-upd"))
                            .setCountry(details.phoneCode.countryCut);
                    }

                    let lastVisitDate = details.lastVisitDate ? new Date(details.lastVisitDate).toLocaleDateString('ru') : "-"
                    $("#client-visit-count-upd").text("Количество посещений: " + details.visitCount);
                    $("#client-last-visit-date-upd").text("Последнее посещение: " + lastVisitDate);

                    loadClientAppointmentsInfoForModal(idUserDetails);

                    if (details.masterOwner != null && typeof details.masterOwner != 'undefined') {
                        shownUserDetails = details;
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
                about: {
                    text: aboutText
                },
                masterOwner: shownUserDetails.masterOwner
            }

            $.ajax({
                method: "patch",
                url: "/user-details/" + shownUserDetails.idUserDetails,
                contentType: "application/json",
                dataType: "json",
                data: JSON.stringify(updatedUserDetails),
                success: () => {
                    $("#close-modal-btn").click();
                    repairDefaultMessagePopup();
                    $("#popup-message-text").text("Данные клиента успешно сохранены!");
                    $("#popup-message-header").text("Данные сохранены");
                    openPopup('message-popup');
                    loadClientsAndShowTable(shownUserDetails.masterOwner.idAccount);
                },
                error: () => {
                    $("#close-modal-btn").click();
                    repairDefaultMessagePopup();
                    $("#popup-message-text").text("Невозможно сохранить данные о записях клиента");
                    $("#popup-message-header").text("Ошибка!");
                    openPopup('message-popup');
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
            document.querySelector("#client-phone-input-upd")).isValidNumber()) {
            flag = false;
        }

        return flag;
    }

    function loadClientAppointmentsInfoForModal(idUserDetails) {
        $.ajax({
            method: "get",
            url: "/accounts/" + accountJson.idAccount + "/appointments",
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
                repairDefaultMessagePopup();
                $("#popup-message-text").text("Невозможно загрузить данные о записях клиента");
                $("#popup-message-header").text("Ошибка!");
                openPopup('message-popup');
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

        appointmentsJSON.forEach(function (appointment) {
            let serviceNameVal = appointment.service.name;
            let appointmentDateVal = new Date(appointment.appointmentDateTime).toLocaleDateString('ru');
            let appointmentTimeVal = new Date(appointment.appointmentDateTime).toLocaleTimeString('ru');
            let durationTimeVal = appointment.service.time;
            let priceVal = appointment.service.price;

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
