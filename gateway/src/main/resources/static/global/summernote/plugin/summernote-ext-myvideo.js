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
    let myVideoPlugin = function (context) {
        let self = this;
        let options = context.options;
        let lang = options.langInfo;
        let ui = $.summernote.ui;

        context.memo('button.myvideo', function () {
            let button = ui.button({
                contents: ui.icon(options.icons.video),
                tooltip: lang.video.video,
                click: function () {
                    self.show();
                }
            });
            return button.render();
        });

        context.memo('button.removeVideo', function () {
            let button = ui.button({
                contents: ui.icon(options.icons.trash),
                click: function () {
                    self.removeVideoNode(self.$popover.attr("_id"));
                }
            });
            return button.render();
        });

        this.initialize = function () {
            self.createVideoDialog();
            self.createVideoPopover();
        };

        this.createVideoDialog = function () {
            let dialog = $('<div>');
            dialog.addClass("modal fade");
            dialog.attr("tabindex", "-1");
            let body = [
                '<div class="subscribe-newsletter-area">',
                '<div class="modal-dialog modal-dialog-centered" role="document">',
                '<div class="modal-content">',
                '<button type="button" class="close" data-dismiss="modal" aria-label="Close">',
                '<spanaria-hidden="true">&times;</span>',
                '</button>',
                '<div class="modal-body">',
                "<h5 class=\"title\">" + lang.video.insert + "</h5>",
                '<label for="select_file" class="btn original-btn">' + lang.image.selectFromFiles + '</label>',
                '<input id="select_file" type="file" accept="video/*" class="hidden"/>',
                '</div>',
                '</div>',
                '</div>',
                '</div>'
            ];
            this.$dialog = dialog.html(body.join("")).appendTo('body');
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
                videojs(nodeid, options, function () {
                    this.on('touchstart', function () {
                        self.updateVideoPopover(nodeid);
                    });
                });
                ui.hideDialog(self.$dialog);
            }).fail(function () {
                context.invoke('editor.restoreRange');
            });
        };

        this.createVideoPopover = function () {
            this.$popover = ui.popover({
                className: 'note-video-popover'
            }).render().appendTo('body');
            let content = this.$popover.find('.popover-content,.note-popover-content');
            context.invoke('buttons.build', content, ['remove', ['removeVideo']]);
        };

        this.updateVideoPopover = function (id) {
            let editable = context.layoutInfo.editable[0];
            let block = $(editable).find('div[id="' + id + '"]');
            let pos = $(block).offset();
            let height = $(block).innerHeight();
            let posEditor = $.summernote.dom.posFromPlaceholder(editable);
            $(".note-popover,.note-control-selection").css('display', 'none');
            self.$popover.css({
                display: 'block',
                left: pos.left,
                top: Math.min(pos.top + height, posEditor.top)
            });
            self.$popover.attr("_id", id);
        };

        this.removeVideoNode = function (id) {
            let editable = context.layoutInfo.editable[0];
            let block = $(editable).find('div[id="' + id + '"]');
            $(block).remove();
            self.$popover.css('display', 'none');
        };

        this.showVideoDialog = function () {
            return $.Deferred(function (deferred) {
                let videoInput = self.$dialog.find('input[type="file"]');
                ui.onDialogShown(self.$dialog, function () {
                    context.triggerEvent('dialog.shown');
                    videoInput.replaceWith(videoInput.clone().on('change', function (event) {
                        deferred.resolve(event.target.files || event.target.value);
                    }).val(''));
                });
                ui.onDialogHidden(self.$dialog, function () {
                    videoInput.off('change');
                    if (deferred.state() === 'pending') {
                        deferred.reject();
                    }
                });
                ui.showDialog(self.$dialog);
            })
        };

        this.createVideoNode = function (id, data) {
            if (toString.call(data) == "[object FileList]") {
                let video = $('<video controls>')
                    .attr("id", id)
                    .attr("data-filename", data[0].name);
                video.addClass('video-js');
                video.addClass('vjs-big-play-centered');
                video.html('<source src="' + URL.createObjectURL(data[0]) + '" type="' + data[0].type + '">');
                return video[0];
            }
            return false;
        };
    };

    $.extend($.summernote.plugins, {
        'myvideo': myVideoPlugin
    });
}));
