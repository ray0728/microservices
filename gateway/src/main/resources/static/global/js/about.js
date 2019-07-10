$(document).ready(function () {
    let alltext = $.find('p[class="card-text"]')
    $.each(alltext, function (index, text) {
        limit(text, 3);
    })
});

function limit(obj, row) {
    $(obj).css("line-height", "20px");
    let objH = $(obj).height();
    if (objH <= row * 20) {
        return;
    } else {
        $(obj).css({
            "height": row * 20 + "px",
            "overflow": "hidden",
            "position": "relative"
        });
        let span1 = $("<span class='ellipsis'>...</span>");
        let span2 = $("<span class='op1'></span>");
        let span3 = $("<span class='op2'></span>");
        let span4 = $("<span class='op3'></span>");
        $(obj).append(span1);
        $(obj).append(span2);
        $(obj).append(span3);
        $(obj).append(span4);
    }
}