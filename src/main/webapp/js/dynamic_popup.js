let fields = []
let currentTabName = "general";

function setCurrentTabName(newTabName) {
    currentTabName = newTabName;
}

function openPopup(popupName) {
    $('.popup-bg').fadeIn(200);
    $('.' + popupName).fadeIn(200);
}

function repairDefaultMessagePopup() {
    $("#decline-message-btn")[0].style.display = "none";
    $("#confirm-message-btn")[0].setAttribute("onclick", "closePopup('message-popup')");
}

function closePopup(popupName) {
    $('.popup-bg').fadeOut(200);
    $('.' + popupName).fadeOut(200);
}

/**
 * При нажатии на кнопку "Подтвердить" в всплывающем окне редактирования происходит валидация полей, а затем
 * отправка данных на сервер для изменения их в базе данных
 */
function confirmEdit() {
    if (validateAllFields()) {
        let url = "/accounts/" + accountJson.idAccount;

        let accountJsonPatch = {
            userDetails: {}
        }

        Array.from($(".popup-input-field")).forEach(field => {
            let nestedObjectType = field.getAttribute("nestedObjectType");
            let fieldValue = field.value !== null ? field.value : "";

            if (nestedObjectType != null && nestedObjectType !== "") {
                if (nestedObjectType === 'userDetails') {
                    accountJsonPatch.userDetails[field.name] = fieldValue;

                    if (field.id === "edit-phone") {
                        let countryData = window.intlTelInputGlobals.getInstance(
                            document.querySelector("#edit-phone")).getSelectedCountryData();
                        accountJsonPatch.userDetails["phoneCode"] = {
                            phoneCode: countryData.dialCode,
                            countryCut: countryData.iso2
                        };
                    }
                } else {
                    if (accountJsonPatch.userDetails[nestedObjectType] == null) {
                        accountJsonPatch.userDetails[nestedObjectType] = {};
                    }
                    accountJsonPatch.userDetails[nestedObjectType][field.name] = fieldValue;
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
                closePopup('edit-popup');
                repairDefaultMessagePopup();
                $("#popup-message-text")[0].innerText = "Изменения прошли успешно!"
                $(".message-popup > .popup-title > #popup-message-header")[0].innerText = "Изменения прошли успешно!";
                openPopup('message-popup');

                accountJson = data;

                loadSettingTab(currentTabName, accountJson.idAccount);
                countPercentage();
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
    $("#field-box-placeholder").html("");
    countryCache = null;
    countryRequesting = false;
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
    let divLabel = document.createElement("div");
    divLabel.setAttribute("class", "common-black-text");
    divLabel.innerText = labelText;

    let inputField = document.createElement(fieldType ? fieldType : "input");
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

    let fieldPlaceholder = $("#field-box-placeholder")[0]
    fieldPlaceholder.appendChild(divLabel);
    if (typeof (validations) !== "undefined") {
        validations.forEach(function (validation) {
            let invalidLabel = document.createElement("label");
            invalidLabel.setAttribute("class", "invalid-field-label-popup");
            invalidLabel.innerText = validation.invalidMessage;
            invalidLabel.setAttribute("display", "none");
            invalidLabel.setAttribute("id", inputField.name + "-" + validation.invalidReason);
            invalidLabel.setAttribute("reason", validation.invalidReason);
            fieldPlaceholder.appendChild(invalidLabel);
        });
        inputField.setAttribute("valid", false);
        inputField.setAttribute("onKeyup", "validateField(this)");
    }
    fieldPlaceholder.appendChild(inputField);

    fields.push(inputField);

    if (isPhoneCode) {
        let span = document.createElement('span');
        span.setAttribute("id", "error-msg-edit-phone");
        span.classList.add("hidden");
        inputField.after(span);
        initPhoneCodeWidget("edit-phone");
    }

    return inputField;
}

const InvalidReason = {
    PHONE: "phone",
    EMAIL: "email",
    EMPTY: "empty",
    SHORT: "short",
    LONG: "long",
    USERNAME: "username",
    NAME: "name"
}

class Validation {
    constructor(invalidMessage, InvalidReason) {
        this.invalidMessage = invalidMessage;
        this.invalidReason = InvalidReason;
    }

}

/**
 * Валидация всех полей на всплывающем окне
 * @returns {boolean}
 */
function validateAllFields() {
    var flag = true;
    for (var field of fields) {
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
    let thisFieldName = thisField.name;
    let invalidLabels = $("label[id|='" + thisFieldName + "']");
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
            // case InvalidReason.PHONE: {
            //     if (!/^(\s*)?([- _():=+]??\d[- _():=+]?){9,14}(\s*)?$/.test(thisField.value)) {
            //         thisField.setAttribute("reason", InvalidReason.PHONE)
            //         $("#" + thisFieldName + "-" + InvalidReason.PHONE).css("display", "block");
            //         isReasonShouldBeEmpty = false;
            //     } else {
            //         $("#" + thisFieldName + "-" + InvalidReason.PHONE).css("display", "none");
            //     }
            //     break;
            // }
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
