var xhr_upload = [];
var abort_upload = false;
var ClockHashMap = function () {
    let size = 0;
    let entry = new Object();
    this.put = function (key, value) {
        if (!this.containsKey(key)) {
            size++;
            entry[key] = value;
        }
    }
    this.get = function (key) {
        return this.containsKey(key) ? entry[key] : null;
    }
    this.remove = function (key) {
        if (this.containsKey(key) && clearInterval(entry[key])) {
            delete entry[key];
            size--;
        }
    }
    this.containsKey = function (key) {
        return (key in entry);
    }
}
var xhr_upload_clock = new ClockHashMap();

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

    $("#org_log").length > 0 && $('#summernote').summernote("code", $("#org_log").val()) && $("#org_log").remove();
    $(".card-header").css("z-index", "auto");
    $.base64.utf8encode = true;
    $("#errinfo").hide();
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
    !error && $("#select_category").val(userdefcategory);
    !error && input.val("");
    !error && $("#addLogType").modal('hide');
});

$("#extmodal").on('show.bs.modal', function (e) {
    let header = $(this).find('h5[class="title"]');
    let content = $(this).find('div[class="modal-div"]');
    let source = $(e.relatedTarget).text();
    $("#btn-close").removeAttr("disabled");
    source = $.trim(source);
    if (source == "Publish") {
        header.text("Prepare to create an article") && content.html(createBody());
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
    let header = $(this).find('h5[class="title"]');
    let content = $(this).find('div[class="modal-div"]');
    let progress = $(this).find('div.progress-bar');
    source = $.trim(source);
    if (source == "Publish") {
        let resid = $($.find('div[name="context"]')[0]).data("id");
        let titleobj = $.find('input[name="title"]');
        let category = $("#select_category").find(":selected").val();
        let title = $(titleobj).val();
        let error = !title && !!($(titleobj).css("border-color", "red")) || !($(titleobj).css("border-color", ""));
        error = (typeof (category) == "undefined" && !!($('select').css("border-color", "red")) || !($('select').css("border-color", ""))) || error;
        error && $('#extmodal').modal('hide');
        if (error) {
            return;
        }
        !resid && createLog(header, progress);
        resid && updateLog(resid, progress) && uploadCover(resid);
    }
});

abortUpload = function (resid) {
    abort_upload = true;
    $.ajax({
        url: "/api/res/blog/files?id=" + resid + "&name=" + xhr_upload.join(";"),
        type: "Delete",
        cache: false,
        processData: false,
        contentType: false
    });
};

$('#extmodal').on('hidden.bs.modal', function (e) {
    abort_upload = false;
});

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

createCoverUploadBody = function (filename) {
    body = [];
    body.push('<div class="progress progresswithlabel mt-2 mb-2">');
    body.push('<div class="progress-bar progress-bar-striped bar" style="width:0%">');
    body.push('<span>' + filename + '</span>');
    body.push('</div>');
    body.push('</div>');
    return body.join('');
};

uploadResFiles = function (lid) {
    $("#upload_div").empty();
    let header = $("#extmodal").find('h5[class="title"]');
    let progress = $("#extmodal").find('div.progress-bar');
    $(progress[0]).css("width", "75%");
    header.text("Upload files in the article");
    let files = $(".note-editable").find('video, img');
    files.length && (files = filterLocalResource(files));
    files.length && $('#upload_div').append(dynamicsUploadFilesBody(files));
    processUpload(lid);
}

waitHLSFinish = function (lid, filename, progress) {
    $.get("/rst/hls?lid=" + lid + "&file=" + $.base64.encode(filename), function (data, status) {
        if (data == "1" && status == "success") {
            $(progress).css('width', '100%');
            xhr_upload.splice(filename, 1);
            xhr_upload_clock.remove(filename);
            jump(lid);
        }
    });
}

jump = function (lid) {
    if (xhr_upload.length != 0) {
        return;
    }
    $('#btn-close').attr("disabled", "true");
    $.post("/api/res/blog/update", {
        'id': lid,
        'status': 0,
        '_csrf': $("meta[name='_csrf']").attr("content")
    }, function (ret, status) {
        $.ajax({
            url: "/rst/hls?lid=" + lid,
            type: 'DELETE'
        });
        window.location.href = "/blog/article?id=" + lid;
    });
}

createBody = function () {
    body = [];
    body.push('<div>');
    body.push('<div class="progress progresswithlabel mb-2">');
    body.push('<div class="progress-bar progress-bar-striped bar" style="width:0%">');
    body.push('</div>')
    body.push('</div>')
    body.push('<div id="upload_div" class="mt-4">')
    body.push('</div>')
    body.push('</div>')
    return body.join('');
};

errorOccurred = function () {
    $("#errinfo").is(':hidden') && $("#errinfo").show();
    let spanobj = $("#errinfo").find("span");
    $(spanobj).text("The server is busy, please try again later");
    $('#extmodal').modal('hide');
};

createLog = function (header, progress) {
    header.text("Prepare to create an article");
    $.post("/api/res/blog/new", {
        '_csrf': $("meta[name='_csrf']").attr("content")
    }, function (data, status) {
        status == "success" && $(progress[0]).css("width", "25%") && updateLog(data, progress);
        status != "success" && errorOccurred();
    });
};

updateLog = function (lid, progress) {
    let title = $($.find('input[name="title"]')).val();
    let category = $("#select_category").find(":selected").val();
    let tags = $($.find('input[name="tag"]')).val().replace(/；/g, ";").split(";");
    tags = tags.filter(function (s) {
        return s && s.trim();
    });
    $('#summernote').summernote('code');
    let code = replaceNode($('#summernote').summernote('code'), lid);
    $.post("/api/res/blog/update", {
        'id': lid,
        'log': $(code).html(),
        'title': title,
        'type': category,
        'tags':tags,
        '_csrf': $("meta[name='_csrf']").attr("content")
    }, function (ret, status) {
        status == "success" && $(progress[0]).css("width", "50%") && uploadCover(lid);
        status != "success" && errorOccurred();
    });
};

uploadCover = function (lid) {
    let header = $("#extmodal").find('h5[class="title"]');
    header.text("Upload Cover Image");
    let progress = $("#extmodal").find('div.progress-bar');
    if ($("#cover_img").attr("src").indexOf("blob") == 0) {
        let filename = $("#cover_img").data("filename");
        $('#upload_div').append(createCoverUploadBody(filename));
        blobFileTransfer(lid, "cover_" + filename, $("#cover_img").attr("src"), "cover", progress[1], 0);
    } else {
        $(progress[0]).css("width", "50%");
        uploadResFiles(lid);
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
            '<source src="/api/res/blog/video/' + lid + '/' + $.base64.encode($(node).data('filename')) + '" type="application/x-mpegURL">',
            '</video>'
        ].join("");
        $(node).replaceWith(origial_video);
    });

    $.each(imagenodes, function (index, node) {
        let width = $(node).css('width');
        if ($(node).attr('src').indexOf("blob") == 0) {
            let origial_img = $('<img>');
            origial_img.css('width', width);
            origial_img.attr('src', '/api/res/blog/img/' + lid + "/" + $.base64.encode($(node).data('filename')));
            $(node).replaceWith(origial_img);
        }
    });
    return block[0];
};

