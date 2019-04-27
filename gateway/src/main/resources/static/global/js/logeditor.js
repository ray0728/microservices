var xhr_upload = [];
var abort_upload = false;
$(document).ready(function () {
    $('#summernote').summernote({
        popatmouse: false,
        placeholder: "Let's write",
        height: $(window).height()/3,
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
        $(".note-video-popover").css('display', 'none');
    });

    $('#summernote').on('summernote.scroll', function () {
        $(".note-video-popover").css('display', 'none');
    });

    $('#summernote').on('summernote.mousedown', function () {
        $(".note-video-popover").css('display', 'none');
    });
});

$("#extmodal").on('show.bs.modal', function (e) {
    let header = $(this).find('h5[class="title"]');
    let content = $(this).find('div[class="modal-div"]');
    if ($(e.relatedTarget).text() == "Publish") {
        let files = $(".note-editable").find('video[class="vjs-tech"]');
        if (files.length > 0) {
            header.text("Upload Files");
            content.html(dynamicsUploadFilesBody(files));
        } else {
            header.text("Create New Diary");
            content.html(createBody());
        }
    } else {
        let title = $($.find('input[class="flex-grow-1 title"]')).val();
        let code = $('#summernote').summernote('code');
        header.text(title);
        content.html(code);
    }
});

$('#addLogType').on('hidden.bs.modal', function () {
    let input = $(this).find('input');
    let userdefcategory = $(input).val();
    let shoudadd = true;
    $("#category option").each(function () {
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

createLog = function () {
    let title = $($.find('input[class="flex-grow-1 title"]')).val();
    let category = $("#category").find(":selected").val();
    let formData = new FormData();
    let tags = $($.find('input[class="flex-grow-1"]')).val().split(";");
    formData.append("title", title);
    formData.append("type", category);
    formData.append("tags", tags);
    formData.append("_csrf", $($.find('input[type="hidden"]')).val());
    $.ajax({
        url: "/diary/api/res/new",
        data: formData,
        type: "Post",
        cache: false,
        processData: false,
        contentType: false,
        success: function (resid) {
            autoDetect(resid);
        },
        error: function (res) {
            console.log("post new err:" + res);
        }
    });
};

autoDetect = function (resid) {
    $.get("/diary/api/res/files?id=" + resid + "&onlyurl=true", function (url, status) {
        if (status != "success") {
            return;
        }
        switch ($('#extmodal').find('h5[class="title"]').text()) {
            case ("Upload Files"):
                processUpload(resid, url);
                break;
            case ("Create New Diary"):
                appendLog(resid, url);
                break;
        }
    });
};

$('#extmodal').on('shown.bs.modal', function (e) {
    if ($(e.relatedTarget).text() == "Publish") {
        let resid = $($('p:contains("Title")')[0]).val();
        let titleobj = $.find('input[class="flex-grow-1 title"]');
        let category = $("#category").find(":selected").val();
        let error = false;
        if ($(titleobj).val() == "") {
            $($('p:contains("Title")')[0]).css("color", "red");
            $(titleobj).css("border-color", "red");
            error = true;
        } else {
            $($('p:contains("Title")')[0]).css("color", "");
            $(titleobj).css("border-color", "");
        }
        if (typeof (category) == "undefined") {
            $($('p:contains("Category")')[0]).css("color", "red");
            $('#category').css("border-color", "red");
            error = true;
        } else {
            $($('p:contains("Category")')[0]).css("color", "");
            $('#category').css("border-color", "");
        }
        if (error) {
            $('#extmodal').modal('hide');
        } else if (resid == 0 || resid == "") {
            createLog();
        } else {
            autoDetect(resid);
        }
    }
});

$('#extmodal').on('hide.bs.modal', function (e) {
    if ($(e.relatedTarget).text() == "Publish") {
        abort_upload = true;
        $.each(xhr_upload, function (index, filename) {
            $.ajax({
                // TODO:missing log_id
                url: "/diary/api/res/files?name=" + filename,
                type: "Delete",
                cache: false,
                processData: false,
                contentType: false
            });
        });
    }
});

$('#extmodal').on('hidden.bs.modal', function (e) {
    if ($(e.relatedTarget).text() == "Publish") {
        abort_upload = false;
    }
});

createBody = function () {
    body = [];
    body.push('<div class="progress progresswithlabel">');
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
        body.push('<div class="progress progresswithlabel mb-2" value="' + url + '">');
        body.push('<div class="progress-bar progress-bar-striped bar" style="width:0%">');
        body.push('<span>' + filename + '</span>')
        body.push('</div>')
        body.push('</div>')
    });
    return body.join('');
};

appendLog = function (lid, url) {
    $('div.hiddendiv').html($('#summernote').summernote('code'));
    replaceVideoNode($('div.hiddendiv'), url);
    if (xhr_upload.length == 0) {
        $.post("/diary/api/res/update", {
            id: lid,
            log: $('div.hiddendiv').html(),
            _csrf: $($.find('input[type="hidden"]')).val()
        }, function (data, status) {
            if (status == "success") {
                $('#extmodal').modal("hide");
                window.location.href = "/diary/list";
            } else {
                console.log("post update err:" + status);
            }
        });
    }
};

replaceVideoNode = function (code, url) {
    let videonodes = $(code).find('div.video-js');
    $.each(videonodes, function (index, node) {
        let id = $(node).attr('id');
        let name = $(node).data('filename');
        let origial_video = [
            '<video controls class="video-js vjs-big-play-centered" id="' + id + '">',
            '<source src="' + url + "/" + name + '">',
            '</video>'
        ].join("");
        $(node).replaceWith(origial_video);
    });
};

processUpload = function (lid, url) {
    let uploadlist = $('#extmodal').find('div[class="progress progresswithlabel mb-2"]');
    $.each(uploadlist, function (index, data) {
        let progress = $(data).find('div[class="progress-bar progress-bar-striped bar"]');
        let url = $(data).attr("value");
        let filename = $(data).find("span").text();
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
                }), 2097152, progress, url);
            }
        };
        xhr.open('GET', url, true);
        xhr.send();
    });
};

