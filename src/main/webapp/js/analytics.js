{
    let appointmentsDataCache;
    let clientsDataCache;

    function initFullChart(chartName, idAccount) {
        initSelectors(chartName);
        initSelectListeners(chartName, idAccount);
        redrawChart(chartName, idAccount)
    }

    function redrawChart(chartName, idAccount) {
        switch (chartName) {
            case "appointments": {
                loadAppointmentsCache(idAccount);

                let data = reloadAppointmentsStatistics(chartName)
                new Chartist.Line('.ct-chart-' + chartName, data, getOptions());
                break;
            }
            case "appointments-percent": {
                loadAppointmentsCache(idAccount);

                let data = reloadAppointmentPercentStatistics(chartName)
                new Chartist.Bar('.ct-chart-' + chartName, data, getOptions());
                break;
            }
            case "new-clients": {
                loadClientsCache(idAccount);

                let data = reloadClientsNewStatistics(chartName);
                Chartist.Line('.ct-chart-' + chartName, data, getOptions(1000));
            }
        }
    }

    function initSelectListeners(chartName, idAccount) {
        let chartNames = [];
        if (chartName === "new-clients") {
            chartNames.push(chartName + "-start");
            chartNames.push(chartName + "-end");
        } else {
            chartNames.push(chartName);
        }

        chartNames.forEach(cn => {
            $("#year-selector-" + cn).on("change", () => {
                redrawChart(chartName, idAccount);
            })

            $("#month-selector-" + cn).on("change", () => {
                redrawChart(chartName, idAccount);
            })
        })
    }

    function reloadAppointmentPercentStatistics(chartName) {
        let labels = []
        let totalSeries = []
        let monthSeries = []

        appointmentsDataCache.forEach(appointment => {
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

    function reloadAppointmentsStatistics(chartName) {
        let labels = []
        let series = []
        for (let i = 1; i <= new Date($("#year-selector-" + chartName).val(),
            $("#month-selector-" + chartName).val(), 11).daysInMonth(); i++) {
            labels.push(i);
            series.push(0);
        }
        appointmentsDataCache.forEach(appointment => {
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

    function reloadClientsNewStatistics(chartName) {
        let labels = []
        let series = []
        let startMonthSelector = $("#month-selector-" + chartName + "-start");
        let endMonthSelector = $("#month-selector-" + chartName + "-end");
        let startYearSelector = $("#year-selector-" + chartName + "-start");
        let endYearSelector = $("#year-selector-" + chartName + "-end");

        if (+startYearSelector.val() > +endYearSelector.val()
            || (+startYearSelector.val() === +endYearSelector.val()
                && +startMonthSelector.val() > +endMonthSelector.val())) {
            callMessagePopup("Ошибка", "Выберите корректную дату");
            return;
        }

        for (let year = startYearSelector.val(); year <= +endYearSelector.val(); year++) {
            if (year === endYearSelector.val() && year === startYearSelector.val()) {
                for (let month = +startMonthSelector.val(); month <= +endMonthSelector.val(); month++) {
                    labels.push(+month + 1 + "." + year);
                    series.push(0)
                }
            } else if (year === +endYearSelector.val() && year !== +startYearSelector.val()) {
                for (let month = 0; month <= +endMonthSelector.val(); month++) {
                    labels.push(+month + 1 + "." + year);
                    series.push(0)
                }
            } else if (year === startYearSelector.val() && year !== endYearSelector.val()) {
                for (let month = +startMonthSelector.val(); month <= 11; month++) {
                    labels.push(+month + 1 + "." + year);
                    series.push(0)
                }
            } else {
                for (let month = 0; month <= 11; month++) {
                    labels.push(+month + 1 + "." + year);
                    series.push(0)
                }
            }
        }

        clientsDataCache.forEach(clientData => {
            if (typeof clientData.firstVisitDate !== "undefined") {
                let clientFirstVisitDate = new Date(clientData.firstVisitDate);
                labels.forEach(lbl => {
                    let lblDate = new Date(lbl.split('.')[1], lbl.split('.')[0] - 1, 10)
                    if (lblDate.getMonth() === clientFirstVisitDate.getMonth()
                        && lblDate.getFullYear() === clientFirstVisitDate.getFullYear()) {
                        series[labels.indexOf(lbl)]++;
                    }
                })
            }
        })

        return {
            labels: labels,
            series: [
                series
            ]
        };
    }

    function initSelectors(chartName) {
        let chartNames = [];
        if (chartName === "new-clients") {
            chartNames.push(chartName + "-start");
            chartNames.push(chartName + "-end");
        } else {
            chartNames.push(chartName);
        }

        chartNames.forEach(ch => {
            $("#month-selector-" + ch).val(new Date().getMonth())
            $("#year-selector-" + ch).html('');
            for (let i = -5; i < 5; i++) {
                let year = new Date().getFullYear() + i;
                if (i === 0) {
                    $("#year-selector-" + ch).append($("<option value='" + year + "' selected>"
                        + year + "</option>"))
                } else {
                    $("#year-selector-" + ch).append($("<option value='" + year + "'>"
                        + year + "</option>"))
                }
            }
        })
    }

    function getOptions(customWidth) {
        return {
            width: typeof customWidth !== "undefined" ? customWidth : 600,
            height: 300,
            axisY: {
                type: Chartist.AutoScaleAxis,
                onlyInteger: true,
                low: 0
            },
            lineSmooth: false
        };
    }

    function loadAppointmentsCache(idAccount) {
        if (typeof appointmentsDataCache === "undefined") {
            appointmentsDataCache = loadAppointmentsData(idAccount, true);
        }
    }

    function loadClientsCache(idAccount) {
        if (typeof clientsDataCache === "undefined") {
            clientsDataCache = loadClients(idAccount)
        }
    }

    function add(accumulator, a) {
        return accumulator + a;
    }
}