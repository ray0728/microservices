$(document).ready(function () {
    let videos = $.find('video.video-js');
    let options = {
        fluid: true,
        controls: true,
        preload: 'auto',
        controlBar: {
            volumePanel: {
                inline: false
            },
            remainingTimeDisplay: false,
        }
    };
    $.each(videos, function (index, video) {
        videojs($(video).attr('id'), options);
    });
    $("#errinfo").hide();
});

$('.original-btn').click(function () {
    let formObj = $(this).parent().parent();
    let nameObj = $(formObj).find('input[name="name"]');
    let emailObj = $(formObj).find('input[name="email"]');
    let messageObj = $(formObj).find('textarea[name="message"]');
    let name = $(nameObj).val();
    let email = $(emailObj).val();
    let message = $(messageObj).val();
    let error = !name && !!($(nameObj).css("border-color", "red")) || !($(nameObj).css("border-color", ""));
    error = (!email && !!($(emailObj).css("border-color", "red")) || !($(emailObj).css("border-color", ""))) || error;
    error = (!message && !!($(messageObj).css("border-color", "red")) || !($(messageObj).css("border-color", ""))) || error;
    let titleObj = $.find('a.post-headline');
    if (!error) {
        let formData = new FormData();
        formData.append("id", $(titleObj).attr("value"));
        formData.append("name", name);
        formData.append("email", email);
        formData.append("message", message);
        formData.append("_csrf", $("meta[name='_csrf']").attr("content"));
        $.ajax({
            url: "/api/blog/res/reply/new",
            data: formData,
            type: "Post",
            cache: false,
            processData: false,
            contentType: false,
            success: function () {
                $('#comments').load("/blog/reply?id=" + $(titleObj).attr("value"));
                $(nameObj).val("");
                $(emailObj).val("");
                $(messageObj).val("");
            }
        });
    }
});

$('#cb_delete').change(function () {
    if ($(this).is(':checked')) {
        $("#btn_delete").removeAttr("disabled");
    } else {
        $("#btn_delete").attr("disabled", "true");
    }
});

$("#btn_edit").click(function () {
    let logid = $(this).data("id");
    window.location.href = "/blog/edit?id=" + logid;
});

$("#btn_delete").click(function () {
    let logid = $(this).data("id");
    $.ajax({
        url: '/api/res/blog/delete',
        type: 'DELETE',
        data: {
            '_csrf': $("meta[name='_csrf']").attr("content"),
            id: logid
        },
        success: function (result) {
            window.location.href = "/blog/list";
        },
        error: function () {
            $("#errinfo").is(':hidden') && $("#errinfo").show();
            let spanobj = $("#errinfo").find("span");
            $(spanobj).text("Delete will not take effect");
        }
    });
});