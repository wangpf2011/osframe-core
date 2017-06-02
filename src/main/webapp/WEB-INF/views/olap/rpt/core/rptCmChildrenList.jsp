<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>子报表模板管理</title>
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
		//打开上传共通弹出页面
		function doUpLoadFile(){
			//form上传的URL 业务地址-自定义修改
           	var upUrl='${ctx}/rpt/core/rptCmChildren/regupload';
           	
           	//弹出PopUP
    		top.$.jBox.open("iframe:${ctx}/pc/consInfo/uploadPopup?upUrl="+upUrl, "上传模板文件", 500, $(top.document).height()-400, {
                buttons:{"确定":"ok", "关闭":true}, submit:function(v, h, f){
                    if (v=="ok"){
                    	//获得弹出页面隐藏域值
	                    var fileName= h.find("iframe")[0].contentWindow.$("#fileName").val();
	                    var filePath= h.find("iframe")[0].contentWindow.$("#filePath").val();
	                    
	                    $("#searchForm").attr("action", "${ctx}/rpt/core/rptCmChildren/saveSingle?fileName="+fileName+"&filePath="+filePath);
        		    	$("#searchForm").submit();
                    }else if (v=="clear"){
                    }
                }, loaded:function(h){
                    $(".jbox-content", top.document).css("overflow-y","hidden");
                }
            });
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
	    <li><a href="${ctx}/rpt/core/rptCm/">报表模板列表</a></li>
		<li class="active"><a href="${ctx}/rpt/core/rptCmChildren/list?cmId=${cmId}">子报表模板维护列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="rptCmChildren" action="${ctx}/rpt/core/rptCmChildren/" method="post" class="breadcrumb form-search">
		<form:hidden path="cmId"/>
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
			<input id="btnSubmit" class="btn btn-primary" type="button" value="上传" onclick="doUpLoadFile()"/></div>
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>引用文件</th>
				<th>模板文件</th>
				<shiro:hasPermission name="rpt:core:rptCmChildren:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="rptCmChildren">
			<tr>
				<td><a href="${ctx}/rpt/core/rptCmChildren/form?id=${rptCmChildren.id}">
					${rptCmChildren.fileName}
				</a></td>
				<td>
					${rptCmChildren.filePath}
				</td>
				<shiro:hasPermission name="rpt:core:rptCmChildren:edit"><td>
					<a href="${ctx}/rpt/core/rptCmChildren/delete?id=${rptCmChildren.id}&cmId=${cmId}" onclick="return confirmx('确认要删除该子报表模板吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>