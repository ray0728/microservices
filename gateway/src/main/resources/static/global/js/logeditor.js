var xhr_upload = [];
var abort_upload = false;
$(document).ready(function () {
    $('#summernote').summernote({
        popatmouse: false,
        placeholder: "Let's write",
        height: $(window).height() / 3,
        fontSizes: ['12', '14', '16', '18', '24', '36', '48'],
        toolbar: [
            ['fontname', ['fontname']],
            ['style', ['bold', 'italic', 'underline', 'clear']],
            ['fontsize', ['fontsize']],
            ['color', ['color']],
            ['para', ['ul', 'ol', 'paragraph']],
            ['height', ['height']],
            ['insert', ['myimage', 'myvideo', 'link', 'table', 'hr']],
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
    $(".card-header").css("z-index", "auto");
    $.base64.utf8encode = true;
});


$('button.btn-type').click(function (e) {
    let parentObj = $(this).parent();
    let input = $(parentObj).find('input');
    let userdefcategory = $(input).val();
    let error = !userdefcategory && !!($(input).css("border-color", "red")) || !($(input).css("border-color", ""));
    let shoudadd = true;
    !error && $("select option").each(function () {
        shoudadd = !($(this).text() == userdefcategory);
    });
    let newoption = new Option(userdefcategory, userdefcategory);
    !error && shoudadd && $('select').append(newoption);
    !error && $("select").val(userdefcategory);
    !error && input.val("");
    !error && $("#addLogType").modal('hide');
});

createLog = function () {
    let title = $($.find('input[name="title"]')).val();
    let category = $("select").find(":selected").val();
    let formData = new FormData();
    let tags = $($.find('input[name="tag"]')).val().replace(/；/g, ";").split(";");
    tags = tags.filter(function(s){ return s&&s.trim();});
    formData.append("title", title);
    formData.append("type", category);
    tags.length > 0 && formData.append("tags", tags);
    formData.append("_csrf", $($.find('input[type="hidden"]')).val());
    $.ajax({
        url: "/blog/api/res/new",
        data: formData,
        type: "Post",
        cache: false,
        processData: false,
        contentType: false,
        success: function (resid) {
            autoDetect(resid);
        },
        error: function (res) {
            console.log(res);
        }
    });
};

autoDetect = function (resid) {
    switch ($('#extmodal').find('h5[class="title"]').text()) {
        case ("Upload Files"):
            processUpload(resid);
            break;
        case ("Create New Blog"):
            appendLog(resid);
            break;
    }
};

$("#extmodal").on('show.bs.modal', function (e) {
    let header = $(this).find('h5[class="title"]');
    let content = $(this).find('div[class="modal-div"]');
    let source = $(e.relatedTarget).text();
    source = $.trim(source);
    if (source == "Publish") {
        let files = $(".note-editable").find('video, img');
        files.length && (files = filterLocalResource(files));
        files.length && header.text("Upload Files") && content.html(dynamicsUploadFilesBody(files));
        !files.length && header.text("Create New Blog") && content.html(createBody());
    } else {
        let title = $($.find('input[name="title"]')).val();
        let code = $('#summernote').summernote('code');
        header.text(title);
        content.html(code);
    }
});

filterLocalResource = function (files) {
    let localRes = [];
    $.each(files, function (index, file) {
        file.src.indexOf("blob") == 0 && localRes.push(file);
    });
    return localRes;
};

$('#extmodal').on('shown.bs.modal', function (e) {
    let source = $(e.relatedTarget).text();
    source = $.trim(source);
    if (source == "Publish") {
        let resid = $($.find('div[name="context"]')).val();
        let titleobj = $.find('input[name="title"]');
        let category = $("select").find(":selected").val();
        let title = $(titleobj).val();
        let error = !title && !!($(titleobj).css("border-color", "red")) || !($(titleobj).css("border-color", ""));
        error = (typeof (category) == "undefined" && !!($('select').css("border-color", "red")) || !($('select').css("border-color", ""))) || error;
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
    let source = $(e.relatedTarget).text();
    source = $.trim(source);
    if (source == "Publish") {
        abort_upload = true;
        $.each(xhr_upload, function (index, filename) {
            $.ajax({
                // TODO:missing log_id
                url: "/blog/api/res/files?name=" + filename,
                type: "Delete",
                cache: false,
                processData: false,
                contentType: false
            });
        });
    }
});

$('#extmodal').on('hidden.bs.modal', function (e) {
    let source = $(e.relatedTarget).text();
    source = $.trim(source);
    if (source == "Publish") {
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
        let filename = $(file).data("filename");
        let url = file.src;
        if (url.indexOf("blob") == 0) {
            body.push('<div class="progress progresswithlabel mb-2" value="' + url + '">');
            body.push('<div class="progress-bar progress-bar-striped bar" style="width:0%">');
            body.push('<span>' + filename + '</span>');
            body.push('</div>');
            body.push('</div>');
        }
    });
    return body.join('');
};
uploadCover = function(lid){
    let upload = $("#cover_img").attr("src").indexOf("blob");

};

appendLog = function (lid) {
    $('#summernote').summernote('code');
    let code = replaceNode($('#summernote').summernote('code'), lid);
    let formData = new FormData();
    formData.append("id", lid);
    formData.append("log", $(code).html());
    formData.append("_csrf", $($.find('input[type="hidden"]')).val());
    if (xhr_upload.length == 0) {
        $.ajax({
            url: "/blog/api/res/update",
            data: formData,
            type: "Post",
            cache: false,
            processData: false,
            contentType: false,
            success: function (resid) {
                $('#extmodal').modal("hide");
                window.location.href = "/blog/list";
            },
            error: function (res) {
                console.log("post log err:" + res);
            }
        });
    }
};

replaceNode = function (code, lid) {
    let block = $('<div>');
    block.html(code);
    let videonodes = $(block).find('div.video-js');
    let imagenodes = $(block).find('img');
    $.each(videonodes, function (index, node) {
        let id = $(node).attr('id');
        let origial_video = [
            '<video controls class="video-js vjs-big-play-centered" id="' + id + '">',
            '<source src="/blog/api/res/video/' + lid + '/' + $.base64.encode($(node).data('filename')) + '">',
            '</video>'
        ].join("");
        $(node).replaceWith(origial_video);
    });

    $.each(imagenodes, function (index, node) {
        let width = $(node).css('width');
        if ($(node).attr('src').indexOf("blob") == 0) {
            let origial_img = $('<img>');
            origial_img.css('width', width);
            origial_img.attr('src', '/blog/api/res/img/' + lid + "/" + $.base64.encode($(node).data('filename')));
            $(node).replaceWith(origial_img);
        }
    });
    return block[0];
};

processUpload = function (lid) {
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
        formData.append("index", currentChunk);
        formData.append("total", chunks);
        formData.append("file", filedata);
        formData.append("chunksize", chunkSize);
        formData.append("checksum", checksum);
        formData.append("_csrf", $($.find('input[type="hidden"]')).val());
        $.ajax({
            url: "/blog/api/res/" + ((file.type.indexOf("video") == 0) ? "video/" : "img/") + lid + "/" + $.base64.encode(file.name),
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
                    appendLog(lid);
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

$('#cover_img').on("click",function (e) {
    $('#upload_cover').click();
    e.preventDefault();
});

$('#upload_cover').on("change", function (e) {
    let file = e.target.files;
    $('#cover_img').attr('src',URL.createObjectURL(file[0]));
});



