<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>报表模板管理</title>
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
		<li class="active"><a href="${ctx}/rpt/core/rptCm/checkList">报表模板审核列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="rptCm" action="${ctx}/rpt/core/rptCm/checkList" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
			  <div>
			 <label>报表模板名称：</label>
				<form:input path="cmName" htmlEscape="false" class="input-medium"/>
			 <label>报表分类：</label>
				<form:select path="rptCategory" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('ln_rpt_rptcategory')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			 <label>报表类型：</label>
				<form:select path="rptType" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('ln_rpt_rpttype')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></div>
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>报表模板名称</th>
				<th>报表分类</th>
				<th>报表类型</th>
				<th>文号</th>
				<th>审核人</th>
				<th>审核时间</th>
				<shiro:hasPermission name="rpt:core:rptCm:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="rptCm">
			<tr>
				<td><a href="${ctx}/rpt/core/rptCm/form?id=${rptCm.id}">
					${rptCm.cmName}
				</a></td>
				<td>
					${fns:getDictLabel(rptCm.rptCategory, 'ln_rpt_rptcategory', '')}
				</td>
				<td>
					${fns:getDictLabel(rptCm.rptType, 'ln_rpt_rpttype', '')}
				</td>
				<td>
					${rptCm.fileNo}
				</td>
				<td>
					${rptCm.auditPerson.name}
				</td>
				<td>
					<fmt:formatDate value="${rptCm.auditTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<shiro:hasPermission name="rpt:core:rptCm:edit"><td>
				<c:if test="${rptCm.flowStatus=='1'}">
    				<a href="${ctx}/rpt/core/rptCm/check?id=${rptCm.id}">审核</a>
    			</c:if>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>