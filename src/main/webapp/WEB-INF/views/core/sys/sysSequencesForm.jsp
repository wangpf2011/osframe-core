<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>序列管理管理</title>
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
		<li><a href="${ctx}/sys/sysSequences/">序列管理列表</a></li>
		<li class="active"><a href="${ctx}/sys/sysSequences/form?id=${sysSequences.id}">序列管理<shiro:hasPermission name="sys:sysSequences:edit">${not empty sysSequences.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="sys:sysSequences:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="sysSequences" action="${ctx}/sys/sysSequences/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<tags:message content="${message}"/>
		<div class="control-group">
			<label class="control-label">序列名称：</label>
			<div class="controls">
	            <c:choose>
					<c:when test="${not empty sysSequences.id}" >
						<form:input path="seqName" htmlEscape="false" disabled="true" class="input-xlarge required" placeholder="不可重复：如seq_tablename_snno"/>
					</c:when>
					<c:otherwise>
						<form:input path="seqName" htmlEscape="false" class="input-xlarge required" placeholder="不可重复：如seq_tablename_snno"/>
					</c:otherwise>
				</c:choose>
				
			</div>
		</div>		
		<div class="control-group">
			<label class="control-label">当前序列值：</label>
			<div class="controls">
				<form:input path="seqVal" htmlEscape="false" class="input-xlarge required digits"  placeholder="数字"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">序列位数：</label>
			<div class="controls">
				<form:input path="seqWidth" htmlEscape="false" class="input-xlarge " placeholder="位数不足 自动补充0)"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">前缀：</label>
			<div class="controls">
				<form:input path="leftpad" htmlEscape="false" class="input-xlarge " placeholder="序列的前缀"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">后缀：</label>
			<div class="controls">
				<form:input path="rightpad" htmlEscape="false" class="input-xlarge "  placeholder="序列的后缀"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">备注：</label>
			<div class="controls">
				<form:input path="remarks" htmlEscape="false" class="input-xlarge " />
			</div>
		</div>

		<div class="form-actions">
			<shiro:hasPermission name="sys:sysSequences:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>