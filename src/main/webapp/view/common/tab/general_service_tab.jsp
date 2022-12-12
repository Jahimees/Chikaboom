<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="content">
    <div>
        <div class="big-text">Мои услуги</div>

        <div id="user-service-placeholder" class="d-block w-100">

        </div>

        <div class="purple-button middle-box w-50" onclick="prepareToCreateNewService()">Создать новую услугу</div>

        <div id="user-service-edit" class="margin-5-0 row" style="display: none">
            <input type="number" id="userservice-id-input" style="display: none">
            <select id="service-select" onchange="reloadSubserviceSelectOptions()" name="service-type" class="col-2">
                <option value="1">
                    Ногтевой сервис
                </option>
                <option value="2">
                    Ногтевой сервис
                </option>
            </select>
            <select id="subservice-select" name="service-type" class="col-2">
                <option value="1">
                    Ногтевой сервис
                </option>
                <option value="2">
                    Ногтевой сервис
                </option>
            </select>
            <input title="Название услуги" id="userservice-name-input" class="col-2" placeholder="Покрытие гель-лаком">
            <input title="Цена за услугу" id="userservice-price-input" onchange="setTwoNumberDecimal(this)"
                   type="number" class="col-2"
                   placeholder="105.40">
            <select title="Приблизительное время выполнения услуги" id="userservice-time-select" class="col-2">
                <option>30 минут</option>
                <option>1 час</option>
                <option>1 час 30 минут</option>
                <option>2 часа</option>
                <option>2 часа 30 минут</option>
                <option>3 часа</option>
                <option>3 часа 30 минут</option>
                <option>4 часа</option>
                <option>4 часа 30 минут</option>
                <option>5 часов</option>
            </select>
            <input type="button" onclick="saveUserService()" class="edit-button col-1" value="Сохранить">
            <div onclick="hideEditRow()" class="btn col-1">
                <img width="22px" src="/image/icon/cross_icon.svg">
            </div>
        </div>

    </div>
</div>

<script>

    function loadServiceTab() {
        $.ajax({
            type: "get",
            url: "/chikaboom/personality/${idAccount}/services/info",
            contentType: "application/text",
            dataType: "text",
            data: {},
            success: function (data) {
                userServicesJson = JSON.parse(data);
                console.log("general service tab successfully loaded");
                fillServiceTable(userServicesJson);
                loadServiceSelectOptions();
            },
            error: function () {
                loadUnderConstruction();
            }
        });
    }


    $(document).ready(function () {
        loadServiceTab();
    })

    function setTwoNumberDecimal(thisObj) {
        thisObj.value = parseFloat(thisObj.value).toFixed(2);
    }

</script>