{
    let loadedClientsDetails = [];

    function addRowToDataTable(clientDetails, tableId) {
        let tableName = tableId ? tableId : "default";

        loadedClientsDetails.push(clientDetails);
        let phoneText = "";
        if (clientDetails.userDetails !== null) {
            phoneText = clientDetails.phone ? clientDetails.phone : "Номер не указан";
        }

        let name = (clientDetails.firstName ? clientDetails.firstName + " " : "")
            + (clientDetails.lastName ? clientDetails.lastName : "");

        name = name ? name : "Безымянный";
        let nameDiv = "<div class='btn-light btn m-2 master-only' data-bs-toggle='modal' user-details-id='"
            + clientDetails.idUserDetails + "' data-bs-target='#clientInfoModal'>" + name + "</div>";
        $("#" + tableName + "_table").DataTable().row.add([
            nameDiv,
            phoneText,
            clientDetails.visitCount,
            clientDetails.lastVisitDate != null ? new Date(clientDetails.lastVisitDate).toLocaleDateString('ru') : "",
        ]).draw();
    }

    function createClient(idAccount) {
        let firstNameVal = $("#client-first-name-input").val();
        let lastNameVal = $("#client-last-name-input").val();
        let phoneVal = $("#client-phone-input").val();
        let aboutVal = $("#client-about-input").val();
        // let phoneCodeVal = $("#country-phone-client-create > .country-phone-selector > .country-phone-selected > span").text();

        let selectedCountryData = iti.getSelectedCountryData();
        let phoneCode = selectedCountryData.dialCode;
        let countryCut = selectedCountryData.iso2;

        firstNameVal = secureCleanValue(firstNameVal);
        lastNameVal = secureCleanValue(lastNameVal);
        phoneVal = secureCleanValue(phoneVal);
        aboutVal = secureCleanValue(aboutVal);

        // phoneCodeVal = secureCleanValue(phoneCodeVal);

        let flag = validateFields(firstNameVal, lastNameVal, phoneVal, aboutVal);

        if (!flag) {
            return;
        }

        let userDetailsObject = {
            phone: phoneVal,
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
                addRowToDataTable(clientDetails);
                $('#createClientModal').modal('hide');
            },
            error: function () {
                $('#createClientModal').modal('hide');
                repairDefaultMessagePopup();
                $("#popup-message-text")[0].innerText = "Невозможно загрузить информацию о клиентах!"
                $(".message-popup > .popup-title > #popup-message-header")[0].innerText = "ОШИБКА!";
                openPopup('message-popup');
            }
        })
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



        if (!window.intlTelInputGlobals.getInstance(document.querySelector("#client-phone-input")).isValidNumber()) {
            // $("#client-phone-input").css("border-color", "red")
            flag = false;
        } else {
            // $("#client-phone-input").css("border-color", "none")
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
                return /^|[a-zA-Zа-яА-Я]+$/.test(value);
            }
            case 'phone': {
                if (value === '') {
                    return true;
                }
                return /^(\s*)?([- _():=+]??\d[- _():=+]?){9,14}(\s*)?$/.test(value);
            }
            case 'about': {
                return value.length <= 300
            }
        }
    }

///////////////////////////////////////////CLIENTS DATATABLE///////////////////////////////////////

    function loadClientInformation(idMasterAccount) {
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
                $("#popup-message-text")[0].innerText = "Невозможно загрузить информацию о клиентах!"
                $(".message-popup > .popup-title > #popup-message-header")[0].innerText = "ОШИБКА!";
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

            loadedClientsDetails.forEach(function (details) {
                if (details.idUserDetails == idUserDetails) {
                    $("#client-first-name-input-upd").val(details.firstName);
                    $("#client-last-name-input-upd").val(details.lastName);

                    $("#client-about-input-upd").val(details.about ? details.about.text : "");
                    $("#client-phone-input-upd").val(details.phone);

                    window.intlTelInputGlobals.getInstance(
                        document.querySelector("#client-phone-input-upd"))
                        .setCountry(details.phoneCode.countryCut);

                    let lastVisitDate = details.lastVisitDate ? new Date(details.lastVisitDate).toLocaleDateString('ru') : "-"
                    $("#client-visit-count-upd").text("Количество посещений: " + details.visitCount);
                    $("#client-last-visit-date-upd").text("Последнее посещение: " + lastVisitDate);

                    loadClientAppointmentsInfoForModal(idUserDetails);
                    return;
                }
            })
        })
    })

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
                $("#popup-message-text")[0].innerText = "Невозможно загрузить данные о записях клиента"
                $("#popup-message-header")[0].innerText = "Ошибка!";
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

//TODO 1) Нельзя загрузить 2 phoneCode