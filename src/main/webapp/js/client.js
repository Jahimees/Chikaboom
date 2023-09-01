{
    let loadedClientsDetails = [];

    function fillClientsTable(clientsJSON, tableId) {
        let tableName = tableId ? tableId : "default"
        loadedClientsDetails = [];

        if (!$.fn.DataTable.isDataTable('#' + tableName)) {
            $("#" + tableName + "_table").DataTable().data().clear();
        }

        clientsJSON.forEach(function (clientDetails) {
            addRowToDataTable(clientDetails)
        })
    }

    function addRowToDataTable(clientDetails, tableId) {
        let tableName = tableId ? tableId : "default";

        loadedClientsDetails.push(clientDetails);
        let phoneText = "";
        if (clientDetails.userDetails !== null) {
            phoneText = clientDetails.phoneCode ? "+" + clientDetails.phoneCode.phoneCode + " " + clientDetails.phone : " ";
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
        let phoneCodeVal = $("#country-phone-client-create > .country-phone-selector > .country-phone-selected > span").text();

        firstNameVal = secureCleanValue(firstNameVal);
        lastNameVal = secureCleanValue(lastNameVal);
        phoneVal = secureCleanValue(phoneVal);
        aboutVal = secureCleanValue(aboutVal);
        phoneCodeVal = secureCleanValue(phoneCodeVal);

        let flag = validateFields(firstNameVal, lastNameVal, phoneVal, aboutVal);

        if (!flag) {
            return;
        }

        let userDetailsObject = {
            phone: phoneVal,
            firstName: firstNameVal,
            lastName: lastNameVal,
            about: {
                text: aboutVal
            },
            phoneCode: {
                phoneCode: phoneCodeVal
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

        if (!isValid(phoneVal, 'phone')) {
            $("#phone-invalid-label").css("display", "block");
            flag = false;
        } else {
            $("#phone-invalid-label").css("display", "none");
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

    $(document).ready(function () {

        $("#clientInfoModal").on("show.bs.modal", function (event) {
            let idUserDetails = event.relatedTarget.getAttribute("user-details-id");

            loadedClientsDetails.forEach(function (details) {
                if (details.idUserDetails == idUserDetails) {
                    $("#client-first-name-input-upd").val(details.firstName);
                    $("#client-last-name-input-upd").val(details.lastName);

                    $("#client-about-input-upd").val(details.about ? details.about.text : "");
                    $("#client-phone-input-upd").val(details.phone);

                    $('#client-phone-input-upd').phonecode({
                        preferCo: details.phoneCode ? details.phoneCode.countryCut : "by",
                        id: 'client-info'
                    })


                    let lastVisitDate = details.lastVisitDate ? new Date(details.lastVisitDate).toLocaleDateString('ru') : "-"
                    $("#client-visit-count-upd").text("Количество посещений: " + details.visitCount);
                    $("#client-last-visit-date-upd").text("Последнее посещение: " + lastVisitDate);

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
                            // initDataTable("client-appointments", 'client_appointments_table')
                            fillClientAppointmentsTable(data, 'client_appointments')
                        },
                        error: function () {
                            repairDefaultMessagePopup();
                            $("#popup-message-text")[0].innerText = "Невозможно загрузить данные о записях клиента"
                            $("#popup-message-header")[0].innerText = "Ошибка!";
                            openPopup('message-popup');
                        }

                    })
                    return;
                }
            })
        })
    })

    function fillClientAppointmentsTable(appointmentsJSON, tableId) {
        let tableName = tableId ? tableId : "default";

        if (!$.fn.DataTable.isDataTable('#' + tableName)) {
            $("#" + tableName + "_table").DataTable().data().clear();
        }

        appointmentsJSON.forEach(function (appointment) {
            let serviceNameVal = appointment.service.name;
            let appointmentDateVal = new Date(appointment.appointmentDateTime).toLocaleDateString('ru');
            let appointmentTimeVal = new Date(appointment.appointmentDateTime).toLocaleTimeString('ru');
            let durationTimeVal = appointment.service.time;
            let priceVal = appointment.service.price;

            $("#" + tableName + "_table").DataTable().row.add([
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
//TODO 2) Если повторно вызвать вкладку мои клиенты на вкладке информации о клиенте продублируется header и footer таблицы
//TODO 3) Если клиент не записывался ещё к мастеру в таблице в информации о клиенет у него будут отображаться данные с предыдущей выгрузки