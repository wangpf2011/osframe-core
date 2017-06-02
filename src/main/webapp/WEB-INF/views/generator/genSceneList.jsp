<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>代码生成场景管理管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/generator/genScene/">场景配置列表</a></li>
		<shiro:hasPermission name="generator:genScene:edit">
		<li><a href="${ctx}/generator/genScene/form">场景配置添加</a></li>
		</shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="genScene" action="${ctx}/generator/genScene/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<label>表名称 ：</label><form:input path="name" htmlEscape="false" maxlength="50" class="input-small"/>
		&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
		<tr>
			<th>描述</th><th>表名称</th><th>备注</th><th>创建日期</th>
			<shiro:hasPermission name="generator:genScene:edit"><th>操作</th></shiro:hasPermission>
		</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="genScene">
			<tr>
				<td><a href="${ctx}/generator/genScene/editform?id=${genScene.id}">${genScene.comments}</a></td>
				<td><a href="${ctx}/generator/genScene/editform?id=${genScene.id}">${genScene.name}</a></td>
				<td>${genScene.remarks}</td>
				<td><fmt:formatDate value="${genScene.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				
				<shiro:hasPermission name="generator:genScene:edit"><td>
    				<a href="${ctx}/generator/genScene/editform?id=${genScene.id}">修改</a>
					<a href="${ctx}/generator/genScene/delete?id=${genScene.id}" onclick="return confirmx('确认要删除该代码生成场景管理吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
