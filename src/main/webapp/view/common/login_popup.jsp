<%@ page contentType="text/html;charset=UTF-8" %>
<div class="popup-bg"></div>

<div class="login-popup">
    <div class="close-login-popup" tabindex="1">
        X
    </div>
    <div class="popup-title">
        <h3>Авторизация</h3>
    </div>
    <div class="popup-body">
        <label class="invalid-field-label" id="l-input-phone-incorrect">Некорректный номер телефона</label>
        <label class="invalid-field-label" id="l-input-phone-empty">Поле не может быть пустым</label>
        <div class="image-input">
            <img class="small-popup-icon" src="../../image/icon/phone_icon.svg" alt="no_image">
            <input id="l-input-phone" class="phone-input" name="phone" required placeholder="Введите номер телефона" valid="false">
        </div>
        <label class="invalid-field-label" id="l-input-password-empty">Поле не может быть пустым</label>
        <div class="image-input">
            <img class="small-popup-icon" src="../../image/icon/password_icon.svg" alt="no_image">
            <input id="l-input-password" name="password" type="password" required placeholder="Введите пароль"
                   valid="false">
        </div>

        <div>
            <label class="invalid-field-label" id="l-phone-or-password-incorrect">
                Введенные данные некорректны, попробуйте ещё раз</label>
        </div>

        <hr>

        <a href="/chikaboom/under_construction" class="small-info">Забыли пароль?</a>
        <button id="confirm-login" class="btn btn-dark confirm-btn">
            Войти
        </button>
    </div>
</div>