processUpload = function (lid) {
    let uploadlist = $('#upload_div').find('div[class="progress progresswithlabel mb-2"]');
    $.each(uploadlist, function (index, data) {
        let progress = $(data).find('div[class="progress-bar progress-bar-striped bar"]');
        let url = $(data).attr("value");
        let filename = $(data).find("span").text();
        blobFileTransfer(lid, filename, url, null, progress, 1);
    });
    !uploadlist.length && jump(lid);
};

blobFileTransfer = function (lid, filename, url, type, progress, nextstep) {
    let xhr = new XMLHttpRequest;
    xhr.responseType = 'blob';
    xhr.onload = function () {
        if (this.status == 200) {
            let blobdata = xhr.response;
            blobdata.name = filename;
            blobdata.lastModifiedDate = $.now();
            compressImage(lid, filename, new File([blobdata], filename, {
                type: blobdata.type,
                lastModified: Date.now()
            }), type, progress, nextstep);
        }
    };
    xhr.open('GET', url, true);
    xhr.send();
};

compressImage = function (lid, filename, file, type, progress, nextstep) {
    if (file.type.indexOf("image") == 0 && (file.size / 1024 > 1025)) {
        let ready = new FileReader();
        ready.readAsDataURL(file);
        ready.onload = function () {
            let img = new Image();
            img.src = this.result;
            img.onload = function () {
                let that = this;
                let w = that.width,
                    h = that.height,
                    scale = w / h;
                let quality = 0.7;
                let canvas = document.createElement('canvas');
                let ctx = canvas.getContext('2d');
                let anw = document.createAttribute("width");
                anw.nodeValue = w;
                let anh = document.createAttribute("height");
                anh.nodeValue = h;
                canvas.setAttributeNode(anw);
                canvas.setAttributeNode(anh);
                ctx.drawImage(that, 0, 0, w, h);
                let base64 = canvas.toDataURL('image/jpeg', quality);
                let afterfile = convertBase64UrlToFile(filename, base64);
                sliceUpload(lid, afterfile, 2097152, type, progress, nextstep);
            }
        }
    } else {
        sliceUpload(lid, file, 2097152, type, progress, nextstep);
    }
}

