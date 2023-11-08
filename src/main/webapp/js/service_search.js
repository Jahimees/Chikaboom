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
            async: false,
            data: {
                serviceSubtypeIds: serviceSubtypeIdListStr
            },
            success: function (data) {
                console.log("Endpoint 18 done::: ")
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
                console.log("Endpoint 10 done::: ")
                data.forEach(function (serviceSubtypeFacade) {
                    let divMediumText = $("<div class='medium-text'></div>")
                    let input = $("<input type='checkbox' class='service-subtype-checkbox' id='" +
                        serviceSubtypeFacade.idServiceSubtype + "'>")
                    let label = $("<label for='" + serviceSubtypeFacade.idServiceSubtype + "'></label>")
                        .text(serviceSubtypeFacade.name)

                    divMediumText.append(input);
                    divMediumText.append(label);

                    $("#service-subtype-block").append(divMediumText);
                    let serviceTypeNamePlaceHolder = $("#service-type-name-placeholder");
                    serviceTypeNamePlaceHolder.text(serviceSubtypeFacade.serviceTypeFacade.name)
                    serviceTypeNamePlaceHolder.attr("style", "text-decoration: none; color: #523870")
                })
            }
        })
    }

    function fillResultSearchTable(serviceFacadeListJson) {
        const searchResultPlaceHolder = $("#search-result-placeholder");
        searchResultPlaceHolder.html("");

        if (serviceFacadeListJson.length !== 0) {
            serviceFacadeListJson.forEach(function (serviceFacade) {
                let idMasterAccount = serviceFacade.accountFacade.idAccount;

                if (typeof $("#card-" + idMasterAccount)[0] === "undefined") {
                    const totalName = extractTotalName(serviceFacade.accountFacade);

                    const divCard = $('<div id="card-' + idMasterAccount + '" style="margin: 0 20px 10px 0"></div>');
                    const divFront = $('<div class="front-side"></div>');
                    const imgAvatar = $("<img class='result-image' onerror=\"this.src='../../../image/user/no_photo.jpg'\" " +
                        "src='/image/user/" + idMasterAccount + "/avatar.jpeg'>")
                    const divName = $("<div class='result-item-name'>" + totalName + "</div>")

                    const divBack = $('<div class="back-side"></div>');
                    const accLink = $('<a href="/chikaboom/account/' + idMasterAccount + '" ' +
                        'class="btn btn-light non-decorated-link">Ссылка на аккаунт</a>')
                    const hr = $('<hr style="margin: 10px 0 0 0">')

                    divFront.append(imgAvatar);
                    divFront.append(divName);

                    divBack.append(accLink);
                    divBack.append(hr);

                    divCard.append(divFront);
                    divCard.append(divBack);

                    $("#search-result-placeholder").append(divCard);

                    $.ajax({
                        method: "get",
                        url: "/accounts/" + idMasterAccount + "/comments",
                        contentType: "application/json",
                        success: (commentsJson) => {
                            let positive = 0;
                            let negative = 0;
                            commentsJson.forEach(comment => {
                                if (comment.good) {
                                    positive++;
                                } else {
                                    negative++;
                                }
                            })

                            const avg = wilson_score(positive, negative);
                            const divInfo = $("<div class='result-item'><p class='small-text card-rating-text'>" +
                                "Рейтинг: " + Math.round(avg * 100) / 100 + " | " + positive + " <i style='color: darkgreen' class='fas fa-thumbs-up'></i> " +
                                negative + "<i style='color: darkred' class='fas fa-thumbs-down'></i>" +
                                "</p></div>");
                            divFront.append(divInfo);
                            $("#card-" + idMasterAccount).attr('avg', avg);
                            sortCards();
                        },
                        error: () => {
                            callMessagePopup("Ошибка", "Что-то пошло не так. Невозможно загрузить отзывы");
                        }
                    })
                }

                $("#card-" + idMasterAccount + " > .back-side").append(
                    $('<div class="service-elem">' + serviceFacade.name + ' - '
                        + serviceFacade.price + 'р.</div>')
                )
            })
            sortCards();
            rebindCards();
        } else {
            const divLbl = $("<div class='common-text'></div>").text("Поиск не дал результатов...");

            searchResultPlaceHolder.append(divLbl);
        }
    }

    function sortCards() {
        let cardArray = $( "div[id^=card-]" ).sort(function(a, b) {
            return b.getAttribute('avg') - a.getAttribute('avg');
        })
        cardArray.each(function (index) {
            $("#search-result-placeholder").append($(this))
        })
    }

    function rebindCards() {
        $(".back-side").unbind();
        $(".back-side").on("click", (e) => {
            $(e.currentTarget).fadeOut(100, () => {
                $(e.currentTarget.previousElementSibling).fadeIn(100);
            });
        })

        $(".front-side").unbind();
        $(".front-side").on("click", (e) => {
            $(e.currentTarget).fadeOut(100, () => {
                $(e.currentTarget.nextElementSibling).fadeIn(100)
            });
        })
    }

}
