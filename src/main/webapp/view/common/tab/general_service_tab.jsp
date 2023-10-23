<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="content">
    <div>
        <div class="big-text">Мои услуги</div>

        <div id="service-placeholder" class="d-block w-100">
        </div>

        <div class="purple-button middle-box w-50" onclick="prepareToCreateNewService()">Создать новую услугу</div>

        <div id="service-edit" class="margin-5-0 row" style="display: none">
            <input type="number" id="service-id-input" style="display: none">
            <select id="service-type-select" onchange="reloadServiceSubtypeSelectOptions()" name="service-type" class="col-2">
                <option value="1">
                    Ногтевой сервис
                </option>
                <option value="2">
                    Ногтевой сервис
                </option>
            </select>
            <select id="service-subtype-select" name="service-type" class="col-2">
                <option value="1">
                    Ногтевой сервис
                </option>
                <option value="2">
                    Ногтевой сервис
                </option>
            </select>
            <input title="Название услуги" id="service-name-input" class="col-2" placeholder="Покрытие гель-лаком">
            <input title="Цена за услугу" id="service-price-input" onchange="setTwoNumberDecimal(this)"
                   type="number" class="col-2"
                   placeholder="105.40">
            <select title="Приблизительное время выполнения услуги" id="service-time-select" class="col-2">
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
            <input type="button" onclick="saveService()" class="edit-button col-1" value="Сохранить">
            <div onclick="hideEditRow()" class="btn col-1">
                <img width="22px" src="../../../image/icon/cross_icon.svg">
            </div>
        </div>
    </div>
</div>

<script>

    $(document).ready(() => {
        loadServiceTab(${idAccount}, false);
    })

    function setTwoNumberDecimal(thisObj) {
        thisObj.value = parseFloat(thisObj.value).toFixed(2);
    }

</script>