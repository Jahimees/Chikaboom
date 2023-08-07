<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<div class="content">

    <div class="row w-100 input-box">
        <div class="col-4 common-black-text">
            Фотография профиля:
        </div>
        <div class="col-4 common-text placeholder">
            <input id="avatar-input" type="file" accept="image/jpeg" name="file">
        </div>
        <div class="col-1 edit-button">
            <input onclick="uploadAvatarImage()" style="font-size: 20px" type="submit" value="Загрузить">
        </div>

    </div>
    <hr>
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
    <sec:authorize access="hasRole('ROLE_MASTER')">
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
    </sec:authorize>
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
                locations: {country: "*"}
            },
            onSelect: function (suggestion) {
                console.log(suggestion);
            }
        });
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
                console.log(suggestion);
            }
        });
        openPopup("edit-popup");
    }

    function uploadAvatarImage() {
        let formData = new FormData();
        formData.append("file", $("#avatar-input")[0].files[0]);
        formData.append("fileName", "avatar.jpeg");
        if (window.FormData === undefined) {
            alert('В вашем браузере FormData не поддерживается')
        }
        console.log("uploading data " + ${idAccount});
        $.ajax({
            method: "POST",
            dataType: "json",
            contentType: false,
            processData: false,
            url: "/chikaboom/upload/file/${idAccount}",
            data: formData,
            statusCode: {
                201: function () {
                    $("#popup-message-text")[0].innerText = "Ваше новое фото профиля успешно было загружено!"
                    $(".message-popup > .popup-title > #popup-message-header")[0].innerText = "Фотография успешно загружена!";
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

    $(document).ready(function () {
        let aboutProfession;
        let aboutText;

        if (accountJson.roles[0].name === "ROLE_MASTER") {
            aboutProfession = accountJson.about != null && typeof accountJson.about != 'undefined' ? accountJson.about.profession : "";
            aboutText = accountJson.about != null && typeof accountJson.about != 'undefined' ? accountJson.about.text : "";
            $("#address-placeholder")[0].innerText = accountJson.address;
            $("#about-text-placeholder")[0].innerText = aboutProfession != null ? aboutProfession : "" + "\n" + aboutText != null ? aboutText : "";
        }

        $("#nickname-placeholder")[0].innerText = accountJson.nickname;
    });
</script>
