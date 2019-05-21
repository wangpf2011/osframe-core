<%@ page contentType="text/html;charset=UTF-8" isErrorPage="true" %>
<%@ page import="com.wf.ssm.common.beanvalidator.BeanValidators"%>
<%@ page import="org.slf4j.Logger,org.slf4j.LoggerFactory" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%response.setStatus(200);%>
<%
	Throwable ex = null;
	if (exception != null)
		ex = exception;
	if (request.getAttribute("javax.servlet.error.exception") != null)
		ex = (Throwable) request.getAttribute("javax.servlet.error.exception");
	//记录日志
	if (ex!=null){
		Logger logger = LoggerFactory.getLogger("500.jsp");
		logger.error(ex.getMessage(), ex);
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>500 - 系统内部错误</title>
	<%@include file="/WEB-INF/views/include/head.jsp" %>
</head>
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
		url(${ctxStatic}/images/500.png)
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
		url(${ctxStatic}/images/500.png)
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

<body>
<div>
	<div class="main-404"></div>
		<h2>
			<i></i>系统发生内部错误!<br><b id="counter">20</b>秒跳转到<div><a href="javascript:" onclick="history.go(-1);" class="btn">上一页</a></div>
		</h2>
	</div>
	<script>(function(){var counter = 20, cNode = document.getElementById("counter");var itv = setInterval(function(){cNode.innerHTML = --counter;if(counter == 0) clearInterval(itv), history.go(-1);}, 1000);})();</script>
	
	<div class="container-fluid">
		<p>错误信息：</p><p>
		<%
			if (ex!=null){
				if (ex instanceof javax.validation.ConstraintViolationException){
					for (String s : BeanValidators.extractPropertyAndMessageAsList((javax.validation.ConstraintViolationException)ex, ": ")){
						out.print(s+"<br/>");
					}
				}else{
					out.print(ex+"<br/>");
				}
			}
		%>
		</p>
		
		<script>try{top.$.jBox.closeTip();}catch(e){}</script>
	</div>
</body>
</html>