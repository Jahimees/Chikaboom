<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<!-- Modal -->
<div class="modal fade" id="registerModal" tabindex="-1" aria-labelledby="registerModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="registerModalLabel">Регистрация</h5>
                <button type="button" class="close close-button" data-bs-dismiss="modal" aria-label="Закрыть">
                    <span aria-hidden="true">X</span>
                </button>
            </div>
            <div class="modal-body">
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

                <div class="middle-box w-80">
                    <input type="radio" name="roles" checked value="2"> Я клиент
                    <input type="radio" name="roles" value="1"> Я мастер
                </div>

                <div>
                    <label class="invalid-field-label-popup" id="r-phone-duplicate">Аккаунт с таким номером телефона
                        или именем пользователя уже существует!</label>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Закрыть</button>
                <button id="confirm-register" type="button" class="btn btn-dark btn-primary">
                    Зарегистрироваться
                </button>
            </div>
        </div>
    </div>
</div>

<script>
    $(document).ready(() => {
        initPhoneCodeWidget("r-input-phone");
    })
</script>