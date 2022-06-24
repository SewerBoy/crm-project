<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
	<base href="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/">
	<meta charset="UTF-8">

<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script><
	<script type="text/javascript" src="jquery/bs_typeahead/bootstrap3-typeahead.min.js"></script>
	<script type="text/javascript">
		$(function () {
			//给单击市场活动源图标条件单击事件
			$("#searchActivity").click(function() {
				//弹出查找市场活动模态窗口
				$("#findMarketActivity").modal("show");
			});

			//给市场互动搜索框添加键盘弹起事件
			$("#activityInput").keyup(function() {
				//收集参数
				var name = this.value;
				//发送ajax请求
				$.ajax({
					url:'workbench/transaction/queryActivityByVagueName.do',
					data:{
						name:name
					},
					type:'post',
					dataType:'json',
					success:function(data) {
						//渲染页面
						var htmlStr = "";
						$.each(data, function(index, obj) {
							htmlStr += "<tr>";
							htmlStr += "<td><input type=\"radio\" value=\""+obj.id+"\" class=\"activityC\" activityName=\""+obj.name+"\" name=\"activity\"/></td>";
							htmlStr += "<td>"+obj.name+"</td>";
							htmlStr += "<td>"+obj.startDate+"</td>";
							htmlStr += "<td>"+obj.endDate+"</td>";
							htmlStr += "<td>"+obj.owner+"</td>";
							htmlStr += "</tr>";
						})
						$("#tBody").html(htmlStr);

					}

				});
			});

			//给上面单选按钮添加单击事件
			$("#tBody").on("click", "input[type='radio']", function() {
				//获取市场活动的id，和name
				var id = this.value;
				var name = $(this).attr("activityName");
				//关闭模态窗口
				$("#findMarketActivity").modal("hide");

				//给市场活动源赋值
				$("#create-activitySrc").val(name);
				$("#activityId").val(id);

			});

			//给联系人名称查找按钮添加单击事件
			$("#contactName").click(function() {
				//弹出查找联系人模态窗口
				$("#findContacts").modal("show");
			});

			//给查找联系人搜索框添加键盘弹起事件
			$("#contactsInput").keyup(function() {
				//收集参数
				var fullname = this.value;
				//发送ajax请求
				$.ajax({
					url:'workbench/transaction/queryContactsByVagueName.do',
					data:{
						fullname:fullname
					},
					type:'post',
					dataType:'json',
					success:function(data) {
						var htmlStr = "";
						//遍历联系人
						$.each(data, function(index, obj) {
							htmlStr += "<tr>";
							htmlStr += "<td><input type=\"radio\" value=\""+obj.id+"\" contactName=\""+obj.fullname+"\" name=\"activity\"/></td>";
							// htmlStr += "<td><input type=\"radio\" value=\""+obj.id+"\" activityName=\""+obj.name+"\" name=\"activity\"/></td>";
							htmlStr += "<td>"+obj.fullname+"</td>";
							htmlStr += "<td>"+obj.email+"</td>";
							htmlStr += "<td>"+obj.mphone+"</td>";
							htmlStr += "</tr>";
						});
						//渲染页面
						$("#contactBody").html(htmlStr);
					}
				});

			});

			//给上面单选按钮添加单击事件
			$("#contactBody").on("click", "input[type='radio']", function() {
				//收集参数
				var fullname = $(this).attr("contactName");
				var id = this.value;

				//关闭模态窗口
				$("#findContacts").modal("hide");

				//给联系人名称文本框和隐藏域赋值
				$("#create-contactsName").val(fullname);
				$("#contactId").val(id);

			})

			//给阶段多选框加入change事件
			$("#create-transactionStage").change(function () {
				//收集参数
				// var stageValue = $("#create-transactionStage").val();此功能跟下面一样
				var stageValue = $(this).find("option:selected").text();

				//判断stageValue是否为空
				if(stageValue == "") {
					//为可能性赋值
					$("#create-possibility").val("");
					return;
				}
				//发送ajax请求
				$.ajax({
					url:'workbench/transaction/getPossibility.do',
					data:{
						stageValue:stageValue
					},
					type:'post',
					dataType:'json',
					success:function(data) {
						alert(data);
						//为可能性赋值
						$("#create-possibility").val(data);
					}
				});
			});

			//给客户名称添加键盘弹起事件,并模糊查询公司
			$("#create-accountName").typeahead({
				source:function(jquery,process) {
					//jquery:为文本输入框内容,process:此函数用于显示数据,放进去即可
					//发送ajax请求
					$.ajax({
						url:'workbench/transaction/queryCustomerByName.do',
						data:{
							name:jquery
						},
						type:'post',
						dataType:'json',
						success:function(data) {
							//调用process函数显示数据
							process(data);
						}
					});
				}
			}
			);

			//给"预计成交日期"添加日期选项框
			$("#create-expectedClosingDate").datetimepicker({
				language:'zh-CN',
				format:'yyyy-mm-dd',
				minView:'moth',
				initialDate:new Date(),
				autoclose:true,
				todayBtn:true,
				clearBtn:true
			});

			//给"保存"按钮添加单击事件
			$("#saveCreateTransaction").click(function() {
				//收集参数
				var money = $("#create-amountOfMoney").val();
				var name = $("#create-transactionName").val();
				var owner = $("#create-transactionOwner").val();
				var expectedDate = $("#create-expectedClosingDate").val();
				var stage = $("#create-transactionStage").val();
				var type = $("#create-transactionType").val();
				var source = $("#create-clueSource").val();
				var activityId = $("#create-activityId").val();
				var contactsId = $("#contactId").val();
				var description = $("#create-describe").val();
				var contactSummary = $("#create-contactSummary").val();
				var nextContactTime = $("#create-nextContactTime").val();
				//发送ajax请求
				$.ajax({
					url:'workbench/transaction/saveCreateTransaction.do',
					data: {
						money: money,
						name: name,
						owner: owner,
						expectedDate: expectedDate,
						stage: stage,
						type: type,
						source: source,
						activityId: activityId,
						contactsId: contactsId,
						description: description,
						contactSummary: contactSummary,
						nextContactTime: nextContactTime
					},
					type:'post',
					dataType:'json',
					success:function(data) {
						if(data.code == "1") {
							//跳转主页面
							window.location.href='workbench/transaction/index.do'
						}else {
							alert(data.message);
						}
					}
				});

			});
		});
	</script>
