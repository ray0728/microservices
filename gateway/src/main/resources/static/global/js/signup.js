$(document).ready(function () {
    $("#errinfo").hide();
});
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

errorOccurred = function (msg) {
    $("#errinfo").is(':hidden') && $("#errinfo").show();
    let spanobj = $("#errinfo").find("span");
    !msg.length && $(spanobj).text("The server is busy, please try again later");
    msg.length && $(spanobj).text(msg);
    $('#createAccountModal').modal('hide');
};

createAccount = function () {
    let progress = $(this).find('div.progress-bar');
    $.post("/rst/join", {
        'name': $('#username').val(),
        'email': $('#email').val(),
        'passwd': $('#passwd').val(),
        'signature': $('#signature').val(),
        'resume': $('#resume').val(),
        '_csrf': $($.find('input[type="hidden"]')).val()
    }, function (ret, status) {
        status == "success"  && $(progress[0]).css("width", "25%") && processAvatarUpload(ret, progress);
        status != "success"  && errorOccurred(ret);
    });
}

processAvatarUpload = function (uid, progress) {
    let url = $('#avatar_img').attr("src");
    let filename = $('#avatar_img').data("filename");
    filename && blobFileTransfer(uid, filename, url, progress);
};

blobFileTransfer = function (uid, filename, url, progress) {
    let xhr = new XMLHttpRequest;
    xhr.responseType = 'blob';
    xhr.onload = function () {
        if (this.status == 200) {
            $(progress[0]).css("width", "50%");
            let blobdata = xhr.response;
            blobdata.name = filename;
            blobdata.lastModifiedDate = $.now();
            compressImage(uid, filename, new File([blobdata], filename, {
                type: blobdata.type,
                lastModified: Date.now()
            }), progress);
        }
    };
    xhr.open('GET', url, true);
    xhr.send();
};

compressImage = function (uid, filename, file, progress) {
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
            $(progress[0]).css("width", "75%");
            sliceUpload(uid, afterfile, 2097152, progress);
        }
    }
}

sliceUpload = function (uid, file, chunkSize, progress) {
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
            url: "/api/user/account/avatar/" + uid,
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
                    fileReader.readAsBinaryString(filedata);
                } else if (respond == "abort") {
                    errorOccurred("");
                } else if (currentChunk + 1 < chunks) {
                    currentChunk++;
                    $(progress).css('width', (2500 * chunks / currentChunk) + '%');
                    start = currentChunk * chunkSize;
                    end = start + chunkSize >= file.size ? file.size : start + chunkSize;
                    filedata = blobSlice.call(file, start, end);
                    fileReader.readAsBinaryString(filedata);
                } else {
                    $(progress).css('width', '100%');
                    window.location.href = "/login";
                }
            },
            error: function (ret) {
                errorOccurred("");
            }
        });
    };
    let filedata = blobSlice.call(file, start, end);
    fileReader.readAsBinaryString(filedata);
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
