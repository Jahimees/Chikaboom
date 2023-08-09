<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="content">
    <div class="row w-100 input-box">
        <div class="col-4 common-black-text">
            Электронная почта:
        </div>
        <div id="email-placeholder" class="col-4 common-text placeholder">

        </div>
        <div id="change-email-btn" onclick="openEditEmailPopup()" class="col-1 edit-button">
            <img src="/image/icon/edit_icon.svg">
        </div>
    </div>
    <hr>
    <div class="row w-100 input-box">
        <div class="col-4 common-black-text">
            Номер телефона:
        </div>
        <div id="phone-placeholder" class="col-4 common-text placeholder">

        </div>
        <div id="change-phone-btn" onclick="openPhoneEditPopup()" class="col-1 edit-button">
            <img src="/image/icon/edit_icon.svg">
        </div>
    </div>
    <hr>
    <div class="row w-100 input-box">
        <div class="col-4 common-black-text">
            Пароль:
        </div>
        <div id="password-placeholder" class="col-4 common-text placeholder">
            *****
        </div>
        <div id="change-password-btn" onclick="openPasswordEditPopup()" class="col-1 edit-button">
            <img src="/image/icon/edit_icon.svg">
        </div>
    </div>
    <hr>
</div>

<script rel="script" src="/js/jquery-ui-1.10.4.custom.min.js"></script>

<script src="/js/phonecode/countries.js"></script>
<script src="/js/phonecode/phonecode.js"></script>

<script>
    function openEditEmailPopup() {
        dropAllFields();
        addField("Электронная почта", "email", "text", "example@gmail.com", false, [new Validation("Неверный формат электронной почты", InvalidReason.EMAIL)]);
        openPopup("edit-popup");
    }

    function openPhoneEditPopup() {
        dropAllFields();
        addField("Номер телефона", "phone", "text", "(44) 111-11-11", true, [new Validation("Неверный шаблон телефона", InvalidReason.PHONE),
            new Validation("Поле не может быть пустым", InvalidReason.EMPTY)]);
        openPopup("edit-popup");
    }

    function openPasswordEditPopup() {
        dropAllFields();
        addField("Старый пароль", "oldPassword", "password", "*****", false, [new Validation("Поле не может быть пустым", InvalidReason.EMPTY)]);
        addField("Новый пароль", "newPassword", "password", "*****", false, [new Validation("Поле не может быть пустым", InvalidReason.EMPTY)]);
        addField("Подтвердите новый пароль", "confirmNewPassword", "password", "*****", false, [new Validation("Поле не может быть пустым", InvalidReason.EMPTY)]);
        openPopup("edit-popup");
    }

    $(document).ready(function () {
        $("#email-placeholder")[0].innerText = accountJson.email;
        $("#phone-placeholder")[0].innerText = "+" + accountJson.phoneCode.phoneCode + " " + accountJson.phone;
    });
</script>