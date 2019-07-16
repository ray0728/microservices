$(document).ready(function () {
    $("#errinfo").hide();
});

$('#submit').click(function (e) {
    $.post("/login",{
            "username": $('#username').val(),
            "password": $('#password').val(),
            '_csrf':$("meta[name='_csrf']").attr("content")
        },
        function(data){
            window.location.href = "/";
        })
        .error(function(xhr,status,info) {
            $("#errinfo").is(':hidden') && $("#errinfo").show();
            let spanobj = $("#errinfo").find("span");
            let msg = xhr.responseText;
            !msg.length && $(spanobj).text("The server is busy, please try again later");
            msg.length && $(spanobj).text(msg);
        });
});