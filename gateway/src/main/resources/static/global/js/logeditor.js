var xhr_upload = [];
var abort_upload = false;
$(document).ready(function () {
    $('#summernote').summernote({
        popatmouse: false,
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

    $('#summernote').on('summernote.dialog.shown', function () {
        console.log('dialog shown');
        $(".note-video-popover").css('display', 'none');
    });

    $('#summernote').on('summernote.scroll', function () {
        console.log('page scroll');
        $(".note-video-popover").css('display', 'none');
    });

    $('#summernote').on('summernote.mousedown', function () {
        console.log('mousedown');
        $(".note-video-popover").css('display', 'none');
    });
});


$("#logform").submit(function (event) {
    let title = $(this).find('input[type="text"]').val();
    let category = $("#category").find(":selected").val();
    let code = $('#summernote').summernote('code');
    if ("" == title) {
        $(this).find('input[type="text"]').css("border-color", "red");
        event.preventDefault();
    } else {
        $(this).find('input[type="hidden"]').val(code);
    }
});

$("#previewmodal").on('show.bs.modal', function () {
    let title = $("#logform").find('input[type="text"]').val();
    let code = $('#summernote').summernote('code');
    let header = $(this).find('h4[class="modal-title"]');
    let content = $(this).find('div[id="content"]');
    header.text(title);
    content.html(code);
});

$('#addcategory').click(function () {
    let dialog = this.parent().parent();
    let input = dialog.find('input[id="classifier"]');
    let userdefcategory = input.val();
    let shoudadd = true;
    $("#category options").each(function () {
        if ($(this).text() == userdefcategory) {
            shoudadd = false;
        }
    });
    if (shoudadd) {
        let newoption = new Option(userdefcategory, userdefcategory);
        $('#category').append(newoption);
    }
    input.val("");
});

$('#uploadmodal').on('show.bs.modal', function () {
    let title = $("#logform").find('input[type="text"]');
    let files = $("#logform").find('video[class="vjs-tech"]');
    let header = $(this).find('h4[class="modal-title"]');
    let content = $(this).find('div[id="content"]');
    if (title.val() == "") {
        title.css("border-color", "red");
        $('#uploadmodal').modal("hide");
    } else if (files.length > 0) {
        header.text("Upload Files");
        content.html(dynamicsUploadFilesBody(files));
    } else {
        header.text("Create New Diary");
        content.html(createBody());
    }
});

$('#uploadmodal').on('shown.bs.modal', function () {
    let header = $(this).find('h4[class="modal-title"]');
    processUpload(0);
});

$('#uploadmodal').on('hide.bs.modal', function () {
    abort_upload = true;
    $.each(xhr_upload, function (index, filename) {
        $.ajax({
            url: "delete?filename=" + filename,
            type: "Delete",
            cache: false,
            processData: false,
            contentType: false
        });
    });
});

$('#uploadmodal').on('hidden.bs.modal', function () {
    abort_upload = false;
});

createBody = function () {
    body = [];
    body.push('<div class="progress progresswithlabel" value="' + url + '">');
    body.push('<div class="progress-bar progress-bar-striped bar" style="width:0%">');
    body.push('</div>')
    body.push('</div>')
    return body.join('');
};

dynamicsUploadFilesBody = function (files) {
    body = [];
    $.each(files, function (index, file) {
        let filename = file.getAttribute("data-filename");
        let url = file.src;
        body.push('<div class="progress progresswithlabel" value="' + url + '">');
        body.push('<div class="progress-bar progress-bar-striped bar" style="width:0%">');
        body.push('<span>' + filename + '</span>')
        body.push('</div>')
        body.push('</div>')
    });
    return body.join('');
};


processUpload = function (lid) {
    let trlist = $('#uploadmodal').find('tr[class="progress progresswithlabel"]');
    $.each(trlist, function (index, row) {
        let progress = $(row).find('div[class="progress-bar progress-bar-striped bar"]');
        let url = $(row).attr("value");
        let filename = $(row).find("span").text();
        let xhr = new XMLHttpRequest;
        xhr.responseType = 'blob';
        xhr.onload = function () {
            if (this.status == 200) {
                let blobdata = xhr.response;
                blobdata.name = filename;
                blobdata.lastModifiedDate = $.now();
                progress.text = "uploading";
                sliceUpload(lid, new File([blobdata], filename, {
                    type: blobdata.type,
                    lastModified: Date.now()
                }), 2097152, progress);
            }
        };
        xhr.open('GET', url, true);
        xhr.send();
    });
};

sliceUpload = function (lid, file, chunkSize, progress) {
    let chunks = Math.ceil(file.size / chunkSize);
    let currentChunk = 0;
    let checksum;
    let blobSlice = File.prototype.slice || File.prototype.mozSlice || File.prototype.webkitSlice;
    let spark = new SparkMD5();
    let fileReader = new FileReader();
    let start = 0;
    let end = chunkSize >= file.size ? file.size : chunkSize;

    fileReader.onload = function (e) {
        spark.appendBinary(e.target.result);
        checksum = spark.end();
    };

    fileReader.onloadend = function (e) {
        let formData = new FormData();
        formData.append("resid", lid);
        formData.append("index", currentChunk);
        formData.append("count", chunks);
        formData.append("filename", file.name);
        formData.append("file", filedata);
        formData.append("chunksize", chunkSize);
        formData.append("checksum", checksum);
        $.ajax({
            url: "upload",
            data: formData,
            type: "Post",
            cache: false,
            processData: false,
            contentType: false,
            success: function (res) {
                if (res == "resend") {
                    console.log("resend");
                    start = currentChunk * chunkSize;
                    end = start + chunkSize >= file.size ? file.size : start + chunkSize;
                    filedata = blobSlice.call(file, start, end);
                    if (!abort_upload) {
                        fileReader.readAsBinaryString(filedata);
                    }
                } else if (res == "abort") {
                    console.log("abort upload");
                    xhr_upload.splice(file.name, 1);
                } else if (currentChunk + 1 < chunks) {
                    currentChunk++;
                    progress.css('width:' + (currentChunk * 100 / chunks) + '%');
                    start = currentChunk * chunkSize;
                    end = start + chunkSize >= file.size ? file.size : start + chunkSize;
                    filedata = blobSlice.call(file, start, end);
                    if (!abort_upload) {
                        fileReader.readAsBinaryString(filedata);
                    }
                } else {
                    progress.css('width:100%');
                    xhr_upload.splice(file.name, 1);
                }
            },
            error: function (res) {
                console.log(res);
            }
        });
    };
    let filedata = blobSlice.call(file, start, end);
    if (!abort_upload) {
        fileReader.readAsBinaryString(filedata);
    }
    xhr_upload.push(file.name);

};