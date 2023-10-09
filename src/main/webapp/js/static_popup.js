{
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
                [{name: "ROLE_CLIENT"}] : [{name: "ROLE_CLIENT"}, {name: "ROLE_MASTER"}];

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
                    $("#registerModal").modal('hide');
                    callMessagePopup("Регистрация успешна!", "Вы успешно прошли регистрацию!")
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
                hideWarnWrongLoginData();
                var hrefParts = location.href.split("#")
                location.href = hrefParts[0];
            },
            error: function () {
                showWarnWrongLoginData();
            }
        });
    });

    $("#l-input-phone").on("keyup", function () {
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

    let register_fields = $(".r-popup-input-field");
    let login_fields = $(".l-popup-input-field");
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
        } else if (field.id === "r-input-confirm-password" && $("#" + field.id).val() !== $("#r-input-password").val()) {
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
        let intlInstance = window.intlTelInputGlobals.getInstance(
            document.querySelector("#l-input-phone"));

        if (field.value == null || field.value === "") {
            field.setAttribute("reason", "empty");
        } else if (field.id === "l-input-phone" && !intlInstance.isValidNumber()) {
            field.setAttribute("reason", "incorrect");
        } else {
            field.style.borderColor = ""
            field.setAttribute("valid", true);
            field.setAttribute("reason", "");
        }

        defineIsFieldValid(field);
    }

    function validateAllAuthorizeFields() {
        let flag = true;
        for (let field of login_fields) {
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
        if (field.getAttribute("reason") !== '' && field.getAttribute("id")) {
            field.style.borderColor = "#ff4444";
            field.setAttribute("valid", false);
            $("#" + field.id + "-" + field.getAttribute("reason")).css("display", "block");
            return false;
        }

        return true;
    }

    function showWarnWrongLoginData() {
        $("#l-phone-or-password-incorrect").css("display", "block");
        $("#l-input-password").val("");
        $("#l-input-password").attr("valid", "false");
    }

    function hideWarnWrongLoginData() {
        $("#l-phone-or-password-incorrect").css("display", "none");
    }

    function showWarnPhoneDuplicate() {
        $("#r-phone-duplicate").css("display", "block");
    }

    $("#login-submit-btn").on("click", () => {
        if (validateAllAuthorizeFields()) {
            let selectedCountryData = window.intlTelInputGlobals.getInstance(
                document.querySelector("#l-input-phone")).getSelectedCountryData();
            let $inputUsername = $("#l-input-phone");
            let $hiddenInput = $("#l-hidden-input-phone");
            let $loginForm = $("#login-form");
            $hiddenInput.val($inputUsername.val() + "_" + selectedCountryData.iso2);
            $loginForm.append("<input name='isRequestFromUI' value='true' hidden='hidden'>")
            $loginForm.submit();
        }
    })
}
