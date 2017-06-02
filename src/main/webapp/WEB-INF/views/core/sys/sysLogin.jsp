<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
	
<html>
<head>
	<title>${fns:getConfig('productName')} 登录</title>
	<meta name="decorator" content="default"/>

    <link rel="stylesheet" href="${ctxStatic}/common/typica-login.css">
	<style type="text/css">
		.control-group{border-bottom:0px;}
	</style>
    <script src="${ctxStatic}/common/backstretch.min.js"></script>
   
	<script type="text/javascript">
		$(document).ready(function() {
			//
			if(navigator.userAgent.indexOf("MSIE")  != -1){//IE浏览器
			//	alert("您正在使用‘IE’浏览器！为了您更好的用户体验，建议您使用Chrome或FireFox浏览器");
			}
			//
			$.backstretch([
 		      "${ctxStatic}/images/lnint_bg_2.jpg", 
 		      "${ctxStatic}/images/lnint_bg_1.jpg",
 		      "${ctxStatic}/images/lnint_bg_3.jpg"
 		  	], {duration: 4800, fade: 2000});
			$("#loginForm").validate({
				rules: {
					validateCode: {remote: "${pageContext.request.contextPath}/servlet/validateCodeServlet"}
				},
				messages: {
					username: {required: "请填写用户名."},password: {required: "请填写密码."},
					validateCode: {remote: "验证码不正确.", required: "请填写验证码."}
				},
				errorLabelContainer: "#messageBox",
				errorPlacement: function(error, element) {
					error.appendTo($("#loginError").parent());
				} 
			});
		});
		// 如果在框架中，则跳转刷新上级页面
		if(self.frameElement && self.frameElement.tagName=="IFRAME"){
			parent.location.reload();
		}
	</script>
	<script type="text/javascript">
       function openSearch()
       {
		 window.open("http://www.baidu.com/s?word="+$("#openSearch1").val() );
       }
	</script>
</head>
<body>

    <div class="navbar navbar-fixed-top">
      <div class="navbar-inner">
        <div class="container">
          <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </a>
          <a class="brand" href="${ctx}"><img src="${ctxStatic}/images/logo.png" alt="lnoa" style="height:40px;"></a>
        </div>
        <!-- IE浏览器提示 -->
        <!--[if  IE]>
		<div class='alert alert-block' style="text-align:left;padding-bottom:5px;">
		<a class="close" data-dismiss="alert">x</a>
		<p>温馨提示：您正在使用IE浏览器(内核)。为了您获得更好的浏览体验和速度，我们强烈建议您使用较新版本的<a href="http://www.google.cn/intl/zh-CN/chrome/" target="_blank">谷歌浏览器</a>、
		 <a href="http://www.firefox.com.cn/download/" target="_blank">火狐浏览器</a>、
		 <a href="http://chrome.360.cn/" target="_blank">360浏览器(极速版)</a>等
		 </p></div>
		 <![endif]-->
      </div>
    </div>

    <div class="container">
		<!--[if lte IE 7]><br/><div class='alert alert-block' style="text-align:left;padding-bottom:10px;"><a class="close" data-dismiss="alert">x</a><h4>温馨提示：</h4><p>你使用的浏览器版本过低。为了获得更好的浏览体验，我们强烈建议您 <a href="http://browsehappy.com" target="_blank">升级</a> 到最新版本的IE浏览器，或者使用较新版本的 Chrome、Firefox、Safari 等。</p></div><![endif]-->
		
		<%String error = (String) request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);%>
		
		<div id="messageBox" class="alert alert-error <%=error==null?"hide":""%>"><button data-dismiss="alert" class="close">×</button>
			<label id="loginError" class="error"><%=error==null?"":"com.lnint.jess.core.sys.security.CaptchaException".equals(error)?"验证码错误, 请重试.":"用户或密码错误, 请重试." %></label>
		</div>
		
        <div id="login-wraper">
            <form id="loginForm"  class="form login-form" action="${ctx}/login" method="post">
                <legend><span style="color: #18618A;font-size: 25px;">帐号登录</span></legend>
                <div class="body">
					<div class="control-group">
						<div class="controls">
							<input type="text" id="username" name="username" value="admin" class="required" value="${username}" placeholder="登录名">
						</div>
					</div>
					
					<div class="control-group">
						<div class="controls">
							<input type="password" id="password" value="admin123" name="password" class="required" placeholder="密码"/>
						</div>
					</div>
					<c:if test="${isValidateCodeLogin}"><div class="validateCode">
						<label for="password">密　码：</label>
						<tags:validateCode name="validateCode" inputCssStyle="margin-bottom:0;"/>
					</div></c:if>
                </div>
                <div class="footer">
                    <label class="checkbox inline">
                        <input type="checkbox" id="rememberMe" name="rememberMe"> <span style="color:#08c;">记住我</span>
                    </label>
                    <input class="btn btn-primary" type="submit" value="登 录"/>
                </div>
				<div id="themeSwitch" class="dropdown pull-right">
<%-- 					<a class="dropdown-toggle" data-toggle="dropdown" href="#">${fns:getDictLabel(cookie.theme.value,'theme','默认主题')}<b class="caret"></b></a> --%>
					<ul class="dropdown-menu">
					  <c:forEach items="${fns:getDictList('theme')}" var="dict"><li><a href="#" onclick="location='${pageContext.request.contextPath}/theme/${dict.value}?url='+location.href">${dict.label}</a></li></c:forEach>
					</ul>
				</div>
            </form>
        </div>
    </div>
    <footer class="white navbar-fixed-bottom">
		
    </footer>
    

  </body>
</html>