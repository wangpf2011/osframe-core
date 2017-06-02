<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>序列管理管理</title>
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
		<li class="active"><a href="${ctx}/sys/sysSequences/">序列管理列表</a></li>
		  <shiro:hasPermission name="sys:sysSequences:edit"><li><a href="${ctx}/sys/sysSequences/form">序列管理添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="sysSequences" action="${ctx}/sys/sysSequences/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		 <label>序列名称：</label>
		<form:input path="seqName" htmlEscape="false" class="input-medium"/>
		<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></div>
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>序列名称</th>
				<th>当前序列值</th>
				<th>序列位数</th>
				<th>前缀</th>
				<th>后缀</th>
				<th>备注</th>
				<shiro:hasPermission name="sys:sysSequences:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="sysSequences">
			<tr>
				<td><a href="${ctx}/sys/sysSequences/form?id=${sysSequences.id}">
				${sysSequences.seqName}
				</a></td>
				<td>
					${sysSequences.seqVal}
				</td>
				<td>
					${sysSequences.seqWidth}
				</td>
				<td>
					${sysSequences.leftpad}
				</td>
				<td>
					${sysSequences.rightpad}
				</td>
				<td>
					${sysSequences.remarks}
				</td>				
				<shiro:hasPermission name="sys:sysSequences:edit"><td>
    				<a href="${ctx}/sys/sysSequences/form?id=${sysSequences.id}">修改</a>
					<a href="${ctx}/sys/sysSequences/delete?id=${sysSequences.id}" onclick="return confirmx('确认要删除该序列管理吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>