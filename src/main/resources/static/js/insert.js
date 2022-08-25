"use strict";

$(function() {
	$("#password").on("keyup", function() {
		if ($("#password").val() !== $("#password2").val()) {
			$("#password-mismatch").show();
			$("#password-mismatch").text("パスワードが一致していません");
		} else {
			$("#password-mismatch").hide();
		}
	});
	
	$("#password2").on("keyup", function() {
		if ($("#password").val() !== $("#password2").val()) {
			$("#password-mismatch").show();
			$("#password-mismatch").text("パスワードが一致していません");
		} else {
			$("#password-mismatch").hide();
		}
	});
});