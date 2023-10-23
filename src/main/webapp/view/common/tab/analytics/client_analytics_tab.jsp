<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<link rel="stylesheet" href="../../../css/chartist/chartist.min.css">
<div class="content">
    <div style="display: flex">
        <div class="w-45">
            <div class="medium-text">Количество новых клиентов</div>
            <div id="select-box-new-clients" class="flex-box-neutral">
                <div data-tooltip="Начальная дата" class="flex-box-neutral margin-0-10px">
                    <select id="month-selector-new-clients-start" class="form-control">
                        <option value="0">Январь</option>
                        <option value="1">Февраль</option>
                        <option value="2">Март</option>
                        <option value="3">Апрель</option>
                        <option value="4">Май</option>
                        <option value="5">Июнь</option>
                        <option value="6">Июль</option>
                        <option value="7">Август</option>
                        <option value="8">Сентябрь</option>
                        <option value="9">Октябрь</option>
                        <option value="10">Ноябрь</option>
                        <option value="11">Декабрь</option>
                    </select>
                    <select id="year-selector-new-clients-start" class="form-control">
                    </select>
                </div>
                <div data-tooltip="Конечная дата" class="flex-box-neutral margin-0-10px">
                    <select id="month-selector-new-clients-end" class="form-control">
                        <option value="0">Январь</option>
                        <option value="1">Февраль</option>
                        <option value="2">Март</option>
                        <option value="3">Апрель</option>
                        <option value="4">Май</option>
                        <option value="5">Июнь</option>
                        <option value="6">Июль</option>
                        <option value="7">Август</option>
                        <option value="8">Сентябрь</option>
                        <option value="9">Октябрь</option>
                        <option value="10">Ноябрь</option>
                        <option value="11">Декабрь</option>
                    </select>
                    <select id="year-selector-new-clients-end" class="form-control">
                    </select>
                </div>
            </div>
            <div class="ct-chart-new-clients ct-perfect-fourth"></div>
        </div>
    </div>
</div>

<script src="../../../js/chartist/chartist.min.js"></script>
<script src="../../../js/analytics.js"></script>
<script>

    $(document).ready(() => {
        initFullChart("new-clients", ${idAccount});
    })
</script>
