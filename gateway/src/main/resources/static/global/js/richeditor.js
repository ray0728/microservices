function initEditor(info) {
    console.log(info);
    $('#summernote').summernote({
        placeholder:info,
        height: 400,
        fontSizes: ['12', '14', '16', '18', '24', '36', '48'],
        toolbar: [
            ['fontname',['fontname']],
            ['style', ['bold', 'italic', 'underline', 'clear']],
            ['fontsize', ['fontsize']],
            ['color', ['color']],
            ['para', ['ul', 'ol', 'paragraph']],
            ['height', ['height']],
            ['insert',['picture','video','link','table','hr']],
            ['misc',['fullscreen','undo','redo']]
        ],
        disableDragAndDrop:true,
        shortcut:false
    });
};