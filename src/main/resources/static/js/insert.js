'use strict'
$(function(){
	$('#btn-primary').on('click', function(){
		$('#btn-primary').prop('disabled', true);
		$('#insertAdministratorForm').submit();
	});
});