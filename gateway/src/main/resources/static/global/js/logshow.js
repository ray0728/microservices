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