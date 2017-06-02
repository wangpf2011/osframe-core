<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>流程启动</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#name").focus();
			$("#inputForm").validate({
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
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="#">流程启动</a></li>
	</ul>
	<tags:message content="${message}"/>
	<form:form id="inputForm" modelAttribute="form" action="${ctx}/workflow/form/dynamic/startProc/${procDefId}" method="post" class="form-horizontal">
		<sys:message content="${message}"/>
		
		<c:forEach items="${form}" var="prop">
			<c:choose>
				<c:when test="${prop.type=='string'}">
					<div class="control-group">
						<label class="control-label">${prop.name}</label>
						<div class="controls">
							<input type='text' name="fp_${prop.id}" htmlEscape="false" class="required"/>
						</div>
					</div>
				</c:when>
				<c:when test="${prop.type=='long'}">
					<div class="control-group">
						<label class="control-label">${prop.name}</label>
						<div class="controls">
							<input type='text' name="fp_${prop.id}" htmlEscape="false" class="number required"/>
						</div>
					</div>
				</c:when>
				<c:when test="${prop.type=='date'}" >
					<div class="control-group">
						<label class="control-label">${prop.name}</label>
						<div class="controls">
							<input id="fp_${prop.id}" name="fp_${prop.id}" type="text" readonly="readonly" maxlength="20" class="Wdate required"
								onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
						</div>
					</div>
				</c:when>
				<c:when test="${prop.type=='enum'}">
					<div class="control-group">
						<label class="control-label">${prop.name}</label>
						<div class="controls">
							<select id="${prop.id}" name="fp_${prop.id}">
							<c:forEach items="${prop.dicts}" var="dict">
								<option value="${dict.value}">${dict.label}</option>
							</c:forEach>
							</select>
						</div>
					</div>
				</c:when>
				<c:otherwise>
					<div class="control-group">
						<label class="control-label">${prop.name}</label>
						<div class="controls">
							<input type='text' name="fp_${prop.id}" htmlEscape="false" class="input-xlarge digits required"/>
						</div>
					</div>
				</c:otherwise>
			</c:choose>
		</c:forEach>
		<div class="form-actions">
			<shiro:hasPermission name="oa:leave:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>
