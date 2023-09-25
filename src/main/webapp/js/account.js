{
    let servicesJson;

    function initializePage(idAccount) {
        accountJson = loadAccount(idAccount);

        let nameText;
        if (accountJson.userDetails != null) {
            nameText = (accountJson.userDetails.firstName ? accountJson.userDetails.firstName + " " : "")
                + (accountJson.userDetails.lastName ? accountJson.userDetails.lastName + " " : "")
        }
        $("#username-placeholder").text(nameText ? nameText : "@" + accountJson.username);
        if (accountJson.accountSettings.phoneVisible) {
            let phoneText = "Телефон: " + accountJson.userDetails.displayedPhone

            $("#phone-placeholder").text(phoneText)
}
        if (isMaster(accountJson)) {
            servicesJson = loadMastersServices(accountJson.idAccount);
            fillServiceTable(servicesJson, true);
            initAppointmentModal(servicesJson)
            // fillServicesToModal(servicesJson);
            // fillWorkingDaysToModal(accountJson);

            // masterAppointmentsJson = loadMastersAppointments(accountJson.idAccount);
            let addressData = accountJson.address != null ? accountJson.address : "";
            $("#address-placeholder").text("Адрес: " + addressData);
            $("#profession-placeholder").text(accountJson.userDetails.about !== null ? accountJson.userDetails.about.profession : "");
            $("#about-text-placeholder").text(accountJson.userDetails.about !== null ? accountJson.userDetails.about.text : "");
        } else {
            $(".master-only").remove();
            $(".main-information").css("height", "auto");
        }
    }

    function loadAccount(idAccount) {
        var accountJson

        $.ajax({
            contentType: "application/json",
            dataType: "json",
            method: "get",
            async: false,
            url: "/accounts/" + idAccount,
            success: function (data) {
                return accountJson = data;
            },
            error: function () {
                location.href = "/chikaboom/404";
            }
        });

        return accountJson;
    }

    function isMaster(accountJson) {
        var result = false;

        accountJson.roles.forEach(function (role) {
            if (role.name === "ROLE_MASTER") {
                result = true;
            }
        });

        return result;
    }
}