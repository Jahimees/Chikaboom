<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<div class="content">
    <div id="first-row">
        <div class="input-box">
            <div class="common-black-text padding-0-0-20-px">
                Фото аккаунта
            </div>
            <input id="avatar-input" type="file" accept="image/jpeg" name="file" hidden>
            <div class="photo-placeholder">
                <img class="personality-avatar-image" src="/image/user/${idAccount}/avatar.jpeg"
                     onerror="this.src='/image/user/no_photo.jpg'" alt="error on load" onclick="chooseAvatarImage()">
            </div>
        </div>
        <div class="input-box">
            <div class="common-black-text padding-0-0-20-px">
                Личная информация
            </div>

            <div class="small-pink-text">
                Имя пользователя:
            </div>
            <div id="username-placeholder" onclick="openEditUsernamePopup()"
                 class="small-black-text bordered-text setting-placeholder-text-decorator">
            </div>
            <sec:authorize access="hasRole('ROLE_MASTER')">
                <div class="small-pink-text">
                    Адрес:
                </div>
                <div id="address-placeholder" onclick="openEditAddressPopup()"
                     class="small-black-text bordered-text setting-placeholder-text-decorator">
                </div>
            </sec:authorize>
        </div>
    </div>
    <div id="second-row">
        <div class="input-box">
            <div class="common-black-text padding-0-0-20-px">
                Данные аккаунта
            </div>

            <div class="small-pink-text">
                Электронная почта:
            </div>
            <div id="email-placeholder" onclick="openEditEmailPopup()" class="
            small-black-text bordered-text setting-placeholder-text-decorator">
            </div>

            <div class="small-pink-text">
                Номер телефона:
            </div>
            <div id="phone-placeholder" onclick="openPhoneEditPopup()" class="
        small-black-text bordered-text setting-placeholder-text-decorator">
            </div>
            <div class="form-check form-switch padding-0-0-20-px">
                <input class="form-check-input" type="checkbox" id="phone-invisible-toggle" checked>
                <label class="form-check-label small-black-text" for="phone-invisible-toggle">Отображать в профиле</label>
            </div>

            <div class="small-pink-text">
                Пароль:
            </div>
            <div id="password-placeholder" onclick="openPasswordEditPopup()" class="
            small-black-text bordered-text setting-placeholder-text-decorator">
                ********************
            </div>
        </div>
        <sec:authorize access="hasRole('ROLE_MASTER')">
            <div class="input-box">
                <div class="common-black-text padding-0-0-20-px">
                    Информация о себе
                </div>

                <div class="small-pink-text">
                    Вид деятельности:
                </div>
                <div id="about-profession-placeholder" onclick="openEditAboutPopup()" class="
            small-black-text bordered-text setting-placeholder-text-decorator">
                </div>
                <div class="small-pink-text">
                    О себе:
                </div>
                <div id="about-text-placeholder" onclick="openEditAboutPopup()" class="
            small-black-text bordered-text setting-placeholder-text-decorator">
                </div>
            </div>
        </sec:authorize>
    </div>
</div>

<script rel="script" src="/js/jquery-ui-1.10.4.custom.min.js"></script>

<script src="/js/phonecode/countries.js"></script>
<script src="/js/phonecode/phonecode.js"></script>

<script>
    $(document).ready(function () {
        fillGeneralSettingTab(${idAccount});
    });

    $("#avatar-input").on("change", function () {
        uploadAvatarImage(${idAccount});
    })

    $("#phone-invisible-toggle").on("click", function () {
        let isPhoneVisible = $("#phone-invisible-toggle").prop("checked");

        $.ajax({
            type: "PATCH",
            url: "/accounts/${idAccount}",
            contentType: "application/json",
            dataType: "json",
            data: JSON.stringify({
                phoneVisible: isPhoneVisible
            }),
            error: function () {
                repairDefaultMessagePopup();
                $("#popup-message-text")[0].innerText = "Что-то пошло не так! Невозможно установить видимость телефона"
                $(".message-popup > .popup-title > #popup-message-header")[0].innerText = "Проблема на сервере!";
                openPopup('message-popup');
            }
        })
    })
</script>