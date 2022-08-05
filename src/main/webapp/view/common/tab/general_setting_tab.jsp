<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="content">
    <div class="row w-100 input-box">
        <div class="col-4 common-black-text">
            Электронная почта:
        </div>
        <div id="email-placeholder" class="col-4 common-text placeholder">
            ${email}
        </div>
        <input id="email-change-input" class="col-4 common-text" style="display: none">
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
            +${phoneCode} ${phone}
        </div>
        <input id="phone-change-input" class="col-4 common-text" style="display: none">
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
        <input id="password-change-input" class="col-4 common-text" style="display: none">
        <div id="change-password-btn" onclick="openChangeInputFields(this)" class="col-1 edit-button">
            <img src="/image/icon/edit_icon.svg">
        </div>
    </div>
    <hr>
</div>

<script src="../../../js/tab/general_setting_tab.js"></script>

<script>
    function openEditEmailPopup() {
        dropAllFields();
        addField("Электронная почта", "email", "text", "example@gmail.com");
        openPopup("edit-popup");
    }

    function openPhoneEditPopup() {
        dropAllFields();
        addField("Номер телефона", "phone", "text", "(44) 111-11-11", true);
        openPopup("edit-popup");
    }
</script>