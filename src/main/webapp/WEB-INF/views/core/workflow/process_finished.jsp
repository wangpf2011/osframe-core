<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>运行中的流程</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/dialog.jsp" %>
	<script type="text/javascript">
		$(document).ready(function(){
			top.$.jBox.tip.mess = null;
		});
		function page(n,s){
        	location = '${ctx}/workflow/process/finished/?pageNo='+n+'&pageSize='+s;
        }
		
		// 关闭提示框
		function closeTip(){
			top.$.jBox.closeTip();
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/workflow/process/">流程管理</a></li>
		<li><a href="${ctx}/workflow/process/running">运行中的流程</a></li>
		<li class="active"><a href="${ctx}/workflow/process/finished/">已完成的流程</a></li>
	</ul>
	<form id="searchForm" action="${ctx}/workflow/process/running/" method="post" class="breadcrumb form-search">
		<label>流程实例ID：</label><input type="text" id="procInsId" name="procInsId" value="${procInsId}" class="input-medium"/>
		<label>流程定义Key：</label><input type="text" id="procDefKey" name="procDefKey" value="${procDefKey}" class="input-medium"/>
		&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
	</form>
	<tags:message content="${message}"/>
	<table class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>流程实例ID</th>
				<th>流程定义ID</th>
				<th>流程启动时间</th>
				<th>流程结束时间</th>
				<th>流程结束原因</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list }" var="procIns">
				<tr>
					<td>${procIns.id }</td>
					<td>${procIns.processDefinitionId }</td>
					<td>${procIns.startTime }</td>
					<td>${procIns.endTime }</td>
					<td>${empty hpi.deleteReason ? "正常结束" : hpi.deleteReason}</td>
				</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
