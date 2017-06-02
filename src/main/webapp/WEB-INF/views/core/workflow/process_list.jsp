<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>流程管理</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/dialog.jsp" %>
	<script type="text/javascript">
		$(document).ready(function(){
			top.$.jBox.tip.mess = null;
		});
		function page(n,s){
        	location = '${ctx}/workflow/process/?pageNo='+n+'&pageSize='+s;
        }
		function updateCategory(id, category){
			$.jBox($("#categoryBox").html(), {title:"设置分类", buttons:{"关闭":true}, submit: function(){}});
			$("#categoryBoxId").val(id);
			$("#categoryBoxCategory").val(category);
		}
	</script>
	<script type="text/template" id="categoryBox">
		<form id="categoryForm" action="${ctx}/workflow/process/updateCategory" method="post" enctype="multipart/form-data"
			style="text-align:center;" class="form-search"><br/>
			<input id="categoryBoxId" type="hidden" name="procDefId" value="" />
			<select id="categoryBoxCategory" name="category">
				<c:forEach items="${fns:getDictList('act_category')}" var="dict">
					<option value="${dict.value}">${dict.label}</option>
				</c:forEach>
			</select>
			<br/><br/>　　
			<input id="categorySubmit" class="btn btn-primary" type="submit" value="   保    存   "/>　　
		</form>
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/workflow/process/">流程管理</a></li>
		<li><a href="${ctx}/workflow/process/running">运行中的流程</a></li>
		<li><a href="${ctx}/workflow/process/finished/">已完成的流程</a></li>
	</ul>
	<form id="searchForm" action="${ctx}/workflow/process/" method="post" class="breadcrumb form-search">
		<select id="category" name="category" class="input-medium">
			<option value="">全部分类</option>
			<c:forEach items="${fns:getDictList('act_category')}" var="dict">
				<option value="${dict.value}" ${dict.value==category?'selected':''}>${dict.label}</option>
			</c:forEach>
		</select>
		&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
	</form>
	<tags:message content="${message}"/>
	<table class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>流程分类</th>
				<th>流程ID</th>
				<th>流程标识</th>
				<th>流程名称</th>
				<th>流程版本</th>
				<th>流程XML</th>
				<th>流程图片</th>
				<th>部署时间</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="object">
				<c:set var="process" value="${object[0]}" />
				<c:set var="deployment" value="${object[1]}" />
				<tr>
					<td><a href="javascript:updateCategory('${process.id}', '${process.category}')" title="设置分类">${fns:getDictLabel(process.category,'act_category','无分类')}</a></td>
					<td>${process.id}</td>
					<td>${process.key}</td>
					<td>${process.name}</td>
					<td><b title='流程版本号'>V: ${process.version}</b></td>
					<td><a target="_blank" href="${ctx}/workflow/process/resource/read?procDefId=${process.id}&resType=xml">${process.resourceName}</a></td>
					<td><a target="_blank" href="${ctx}/workflow/process/resource/read?procDefId=${process.id}&resType=image">${process.diagramResourceName}</a></td>
					<td><fmt:formatDate value="${deployment.deploymentTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
					<td>
						<c:if test="${process.suspended}">
							<a href="${ctx}/workflow/process/update/active?procDefId=${process.id}" onclick="return confirmx('确认要激活吗？', this.href)">激活</a>
						</c:if>
						<c:if test="${!process.suspended}">
							<a href="${ctx}/workflow/process/update/suspend?procDefId=${process.id}" onclick="return confirmx('确认挂起除吗？', this.href)">挂起</a>
						</c:if>
						<a href='${ctx}/workflow/process/delete?deploymentId=${process.deploymentId}' onclick="return confirmx('确认要删除该流程吗？', this.href)">删除</a>
                        <a href='${ctx}/workflow/process/convert/toModel?procDefId=${process.id}' onclick="return confirmx('确认要转换为模型吗？', this.href)">转换为模型</a>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>