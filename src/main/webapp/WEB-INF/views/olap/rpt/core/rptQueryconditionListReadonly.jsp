<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>报表查询条件维护管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			//$("#name").focus();
			$("#searchForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
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
		<li><a href="${ctx}/rpt/core/rptCm/readForm?id=${rptCm.id}">报表模板查看</a></li>
		<li><a href="${ctx}/rpt/core/rptCmChildren/readList?cmId=${rptCm.id}">子报表模板</a></li>
		<li class="active"><a href="${ctx}/rpt/core/rptCm/readQueryList?id=${rptCm.id}">报表查询条件</a></li>
	</ul>
	<form:form id="inputForm" modelAttribute="rptCm" action="${ctx}/rpt/core/rptCm/saveQuery" method="post">
		    <form:hidden path="id"/>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>显示名称</th>
				<th>显示id</th>
				<th>显示类型</th>
				<th>数据字典</th>
			</tr>
		</thead>
		<tbody id="editList">
		<c:forEach items="${rptCm.rptQueryconditionList}" var="rptQuerycondition" varStatus="entityIndex">
		<c:if test="${rptQuerycondition.isSystem=='0'}">
			<tr>
			<input type="hidden" name="rptQueryconditionList[${entityIndex.index}].id" value="${rptQuerycondition.id}"/>
						<td>
									<input name="rptQueryconditionList[${entityIndex.index}].showName" value="${rptQuerycondition.showName}" readonly/>
						</td>
						<td>
									<input name="rptQueryconditionList[${entityIndex.index}].showId" value="${rptQuerycondition.showId}" readonly/>
						</td>
						<td>
									<select name="rptQueryconditionList[${entityIndex.index}].showType" value="${rptQuerycondition.showType}" disabled><c:forEach items="${fns:getDictList('ln_rpt_showtype')}" var="dict"><option value="${dict.value}" ${dict.value==rptQuerycondition.showType?'selected':''} title="${dict.description}">${dict.label}</option></c:forEach></select>
						</td>
						<td>
									<input name="rptQueryconditionList[${entityIndex.index}].showDict" value="${rptQuerycondition.showDict}" readonly/>
						</td>
			</tr>
			</c:if>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</form:form>
</body>
</html>