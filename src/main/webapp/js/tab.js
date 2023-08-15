function selectCurrentTab(thisObj) {
    Array.from($(".menu-box-horizontal > div")).forEach(function (elem) {
        elem.setAttribute("selected", "false");
    });

    thisObj.setAttribute("selected", "true");
}

function loadAppointmentConcreteTab(tabName, idAccount) {
    console.log("load concrete tab")
    $.ajax({
        type: "get",
        url: "/chikaboom/personality/" + idAccount + "/appointment/" + tabName,
        contentType: "application/text",
        dataType: "text",
        success: function (data) {
            setCurrentTabName(tabName);
            console.log("Load appointment tab: " + tabName);
            $("#appointment-tab-placeholder").html(data);
        },
        error: function () {
            loadUnderConstruction();
        }
    })
}

function loadSettingTab(tabName, idAccount) {
    $.ajax({
        type: "get",
        url: "/chikaboom/personality/" + idAccount + "/settings/" + tabName,
        contentType: "application/text",
        dataType: "text",
        success: function (data) {
            setCurrentTabName(tabName);
            console.log("Load setting tab " + tabName);
            $("#setting-content-placeholder").html(data);
        },
        error: function () {
            loadUnderConstruction();
        }
    });
}

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
    addField("Новый пароль", "password", "password", "*****", false, [new Validation("Поле не может быть пустым", InvalidReason.EMPTY)]);
    addField("Подтвердите новый пароль", "confirmNewPassword", "password", "*****", false, [new Validation("Поле не может быть пустым", InvalidReason.EMPTY)]);
    openPopup("edit-popup");
}

function fillGeneralSettingTab(idAccount) {
    $.ajax({
        method: "get",
        url: "/accounts/" + idAccount,
        contentType: "application/json",
        dataType: "json",
        success: function (accountJson) {

            var accountData = accountJson;

            $("#email-placeholder")[0].innerText = accountData.email;
            $("#phone-placeholder")[0].innerText = "+" + accountData.phoneCode.phoneCode + " " + accountData.phone;
        },
        error: function (data) {
            repairDefaultMessagePopup();
            $("#popup-message-text")[0].innerText = "При загрузке страницы что-то пошло не так!"
            $("#popup-message-header")[0].innerText = "Что-то пошло не так!";
            openPopup('message-popup');
        }
    })
}

function loadUnderConstruction() {
    $.ajax({
        type: "get",
        url: "/chikaboom/under_construction",
        contentType: "application/text",
        dataType: "text",
        data: {},
        success: function (data) {
            $("#setting-content-placeholder").html(data);
        },
        error: function () {
            console.error("ERROR")
        }
    });
}
