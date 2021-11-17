'use strict'

$(function(){
	$(document).on('keyup','#password',function(){
		checkPass()	
	});
	
	$(document).on('keyup','#confirmPassword',function(){
		checkPass()	
	});
	


function checkPass(){
		let hostUrl = "http://localhost:8080/checkConPass";
		let password = $('#password').val();
		let confirmPassword = $('#confirmPassword').val();
		
		$.ajax({
			url : hostUrl,
			type : 'post',
			dataType : 'json',
			data :{
				password : password,
				confirmPassword : confirmPassword,
			},
			async : true ,
		}).done(function(data){
			// コンソールに取得データを表示
			console.log(data);
			console.dir(JSON.stringify(data));
			console.log(data.duplicateMessage);
			$('#duplicateMessage').text(data.duplicateMessage);
		}).fail(function(XMLHttpRequest,textStatus,errorThrown){
			alert('エラーが発生しました！')
			console.log('XMLHttpRequest : ' + XMLHttpRequest.status);
			console.log('textStatus : ' + textStatus);
			console.log('errorThrown : ' + errorThrown.message);
		})
	};
	
	$('#register').on('click',function(){
		$('#register').prop('disabled',true);
//		console.log('ボタンを止めました！');
		$('#insertAdministrator').submit();
	});
	
	
	
});
	
	