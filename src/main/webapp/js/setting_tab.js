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
    loadPersonalizationSetting();
});

$("#security-setting-tab").on("click", function () {
    selectCurrentSetting(this);
    loadSecuritySetting();
});

$("#notification-setting-tab").on("click", function () {
    selectCurrentSetting(this);
    loadNotificationSetting();
});

$("#payment-details-setting-tab").on("click", function () {
    selectCurrentSetting(this);
    loadPaymentDetailsSetting();
});

function selectCurrentSetting(thisObj) {
    Array.from($(".menu-box-horizontal > div")).forEach(function (elem) {
        elem.setAttribute("selected", "false");
    });

    thisObj.setAttribute("selected", "true");
}

