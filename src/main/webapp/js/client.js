function fillClientsTable(clientsJSON) {

    if (typeof $("#default_table").DataTable() !== 'undefined') {
        $("#default_table").DataTable().data().clear();
    }

    clientsJSON.forEach(function (clientDetails) {
        addRowToDataTable(clientDetails)
    })
}

function addRowToDataTable(clientDetails) {
    let phoneText = "";
    if (clientDetails.userDetails !== null) {
        phoneText = clientDetails.phoneCode ? "+" + clientDetails.phoneCode.phoneCode + " " + clientDetails.phone : " ";
    }

    let name = (clientDetails.firstName ? clientDetails.firstName + " " : "")
        + (clientDetails.lastName ? clientDetails.lastName : "");

    name = name ? name : "Безымянный";
    let nameDiv = "<div class='purple-button m-2 master-only' data-bs-toggle='modal' data-bs-target='#clientInfoModal'>" + name + "</div>";
    $("#default_table").DataTable().row.add([
        nameDiv,
        phoneText,
        clientDetails.visitCount,
        clientDetails.lastVisitDate != null ? new Date(clientDetails.lastVisitDate).toLocaleDateString('ru') : ""

    ]).draw();
}

function createClient(idAccount) {
    let firstNameVal = $("#client-first-name-input").val();
    let lastNameVal = $("#client-last-name-input").val();
    let phoneVal = $("#client-phone-input").val();
    let aboutVal = $("#client-about-input").val();
    let phoneCodeVal = $(".country-phone-selected > span").text();

    firstNameVal = secureCleanValue(firstNameVal);
    lastNameVal = secureCleanValue(lastNameVal);
    phoneVal = secureCleanValue(phoneVal);
    aboutVal = secureCleanValue(aboutVal);
    phoneCodeVal = secureCleanValue(phoneCodeVal);

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