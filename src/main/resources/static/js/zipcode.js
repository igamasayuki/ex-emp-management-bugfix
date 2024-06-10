"use strict";

$(function(){
    $("#zipCodeBtn").on("click", () => {
        $.ajax({
            url: "https://zipcoda.net/api",
            type: "get",
            dataType: "json",
            data: {
                zipcode: $("#zipCode").val()
            },
            async: true
        }).done((data) => {
            const address = data.items[0].components.reduce((str, str1) => str + str1);
            $("#address1").val(address);
        }).fail((XMLHttpRequest, textStatusm, errorThrown) => {
            alert("エラー");
        })
    })
})
