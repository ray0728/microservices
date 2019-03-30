$(document).ready(function () {
    $('#summernote').summernote({
        placeholder: "Let's write",
        height: 400,
        fontSizes: ['12', '14', '16', '18', '24', '36', '48'],
        toolbar: [
            ['fontname', ['fontname']],
            ['style', ['bold', 'italic', 'underline', 'clear']],
            ['fontsize', ['fontsize']],
            ['color', ['color']],
            ['para', ['ul', 'ol', 'paragraph']],
            ['height', ['height']],
            ['insert', ['picture', 'myvideo', 'link', 'table', 'hr']],
            ['misc', ['fullscreen', 'undo', 'redo']]
        ],
        disableDragAndDrop: true,
        shortcut: false
    });
});


$("#logform").submit(function (event) {
    console.log("handler for submit");
    var title = $(this).find('input[type="text"]').val();
    var category = $("#category").find(":selected").val();
    var code = $('#summernote').summernote('code');
    console.log(title);
    console.log(category);
    console.log(code);
    if ("" == title) {
        $(this).find('input[type="text"]').css("border-color", "red");
        event.preventDefault();
    } else {
        $(this).find('input[type="hidden"]').val(code);
    }
});

$("#previewmodal").on('show.bs.modal', function () {
    console.log("modal shown");
    var title = $("#logform").find('input[type="text"]').val();
    var code = $('#summernote').summernote('code');
    var header = $(this).find('h4[class="modal-title"]');
    var content = $(this).find('div[id="content"]');
    header.text(title);
    content.html(code);
});

$('#addcategory').click(function () {
    var dialog = this.parent().parent();
    var input = dialog.find('input[id="classifier"]');
    var userdefcategory = input.val();
    var shoudadd = true;
    $("#category options").each(function () {
        console.log($(this).text());
        if ($(this).text() == userdefcategory) {
            shoudadd = false;
        }
    });
    if (shoudadd) {
        var newoption = new Option(userdefcategory, userdefcategory);
        $('#category').append(newoption);
    }
    input.val("");
});

$('#btn_submit').click(function () {
    var title = $("#logform").find('input[type="text"]');
    var files = $("#logform").find('video[class="note-video-clip"]');
    console.log(files);
    console.log(files.length());
    if (title.val() == "") {
        title.css("border-bottom-color", "red");
    } else if (files.length() > 0) {
        this.$upload = uploadDialog(files);
        this.$upload.show();
    } else {
        console.log("submit");
        // $("#logform").submit();
    }
});

this.uploadDialog = function () {
    var ui = $.summernote.ui
    var self = this;
    this.initialize = function (files) {
        self.createUploadDialog(files);
    };

    this.createUploadDialog = function (files) {
        var $uploadDialog = $('<div class="modal" aria-hidden="false" tabindex="-1" role="dialog"/>');
        node = [];
        node.push('<div class="modal-dialog">');
        node.push('<div class="modal-content">');
        node.push('<div class="modal-header">');
        node.push('<h4 class="modal-title">Upload files</h4>');
        node.push('</div>');
        node.push('<div class="modal-body modal-div" id="content">');
        dynamicsBody(node, files);
        node.push('</div>');
        node.push('<div class="modal-footer">');
        node.push('<button type="button" class="btn btn-danger" data-dismiss="modal">Cancel</button>');
        node.push('</div>');
        node.push('</div>');
        node.push('</div>');
        console.log(node);
        $uploadDialog.html(node.join(''));
        self.$dialog = $uploadDialog[0].render().append('body');
    };

    this.dynamicsBody = function (body, files) {
        body.push('<div class="table-responsive-md">');
        body.push('<table class="table">');
        body.push('<thead  class="thead-light">');
        body.push('<tr>');
        body.push('<th>#</th>');
        body.push('<th>File</th>');
        body.push('<th>Status</th>');
        body.push('</tr>');
        body.push('</thead>');
        body.push('<tbody>');
        JQuery.each(files, function (index, file) {
            var filename = file.data("filename");
            var url = file.src;
            body.push('<tr>');
            body.push('<td>' + (index + 1) + '</td>');
            body.push('<td value=' + url + '>' + filename + '</td>');
            body.push('</tr>')
        });
        body.push('</tbody>');
        body.push('</table>');
        return body;
    };

    this.show = function () {
        self.showDilaog().then(function (isupload) {
            console.log(isupload);
            // var file = new File([blob], "uploaded_file.jpg", { type: "image/jpeg", lastModified: Date.now() })
        }).fail(function () {
            self.$dialog.remove;
        });
    };

    this.showDialog = function () {
        return $.Deferred(function (deferred) {
            var $cancelBtn = self.$dialog.find('button[class="btn btn-danger"]');
            ui.onDialogShown(self.$dialog, function () {
                deferred.resolve(true);
                $cancelBtn.click(function (event) {
                    event.preventDefault();
                    deferred.resolve(false);
                });
            });
            ui.onDialogHidden(self.$dialog, function () {
                $cancelBtn.off('click');
                if (deferred.state() === 'pending') {
                    deferred.reject();
                }

            });
            ui.showDialog(self.$dialog);
        });
    }
};