<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>  
<!doctype html>
<html lang="zh">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
    <title>专利搜索引擎</title>
    <link rel="stylesheet" type="text/css" href="css/default.css">
    <link rel="stylesheet" type="text/css" href="css/search-form-1.css">
    <link rel="stylesheet" type="text/css" href="css/bootstrap.min.css" >
    <style type="text/css">
.page{
 text-align:center;
 margin:28px 0;}

.page a{
 font-size:14px;
 padding:1px 8px;
 background:#fff;
 border:1px #ccc solid;
 margin:0 5px;}

.page a.current{
 color:#fff;
 font-weight:bold;
 background:#0198f1;
 border:1px #0370cb solid;}

.page span{
 margin-left:10px;
}

.disableCss{
 text-decoration:none;
 outline:none;
 pointer-events:none;
 color:#afafaf;
 cursor:default;
}
</style>
    <script src="js/jquery-1.11.0.min.js" type="text/javascript"></script>
    <script type="text/javascript" src="js/page.js"></script>
</head>
<body>
    <form action="searchServlet" onsubmit="return submitFn(this);"  method="get">
    <input type="hidden" id="pageNum" name="pageNum" value="${pageNum}" />
    <div class="search-wrapper active" style = "margin-top:30px; margin-left:70px;">
        <div class="input-holder">
            <input type="text" class="search-input" name="keyword" value="${keyword }"/>
            <button class="search-icon" id="submitBtn" onclick="submit"><span></span></button>
        </div>
        <div class="radio">
            <label><input name="type" type="radio" value="0" <c:if test="${type==0 }">checked</c:if> />专利号检索</label>
            <label><input name="type" type="radio" value="1" <c:if test="${type==1 }">checked</c:if> />关键字检索</label>
        </div>
    </div>
    <div class="result-container" style="margin-top:30px;">
				<p></p>
	</div>
    <div class = "content" style="margin-top:140px; margin-left:70px; font:normal 14px/24px 'MicroSoft YaHei';">
        <c:forEach var="patent" items="${patents }" >
        <div>
            <h3 class="t"><a href ="${patent.zlUrl }" target="_blank">${patent.zlName}</a></h3>
            <h4>专利号：${patent.zlNumber } </h4>
            <h4>申请人： ${patent. zlApplicant }</h4>
            <h4>申请日： ${patent.zlDate }</h4>
            <div>摘要: ${patent.zlAbstract }</div>
        </div>
        </c:forEach>    
    </div>
     <div id="pageMod" class="page" _pages="50" _pageNum="${pageNum}" _pageSize="${pageSize}", _total="500" _ajaxParam="false">
          <div id="pagnation"  Style="margin-top:30px;margin-left:-1350px;" class="page"> </div>
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
</body>

</html>