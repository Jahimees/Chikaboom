<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<link rel="stylesheet" href="../../../css/chartist/chartist.min.css">
<div class="content">
    <div>
        <div class="ct-chart ct-perfect-fourth"></div>
    </div>
</div>
<script src="../../../js/chartist/chartist.min.js"></script>

<script>
    $(document).ready(function () {
        let labels = []
        let series = []
        for (var i = 1; i <= new Date().daysInMonth(); i++) {
            labels.push(i);
            series.push(0);//{x: i, y: 0}
        }

        let appointmentsData
        $.ajax({
            method: "get",
            url: "/accounts/" + ${idAccount} +"/income-appointments",
            contentType: "application/json",
            dataType: "json",
            async: false,
            success: function (json) {
                // TODO optimize. Не нужны прошлые записи. Их догружать отдельно
                appointmentsData = json
            }
        })

        appointmentsData.forEach(appointment => {
            let appointmentDate = new Date(appointment.appointmentDateTime);
            if (appointmentDate.getMonth() === new Date().getMonth()) {
                series[appointmentDate.getDate() - 1] += 1;
                // series[appointmentDate.getDate() - 1].y += 1;
            }
        })

        // TEST
        var data = {
            labels: labels,
            series: [
                series
            ]
        };

        // var options = {
        //     width: 800,
        //     height: 300,
        //     scaleMinSpace: 125,
        //     // Can be set to true or false. If set to true, the scale will be generated with whole numbers only.
        //     onlyInteger: true,
        //     // The reference value can be used to make sure that this value will always be on the chart. This is especially useful on bipolar charts where the bipolar center always needs to be part of the chart.
        //     divisor: 4,
        // };

        // new Chartist.Line('.ct-chart', data, options);
        new Chartist.Line('.ct-chart',
            data
        , {
            width: 600,
            height: 300,
            // axisX: {
                // type: Chartist.AutoScaleAxis,
                // onlyInteger: true
            // },
            axisY: {
                type: Chartist.AutoScaleAxis,
                // ticks: [1,2,3,4,5,6,7,8,9,10,30],
                onlyInteger: true,
                low: 0
            },
            // lineSmooth: Chartist.Interpolation.step(),
            // showPoint: false,
            lineSmooth: false
        });
    })
</script>
