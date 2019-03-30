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


$('#uploadmodal').on('shown.bs.modal', function () {
    var title = $("#logform").find('input[type="text"]');
    var files = $("#logform").find('video[class="note-video-clip"]');
    var header = $(this).find('h4[class="modal-title"]');
    var content = $(this).find('div[id="content"]');
    if (title.val() == "") {
        title.css("border-color", "red");
        this.hide();
    }else if(files.length > 0){
        header.text("Upload Files");
        content.html(dynamicsBody(files));
    }
    console.log("submit");
    // $("#logform").submit();
});

dynamicsBody = function (files) {
    body = [];
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
    $.each(files, function (index, file) {
        console.log(file);
        var filename = file.getAttribute("data-filename");
        var url = file.src;
        body.push('<tr class="table-warning">');
        body.push('<td>' + (index + 1) + '</td>');
        body.push('<td value=' + url + '>' + filename + '</td>');
        body.push('<td >Waiting</td>');
        body.push('</tr>')
    });
    body.push('</tbody>');
    body.push('</table>');
    return body.join('');
};

showDialog = function (dialog) {
    var ui = $.summernote.ui;
    console.log("show dialog");
    return $.Deferred(function (deferred) {
        var $cancelBtn = dialog.find('button[class="btn btn-danger"]');
        ui.onDialogShown(dialog, function () {
            deferred.resolve(true);
            $cancelBtn.click(function (event) {
                event.preventDefault();
                deferred.resolve(false);
            });
        });
        ui.onDialogHidden(dialog, function () {
            $cancelBtn.off('click');
            if (deferred.state() === 'pending') {
                deferred.reject();
            }

        });
        ui.showDialog(dialog);
    });
};

blobToFile = function (url, filename) {

}