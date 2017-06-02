<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>文件上传通用页面</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#uploadForm").attr('action',"${upUrl}");
		});
	</script>
</head>
<body>
<div class="container-fluid">
	<form id="uploadForm" action="" method="post" enctype="multipart/form-data" style="padding-left:20px;text-align:center;" class="form-search" ><br/>
		<div class="control-group">
			<input id="uploadFile" name="file" type="file" style="width:330px"/>
		</div>
		<div class="form-actions">
	  		<span class="button-wrap">
				<input id="btnImportSubmit" class="button button-pill button-raised button-highlight"  type="submit" value="   确认上传   " />
			</span>
		</div>
	</form>
</div>
</body>
</html>