sliceUpload = function (lid, file, chunkSize, progress, url) {
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
        formData.append("id", lid);
        formData.append("index", currentChunk);
        formData.append("total", chunks);
        formData.append("name", file.name);
        formData.append("file", filedata);
        formData.append("chunksize", chunkSize);
        formData.append("checksum", checksum);
        formData.append("_csrf", $($.find('input[type="hidden"]')).val());
        $.ajax({
            url: "/diary/api/res/upload",
            data: formData,
            type: "Post",
            cache: false,
            processData: false,
            contentType: false,
            success: function (res) {
                if (res == "resend") {
                    start = currentChunk * chunkSize;
                    end = start + chunkSize >= file.size ? file.size : start + chunkSize;
                    filedata = blobSlice.call(file, start, end);
                    if (!abort_upload) {
                        fileReader.readAsBinaryString(filedata);
                    }
                } else if (res == "abort") {
                    xhr_upload.splice(file.name, 1);
                } else if (currentChunk + 1 < chunks) {
                    currentChunk++;
                    $(progress[0]).css('width', (currentChunk * 100 / chunks) + '%');
                    start = currentChunk * chunkSize;
                    end = start + chunkSize >= file.size ? file.size : start + chunkSize;
                    filedata = blobSlice.call(file, start, end);
                    if (!abort_upload) {
                        fileReader.readAsBinaryString(filedata);
                    }
                } else {
                    $(progress[0]).css('width', '100%');
                    xhr_upload.splice(file.name, 1);
                    appendLog(lid, url);
                }
            },
            error: function (res) {
                console.log("post upload err:" + res);
            }
        });
    };
    let filedata = blobSlice.call(file, start, end);
    if (!abort_upload) {
        fileReader.readAsBinaryString(filedata);
    }
    xhr_upload.push(file.name);

};
