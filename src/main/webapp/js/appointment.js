function fillAppointmentsTable(appointmentsJSON, isIncomeAppointment) {
    let dataTable;

    $("#appointment-placeholder").empty();

    if (typeof dataTable !== 'undefined') {
        dataTable.destroy();
    }
    $("#example-tbody").empty();

    let timeOptions = {
        hour: 'numeric',
        minute: 'numeric'
    }

    appointmentsJSON.forEach(function (appointment) {
        // let row = document.createElement("div");
        // row.setAttribute("class", "service-row row");
        // row.setAttribute("style", "color: black");
        //
        // let serviceNameDiv = document.createElement("div");
        // serviceNameDiv.setAttribute("class", "col-2")
        // serviceNameDiv.innerHTML = appointment.service.name;
        //
        // let dateDiv = document.createElement("div");
        // dateDiv.setAttribute("class", "col-2");
        // dateDiv.innerText = new Date(appointment.appointmentDateTime).toLocaleDateString("ru");
        //
        // let appointmentTimeDiv = document.createElement("div");
        // appointmentTimeDiv.setAttribute("class", "col-1");
        // appointmentTimeDiv.innerHTML = new Date(appointment.appointmentDateTime).toLocaleTimeString("ru", timeOptions);
        //
        // let priceDiv = document.createElement("div");
        // priceDiv.setAttribute("class", "col-1");
        // priceDiv.innerHTML = appointment.service.price;
        //
        // let timeDiv = document.createElement("div");
        // timeDiv.setAttribute("class", "col-1");
        // timeDiv.innerHTML = appointment.service.time;
        //
        // let accountPhoneDiv = document.createElement("div");
        // accountPhoneDiv.setAttribute("class", "col-2")
        //
        // let accountNameDiv = document.createElement("div");
        // accountNameDiv.setAttribute("class", "col-2")
        //
        // let deleteBtnDiv = document.createElement("div");
        // deleteBtnDiv.setAttribute("class", "col-1");
        // deleteBtnDiv.setAttribute("style", "cursor: pointer");

        if (new Date(appointment.appointmentDateTime).getTime() > new Date().getTime()) {
            $("#example-tbody").append("<tr id='appointment-" + appointment.idAppointment + "'></tr>");
        } else {
            $("#example-tbody").append("<tr style='background-color: #FFC7ED' id='appointment-" + appointment.idAppointment + "'></tr>");
        }

        $("#example-tbody").append("<tr id='appointment-" + appointment.idAppointment + "'></tr>");
        let $appointmentRow = $("#appointment-" + appointment.idAppointment);

        $appointmentRow.append("<td>" + appointment.service.name + "</td>")
        $appointmentRow.append("<td>" + new Date(appointment.appointmentDateTime).toLocaleDateString('ru') + "</td>")
        $appointmentRow.append("<td>" + new Date(appointment.appointmentDateTime).toLocaleTimeString("ru", timeOptions) + "</td>")
        $appointmentRow.append("<td>" + appointment.service.price + "</td>")
        $appointmentRow.append("<td>" + appointment.service.time + "</td>")

        if (isIncomeAppointment) {
            // accountPhoneDiv.innerHTML = appointment.clientAccount.phoneCode.phoneCode + " " + appointment.clientAccount.phone;
            // accountNameDiv.innerHTML = appointment.clientAccount.username;
            // deleteBtnDiv.setAttribute("onclick",
            //     "callConfirmDeleteIncomeAppointmentPopup("
            //     + appointment.masterAccount.idAccount + "," + appointment.idAppointment + ")")

            $appointmentRow.append("<td>" + appointment.clientAccount.phoneCode.phoneCode + " "
                + appointment.clientAccount.phone + "</td>")
            $appointmentRow.append("<td>" + appointment.clientAccount.username + "</td>")
            $appointmentRow.append("<td style='cursor: pointer' onclick='callConfirmDeleteIncomeAppointmentPopup("
                + appointment.masterAccount.idAccount + "," + appointment.idAppointment + ")'>" +
                "<img src='/image/icon/cross_icon.svg' style='width: 15px'>" +
                "</td>")
        } else {
            // accountPhoneDiv.innerHTML = appointment.masterAccount.phoneCode.phoneCode + " "
            //     + appointment.masterAccount.phone;
            // accountNameDiv.innerHTML = appointment.masterAccount.username;
            // deleteBtnDiv.setAttribute("onclick",
            //     "callConfirmDeleteOutcomeAppointmentPopup("
            //     + appointment.clientAccount.idAccount + "," + appointment.idAppointment + ")")

            $appointmentRow.append("<td>" + appointment.masterAccount.phoneCode.phoneCode + " "
                + appointment.masterAccount.phone + "</td>")
            $appointmentRow.append("<td>" + appointment.masterAccount.username + "</td>")
            $appointmentRow.append("<td style='cursor: pointer' onclick='callConfirmDeleteOutcomeAppointmentPopup("
                + appointment.clientAccount.idAccount + "," + appointment.idAppointment + "')>" +
                "<img src='/image/icon/cross_icon.svg' style='width: 15px'>" +
                "</td>")
        }

        // let img = document.createElement("img");
        // img.setAttribute("src", "/image/icon/cross_icon.svg");
        // img.setAttribute("style", "width: 15px");
        //
        // deleteBtnDiv.appendChild(img);
        //
        // row.appendChild(serviceNameDiv);
        // row.appendChild(dateDiv);
        // row.appendChild(appointmentTimeDiv);
        // row.appendChild(priceDiv);
        // row.appendChild(timeDiv);
        // row.appendChild(accountPhoneDiv);
        // row.appendChild(accountNameDiv);
        // row.appendChild(deleteBtnDiv);
        //
        // $("#appointment-placeholder")[0].appendChild(row);
    })

    dataTable = new DataTable('#example', {
        order: [[3, 'desc']],
        "language": {
            "decimal":        "",
            "emptyTable":     "Информация не доступна",
            "info": "Показано страниц _PAGE_ из _PAGES_",
            "infoEmpty":      "Показывается с 0 по 0 из 0 данных",
            "infoFiltered":   "(отфильтровано из _MAX_ всех возможных данных)",
            "infoPostFix":    "",
            "thousands":      ",",
            "lengthMenu":     "Показывать _MENU_ на одной странице",
            "loadingRecords": "Загрузка...",
            "processing":     "",
            "search":         "Поиск:",
            "zeroRecords":    "Совпадений не найдено",
            "paginate": {
                "first":      "Первая",
                "last":       "Последняя",
                "next":       "Следующая",
                "previous":   "Предыдущая"
            },
            "aria": {
                "sortAscending":  ": активировать сортировку по возрастанию",
                "sortDescending": ": активировать сортировку по убыванию"
            }
        }
    });
}

