<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8" />
<title>TempHum</title>
<base target="_blank" />
<link rel="stylesheet"
	href="https://cdn.bootcss.com/bootstrap/3.3.4/css/bootstrap.min.css">
<script src="https://cdn.bootcss.com/jquery/1.11.2/jquery.min.js"></script>
<script
	src="https://cdn.bootcss.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
<!-- JavaScript -->
<script type="text/javascript">
	//http://localhost:8080/oauth2/temphum/get/findLast
	var url = 'temphum/get/findLast';
	$.getJSON(url, function(data) {
		//console.log(data);
		//$('#result').append('<p>loc_id:'+data.humidity+'</p>');
		//$('#result').append('<p>name:'+data.temperature+'</p>');  
		//$('#result').append('<p>created:'+data.date+'</p>'); 
		
		$('#jsonDataList').append('<h3>时间：'+data.date+'</h3>');
		$('#jsonDataList').append('<h3>温度：'+data.temperature+'</h3>');  
		$('#jsonDataList').append('<h3>湿度：'+data.humidity+'</h3>');

		//$('#humidity').append(data.humidity);
		//$('#temperature').append(data.temperature);
		//$('#date').append(data.date);
		//åé¢çä¹é½ä¸æ ·            
	});
</script>
<style type="text/css">
#tetle {
	color: #0000FF;
	padding-left: 10px;
	font-size: 30pt;
	font-family: Arial, Helvetica, sans-serif;
}
</style>
</head>
<body>
	<h1 id="tetle" class="text-muted">温湿度</h1>
	<div id="jsonDataList"></div>
</body>
</html>