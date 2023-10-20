{
    function initFullChart(chartName, idAccount) {
        initSelectors(chartName);
        let appointmentsData = loadAppointmentsData(idAccount, true);
        console.log(appointmentsData)
        initSelectListeners(appointmentsData, chartName);
        redrawChart(appointmentsData, chartName)
    }

    function redrawChart(appointmentsData, chartName) {
        if (chartName === "appointments") {
            let data = reloadAppointmentsStatistics(appointmentsData, chartName)
            new Chartist.Line('.ct-chart-' + chartName, data, getOptions());
        } else {
            let data = reloadAppointmentPercentStatistics(appointmentsData, chartName)
            new Chartist.Bar('.ct-chart-' + chartName, data, getOptions());
        }
    }

    function initSelectListeners(appointmentsData, chartName) {
        $("#year-selector-" + chartName).on("change", () => {
            redrawChart(appointmentsData, chartName);
        })

        $("#month-selector-" + chartName).on("change", () => {
            redrawChart(appointmentsData, chartName);
        })
    }

    function reloadAppointmentPercentStatistics(appointmentsData, chartName) {
        let labels = []
        let totalSeries = []
        let monthSeries = []

        appointmentsData.forEach(appointment => {
            let appointmentDate = new Date(appointment.appointmentDateTime);

            if (!labels.includes(appointment.serviceFacade.name)) {
                labels.push(appointment.serviceFacade.name);
                totalSeries.push(1);
                monthSeries.push(0)
            } else {
                totalSeries[labels.indexOf(appointment.serviceFacade.name)] += 1;
            }

            if (appointmentDate.getMonth().toString() === $("#month-selector-" + chartName).val().toString()
                && appointmentDate.getFullYear().toString() === $("#year-selector-" + chartName).val().toString()) {
                monthSeries[labels.indexOf(appointment.serviceFacade.name)] += 1;
            }
        })

        let totalSeriesSum = totalSeries.reduce(add, 0); // with initial value to avoid when the array is empty
        let monthSeriesSum = monthSeries.reduce(add, 0);
        for (let i = 0; i < totalSeries.length; i++) {
            totalSeries[i] = totalSeries[i] * 100 / totalSeriesSum;
        }
        for (let i = 0; i < monthSeries.length; i++) {
            monthSeries[i] = monthSeries[i] * 100 / monthSeriesSum;
        }

        return {
            labels: labels,
            series: [totalSeries, monthSeries]
        }
    }

    function reloadAppointmentsStatistics(appointmentsData, chartName) {
        let labels = []
        let series = []
        for (let i = 1; i <= new Date($("#year-selector-" + chartName).val(),
            $("#month-selector-" + chartName).val(), 11).daysInMonth(); i++) {
            labels.push(i);
            series.push(0);
        }
        appointmentsData.forEach(appointment => {
            let appointmentDate = new Date(appointment.appointmentDateTime);
            if (appointmentDate.getMonth().toString() === $("#month-selector-" + chartName).val().toString()
                && appointmentDate.getFullYear().toString() === $("#year-selector-" + chartName).val().toString()) {

                series[appointmentDate.getDate() - 1] += 1;
            }
        })

        return {
            labels: labels,
            series: [series]
        }
    }

    function initSelectors(chartName) {
        $("#month-selector-" + chartName).val(new Date().getMonth())
        $("#year-selector-" + chartName).html('');
        for (let i = -5; i < 5; i++) {
            let year = new Date().getFullYear() + i;
            if (i === 0) {
                $("#year-selector-" + chartName).append($("<option value='" + year + "' selected>"
                    + year + "</option>"))
            } else {
                $("#year-selector-" + chartName).append($("<option value='" + year + "'>"
                    + year + "</option>"))
            }
        }
    }

    function getOptions() {
        return {
            width: 600,
            height: 300,
            axisY: {
                type: Chartist.AutoScaleAxis,
                onlyInteger: true,
                low: 0
            },
            lineSmooth: false
        };
    }

    function add(accumulator, a) {
        return accumulator + a;
    }
}