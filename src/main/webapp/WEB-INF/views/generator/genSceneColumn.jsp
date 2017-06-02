<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>标准代码生成器-S3</title>
	<meta name="decorator" content="default"/>
	<script id="relatp" type="text/javascript">
		<c:forEach items="${fns:getDictList('sys_gen_relate')}" var="dict">
			<option value="${dict.value}" ${dict.value==relatecolumn.relateType?'selected':''} title="${dict.description}">${dict.label}</option>
		</c:forEach>
	</script>
	<script id="relaop" type="text/javascript">
		<c:forEach items="${fns:getDictList('sys_gen_relate_op')}" var="dict">
			<option value="${dict.value}" ${dict.value==relatecolumn.relateOp?'selected':''} title="${dict.description}">${dict.label}</option>
		</c:forEach>
	</script>
	<script id="chlrelatp" type="text/javascript">
		<c:forEach items="${fns:getDictList('sys_gen_relate')}" var="dict">
			<option value="${dict.value}" ${dict.value==subrelatecolumn.relateType?'selected':''} title="${dict.description}">${dict.label}</option>
		</c:forEach>
	</script>
	<script id="chlrelaop" type="text/javascript">
		<c:forEach items="${fns:getDictList('sys_gen_relate_op')}" var="dict">
			<option value="${dict.value}" ${dict.value==subrelatecolumn.relateOp?'selected':''} title="${dict.description}">${dict.label}</option>
		</c:forEach>
	</script>
	<script type="text/javascript">
		$(document).ready(function() {
			//
			$("#className").focus();
		});
			///代码生成
			//jquery ajax访问例子，返回json字符串号
			function doGenCode(){
				//
                var paramObj = { sc:{ "tabcols" :[]}} ;
				//获得 资源类信息基本信息
                paramObj.sc["packageName"] = $("#packageName" ).val();
                paramObj.sc["moduleName"] = $("#moduleName" ).val();
                paramObj.sc["subModuleName"] = $("#subModuleName" ).val();
                paramObj.sc["className"] = $( "#className").val();
                paramObj.sc["classAuthor"] = $( "#classAuthor").val();
                paramObj.sc["functionName"] = $( "#functionName").val();
                paramObj.sc["tableName"] = $( "#tableName").val();
                paramObj.sc["javaResFilePath"] = $( "#javaResFilePath").val();
                
				//获得 Entity 字段 配置相关信息
		        var stData={};
		        stData['colname'] ="测试列名";
                paramObj.sc.tabcols.push(stData);
				var paramStr = $.toJSON(paramObj.sc);
				$.ajax({
				    url : '${ctx}/mvc/codeGenTools/doGenCode',
					type : 'POST',
					data : {codeS : paramStr },
					async:false,
					success : function(resp, textStatus) {
						 var dataObj=eval("("+resp+")");//转换为json对象
				         var str= new Array();   
				         str=dataObj.message.split(";");   
				         var html="";
			             for (i=0;i<str.length ;i++ ){ 
			            	 if(html==""){
			            		 html='<li>'+str[i]+'</li>';
			             	 }else{
			             		 html=html+'<li>'+str[i]+'</li>';
			             	 }
					     }   
			            $("#div_saomao_zhifu").fadeIn(); 
			            $("#codeResShow").html(html);
						$("#pay_show_a").click();
					},
					error : function(){
						alert("失败！");
					} 
				});
			}
			function addLine(){
				var cnt = $("#jlcz tr").length;
				var newrow = '<tr  class="warning">';
				newrow+='<td>';
				newrow+='<select name="relatecolumnList[' + cnt + '].relateType" class="required input-mini" style="width:180px;*width:170px" onchange="relTypeChange(this)">';
				newrow+=$("#relatp").html();
				newrow+='</select>';
				newrow+='<input type="hidden" name="relatecolumnList[' + cnt + '].id" value="${relatecolumn.id}"/>';
				newrow+='<input type="hidden" name="relatecolumnList[' + cnt + '].delFlag" value="${relatecolumn.delFlag}"/>';
				newrow+='<input type="hidden" name="relatecolumnList[' + cnt + '].genScene" value="${relatecolumn.genScene}"/>';
				newrow+='</td>';
				newrow+='<td>';
				newrow+='<input type="text" name="relatecolumnList[' + cnt + '].relateEntity" value="${relatecolumn.relateEntity}"/>';
				newrow+='</td>';
				newrow+='<td>';
				newrow+='<input type="text" name="relatecolumnList[' + cnt + '].mappedby" value="${relatecolumn.mappedby}"/>';
				newrow+='</td>';
				newrow+='<td>';
				newrow+='<input type="text" name="relatecolumnList[' + cnt + '].jointable" value="${relatecolumn.jointable}"/>';
				newrow+='</td>';
				newrow+='<td>';
				newrow+='<input type="text" name="relatecolumnList[' + cnt + '].joincolumn" value="${relatecolumn.joincolumn}" disabled/>';
				newrow+='</td>';
				newrow+='<td>';
				newrow+='<input type="text" name="relatecolumnList[' + cnt + '].inverseJoincolumn" value="${relatecolumn.inverseJoincolumn}" maxlength="200" class="required" style="width:100px;" disabled/>';
				newrow+='</td>';
				newrow+='<td>';
				newrow+='<select name="relatecolumnList[' + cnt + '].relateOp" class="required input-mini" style="width:85px;*width:75px">';
				newrow+=$("#relaop").html();
				newrow+='</select>';
				newrow+='</td>';
				newrow+='<td>';
				newrow+='<input type="button" name="deladdtr'+ cnt +'" value="删除" onclick="deltr(this)" class="btn btn-primary" style="width:50px;"/>';
				newrow+='</td>';
				newrow+='</tr>';
				$('#jlcz').append(newrow);
			}
			function addLineChl(){
				var cnt = $("#jlczchl tr").length;
				var newrow = '<tr  class="warning">';
				newrow+='<td>';
				newrow+='<select name="subRelatecolumnList[' + cnt + '].relateType" class="required input-mini" style="width:180px;*width:170px" onchange="relTypeChange(this)">>';
				newrow+=$("#relatp").html();
				newrow+='</select>';
				newrow+='<input type="hidden" name="subRelatecolumnList[' + cnt + '].id" value="${subrelatecolumn.id}"/>';
				newrow+='<input type="hidden" name="subRelatecolumnList[' + cnt + '].delFlag" value="${subrelatecolumn.delFlag}"/>';
				newrow+='<input type="hidden" name="subRelatecolumnList[' + cnt + '].genScene" value="${subrelatecolumn.genScene}"/>';
				newrow+='</td>';
				newrow+='<td>';
				newrow+='<input type="text" name="subRelatecolumnList[' + cnt + '].relateEntity" value="${subrelatecolumn.relateEntity}"/>';
				newrow+='</td>';
				newrow+='<td>';
				newrow+='<input type="text" name="subRelatecolumnList[' + cnt + '].mappedby" value="${subrelatecolumn.mappedby}"/>';
				newrow+='</td>';
				newrow+='<td>';
				newrow+='<input type="text" name="subRelatecolumnList[' + cnt + '].jointable" value="${subrelatecolumn.jointable}"/>';
				newrow+='</td>';
				newrow+='<td>';
				newrow+='<input type="text" name="subRelatecolumnList[' + cnt + '].joincolumn" value="${subrelatecolumn.joincolumn}" disabled/>';
				newrow+='</td>';
				newrow+='<td>';
				newrow+='<input type="text" name="subRelatecolumnList[' + cnt + '].inverseJoincolumn" value="${subrelatecolumn.inverseJoincolumn}" maxlength="200" class="required" style="width:100px;" disabled/>';
				newrow+='</td>';
				newrow+='<td>';
				newrow+='<select name="subRelatecolumnList[' + cnt + '].relateOp" class="required input-mini" style="width:85px;*width:75px">';
				newrow+=$("#relaop").html();
				newrow+='</select>';
				newrow+='</td>';
				newrow+='<td>';
				newrow+='<input type="button" name="deladdtr'+ cnt +'" value="删除" onclick="deltr(this)" class="btn btn-primary" style="width:50px;"/>';
				newrow+='</td>';
				newrow+='</tr>';
				$('#jlczchl').append(newrow);
			}
			function goToStep2(){
				window.location.href="${ctx}/generator/genScene/save?genScene=${genScene}";
			}
			function parentfun(){
				$("#parentli").attr("class","active");
				$("#childli").attr("class","");
				$("#parentpage").css("display","block");
				$("#childpage").css("display","none");
			}
			function childfun(){
				$("#parentli").attr("class","");
				$("#childli").attr("class","active");
				$("#parentpage").css("display","none");
				$("#childpage").css("display","block");
			}
			function smallChange(obj){
				var v = document.getElementsByName(obj.name);
				var $v = $(v);
				if($v[0].checked == true) {
					$("#"+obj.name+"Val").val("1");
					$v.parent().nextAll().find("input[type='checkbox']").attr('disabled',false);
					$v.attr("checked","checked");
				} else {
					$("#"+obj.name+"Val").val("0");
					$v.parent().nextAll().find("input[type='checkbox']").attr('disabled',true);
					$v.removeAttr("checked");
					$v.parent().nextAll().find("input[type='hidden']").val("0");
					$v.parent().nextAll().find("input[type='checkbox']").removeAttr("checked");
				}
			}
			function valueChange(obj){
				var v = document.getElementsByName(obj.name);
				var $v = $(v);
				if($v.prop("checked") == true){
					$("#"+obj.name+"Val").attr("value","1");
					$v.attr("checked","checked");
				}else {
					$("#"+obj.name+"Val").attr("value","0");
					$v.removeAttr("checked");
				}
			}
			function deltr(obj){
				var v = document.getElementsByName(obj.name);
				var $v = $(v);
				$v.parent().parent().hide();
				if(obj.name.indexOf("deladdtr") != -1){
					$v.parent().parent().empty();
				} else {
					$v.parent().prev().prev().children().attr("value","99");
				}
			}
			
			$(document).ready(function() {
				$("#name").focus();
			});
			
			function typeChange(obj){
				var v = document.getElementsByName(obj.name);
				var $v = $(v);
				if($v.val() == "2"){
					$v.parent().next().children().removeAttr("disabled");
				}else{
					$v.parent().next().children().attr("disabled","disabled");
				}
			}
			function relTypeChange(obj){
				var v = document.getElementsByName(obj.name);
				var $v = $(v);
				if($v.val() == "4" || $v.val() == "5"){
					$v.parent().next().next().children().attr("disabled","disabled");
					$v.parent().next().next().next().children().attr("disabled","disabled");
					$v.parent().next().next().next().nextAll().find("input[type='text']").removeAttr("disabled");
				}else if($v.val() == "2"){
					$v.parent().next().next().next().children().attr("disabled","disabled");
					$v.parent().next().next().next().next().children().removeAttr("disabled");
					$v.parent().next().next().next().next().next().children().attr("disabled","disabled");
				}else{
					$v.parent().next().next().children().removeAttr("disabled");
					$v.parent().next().next().next().children().removeAttr("disabled");
					$v.parent().next().next().next().nextAll().find("input[type='text']").attr("disabled","disabled");
				}
			}
			
			function relateChange(obj){
				var v = document.getElementsByName(obj.name);
				var $v = $(v);
				if($v.val() != "0"){
					$v.parent().next().children().removeAttr("disabled");
					$v.parent().next().next().children().removeAttr("disabled");
				}else{
					$v.parent().next().children().attr("disabled","disabled");
					$v.parent().next().next().children().attr("disabled","disabled");
				}
			}
			
	</script>
