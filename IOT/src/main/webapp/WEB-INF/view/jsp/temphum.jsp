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
<script src="https://cdn.bootcss.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
<!-- JavaScript -->
<script type="text/javascript">
	//http://localhost:8080/oauth2/temphum/get/findLast
	var url = 'temphum/get/findLast';
	$.getJSON(url, function(data) {
		//console.log(data);
		//$('#result').append('<p>loc_id:'+data.humidity+'</p>');
		//$('#result').append('<p>name:'+data.temperature+'</p>');  
		//$('#result').append('<p>created:'+data.date+'</p>'); 
		//$('#jsonDataList').append('<td>'+data.humidity+'</td>');
		//$('#jsonDataList').append('<td>'+data.temperature+'</td>');  
		//$('#jsonDataList').append('<td>'+data.date+'</td>');

		$('#humidity').append(data.humidity);
		$('#temperature').append(data.temperature);
		$('#date').append(data.date);
		//åé¢çä¹é½ä¸æ ·            
	});
</script>
<style>
#date {
	width: 250px;
	color: #0000FF;
	font-size: 16pt;
	padding-left: 10px;
	font-family: Arial, Helvetica, sans-serif;
}

#temperature {
	width: 100px;
	color: #0000FF;
	padding-left: 10px;
	font-size: 16pt;
	font-family: Arial, Helvetica, sans-serif;
}

#humidity {
	width: 100px;
	color: #0000FF;
	padding-left: 10px;
	font-size: 16pt;
	font-family: Arial, Helvetica, sans-serif;
}

#caozuo {
	width: 100px;
	color: #0000FF;
	padding-left: 10px;
	font-size: 16pt;
	font-family: Arial, Helvetica, sans-serif;
}

#date1 {
	width: 250px;
	color: #0000FF;
	font-size: 16pt;
	padding-left: 10px;
	font-family: Arial, Helvetica, sans-serif;
}

#temperature1 {
	width: 100px;
	color: #0000FF;
	padding-left: 10px;
	font-size: 16pt;
	font-family: Arial, Helvetica, sans-serif;
}

#humidity1 {
	width: 100px;
	color: #0000FF;
	padding-left: 10px;
	font-size: 16pt;
	font-family: Arial, Helvetica, sans-serif;
}

#caozuo1 {
	width: 100px;
	color: #0000FF;
	padding-left: 10px;
	font-size: 16pt;
	font-family: Arial, Helvetica, sans-serif;
}

#temphumlist {
	width: 550px;
	height: 50px;
	border: 0px;
}
</style>
</head>
<body>
	<div class="container">
		<h3 class="text-muted">温湿度</h3>
		<table id="temphumlist" class="table-bordered">
			<thead id="temphumlist">
				<tr>
					<th id="date1">时间</th>
					<th id="temperature1">温度</th>
					<th id="humidity1">湿度</th>
					<th id="caozuo1">操作</th>
				</tr>
			</thead>
			<tbody id="temphumlist">
				<tr>
					<td id="date"></td>
					<td id="temperature"></td>
					<td id="humidity"></td>
					<td id="caozuo"><a target="_blank"id="down_link" href="#">刷新</a>
					<td>
				</tr>
			</tbody>
		</table>
	</div>
</body>
</html>