<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<div class="popup register-popup">
    <div class="popup-title">
        <h3>Регистрация</h3>
    </div>
    <div class="popup-body">
        <div class="common-black-text">Логин</div>
        <label class="invalid-field-label-popup" id="r-input-username-short">Имя пользователя слишком
            короткое</label>
        <label class="invalid-field-label-popup" id="r-input-username-incorrect">Можно использовать только буквы и 1
            пробел между словами</label>
        <label class="invalid-field-label-popup" id="r-input-username-empty">Поле не может быть пустым</label>
        <div class="popup-input middle-box w-80">
            <input id="r-input-username" class="r-popup-input-field" name="username" required placeholder="Rosaline"
                   valid="false" maxlength="40">
        </div>

        <div class="common-black-text">Номер телефона</div>
        <div class="popup-input middle-box w-80">
            <input id="r-input-phone" class="phone-input-popup" name="phone" required
                   valid="false" maxlength="30">
            <span id="error-msg-r-input-phone" class="hide"></span>
        </div>

        <div class="common-black-text">Пароль</div>
        <label class="invalid-field-label-popup" id="r-input-password-empty">Поле не может быть пустым</label>
        <label class="invalid-field-label-popup" id="r-input-password-short">Пароль должен быть длиннее 5
            символов</label>
        <div class="popup-input middle-box w-80">
            <input id="r-input-password" class="r-popup-input-field" name="password" type="password" required
                   placeholder="*****"
                   maxlength="50">
        </div>

        <div class="common-black-text">Подтверждение пароля</div>
        <label class="invalid-field-label-popup" id="r-input-confirm-password-empty">Поле не может быть
            пустым</label>
        <label class="invalid-field-label-popup" id="r-input-confirm-password-different">Пароли не совпадают</label>
        <div class="popup-input middle-box w-80">
            <input id="r-input-confirm-password" class="r-popup-input-field" type="password" required
                   placeholder="*****"
                   maxlength="50">
        </div>

        <div>
            <label><input type="radio" name="roles" checked value="2"> Я клиент</label>
            <label><input type="radio" name="roles" value="1"> Я мастер</label>
        </div>

        <div>
            <label class="invalid-field-label-popup" id="r-phone-duplicate">Аккаунт с таким номером телефона
                или именем пользователя уже существует!</label>
        </div>

        <button id="confirm-register" class="btn btn-dark confirm-popup-btn">
            Зарегистрироваться
        </button>
    </div>
</div>

<script>
    $(document).ready(() => {
        initPhoneCodeWidget("r-input-phone");
    })
</script>