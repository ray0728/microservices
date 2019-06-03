$('a.page-inner-query').click(function () {
    $('#list_area').load($(this).attr('href'));
    return false;
});