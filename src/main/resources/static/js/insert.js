'use strict'
$(function(){
	$('#btn-primary').on('click', function(){
		$('#btn-primary').prop('disabled', true);
		$('#insertAdministratorForm').submit();
	});
});

//$(function(){
//	$(document).on('load', function(){
//	
//			$('#btn-primary').prop('disabled', true);
//			
//			if($('#name').val() != null && $('#mailAddress').val() != null && $('#password').val() == $('#confirmationPassword').val()){
//				$('#btn-primary').prop('disabled', false);
//			}
//				
//	});
//});