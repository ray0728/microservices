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


$('#uploadmodal').on('shown.bs.modal', function () {
    let title = $("#logform").find('input[type="text"]');
    let files = $("#logform").find('video[class="vjs-tech"]');
    let header = $(this).find('h4[class="modal-title"]');
    let content = $(this).find('div[id="content"]');
    if (title.val() == "") {
        title.css("border-color", "red");
        $('#uploadmodal').modal("hide");
    } else if (files.length > 0) {
        header.text("Upload Files");
        content.html(dynamicsBody(files));
        processUpload(0);
    }else {
        // $('#uploadmodal').modal("hide");
        console.log("submit");
        // $("#logform").submit();
    }
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
        let filename = file.getAttribute("data-filename");
        let url = file.src;
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


processUpload = function (lid) {
    let trlist = $('#uploadmodal').find('tr[class="table-warning"]');
    $.each(trlist, function (index, row) {
        let obj = $(row).find("td:eq(1)");
        let progress = $(row).find("td:eq(2)");
        let url = $(obj).attr("value");
        let filename = $(obj).text();
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

sliceUpload = function (lid, file, chunkSize) {
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
                    fileReader.readAsBinaryString(filedata);
                } else if (res == "abort") {
                    console.log("abort upload");
                } else if (currentChunk + 1 < chunks) {
                    currentChunk++;
                    start = currentChunk * chunkSize;
                    end = start + chunkSize >= file.size ? file.size : start + chunkSize;
                    filedata = blobSlice.call(file, start, end);
                    fileReader.readAsBinaryString(filedata);
                }
            },
            error: function (res) {
                console.log(res);
            }
        });
    };
    let filedata = blobSlice.call(file, start, end);
    fileReader.readAsBinaryString(filedata);

};