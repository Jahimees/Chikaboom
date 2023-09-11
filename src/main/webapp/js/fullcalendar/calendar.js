$(function () {
    showTodaysDate();
    loadMasterAppointments();
});

var initializeCalendar = function (appointmentsForCalendar) {
    $('.calendar').fullCalendar({
        editable: true,
        eventLimit: true, // allow "more" link when too many events
        // create events
        events: appointmentsForCalendar,
        firstDay: 1,
        defaultTimedEventDuration: '00:30:00',
        forceEventDuration: true,
        eventBackgroundColor: '#523870',
        editable: false,
        height: screen.height - 160,
        timezone: 'Russia/Moscow',
    });
}

function loadMasterAppointments() {
    let appointmentsForCalendar = [];
    $.ajax({
        type: "get",
        url: "/accounts/" + accountJson.idAccount + "/income-appointments",
        contentType: "application/json",
        dataType: "json",
        success: function (masterAppointments) {
            masterAppointments.forEach(function (masterAppointment) {
                let visitorName = masterAppointment.userDetailsClient.firstName ? masterAppointment.userDetailsClient.firstName : "Неизвестный";
                let title = masterAppointment.service.name + " - " + visitorName;
                let appointmentDateTimeStart = new Date(masterAppointment.appointmentDateTime);

                let serviceTime = masterAppointment.service.time;
                let serviceDurationTime = serviceTime.replace(' минут', '').split(' час');
                let duration;

                if (serviceDurationTime.length === 1) {
                    duration = 1;
                } else {
                    serviceDurationTime[1] = serviceDurationTime[1].replace('а', '');

                    duration = serviceDurationTime[0] * 60;
                    duration += serviceDurationTime[1] === '' ? 0 : 30;
                }

                let appointmentDateTimeEnd = new Date(masterAppointment.appointmentDateTime);
                let minutes = appointmentDateTimeEnd.getMinutes() + duration;
                appointmentDateTimeEnd.setMinutes(minutes);
                let appointmentObj = {
                    id: masterAppointment.idAppointment,
                    title: title,
                    start: appointmentDateTimeStart,
                    end: appointmentDateTimeEnd
                }
                appointmentsForCalendar.push(appointmentObj)
            })

            initializeCalendar(appointmentsForCalendar);
            getCalendars();
            initializeRightCalendar();
            initializeLeftCalendar();
            disableEnter();
        }
    })
}


/* --------------------------initialize calendar-------------------------- */

var initializeRightCalendar = function () {
    $cal2.fullCalendar('changeView', 'agendaDay');

    $cal2.fullCalendar('option', {
        firstDay: 1,
        slotEventOverlap: false,
        allDaySlot: false,
        header: {
            right: 'prev,next today'
        },
        selectable: true,
        selectHelper: true,
        select: function (start, end) {
            newEvent(start, end);
        },
        eventClick: function (calEvent, jsEvent, view) {
            obj = calEvent;
            editEvent(calEvent);
        },
    });
}

function addOrRemoveWorkingDate(date) {
    var flag = true;

    workingDays.workingDays.forEach(function (workDate) {
        var currentDate = new Date(Date.parse(JSON.parse(JSON.stringify(date))))
        var workingDate = new Date(Date.parse(JSON.parse(JSON.stringify(workDate))))
        if (currentDate.getDate() === workingDate.getDate()
            && currentDate.getMonth() === workingDate.getMonth()
            && currentDate.getFullYear() === workingDate.getFullYear()) {

            var index = workingDays.workingDays.indexOf(workDate);
            workingDays.workingDays.splice(index, 1);
            flag = false;
        }
    })

    if (flag) {
        var currentDateObj = new Date(date);
        workingDays.workingDays.push(currentDateObj);
    }

    saveWorkingDays();

    initializeLeftCalendar();
}

function saveWorkingDays() {
    workingDays.workingDays = JSON.stringify(workingDays.workingDays);

    $.ajax({
        type: "PUT",
        url: "/accounts/" + accountJson.idAccount + "/workingDays",
        contentType: "application/json",
        dataType: "json",
        data: JSON.stringify(workingDays),
        error: function () {
            alert("Данные не сохранены в базу данных. Неизвестная ошибка");
        }
    });

    workingDays.workingDays = JSON.parse(workingDays.workingDays);
}

/* -------------------manage cal1 (left pane)------------------- */
var initializeLeftCalendar = function () {
    $cal1.fullCalendar('option', {

        firstDay: 1,
        header: {
            left: 'prev,next today',
            center: 'title',
            right: 'month,agendaWeek'
        },
        navLinks: false,
        dayClick: function (date) {
            cal2GoTo(date);
        },
        eventClick: function (calEvent) {
            cal2GoTo(calEvent.start);
        },
        dayRender: function (date, cell) {
            if (checkDate(date)) {
                cell.css("background-color", "white");
            } else {
                cell.css("background-color", "#F4F4F4");
            }
        },

    });

}

function checkDate(date) {
    var flag = false;
    workingDays.workingDays.forEach(function (workDate) {
        var currentDate = new Date(Date.parse(JSON.parse(JSON.stringify(date))))
        var workingDate = new Date(Date.parse(JSON.parse(JSON.stringify(workDate))))

        if (currentDate.getDate() === workingDate.getDate()
            && currentDate.getMonth() === workingDate.getMonth()
            && currentDate.getFullYear() === workingDate.getFullYear()) {
            flag = true;
        }
    })
    return flag;
}

