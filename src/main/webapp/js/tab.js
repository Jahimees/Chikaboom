function selectCurrentTab(thisObj) {
    Array.from($(".menu-box-horizontal > div")).forEach(function (elem) {
        elem.setAttribute("selected", "false");
    });

    thisObj.setAttribute("selected", "true");
}

function loadAppointmentConcreteTab(tabName, idAccount) {
    $.ajax({
        type: "get",
        url: "/chikaboom/personality/" + idAccount + "/appointment/" + tabName,
        contentType: "application/text",
        dataType: "text",
        success: function (data) {
            setCurrentTabName(tabName);
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
            $("#setting-content-placeholder").html(data);
        },
        error: function () {
            loadUnderConstruction();
        }
    });
}
//////////////////////////////////SETTING TAB//////////////////////////////////////////////
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

function openEditUsernamePopup() {
    dropAllFields();
    addField("Имя пользователя", "username", "text", "Rosalline", false,
        [new Validation("Поле не может быть пустым", InvalidReason.EMPTY),
            new Validation("Имя слишком короткое", InvalidReason.SHORT),
            new Validation("Можно использовать только буквы и 1 пробел между словами", InvalidReason.USERNAME)]);
    openPopup("edit-popup");
}

function openEditAboutPopup() {
    dropAllFields();
    let professionInputField = addField("Вид деятельности", "profession", "text", "Мастер по маникюру", false,
        [new Validation("Название слишком длинное", InvalidReason.LONG)], "input", "about");
    let aboutTextInputField = addField("О себе", "text", "text", "Напишите пару слов о себе", false, [], "textarea", "about");
    professionInputField.value = accountJson.about != null ? accountJson.about.profession : "";
    aboutTextInputField.value = accountJson.about != null ? accountJson.about.text : "";
    openPopup("edit-popup");
}

function openEditAddressPopup() {
    dropAllFields();
    let addressInputField = addField("Адрес", "address", "text", "Укажите свой адрес работы", false, [], "input");
    addressInputField.id = "address-input";
    $("#address-input").suggestions({
        token: "1d06cefc6ea71518b0141a136c76497406f321b2",
        type: "ADDRESS",
        geoLocation: false,
        constraints: {
            locations: {country: "*"}
        },
        onSelect: function (suggestion) {
        }
    });
    openPopup("edit-popup");
}

function chooseAvatarImage() {
    $("#avatar-input").click();
}

function fillGeneralSettingTab(idAccount) {
    $.ajax({
        method: "get",
        url: "/accounts/" + idAccount,
        contentType: "application/json",
        dataType: "json",
        success: function (accountJson) {

            var accountData = accountJson;
            $("#greeting-info-box").text("Добро пожаловать, " + accountData.username);
            $("#email-placeholder").text(accountData.email);
            $("#phone-placeholder").text("+" + accountData.phoneCode.phoneCode + " " + accountData.phone);
            $("#phone-invisible-toggle").prop("checked", accountData.phoneVisible)
            $("#username-placeholder").text(accountData.username);
            if (accountData.roles[0].name === "ROLE_MASTER") {
                let aboutProfession = accountJson.about != null && typeof accountJson.about != 'undefined' ? accountJson.about.profession : "";
                let aboutText = accountJson.about != null && typeof accountJson.about != 'undefined' ? accountJson.about.text : "";
                $("#address-placeholder").text(accountJson.address);
                $("#about-profession-placeholder").text(aboutProfession != null ? aboutProfession : "");
                $("#about-text-placeholder").text(aboutText != null && aboutText !== "" ? aboutText : "Информация о себе пуста");
            }
        },
        error: function (data) {
            repairDefaultMessagePopup();
            $("#popup-message-text").text("При загрузке страницы что-то пошло не так!");
            $("#popup-message-header").text("Что-то пошло не так!");
            openPopup('message-popup');
        }
    })
}

function uploadAvatarImage(idAccount) {
    let formData = new FormData();
    formData.append("file", $("#avatar-input")[0].files[0]);
    formData.append("fileName", "avatar.jpeg");
    if (window.FormData === undefined) {
        alert('В вашем браузере FormData не поддерживается')
    }
    $.ajax({
        method: "POST",
        dataType: "json",
        contentType: false,
        processData: false,
        async: false,
        url: "/chikaboom/upload/file/" + idAccount,
        data: formData,
        statusCode: {
            201: function () {
                $("#popup-message-text")[0].innerText = "Ваше новое фото профиля успешно было загружено!"
                $(".message-popup > .popup-title > #popup-message-header")[0].innerText = "Фотография успешно загружена!";
                let $img = $(".personality-avatar-image");
                let $small_image = $(".small-avatar-image");
                $img.attr("src", $img.attr("src").split("?")[0] + "?" + Math.random());
                $small_image.attr("src", $small_image.attr("src").split("?")[0] + "?" + Math.random());
                openPopup('message-popup');
            },
            400: function () {
                $("#popup-message-text")[0].innerText = "Произошла ошибка! Фотографию не удалось загрузить!"
                $(".message-popup > .popup-title > #popup-message-header")[0].innerText = "ОШИБКА!";
                openPopup('message-popup');
            }
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
