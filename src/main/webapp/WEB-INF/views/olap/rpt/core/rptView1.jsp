<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%
	//String sbuffer2=request.getAttribute("sbuffer").toString();
%>
<html>
<head>
	<title>报表预览</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			
		});
		function downLoadExcel(){
			window.location='${ctx}/rpt/view/expExl';
		}
		function downLoadPdf(){
			window.location='${ctx}/rpt/view/expPdf';
		}
		function downLoadWord(){
			window.location='${ctx}/rpt/view/expWord';
		}
</script>
</head>
<body>
<ul class="nav nav-tabs">
		<li class="active"><a>报表预览</a></li>
	
	</ul>
<table>
<tr>
<td colspan="3" align="right">
	    <input id="btnSubmit" class="btn btn-primary" type="button"  onclick="downLoadPdf()" value="导出PDF"/>
  		<input id="btnSubmit" class="btn btn-primary" type="button"  onclick="downLoadExcel()" value="导出EXCEL"/>
  		<input id="btnSubmit" class="btn btn-primary" type="button"  onclick="downLoadWord()" value="导出Word"/>
 </td>
</tr>
<tr>
<td>
 <iframe src="1.html" width="450" height="400" frameborder="0" scrolling="no">
 </iframe>
</td>
</tr>
</table>
	
</body>
</html>
