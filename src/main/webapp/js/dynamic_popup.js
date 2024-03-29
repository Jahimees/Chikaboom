{
    let fields = []
    const $confirmMessageBtn = $("#confirm-message-btn");
    const $confirmEditBtn = $("#confirmEditBtn");

    function repairDefaultMessagePopup() {
        $("#decline-message-btn").css("display", "none");
        $confirmMessageBtn.attr("data-bs-dismiss", "modal");
        $confirmMessageBtn.attr("onclick", "");
    }

    function callMessagePopup(title, text) {
        repairDefaultMessagePopup();
        $("#messageModalText").text(text);
        $("#messageModalLabel").text(title);
        $("#messageModal").modal('show');
    }

    /**
     * При нажатии на кнопку "Подтвердить" в всплывающем окне редактирования происходит валидация полей, а затем
     * отправка данных на сервер для изменения их в базе данных
     */
    function confirmEdit() {
        if (validateAllFields()) {
            const url = "/accounts/" + accountFacadeJson.idAccount;

            const accountJsonPatch = {
                userDetailsFacade: {}
            }

            Array.from($(".popup-input-field")).forEach(field => {
                let nestedObjectType = field.getAttribute("nestedObjectType");
                let fieldValue = field.value !== null ? field.value : "";

                if (nestedObjectType != null && nestedObjectType !== "") {
                    if (nestedObjectType === 'userDetailsFacade') {
                        accountJsonPatch.userDetailsFacade[field.name] = fieldValue;

                        if (field.id === "edit-phone") {
                            const countryData = window.intlTelInputGlobals.getInstance(
                                $("#edit-phone")[0].getSelectedCountryData());
                            accountJsonPatch.userDetailsFacade["phoneCodeFacade"] = {
                                phoneCode: countryData.dialCode,
                                countryCut: countryData.iso2
                            };
                        }
                    } else {
                        if (typeof accountJsonPatch.userDetailsFacade[nestedObjectType] == "undefined") {
                            accountJsonPatch.userDetailsFacade[nestedObjectType] = {};
                        }
                        accountJsonPatch.userDetailsFacade[nestedObjectType][field.name] = fieldValue;
                    }
                } else {
                    accountJsonPatch[field.name] = fieldValue;
                }
            });

            $.ajax({
                type: "PATCH",
                url: url,
                contentType: "application/json",
                dataType: "json",
                data: JSON.stringify(accountJsonPatch),
                success: function (data) {
                    console.log("Endpoint 8 done::: ");
                    $("#editModal").modal('hide');
                    callMessagePopup("Изменения прошли успешно!", "Изменения прошли успешно!")

                    accountFacadeJson = data;

                    loadSettingTab('general', accountFacadeJson.idAccount);
                    countPercentage();
                },
                error: function () {
                    $("#e-input-data-incorrect-label").css("display", "block");
                }
            });
        }
    }

    function confirmAccountSettingsEdit() {
        if (validateAllFields()) {
            const url = "/accounts/" + accountFacadeJson.idAccount + "/settings";

            let accountSettingsJson = {
                idAccountSettings: accountFacadeJson.accountSettingsFacade.idAccountSettings,
            }
            Array.from($(".popup-input-field")).forEach(field => {
                if (field.name === 'defaultWorkingDayStart' || field.name === 'defaultWorkingDayEnd') {
                    if (field.value.trim() !== '') {
                        accountSettingsJson[field.name] = new Date("2000-09-09 " + field.value.trim() + ":00")
                            .toLocaleTimeString('ru');
                    }
                }
            });

            $.ajax({
                type: "PATCH",
                url: url,
                contentType: "application/json",
                dataType: "json",
                data: JSON.stringify(accountSettingsJson),
                async: false,
                success: function (data) {
                    console.log("Endpoint 9 done::: ");
                    $("#editModal").modal('hide');
                    callMessagePopup("Изменения прошли успешно!", "Изменения прошли успешно!")

                    accountFacadeJson.accountSettingsFacade = data;

                    loadSettingTab('personalization', accountFacadeJson.idAccount);
                },
                error: function () {
                    $("#e-input-data-incorrect-label").css("display", "block");
                }
            });
        }
    }

    /**
     * Уничтожает все поля в всплывающем окне
     */
    function dropAllFields() {
        $confirmEditBtn.unbind();
        $confirmEditBtn.on("click", confirmEdit);

        $("#field-box-placeholder").html("");
        fields = [];
    }

    /**
     * Добавляет новое поле на всплывающее окно
     *
     * @param labelText надпись над полем
     * @param fieldName параметр name поля
     * @param inputType тип поля
     * @param placeHolderText текст в placeholder
     * @param isPhoneCode флаг, является ли поле номером телефона
     * @param validations массив правил валидации поля
     */
    function addField(labelText, fieldName, inputType, placeHolderText, isPhoneCode, validations, fieldType, nestedObjectType) {
        const divLabel = $("<div class='common-black-text'></div>").text(labelText)

        const inputField = document.createElement(fieldType ? fieldType : "input");
        if (isPhoneCode) {
            inputField.id = "edit-phone";
        }

        inputField.setAttribute("class", "popup-input-field");
        if (nestedObjectType != null && nestedObjectType !== "") {
            inputField.setAttribute("nestedObjectType", nestedObjectType);
        }

        inputField.type = inputType;
        inputField.name = fieldName;
        inputField.placeholder = placeHolderText ? placeHolderText : '';

        const fieldPlaceholder = $("#field-box-placeholder")
        fieldPlaceholder.append(divLabel);
        if (typeof (validations) !== "undefined") {
            validations.forEach(function (validation) {
                let invalidLabel = $("<label class='invalid-field-label-popup'></label>")
                invalidLabel.text(validation.invalidMessage)
                    .attr("display", "none")
                    .attr("id", inputField.name + "-" + validation.invalidReason)
                    .attr("reason", validation.invalidReason);

                fieldPlaceholder.append(invalidLabel);
            });
            inputField.setAttribute("valid", false);
            inputField.setAttribute("onKeyup", "validateField(this)");
        }
        fieldPlaceholder.append(inputField);

        fields.push(inputField);

        if (isPhoneCode) {
            initPhoneCodeWidget("edit-phone");
        }

        return inputField;
    }

    /**
     * Валидация всех полей на всплывающем окне
     * @returns {boolean}
     */
    function validateAllFields() {
        let flag = true;
        for (let field of fields) {
            if (field.getAttribute("valid") === 'false') {
                if (!validateField(field)) {
                    flag = false;
                }
            }
        }

        return flag
    }

    let temp;

    /**
     * Валидация конкретного поля
     *
     * @param thisField конкретное поле, которое должно быть проверено
     */
    function validateField(thisField) {
        const thisFieldName = thisField.name;
        const invalidLabels = $("label[id|='" + thisFieldName + "']");
        let isReasonShouldBeEmpty = true;
        for (let label of invalidLabels) {
            switch (label.getAttribute("reason")) {
                case InvalidReason.EMPTY: {
                    if (thisField.value.trim().length === 0) {
                        thisField.setAttribute("reason", InvalidReason.EMPTY)
                        $("#" + thisFieldName + "-" + InvalidReason.EMPTY).css("display", "block");
                        isReasonShouldBeEmpty = false;
                    } else {
                        $("#" + thisFieldName + "-" + InvalidReason.EMPTY).css("display", "none");
                    }
                    break;
                }
                case InvalidReason.SHORT: {
                    if (thisField.value.trim().length <= 4) {
                        thisField.setAttribute("reason", InvalidReason.SHORT)
                        $("#" + thisFieldName + "-" + InvalidReason.SHORT).css("display", "block");
                        isReasonShouldBeEmpty = false;
                    } else {
                        $("#" + thisFieldName + "-" + InvalidReason.SHORT).css("display", "none");
                    }
                    break;
                }
                case InvalidReason.EMAIL: {
                    if (!/^([a-z0-9_-]+\.)*[a-z0-9_-]+@[a-z0-9_-]+(\.[a-z0-9_-]+)*\.[a-z]{2,6}$/.test(thisField.value)) {
                        thisField.setAttribute("reason", InvalidReason.EMAIL)
                        $("#" + thisFieldName + "-" + InvalidReason.EMAIL).css("display", "block");
                        isReasonShouldBeEmpty = false;
                    } else {
                        $("#" + thisFieldName + "-" + InvalidReason.EMAIL).css("display", "none");
                    }
                    break;
                }
                case InvalidReason.PHONE: {
                    if (!window.intlTelInputGlobals.getInstance(
                        document.querySelector("#edit-phone")).isValidNumber()) {
                        thisField.setAttribute("reason", InvalidReason.PHONE)
                        $("#" + thisFieldName + "-" + InvalidReason.PHONE).css("display", "block");
                        isReasonShouldBeEmpty = false;
                    } else {
                        $("#" + thisFieldName + "-" + InvalidReason.PHONE).css("display", "none");
                    }
                    break;
                }
                case InvalidReason.LONG: {
                    temp = thisField;
                    if (thisField.value.trim().length > 30) {
                        thisField.setAttribute("reason", InvalidReason.LONG)
                        $("#" + thisFieldName + "-" + InvalidReason.LONG).css("display", "block");
                        isReasonShouldBeEmpty = false;
                    } else {
                        $("#" + thisFieldName + "-" + InvalidReason.LONG).css("display", "none");
                    }
                    break;
                }
                case InvalidReason.USERNAME: {
                    if (!/^[a-zA-Z0-9]+$/.test(thisField.value)) {
                        thisField.setAttribute("reason", InvalidReason.USERNAME)
                        $("#" + thisFieldName + "-" + InvalidReason.USERNAME).css("display", "block");
                        isReasonShouldBeEmpty = false;
                    } else {
                        $("#" + thisFieldName + "-" + InvalidReason.USERNAME).css("display", "none");
                    }
                    break;
                }
                case InvalidReason.NAME: {
                    if (!/^[a-zA-Zа-яА-Я]+$/.test(thisField.value)) {
                        thisField.setAttribute("reason", InvalidReason.NAME)
                        $("#" + thisFieldName + "-" + InvalidReason.NAME).css("display", "block");
                        isReasonShouldBeEmpty = false;
                    } else {
                        $("#" + thisFieldName + "-" + InvalidReason.NAME).css("display", "none");
                    }
                    break;
                }
                case InvalidReason.TIME: {
                    if (!/^(?:\d|[01]\d|2[0-3]):[0-5]\d$/.test(thisField.value) && thisField.value.trim() !== '') {
                        thisField.setAttribute("reason", InvalidReason.TIME);
                        $("#" + thisFieldName + "-" + InvalidReason.TIME).css("display", "block");
                        isReasonShouldBeEmpty = false;
                    } else {
                        $("#" + thisFieldName + "-" + InvalidReason.TIME).css("display", "none");
                    }
                }
            }
        }

        if (isReasonShouldBeEmpty) {
            thisField.setAttribute("reason", "");
        }

        thisField.setAttribute("valid", thisField.getAttribute("reason") === ""
            || thisField.getAttribute("reason") === null);

        return thisField.getAttribute("reason") === ""
            || thisField.getAttribute("reason") === null;
    }
}

class Validation {
    constructor(invalidMessage, InvalidReason) {
        this.invalidMessage = invalidMessage;
        this.invalidReason = InvalidReason;
    }

}

const InvalidReason = {
    PHONE: "phone",
    EMAIL: "email",
    EMPTY: "empty",
    SHORT: "short",
    LONG: "long",
    USERNAME: "username",
    NAME: "name",
    TIME: "time"
}