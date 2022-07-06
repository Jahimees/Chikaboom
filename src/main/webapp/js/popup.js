//TODO Избавиться от дублирования кода по всему файлу
$('.open-login-popup').on('click', function (e) {
    closeRegisterPopup();
    e.preventDefault();
    $('.login-popup-bg').fadeIn(800);
    $('.login-popup').fadeIn(800);
});

function closeLoginPopup() {
    $('.login-popup-bg').fadeOut(800);
    $('.login-popup').fadeOut(800);
}

$('.close-login-popup, .login-popup-bg').on('click', function () {
    closeLoginPopup();
    repaintLoginFields();
});

$('.open-register-popup').on('click', function (e) {
    closeLoginPopup();
    e.preventDefault();
    $('.register-popup-bg').fadeIn(800);
    $('.register-popup').fadeIn(800);
});

function closeRegisterPopup() {
    $('.register-popup-bg').fadeOut(800);
    $('.register-popup').fadeOut(800);
}

$('.close-register-popup, .register-popup-bg').on('click', function () {
    closeRegisterPopup();
    repaintRegisterFields();
});

$("#confirm-register").on("click", function () {
    if (validateAllRegisterFields()) {
        var phoneCode = $("#country-phone-register > .country-phone-selector > .country-phone-selected > span")[0].firstChild.textContent;
        var phone = $("#r-input-phone")[0].value;
        var password = $("#r-input-password")[0].value;
        console.log(phoneCode);
        console.log(phone);
        console.log(password);
        $.ajax({
            type: "GET",
            url: "/chikaboom/registration", //TODO выглядит не ок прям совсем
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
            error: function (e) {
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
    validateAuthorizeField($("#l-input-phone")[0]);
});

$("#l-input-password").on("keyup", function () {
    validateAuthorizeField($("#l-input-password")[0]);
});

$("#r-input-phone").on("keyup", function () {
    validateRegisterField($("#r-input-phone")[0]);
});

$("#r-input-password").on("keyup", function () {
    validateRegisterField($("#r-input-password")[0]);
});

$("#r-input-confirm-password").on("keyup", function () {
    validateRegisterField($("#r-input-confirm-password")[0]);
});

let register_fields = $(".register-popup > .popup-body > .image-input > input");
let login_fields = $(".login-popup > .popup-body > .image-input > input");

function repaintLoginFields() {
    for (let field of login_fields) {
        field.style.borderColor = "";
        field.setAttribute("valid", false);
        field.value = "";
        $("#" + field.id + "-" + field.getAttribute("reason")).css("display", "none");
    }
    hideWarnWrongLoginData();
}

function repaintRegisterFields() {
    for (let field of register_fields) {
        field.style.borderColor = "";
        field.setAttribute("valid", false);
        field.value = "";
        $("#" + field.id + "-" + field.getAttribute("reason")).css("display", "none");
    }
    hideWarnPhoneDuplicate();
}

let reasons = ["empty", "incorrect", "different", "short"];

function validateRegisterField(field) {
    for (let reason of reasons) {
        $("#" + field.id + "-" + reason).css("display", "none");
    }

    if (field.value == null || field.value === "") {
        field.setAttribute("reason", "empty");
    } else if (field.value.length < 9 && field.id !== "r-input-confirm-password" && field.id !== "r-input-password") {
        field.setAttribute("reason", "short");
    } else if (field.value.length < 5 && field.id === "r-input-password") {
        field.setAttribute("reason", "short");
    } else if (field.id === "r-input-phone" && !/^(\s*)?([- _():=+]??\d[- _():=+]?){9,14}(\s*)?$/.test(field.value)) {
        field.setAttribute("reason", "incorrect");
    } else if (field.id === "r-input-confirm-password" && $("#r-input-confirm-password")[0].value !== $("#r-input-password")[0].value) {
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
            validateAuthorizeField(field);
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
