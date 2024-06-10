"use strict";

$(function(){
    $(".paging").paginathing({
        perPage: 10,
        prevText: '&lt',
        nextText: '&gt',
        activeClass: 'navi-active',
    });
})