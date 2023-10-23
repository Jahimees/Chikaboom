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
                     onerror="this.src='../../../image/user/no_photo.jpg'" alt="error on load" onclick="chooseAvatarImage()">
            </div>
        </div>
        <div class="input-box">
            <div class="common-black-text padding-0-0-20-px">
                Личная информация
            </div>

            <div class="small-pink-text">
                Имя пользователя:
            </div>
            <input id="username-placeholder" onclick="openEditUsernamePopup()"
                   class="small-black-text bordered-text setting-placeholder-text-decorator"
                   placeholder="Ваше имя пользователя" readonly>
            <sec:authorize access="hasRole('ROLE_MASTER')">
                <div class="small-pink-text">
                    Адрес:
                </div>
                <input id="address-placeholder" onclick="openEditAddressPopup()"
                       class="small-black-text bordered-text setting-placeholder-text-decorator"
                       placeholder="Адрес работы" readonly>
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
            <input id="email-placeholder" onclick="openEditEmailPopup()" class="
            small-black-text bordered-text setting-placeholder-text-decorator" placeholder="sample@com.ru" readonly>

            <div class="small-pink-text">
                Номер телефона:
            </div>
            <input id="phone-placeholder" onclick="openPhoneEditPopup()" class="
        small-black-text bordered-text setting-placeholder-text-decorator" placeholder="11111111" readonly>
            <div class="form-check form-switch padding-0-0-20-px">
                <input class="form-check-input" type="checkbox" id="phone-invisible-toggle" checked>
                <label class="form-check-label small-black-text" for="phone-invisible-toggle">Отображать в
                    профиле</label>
            </div>

            <div class="small-pink-text">
                Пароль:
            </div>
            <div id="password-placeholder" onclick="openPasswordEditPopup()" class="
            small-black-text bordered-text setting-placeholder-text-decorator">
                ********************
            </div>
        </div>
            <div class="input-box">
                <div class="common-black-text padding-0-0-20-px">
                    Информация о себе
                </div>
                <div class="small-pink-text">
                    Имя и фамилия:
                </div>
                <input id="name-placeholder"
                       onclick="openEditFirstAndLastNamesPopup()"
                       class="small-black-text bordered-text setting-placeholder-text-decorator"
                       placeholder="Валерия Лаврушкина"
                       readonly>
                <sec:authorize access="hasRole('ROLE_MASTER')">
                <div class="small-pink-text">
                    Вид деятельности:
                </div>
                <input id="about-profession-placeholder"
                       onclick="openEditAboutPopup()"
                       class="small-black-text bordered-text setting-placeholder-text-decorator"
                       placeholder="Мастер по маникюру"
                       readonly>
                <div class="small-pink-text">
                    О себе:
                </div>
                <textarea id="about-text-placeholder" onclick="openEditAboutPopup()"
                          class="small-black-text bordered-text setting-placeholder-text-decorator"
                          placeholder="Напишите пару слов о себе..."
                          readonly>
                </textarea>
                </sec:authorize>

            </div>
    </div>
</div>


<script>
    $(document).ready(() => {
        fillGeneralSettingTab(${idAccount});

        setTimeout(countPercentage, 500);
        progressView();
    });


    $("#avatar-input").on("change", function () {
        uploadAvatarImage(${idAccount});
    })

    $("#phone-invisible-toggle").on("click", function (e) {
        setPhoneVisibility(e.target.checked, ${idAccount})
    })
</script>
