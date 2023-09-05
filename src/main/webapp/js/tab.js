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
            repairDefaultMessagePopup();
            $("#popup-message-text")[0].innerText = "Невозможно загрузить информацию о записях!"
            $(".message-popup > .popup-title > #popup-message-header")[0].innerText = "ОШИБКА!";
            openPopup('message-popup');
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
            repairDefaultMessagePopup();
            $("#popup-message-text")[0].innerText = "Невозможно загрузить информацию о настройках!"
            $(".message-popup > .popup-title > #popup-message-header")[0].innerText = "ОШИБКА!";
            openPopup('message-popup');
        }
    });
}

///////////////////////////////////DATATABLE///////////////////////////////////////////////

function initDataTable(tableId) {
    let tableName = tableId ? tableId : "default";

    new DataTable('#' + tableName + "_table", {
        order: [[1, 'asc'], [2, 'asc']],
        "language": {
            "decimal": "",
            "emptyTable": "Информации не найдено",
            "info": "Показана страница _PAGE_ из _PAGES_",
            "infoEmpty": "Данные не найдены",
            "infoFiltered": "(отфильтровано из _MAX_ всех возможных данных)",
            "infoPostFix": "",
            "thousands": ",",
            "lengthMenu": "Показывать _MENU_ на одной странице",
            "loadingRecords": "Загрузка...",
            "processing": "",
            "search": "Поиск:",
            "zeroRecords": "Совпадений не найдено",
            "paginate": {
                "first": "Первая",
                "last": "Последняя",
                "next": "Следующая",
                "previous": "Предыдущая"
            },
            "aria": {
                "sortAscending": ": активировать сортировку по возрастанию",
                "sortDescending": ": активировать сортировку по убыванию"
            }
        }
    })

    if (tableId === "appointment") {
        initFilter();
    }
}

function initFilter() {
    $.fn.dataTable.ext.search.push(
        function (settings, data, dataIndex) {
            let $pastAppointmentToggle = $("#past-appointment-toggle");

            let appDateArr = data[1].split(".");
            let appointmentDate = new Date(appDateArr[2], appDateArr[1] - 1, appDateArr[0]);
            let appTimeArr = data[2].split(":");
            appointmentDate.setHours(appTimeArr[0]);
            appointmentDate.setMinutes(appTimeArr[1]);

            if ($pastAppointmentToggle.prop("checked")) {
                return true;
            } else {
                if (appointmentDate.getTime() <= new Date().getTime()) {
                    return false;
                }
            }
            return true;
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
    addField("Номер телефона", "phone", "text", null, true, [new Validation("Неверный шаблон телефона", InvalidReason.PHONE),
        new Validation("Поле не может быть пустым", InvalidReason.EMPTY)], 'input', 'userDetails');
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
            new Validation("Можно использовать только латинские буквы и цифры", InvalidReason.USERNAME)]);
    openPopup("edit-popup");
}

function openEditAboutPopup() {
    dropAllFields();
    let professionInputField = addField("Вид деятельности", "profession", "text", "Мастер по маникюру", false,
        [new Validation("Название слишком длинное", InvalidReason.LONG)], "input", "about");
    let aboutTextInputField = addField("О себе", "text", "text", "Напишите пару слов о себе", false, [], "textarea", "about");
    professionInputField.value = accountJson.userDetails.about != null ? accountJson.userDetails.about.profession : "";
    aboutTextInputField.value = accountJson.userDetails.about != null ? accountJson.userDetails.about.text : "";
    openPopup("edit-popup");
}

function openEditFirstAndLastNamesPopup() {
    dropAllFields();
    let firstNameInputField = addField("Имя", "firstName", "text", "Валерия", false,
        [new Validation("Название слишком длинное", InvalidReason.LONG),
            new Validation("Имя может содержать только буквы", InvalidReason.NAME)], "input", 'userDetails');
    let lastNameInputField = addField("Фамилия", "lastName", "text", "Лаврушкина", false,
        [new Validation("Название слишком длинное", InvalidReason.LONG),
            new Validation("Фамилия может содержать только буквы", InvalidReason.NAME)], "input", 'userDetails');
    if (accountJson.userDetails !== null) {
        firstNameInputField.value = accountJson.userDetails.firstName != null ? accountJson.userDetails.firstName : "";
        lastNameInputField.value = accountJson.userDetails.lastName != null ? accountJson.userDetails.lastName : "";
    } else {
        firstNameInputField.value = "";
        lastNameInputField.value = "";
    }
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
        async: false,
        success: function (accountJson) {

            $("#email-placeholder").val(accountJson.email);

            let phoneText = "";
            if (accountJson.userDetails === null) {
                accountJson.userDetails = {};
            }

            if (accountJson.userDetails.phoneCode !== null && typeof accountJson.userDetails.phoneCode !== "undefined") {
                phoneText = accountJson.userDetails.phone !== null ? accountJson.userDetails.phone : " ";
            }

            $("#phone-placeholder").val(phoneText);
            $("#phone-invisible-toggle").prop("checked", accountJson.phoneVisible)
            $("#username-placeholder").val(accountJson.username);
            let nameText = (accountJson.userDetails.firstName ? accountJson.userDetails.firstName + " " : "") +
                (accountJson.userDetails.lastName ? accountJson.userDetails.lastName : "");
            $("#name-placeholder").val(nameText);
            $("#greeting-info-box").text("Добро пожаловать, " + (nameText ? nameText : accountJson.username) + "!");

            if (accountJson.roles[0].name === "ROLE_MASTER") {
                let aboutProfession = accountJson.userDetails.about != null
                && typeof accountJson.userDetails.about != 'undefined'
                    ? accountJson.userDetails.about.profession : "";

                let aboutText = accountJson.userDetails.about != null
                && typeof accountJson.userDetails.about != 'undefined' ? accountJson.userDetails.about.text : "";

                $("#address-placeholder").val(accountJson.address);
                $("#about-profession-placeholder").val(aboutProfession != null ? aboutProfession : "");
                $("#about-text-placeholder").text(aboutText != null && aboutText !== "" ? aboutText : "");
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
