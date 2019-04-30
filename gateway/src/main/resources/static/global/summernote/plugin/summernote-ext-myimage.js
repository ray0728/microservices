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
    let myImagePlugin = function (context) {
        let self = this;
        let options = context.options;
        let lang = options.langInfo;
        let ui = $.summernote.ui;

        context.memo('button.myimage', function () {
            let button = ui.button({
                contents: ui.icon(options.icons.picture),
                tooltip: lang.image.image,
                click: function () {
                    self.show();
                }
            });
            return button.render();
        });

        this.initialize = function () {
            self.createImageDialog();
        };

        this.createImageDialog = function () {
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
                "<h5 class=\"title\">" + lang.image.insert + "</h5>",
                // '<div class="newsletterForm">',
                '<label for="select_file_img" class="btn original-btn">' + lang.image.selectFromFiles + '</label>',
                '<input id="select_file_img" type="file" accept="image/*" class="hidden" multiple="multiple"/>',
                '<h6 class="note-form-label">' + lang.image.url + '</h6>',
                '<input class="note-image-url form-control note-form-control note-input" type="text" />',
                '<button type="button" class="btn original-btn" data-dismiss="modal" disabled>' + lang.image.insert + '</button>',
                // '</div>',
                '</div>',
                '</div>',
                '</div>',
                '</div>'
            ];
            this.$dialog = dialog.html(body.join("")).appendTo('body');
        };

        this.show = function () {
            context.invoke('editor.saveRange');
            self.showImageDialog().then(function (data) {
                context.invoke('editor.restoreRange');
                let node = self.createImageNode(data);
                if (node) {
                    context.invoke('editor.insertNode', node);
                }
                context.invoke('editor.insertParagraph');
                ui.hideDialog(self.$dialog);
            }).fail(function () {
                context.invoke('editor.restoreRange');
            });
        };

        this.showImageDialog = function () {
            return $.Deferred(function (deferred) {
                let videoInput = self.$dialog.find('input[type="file"]');
                let videoUrl = self.$dialog.find('.note-image-url');
                let insertBtn = self.$dialog.find('button:contains("' + lang.image.insert + '")');
                ui.onDialogShown(self.$dialog, function () {
                    context.triggerEvent('dialog.shown');
                    videoInput.replaceWith(videoInput.clone().on('change', function (event) {
                        deferred.resolve(event.target.files || event.target.value);
                    }).val(''));
                    insertBtn.click(function (event) {
                        event.preventDefault();
                        deferred.resolve(videoUrl.val());
                    });
                    videoUrl.on('keyup paste', function () {
                        let url = videoUrl.val();
                        ui.toggleBtn(insertBtn, url);
                    }).val('');
                });
                ui.onDialogHidden(self.$dialog, function () {
                    videoInput.off('change');
                    videoUrl.off('input');
                    insertBtn.off('click');
                    if (deferred.state() === 'pending') {
                        deferred.reject();
                    }
                    deferred.resolve(videoUrl.val());
                });
                ui.showDialog(self.$dialog);
            })
        };

        this.createImageNode = function(data){
            let datatype = toString.call(data);
            let block = null;
            if (datatype == "[object FileList]") {
                block = $('<div>')
                $.each(data, function (index, file) {
                    let image = $('<img>')
                        .css("width", "100%")
                        .attr("data-filename", file.name)
                        .attr("src", URL.createObjectURL(file));
                    $(block).append(image);
                });
            }else if(datatype == "[object String]"){
                block=  $('<img>')
                    .css("width", "100%")
                    .attr("src", data);
            }
            if(block != null){
                return block[0];
            }
            return false;
        };
    };

    $.extend($.summernote.plugins, {
        'myimage': myImagePlugin
    });
}));
