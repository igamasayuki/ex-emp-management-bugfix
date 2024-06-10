"use strict";

$(function(){
    $("#employeeName").on("keyup", (e) => {
        const url = "http://localhost:8080/employee-api/nameList";
        const employeeName = $(e.target).val();
        $.ajax({
            url: url,
            type: "post",
            dataType: "json",
            data: {
                employeeName: employeeName
            },
            async: true
        }).done((data) => {
            const employeeNameList = data.employeeList.map(employee => employee.name);
            $("#employeeName").autocomplete({
                source: employeeNameList
            })
        }).fail((XMLHttpRequest, textStatusm, errorThrown) => {
            alert("エラーが発生");
        })
    })
})