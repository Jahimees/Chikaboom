$('.open-popup').on('click', function (e) {
    e.preventDefault();
    $('.popup-bg').fadeIn(800);
    $('html').addClass('no-scroll');
});

$('.close-popup').on('click', function () {
    $('.popup-bg').fadeOut(800);
    $('html').removeClass('no-scroll');
});
