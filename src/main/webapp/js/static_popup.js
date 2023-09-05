//TODO Избавиться от дублирования кода по всему файлу
function openPopup(popupName) {
    $('.popup-bg').fadeIn(200);
    $('.' + popupName).fadeIn(200);
}

function closePopup(popupName) {
    $('.popup-bg').fadeOut(200);
    $('.' + popupName).fadeOut(200);
    repaintLoginRegisterFields();
}

$('.popup-bg').on('click', function () {
    closePopup('login-popup');
    closePopup('register-popup');
    closePopup('message-popup');
    closePopup('edit-popup');
});

$("#confirm-register").on("click", function () {
    if (validateAllRegisterFields()) {
        let selectedCountryData = window.intlTelInputGlobals.getInstance(
            document.querySelector("#r-input-phone")).getSelectedCountryData();

        let phoneCode = {
            phoneCode: selectedCountryData.dialCode,
            countryCut: selectedCountryData.iso2
        }
        let phone = $("#r-input-phone").val();
        let password = $("#r-input-password").val();
        let username = $("#r-input-username").val();
        let roles = $("role :checked, :radio")[0].checked ?
            [{name: "ROLE_CLIENT"}] : [{name: "ROLE_CLIENT"},{name: "ROLE_MASTER"}];

        let account = {
            userDetails: {
                phoneCode: phoneCode,
                phone: phone,
            },
            password: password,
            username: username,
            roles: roles
        }

        $.ajax({
            type: "POST",
            url: "/chikaboom/registration",
            contentType: "application/json",
            dataType: "json",
            data: JSON.stringify(account),
            success: function () {
                closePopup('register-popup');
                closePopup('login-popup');
                repairDefaultMessagePopup();
                $("#popup-message-text").text("Вы успешно прошли регистрацию!");
                $("#popup-message-header").text("Регистрация успешна!");
                openPopup('message-popup');
            },
            error: function () {
                showWarnPhoneDuplicate();
            }
        });
    }
});

$("#login-form").submit(function (e) {
    e.preventDefault();
    $.ajax({
        url: "/login",
        type: 'POST',
        beforeSend: function (xhr) {
            xhr.withCredentials = true;
        },
        data: $('#login-form').serialize(),
        success: function () {
            var hrefParts = location.href.split("#")
            location.href = hrefParts[0];
        },
        error: function () {
            showWarnWrongLoginData();
        }
    });
});

$("#l-input-username").on("keyup", function () {
    validateAuthorizeField(this);
})
$("#l-input-password").on("keyup", function () {
    validateAuthorizeField(this);
});

$("#r-input-username").on("keyup", function () {
    validateRegisterField(this);
});

$("#r-input-password").on("keyup", function () {
    validateRegisterField(this);
});

$("#r-input-confirm-password").on("keyup", function () {
    validateRegisterField(this);
});

let login_register_fields = [];
let register_fields = $(".register-popup > .popup-body > .popup-input > input");
let login_fields = $(".login-popup > .popup-body > .popup-input > input");

function repaintLoginRegisterFields() {
    if (login_register_fields.length === 0) {
        for (let field of login_fields) {
            login_register_fields.push(field);
        }
        for (let field of register_fields) {
            login_register_fields.push(field);
        }
    }

    for (let field of login_register_fields) {
        field.style.borderColor = "";
        field.setAttribute("valid", false);
        field.value = "";
        $("#" + field.id + "-" + field.getAttribute("reason")).css("display", "none");
    }

    hideWarnWrongLoginData();
    hideWarnPhoneDuplicate();
}

let reasons = ["empty", "incorrect", "different", "short"];

function validateRegisterField(field) {
    for (let reason of reasons) {
        $("#" + field.id + "-" + reason).css("display", "none");
    }

    if (field.value == null || field.value === "") {
        field.setAttribute("reason", "empty");
    } else if (field.value.length < 2 && field.id === "r-input-username") {
        field.setAttribute("reason", "short");
    } else if (field.value.length < 5 && field.id === "r-input-password") {
        field.setAttribute("reason", "short");
    } else if (field.id === "r-input-phone" && !window.intlTelInputGlobals.getInstance(
        document.querySelector("#r-input-phone")).isValidNumber()) {
        field.setAttribute("reason", "incorrect");
    } else if (field.id === "r-input-username" && !/^[a-zA-ZА-Яа-я]+\s{0,1}[a-zA-ZА-Яа-я]+$/.test(field.value)) {
        field.setAttribute("reason", "incorrect");
    } else if (field.id === "r-input-confirm-password" && $("#" + field.id)[0].value !== $("#r-input-password")[0].value) {
        field.setAttribute("reason", "different");
    } else {
        field.style.borderColor = ""
        field.setAttribute("valid", true);
        field.setAttribute("reason", "");
    }

    defineIsFieldValid(field);
}

function validateAuthorizeField(field) {
    for (let reason of reasons) {
        $("#" + field.id + "-" + reason).css("display", "none");
    }

    if (field.value == null || field.value === "") {
        field.setAttribute("reason", "empty");
    } else if (field.id === "l-input-username" && !/^[a-zA-ZА-Яа-я]+\s{0,1}[a-zA-ZА-Яа-я]+$/.test(field.value)) {
        field.setAttribute("reason", "incorrect");
    } else {
        field.style.borderColor = ""
        field.setAttribute("valid", true);
        field.setAttribute("reason", "");
    }

    defineIsFieldValid(field);
}

function validateAllAuthorizeFields() {
    var flag = true;
    for (var field of login_fields) {
        if (field.getAttribute("valid") === 'false') {
            validateAuthorizeField(field);
            flag = false;
        }
    }

    return flag
}

function validateAllRegisterFields() {
    var flag = true;
    for (var field of register_fields) {
        if (field.getAttribute("valid") === 'false') {
            validateRegisterField(field);
            flag = false;
        }
    }

    return flag
}

function defineIsFieldValid(field) {
    if (field.getAttribute("reason") !== '') {
        field.style.borderColor = "#ff4444";
        field.setAttribute("valid", false);
        $("#" + field.id + "-" + field.getAttribute("reason")).css("display", "block");
        return false;
    }

    return true;
}

function showWarnWrongLoginData() {
    $("#l-phone-or-password-incorrect").css("display", "block");
    $("#l-input-password")[0].value = ""
    $("#l-input-password")[0].setAttribute("valid", "false");
}

function hideWarnWrongLoginData() {
    $("#l-phone-or-password-incorrect").css("display", "none");
}

function showWarnPhoneDuplicate() {
    $("#r-phone-duplicate").css("display", "block");
}

function hideWarnPhoneDuplicate() {
    $("#r-phone-duplicate").css("display", "none");
}
