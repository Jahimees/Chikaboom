{
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
                $("#appointment-tab-placeholder").html(data);
            },
            error: function () {
                callMessagePopup("Ошибка!", "Невозможно загрузить информацию о записях!");
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
                $("#setting-content-placeholder").html(data);
            },
            error: function () {
                callMessagePopup("Ошибка!", "Невозможно загрузить информацию о настройках!")
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
            }
        );
    }

//////////////////////////////////SETTING TAB//////////////////////////////////////////////
    function openEditEmailPopup() {
        dropAllFields();
        addField("Электронная почта", "email", "text", "example@gmail.com",false,
            [new Validation("Неверный формат электронной почты", InvalidReason.EMAIL)]);
        $("#editModal").modal('show');
    }

    function openPhoneEditPopup() {
        dropAllFields();
        addField("Номер телефона", "phone", "text", null,
            true,
            [new Validation("Неверный шаблон телефона", InvalidReason.PHONE),
                new Validation("Поле не может быть пустым", InvalidReason.EMPTY),
                new Validation("Неверный формат телефона", InvalidReason.PHONE)],
            'input', 'userDetailsFacade');
        $("#editModal").modal('show');
    }

    function openPasswordEditPopup() {
        dropAllFields();
        addField("Старый пароль", "oldPassword", "password", "*****",
            false, [new Validation("Поле не может быть пустым", InvalidReason.EMPTY)]);
        addField("Новый пароль", "password", "password", "*****",
            false, [new Validation("Поле не может быть пустым", InvalidReason.EMPTY)]);
        addField("Подтвердите новый пароль", "confirmNewPassword", "password",
            "*****", false,
            [new Validation("Поле не может быть пустым", InvalidReason.EMPTY)]);
        $("#editModal").modal('show');
    }

    function openEditUsernamePopup() {
        dropAllFields();
        addField("Имя пользователя", "username", "text", "Rosalline", false,
            [new Validation("Поле не может быть пустым", InvalidReason.EMPTY),
                new Validation("Имя слишком короткое", InvalidReason.SHORT),
                new Validation("Можно использовать только латинские буквы и цифры", InvalidReason.USERNAME)]);
        $("#editModal").modal('show');
    }

    function openEditDefaultWorkingTimePopup() {
        dropAllFields();
        addField("Начало рабочего дня", "defaultWorkingDayStart", "text", "9:00", false,
            [new Validation("Время должно соответстовать формату hh:mm", InvalidReason.TIME)]);
        addField("Конец рабочего дня", "defaultWorkingDayEnd", "text", "18:00", false,
            [new Validation("Время должно соответстовать формату hh:mm", InvalidReason.TIME)]);
        $("#confirmEditBtn").unbind()
        $("#confirmEditBtn").on("click", confirmAccountSettingsEdit);
        $("#editModal").modal('show');
    }

    function openEditAboutPopup() {
        dropAllFields();
        let professionInputField = addField("Вид деятельности", "profession", "text", "Мастер по маникюру", false,
            [new Validation("Название слишком длинное", InvalidReason.LONG)],
            "input", "aboutFacade");
        let aboutTextInputField = addField("О себе", "text", "text",
            "Напишите пару слов о себе", false, [], "textarea", "aboutFacade");
        professionInputField.value = accountFacadeJson.userDetailsFacade.aboutFacade != null ?
            accountFacadeJson.userDetailsFacade.aboutFacade.profession : "";
        aboutTextInputField.value = accountFacadeJson.userDetailsFacade.aboutFacade != null ?
            accountFacadeJson.userDetailsFacade.aboutFacade.text : "";
        $("#editModal").modal('show');
    }

    function openEditFirstAndLastNamesPopup() {
        dropAllFields();
        let firstNameInputField = addField("Имя", "firstName", "text", "Валерия", false,
            [new Validation("Название слишком длинное", InvalidReason.LONG),
                new Validation("Имя может содержать только буквы", InvalidReason.NAME)],
            "input", 'userDetailsFacade');
        let lastNameInputField = addField("Фамилия", "lastName", "text", "Лаврушкина", false,
            [new Validation("Название слишком длинное", InvalidReason.LONG),
                new Validation("Фамилия может содержать только буквы", InvalidReason.NAME)],
            "input", 'userDetailsFacade');
        if (typeof accountFacadeJson.userDetailsFacade !== "undefined") {
            firstNameInputField.value = accountFacadeJson.userDetailsFacade.firstName != null ? accountFacadeJson.userDetailsFacade.firstName : "";
            lastNameInputField.value = accountFacadeJson.userDetailsFacade.lastName != null ? accountFacadeJson.userDetailsFacade.lastName : "";
        } else {
            firstNameInputField.value = "";
            lastNameInputField.value = "";
        }
        $("#editModal").modal('show');
    }

    function openEditAddressPopup() {
        dropAllFields();
        let addressInputField = addField("Адрес", "address", "text",
            "Укажите свой адрес работы", false, [], "input");
        addressInputField.id = "address-input";

        if (typeof accountFacadeJson.address != "undefined") {
            addressInputField.value = accountFacadeJson.address;
        }

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
        $("#editModal").modal('show');
    }

    function chooseAvatarImage() {
        $("#avatar-input").click();
    }

    function fillGeneralSettingTab(idAccount) {
        if (typeof accountFacadeJson == "undefined") {
            accountFacadeJson = loadAccount(idAccount);
        }

        $("#email-placeholder").val(accountFacadeJson.email);

        let phoneText = "";
        if (accountFacadeJson.userDetailsFacade === null) {
            accountFacadeJson.userDetailsFacade = {};
        }

        if (accountFacadeJson.userDetailsFacade.phoneCodeFacade !== null && typeof
            accountFacadeJson.userDetailsFacade.phoneCodeFacade !== "undefined") {
            phoneText = accountFacadeJson.userDetailsFacade.displayedPhone !== null ?
                accountFacadeJson.userDetailsFacade.displayedPhone : " ";
        }

        $("#phone-placeholder").val(phoneText);
        $("#phone-invisible-toggle").prop("checked", accountFacadeJson.accountSettingsFacade.phoneVisible)
        $("#username-placeholder").val(accountFacadeJson.username);
        let nameText = (accountFacadeJson.userDetailsFacade.firstName ? accountFacadeJson.userDetailsFacade.firstName + " " : "") +
            (accountFacadeJson.userDetailsFacade.lastName ? accountFacadeJson.userDetailsFacade.lastName : "");
        $("#name-placeholder").val(nameText);
        $("#greeting-info-box").text("Добро пожаловать, " + (nameText ? nameText : accountFacadeJson.username) + "!");

        if (accountFacadeJson.rolesFacade.length === 2 &&
            (accountFacadeJson.rolesFacade[0].name === "ROLE_MASTER" ||
                accountFacadeJson.rolesFacade[1].name === "ROLE_MASTER")) {
            let aboutProfession = accountFacadeJson.userDetailsFacade.aboutFacade != null
            && typeof accountFacadeJson.userDetailsFacade.aboutFacade != 'undefined'
                ? accountFacadeJson.userDetailsFacade.aboutFacade.profession : "";

            let aboutText = accountFacadeJson.userDetailsFacade.aboutFacade != null
            && typeof accountFacadeJson.userDetailsFacade.aboutFacade != 'undefined' ?
                accountFacadeJson.userDetailsFacade.aboutFacade.text : "";

            $("#address-placeholder").val(accountFacadeJson.address);
            $("#about-profession-placeholder").val(aboutProfession != null ? aboutProfession : "");
            $("#about-text-placeholder").text(aboutText != null && aboutText !== "" ? aboutText : "");
        }
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
                    callMessagePopup("Фотография успешно загружена!", "Ваше новое фото профиля успешно было загружено!");
                    let $img = $(".personality-avatar-image");
                    let $small_image = $(".small-avatar-image");
                    $img.attr("src", $img.attr("src").split("?")[0] + "?" + Math.random());
                    $small_image.attr("src", $small_image.attr("src").split("?")[0] + "?" + Math.random());
                },
                400: function () {
                    callMessagePopup("Ошибка!", "Произошла ошибка! Фотографию не удалось загрузить!");
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
}