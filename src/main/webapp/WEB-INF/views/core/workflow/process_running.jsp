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
        	location = '${ctx}/workflow/process/running/?pageNo='+n+'&pageSize='+s;
        }
		// 提示输入对话框
		function promptx(title, lable, href, closed){
			top.$.jBox("<div class='form-search' style='padding:20px;text-align:center;'>" + lable + "：<input type='text' id='txt' name='txt'/></div>", {
					title: title, submit: function (v, h, f){
			    if (f.txt == '') {
			        top.$.jBox.tip("请输入" + lable + "。", 'error');
			        return false;
			    }
				if (typeof href == 'function') {
					href();
				}else{
					resetTip(); //loading();
					location = href + encodeURIComponent(f.txt);
				}
			},closed:function(){
				if (typeof closed == 'function') {
					closed();
				}
			}});
			return false;
		}
		// 恢复提示框显示
		function resetTip(){
			top.$.jBox.tip.mess = null;
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
		<li class="active"><a href="${ctx}/workflow/process/running">运行中的流程</a></li>
		<li><a href="${ctx}/workflow/process/finished/">已完成的流程</a></li>
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
				<th>执行ID</th>
				<th>流程实例ID</th>
				<th>流程定义ID</th>
				<th>当前环节</th>
				<th>是否挂起</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="procIns">
				<tr>
					<td>${procIns.id}</td>
					<td>${procIns.processInstanceId}</td>
					<td>${procIns.processDefinitionId}</td>
					<td>${procIns.activityId}</td>
					<td>${procIns.suspended}</td>
					<td>
						<a href="${ctx}/workflow/form/dynamic/taskview?procDefId=${procIns.processDefinitionId}&procInsId=${procIns.processInstanceId}&execId=${procIns.id}">详情</a>
						<shiro:hasPermission name="workflow:process:edit">
							<a href="${ctx}/workflow/process/deleteProcIns?procInsId=${procIns.processInstanceId}&reason=" onclick="return promptx('删除流程','删除原因',this.href);">删除流程</a>
						</shiro:hasPermission>&nbsp;
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
