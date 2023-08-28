function fillClientsTable(clientsJSON) {

    if (typeof $("#default_table").DataTable() !== 'undefined') {
        $("#default_table").DataTable().data().clear();
    }

    clientsJSON.forEach(function (client) {

        $("#default_table").DataTable().row.add([
            client.username,
            client.phoneCode.phoneCode + " " + client.phone,
            client.visitCount,
            new Date(client.lastVisitDate).toLocaleDateString('ru')
        ])
            .draw()
            .node();
    })
}