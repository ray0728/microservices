$('#avatar_img').on("click", function (e) {
    $('#upload_avatar').click();
    e.preventDefault();
});

$('#upload_avatar').on("change", function (e) {
    let file = e.target.files;
    $('#avatar_img').attr('src', URL.createObjectURL(file[0]));
    $('#avatar_img').attr("data-filename", file[0].name);
});
