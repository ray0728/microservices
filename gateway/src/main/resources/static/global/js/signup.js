$('#avatar_img').on("click", function (e) {
    $('#upload_avatar').click();
    e.preventDefault();
});

$('#upload_avatar').on("change", function (e) {
    let file = e.target.files;
    $('#avatar_img').attr('src', URL.createObjectURL(file[0]));
    $('#avatar_img').attr("data-filename", file[0].name);
});

$('#createAccountModal').on('shown.bs.modal', function (e) {
    createAccount();
});
createAccount = function () {
    let progress = $(this).find('div.progress-bar');
    $.post("/join", {
        'username': $('#username').val(),
        'email': $('#email').val(),
        'passwd': $('#passwd').val(),
        'signature': $('#signature').val(),
        'resume': $('#resume').val(),
        '_csrf': $($.find('input[type="hidden"]')).val()
    }, function (ret, status) {
        status == "success" && $(progress[0]).css("width", "25%") && uploadCover(lid);
        status != "success" && errorOccurred();
    });
}

processAvatarUpload = function () {
    let progress = $(this).find('div.progress-bar');
    let url = $('#avatar_img').src;
    let filename = $('#avatar_img').data("filename");
    filename && blobFileTransfer(filename, url, progress);
    !filename && submitcreate(progress);
};

blobFileTransfer = function (filename, url, progress) {
    let xhr = new XMLHttpRequest;
    xhr.responseType = 'blob';
    xhr.onload = function () {
        if (this.status == 200) {
            $(progress[0]).css("width", "25%");
            let blobdata = xhr.response;
            blobdata.name = filename;
            blobdata.lastModifiedDate = $.now();
            compressImage(filename, new File([blobdata], filename, {
                type: blobdata.type,
                lastModified: Date.now()
            }), type, progress);
        }
    };
    xhr.open('GET', url, true);
    xhr.send();
};

compressImage = function (filename, file, progress) {
    let ready = new FileReader();
    ready.readAsDataURL(file);
    ready.onload = function () {
        let img = new Image();
        img.src = this.result;
        img.onload = function () {
            let that = this;
            let originWidth = that.width;
            let originHeight = that.height;
            let maxWidth = 300, maxHeight = 300;
            let targetWidth = originWidth, targetHeight = originHeight;
            if (originWidth > maxWidth || originHeight > maxHeight) {
                if (originWidth / originHeight > maxWidth / maxHeight) {
                    targetWidth = maxWidth;
                    targetHeight = Math.round(maxWidth * (originHeight / originWidth));
                } else {
                    targetHeight = maxHeight;
                    targetWidth = Math.round(maxHeight * (originWidth / originHeight));
                }
            }
            let quality = 0.7;
            let canvas = document.createElement('canvas');
            let context = canvas.getContext('2d');
            canvas.width = targetWidth;
            canvas.height = targetHeight;
            context.clearRect(0, 0, targetWidth, targetHeight);
            context.drawImage(that, 0, 0, targetWidth, targetHeight);
            let base64 = canvas.toDataURL('image/jpeg', quality);
            let afterfile = convertBase64UrlToFile(filename, base64);
            $(progress[0]).css("width", "50%");
            sliceUpload(afterfile, 2097152, progress);
        }
    }
}

sliceUpload = function (file, chunkSize, progress) {
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
            url: "/api/account/" + type + "/" + lid + "/" + $.base64.encode(file.name),
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
