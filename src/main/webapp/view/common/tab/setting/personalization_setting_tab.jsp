<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<div class="content">
    <sec:authorize access="hasRole('ROLE_MASTER')">
        <div id="first-row">
            <div class="input-box">
                <div class="common-black-text padding-0-0-20-px">
                    Рабочий график
                </div>

                <div class="small-pink-text">
                    Время рабочего дня по умолчанию:
                </div>
                <input id="default-working-time-placeholder" onclick="openEditDefaultWorkingTimePopup()"
                       class="small-black-text bordered-text setting-placeholder-text-decorator"
                       placeholder="" readonly>
            </div>
        </div>
        <div id="second-row">
            <div class="input-box" style="width: 400px">
                <div class="common-black-text padding-0-0-20-px">
                    Галерея фотографий
                </div>
                <input id="image-input" type="file" accept="image/jpeg" name="file" hidden>
                <div class="violet-button" onclick="chooseNewImage()">Загрузить фотографию</div>
                <div class="margin-10px-0" style="height: 150px; overflow: auto">
                    <table>
                        <tr style="margin-bottom: 5px">
                            <td class="col-md-3"><img src="../../image/user/13/avatar.jpeg" style="width: 50px; height: 50px"></td>
                            <td class="col-md-7">Название файла.jpeg</td>
                            <td class="padding-0-5 col-md-2">X</td>
                        </tr>
                        <tr style="margin-bottom: 5px">
                            <td class="col-md-3"><img src="../../image/user/13/avatar.jpeg" style="width: 50px; height: 50px"></td>
                            <td class="col-md-7">Название файла.jpeg</td>
                            <td class="padding-0-5 col-md-2">X</td>
                        </tr>
                        <tr style="margin-bottom: 5px">
                            <td class="col-md-3"><img src="../../image/user/13/avatar.jpeg" style="width: 50px; height: 50px"></td>
                            <td class="col-md-7">Название файла.jpeg</td>
                            <td class="padding-0-5 col-md-2">X</td>
                        </tr>
                        <tr style="margin-bottom: 5px">
                            <td class="col-md-3"><img src="../../image/user/13/avatar.jpeg" style="width: 50px; height: 50px"></td>
                            <td class="col-md-7">Название файла.jpeg</td>
                            <td class="padding-0-5 col-md-2">X</td>
                        </tr>
                        <tr style="margin-bottom: 5px">
                            <td class="col-md-3"><img src="../../image/user/13/avatar.jpeg" style="width: 50px; height: 50px"></td>
                            <td class="col-md-7">Название файла.jpeg</td>
                            <td class="padding-0-5 col-md-2">X</td>
                        </tr>
                    </table>
                </div>
            </div>
        </div>
    </sec:authorize>
</div>

<script>
    $(document).ready(function () {
        $("#default-working-time-placeholder")
            .attr("placeholder", accountFacadeJson.accountSettingsFacade.defaultWorkingDayStart +
                " - " + accountFacadeJson.accountSettingsFacade.defaultWorkingDayEnd);
    });

    function chooseNewImage() {
        $("#image-input").click();
    }

    $("#image-input").on("change", function () {
        uploadImage(${idAccount});
    })

    function uploadImage(idAccount) {
        console.log("gag")
        let formData = new FormData();
        formData.append("file", $("#image-input")[0].files[0]);
        let uuid = self.crypto.randomUUID();
        formData.append("fileName", "/gallery/" + uuid + ".jpeg");
        if (window.FormData === undefined) {
            alert('В вашем браузере FormData не поддерживается')
        }
        $.ajax({
            method: "POST",
            dataType: "json",
            contentType: false,
            processData: false,
            async: false,
            url: "/chikaboom/upload/file/" + idAccount,
            data: formData,
            statusCode: {
                201: function () {
                    callMessagePopup("Фотография успешно загружена!", "Ваше новое фото профиля успешно было загружено!");
                    // let $img = $(".personality-avatar-image");
                    // let $small_image = $(".small-avatar-image");
                    // $img.attr("src", $img.attr("src").split("?")[0] + "?" + Math.random());
                    // $small_image.attr("src", $small_image.attr("src").split("?")[0] + "?" + Math.random());
                },
                400: function () {
                    callMessagePopup("Ошибка!", "Произошла ошибка! Фотографию не удалось загрузить!");
                }
            }
        })
    }
</script>
