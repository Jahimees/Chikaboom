<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="content">
    <div class="row w-100 input-box">
        <div class="col-4 common-black-text">
            Имя пользователя:
        </div>
        <div id="nickname-placeholder" class="col-4 common-text placeholder">
        </div>
        <div id="change-nickname-btn" onclick="openEditNicknamePopup()" class="col-1 edit-button">
            <img src="/image/icon/edit_icon.svg">
        </div>
    </div>
    <hr>
    <div class="row w-100 input-box">
        <div class="col-4 common-black-text">
            Адрес:
        </div>
        <div id="address-placeholder" class="col-4 common-text placeholder">
        </div>
        <div id="change-address-btn" onclick="openEditAddressPopup()" class="col-1 edit-button">
            <img src="/image/icon/edit_icon.svg">
        </div>
    </div>
    <hr>
    <div class="row w-100 input-box">
        <div class="col-4 common-black-text">
            О себе:
        </div>
        <div id="about-text-placeholder" class="col-4 common-text placeholder">
        </div>
        <div id="change-about-text-btn" onclick="openEditAboutPopup()" class="col-1 edit-button">
            <img src="/image/icon/edit_icon.svg">
        </div>
    </div>

</div>

<script>
    function openEditNicknamePopup() {
        dropAllFields();
        addField("Имя пользователя", "nickname", "text", "Rosalline", false,
            [new Validation("Поле не может быть пустым", InvalidReason.EMPTY),
                new Validation("Имя слишком короткое", InvalidReason.SHORT)]);
        openPopup("edit-popup");
    }

    function openEditAddressPopup() {
        dropAllFields();
        let addressInputField = addField("Адрес", "address", "text", "Укажите свой адрес работы", false, [], "input");
        addressInputField.id = "address-input";
        $("#address-input").suggestions({
            token: "token",
            type: "ADDRESS",
            geoLocation: false,
            constraints: {
                locations: { country: "*" }
            },
            onSelect: function(suggestion) {
                console.log(suggestion);
            }
        });
        openPopup("edit-popup");
    }

    function openEditAboutPopup() {
        dropAllFields();
        let professionInputField = addField("Вид деятельности", "profession", "text", "Мастер по маникюру", false,
            [new Validation("Название слишком длинное", InvalidReason.LONG)]);
        let aboutTextInputField = addField("О себе", "aboutText", "text", "Напишите пару слов о себе", false, [], "textarea");
        professionInputField.value = aboutJson.profession;
        aboutTextInputField.value = aboutJson.text;
        openPopup("edit-popup");
    }

    $(document).ready(function () {
        $("#nickname-placeholder")[0].innerText = accountJson.nickname;
        $("#address-placeholder")[0].innerText = addressJson.address;
        $("#about-text-placeholder")[0].innerText = aboutJson.profession + "\n" + aboutJson.text;
    });
</script>
