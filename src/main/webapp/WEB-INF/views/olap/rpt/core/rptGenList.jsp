<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>生成报表管理</title>
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
		function toDownload(path){
			$.ajax({
				url : '${ctx}/rpt/core/rptGen/isExist?path='+path,
				type : 'POST',
				async:false,
				success : function(resp, textStatus) {
					//console.log(resp);
					var dataObj=eval("("+resp+")");//转换为json对象
					if(dataObj.result=="success"){
						window.open('${ctxMain}'+path);
					}else {
						top.$.jBox.tip("文件不存在!", 'info');
					}
				},
				error : function(){
					alert("失败！");
				} 
			});
		}
		function toDownloadPdf(path,name){
			$.ajax({
				url : '${ctx}/rpt/core/rptGen/isExist?path='+path,
				type : 'POST',
				async:false,
				success : function(resp, textStatus) {
					//console.log(resp);
					var dataObj=eval("("+resp+")");//转换为json对象
					if(dataObj.result=="success"){
						window.location="${ctx}/rpt/core/rptGen/downloadPdf?path="+path+"&filename="+name;
					}else {
						top.$.jBox.tip("文件不存在!", 'info');
					}
				},
				error : function(){
					alert("失败！");
				} 
			});
		}
		function viewRpt(path){
			$.ajax({
				url : '${ctx}/rpt/core/rptGen/isExist?path='+path,
				type : 'POST',
				async:false,
				success : function(resp, textStatus) {
					//console.log(resp);
					var dataObj=eval("("+resp+")");//转换为json对象
					if(dataObj.result=="success"){
						window.location="${ctx}/rpt/core/rptGen/viewRpt?path="+path;
					}else {
						top.$.jBox.tip("文件不存在!", 'info');
					}
				},
				error : function(){
					alert("失败！");
				} 
			});
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/rpt/core/rptGen/">生成报表列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="rptGen" action="${ctx}/rpt/core/rptGen/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
			</div><div style="margin-top:8px;">
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
				<label>报表日期：</label>
			<input id="rptDate" name="rptDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate "
				value="<fmt:formatDate value="${rptGen.rptDate}" pattern="yyyy-MM-dd"/>"
				onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></div>
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
			    <th>报表分类</th>
			    <th>报表类型</th>
			    <th>报表模板</th>
				<th>报表时间</th>
				<th>所属运营商</th>
				<shiro:hasPermission name="rpt:core:rptGen:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="rptGen">
			<tr>
				<td>
					${fns:getDictLabel(rptGen.rptCategory, 'ln_rpt_rptcategory', '')}
				</td>
				<td>
					${fns:getDictLabel(rptGen.rptType, 'ln_rpt_rpttype', '')}
				</td>
				<td>
					${rptGen.rptCm.cmName}
				</td>
				<td>
					<fmt:formatDate value="${rptGen.rptDate}" pattern="yyyy-MM-dd"/>
				</td>
				<td>
					${rptGen.corporation.name}
				</td>
				<shiro:hasPermission name="rpt:core:rptGen:view"><td>
    				<a href="javascript:viewRpt('${rptGen.filePath}')">预览</a>
    				<a href="${ctxMain}${rptGen.filePath}">预览</a>
    				<a href="javascript:toDownload('${rptGen.pathExcel}')">excel下载</a>
    				<a href="javascript:toDownloadPdf('${rptGen.pathPdf}','${rptGen.namePdf}')">pdf下载</a>
    				<a href="javascript:toDownload('${rptGen.pathWord}')">word下载</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>