<%@ page contentType="text/html;charset=UTF-8" %>
<div class="popup-bg"></div>

<div class="popup login-popup">
    <div class="close-popup" onclick="closePopup('login-popup')" tabindex="1">
        X
    </div>
    <div class="popup-title">
        <h3>Авторизация</h3>
    </div>
    <div class="popup-body">
        <label class="invalid-field-label-popup" id="l-input-phone-incorrect">Некорректный номер телефона</label>
        <label class="invalid-field-label-popup" id="l-input-phone-empty">Поле не может быть пустым</label>
        <div class="popup-image">
            <input id="l-input-phone" class="phone-input-popup" name="phone" required
                   placeholder="(44) 111-11-11" valid="false">
        </div>
        <label class="invalid-field-label-popup" id="l-input-password-empty">Поле не может быть пустым</label>
        <div class="popup-image">
            <input id="l-input-password" class="popup-input-field" name="password" type="password" required
                   placeholder="*****" valid="false">
        </div>

        <div>
            <label class="invalid-field-label-popup" id="l-phone-or-password-incorrect">
                Введенные данные некорректны, попробуйте ещё раз</label>
        </div>

        <hr>

        <a href="/chikaboom/under_construction" class="small-info-popup">Забыли пароль?</a>
        <button id="confirm-login" class="btn btn-dark confirm-popup-btn">
            Войти
        </button>
    </div>
</div>
