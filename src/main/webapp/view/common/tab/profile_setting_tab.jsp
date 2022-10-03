<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="content">
    <div class="row w-100 input-box">
        <div class="col-4 common-black-text">
            Имя пользователя:
        </div>
        <div id="nickname-placeholder" class="col-4 common-text placeholder">
        </div>
        <input id="nickname-change-input" class="col-4 common-text" style="display: none">
        <div id="change-nickname-btn" onclick="openEditNicknamePopup()" class="col-1 edit-button">
            <img src="/image/icon/edit_icon.svg">
        </div>
    </div>
    <hr>
</div>

<script>
    function openEditNicknamePopup() {
        dropAllFields();
        addField("Имя пользователя", "nickname", "text", "Rosalline", false,
            [new Validation("Поле не может быть пустым", InvalidReason.EMPTY),
                new Validation("Имя слишком короткое", InvalidReason.SHORT)]);
        openPopup("edit-popup");
    }

    $(document).ready(function () {
        $("#nickname-placeholder")[0].innerText = accountJson.nickname;
    });
</script>