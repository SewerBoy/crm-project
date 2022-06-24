<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
	<base href="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/">
<meta charset="UTF-8">
<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript">
		$(function () {
			//给浏览器窗口添加键盘按下事件
			$(window).keydown(function(e) {
				//alert(e.keyCode);
				if(e.keyCode == 13) {
					$("#loginBtn").click();
				}
			})

			//给‘登录’按钮添加单击事件
			$("#loginBtn").click(function () {
				var loginAct = $.trim($("#loginAct").val());
				var loginPwd = $.trim($("#loginPwd").val());
				var isRemPwd = $.trim($("#isRemPwd").prop("checked"));
				//判断参数是否合法
				if(loginAct == "") {
					alert("您还未输入账号,请输入!");
					return;
				}
				if(loginPwd == "") {
					alert("您还未输入密码,请输入!");
					return;
				}

				//发送异步请求

				$.ajax({
					url:'settings/qx/user/login.do',
					data:{
						loginAct:loginAct,
						loginPwd:loginPwd,
						isRemPwd:isRemPwd
					},
					type:'post',
					dataType:'json',
					success:function(data) {
						if(data.code == 1){
							//跳转到业务主页面
							window.location.href=('workbench/index.do');
						}else {
							$("#msg").html(data.message);
						}
					},
					beforeSend:function() {
						/*当发送ajax前,会自动执行本函数
						* 此函数的返回结果可以影响ajax执行
						* 若结果为true则,执行ajax,若结果为false,则不执行ajax*/
						$("#msg").text("loding...");
						return true;
					}

						}
				)
			})
		})
</script>
</head>
<body>
	<div style="position: absolute; top: 0px; left: 0px; width: 60%;">
		<img src="image/IMG_7114.JPG" style="width: 100%; height: 90%; position: relative; top: 50px;">
	</div>
	<div id="top" style="height: 50px; background-color: #3C3C3C; width: 100%;">
		<div style="position: absolute; top: 5px; left: 0px; font-size: 30px; font-weight: 400; color: white; font-family: 'times new roman'">CRM &nbsp;<span style="font-size: 12px;">&copy;2019&nbsp;动力节点</span></div>
	</div>
	
	<div style="position: absolute; top: 120px; right: 100px;width:450px;height:400px;border:1px solid #D5D5D5">
		<div style="position: absolute; top: 0px; right: 60px;">
			<div class="page-header">
				<h1>登录</h1>
			</div>
			<form action="workbench/index.html" class="form-horizontal" role="form">
				<div class="form-group form-group-lg">
					<div style="width: 350px;">
						<input id="loginAct" class="form-control" type="text" value="${cookie.loginAct.value}" placeholder="用户名">
					</div>
					<div style="width: 350px; position: relative;top: 20px;">
						<input id="loginPwd" class="form-control" type="password" value="${cookie.loginPwd.value}" placeholder="密码">
					</div>
					<div class="checkbox"  style="position: relative;top: 30px; left: 10px;">
							<label>
								<c:if test="${not empty cookie.loginAct.value and not empty cookie.loginPwd.value}">
									<input id="isRemPwd" type="checkbox" checked> 十天内免登录
								</c:if>
								<c:if test="${empty cookie.loginAct.value or empty cookie.loginPwd.value}">
									<input id="isRemPwd" type="checkbox"> 十天内免登录
								</c:if>
							</label>
						&nbsp;&nbsp;
						<span id="msg"></span>
					</div>
					<button type="button" id="loginBtn" class="btn btn-primary btn-lg btn-block"  style="width: 350px; position: relative;top: 45px;">登录</button>
				</div>
			</form>
		</div>
	</div>
</body>
</html>