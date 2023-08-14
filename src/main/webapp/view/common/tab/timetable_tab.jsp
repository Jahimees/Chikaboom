<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<body>

<div id="timetable-placeholder" style="background-color: white">

</div>
<div class="medium-text">Определите ваш рабочий день</div>
<div id="current-working-day-duration" class="medium-text"></div>
<label id="working-day-warn" class="invalid-field-label-popup small-text"
       style="display: none">Начало рабочего дня не может быть позже конца рабочего дня!</label>
<div class="row medium-text">
    <div class="col-4">
        <label for="working-day-start">Начало рабочего дня</label>
        <label id="working-day-start-warn" class="invalid-field-label-popup small-text" for="working-day-start"
               style="display: none">Не соответствует
            шаблону!</label>
        <input id="working-day-start" type="text" placeholder="9:00">
    </div>
    <div class="col-4">
        <label for="working-day-end">Конец рабочего дня</label>
        <label id="working-day-end-warn" class="invalid-field-label-popup small-text" for="working-day-end"
               style="display: none">Не соответствует
            шаблону!</label>
        <input id="working-day-end" type="text" placeholder="17:00">
    </div>
    <input id="save-work-time-btn" type="button" class="button" value="Сохранить">
</div>
</body>

<script>
    var workingDays = JSON.parse(JSON.stringify(${workingDays}));

    $(document).ready(function () {
        if (workingDays.workingDays !== null) {
            workingDays.workingDays = JSON.parse(workingDays.workingDays);
        } else {
            workingDays.workingDays = [];
        }

        reloadWorkingDayDuration();

        //TODO перенести в отдельный метод + создать отдельный js файл
        $.ajax({
            type: "get",
            url: "/chikaboom/calendar",
            contentType: "application/text",
            dataType: "text",
            data: {},
            success: function (data) {
                console.log("loading timetable");
                $("#timetable-placeholder").html(data);

                setTimeout(function () {
                    var button = document.createElement("button");
                    button.innerHTML = "Сделать рабочим";
                    button.setAttribute("class", "fc-button fc-state-default");
                    button.setAttribute("type", "button");
                    button.setAttribute("onclick", "addOrRemoveWorkingDate($('#calendar2 .fc-day')[0].getAttribute('data-date'))");
                    $("#calendar2 .fc-right").append(button);
                }, (1000))
            },
            error: function () {
                underConstruction();
            }
        })

    });

    $("#save-work-time-btn").on("click", function () {
        var startVal = $("#working-day-start")[0].value;
        var endVal = $("#working-day-end")[0].value;

        var regexp = /^(?:\d|[01]\d|2[0-3]):[0-5]\d$/;

        var startLbl = $("#working-day-start-warn");
        var endLbl = $("#working-day-end-warn");
        var totalLbl = $("#working-day-warn");

        var startFlag = false;
        var endFlag = false;

        if (regexp.test(startVal)) {
            startFlag = true;
            startLbl.css("display", "none");
        } else {
            startLbl.css("display", "block");
            startFlag = false;
        }

        if (regexp.test(endVal)) {
            endLbl.css("display", "none");
            endFlag = true;
        } else {
            endLbl.css("display", "block");
            endFlag = false;
        }

        var startNumber = parseInt(startVal.replace(':', ''));
        var endNumber = parseInt(endVal.replace(':', ''));

        if (startNumber >= endNumber) {
            totalLbl.css("display", "block");
            startFlag = false;
            endFlag = false;
        } else {
            totalLbl.css("display", "none");
        }

        if (startFlag && endFlag) {
            workingDays.workingDayStart = startNumber;
            workingDays.workingDayEnd = endNumber;
            saveWorkingDays();
            reloadWorkingDayDuration();
        }
    })

    function reloadWorkingDayDuration() {
        var workingDayStartString = JSON.stringify(workingDays.workingDayStart);
        var workingDayEndString = JSON.stringify(workingDays.workingDayEnd);

        var startTime = workingDayStartString.length === 4 ?
            workingDayStartString.substring(0, 2) + ":" + workingDayStartString.substring(2, 4)
            : workingDayStartString.substring(0, 1) + ":" + workingDayStartString.substring(1, 3);
        var endTime = +workingDayEndString.length === 4 ?
            workingDayEndString.substring(0, 2) + ":" + workingDayEndString.substring(2, 4)
            : workingDayEndString.substring(0, 1) + ":" + workingDayEndString.substring(1, 3)

        $("#current-working-day-duration")[0].innerText = "Ваш текущий рабочий день: " + startTime + " - " + endTime;

    }

</script>
