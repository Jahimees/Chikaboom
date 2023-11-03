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
            deleteFavorite(currentFavoriteCache.idFavorite, idAccountClient);
        }
    })

    function setStar(empty) {
        $addOrRemoveFavorite.children(0).remove();
        let img;
        if (empty) {
            img = $("<img class='w-70px' src='../../image/icon/favorite_star.png'>");
        } else {
            img = $("<img class='w-70px' src='../../image/icon/favorite_star_filled.png'>");
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

            const visibleName = extractTotalName(favoriteFacade.favoriteMasterFacade);
            nameText = "<a href='/chikaboom/account/" + favoriteFacade.favoriteMasterFacade.idAccount + "'>"
                + visibleName + "</a>";

            const phone = favoriteFacade.favoriteMasterFacade.userDetailsFacade.displayedPhone;
            phoneText = phone ? phone : "Номер скрыт";
            const rowNode = $dataTable.DataTable().row.add([
                "<img class='favorite-master-img' onerror=\"this.src='../../image/user/no_photo.jpg'\" " +
                "src='../../image/user/" + favoriteFacade.favoriteMasterFacade.idAccount + "/avatar.jpeg'>",
                nameText,
                phoneText,
                "<img onclick='callConfirmDeleteFavorite(" + favoriteFacade.idFavorite + ")' src='/image/icon/cross_icon.svg' " +
                "class='delete-button'>"
            ])
                .draw()
                .node();
        });
    }

    function callConfirmDeleteFavorite(idFavorite) {
        callMessagePopup("Удаление", "Вы действительно хотите удалить мастера из избранного?")

        $("#decline-message-btn").css("display", "block");
        $("#confirm-message-btn").attr("onclick", "deleteFavorite(" + idFavorite + ")");
    }

    function deleteFavorite(idFavorite, idAccount) {
        let isPersonalityPage = typeof idAccount === "undefined";

        idAccount = idAccount ? idAccount : accountFacadeJson.idAccount;

        $.ajax({
            method: "delete",
            url: "/accounts/" + idAccount + "/favorites/" + idFavorite,
            contentType: "application/json",
            success: () => {
                if (isPersonalityPage) {
                    loadFavoritesTable(idAccount);
                } else {
                    setStar(true);
                    currentFavoriteCache = undefined;
                }
            },
            error: () => {
                callMessagePopup("Ошибка", "Не удалось удалить мастера из избранного");
            }
        })
    }
}
