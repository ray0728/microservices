$('a.page-link').click(function () {
    $('#list_area').load($(this).attr('href'));
    return false;
});