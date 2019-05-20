var clock;
$(document).ready(function(){
    window.setInterval(checkNews, 10000);
});

checkNews = function () {
    $("#newsarea").load("/news");
    clock = window.setInterval(resetNews, 1000);
}

resetNews = function () {
    if ($.fn.simpleTicker) {
        $.simpleTicker($("#breakingNewsTicker"), {
            speed: 1000,
            delay: 3500,
            easing: 'swing',
            effectType: 'roll'
        });
    }
    window.clearInterval(clock);
}