</head>
<body>

	<!-- 查找市场活动 -->	
	<div class="modal fade" id="findMarketActivity" role="dialog">
		<div class="modal-dialog" role="document" style="width: 80%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title">查找市场活动</h4>
				</div>
				<div class="modal-body">
					<div class="btn-group" style="position: relative; top: 18%; left: 8px;">
						<form class="form-inline" role="form">
						  <div class="form-group has-feedback">
						    <input type="text" class="form-control" id="activityInput" style="width: 300px;" placeholder="请输入市场活动名称，支持模糊查询">
						    <span class="glyphicon glyphicon-search form-control-feedback"></span>
						  </div>
						</form>
					</div>
					<table id="activityTable3" class="table table-hover" style="width: 900px; position: relative;top: 10px;">
						<thead>
							<tr style="color: #B3B3B3;">
								<td></td>
								<td>名称</td>
								<td>开始日期</td>
								<td>结束日期</td>
								<td>所有者</td>
							</tr>
						</thead>
						<tbody id="tBody">
							<%--<tr>
								<td><input type="radio" name="activity"/></td>
								<td>发传单</td>
								<td>2020-10-10</td>
								<td>2020-10-20</td>
								<td>zhangsan</td>
							</tr>
							<tr>
								<td><input type="radio" name="activity"/></td>
								<td>发传单</td>
								<td>2020-10-10</td>
								<td>2020-10-20</td>
								<td>zhangsan</td>
							</tr>--%>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>

	<!-- 查找联系人 -->	
	<div class="modal fade" id="findContacts" role="dialog">
		<div class="modal-dialog" role="document" style="width: 80%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title">查找联系人</h4>
				</div>
				<div class="modal-body">
					<div class="btn-group" style="position: relative; top: 18%; left: 8px;">
						<form class="form-inline" role="form">
						  <div class="form-group has-feedback">
						    <input type="text" class="form-control" id="contactsInput" style="width: 300px;" placeholder="请输入联系人名称，支持模糊查询">
						    <span class="glyphicon glyphicon-search form-control-feedback"></span>
						  </div>
						</form>
					</div>
					<table id="activityTable" class="table table-hover" style="width: 900px; position: relative;top: 10px;">
						<thead>
							<tr style="color: #B3B3B3;">
								<td></td>
								<td>名称</td>
								<td>邮箱</td>
								<td>手机</td>
							</tr>
						</thead>
						<tbody id="contactBody">
							<%--<tr>
								<td><input type="radio" name="activity"/></td>
								<td>李四</td>
								<td>lisi@bjpowernode.com</td>
								<td>12345678901</td>
							</tr>
							<tr>
								<td><input type="radio" name="activity"/></td>
								<td>李四</td>
								<td>lisi@bjpowernode.com</td>
								<td>12345678901</td>
							</tr>--%>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
	
	
	<div style="position:  relative; left: 30px;">
		<h3>创建交易</h3>
	  	<div style="position: relative; top: -40px; left: 70%;">
			<button type="button" class="btn btn-primary" id="saveCreateTransaction">保存</button>
			<button type="button" class="btn btn-default">取消</button>
		</div>
		<hr style="position: relative; top: -40px;">
	</div>
	<form class="form-horizontal" role="form" style="position: relative; top: -30px;">
		<div class="form-group">
			<label for="create-transactionOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
				<select class="form-control" id="create-transactionOwner">
					<c:forEach items="${userList}" var="user">
						<option value="${user.id}">${user.name}</option>
					</c:forEach>
				 <%-- <option>zhangsan</option>
				  <option>lisi</option>
				  <option>wangwu</option>--%>
				</select>
			</div>
			<label for="create-amountOfMoney" class="col-sm-2 control-label">金额</label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="create-amountOfMoney">
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-transactionName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="create-transactionName">
			</div>
			<label for="create-expectedClosingDate" class="col-sm-2 control-label">预计成交日期<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="create-expectedClosingDate">
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-accountName" class="col-sm-2 control-label">客户名称<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="create-accountName" placeholder="支持自动补全，输入客户不存在则新建">
			</div>
			<label for="create-transactionStage" class="col-sm-2 control-label">阶段<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
			  <select class="form-control" id="create-transactionStage">
			  	<option></option>
				  <c:forEach items="${stageList}" var="stage">
					  <option value="${stage.id}">${stage.value}</option>
				  </c:forEach>
			  	<%--<option>资质审查</option>
			  	<option>需求分析</option>
			  	<option>价值建议</option>
			  	<option>确定决策者</option>
			  	<option>提案/报价</option>
			  	<option>谈判/复审</option>
			  	<option>成交</option>
			  	<option>丢失的线索</option>
			  	<option>因竞争丢失关闭</option>--%>
			  </select>
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-transactionType" class="col-sm-2 control-label">类型</label>
			<div class="col-sm-10" style="width: 300px;">
				<select class="form-control" id="create-transactionType">
				  <option></option>
					<c:forEach items="${transactionTypeList}" var="transaction">
						<option value="${transaction.id}">${transaction.value}</option>
					</c:forEach>
				  <%--<option>已有业务</option>
				  <option>新业务</option>--%>
				</select>
			</div>
			<label for="create-possibility" class="col-sm-2 control-label">可能性</label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="create-possibility" readonly>
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-clueSource" class="col-sm-2 control-label">来源</label>
			<div class="col-sm-10" style="width: 300px;">
				<select class="form-control" id="create-clueSource">
				  <option></option>
					<c:forEach items="${sourceList}" var="source">
						<option value="${source.id}">${source.value}</option>
					</c:forEach>
				 <%-- <option>广告</option>
				  <option>推销电话</option>
				  <option>员工介绍</option>
				  <option>外部介绍</option>
				  <option>在线商场</option>
				  <option>合作伙伴</option>
				  <option>公开媒介</option>
				  <option>销售邮件</option>
				  <option>合作伙伴研讨会</option>
				  <option>内部研讨会</option>
				  <option>交易会</option>
				  <option>web下载</option>
				  <option>web调研</option>
				  <option>聊天</option>--%>
				</select>
			</div>
			<label for="create-activitySrc" class="col-sm-2 control-label">市场活动源&nbsp;&nbsp;<a href="javascript:void(0);" id="searchActivity"><span class="glyphicon glyphicon-search"></span></a></label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="hidden" id="activityId">
				<input type="text" class="form-control" id="create-activitySrc">
			</div>
		</div>
		
		<div class="form-group">
			<%--隐藏域--%>
			<input type="hidden" id="contactId">
			<label for="create-contactsName" class="col-sm-2 control-label">联系人名称&nbsp;&nbsp;<a href="javascript:void(0);" id="contactName"><span class="glyphicon glyphicon-search"></span></a></label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="create-contactsName">
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-describe" class="col-sm-2 control-label">描述</label>
			<div class="col-sm-10" style="width: 70%;">
				<textarea class="form-control" rows="3" id="create-describe"></textarea>
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-contactSummary" class="col-sm-2 control-label">联系纪要</label>
			<div class="col-sm-10" style="width: 70%;">
				<textarea class="form-control" rows="3" id="create-contactSummary"></textarea>
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-nextContactTime" class="col-sm-2 control-label">下次联系时间</label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="create-nextContactTime">
			</div>
		</div>
		
	</form>
</body>
</html>