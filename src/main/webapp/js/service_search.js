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

                $("#service-subtype-block")[0].appendChild(divMediumText);
                let serviceTypeNamePlaceHolder = $("#service-type-name-placeholder")[0];
                serviceTypeNamePlaceHolder.innerText = subtype.serviceType.name;
                serviceTypeNamePlaceHolder.setAttribute("style", "text-decoration: none; color: #523870")
            })
        }
    })
}

function fillResultSearchTable(serviceListJson) {
    let searchResultPlaceHolder = $("#search-result-placeholder")[0];
    searchResultPlaceHolder.innerHTML = "";

    if (serviceListJson.length !== 0) {
        serviceListJson.forEach(function (service) {
            let price = service.price;
            let serviceName = service.name;
            let idMasterAccount = service.account.idAccount
            let masterName = service.account.username;

            let accountHref = document.createElement("a");
            accountHref.setAttribute("href", "/chikaboom/account/" + idMasterAccount);
            accountHref.setAttribute("class", "col-xl-3 non-decorated-link");

            let img = document.createElement("img");
            img.setAttribute("class", "result-image");
            img.setAttribute("src", "/image/user/" + idMasterAccount + "/avatar.jpeg");

            let divName = document.createElement("div");
            divName.setAttribute("class", "result-item-name");

            let pName = document.createElement("p");
            pName.setAttribute("class", "small-white-text");
            pName.innerText = masterName;

            let divInfo = document.createElement("div");
            divInfo.setAttribute("class", "result-item");

            let pServiceName = document.createElement("p");
            pServiceName.setAttribute("class", "small-white-text");
            pServiceName.innerText = serviceName;

            let pPrice = document.createElement("p");
            pPrice.setAttribute("class", "small-text");
            pPrice.setAttribute("style", "background-color: antiquewhite; border-radius: 2px; text-align: center; font-weight: bold");
            pPrice.innerText = price + " руб.";

            divName.appendChild(pName);

            divInfo.appendChild(pServiceName);
            divInfo.appendChild(pPrice);

            accountHref.appendChild(img);
            accountHref.appendChild(divName);
            accountHref.appendChild(divInfo);

            searchResultPlaceHolder.appendChild(accountHref);
        })
    } else {
        let divLbl = document.createElement("div");
        divLbl.setAttribute("class", "common-text");
        divLbl.innerText = "Поиск не дал результатов...";

        searchResultPlaceHolder.appendChild(divLbl);
    }
}