var getCalendars = function () {
    $cal = $('.calendar');
    $cal1 = $('#calendar1');
    $cal2 = $('#calendar2');
}

var cal2GoTo = function (date) {
    $cal2.fullCalendar('gotoDate', date);
}


var newEvent = function (start, nd) {
    $('input#title').val("");
    let $createEventModal = $("#createEventModal");
    $createEventModal.modal('show');
    $('#submit').unbind();
    $('#submit').on('click', function () {
        var title = $('input#title').val();

        if (title) {
            var eventData = {
                title: title,
                start: JSON.parse(JSON.stringify(start)),
                end: nd
            };
            $cal.fullCalendar('renderEvent', eventData, true);
            $createEventModal.modal('hide');
        } else {
            $createEventModal.modal('hide');
            callMessagePopup("Невозможно создать событие", "Название не может быть пустым");
        }
    });
}

var editEvent = function (calEvent) {
    $('input#editTitle').val(calEvent.title);
    let $updateEventModal = $("#updateEventModal");
    $updateEventModal.modal('show');
    $('#update').unbind();
    $('#update').on('click', function () {
        var title = $('input#editTitle').val();
        $updateEventModal.modal('hide');
        if (title) {
            calEvent.title = title
            $cal.fullCalendar('updateEvent', calEvent);
        } else {
            callMessagePopup("Невозможно обновить событие", "Название не может быть пустым")
        }
    });
    $('#delete').on('click', function () {
        $('#delete').unbind();
        if (calEvent._id.includes("_fc")) {
            $cal1.fullCalendar('removeEvents', [getCal1Id(calEvent._id)]);
            $cal2.fullCalendar('removeEvents', [calEvent._id]);
        } else {
            $cal.fullCalendar('removeEvents', [calEvent._id]);
        }
        $updateEventModal.modal('hide');
    });
}

/* --------------------------load date in navbar-------------------------- */
var showTodaysDate = function () {
    var n = new Date();
    var y = n.getFullYear();
    var m = n.getMonth() + 1;
    var d = n.getDate();
    $("#todaysDate").html("Сегодня " + d + "/" + m + "/" + y);
};

/* full calendar gives newly created given different ids in month/week view
    and day view. create/edit event in day (right) view, so correct for
    id change to update in month/week (left)
  */
var getCal1Id = function (cal2Id) {
    var num = cal2Id.replace('_fc', '') - 1;
    var id = "_fc" + num;
    return id;
}

var disableEnter = function () {
    $('body').bind("keypress", function (e) {
        if (e.keyCode == 13) {
            e.preventDefault();
            return false;
        }
    });
}

function loadWorkingDaysData(idAccount) {
    $.ajax({
        method: "get",
        url: "/accounts/" + idAccount + "/workingDays",
        contentType: "application/json",
        dataType: "json",
        async: false,
        success: function (data) {
            workingDays = data;
            loadAccountCalendar();
        },
        error: function () {
            callMessagePopup("Что-то пошло не так!", "Невозможно загрузить расписание!");
        }
    })
}

function loadAccountCalendar() {
    if (workingDays !== null && workingDays.workingDays !== null) {
        workingDays.workingDays = JSON.parse(workingDays.workingDays);
    } else {
        workingDays.workingDays = [];
    }

    reloadWorkingDayDuration();

    $.ajax({
        type: "get",
        url: "/chikaboom/personality/calendar",
        contentType: "application/text",
        dataType: "text",
        async: false,
        data: {},
        success: function (data) {
            $("#timetable-placeholder").html(data);

            setTimeout(function () {
                let button = document.createElement("button");
                button.innerHTML = "Сделать рабочим";
                button.setAttribute("class", "fc-button fc-state-default");
                button.setAttribute("type", "button");
                button.setAttribute("onclick", "addOrRemoveWorkingDate($('#calendar2 .fc-day').attr('data-date'))");
                $("#calendar2 .fc-right").append(button);
            }, (1000))
        },
        error: function () {
            underConstruction();
        }
    })
}

function reloadWorkingDayDuration() {
    var workingDayStartString = JSON.stringify(workingDays.workingDayStart);
    var workingDayEndString = JSON.stringify(workingDays.workingDayEnd);

    var startTime = workingDayStartString.length === 4 ?
        workingDayStartString.substring(0, 2) + ":" + workingDayStartString.substring(2, 4)
        : workingDayStartString.substring(0, 1) + ":" + workingDayStartString.substring(1, 3);
    var endTime = +workingDayEndString.length === 4 ?
        workingDayEndString.substring(0, 2) + ":" + workingDayEndString.substring(2, 4)
        : workingDayEndString.substring(0, 1) + ":" + workingDayEndString.substring(1, 3)

    $("#current-working-day-duration").text("Ваш текущий рабочий день: " + startTime + " - " + endTime);

}

$("#save-work-time-btn").on("click", function () {
    var startVal = $("#working-day-start").val();
    var endVal = $("#working-day-end").val();

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
        reloadWorkingDayDuration(workingDays);
    }
})
