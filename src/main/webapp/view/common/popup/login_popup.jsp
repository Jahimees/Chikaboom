<%@ page contentType="text/html;charset=UTF-8" %>
<div class="popup-bg"></div>

<div class="popup login-popup">
    <div class="popup-title">
        <h3>Авторизация</h3>
    </div>
    <form id="login-form">
        <div class="popup-body">
            <div class="common-black-text">Имя пользователя</div>
            <label class="invalid-field-label-popup" id="l-input-username-empty">Поле не может быть пустым</label>
            <label class="invalid-field-label-popup" id="l-input-username-incorrect">Можно использовать только буквы и 1
                пробел между словами</label>
            <div class="popup-image">
                <input id="l-input-username" class="popup-input-field" name="username" required placeholder="Rosaline"
                       valid="false">
            </div>
            <div class="common-black-text">Пароль</div>
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
            <button type="submit" class="btn btn-dark confirm-popup-btn">
                Войти
            </button>
        </div>
    </form>
</div>