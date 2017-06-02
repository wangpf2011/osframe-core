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
			
		});
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/rpt/core/rptCm/">报表模板列表</a></li>
		<li class="active"><a href="${ctx}/rpt/core/rptCm/form?id=${rptCm.id}">报表模板审核<shiro:hasPermission name="rpt:core:rptCm:edit">${not empty rptCm.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="rpt:core:rptCm:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="rptCm" action="${ctx}/rpt/core/rptCm/checkSave" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="filePath" id="filePath"/>
		<form:hidden path="fileName" id="fileName"/>
		<tags:message content="${message}"/>		
		<div class="control-group">
			<label class="control-label">报表模板名称：</label>
			<div class="controls">
				<form:input path="cmName" htmlEscape="false" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">报表分类：</label>
			<div class="controls">
				<form:select path="rptCategory" id="rptCategory" class="input-xlarge ">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('ln_rpt_rptcategory')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">报表类型：</label>
			<div class="controls">
				<form:select path="rptType" class="input-xlarge ">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('ln_rpt_rpttype')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">模板文件：</label>
			<div class="controls">
			  <form:input path="realFilename" id="relfileName" htmlEscape="false" class="input-xlarge required" readonly="true"/>
				<input id="btnUpload" class="btn btn-primary" type="button" value="上传" onclick="doUpLoadFile()"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">文号：</label>
			<div class="controls">
				<form:input path="fileNo" htmlEscape="false" class="input-xlarge required" maxlength="32"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">审核结果：</label>
			<div class="controls">
				<form:select path="auditResult" class="input-xlarge required">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('sys_yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">审核意见：</label>
			<div class="controls">
				<form:textarea path="auditContent" htmlEscape="false" rows="4" class="input-xxlarge " maxlength="255"/>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="rpt:core:rptCm:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>