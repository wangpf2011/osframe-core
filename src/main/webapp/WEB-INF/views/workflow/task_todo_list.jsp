<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>待办任务</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
	$(document).ready(function() {
		$("#searchForm").validate({
			submitHandler: function(form){
				loading('正在查询，请稍等...');
				form.submit();
			}
		});
	});
		/**
		 * 签收任务
		 */
		function claim(taskId) {
			$.get('${ctx}/workflow/task/claim' ,{taskId: taskId}, function(data) {
				if (data == 'true'){
		        	top.$.jBox.tip('签收完成');
		            location = '${ctx}/workflow/task/todo/';
				}else{
		        	top.$.jBox.tip('签收失败');
				}
		    });
		}
		
		/**
		 * 委托任务
		 */
		function delegate(taskId) {
			$("#jbox-iframe").attr("height","280px");
			// 正常打开	
			top.$.jBox.open("iframe:${ctx}/tag/treeselect?url="+encodeURIComponent("/yunoa/cs/customer/userDataAll?dept=cf30362e68444323b7ed80be1e44a71e"), "选择人员", 300, 400, {
				buttons:{"确定":"ok", "关闭":true}, submit:function(v, h, f) {
					if(v=="ok") {
						var tree = h.find("iframe")[0].contentWindow.tree;
						var nodes = tree.getSelectedNodes();
						if(nodes.length > 0) {
							for(var i=0; i<nodes.length; i++){
								if (nodes[i].level == 0){
									top.$.jBox.tip("不能选择根节点（"+nodes[i].name+"）请重新选择。");
									return false;
								}
								if (nodes[i].isParent){
									top.$.jBox.tip("不能选择父节点（"+nodes[i].name+"）请重新选择。");
									return false;
								}
							}
							var usrId = nodes[0].id;
							$.get('${ctx}/workflow/task/delegate' ,{taskId: taskId, usrId: usrId}, function(data) {
								if (data == 'true'){
						            location = '${ctx}/workflow/task/todo/';
								}else{
						        	top.$.jBox.tip('委托任务失败');
								}
						    });
						}
					}
				}
			});
			$("#jbox-iframe").attr("height","280px");
		}
		/**
		 * 转移任务
		 */
		function transfer(taskId,taskDefinitionKey) {
			var url;
			if(taskDefinitionKey=="info_receive"){
				url="/infoback/information/transferUser";
			}else if(taskDefinitionKey=="send_check"||taskDefinitionKey=="receive_check"){
				url="/infoback/information/checkUser?enname=info_check";
			}
			$("#jbox-iframe").attr("height","280px");
			// 正常打开	
			top.$.jBox.open("iframe:${ctx}/tag/treeselect?url="+encodeURIComponent(url), "选择转移人员", 300, 400, {
				buttons:{"确定":"ok", "关闭":true}, submit:function(v, h, f) {
					if(v=="ok") {
						var tree = h.find("iframe")[0].contentWindow.tree;
						var nodes = tree.getSelectedNodes();
						if(nodes.length > 0) {
							for(var i=0; i<nodes.length; i++){
								if (nodes[i].level == 0){
									top.$.jBox.tip("不能选择根节点（"+nodes[i].name+"）请重新选择。");
									return false;
								}
								if (nodes[i].isParent){
									top.$.jBox.tip("不能选择父节点（"+nodes[i].name+"）请重新选择。");
									return false;
								}
							}
							var usrId = nodes[0].id;
							$.get('${ctx}/workflow/task/delegate' ,{taskId: taskId, usrId: usrId}, function(data) {
								if (data == 'true'){
						            location = '${ctx}/workflow/task/todo/';
								}else{
						        	top.$.jBox.tip('转移任务失败');
								}
						    });
						}
					}
				}
			});
			$("#jbox-iframe").attr("height","280px");
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/workflow/task/todo/">待办任务</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="act" action="${ctx}/workflow/task/todo/" method="get" class="breadcrumb form-search">
		<div>
			<label>流程类型：&nbsp;</label>
			<form:select path="procDefKey" class="input-medium">
				<form:option value="" label="全部流程"/>
				<form:options items="${fns:getDictList('act_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</form:select>
			<label>创建时间：</label>
			<input id="beginDate"  name="beginDate"  type="text" readonly="readonly" maxlength="20" class="input-medium Wdate" style="width:163px;"
				value="<fmt:formatDate value="${act.beginDate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"/>
				　--　
			<input id="endDate" name="endDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate" style="width:163px;"
				value="<fmt:formatDate value="${act.endDate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"/>
			&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
		</div>
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>标题</th>
				<th>当前环节</th>
				<th>流程名称</th>
				<th>流程版本</th>
				<th>发起人</th>
				<th>创建时间</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${list}" var="act">
				<c:set var="task" value="${act.task}" />
				<c:set var="vars" value="${act.vars}" />
				<c:set var="procDef" value="${act.procDef}" /><%--
				<c:set var="procExecUrl" value="${act.procExecUrl}" /> --%>
				<c:set var="status" value="${act.status}" />
				<tr>
					<td>
						<c:if test="${empty task.assignee}">
							<a href="javascript:claim('${task.id}');" onclick="return confirmx('确认要签收【${fns:abbr(not empty vars.map.title ? vars.map.title : task.id, 60)}】？', this.href)" title="签收任务">${fns:abbr(not empty act.vars.map.title ? act.vars.map.title : task.id, 60)}</a>
						</c:if>
						<c:if test="${not empty task.assignee}">
							<a href="${ctx}/workflow/task/form?taskId=${task.id}&taskName=${fns:urlEncode(task.name)}&taskDefKey=${task.taskDefinitionKey}&procInsId=${task.processInstanceId}&procDefId=${task.processDefinitionId}&status=${status}">${fns:abbr(not empty vars.map.title ? vars.map.title : task.id, 60)}</a>
						</c:if>
					</td>
					<td>
						<a target="_blank" href="${ctx}/workflow/task/trace/photo/${task.processDefinitionId}/${task.executionId}">${task.name}</a>
					</td>
					<td>${procDef.name}</td>
					<td><b title='流程版本号'>V: ${procDef.version}</b></td>
					<td>${act.assigneeName}</td>
					<td><fmt:formatDate value="${task.createTime}" type="both"/></td>
					<td>
						<c:if test="${empty task.assignee}">
							<a href="javascript:claim('${task.id}');" onclick="return confirmx('确认要签收【${fns:abbr(not empty vars.map.title ? vars.map.title : task.id, 60)}】？', this.href)" >签收任务</a>
						</c:if>
						<c:if test="${not empty task.assignee}">
							<a href="${ctx}/workflow/task/form?taskId=${task.id}&taskName=${fns:urlEncode(task.name)}&taskDefKey=${task.taskDefinitionKey}&procInsId=${task.processInstanceId}&procDefId=${task.processDefinitionId}&status=${status}">任务办理</a>
						    <%-- <c:if test="${fn:contains(task.processDefinitionId,'work_card')}">
								<a href="javascript:delegate('${task.id}');">委托任务</a>
						    </c:if>
						    <c:if test="${fn:contains(task.processDefinitionId,'info_back')}">
									<a href="javascript:transfer('${task.id}','${task.taskDefinitionKey }');">转移</a>
						    </c:if> --%>
						</c:if>
						<shiro:hasPermission name="act:process:edit">
							<c:if test="${empty task.executionId}">
								<a href="${ctx}/workflow/task/deleteTask?taskId=${task.id}&reason=" onclick="return promptx('删除任务','删除原因',this.href);">删除任务</a>
							</c:if>
						</shiro:hasPermission>
						<a target="_blank" href="${ctx}/workflow/task/trace/photo/${task.processDefinitionId}/${task.executionId}">跟踪</a><%--  
						<a target="_blank" href="${ctx}/workflow/task/trace/info/${task.processInstanceId}">跟踪信息</a> --%>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</body>
</html>
