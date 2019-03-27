$(document).ready(function () {
    initEditor("write here...");
});

function initEditor(info) {
    console.log(info);
    $('#summernote').summernote({
        placeholder: info,
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
};

function addcategory(object) {
    var dialog = $(object).parent().parent();
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
        // $(newoption).html("option text");
        $('#category').append(newoption);
    }
    input.val("");
};

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