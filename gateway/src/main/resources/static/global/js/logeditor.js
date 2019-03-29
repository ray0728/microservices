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
    if(title.val() == ""){
        title.css("border-bottom-color", "red");
    }else if(files.size > 0){

    }else{
        $("#logform").submit();
    }
});

function showUploadDialog(files) {
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
    var buttonClass = 'btn btn-danger';
    var footer = "<input type=\"button\" href=\"#\" class=\"" + buttonClass + "\" value=\"Close\">";
    this.$dialog = ui.dialog({
        title: lang.video.insert,
        fade: false,
        body: body,
        footer: footer
    }).render().appendTo('body');
}