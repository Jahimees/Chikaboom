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
                    let divMediumText = $("<div class='medium-text'></div>")
                    let input = $("<input type='checkbox' class='service-subtype-checkbox' id='" + subtype.idServiceSubtype + "'>")
                    let label = $("<label for='" + subtype.idServiceSubtype + "'></label>").text(subtype.name)

                    divMediumText.appendChild(input);
                    divMediumText.appendChild(label);

                    $("#service-subtype-block").append(divMediumText);
                    let serviceTypeNamePlaceHolder = $("#service-type-name-placeholder");
                    serviceTypeNamePlaceHolder.text(subtype.serviceType.name)
                    serviceTypeNamePlaceHolder.attr("style", "text-decoration: none; color: #523870")
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