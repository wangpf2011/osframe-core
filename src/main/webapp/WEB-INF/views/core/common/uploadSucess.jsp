<%@ page  contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
	<head>
	<title>上传文件成功</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		function goLogin(){
			top.$.jBox.getBox().find("button[value='ok']").trigger("click");
		}
	</script>
	</head>
	<body >
		<input id="fileName" name="fileName" type="hidden"  value="${fileName}"/>
		<input id="filePath" name="filePath" type="hidden"  value="${filePath}"/>
		<input id="relfileName" name="relfileName" type="hidden"  value="${relfileName}"/>
		<div class="container-fluid">
			<div class="row-fluid">
				<div class="hero-unit">
					<h3>
						恭喜您， 上传文件成功！
					</h3>
					<p>
						<b id="counter">2</b>秒 关闭当前页面
						<a href="javascript:viod(0)"  onclick="goLogin()" class="btn">返回</a>
					</p>
				</div>
			 </div>
		</div>
			<!-- 注册协议区域 -->
			<script>(function(){var counter = 2, cNode = document.getElementById("counter");var itv = setInterval(function(){cNode.innerHTML = --counter;if(counter == 0) clearInterval(itv), goLogin();}, 1000);})();</script>
		</body>
	</html>
