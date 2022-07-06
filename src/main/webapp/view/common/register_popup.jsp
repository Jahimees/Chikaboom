<%@ page contentType="text/html;charset=UTF-8" %>
<div class="register-popup-bg"></div>

<div class="register-popup">
    <div class="close-register-popup" tabindex="1">
        X
    </div>
    <div class="popup-title">
        <h3>Регистрация</h3>
    </div>
    <div class="popup-body">
        <label class="invalid-field-label" id="r-input-phone-incorrect">Некорректный номер телефона</label>
        <label class="invalid-field-label" id="r-input-phone-empty">Поле не может быть пустым</label>
        <label class="invalid-field-label" id="r-input-phone-short">Номер телефона должен быть длиннее 8 символов</label>
        <div class="image-input">
            <img class="small-popup-icon" src="../../image/icon/login_icon.svg" alt="no_image">
            <input id="r-input-phone" class="phone-input" name="phone" required placeholder="Введите номер телефона" valid="false">
        </div>

        <label class="invalid-field-label" id="r-input-password-empty">Поле не может быть пустым</label>
        <label class="invalid-field-label" id="r-input-password-short">Пароль должен быть длиннее 5 символов</label>
        <div class="image-input">
            <img class="small-popup-icon" src="../../image/icon/password_icon.svg" alt="no_image">
            <input id="r-input-password" name="password" type="password" required placeholder="Введите пароль">
        </div>

        <label class="invalid-field-label" id="r-input-confirm-password-empty">Поле не может быть пустым</label>
        <label class="invalid-field-label" id="r-input-confirm-password-different">Пароли не совпадают</label>
        <div class="image-input">
            <img class="small-popup-icon" src="../../image/icon/password_icon.svg" alt="no_image">
            <input id="r-input-confirm-password" type="password" required placeholder="Подтвердите пароль">
        </div>

        <div>
            <label class="invalid-field-label" id="r-phone-duplicate">Аккаунт с таким номером телефона уже существует!</label>
        </div>

        <button id="confirm-register" class="btn btn-dark confirm-btn">
            Зарегистрироваться
        </button>
    </div>
</div>
