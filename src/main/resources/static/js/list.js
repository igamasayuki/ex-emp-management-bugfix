"use strict";

$(function() {
	$("#search-name").on("keyup", function() {
		let hostUrl = "http://localhost:8080/searchname/search";
		let inputName = $("#search-name").val();
		$.ajax({
			url: hostUrl,
			type: 'post',
			dataType: 'json',
			data: {
				name: inputName
			},
			async: true
		}).done(function(data) {
			console.log(data);
			console.dir(JSON.stringify(data));
			$("#search-name").autocomplete({
				source : data,
				autoFocus: true,
				delay: 500,
				minLength: 1
			});
		}).fail(function(XMLHttpRequest, textStatus, errorThrown) {
			alert("エラーが発生しました");
			console.log("XMLHttpRequest: " + XMLHttpRequest.status);
			console.log("textStatus: " + textStatus);
			console.log("errorThrown: " + errorThrown.message);
		});
	});
});