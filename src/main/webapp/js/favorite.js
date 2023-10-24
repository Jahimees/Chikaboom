{
    let idAccountOwner;
    let idAccountClient;
    let currentFavoriteCache;
    let favoritesCache;
    const $addOrRemoveFavorite = $("#add-remove-favorite");

    function initStarImage(idAccount, idLoggedAccount) {
        idAccountOwner = idAccount;
        idAccountClient = idLoggedAccount;

        if (typeof idAccountClient !== "undefined" && idAccountOwner !== idAccountClient) {
            $.ajax({
                method: "get",
                url: "/favorites",
                data: {
                    idFavoriteMaster: idAccountOwner,
                    idFavoriteOwner: idAccountClient
                },
                success: (data) => {
                    setStar(false)
                    currentFavoriteCache = data;
                },
                error: (data) => {
                    setStar(true)
                    currentFavoriteCache = data.responseJSON;
                }
            })
        } else {
            $addOrRemoveFavorite.remove();
        }
    }

    $addOrRemoveFavorite.on("click", () => {
        if (typeof currentFavoriteCache == "undefined" ||
            currentFavoriteCache.status === 404
        ) {

            const favoriteFacade = {
                favoriteOwnerFacade: {
                    idAccount: idAccountClient
                },
                favoriteMasterFacade: {
                    idAccount: idAccountOwner
                }
            }

            $.ajax({
                method: "post",
                url: "/accounts/" + idAccountClient + "/favorites",
                dataType: "json",
                contentType: "application/json",
                data: JSON.stringify(favoriteFacade),
                success: (data) => {
                    setStar(false)
                    currentFavoriteCache = data
                }
            })
        } else {
            $.ajax({
                method: "delete",
                url: "/accounts/" + idAccountClient + "/favorites/" + currentFavoriteCache.idFavorite,
                contentType: "application/json",
                success: () => {
                    setStar(true);
                    currentFavoriteCache = undefined;
                }

            })
        }
    })

    function setStar(empty) {
        $addOrRemoveFavorite.children(0).remove();
        let img;
        if (empty) {
            img = $("<img style='width: 70px;' src='../../image/icon/favorite_star.png'>");
        } else {
            img = $("<img style='width: 70px;' src='../../image/icon/favorite_star_filled.png'>");
        }
        $addOrRemoveFavorite.append(img);
    }

    function loadFavoritesTable(idAccount) {
        $.ajax({
            method: "get",
            url: "/accounts/" + idAccount + "/favorites",
            contentType: "application/json",
            async: false,
            success: (favoritesFacadeJSON) => {
                favoritesCache = favoritesFacadeJSON;
            }
        })

        fillFavoritesTable()
    }

    function fillFavoritesTable() {
        const tableName = "favorites";
        const $dataTable = $("#favorites_table");

        destroyAndInitDataTable(tableName, $dataTable)

        favoritesCache.forEach(function (favoriteFacade) {
            let nameText, phoneText;

            let firstName = secureCleanValue(favoriteFacade.favoriteMasterFacade.userDetailsFacade.firstName);
            let lastName = secureCleanValue(favoriteFacade.favoriteMasterFacade.userDetailsFacade.lastName);
            firstName = firstName ? firstName + " " : "";
            lastName = lastName ? lastName : "";
            const visibleName = (firstName + lastName).trim() ? (firstName + lastName).trim()
                : favoriteFacade.favoriteMasterFacade.username;
            nameText = "<a href='/chikaboom/account/" + favoriteFacade.favoriteMasterFacade.idAccount + "'>"
                + visibleName + "</a>";

            const phone = favoriteFacade.favoriteMasterFacade.userDetailsFacade.displayedPhone;
            phoneText = phone ? phone : "Номер скрыт";
            const rowNode = $dataTable.DataTable().row.add([
                nameText,
                nameText,
                phoneText,
                "<img onclick='' src='/image/icon/cross_icon.svg' style='cursor: pointer; width: 15px'>"
            ])
                .draw()
                .node();
        });
    }

}
