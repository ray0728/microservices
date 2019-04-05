(function (factory) {
    /* global define */
    if (typeof define === 'function' && define.amd) {
        // AMD. Register as an anonymous module.
        define(['jquery'], factory);
    } else if (typeof module === 'object' && module.exports) {
        // Node/CommonJS
        module.exports = factory(require('jquery'));
    } else {
        // Browser globals
        factory(window.jQuery);
    }
}(function ($) {
    var myVideoPlugin = function (context) {
        var self = this;
        var options = context.options;
        var lang = options.langInfo;
        var ui = $.summernote.ui;

        context.memo('button.myvideo', function () {
            var button = ui.button({
                contents: ui.icon(options.icons.video),
                tooltip: lang.video.video,
                click: function () {
                    self.show();
                }
            });
            return button.render();
        });

        this.initialize = function () {
            self.createVideoDialog();
        };

        this.createVideoDialog = function () {
            var body = [
                '<div class="form-group note-form-group note-group-select-from-files">',
                '<label class="note-form-label">' + lang.image.selectFromFiles + '</label>',
                '<input class="note-image-input note-form-control note-input" type="file" name="files" accept="video/*"" />',
                '</div>',
                '<div class="form-group note-form-group row-fluid">',
                "<label class=\"note-form-label\">" + lang.video.url + " <small class=\"text-muted\">" + lang.video.providers + "</small></label>",
                '<input class="note-video-url form-control note-form-control note-input" type="text" />',
                '</div>'
            ].join('');
            var buttonClass = 'btn btn-primary note-btn note-btn-primary note-video-btn';
            var footer = "<input type=\"button\" href=\"#\" class=\"" + buttonClass + "\" value=\"" + lang.video.insert + "\" disabled>";
            this.$dialog = ui.dialog({
                title: lang.video.insert,
                fade: options.dialogsFade,
                body: body,
                footer: footer
            }).render().appendTo('body');
        };

        this.show = function () {
            context.invoke('editor.saveRange');
            self.showVideoDialog().then(function (data) {
                context.invoke('editor.restoreRange');
                let nodeid = "vid_" + $.now();
                let node = self.createVideoNode(nodeid, data);
                if (node) {
                    context.invoke('editor.insertNode', node);
                }
                context.invoke('editor.insertParagraph');
                let options = {
                    fluid:true,
                    controls:true,
                    preload:'auto',
                    userActions: {
                        doubleClick: videoDoubleClickHandler
                    }
                };
                console.log(node);
                console.log(nodeid);
                videojs(nodeid, options);
                ui.hideDialog(self.$dialog);
            }).fail(function () {
                context.invoke('editor.restoreRange');
            });
        };

        function videoDoubleClickHandler(e) {
            console.log(e);
        }

        this.showVideoDialog = function () {
            return $.Deferred(function (deferred) {
                var $videoInput = self.$dialog.find('.note-image-input');
                var $videoUrl = self.$dialog.find('.note-video-url');
                var $videoBtn = self.$dialog.find('.note-video-btn');
                ui.onDialogShown(self.$dialog, function () {
                    context.triggerEvent('dialog.shown');
                    $videoInput.replaceWith($videoInput.clone().on('change', function (event) {
                        deferred.resolve(event.target.files || event.target.value);
                    }).val(''));
                    $videoBtn.click(function (event) {
                        event.preventDefault();
                        deferred.resolve($videoUrl.val());
                    });
                    $videoUrl.on('keyup paste', function () {
                        var url = $videoUrl.val();
                        ui.toggleBtn($videoBtn, url);
                    }).val('');
                });
                ui.onDialogHidden(self.$dialog, function () {
                    $videoInput.off('change');
                    $videoUrl.off('input');
                    $videoBtn.off('click');
                    if (deferred.state() === 'pending') {
                        deferred.reject();
                    }
                });
                ui.showDialog(self.$dialog);
            })
        };

        this.createVideoNode = function (id, data) {
            var datatype = toString.call(data);
            if (datatype == "[object String]") {
                return self.createVideoNodeByUrl(id, data);
            } else if (datatype == "[object FileList]") {
                return self.createVideoNodeByFile(id, data);
            }
            return false;
        };

        this.createVideoNodeByFile = function (id, file) {
            console.log(file[0]);
            let video = $('<video controls>')
                .attr("id", id)
                .attr("data-filename", file[0].name);
            video.addClass('video-js');
            video.addClass('vjs-big-play-centered');
            video.html('<source src="' + URL.createObjectURL(file[0]) + '" type="' + file[0].type +'">');
            return video[0];
        };

        this.createVideoNodeByUrl = function (id, url) {
            let ytRegExp = /\/\/(?:www\.)?(?:youtu\.be\/|youtube\.com\/(?:embed\/|v\/|watch\?v=|watch\?.+&v=))([\w|-]{11})(?:(?:[\?&]t=)(\S+))?$/;
            let ytRegExpForStart = /^(?:(\d+)h)?(?:(\d+)m)?(?:(\d+)s)?$/;
            let ytMatch = url.match(ytRegExp);
            let igRegExp = /(?:www\.|\/\/)instagram\.com\/p\/(.[a-zA-Z0-9_-]*)/;
            let igMatch = url.match(igRegExp);
            let vRegExp = /\/\/vine\.co\/v\/([a-zA-Z0-9]+)/;
            let vMatch = url.match(vRegExp);
            let vimRegExp = /\/\/(player\.)?vimeo\.com\/([a-z]*\/)*(\d+)[?]?.*/;
            let vimMatch = url.match(vimRegExp);
            let dmRegExp = /.+dailymotion.com\/(video|hub)\/([^_]+)[^#]*(#video=([^_&]+))?/;
            let dmMatch = url.match(dmRegExp);
            let youkuRegExp = /\/\/v\.youku\.com\/v_show\/id_(\w+)=*\.html/;
            let youkuMatch = url.match(youkuRegExp);
            let qqRegExp = /\/\/v\.qq\.com.*?vid=(.+)/;
            let qqMatch = url.match(qqRegExp);
            let qqRegExp2 = /\/\/v\.qq\.com\/x?\/?(page|cover).*?\/([^\/]+)\.html\??.*/;
            let qqMatch2 = url.match(qqRegExp2);
            if (ytMatch && ytMatch[1].length === 11) {
                var youtubeId = ytMatch[1];
                var start = 0;
                if (typeof ytMatch[2] !== 'undefined') {
                    var ytMatchForStart = ytMatch[2].match(ytRegExpForStart);
                    if (ytMatchForStart) {
                        for (var n = [3600, 60, 1], i = 0, r = n.length; i < r; i++) {
                            start += (typeof ytMatchForStart[i + 1] !== 'undefined' ? n[i] * parseInt(ytMatchForStart[i + 1], 10) : 0);
                        }
                    }
                }
                $video = $('<iframe>')
                    .attr('frameborder', 0)
                    .attr('src', '//www.youtube.com/embed/' + youtubeId + (start > 0 ? '?start=' + start : ''))
            } else if (igMatch && igMatch[0].length) {
                $video = $('<iframe>')
                    .attr('frameborder', 0)
                    .attr('src', 'https://instagram.com/p/' + igMatch[1] + '/embed/')
                    .attr('scrolling', 'no')
                    .attr('allowtransparency', 'true');
            } else if (vMatch && vMatch[0].length) {
                $video = $('<iframe>')
                    .attr('frameborder', 0)
                    .attr('src', vMatch[0] + '/embed/simple')
                    .attr('class', 'vine-embed');
            } else if (vimMatch && vimMatch[3].length) {
                $video = $('<iframe webkitallowfullscreen mozallowfullscreen allowfullscreen>')
                    .attr('frameborder', 0)
                    .attr('src', '//player.vimeo.com/video/' + vimMatch[3])
            } else if (dmMatch && dmMatch[2].length) {
                $video = $('<iframe>')
                    .attr('frameborder', 0)
                    .attr('src', '//www.dailymotion.com/embed/video/' + dmMatch[2])
            } else if (youkuMatch && youkuMatch[1].length) {
                $video = $('<iframe webkitallowfullscreen mozallowfullscreen allowfullscreen>')
                    .attr('frameborder', 0)
                    .attr('src', '//player.youku.com/embed/' + youkuMatch[1]);
            } else if ((qqMatch && qqMatch[1].length) || (qqMatch2 && qqMatch2[2].length)) {
                var vid = ((qqMatch && qqMatch[1].length) ? qqMatch[1] : qqMatch2[2]);
                $video = $('<iframe webkitallowfullscreen mozallowfullscreen allowfullscreen>')
                    .attr('frameborder', 0)
                    .attr('src', 'http://v.qq.com/iframe/player.html?vid=' + vid + '&amp;auto=0');
            } else if (mp4Match || oggMatch || webmMatch) {
                $video = $('<video controls>')
                    .attr('src', url)
            } else {
                return false;
            }
            $video.attr('width', '100%').attr('height', 'auto').attr("data-filename", "netfile");
            $video.addClass('note-video-clip');
            return $video[0];
        };
    };
    $.extend($.summernote.plugins, {
        'myvideo': myVideoPlugin
    });
}));