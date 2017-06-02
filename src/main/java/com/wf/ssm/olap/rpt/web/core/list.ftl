<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%
	String sbuffer2=request.getAttribute("sbuffer").toString();
%>
<html>
<head>
	<title>${rptCm.cmName}管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			if('${r"${bz}"}'=="1"){
				$("#fordown").css("display","block");
			}
		});
		function downLoadExcel(){
			$("#searchForm").attr("action",'${r"${ctx}"}/rpt/core/rptShow/expExl');
			$("#searchForm").submit();
		}
		function downLoadPdf(){
			$("#searchForm").attr("action",'${r"${ctx}"}/rpt/core/rptShow/expPdf');
			$("#searchForm").submit();
		}
		function downLoadWord(){
			$("#searchForm").attr("action",'${r"${ctx}"}/rpt/core/rptShow/expWord');
			$("#searchForm").submit();
		}
		function forQuery(){
			$("#searchForm").attr("action",'${r"${ctx}"}/rpt/core/rptShow/rptShow');
			$("#searchForm").submit();
		}
		function doSelOrg(){
			//查询LIST
        	var popUrl='${r"${ctx}"}/sc/nm/office/childPoplist';//弹出组织机构选择页面URL
        	//弹出PopUP
    		top.$.jBox.open("iframe:"+popUrl, "选择组织机构", 900, $(top.document).height()-180, {
                buttons:{"确定":"ok", "关闭":true}, submit:function(v, h, f){
                    if (v=="ok"){
                    	//获得弹出页面隐藏域值
	                    var selIds = h.find("iframe")[0].contentWindow.$("#selItemVals").val();
	                    var selNames = h.find("iframe")[0].contentWindow.$("#selItemNames").val();
	                    //本页面赋值
				        $("#office").val(selIds);
				        $("#officeName").val(selNames);
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
		<li class="active"><a>${rptCm.cmName}</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="rptCm" action='${r"${ctx}"}/rpt/core/rptShow/rptShow' method="post" class="breadcrumb form-search">
	    <form:hidden id="rptType" path="rptType"/>
	    <div>
		<#list rptCm.rptQueryconditionList as c>
		   <#if c.isSystem == "0">
			 <label>${c.showName}：</label>
			<#if c.showType == "0">
				<input id="${c.showId}" name="${c.showId}" htmlEscape="false" class="input-medium" value='${r"${"}${c.showId}${r"}"}'/>
			<#elseif c.showType == "1">
				<select id="${c.showId}" name="${c.showId}" class="input-medium" value='${r"${"}${c.showId}${r"}"}'>
					<option value="" label=""/>
					<c:forEach items="${r'${'}fns:getDictList('${c.showDict}')}" var="dict">
						<option value='${r"${dict.value}"}' ${r"${dict.value==c.showId?'selected':''}"}>${r"${dict.label}"}</option>
					</c:forEach>
				</select>
			<#elseif c.showType == "2">
				<input id="${c.showId}" name="${c.showId}" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
				    value='${r"${"}${c.showId}${r"}"}' onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
			<#elseif c.showType == "3">
				<input id="${c.showId}" name="${c.showId}" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
				    value='${r"${"}${c.showId}${r"}"}' onclick="WdatePicker({dateFmt:'yyyy-MM',isShowClear:false});"/>
		    <#elseif c.showType == "4">
				<input id="${c.showId}" name="${c.showId}" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
				    value='${r"${"}${c.showId}${r"}"}' onclick="WdatePicker({dateFmt:'yyyy',isShowClear:false});"/>
			<#elseif c.showType == "5">
			    <input id="office" name="office" type="hidden" value='${r"${officeObj.id}"}'/>
				<input id="officeName" name="officeName" type="text" htmlEscape="false" maxlength="50" value='${r"${officeObj.name}"}' readonly class="input-medium" onclick="doSelOrg()"/>
				<a  onclick="doSelOrg()" class="btn"><i class="icon-search"></i></a>
			</#if>
			</#if>
		</#list>
			<input id="btnSubmit" class="btn btn-primary" type="button" onclick="forQuery()" value="查询"/></div>
			<div align="right" style="display:none" id="fordown">
				<input id="btnSubmit" class="btn btn-primary" type="button"  onclick="downLoadPdf()" value="导出PDF"/>
		  		<input id="btnSubmit" class="btn btn-primary" type="button"  onclick="downLoadExcel()" value="导出EXCEL"/>
		  		<input id="btnSubmit" class="btn btn-primary" type="button"  onclick="downLoadWord()" value="导出Word"/>
		  	</div>
	</form:form>
	<tags:message content="${r"${message}"}"/>
	<table>
	<tr>
	  <td width="50%">&nbsp;</td>
	  <td align="center" id="viewcontent">
		<%=sbuffer2.toString()%>
	  </td>
	  <td width="50%">&nbsp;</td>
	</tr>
	</table>
</body>
</html>