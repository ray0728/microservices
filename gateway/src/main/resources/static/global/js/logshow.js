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
        formData.append("_csrf", $($.find('input[type="hidden"]')).val());
        $.ajax({
            url: "/blog/api/reply/new",
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

$('input[type="checkbox"]').change(function () {
    let btnobj = $(this).parent().find('.danger-btn');
    $(this).checked && $(btnobj).attr("disabled","");
});