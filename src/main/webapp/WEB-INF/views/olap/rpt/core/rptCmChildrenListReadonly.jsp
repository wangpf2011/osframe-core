<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>子报表模板管理</title>
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
		<li><a href="${ctx}/rpt/core/rptCm/">报表模板列表</a></li>
		<li><a href="${ctx}/rpt/core/rptCm/readForm?id=${cmId}">报表模板查看</a></li>
		<li class="active"><a href="${ctx}/rpt/core/rptCmChildren/readList?cmId=${cmId}">子报表模板</a></li>
		<c:if test="${rptCm.rptCategory==3}">
		<li><a href="${ctx}/rpt/core/rptCm/readQueryList?id=${cmId}">报表查询条件</a></li>
		</c:if>
	</ul>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>引用文件</th>
				<th>模板文件</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="rptCmChildren">
			<tr>
				<td>
					${rptCmChildren.fileName}
				</td>
				<td>
					${rptCmChildren.filePath}
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>