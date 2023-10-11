{
    let servicesFacadeJson;

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
        } else {
            $(".master-only").remove();
            $(".main-information").css("height", "auto");
        }
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