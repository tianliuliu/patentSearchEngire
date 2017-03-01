<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!doctype html>
<html lang="zh">
<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"> 
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
<title>专利搜索引擎</title>
<link rel="stylesheet" type="text/css" href="css/default.css">
<link rel="stylesheet" type="text/css" href="css/search-form.css">
	<script src="js/jquery-1.11.0.min.js" type="text/javascript"></script>
</head>
<body>

	<form action="searchServlet" onsubmit="return submitFn(this);"  method="get">
		<div class="search-wrapper active">
			<div class="input-holder">
				<input type="text" name="keyword" class="search-input" placeholder="patent search..." />
				<button class="search-icon" onclick="submit"><span></span></button>
			</div>
			<div class="radio" style="text-align:center; margin-top:20px;">
				<label><input name="type" type="radio" value="0" />专利号检索</label>
				<label><input name="type" type="radio" value="1" checked/>关键字检索</label>
			</div>
			<div class="result-container" style="margin-top:50px;">
				<p></p>
			</div>
		</div>
	</form>

<script type="text/javascript">
	

	function submitFn(obj){
		value = $(obj).find('.search-input').val().trim();

		
		if(!value.length){
		    _html = "您输入的关键词或专利号为空!";
			$(obj).find('.result-container').html('<h1 style="color:red">' + _html + '</h1>');
			$(obj).find('.result-container').fadeIn(100);
			return false;
		}
		else{
			return true;
		}

	}
</script>

<div style="text-align:center; margin-top:350px; font:normal 14px/24px 'MicroSoft YaHei';">
	<h1 >GDOC专利检索系统</h1>
</div>

</body>
</html>