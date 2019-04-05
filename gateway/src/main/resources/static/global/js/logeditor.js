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
    var title = $(this).find('input[type="text"]').val();
    var category = $("#category").find(":selected").val();
    var code = $('#summernote').summernote('code');
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
    let title = $("#logform").find('input[type="text"]');
    let files = $("#logform").find('video[class="note-video-clip"]');
    let header = $(this).find('h4[class="modal-title"]');
    let content = $(this).find('div[id="content"]');
    if (title.val() == "") {
        title.css("border-color", "red");
        $('#uploadmodal').modal("hide");
    } else if (files.length > 0) {
        header.text("Upload Files");
        content.html(dynamicsBody(files));
        processUpload();
    }
    // $('#uploadmodal').modal("hide");
    console.log("submit");
    // $("#logform").submit();
});

dynamicsBody = function (files) {
    body = [];
    body.push('<div class="table-responsive-md">');
    body.push('<table class="table">');
    body.push('<thead class="thead-light">');
    body.push('<tr>');
    body.push('<th>#</th>');
    body.push('<th>File</th>');
    body.push('<th>Status</th>');
    body.push('</tr>');
    body.push('</thead>');
    body.push('<tbody>');
    $.each(files, function (index, file) {
        var filename = file.getAttribute("data-filename");
        var url = file.src;
        console.log(toString.call(url));
        body.push('<tr class="table-warning">');
        body.push('<td>' + (index + 1) + '</td>');
        body.push('<td value=' + url + '>' + filename + '</td>');
        body.push('<td >Waiting</td>');
        body.push('</tr>')
    });
    body.push('</tbody>');
    body.push('</table>');
    body.push('<div class="progress">');
    body.push('<div class="progress-bar progress-bar-striped progress-bar-animated" style="width:1%"></div>');
    body.push('</div>');
    return body.join('');
};


processUpload = function () {
    var trlist = $('#uploadmodal').find('tr[class="table-warning"]');
    $.each(trlist, function (index, row) {
        var obj = $(row).find("td:eq(1)");
        var status = $(row).find("td:eq(2)");
        var file = blobToFile($(obj).attr("value"), $(obj).text());
        console.log(file);
    });
};

blobToFile = function (url, filename) {
    console.log(url, filename);
    // return new File(toByteArray(url), filename, {type: "video/mp4", lastModified: Date.now()});
    var xhr = new XMLHttpRequest;
    xhr.responseType = 'blob';
    xhr.onload = function() {
        var recoveredBlob = xhr.response;
        var reader = new FileReader;
        reader.onload = function() {
            var blobAsDataUrl = reader.result;
            window.location = blobAsDataUrl;
        };
        reader.readAsDataURL(recoveredBlob);
        console.log(recoveredBlob);
    };
    xhr.open('GET', url);
    xhr.send();
};

toByteArray = function (str) {
    var result = [];
    for (var i = 0; i < str.length; i++) {
        result.push(str.charCodeAt(i).toString(2));
    }
    return resu
};