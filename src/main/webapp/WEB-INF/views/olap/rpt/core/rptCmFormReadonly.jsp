<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>报表模板管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			//$("#name").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				onfocusout: function(element){$(element).valid();},
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
			
			if("${rptCm.rptCategory}"=="3"){
				$("#div1").show();
			}else{
				$("#div1").hide();
			}
		});
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/rpt/core/rptCm/">报表模板列表</a></li>
		<li class="active"><a href="${ctx}/rpt/core/rptCm/readForm?id=${rptCm.id}">报表模板查看</a></li>
		<li><a href="${ctx}/rpt/core/rptCmChildren/readList?cmId=${rptCm.id}">子报表模板</a></li>
		<c:if test="${rptCm.rptCategory==3}">
		<li><a href="${ctx}/rpt/core/rptCm/readQueryList?id=${rptCm.id}">报表查询条件</a></li>
		</c:if>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="rptCm" action="${ctx}/rpt/core/rptCm/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="filePath" id="filePath"/>
		<form:hidden path="fileName" id="fileName"/>
		<tags:message content="${message}"/>		
		<div class="control-group">
			<label class="control-label">报表模板名称：</label>
			<div class="controls">
				<form:input path="cmName" htmlEscape="false" class="input-xlarge " readonly="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">报表分类：</label>
			<div class="controls">
				<form:select path="rptCategory" id="rptCategory" class="input-xlarge " disabled="true">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('ln_rpt_rptcategory')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">报表类型：</label>
			<div class="controls">
				<form:select path="rptType" class="input-xlarge " disabled="true">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('ln_rpt_rpttype')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
		</div>
		<div class="control-group" id="div1">
			<label class="control-label">报表页面名称：</label>
			<div class="controls">
				<form:input path="pageName" htmlEscape="false" class="input-xlarge " readonly="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">模板文件：</label>
			<div class="controls">
			  <form:input path="realFilename" id="relfileName" htmlEscape="false" class="input-xlarge required" readonly="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">文号：</label>
			<div class="controls">
				<form:input path="fileNo" htmlEscape="false" class="input-xlarge required" maxlength="32" readonly="true"/>
			</div>
		</div>
		<div id="div1" class="form-actions">
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>