</head>
<body>
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
	<legend>第二步:信息配置</legend>
	<form:form id="inputForm1" modelAttribute="genScene" action="${ctx}/generator/genScene/save"   method="post" class="form-horizontal" >
	<tags:message content="${message}"/>
	<input id="id" name="id"  value="${genScene.id}"  type="hidden">
	<input id="sceneType" name="sceneType"  value="${genScene.sceneType}" type="hidden" >
		<fieldset>
			<div class="control-group">
					<label>场景描述：</label>
					<input id="comments" name="comments"  value="${genScene.comments}"  type="text" readonly="readonly"/>
					<label>场景类型：</label>
					<input id="sceneTypeLabel" name="sceneTypeLabel"  value="${fns:getDictLabel(genScene.sceneType,'sys_gen_scene_type','')}"  type="text" readonly="readonly"/>
					<label>主表名称：</label>
					<input id="name" name="name"  value="${genScene.name}"  type="text" readonly="readonly"/>
			</div>		
					<c:if test="${genScene.sceneType == '2'||genScene.sceneType == '4'}">
					<div style="margin-top:8px;">
					<label>从表名称：</label>
					<input id="subname" name="subname" value="${genScene.subname}"  type="text" readonly="readonly"/>
					</div>
					</c:if>
			
		</fieldset>
		</br>
