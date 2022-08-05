<%@ page contentType="text/html;charset=UTF-8" %>

<div class="popup-bg"></div>

<div class="popup edit-popup">
    <div class="close-popup" onclick="closePopup('edit-popup')" tabindex="1">
        X
    </div>
    <div class="popup-title">
        <h3 id="popup-message-header">Редактирование</h3>
    </div>
    <div class="popup-body">
        <div id="field-box-placeholder">
            <div class="common-black-text">
                Имя
            </div>
            <input class="popup-input-field" type="text">
        </div>
        <div class="button-box">
            <div class="popup-btn" onclick="closePopup('edit-popup')">
                Подтвердить
            </div>
            <div class="popup-btn" onclick="closePopup('edit-popup')">
                Отменить
            </div>
        </div>
    </div>
</div>

<script>
    function dropAllFields() {
        $("#field-box-placeholder").html("");
        countryCache = null;
        countryRequesting = false;
    }

    function addField(labelText, fieldName, inputType, placeHolderText, isPhoneCode) {
        let divLabel = document.createElement("div");
        divLabel.setAttribute("class", "common-black-text");
        divLabel.innerHTML = labelText;

        let inputField = document.createElement("input");
        inputField.setAttribute("class", "popup-input-field");
        inputField.type = inputType;
        inputField.name = fieldName;
        inputField.placeholder = placeHolderText ? placeHolderText : '';

        if (isPhoneCode) {
            inputField.id = "edit-phone";
        }

        let fieldPlaceholder = $("#field-box-placeholder")[0]
        fieldPlaceholder.appendChild(divLabel);
        fieldPlaceholder.appendChild(inputField);

        if (isPhoneCode) {
            $(function () {
                $('#edit-phone').phonecode({
                    preferCo: 'by',
                    id: 'edit'
                });
            });
        }
    }


</script>

<script rel="script" src="/js/jquery-ui-1.10.4.custom.min.js"></script>

<script src="/js/countries.js"></script>
<script src="/js/phonecode.js"></script>