var restclock;
var tickerclock;
$(document).ready(function(){
    if ($.fn.simpleTicker) {
        tickerclock = $.simpleTicker($("#breakingNewsTicker"), {
            speed: 1000,
            delay: 3500,
            easing: 'swing',
            effectType: 'roll'
        });
    }
    setInterval(checkNews, 10000);
});

checkNews = function () {
    clearInterval(tickerclock);
    $("#newsarea").load("/news");
    restclock = setInterval(resetNews, 1000);
}

resetNews = function () {
    if ($.fn.simpleTicker) {
        tickerclock = $.simpleTicker($("#breakingNewsTicker"), {
            speed: 1000,
            delay: 3500,
            easing: 'swing',
            effectType: 'roll'
        });
    }
    clearInterval(restclock);
}