function loadIncomeAppointmentsData(idAccount) {
    $.ajax({
        method: "get",
        url: "/accounts/" + idAccount + "/income-appointments",
        contentType: "application/json",
        dataType: "json",
        async: false,
        success: function (json) {
            fillAppointmentsTable(json, true)
        }
    })
}

function loadOutcomeAppointmentsData(idAccount) {
    $.ajax({
        method: "get",
        url: "/accounts/" + idAccount + "/outcome-appointments",
        contentType: "application/json",
        dataType: "json",
        success: function (json) {
            fillAppointmentsTable(json, false)
        }
    })
}

function callConfirmDeleteIncomeAppointmentPopup(idAccountMaster, idAppointment) {
    repairDefaultMessagePopup();
    $("#decline-message-btn")[0].style.display = "block";
    $("#confirm-message-btn")[0].setAttribute("onclick", "deleteIncomeAppointment(" + idAccountMaster + "," + idAppointment + ")");

    $("#popup-message-text")[0].innerText = "Вы действительно хотите удалить запись на услугу?";
    $(".message-popup > .popup-title > #popup-message-header")[0].innerText = "Удаление";
    openPopup("message-popup");
}

function deleteIncomeAppointment(idAccountMaster, idAppointment) {
    $.ajax({
        type: "delete",
        url: "/accounts/" + idAccountMaster + "/income-appointments/" + idAppointment,
        contentType: "application/text",
        dataType: "text",
        success: function () {
            repairDefaultMessagePopup();
            $("#popup-message-text")[0].innerText = "Запись успешно удалена!"
            $(".message-popup > .popup-title > #popup-message-header")[0].innerText = "Удалено";
            openPopup('message-popup');
            $("#confirm-message-btn")[0].setAttribute(
                "onclick", "closePopup('message-popup'), loadIncomeAppointmentsData(" + idAccountMaster + ")");
        },
        error: function () {
            repairDefaultMessagePopup();
            $("#popup-message-text")[0].innerText = "Невозможно удалить запись!"
            $(".message-popup > .popup-title > #popup-message-header")[0].innerText = "ОШИБКА!";
            openPopup('message-popup');

        }
    })
}

function callConfirmDeleteOutcomeAppointmentPopup(idAccountClient, idAppointment) {
    repairDefaultMessagePopup();
    $("#decline-message-btn")[0].style.display = "block";
    $("#confirm-message-btn")[0].setAttribute(
        "onclick", "deleteOutcomeAppointment(" + idAccountClient + "," + idAppointment + ")");

    $("#popup-message-text")[0].innerText = "Вы действительно хотите удалить запись на услугу?";
    $(".message-popup > .popup-title > #popup-message-header")[0].innerText = "Удаление";
    openPopup("message-popup");
}

function deleteOutcomeAppointment(idAccountClient, idAppointment) {
    $.ajax({
        type: "delete",
        url: "/accounts/" + idAccountClient + "/outcome-appointments/" + idAppointment,
        contentType: "application/text",
        dataType: "text",
        success: function () {
            repairDefaultMessagePopup();
            $("#popup-message-text")[0].innerText = "Запись успешно удалена!"
            $(".message-popup > .popup-title > #popup-message-header")[0].innerText = "Удалено";
            openPopup('message-popup');
            $("#confirm-message-btn")[0].setAttribute("onclick", "closePopup('message-popup'), loadOutcomeAppointmentsData(" + idAccountClient + ")");
        },
        error: function () {
            repairDefaultMessagePopup();
            $("#popup-message-text")[0].innerText = "Невозможно удалить запись!"
            $(".message-popup > .popup-title > #popup-message-header")[0].innerText = "ОШИБКА!";
            openPopup('message-popup');

        }
    })
}