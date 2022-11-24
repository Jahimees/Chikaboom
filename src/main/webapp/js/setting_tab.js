$("#general-setting-tab").on("click", function () {
    selectCurrentSetting(this);
    loadSettingTab("general");
});

$("#profile-setting-tab").on("click", function () {
    selectCurrentSetting(this);
    loadSettingTab("profile");
});

$("#personalization-setting-tab").on("click", function () {
    selectCurrentSetting(this);
    loadUnderConstruction();
});

$("#security-setting-tab").on("click", function () {
    selectCurrentSetting(this);
    loadUnderConstruction();
});

$("#notification-setting-tab").on("click", function () {
    selectCurrentSetting(this);
    loadUnderConstruction();
});

$("#payment-details-setting-tab").on("click", function () {
    selectCurrentSetting(this);
    loadUnderConstruction();
});

function selectCurrentSetting(thisObj) {
    Array.from($(".menu-box-horizontal > div")).forEach(function (elem) {
        elem.setAttribute("selected", "false");
    });

    thisObj.setAttribute("selected", "true");
}

