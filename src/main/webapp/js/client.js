function fillClientsTable(clientsJSON) {

    if (typeof $("#default_table").DataTable() !== 'undefined') {
        $("#default_table").DataTable().data().clear();
    }

    clientsJSON.forEach(function (clientDetails) {
        let phoneText = "";
        if (clientDetails.userDetails !== null) {
            phoneText = clientDetails.phoneCode ? "+" + clientDetails.phoneCode.phoneCode + " " + clientDetails.phone : " ";
        }

        let name = (clientDetails.firstName ? clientDetails.firstName + " " : " ")
        + (clientDetails.lastName ? clientDetails.lastName : "");

        $("#default_table").DataTable().row.add([
            name ? name : "Безымянный",
            phoneText,
            clientDetails.visitCount,
            new Date(clientDetails.lastVisitDate).toLocaleDateString('ru')
        ]).draw();
    })
}