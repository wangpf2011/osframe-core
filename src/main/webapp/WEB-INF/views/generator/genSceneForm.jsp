<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>代码生成场景管理管理</title>
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
			// 注册下拉框事件
			$('#sceneType').change(function(){ 
				var p1=$(this).children('option:selected').val();
				if(p1=="1"){//
					$("#div_sub").hide();
				}else if(p1=="2"||p1 == '4'){
					$("#div_sub").show();
				}else{
					$("#div_sub").hide();
				}
			});
			/////
		});
		//
		
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/generator/genScene/">场景配置列表</a></li>
		<li class="active">
			<a href="${ctx}/generator/genScene/form?id=${genScene.id}">场景配置
			<shiro:hasPermission name="generator:genScene:edit">${not empty genScene.id?'修改':'添加'}</shiro:hasPermission>
			<shiro:lacksPermission name="generator:genScene:edit">查看</shiro:lacksPermission>
			</a>
		</li>
	</ul><br/>
<div class="container-fluid">
	<div class="row-fluid">
		<div class="span12">
			<h3 class="text-center text-info">
				标准代码生成器 V2015
			</h3>
		</div>
	</div>
</div>
	<div class="container-fluid">
	<legend>第一步:场景选择</legend>
	<form:form id="inputForm" modelAttribute="genScene" action="${ctx}/generator/genScene/goScenSeting" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<tags:message content="${message}"/>
		<div class="control-group">
			<label class="control-label">场景描述:</label>
			<div class="controls">
				<form:input path="comments" htmlEscape="false" maxlength="200" class="required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">表名:</label>
			<div class="controls">
				<form:input path="name" htmlEscape="false" maxlength="200" class="required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">场景:</label>
			<div class="controls">
                <form:select path="sceneType">
                     <form:options items="${fns:getDictList('sys_gen_scene_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                </form:select>
			</div>
		</div>
		<div id="div_sub" class="control-group hide">
			<label class="control-label">从表名:</label>
			<div class="controls">
				<form:input path="subname" htmlEscape="false" maxlength="200" class="required"/>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="generator:genScene:edit">
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="下一步"/>&nbsp;
			</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>