<c:if test="${genScene.sceneType == '2'||genScene.sceneType == '4'}">
	<ul class="nav nav-tabs">
		<li id="parentli" class="active">
			<a href="javascript:parentfun()">主表实体模型信息</a>
		</li>
		<li id="childli">
			<a href="javascript:childfun()">从表实体模型信息</a>
		</li>
	</ul>
</c:if>
	<div id="parentpage">
		<fieldset>
			<div class="control-group">
					<label>主表类名：</label>
					<input id="className" name="className"  value="${genScene.className}"  type="text" maxlength="200"  placeholder="class_name"/>
					<label>页记录数：</label>
					<input id="pageSize" name="pageSize"  value=" ${not empty genScene.pageSize?genScene.pageSize:'10'}" type='text' maxlength="200"  placeholder="10"/>
			        <c:if test="${genScene.sceneType == '5'}">
						<label>树节点显示名称：</label>
						<input id="showName" name="showName"  value="${genScene.showName}"  type="text" maxlength="200"  placeholder="树中要显示的字段名"/>
		            </c:if>
			</div>
		</fieldset>
		
		<div class="row-fluid">
			<div class="span12">
				<table class="table table-hover table-bordered" id="maintbl">
					<thead>
						<tr>
							<th>
								编号
							</th>
							<th>
								 字段名
							</th>
							<th>
								数据类型
							</th>
							<th>
								字段名称
							</th>
							<th>
								JAVA字段
							</th>
							<th>
								级联关系
							</th>
							<th>
								级联实体
							</th>
							<th>
								级联操作
							</th>						
							<th>
								Entity显示
							</th>
							<th>
								可为空
							</th>
							<th>
								查询条件
							</th>
							<th>
								模糊查询
							</th>
							<th>
								列表显示
							</th>
							<c:if test="${genScene.sceneType == '3'}">
							<th>
								可修改
							</th>
							</c:if>
							<c:if test="${genScene.sceneType != '3'}">
							<th>
								表单显示
							</th>
							</c:if>
							<th>
								显示控件
							</th>	
							<th>
								字典名
							</th>																	
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${genScene.columnList}" var="column" varStatus="vs">
							<tr${column.delFlag eq '1'?' class="error" title="已删除的列，保存之后消失！"':''}>
								<td nowrap>
									${vs.index + 1}
										<input type="hidden" name="columnList[${vs.index}].id" value="${column.id}"/>
								</td>
								<td>
									${column.name}
									<input type="hidden" name="columnList[${vs.index}].name" value="${column.name}"/>
								</td>
								<td>
									${column.jdbcType}
									<input type="hidden" name="columnList[${vs.index}].jdbcType" value="${column.jdbcType}"/>
									<input type="hidden" name="columnList[${vs.index}].javaType" value="${column.javaType}"/>
								</td>
								<td>
									<input type="text" name="columnList[${vs.index}].comments"  value="${column.comments}" maxlength="200" class="required" style="width:100px;"/>
								</td>
								<td>
									<input type="text" name="columnList[${vs.index}].javaField" value="${column.javaField}" maxlength="200" class="required" style="width:100px;"/>
								</td>
								<td>
									<select name="columnList[${vs.index}].isRelate" class="required input-mini" style="width:85px;*width:75px" onchange="relateChange(this)">
										<c:forEach items="${fns:getDictList('sys_gen_is_relate')}" var="dict">
											<option value="${dict.value}" ${dict.value==column.isRelate?'selected':''} title="${dict.description}">${dict.label}</option>
										</c:forEach>
									</select>
								</td>
								<td>
									<input type="text" name="columnList[${vs.index}].relateEntity" value="${column.relateEntity}" ${'0'==column.isRelate?'disabled':''} maxlength="200" class="required" style="width:100px;"/>
								</td>
								<td>
									<select name="columnList[${vs.index}].relateOp" class="required input-mini" style="width:95px;*width:85px" ${'0'==column.isRelate?'disabled':''}>
										<c:forEach items="${fns:getDictList('sys_gen_relate_op')}" var="dict">
											<option value="${dict.value}" ${dict.value==column.javaType?'selected':''} title="${dict.description}">${dict.label}</option>
										</c:forEach>
									</select>
								</td>
								<td>
									<input id="${vs.index}${column.id}isEntityVal" name="columnList[${vs.index}].isEntity"  value="${column.isEntity}"  type="hidden">
									<input type="checkbox" name="${vs.index}${column.id}isEntity" value="${column.isEntity}" ${column.isEntity eq '1' ? 'checked' : ''}  onchange="smallChange(this)" />
								</td>
								<td>
									<input id="${vs.index}${column.id}isNullVal" name="columnList[${vs.index}].isNull"  value="${column.isNull}"  type="hidden">
									<input type="checkbox" name="${vs.index}${column.id}isNull" value="${column.isNull}" ${column.isNull eq '1' ? 'checked' : ''} onchange="valueChange(this)"/>
								</td>
								<td>
									<input id="${vs.index}${column.id}isQueryVal" name="columnList[${vs.index}].isQuery"  value="${column.isQuery}"  type="hidden">
									<input type="checkbox" name="${vs.index}${column.id}isQuery" value="${column.isQuery}" ${column.isQuery eq '1' ? 'checked' : ''} onchange="valueChange(this)"/>
								</td>
								<td>
									<input id="${vs.index}${column.id}queryTypeVal" name="columnList[${vs.index}].queryType"  value="${column.queryType}"  type="hidden">
									<input type="checkbox" name="${vs.index}${column.id}queryType" value="${column.queryType}" ${column.queryType eq '1' ? 'checked' : ''} onchange="valueChange(this)"/>
								</td>
								<td>
									<input id="${vs.index}${column.id}isListVal" name="columnList[${vs.index}].isList"  value="${column.isList}"  type="hidden">
									<input type="checkbox" name="${vs.index}${column.id}isList" value="${column.isList}" ${column.isList eq '1' ? 'checked' : ''} onchange="valueChange(this)"/>
								</td>
								<c:if test="${genScene.sceneType == '3'}">
								<td>
									<input id="${vs.index}${column.id}isEditVal" name="columnList[${vs.index}].isEdit"  value="${column.isEdit}"  type="hidden">
									<input type="checkbox" name="${vs.index}${column.id}isEdit" value="${column.isEdit}" ${column.isEdit eq '1' ? 'checked' : ''} onchange="valueChange(this)"/>
								</td>
								</c:if>
								<c:if test="${genScene.sceneType != '3'}">
								<td>
									<input id="${vs.index}${column.id}isFormVal" name="columnList[${vs.index}].isForm"  value="${column.isForm}"  type="hidden">
									<input type="checkbox" name="${vs.index}${column.id}isForm" value="${column.isForm}" ${column.isForm eq '1' ? 'checked' : ''} onchange="valueChange(this)"/>
								</td>
								</c:if>
								<td>
									<select name="columnList[${vs.index}].showType" class="required" style="width:100px;" onchange="typeChange(this)">
										<c:forEach items="${fns:getDictList('sys_gen_show_type')}" var="dict">
											<option value="${dict.value}" ${dict.value==column.showType?'selected':''} title="${dict.description}">${dict.label}</option>
										</c:forEach>
									</select>
								</td>
								<td>
									<input type="text" name="columnList[${vs.index}].dictType" value="${column.dictType}" maxlength="200" class="input-mini" ${'2'!=column.showType?'disabled':''}/>
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	
			<div class="row-fluid">
				<div class="span10">
				</div>
				<div class="span2" align="right">
					 <button  onclick="addLine()"  class="btn btn-primary" type="button" style="position: right">添加级联</button>
				</div>
		</div>
	    <br>
		<div class="row-fluid">
			<div class="span12">
				<table class="table table-hover table-bordered">
				<thead>
					<tr>
						<th>
							级联关系
						</th>
						<th>
							级联实体
						</th>
						<th>
							mappedBy
						</th>
						<th>
						    joinTable
						</th>
						<th>
							joinColumn
						</th>
						<th>
							inverseJoinColumn
						</th>
						<th>
							级联操作
						</th>
						<th>
						</th>
					</tr>
				</thead>
				<tbody id="jlcz">
					<c:forEach items="${genScene.relatecolumnList}" var="relatecolumn" varStatus="relatevs">
						<tr${relatecolumn.delFlag eq '1'?' class="error" title="已删除的列，保存之后消失！"':''}>
								<td>
									<select name="relatecolumnList[${relatevs.index}].relateType" class="required input-mini" style="width:185px;*width:175px" onchange="relTypeChange(this)">
										<c:forEach items="${fns:getDictList('sys_gen_relate')}" var="dict">
											<option value="${dict.value}" ${dict.value==relatecolumn.relateType?'selected':''} title="${dict.description}">${dict.label}</option>
										</c:forEach>
									</select>
										<input type="hidden" name="relatecolumnList[${relatevs.index}].id" value="${relatecolumn.id}"/>
								</td>
								<td>
									<input type="text" name="relatecolumnList[${relatevs.index}].relateEntity" value="${relatecolumn.relateEntity}"/>
								</td>
								<td>
									<input type="text" name="relatecolumnList[${relatevs.index}].mappedby" value="${relatecolumn.mappedby}" ${'4'==relatecolumn.relateType?'readonly':''} ${'5'==relatecolumn.relateType?'readonly':''}/>
								</td>
								<td>
									<input type="text" name="relatecolumnList[${relatevs.index}].jointable" value="${relatecolumn.jointable}"  ${'2'==relatecolumn.relateType?'readonly':''} ${'4'==relatecolumn.relateType?'readonly':''} ${'5'==relatecolumn.relateType?'readonly':''}/>
								</td>
								<td>
									<input type="text" name="relatecolumnList[${relatevs.index}].joincolumn" value="${relatecolumn.joincolumn}" ${'1'==relatecolumn.relateType?'readonly':''} ${'3'==relatecolumn.relateType?'readonly':''}/>
								</td>
								<td>
									<input type="text" name="relatecolumnList[${relatevs.index}].inverseJoincolumn" value="${relatecolumn.inverseJoincolumn}" maxlength="200" class="required" style="width:100px;" ${'1'==relatecolumn.relateType?'readonly':''} ${'2'==relatecolumn.relateType?'readonly':''} ${'3'==relatecolumn.relateType?'readonly':''}/>
								</td>
								<td>
									<select name="relatecolumnList[${relatevs.index}].relateOp" class="required input-mini" style="width:95px;*width:85px">
										<c:forEach items="${fns:getDictList('sys_gen_relate_op')}" var="dict">
											<option value="${dict.value}" ${dict.value==relatecolumn.relateOp?'selected':''} title="${dict.description}">${dict.label}</option>
										</c:forEach>
									</select>
								</td>
								<td>
									<input type="button" name="del${relatevs.index}" value="删除" onclick="deltr(this)" class="btn btn-primary" style="width:50px;"/>
								</td>
							</tr>
					</c:forEach>
				</tbody>
			</table>
			</div>
		</div>
		</div>
		<c:if test="${genScene.sceneType == '2'||genScene.sceneType == '4'}">
			<div id="childpage"  style="display:none">
				<fieldset>
					<div class="control-group">
							<label>从表类名：</label>
							<input id="subclassName" name="subclassName"  value="${genScene.subclassName}"  type="text" maxlength="200"  placeholder="class_name"/>
							<label>页记录数：</label>
							<input id="subpageSize" name="subpageSize"  value="${genScene.subpageSize}"  type='text' maxlength="200"  placeholder="10"/>
					</div>
				</fieldset>
				<div class="row-fluid">
			<div class="span12">
				<table class="table table-hover table-bordered" id="chltbl">
					<thead>
						<tr>
							<th>
								编号
							</th>
							<th>
								 字段名
							</th>
							<th>
								数据类型
							</th>
							<th>
								字段名称
							</th>
							<th>
								JAVA字段
							</th>
							<th>
								级联关系
							</th>
							<th>
								级联实体
							</th>
							<th>
								级联操作
							</th>						
							<th>
								Entity显示
							</th>
							<th>
								可为空
							</th>
							<th>
								查询条件
							</th>
							<th>
								模糊查询
							</th>
							<th>
								列表显示
							</th>
							<c:if test="${genScene.sceneType == '4'}">
							<th>
								可修改
							</th>
							</c:if>
							<c:if test="${genScene.sceneType == '2'}">
							<th>
								表单显示
							</th>
							</c:if>
							<th>
								显示控件
							</th>	
							<th>
								字典名
							</th>																	
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${genScene.subColumnList}" var="subcolumn" varStatus="subvs">
							<tr${subcolumn.delFlag eq '1'?' class="error" title="已删除的列，保存之后消失！"':''}>
								<td nowrap>
									${subvs.index + 1}
										<input type="hidden" name="subColumnList[${subvs.index}].id" value="${subcolumn.id}"/>
								</td>
								<td>
									${subcolumn.name}
									<input type="hidden" name="subColumnList[${subvs.index}].name" value="${subcolumn.name}"/>
								</td>
								<td>
									${subcolumn.jdbcType}
									<input type="hidden" name="subColumnList[${subvs.index}].jdbcType" value="${subcolumn.jdbcType}"/>
									<input type="hidden" name="subColumnList[${subvs.index}].javaType" value="${subcolumn.javaType}"/>
								</td>
								<td>
									<input type="text" name="subColumnList[${subvs.index}].comments" value="${subcolumn.comments}" maxlength="200" class="required" style="width:100px;"/>
								</td>
								<td>
									<input type="text" name="subColumnList[${subvs.index}].javaField" value="${subcolumn.javaField}" maxlength="200" class="required" style="width:100px;"/>
								</td>
								<td>
									<select name="subColumnList[${subvs.index}].isRelate" class="required input-mini" style="width:85px;*width:75px" onchange="relateChange(this)">
										<c:forEach items="${fns:getDictList('sys_gen_is_relate')}" var="dict">
											<option value="${dict.value}" ${dict.value==subcolumn.isRelate?'selected':''} title="${dict.description}">${dict.label}</option>
										</c:forEach>
									</select>
								</td>
								<td>
									<input type="text" name="subColumnList[${subvs.index}].relateEntity" value="${subcolumn.relateEntity}" ${'0'==subcolumn.isRelate?'disabled':''} maxlength="200" class="required" style="width:100px;"/>
								</td>
								<td>
									<select name="subColumnList[${subvs.index}].relateOp" class="required input-mini" style="width:95px;*width:85px" ${'0'==subcolumn.isRelate?'disabled':''}>
										<c:forEach items="${fns:getDictList('sys_gen_relate_op')}" var="dict">
											<option value="${dict.value}" ${dict.value==subcolumn.javaType?'selected':''} title="${dict.description}">${dict.label}</option>
										</c:forEach>
									</select>
								</td>
								<td>
									<input id="sub${subvs.index}isEntityVal" name="subColumnList[${subvs.index}].isEntity"  value="${subcolumn.isEntity}"  type="hidden">
									<input type="checkbox" name="sub${subvs.index}isEntity" value="${subcolumn.isEntity}" ${subcolumn.isEntity eq '1' ? 'checked' : ''}  onchange="smallChange(this)" />
								</td>
								<td>
									<input id="sub${subvs.index}isNullVal" name="subColumnList[${subvs.index}].isNull"  value="${subcolumn.isNull}"  type="hidden">
									<input type="checkbox" name="sub${subvs.index}isNull" value="${subcolumn.isNull}" ${subcolumn.isNull eq '1' ? 'checked' : ''} onchange="valueChange(this)"/>
								</td>
								<td>
									<input id="sub${subvs.index}isQueryVal" name="subColumnList[${subvs.index}].isQuery"  value="${subcolumn.isQuery}"  type="hidden">
									<input type="checkbox" name="sub${subvs.index}isQuery" value="${subcolumn.isQuery}" ${subcolumn.isQuery eq '1' ? 'checked' : ''} onchange="valueChange(this)"/>
								</td>
								<td>
									<input id="sub${subvs.index}queryTypeVal" name="subColumnList[${subvs.index}].queryType"  value="${subcolumn.queryType}"  type="hidden">
									<input type="checkbox" name="sub${subvs.index}queryType" value="${subcolumn.queryType}" ${subcolumn.queryType eq '1' ? 'checked' : ''} onchange="valueChange(this)"/>
								</td>
								<td>
									<input id="sub${subvs.index}isListVal" name="subColumnList[${subvs.index}].isList"  value="${subcolumn.isList}"  type="hidden">
									<input type="checkbox" name="sub${subvs.index}isList" value="${subcolumn.isList}" ${subcolumn.isList eq '1' ? 'checked' : ''} onchange="valueChange(this)"/>
								</td>
								<c:if test="${genScene.sceneType == '4'}">
								<td>
									<input id="sub${subvs.index}isEditVal" name="subColumnList[${subvs.index}].isEdit"  value="${subcolumn.isEdit}"  type="hidden">
									<input type="checkbox" name="sub${subvs.index}isEdit" value="${subcolumn.isEdit}" ${subcolumn.isEdit eq '1' ? 'checked' : ''} onchange="valueChange(this)"/>
								</td>
								</c:if>
								<c:if test="${genScene.sceneType == '2'}">
								<td>
									<input id="sub${subvs.index}isFormVal" name="subColumnList[${subvs.index}].isForm"  value="${subcolumn.isForm}"  type="hidden">
									<input type="checkbox" name="sub${subvs.index}isForm" value="${subcolumn.isForm}" ${subcolumn.isForm eq '1' ? 'checked' : ''} onchange="valueChange(this)"/>
								</td>
								</c:if>
								<td>
									<select name="subColumnList[${subvs.index}].showType" class="required" style="width:100px;" onchange="typeChange(this)">
										<c:forEach items="${fns:getDictList('sys_gen_show_type')}" var="dict">
											<option value="${dict.value}" ${dict.value==subcolumn.showType?'selected':''} title="${dict.description}">${dict.label}</option>
										</c:forEach>
									</select>
								</td>
								<td>
									<input type="text" name="subColumnList[${subvs.index}].dictType" value="${subcolumn.dictType}" maxlength="200" class="input-mini" ${'2'!=column.showType?'disabled':''}/>
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
			
		<div class="row-fluid">
				<div class="span10">
				</div>
				<div class="span2" align="right">
					 <button  onclick="addLineChl()"  class="btn btn-primary" type="button" style="position: right">添加级联</button>
				</div>
		</div>
	    <br>
		<div class="row-fluid">
			<div class="span12">
				<table class="table table-hover table-bordered">
				<thead>
					<tr>
						<th>
							级联关系
						</th>
						<th>
							级联实体
						</th>
						<th>
							JoinColumn
						</th>
						<th>
							mappedBy
						</th>
						<th>
						    joinTable
						</th>
						<th>
							joinColumn
						</th>
						<th>
							inverseJoinColumn
						</th>
						<th>
							级联操作
						</th>
						<th>
						</th>
					</tr>
				</thead>
				<tbody id="jlczchl">
					<c:forEach items="${genScene.subRelatecolumnList}" var="subrelatecolumn" varStatus="subrelatevs">
						<tr${subrelatecolumn.delFlag eq '1'?' class="error" title="已删除的列，保存之后消失！"':''}>
								<td>
									<select name="subRelatecolumnList[${subrelatevs.index}].relateType" class="required input-mini" style="width:185px;*width:175px" onchange="relTypeChange(this)">
										<c:forEach items="${fns:getDictList('sys_gen_relate')}" var="dict">
											<option value="${dict.value}" ${dict.value==subrelatecolumn.relateType?'selected':''} title="${dict.description}">${dict.label}</option>
										</c:forEach>
									</select>
										<input type="hidden" name="subRelatecolumnList[${subrelatevs.index}].id" value="${subrelatecolumn.id}"/>
								</td>
								<td>
									<input type="text" name="subRelatecolumnList[${subrelatevs.index}].relateEntity" value="${subrelatecolumn.relateEntity}"/>
								</td>
								<td>
									<input type="text" name="subRelatecolumnList[${subrelatevs.index}].mappedby" value="${subrelatecolumn.mappedby}" ${'4'==subrelatecolumn.relateType?'disabled':''} ${'5'==subrelatecolumn.relateType?'disabled':''}/>
								</td>
								<td>
									<input type="text" name="subRelatecolumnList[${subrelatevs.index}].jointable" value="${subrelatecolumn.jointable}" ${'2'==subrelatecolumn.relateType?'disabled':''} ${'4'==subrelatecolumn.relateType?'disabled':''} ${'5'==subrelatecolumn.relateType?'disabled':''}/>
								</td>
								<td>
									<input type="text" name="subRelatecolumnList[${subrelatevs.index}].joincolumn" value="${subrelatecolumn.joincolumn}" ${'1'==subrelatecolumn.relateType?'disabled':''} ${'3'==subrelatecolumn.relateType?'disabled':''}/>
								</td>
								<td>
									<input type="text" name="subRelatecolumnList[${subrelatevs.index}].inverseJoincolumn" value="${subrelatecolumn.inverseJoincolumn}" maxlength="200" class="required" style="width:100px;" ${'1'==subrelatecolumn.relateType?'disabled':''} ${'2'==subrelatecolumn.relateType?'disabled':''} ${'3'==subrelatecolumn.relateType?'disabled':''}/>
								</td>
								<td>
									<select name="subRelatecolumnList[${subrelatevs.index}].relateOp" class="required input-mini" style="width:95px;*width:85px">
										<c:forEach items="${fns:getDictList('sys_gen_relate_op')}" var="dict">
											<option value="${dict.value}" ${dict.value==subrelatecolumn.relateOp?'selected':''} title="${dict.description}">${dict.label}</option>
										</c:forEach>
									</select>
								</td>
								<td>
									<input type="button" name="subdel${subrelatevs.index}" value="删除" onclick="deltr(this)" class="btn btn-primary" style="width:50px;"/>
								</td>
							</tr>
					</c:forEach>
				</tbody>
			</table>
			</div>
		</div>
		</div>
		</c:if>
		<div class="row-fluid">
			<div class="span4">
				</div>
				<div class="span4" align="center">
					<shiro:hasPermission name="generator:genScene:edit">
						<input id="btnSubmit" class="btn btn-primary" type="submit" value="保存"/>&nbsp;
					</shiro:hasPermission>
				</div>
				<div class="span4">
			</div>
		</div>
	</div>
</div>
			</form:form>
</body>
</html>
