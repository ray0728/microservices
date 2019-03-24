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
            ['insert', ['picture', 'video', 'link', 'table', 'hr']],
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
    $("#category").each(function () {
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
}