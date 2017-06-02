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
		<li class="active"><a href="${ctx}/rpt/core/rptCm/">报表模板列表</a></li>
		  <shiro:hasPermission name="rpt:core:rptCm:edit"><li><a href="${ctx}/rpt/core/rptCm/form">报表模板添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="rptCm" action="${ctx}/rpt/core/rptCm/" method="post" class="breadcrumb form-search">
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
				<th>所属运营商</th>
				<th>审核状态</th>
				<shiro:hasPermission name="rpt:core:rptCm:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="rptCm">
			<tr>
				<td><a href="${ctx}/rpt/core/rptCm/readForm?id=${rptCm.id}">
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
					${rptCm.corporation.name}
				</td>
				<td>
					${fns:getDictLabel(rptCm.flowStatus, 'ln_mb_status', '')}
				</td>
				<c:if test="${rptCm.flowStatus=='0'||rptCm.flowStatus=='3'}">
				<shiro:hasPermission name="rpt:core:rptCm:edit"><td>
    			    <a href="${ctx}/rpt/core/rptCm/form?id=${rptCm.id}">修改</a>
    			    <a href="${ctx}/rpt/core/rptCmChildren/list?cmId=${rptCm.id}">子报表</a>
    			    <c:if test="${rptCm.rptCategory==3}">
    			      <a href="${ctx}/rpt/core/rptCm/queryList?id=${rptCm.id}">查询条件</a>
    			    </c:if>
    			    <a href="${ctx}/rpt/core/rptCm/forCheck?id=${rptCm.id}">提交</a>
					<a href="${ctx}/rpt/core/rptCm/delete?id=${rptCm.id}" onclick="return confirmx('确认要删除该报表模板吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
				</c:if>
				<c:if test="${rptCm.flowStatus=='1'||rptCm.flowStatus=='2'||rptCm.flowStatus=='4'}">
					<td></td>
				</c:if>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>