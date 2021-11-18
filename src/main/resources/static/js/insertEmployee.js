'use strict'

$(function(){
	$(document).on('click','#getAddress',function(){
		let zipcode = $('#zipCode').val();
		console.log(zipcode);
		$.ajax({
		url: 'http://zipcoda.net/api',
		dataType: 'jsonp',
		data: {
			zipcode: zipcode
		},
		async: true
	}).done(function(data) {
		// 検索成功時にはページに結果を反映
		// コンソールに取得データを表示
		console.log(data);
		console.dir(JSON.stringify(data));
		let displayAddress = data.items[0].pref + data.items[0].address  
//		let displayAddress = data.items[0].components  
		
		$('#autoAddress').val(displayAddress);
	}).fail(function(XMLHttpRequest, textStatus, errorThrown) {
		// 検索失敗時には、その旨をダイアログ表示
		alert('正しい結果を得られませんでした。')
		console.log('XMLHttpRequest : ' + XMLHttpRequest.status);
		console.log('textStatus     : ' + textStatus);
		console.log('errorThrown    : ' + errorThrown.message);
		});
	});
	
	$('#register').on('click',function(){
		let address = $('#autoAddress').val() + $('#inputAddress').val();
		console.log(address);
		$('#address').val(address);
		$('#register').prop('disabled',true);
		$('#insertEmployee').submit();
	});
	
});