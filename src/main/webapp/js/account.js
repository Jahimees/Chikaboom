{
    let servicesFacadeJson;
    let userFilesCache;

    function initializePage(idAccount) {
        accountFacadeJson = loadAccount(idAccount);

        let nameText;
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

            let addressData = accountFacadeJson.address != null ? accountFacadeJson.address : "";
            $("#address-placeholder").text("Адрес: " + addressData);
            $("#profession-placeholder").text(accountFacadeJson.userDetailsFacade.aboutFacade !== null ?
                accountFacadeJson.userDetailsFacade.aboutFacade.profession : "");
            $("#about-text-placeholder").text(accountFacadeJson.userDetailsFacade.aboutFacade !== null ?
                accountFacadeJson.userDetailsFacade.aboutFacade.text : "");

            $("#photo-container").html('');
            userFilesCache = loadUserFiles(accountFacadeJson.idAccount);
            if (typeof userFilesCache !== "undefined") {

                for (let i = userFilesCache.length - 1; i >= 0; i--) {
                    if (!userFilesCache[i].filePath.includes("avatar")) {
                        let a = $('<a href="' + userFilesCache[i].filePath.replace("src/main/webapp", "") + '" ' +
                            'data-toggle="lightbox" ' +
                            'data-gallery="example-gallery" class="col-sm-2 my-lightbox-toggle"> ' +
                            '</a>');
                        let img = $('<img src="' + userFilesCache[i].filePath.replace("src/main/webapp", "") + '" ' +
                            'class="img-fluid">');
                        if (i < userFilesCache.length - 5) {
                            a.attr("hidden", true);
                        }

                        a.append(img);
                        $("#photo-container").append(a);
                    }
                }
            }
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
                return accountFacadeJson = data;
            },
            error: function () {
                location.href = "/chikaboom/404";
            }
        });

        return accountFacadeJson;
    }

    function isMaster(accountFacadeJson) {
        var result = false;

        accountFacadeJson.rolesFacade.forEach(function (role) {
            if (role.name === "ROLE_MASTER") {
                result = true;
            }
        });

        return result;
    }
}