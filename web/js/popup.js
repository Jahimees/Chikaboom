$('.open-login-popup').on('click', function (e) {
    e.preventDefault();
    $('.login-popup-bg').fadeIn(800);
    $('.login-popup').fadeIn(800);
});

function closeLoginPopup() {
    $('.login-popup-bg').fadeOut(800);
    $('.login-popup').fadeOut(800);
}

$('.close-login-popup').on('click',  function () {
    closeLoginPopup();
});

$('.login-popup-bg').on('click', function () {
    closeLoginPopup();
})

$('.open-register-popup').on('click', function (e) {
    e.preventDefault();
    $('.register-popup-bg').fadeIn(800);
    $('.register-popup').fadeIn(800);
});

function closeRegisterPopup() {
    $('.register-popup-bg').fadeOut(800);
    $('.register-popup').fadeOut(800);
}

$('.close-register-popup').on('click',  function () {
    closeRegisterPopup();
});

$('.register-popup-bg').on('click', function () {
    closeRegisterPopup();
})

