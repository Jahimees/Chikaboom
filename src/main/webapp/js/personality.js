{
    function loadConcreteTab(idAccount, thisObj, tabName) {
        selectCurrent(thisObj)

        $.ajax({
            type: "get",
            url: "/chikaboom/personality/" + idAccount + "/" + tabName,
            contentType: "application/text",
            dataType: "text",
            data: {},
            success: function (data) {
                $("#content-placeholder").html(data);
            },
            error: function () {
                callMessagePopup("Ошибка", "Невозможно загрузить вкладку " + tabName + "!")
            }
        });
    }

    function loadStatistic(idAccount, thisObj) {
        selectCurrent(thisObj);
        underConstruction();
    }

    function loadMessages(idAccount, thisObj) {
        selectCurrent(thisObj)
        underConstruction();
    }

    function loadReviews(idAccount, thisObj) {
        selectCurrent(thisObj)
        underConstruction();
    }

    function underConstruction() {
        $.ajax({
            type: "get",
            url: "/chikaboom/under_construction",
            contentType: "application/text",
            dataType: "text",
            data: {},
            success: function (data) {
                $("#content-placeholder").html(data);
            },
            error: function () {
                console.error("ERROR")
            }
        });
    }

    function selectCurrent(thisObj) {
        Array.from($(".menu-box > div")).forEach(function (elem) {
            elem.setAttribute("selected", "false");
        });

        thisObj.setAttribute("selected", "true");
    }

    function setPhoneVisibility(isPhoneVisible, idAccount) {
        $.ajax({
            type: "PATCH",
            url: "/accounts/" + idAccount + "/settings",
            contentType: "application/json",
            dataType: "json",
            data: JSON.stringify({
                phoneVisible: isPhoneVisible
            }),
            error: function () {
                callMessagePopup("Проблема на сервере!", "Что-то пошло не так! Невозможно установить видимость телефона")
            }
        })
    }

///////////////////////////////FILLING PROGRESS BAR///////////////////////////

    function progressView() {
        let diagramBox = document.querySelectorAll('.diagram.progress');
        diagramBox.forEach((box) => {
            let deg = (360 * box.dataset.percent / 100) + 180;
            if (box.dataset.percent >= 50) {
                box.classList.add('over_50');
            } else {
                box.classList.remove('over_50');
            }
            box.querySelector('.piece.right').style.transform = 'rotate(' + deg + 'deg)';
        });
    }

    function countPercentage() {
        let usernameVal = $("#username-placeholder").val();
        let emailVal = $("#email-placeholder").val();
        let phoneVal = $("#phone-placeholder").val();
        let photoSrc = $(".personality-avatar-image").attr("src");
        let nameVal = $("#name-placeholder").val();

        let addressVal = false;

        if (typeof $("#address-placeholder") !== "undefined") {
            addressVal = $("#address-placeholder").val();
        }

        let aboutProfessionVal = false;
        let aboutTextVal = false;

        if (typeof $("#about-profession-placeholder") !== "undefined") {
            aboutProfessionVal = $("#about-profession-placeholder").val();
            aboutTextVal = $("#about-text-placeholder").val();
        }

        let piecesCount = 0;
        let emptyPiecesCount = 0;

        piecesCount++;
        if (usernameVal === null || usernameVal === "") {
            emptyPiecesCount++;
        }

        piecesCount++;
        if (emailVal === null || emailVal === "") {
            emptyPiecesCount++;
        }

        piecesCount++;
        if (phoneVal === null || phoneVal === "") {
            emptyPiecesCount++;
        }

        piecesCount++;
        if (nameVal === null || nameVal === "") {
            emptyPiecesCount++;
        }


        piecesCount++;
        if (photoSrc === "../../../image/user/no_photo.jpg") {
            emptyPiecesCount++;
        }

        if (addressVal !== false) {
            piecesCount++;
            if (addressVal === "") {
                emptyPiecesCount++;
            }
        }

        if (aboutProfessionVal !== false && aboutTextVal !== false) {
            piecesCount++;
            if (aboutProfessionVal === "") {
                emptyPiecesCount++;
            }

            if (aboutTextVal === "") {
                emptyPiecesCount++;
            }
        }

        let percentage = Math.round(((piecesCount - emptyPiecesCount) * 100) / piecesCount);
        $("#progress-percent-placeholder").text(percentage + "%");
        $(".diagram").attr("data-percent", percentage);
        progressView();
    }

///////////////////////////////WINDOW VIEW (RESIZE, STYLE)////////////////////////////////////////
    $(document).ready(function () {
        resizeFlexBox();
    })


    function resizeFlexBox() {
        let height = $(window).height();
        $(".flex-box-purple").attr("style", "height: " + height + "px");
    }

///////////////////////////////LOGOUT////////////////////////////////////////////
    $("#logout-form").submit(() => {
        $.ajax({
            method: "post",
            url: "/logout?isRequestFromUI=true",
            success: () => {
                console.log("logout...")
                location.href = '/chikaboom/service/search/2';
            }
        })
    })

//////////////////////////////Images controls///////////////////////////////

    function loadUserFilesToTable(idAccount) {
        userFilesCache = loadUserFiles(idAccount);
        $("#user-files-table").html('');

        if (typeof userFilesCache != "undefined") {
            userFilesCache.reverse().forEach((userFile) => {
                if (!userFile.filePath.includes("avatar")) {
                    let tr = $("<tr></tr>");
                    let td1 = $("<td class='col-md-3'></td>");
                    let img = $("<img style='width: 150px; height: 150px; object-fit: cover' src='" + userFile.filePath.replace('src/main/webapp', '') + "'>")
                    let td2 = $("<td class='margin-0-10 col-md-2 violet-button'>X</td>");
                    tr.append(td1);
                    tr.append(td2);
                    td1.append(img);
                    td2.attr("onclick", "callConfirmDeleteUserFile(" + userFile.idUserFile + ")");
                    $("#user-files-table").append(tr);
                }
            });
        }
    }

    function callConfirmDeleteUserFile(idUserFile) {
        callMessagePopup("Удаление", "Вы действительно хотите удалить фотографию?")

        $("#decline-message-btn").css("display", "block");
        $("#confirm-message-btn").attr("onclick", "deleteUserFile(" + idUserFile + ")");
    }

    function deleteUserFile(idUserFile) {
        $.ajax({
            method: 'delete',
            url: '/accounts/' + accountFacadeJson.idAccount +'/user_files/' + idUserFile,
            success: () => {
                callMessagePopup("Удалено", "Фотография успешно удалена")
                reloadUserFiles(accountFacadeJson.idAccount);
                loadUserFilesToTable(accountFacadeJson.idAccount);
            },
            error: () => {
                callMessagePopup("Ошибка", "Произошла ошибка удаления")
            }
        })
    }
    function uploadImage(idAccount) {
        let formData = new FormData();
        formData.append("file", $("#image-input")[0].files[0]);
        let uuid = self.crypto.randomUUID();
        formData.append("fileName", "gallery/" + uuid + ".jpeg");
        if (window.FormData === undefined) {
            alert('В вашем браузере FormData не поддерживается')
        }
        $.ajax({
            method: "POST",
            dataType: "json",
            contentType: false,
            processData: false,
            async: false,
            url: "/accounts/" + idAccount + "/user_files",
            data: formData,
            statusCode: {
                201: function () {
                    callMessagePopup("Фотография успешно загружена!", "Ваше новое фото профиля успешно было загружено!");
                    reloadUserFiles(idAccount);
                    loadUserFilesToTable(idAccount)
                },
                400: function () {
                    callMessagePopup("Ошибка!", "Произошла ошибка! Фотографию не удалось загрузить!");
                }
            }
        })
    }

    function chooseNewImage() {
        $("#image-input").click();
    }
}