convertBase64UrlToFile = function (filename, urlData) {
    var arr = urlData.split(','), mime = arr[0].match(/:(.*?);/)[1],
        bstr = atob(arr[1]), n = bstr.length, u8arr = new Uint8Array(n);
    while (n--) {
        u8arr[n] = bstr.charCodeAt(n);
    }
    let blobdata = new Blob([u8arr], {type: mime});
    blobdata.name = filename;
    blobdata.lastModifiedDate = $.now();
    return new File([blobdata], filename, {
        type: blobdata.type,
        lastModified: Date.now()
    });
}

sliceUpload = function (lid, file, chunkSize, type, progress, nextstep) {
    let chunks = Math.ceil(file.size / chunkSize);
    let currentChunk = 0;
    let checksum;
    let blobSlice = File.prototype.slice || File.prototype.mozSlice || File.prototype.webkitSlice;
    let spark = new SparkMD5();
    let fileReader = new FileReader();
    let start = 0;
    let end = chunkSize >= file.size ? file.size : chunkSize;
    !type && (type = ((file.type.indexOf("video") == 0) ? "video" : "img"));
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
        formData.append("_csrf", $("meta[name='_csrf']").attr("content"));
        $.ajax({
            url: "/api/res/blog/" + type + "/" + lid + "/" + $.base64.encode(file.name),
            data: formData,
            type: "Post",
            cache: false,
            processData: false,
            contentType: false,
            success: function (respond) {
                if (respond == "resend") {
                    start = currentChunk * chunkSize;
                    end = start + chunkSize >= file.size ? file.size : start + chunkSize;
                    filedata = blobSlice.call(file, start, end);
                    !abort_upload && fileReader.readAsBinaryString(filedata);
                } else if (respond == "abort") {
                    xhr_upload.splice(file.name, 1);
                } else if (currentChunk + 1 < chunks) {
                    currentChunk++;
                    $(progress).css('width', (currentChunk * 100 / chunks) + '%');
                    start = currentChunk * chunkSize;
                    end = start + chunkSize >= file.size ? file.size : start + chunkSize;
                    filedata = blobSlice.call(file, start, end);
                    !abort_upload && fileReader.readAsBinaryString(filedata);
                } else {
                    if (type == "video") {
                        $(progress).css('width', '99%');
                        xhr_upload_clock.put(file.name, setInterval(function () {
                            waitHLSFinish(lid, file.name, progress);
                        }, 10000));
                    } else {
                        $(progress).css('width', '100%');
                        xhr_upload.splice(file.name, 1);
                        nextstep && jump(lid);
                        !nextstep && uploadResFiles(lid);
                    }
                }
            },
            error: function (ret) {
                errorOccurred();
            }
        });
    };
    let filedata = blobSlice.call(file, start, end);
    if (!abort_upload) {
        fileReader.readAsBinaryString(filedata);
    }
    xhr_upload.push(file.name);

};

$('#cover_img').on("click", function (e) {
    $('#upload_cover').click();
    e.preventDefault();
});

$('#upload_cover').on("change", function (e) {
    let file = e.target.files;
    $('#cover_img').attr('src', URL.createObjectURL(file[0]));
    $('#cover_img').attr("data-filename", file[0].name);
});

$('#btn-close').on('click', function () {
    let resid = $($.find('div[name="context"]')[0]).data("id");
    abortUpload(resid);
});

