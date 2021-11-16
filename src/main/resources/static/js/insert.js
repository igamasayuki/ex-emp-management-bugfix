'use strict'

$(function(){
	$('#register').on('click',function(){
		$('#register').prop('disabled',true);
//		console.log('ボタンを止めました！');
		$('#insertAdministrator').submit();
	});
});