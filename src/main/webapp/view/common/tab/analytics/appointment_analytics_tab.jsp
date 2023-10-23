<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<link rel="stylesheet" href="../../../css/chartist/chartist.min.css">
<div class="content">
    <div style="display: flex">
        <div class="w-45 margin-0-10px">
            <div class="medium-text">Количество записей за месяц</div>
            <div id="select-box-appointments" class="flex-box-white">
                <select id="month-selector-appointments" class="form-control">
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
                <select id="year-selector-appointments" class="form-control">
                </select>
            </div>
            <div class="ct-chart-appointments ct-perfect-fourth"></div>
        </div>
        <div class="w-45">
            <div class="medium-text">% записей на конкретную услугу (за все время и за месяц)</div>
            <div id="select-box-appointments-percent" class="flex-box-white">
                <select id="month-selector-appointments-percent" class="form-control">
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
                <select id="year-selector-appointments-percent" class="form-control">
                </select>
            </div>
            <div class="ct-chart-appointments-percent ct-perfect-fourth"></div>
        </div>
    </div>
</div>

<script src="../../../js/chartist/chartist.min.js"></script>
<script src="../../../js/analytics.js"></script>
<script>

    $(document).ready(() => {
        initFullChart("appointments", ${idAccount});
        initFullChart("appointments-percent", ${idAccount});
    })
</script>
