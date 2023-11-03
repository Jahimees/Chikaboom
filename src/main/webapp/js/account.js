{
    let servicesFacadeJson;
    let userFilesCache;

    function initializePage(idAccount, idLoggedAccount) {
        accountFacadeJson = loadAccount(idAccount);

        if (accountFacadeJson.userDetailsFacade != null) {
            nameText = (accountFacadeJson.userDetailsFacade.firstName ? accountFacadeJson.userDetailsFacade.firstName + " " : "")
                + (accountFacadeJson.userDetailsFacade.lastName ? accountFacadeJson.userDetailsFacade.lastName + " " : "")
        }
        $("#username-placeholder").text(nameText ? nameText : "@" + accountFacadeJson.username);
        if (accountFacadeJson.accountSettingsFacade.phoneVisible) {
            let phoneText = "Телефон: " + accountFacadeJson.userDetailsFacade.displayedPhone
            $("#phone-placeholder").text(phoneText)
        }
        if (isMaster(accountFacadeJson)) {
            servicesFacadeJson = loadMastersServices(accountFacadeJson.idAccount);
            fillServiceTable(servicesFacadeJson, true);
            initAppointmentModal(false, servicesFacadeJson)

            const addressData = accountFacadeJson.address != null ? accountFacadeJson.address : "";
            $("#address-placeholder").text("Адрес: " + addressData);
            $("#profession-placeholder").text(typeof accountFacadeJson.userDetailsFacade.aboutFacade !== "undefined" ?
                accountFacadeJson.userDetailsFacade.aboutFacade.profession : "");
            $("#about-text-placeholder").text(typeof accountFacadeJson.userDetailsFacade.aboutFacade !== "undefined" ?
                accountFacadeJson.userDetailsFacade.aboutFacade.text : "");

            $("#photo-container").html('');
            userFilesCache = loadUserFiles(accountFacadeJson.idAccount);
            if (typeof userFilesCache !== "undefined") {

                for (let i = userFilesCache.length - 1; i >= 0; i--) {
                    if (!userFilesCache[i].filePath.includes("avatar")) {
                        const a = $('<a href="' + userFilesCache[i].filePath.replace("src/main/webapp", "") + '" ' +
                            'data-toggle="lightbox" ' +
                            'data-gallery="example-gallery" class="col-sm-2 my-lightbox-toggle"> ' +
                            '</a>');
                        const img = $('<img src="' + userFilesCache[i].filePath.replace("src/main/webapp", "") + '" ' +
                            'class="img-fluid">');
                        if (i < userFilesCache.length - 5) {
                            a.attr("hidden", true);
                        }

                        a.append(img);
                        $("#photo-container").append(a);
                    }
                }
            }

            initStarImage(idAccount, idLoggedAccount)
        } else {
            $(".master-only").remove();
            $(".main-information").css("height", "auto");
        }
    }

    function reloadUserFiles(idAccount) {
        userFilesCache = undefined;
        loadUserFiles(idAccount);
    }

    function loadUserFiles(idAccount) {
        let userFiles;
        if (typeof userFilesCache !== "undefined") {
            userFiles = userFilesCache;
        } else {
            $.ajax({
                method: 'get',
                url: '/accounts/' + idAccount + '/user_files',
                async: false,
                success: (data) => {
                    userFilesCache = data;
                    userFiles = data;
                },
                error: () => {
                    callMessagePopup("Ошибка!", "Невозможно загрузить галерею!");
                }
            })
        }

        return userFiles;
    }

    function loadAccount(idAccount) {
        var accountFacadeJson

        $.ajax({
            contentType: "application/json",
            dataType: "json",
            method: "get",
            async: false,
            url: "/accounts/" + idAccount,
            success: function (data) {
                console.log("Endpoint 1 done::: ");
                accountFacadeJson = data;
            },
            error: function () {
                location.href = "/chikaboom/404";
            }
        });

        return accountFacadeJson;
    }

    function isMaster(accountFacadeJson) {
        let result = false;

        accountFacadeJson.rolesFacade.forEach(function (role) {
            if (role.name === "ROLE_MASTER") {
                result = true;
            }
        });

        return result;
    }

    ////////////////////////COMMENTS//////////////////////////

    function reloadComments(idAccount, idAccountClient) {
        $.ajax({
            method: "get",
            url: "/accounts/" + idAccount + "/comments",
            async: false,
            contentType: "application/json",
            success: (commentsJson) => {
                commentsCache = commentsJson;
            },
            error: (data) => {
                callMessagePopup("Ошибка", "Что-то пошло не так. Невозможно загрузить отзывы");
            }
        })
        loadCommentsToPage(idAccountClient);
    }

    function deleteComment(idAccount, idComment, idAccountClient) {

        $.ajax({
            method: "delete",
            url: "/accounts/" + idAccount + "/comments/" + idComment,
            success: () => {
                let commentIndx = -1;
                const condition = (element) => {
                    if (element.idComment === idComment) {
                        return commentsCache.indexOf(element);
                    }
                };
                commentIndx = commentsCache.some(condition)
                commentsCache.splice(commentIndx, 1);
                loadComments(idAccount, idAccountClient)
            }
        })
    }

    function initDeleteCommentBind(idAccountClient) {
        const $deleteCommentBtn = $("#delete-comment-btn");

        $deleteCommentBtn.unbind();
        $deleteCommentBtn.on("click", (thisObj) => {
            callMessagePopup("Удаление", "Вы действительно хотите удалить комментарий")

            const idComment = thisObj.target.getAttribute("comment-id");
            const idAccount = thisObj.target.getAttribute("account-id");
            $("#decline-message-btn").css("display", "block");
            $("#confirm-message-btn").attr("onclick", "deleteComment(" + idAccount + ", " + idComment +
                ", " + idAccountClient + ")");

        })
    }

    function initCreateCommentBind(idAccountMaster, idAccountClient) {
        const $sendComment = $("#send-comment");
        $sendComment.unbind();
        $sendComment.on("click", () => {
            const commentText = $("#comment-text-area").val();
            if (commentText.length > 500 || commentText.length === 0) {
                $("#invalid-text-lbl").show();
                return;
            } else {
                $("#invalid-text-lbl").hide();
            }

            const isGood = $("input[name='like']:checked:radio").val();
            if (idAccountClient === "") {
                callMessagePopup("Авторизация", "Чтобы оставлять комментарии необходимо авторизоваться!");
                return;
            }

            const newComment = {
                accountClientFacade: {
                    idAccount: idAccountClient,
                },
                accountMasterFacade: {
                    idAccount: idAccountMaster
                },
                text: secureCleanValue(commentText),
                good: isGood
            }

            $.ajax({
                method: "post",
                url: "/accounts/" + idAccountMaster + "/comments",
                dataType: "json",
                contentType: "application/json",
                data: JSON.stringify(newComment),
                success: () => {
                    reloadComments(idAccountMaster, idAccountClient)
                    initDeleteCommentBind(idAccountClient);
                },
                error: () => {
                    callMessagePopup("Ошибка", "Невозможно создать комментарий. Возможно, Вы пытаетесь создать комментарий" +
                        " пользователю, которому Вы уже ранее оставляли комментарий. Вы можете удалить свой" +
                        " предыдущий комментарий.");
                }

            })
        })
    }

    function loadComments(idAccountMaster, idAccountClient) {
        if (typeof commentsCache === "undefined") {
            reloadComments(idAccountMaster, idAccountClient);
        } else {
            loadCommentsToPage(idAccountClient);
        }
    }

    function loadCommentsToPage(idAccountClient) {
        const commentsContainer = $("#comments-container");
        commentsContainer.html("");

        let positiveCount = 0;
        let negativeCount = 0;
        commentsCache.forEach(comment => {

            const divImageContainer = $("<div>" +
                "<img class='feedback-image' src='../../../image/user/" +
                comment.accountClientFacade.idAccount + "/avatar.jpeg' onerror=\"this.src='../../../image/user/no_photo.jpg'\">" +
                "<div class='small-text'>" + new Date(comment.date).toLocaleDateString('ru') + "</div>" +
                "</div>");

            let totalName = extractTotalName(comment.accountClientFacade);

            let isGoodImg
            if (comment.good) {
                isGoodImg = "<i style='padding-left: 5px' class='fas fa-thumbs-up'></i>";
                positiveCount++;
            } else {
                isGoodImg = "<i style='padding-left: 5px' class='fas fa-thumbs-down'></i>"
                negativeCount++;
            }

            let deleteCommentBtn = "";
            if (idAccountClient !== ""
                && +idAccountClient === comment.accountClientFacade.idAccount) {
                deleteCommentBtn = "<div id='delete-comment-btn' account-id='" + comment.accountMasterFacade.idAccount + "' " +
                    "comment-id='" + comment.idComment + "' " +
                    "class='small-text-button'>Удалить</div>";
            }

            const divReview = $("<div class='review-text-block'>" +
                "<div style='white-space: nowrap' class='medium-text'>" + totalName + isGoodImg + "</div>" +
                "<div class='horizontal-blue-line'></div>" +
                "<div>" + comment.text + "</div>" +
                deleteCommentBtn +
                "</div>");

            const divContainer = $("<div class='d-inline-flex margin-10-20-px' " +
                "style='width: 500px; " +
                "white-space: normal; " +
                "word-break: break-all;'></div>")
            divContainer.append(divImageContainer);
            divContainer.append(divReview);

            commentsContainer.append(divContainer);
        })

        fillStatisticFields(positiveCount, negativeCount);
    }

    function fillStatisticFields(positiveCount, negativeCount) {
        let avg = wilson_score(positiveCount, negativeCount);
        $("#avg-rating").text("ОБЩИЙ РЕЙТИНГ: " + Math.round(avg * 100) / 100);
        $("#pos-rating").html(positiveCount + " <i class='fas fa-thumbs-up'></i>")
        $("#neg-rating").html(negativeCount + " <i class='fas fa-thumbs-down'></i>")
    }
}