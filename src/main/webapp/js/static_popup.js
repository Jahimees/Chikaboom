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
        var phoneCode = $("#country-phone-register > .country-phone-selector > .country-phone-selected > span")[0].firstChild.textContent;
        var phone = $("#r-input-phone")[0].value;
        var password = $("#r-input-password")[0].value;
        var nickname = $("#r-input-nickname")[0].value;
        var role = $("role :checked, :radio")[0].checked ? "client" : "master";
        $.ajax({
            type: "GET",
            url: "/chikaboom/registration",
            contentType: "application/text",
            dataType: "text",
            data: {
                phoneCode: phoneCode,
                phone: phone,
                password: password,
                nickname: nickname,
                role: role
            },
            success: function () {
                closePopup('register-popup');
                closePopup('login-popup');
                repairDefaultMessagePopup();
                $("#popup-message-text")[0].innerText = "Вы успешно прошли регистрацию!"
                $("#popup-message-header")[0].innerText = "Регистрация успешна!";
                openPopup('message-popup');
            },
            error: function () {
                showWarnPhoneDuplicate();
            }
        });
    }
});

$("#confirm-login").on("click", function () {
    if (validateAllAuthorizeFields()) {
        var phoneCode = $("#country-phone-login > .country-phone-selector > .country-phone-selected > span")[0].firstChild.textContent;
        var phone = $("#l-input-phone")[0].value;
        var password = $("#l-input-password")[0].value;
        $.ajax({
            type: "GET",
            url: "/chikaboom/authorization", //TODO выглядит не ок прям совсем
            contentType: "application/text",
            dataType: "text",
            data: {
                phoneCode: phoneCode,
                phone: phone,
                password: password,
            },
            success: function (data) {
                window.location.replace(data);
            },
            error: function () {
                showWarnWrongLoginData();
            }
        });
    }
});

$("#l-input-phone").on("keyup", function () {
    validateAuthorizeField(this);
});

$("#l-input-password").on("keyup", function () {
    validateAuthorizeField(this);
});

$("#r-input-phone").on("keyup", function () {
    validateRegisterField(this);
});

$("#r-input-nickname").on("keyup", function () {
    validateRegisterField(this);
});

$("#r-input-password").on("keyup", function () {
    validateRegisterField(this);
});

$("#r-input-confirm-password").on("keyup", function () {
    validateRegisterField(this);
});

let login_register_fields = [];
let register_fields = $(".register-popup > .popup-body > .popup-image > input");
let login_fields = $(".login-popup > .popup-body > .popup-image > input");

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
    } else if (field.value.length < 9
        && field.id !== "r-input-confirm-password" && field.id !== "r-input-password"
        && field.id !== "r-input-nickname"
    ) {
        field.setAttribute("reason", "short");
    } else if (field.value.length < 2 && field.id === "r-input-nickname") {
        field.setAttribute("reason", "short");
    } else if (field.value.length < 5 && field.id === "r-input-password") {
        field.setAttribute("reason", "short");
    } else if (field.id === "r-input-phone" && !/^(\s*)?([- _():=+]??\d[- _():=+]?){9,14}(\s*)?$/.test(field.value)) {
        field.setAttribute("reason", "incorrect");
    } else if (field.id === "r-input-nickname" && !/^[a-zA-ZА-Яа-я]+\s{0,1}[a-zA-ZА-Яа-я]+$/.test(field.value)) {
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
    } else if (field.id === "l-input-phone" && !/^(\s*)?([- _():=+]??\d[- _():=+]?){9,14}(\s*)?$/.test(field.value)) {
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
