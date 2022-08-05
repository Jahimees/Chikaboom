<%@ page contentType="text/html;charset=UTF-8" %>

<div class="popup register-popup">
    <div class="close-popup" onclick="closePopup('register-popup')" tabindex="1">
        X
    </div>
    <div class="popup-title">
        <h3>Регистрация</h3>
    </div>
    <div class="popup-body">
        <div class="common-black-text">Имя пользователя</div>
        <label class="invalid-field-label-popup" id="r-input-nickname-short">Имя пользователя слишком короткое</label>
        <label class="invalid-field-label-popup" id="r-input-nickname-incorrect">Можно использовать только буквы и 1 пробел между словами</label>
        <label class="invalid-field-label-popup" id="r-input-nickname-empty">Поле не может быть пустым</label>
        <div class="popup-image">
            <input id="r-input-nickname" class="popup-input-field" name="nickname" required placeholder="Имя профиля" valid="false" maxlength="40">
        </div>

        <div class="common-black-text">Номер телефона</div>
        <label class="invalid-field-label-popup" id="r-input-phone-incorrect">Некорректный номер телефона</label>
        <label class="invalid-field-label-popup" id="r-input-phone-empty">Поле не может быть пустым</label>
        <label class="invalid-field-label-popup" id="r-input-phone-short">Номер телефона должен быть длиннее 8
            символов</label>
        <div class="popup-image">
            <input id="r-input-phone" class="phone-input-popup" name="phone" required placeholder="Введите номер телефона"
                   valid="false" maxlength="30">
        </div>

        <div class="common-black-text">Пароль</div>
        <label class="invalid-field-label-popup" id="r-input-password-empty">Поле не может быть пустым</label>
        <label class="invalid-field-label-popup" id="r-input-password-short">Пароль должен быть длиннее 5 символов</label>
        <div class="popup-image">
            <input id="r-input-password" class="popup-input-field" name="password" type="password" required placeholder="Введите пароль"
                   maxlength="50">
        </div>

        <div class="common-black-text">Подтверждение пароля</div>
        <label class="invalid-field-label-popup" id="r-input-confirm-password-empty">Поле не может быть пустым</label>
        <label class="invalid-field-label-popup" id="r-input-confirm-password-different">Пароли не совпадают</label>
        <div class="popup-image">
            <input id="r-input-confirm-password" class="popup-input-field" type="password" required placeholder="Подтвердите пароль"
                   maxlength="50">
        </div>

        <div>
            <label><input type="radio" name="role" checked value="client"> Я клиент</label>
            <label><input type="radio" name="role" value="master"> Я мастер</label>
        </div>

        <div>
            <label class="invalid-field-label-popup" id="r-phone-duplicate">Аккаунт с таким номером телефона уже
                существует!</label>
        </div>

        <button id="confirm-register" class="btn btn-dark confirm-popup-btn">
            Зарегистрироваться
        </button>
    </div>
</div>
