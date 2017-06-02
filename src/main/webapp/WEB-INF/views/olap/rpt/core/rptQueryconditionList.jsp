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
        function addLine(){
			var cnt = $("#editList tr").length;
			var newrow = '<tr  class="">';
			newrow+='<td>';
			newrow+='<input name="rptQueryconditionList[' + cnt + '].showName" value="" />';
			newrow+='</td>';
			newrow+='<td>';
			newrow+='<input name="rptQueryconditionList[' + cnt + '].showId" value="" />';
			newrow+='</td>';
			newrow+='<td>';
			newrow+='<select name="rptQueryconditionList[' + cnt + '].showType"><c:forEach items="${fns:getDictList('ln_rpt_showtype')}" var="dict"><option value="${dict.value}" ${dict.value==rptQuerycondition.showType?"selected":""} title="${dict.description}">${dict.label}</option></c:forEach></select>';
			newrow+='</td>';
			newrow+='<td>';
			newrow+='<input name="rptQueryconditionList[' + cnt + '].showDict" value="" />';
			newrow+='</td>';
			newrow+='<td>';
			newrow+='<a name="deladdtr'+ cnt +'" onclick="deltr(this)">删除</a>';
			newrow+='</td>';
			newrow+='</tr>';
			$('#editList').append(newrow);
		}
		function forSave(){
		    $("#modifyForm").attr("action", "${ctx}/rpt/core/rptQuerycondition/save?rptCmId=${rptCmId}");
	    	$("#modifyForm").submit();
		}
		function deltr(obj){
			var v = document.getElementsByName(obj.name);
			var $v = $(v);
			if(obj.name.indexOf("deladdtr") != -1){
				$v.parent().parent().empty();
			} else {
				$v.parent().prev().prev().children().attr("value","99");
			}
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
	    <li><a href="${ctx}/rpt/core/rptCm/">报表模板列表</a></li>
		<li class="active"><a href="${ctx}/rpt/core/rptCm/queryList">报表查询条件维护列表</a></li>
	</ul>
	<form:form id="inputForm" modelAttribute="rptCm" action="${ctx}/rpt/core/rptCm/saveQuery" method="post">
		    <form:hidden path="id"/>
		   <div id="div1" class="breadcrumb form-search" align="right">
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="保存"/>
				<input id="btnSubmit" class="btn btn-primary" type="button" onclick="addLine()" value="添加一行"/>
		   </div>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>显示名称</th>
				<th>显示id</th>
				<th>显示类型</th>
				<th>数据字典</th>
				<shiro:hasPermission name="rpt:core:rptQuerycondition:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody id="editList">
		<c:forEach items="${rptCm.rptQueryconditionList}" var="rptQuerycondition" varStatus="entityIndex">
		<c:if test="${rptQuerycondition.isSystem=='0'}">
			<tr>
			<input type="hidden" name="rptQueryconditionList[${entityIndex.index}].id" value="${rptQuerycondition.id}"/>
						<td>
									<input name="rptQueryconditionList[${entityIndex.index}].showName" value="${rptQuerycondition.showName}"/>
						</td>
						<td>
									<input name="rptQueryconditionList[${entityIndex.index}].showId" value="${rptQuerycondition.showId}"/>
						</td>
						<td>
									<select name="rptQueryconditionList[${entityIndex.index}].showType" value="${rptQuerycondition.showType}"><c:forEach items="${fns:getDictList('ln_rpt_showtype')}" var="dict"><option value="${dict.value}" ${dict.value==rptQuerycondition.showType?'selected':''} title="${dict.description}">${dict.label}</option></c:forEach></select>
						</td>
						<td>
									<input name="rptQueryconditionList[${entityIndex.index}].showDict" value="${rptQuerycondition.showDict}"/>
						</td>
				<shiro:hasPermission name="rpt:core:rptQuerycondition:edit"><td>
					<a href="${ctx}/rpt/core/rptQuerycondition/delete?id=${rptQuerycondition.id}&rptCmId=${rptCm.id}" onclick="return confirmx('确认要删除该报表查询条件吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:if>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</form:form>
</body>
</html>