<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%response.setStatus(200);%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
	<title>404- 页面找不到了</title>
	<%@include file="/WEB-INF/views/include/head.jsp" %>
<style>
body {
	font-family: microsoft yahei, 宋体;
	padding-top: 75px;
}

.main-404 {
	margin: 0 auto;
	width: 282px;
	height: 343px;
	background:
		url(${ctxStatic}/images/404.png)
		no-repeat 0 0;
}

h2 {
	text-align: center;
	font-weight: normal;
	font-size: 18px;
	line-height: 30px;
	margin: 30px 0;
	padding: 0;
	letter-spacing: 1px;
}

h2 i {
	display: inline-block;
	*display: inline;
	*zoom: 1;
	width: 34px;
	height: 34px;
	background:
		url(${ctxStatic}/images/404.png)
		no-repeat 0 -344px;
	vertical-align: middle;
	margin-right: 5px;
}

h2 b {
	font-weight: normal;
	color: red;
	margin: 0 3px;
}
</style>
</head>
<body>
	<div>
		<div class="main-404"></div>
		<h2>
			<i></i>抱歉，您访问的页面不存在OR正在建设中..<br><b id="counter">5</b>秒跳转到<div><a href="javascript:" onclick="history.go(-1);" class="btn">上一页</a></div>
		</h2>
		
	</div>
	<script>(function(){var counter = 5, cNode = document.getElementById("counter");var itv = setInterval(function(){cNode.innerHTML = --counter;if(counter == 0) clearInterval(itv), history.go(-1);}, 1000);})();</script>
</body>
</html>