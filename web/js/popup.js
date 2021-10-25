$('.open-login-popup').on('click', function (e) {
    e.preventDefault();
    $('.login-popup-bg').fadeIn(800);
    $('.login-popup').fadeIn(800);
});

function closeLoginPopup() {
    $('.login-popup-bg').fadeOut(800);
    $('.login-popup').fadeOut(800);
}

$('.close-login-popup').on('click', function () {
    closeLoginPopup();
    repaintFields();
});

$('.login-popup-bg').on('click', function () {
    closeLoginPopup();
})

$('.open-register-popup').on('click', function (e) {
    e.preventDefault();
    $('.register-popup-bg').fadeIn(800);
    $('.register-popup').fadeIn(800);
});

function closeRegisterPopup() {
    $('.register-popup-bg').fadeOut(800);
    $('.register-popup').fadeOut(800);
}

$('.close-register-popup').on('click', function () {
    closeRegisterPopup();
});

$('.register-popup-bg').on('click', function () {
    closeRegisterPopup();
    repaintFields();
});

$("#confirm-register").on("click", function () {
});

$("#confirm-login").on("click", function () {
});

$("#l-input-email").on("change", function () {
    validateAuthorize();
});

$("#l-input-password").on("change", function () {
    validateAuthorize();
});

$("#r-input-email").on("change", function () {
    validateRegister();
});

$("#r-input-password").on("change", function () {
    validateRegister();
});

$("#r-input-confirm-password").on("change", function () {
    validateRegister();
});

let register_fields = $(".register-popup > .popup-body > form > .image-input > input");
let login_fields = $(".login-popup > .popup-body > form > .image-input > input");

function repaintFields() {
    for (let field of register_fields) {
        field.style.borderColor = "";
    }
    for (let field of login_fields) {
        field.style.borderColor = "";
    }
}

let reasons = ["empty", "incorrect", "different", "short"];

function validateRegister() {
    let validationResult = true;
    for (let field of register_fields) {
        for (let reason of reasons) {
            $("#" + field.id + "-" + reason).css("display", "none");
        }

        if (field.value == null || field.value === "") {
            field.setAttribute("reason", "empty");
        } else if (field.value.length < 5 && field.id !== "r-input-confirm-password") {
            field.setAttribute("reason", "short");
        } else if (field.id === "r-input-email" && !/^([a-z0-9_-]+\.)*[a-z0-9_-]+@[a-z0-9_-]+(\.[a-z0-9_-]+)*\.[a-z]{2,6}$/.test(field.value)) {
            field.setAttribute("reason", "incorrect");
        } else if (field.id === "r-input-confirm-password" && $("#r-input-confirm-password")[0].value !== $("#r-input-password")[0].value) {
            field.setAttribute("reason", "different");
        } else {
            field.style.borderColor = ""
            field.setAttribute("valid", true);
            field.setAttribute("reason", "");
        }

       validationResult = setFieldInvalid(field, validationResult);
    }

    $("#confirm-register")[0].disabled = validationResult === false;
}

function validateAuthorize() {
    let validationResult = true;
    for (let field of login_fields) {
        for (let reason of reasons) {
            $("#" + field.id + "-" + reason).css("display", "none");
        }

        if (field.value == null || field.value === "") {
            field.setAttribute("reason", "empty");
        } else if (field.id === "l-input-email" && !/^([a-z0-9_-]+\.)*[a-z0-9_-]+@[a-z0-9_-]+(\.[a-z0-9_-]+)*\.[a-z]{2,6}$/.test(field.value)) {
            field.setAttribute("reason", "incorrect");
        } else {
            field.style.borderColor = ""
            field.setAttribute("valid", true);
            field.setAttribute("reason", "");
        }

        validationResult = setFieldInvalid(field, validationResult);
    }

    $("#confirm-login")[0].disabled = validationResult === false;
}

function setFieldInvalid(field, validationResult) {
    if (field.getAttribute("reason") !== '') {
        validationResult = false;
        field.style.borderColor = "#ff4444";
        field.setAttribute("valid", false);
        $("#" + field.id + "-" + field.getAttribute("reason")).css("display", "block");
    }

    return validationResult;
}

