<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>报表预览</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			
		});
</script>
</head>
<body>
<ul class="nav nav-tabs">
		<li class="active"><a>报表预览</a></li>
	</ul>
<iframe src="${ctxMain}${path}" style="overflow:visible;"
		scrolling="yes" frameborder="no" width="100%" height="650"></iframe>
	
</body>
</html>
