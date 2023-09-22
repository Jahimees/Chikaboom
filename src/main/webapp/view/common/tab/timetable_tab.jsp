<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<script src='/js/fullcalendar/vendor/moment.min.js'></script>
<script src='/js/fullcalendar/vendor/fullcalendar.js'></script>
<script src='/js/fullcalendar/events.js'></script>
<script src='/js/fullcalendar/calendar.js'></script>

<div id="timetable-placeholder" style="background-color: white; position: sticky">
</div>
<div class="padding-0-5">
    <div class="medium-text">Определите ваш рабочий день по умолчанию</div>
    <div id="current-working-day-duration" class="medium-text"></div>
    <label id="working-day-warn" class="invalid-field-label-popup small-text"
           style="display: none">Начало рабочего дня не может быть позже конца рабочего дня!</label>
    <div class="row medium-text">
        <div data-tooltip="Начало рабочего дня" class="col-4">
            <label id="default-working-day-start-warn" class="invalid-field-label-popup small-text"
                   for="default-working-day-start"
                   style="display: none">Не соответствует
                шаблону!</label>
            <input id="default-working-day-start" type="text" placeholder="9:00">
        </div>
        <div data-tooltip="Конец рабочего дня" class="col-4">
            <label id="default-working-day-end-warn" class="invalid-field-label-popup small-text"
                   for="default-working-day-end"
                   style="display: none">Не соответствует
                шаблону!</label>
            <input id="default-working-day-end" type="text" placeholder="17:00">
        </div>
        <input id="save-default-work-time-btn" type="button" class="button" value="Сохранить">
    </div>
</div>

<script>
    $(document).ready(function () {
        loadWorkingDaysData(${idAccount});
        reloadWorkingDayDuration(accountJson.accountSettings);
    });
</script>
