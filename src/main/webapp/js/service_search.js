{
    function doSearch(idServiceType) {
        let serviceSubtypeIdList = [];

        Array.from($(".service-subtype-checkbox")).forEach(function (serviceSubtypeCheckbox) {
            if (serviceSubtypeCheckbox.checked) {
                serviceSubtypeIdList.push(serviceSubtypeCheckbox.getAttribute("id"));
            }
        })

        let serviceSubtypeIdListStr = JSON.stringify(serviceSubtypeIdList)
            .replace('[', '')
            .replace(']', '')
            .replaceAll("\"", '');

        $.ajax({
            type: "get",
            url: "/service-types/" + idServiceType + "/service-subtypes/services",
            contentType: "application/json",
            dataType: "json",
            data: {
                serviceSubtypeIds: serviceSubtypeIdListStr
            },
            success: function (data) {
                fillResultSearchTable(data);
            }
        })
    }

    function loadSubtypeData(idServiceType) {
        $("#service-subtype-block").empty();

        $.ajax({
            method: "get",
            url: "/service-types/" + idServiceType + "/service-subtypes",
            contentType: "application/json",
            dataType: "json",
            async: false,
            success: function (data) {
                data.forEach(function (subtype) {
                    let divMediumText = document.createElement("div");
                    divMediumText.setAttribute("class", "medium-text")

                    let input = document.createElement("input");
                    input.setAttribute("type", "checkbox");
                    input.setAttribute("class", "service-subtype-checkbox");
                    input.setAttribute("id", subtype.idServiceSubtype);

                    let label = document.createElement("label");
                    label.setAttribute("for", subtype.idServiceSubtype);
                    label.innerText = subtype.name;

                    divMediumText.appendChild(input);
                    divMediumText.appendChild(label);

                    $("#service-subtype-block").append(divMediumText);
                    let serviceTypeNamePlaceHolder = $("#service-type-name-placeholder")[0];
                    serviceTypeNamePlaceHolder.innerText = subtype.serviceType.name;
                    serviceTypeNamePlaceHolder.setAttribute("style", "text-decoration: none; color: #523870")
                })
            }
        })
    }

    function fillResultSearchTable(serviceListJson) {
        let searchResultPlaceHolder = $("#search-result-placeholder");
        searchResultPlaceHolder.html("");

        if (serviceListJson.length !== 0) {
            serviceListJson.forEach(function (service) {
                let price = service.price;
                let serviceName = service.name;
                let idMasterAccount = service.account.idAccount
                let masterName = service.account.username;

                let accountLink = $("<a class='col-xl-3 non-decorated-link' href='/chikaboom/account/" + idMasterAccount + "'></a>")
                let imgAvatar = $("<img class='result-image' src='/image/user/" + idMasterAccount + "/avatar.jpeg'>")
                let divName = $("<div class='result-item-name'></div>")
                let pName = $("<p class='small-white-text'></p>").text(masterName);
                let divInfo = $("<div class='result-item'></div>");
                let pServiceName = $("<p class='small-white-text'></p>").text(serviceName);
                let pPrice = $("<p class='small-text' style='background-color: antiquewhite; " +
                    "border-radius: 2px; text-align: center; font-weight: bold'></p>").text(price + " руб.");

                divName.append(pName);
                divInfo.append(pServiceName, pPrice);
                accountLink.append(imgAvatar, divName, divInfo);
                searchResultPlaceHolder.append(accountLink);
            })
        } else {
            let divLbl = $("<div class='common-text'></div>").text("Поиск не дал результатов...");

            searchResultPlaceHolder.append(divLbl);
        }
    }
}