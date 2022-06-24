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
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>
<!--  PAGINATION plugin -->
<link rel="stylesheet" type="text/css" href="jquery/bs_pagination-master/css/jquery.bs_pagination.min.css">
<script type="text/javascript" src="jquery/bs_pagination-master/js/jquery.bs_pagination.min.js"></script>
<script type="text/javascript" src="jquery/bs_pagination-master/localization/en.js"></script>
<script type="text/javascript">

	$(function(){
		//点击"创建"按钮绑定单击事件
		$("#createActivityBtn").click(function() {
			//初始化工作,初始化表单(先转为dom对象)
			$("#createActivityForm").get(0).reset();

			//弹出创建市场活动的模态窗口
			$("#createActivityModal").modal("show");

		});

		//给"保存"按钮绑定单击事件
		$("#saveCreateActivityBtn").click(function() {
			//收集参数
			var owner = $("#create-marketActivityOwner").val();
			// var owner = $("#create-owner").text();
			var name = $.trim($("#create-marketActivityName").val());
			var cost = $.trim($("#create-cost").val());
			var startDate = $("#create-startDate").val();
			var endDate = $("#create-endDate").val();
			var description = $.trim($("#create-description").val());
			//表单验证
			if(owner == "") {
				alert("所有者不能为空!");
				return;
			}
			if(name == "") {
				alert("活动名不能为空!");
				return;
			}

			if(startDate!="" && endDate!="") {
				//使用字符串的大小代替日期的大小
				if(endDate<startDate) {
					alert("开始日期不能早于结束日期!");
					return;
				}
			}
			/*if(startDate!=""&&endDate!=""){
				//使用字符串的大小代替日期的大小
				if(endDate<startDate){
					alert("结束日期不能比开始日期小");
					return;
				}
			}*/


			//正则表达式验证cost
			var zz = /^(([1-9]\d*)|0)$/;
			if(!zz.test(cost)) {
				alert("成本不能是非负数!");
				return;
			}


			//发送ajax请求
			$.ajax({
				url:'workbench/activity/saveCreanteActivity.do',
				data:{
					owner:owner,
					name:name,
					cost:cost,
					startDate:startDate,
					endDate:endDate,
					description:description
				},
				type:'post',
				dataType:'json',
				success:function(data) {
				if(data.code == "1") {
					$("#createActivityModal").modal("hide")//创建成功,关闭窗口
					//刷新页面,显示第1页数据,保持每页显示的数据数不变
					queryActivityByConditionForPage(1, $("#demo_pag1").bs_pagination('getOption', 'rowsPerPage'));
				}else {
					//提示信息
					alert(atge.message);
					$("#createActivityModal").modal("true")//创建失败,保留窗口(这一步可有可无)
				}
				}
			});
		});

		//当容器加载完成后,对容器调用工具函数
		$(".myDate").datetimepicker({//datetimepicker:此按钮包含了单击事件
			language:'zh-CN',//设置语言
			format:"yyyy-mm-dd",//日期的格式
			minView:"month",//可以选的最小视图
			initialDate:new Date(),//设置初始日期为当前日期
			autoclose:true,//选择一个日期后自动关闭
			todayBtn:true,//设置显示'今天'按钮日期,默认是false
			clearBtn:true//设置显示'清空'按钮,默认是false
		});

		//当市场页面加载完成后,查询所有数据的第一页以及所有数据的总条数,默认每页显示10条
		//收集参数
		queryActivityByConditionForPage(1, 10);

		//给查询按钮绑定单击事件
		$("#queryActivityBtn").click(function() {
			//$("#demo_pag1").bs_pagination('getOption', 'rowsPerPage'): 上一页对象，根据上一页查找每页显示条数
			queryActivityByConditionForPage(1, $("#demo_pag1").bs_pagination('getOption', 'rowsPerPage'));
		});

		//给“全选”按钮添加单击事件
		$("#checkAll").click(function() {
			//this为内置对象，prop方法可以给目标对象赋值
			$("#tBody input[type = 'checkbox']").prop("checked", this.checked);
		});

		$("#tBody").on("click", "input[type='checkbox']", function() {
			/*alert($("#tBody input[type='checkbox']").size());  以#tBody为父(泛) 以input[type='checkbox']为子
			目标为子对象
			alert($("#tBody input[type='checkbox']:checked").size());*/
			if($("#tBody input[type='checkbox']").size() == $("#tBody input[type='checkbox']:checked").size()) {
				$("#checkAll").prop("checked", true);
			}else {
				$("#checkAll").prop("checked", false);
			}
		});

		//给“删除”按钮添加点击事件
		$("#deleteActivityBtn").click(function() {
			//收集参数
			var checkedIds = $("#tBody input[type='checkbox']:checked");
			if(checkedIds.length == 0) {
				alert("请选择要删除的市场活动!");
				return;
			}
			var ids = '';
			$.each(checkedIds, function() {
				ids+="id="+this.value+"&";
			})
			var ids = ids.substr(0, ids.length-1);
			//用户再次点击确定
			if(window.confirm("点解确认即可删除~")) {
				//发送ajax请求
				$.ajax({
					url:'workbench/activity/deleteActivityByIds.do',
					data:ids,
					type:'post',
					dataType:'json',
					success:function(data) {
						if(data.code == 1) {
							//回调,刷新局部页面
							queryActivityByConditionForPage(1, $("#demo_pag1").bs_pagination('getOption', 'rowsPerPage'));
						}else {
							alert(data.message);
						}

					}
				})
			};


		});

		//给“修改”按钮添加单击事件
		//查询
		$("#updateBtn").click(function() {
			if($("#tBody input[type='checkbox']:checked").size() != 1) {
				alert("请选择一个要修改的市场活动!");
				return;
			}

			var id = $("#tBody input[type='checkbox']:checked")[0].value;
			//发送ajax请求
			$.ajax({
				url:'workbench/activity/selectActivityById.do',
				data:{
					id:id
				},
				type:'post',
				dataType:'json',
				success:function(data) {
					//把市场活动的信息显示在修改的模态窗口上
					$("#edit-id").val(data.id);
					$("#edit-marketActivityOwner").val(data.owner);
					$("#edit-marketActivityName").val(data.name);
					$("#edit-startTime").val(data.startDate);
					$("#edit-endTime").val(data.endDate);
					$("#edit-cost").val(data.cost);
					$("#edit-describe").val(data.description);
					//弹出模态窗口
					$("#editActivityModal").modal("show");
				}
			})
		});

		//给“更新”按钮绑定单击事件
		$("#saveEditActivityBtn").click(function() {
			//收集参数
			var id = $("#edit-id").get(0).value;
			var owner = $("#edit-marketActivityOwner").get(0).value;
			var name = $.trim($("#edit-marketActivityName").get(0).value);
			var startDate = $("#edit-startTime").get(0).value;
			var endDate = $("#edit-endTime").get(0).value;
			var cost = $.trim($("#edit-cost").get(0).value);
			var description = $.trim($("#edit-describe").get(0).value);


			//表单验证
			if(name == null) {
				alert("请输入有效活动名称~");
				return;
			}
			if(cost == null) {
				alert("请输入正确成本~");
				return;
			}
			if(description == null) {
				alert("描述内容不能为空~");
				return;
			}
			//发送ajax
			$.ajax({
				url:'workbench/activity/saveEditActivity.do',
				data:{
					id:id,
					owner:owner,
					name:name,
					startDate:startDate,
					endDate:endDate,
					cost:cost,
					description:description
				},
				type:'post',
				dataType:'json',
				success:function(data) {
					if (data.code == 1) {
						//关闭模态窗口
						$("#editActivityModal").modal("hide");
						//更新数据
						queryActivityByConditionForPage($("#demo_pag1").bs_pagination('getOption', 'currentPage'),
						$("#demo_pag1").bs_pagination('getOption', 'rowsPerPage'));
					}else {
						alert(data.message);
						//继续保持模态窗口
						$("#editActivityModal").modal("hide");//这行代码可有可无
					}
				}
			})
		});

		//给"更新"按钮添加单击事件
		$("#saveEditActivityBtn").click(function () {
			//收集参数
			var id=$("#edit-id").val();
			var owner=$("#edit-marketActivityOwner").val();
			var name=$.trim($("#edit-marketActivityName").val());
			var startDate=$("#edit-startTime").val();
			var endDate=$("#edit-endTime").val();
			var cost=$.trim($("#edit-cost").val());
			var description=$.trim($("#edit-description").val());
			//表单验证(作业)

			//发送请求
			$.ajax({
				url:'workbench/activity/saveEditActivity.do',
				data:{
					id:id,
					owner:owner,
					name:name,
					startDate:startDate,
					endDate:endDate,
					cost:cost,
					description:description
				},
				type:'post',
				dataType:'json',
				success:function (data) {
					if(data.code=="1"){
						//关闭模态窗口
						$("#editActivityModal").modal("hide");
						//刷新市场活动列表,保持页号和每页显示条数都不变
						queryActivityByConditionForPage($("#demo_pag1").bs_pagination('getOption', 'currentPage'),$("#demo_pag1").bs_pagination('getOption', 'rowsPerPage'));
					}else{
						//提示信息
						alert(data.message);
						//模态窗口不关闭
						$("#editActivityModal").modal("show");
					}
				}
			});
		});

		//给下载列表数据(批量导出)
		$("#exportActivityAllBtn").click(function() {
			//导出数据都是同步请求,因为ajax无法接收文件数据
			window.location.href="workbench/activity/exportAllActivitys.do";
		});

		//给下载列表数据(选择导出)
		$("#exportActivityXzBtn").click(function() {
			var checkedIds = $("#tBody input[type='checkbox']:checked");
			//判断是否选择数据
			if(checkedIds.length <= 0) {
				alert("请选择你要导出的市场活动！");
				return;
			}
			//封装id
			var ids = '';
			$.each(checkedIds, function() {
				ids +="id="+this.value+"&";
			})
			//发送请求
			window.location.href="workbench/activity/exportActivitysById.do?"+ids;

		});

		//給"导入"按钮添加单击事件
		$("#importActivityBtn").click(function() {
			//获取文件名
			var activityFilName = $("#activityFile").val();
			//判断文件是否为excel文件，截取文件名后缀,并全部转换为小写
			var suffix = activityFilName.substr(activityFilName.lastIndexOf(".")+1).toLocaleLowerCase();
			if(suffix != "xls") {
				alert("仅支持xsl文件");
				return;
			}
			//获取文件的dom对象,dom对象里面存放这信息
			var activityFile = $("#activityFile")[0].files[0];

			if(activityFile.size > 1024*1024*5) {
				alert("文件大于5Mb!");
				return;
			}


			//FormData是ajax提供的接口,可以模拟键值对向后台提交参数,且可以提交二进制数据
			var formData = new FormData();
			formData.append("activityFile", activityFile);

			//发送ajax请求
			$.ajax({
				url:"workbench/activity/importActivity.do",
				data:formData,
				processData:false,//设置ajax向后台提价数据前,是否把所有参数统一转成字符串
				contentType:false,//设置ajax向后台提价数据前,是否把所有参数统一按urlencoded编码
				type:'post',
				dateType:'json',
				success:function(data) {
					if(data.code == 1) {
						alert("已成功插入"+data.retData+"条数据~")
						//关闭模态窗口
						$("#importActivityModal").modal("hide");
						//刷新活动列表,显示第一页
						queryActivityByConditionForPage(1, $("#demo_pag1").bs_pagination('getOption', 'rowsPerPage'));
					}else{
						alert(data.message);
						//继续保持模态窗口
						$("#importActivityModal").modal("show");
					}
				}
			});
		});



	});

	//封装查询市场活动函数
	function queryActivityByConditionForPage(pageNo,pageSize) {
		var name = $("#query-name").val();
		var owner = $("#query-owner").val();
		var startDate = $("#query-startDate").val();
		var endDate = $("#query-endDate").val();
		/*var pageNo = 1;
		var pageSize = 10;*/
		$.ajax({
			url:'workbench/activity/queryAllActivityByConditionForPage.do',
			data:{
				name:name,
				owner:owner,
				startDate:startDate,
				endDate:endDate,
				pageNo:pageNo,
				pageSize:pageSize
			},
			type:'post',
			dataType:'json',
			success:function(data) {
				// //总条数
				// $("#totalRowsB").text(data.totalRows);
				//拼接市场活动列表activityList
				var htmlStr="";
				$.each(data.activityList, function(index,obj) {
					htmlStr+="<tr class="+"active"+">";
					htmlStr+="<td><input type=\"checkbox\" value=\""+obj.id+"\"/></td>";
					htmlStr+="<td><a style=\"text-decoration: none; cursor: pointer;\" onclick=\"window.location.href='workbench/activity/detailActivity.do?id="+obj.id+"'\">"+obj.name+"</a></td>";
					htmlStr+="<td>"+obj.owner+"</td>";
					htmlStr+="<td>"+obj.startDate+"</td>";
					htmlStr+="<td>"+obj.endDate+"</td>";
					htmlStr+="</tr>";
				});
				$("#tBody").html(htmlStr);
				//刷新全选按钮
				$("#checkAll").prop("checked", false);

				//计算总页数
				var totalPages=1;
				var number = data.totalRows/pageSize;
				if (data.totalRows%pageSize == 0) {
					totalPages = number;
				}else {
					totalPages = parseInt(number)+1;
				}

				/*对容器调用bs_pagination工具函数,显示翻页信息*/
				$("#demo_pag1").bs_pagination({
					currentPage:pageNo,//当前页号,相当于pageNo
					rowsPerPage:pageSize, //每页显示条数,相当于pageSize
					totalPages:totalPages,  //总页数(必填)
					totalRows:data.totalRows, //总条数
					visiblePageLinks:5,//最多可以显示的卡片数

					//一下三个默认都为true
					showGoToPage: true, //是否显示“跳转到”部分,
					showRowsPerPage: true, //是否显示“每页显示条数”部分
					showRowsInfo: true, //是否显示记录的信息

					onChangePage:function(event, pageObj) {//event:切换事件本身,pageObj:本页面对象
						//回调函数(递归)
						queryActivityByConditionForPage(pageObj.currentPage, pageObj.rowsPerPage);
					}

				})

			}
		});
	};
	
</script>
</head>
<body>

	<!-- 创建市场活动的模态窗口 -->
	<div class="modal fade" id="createActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form id="createActivityForm" class="form-horizontal" role="form">
					
						<div class="form-group">
							<label for="create-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-marketActivityOwner">
								  <c:forEach items="${userList}" var="user">
									  <option value="${user.id}" id="create-owner">${user.name}</option>
								  </c:forEach>
								</select>
							</div>
                            <label for="create-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-marketActivityName">
                            </div>
						</div>
						
						<div class="form-group">
							<label for="create-startDate" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control myDate" id="create-startDate" readonly>
							</div>
							<label for="create-endDate" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control myDate" id="create-endDate" readonly="true">
							</div>
						</div>
                        <div class="form-group">

                            <label for="create-cost" class="col-sm-2 control-label">成本</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-cost">
                            </div>
                        </div>
						<div class="form-group">
							<label for="create-description" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-description"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="saveCreateActivityBtn">保存</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 修改市场活动的模态窗口 -->
	<div class="modal fade" id="editActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" role="form">
					<input type="hidden" id="edit-id"/><%--隐藏--%>
						<div class="form-group">
							<label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-marketActivityOwner">
									<c:forEach items="${userList}" var="user">
										<option value="${user.id}">${user.name}</option>
									</c:forEach>
								</select>
							</div>
                            <label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-marketActivityName" value="发传单">
                            </div>
						</div>

						<div class="form-group">
							<label for="edit-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control myDate" id="edit-startTime" readonly>
							</div>
							<label for="edit-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control myDate" id="edit-endTime" readonly>
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-cost" class="col-sm-2 control-label">成本</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-cost" value="5,000">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="edit-describe">市场活动Marketing，是指品牌主办或参与的展览会议与公关市场活动，包括自行主办的各类研讨会、客户交流会、演示会、新产品发布会、体验会、答谢会、年会和出席参加并布展或演讲的展览会、研讨会、行业交流会、颁奖典礼等</textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="saveEditActivityBtn">更新</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 导入市场活动的模态窗口 -->
    <div class="modal fade" id="importActivityModal" role="dialog">
        <div class="modal-dialog" role="document" style="width: 85%;">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">
                        <span aria-hidden="true">×</span>
                    </button>
                    <h4 class="modal-title" id="myModalLabel">导入市场活动</h4>
                </div>
                <div class="modal-body" style="height: 350px;">
                    <div style="position: relative;top: 20px; left: 50px;">
                        请选择要上传的文件：<small style="color: gray;">[仅支持.xls]</small>
                    </div>
                    <div style="position: relative;top: 40px; left: 50px;">
                        <input type="file" id="activityFile">
                    </div>
                    <div style="position: relative; width: 400px; height: 320px; left: 45% ; top: -40px;" >
                        <h3>重要提示</h3>
                        <ul>
                            <li>操作仅针对Excel，仅支持后缀名为XLS的文件。</li>
                            <li>给定文件的第一行将视为字段名。</li>
                            <li>请确认您的文件大小不超过5MB。</li>
                            <li>日期值以文本形式保存，必须符合yyyy-MM-dd格式。</li>
                            <li>日期时间以文本形式保存，必须符合yyyy-MM-dd HH:mm:ss的格式。</li>
                            <li>默认情况下，字符编码是UTF-8 (统一码)，请确保您导入的文件使用的是正确的字符编码方式。</li>
                            <li>建议您在导入真实数据之前用测试文件测试文件导入功能。</li>
                        </ul>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button id="importActivityBtn" type="button" class="btn btn-primary">导入</button>
                </div>
            </div>
        </div>
    </div>
	
	
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>市场活动列表</h3>
			</div>
		</div>
	</div>
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">
		
			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input class="form-control" type="text" id="query-name">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" id="query-owner">
				    </div>
				  </div>


				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">开始日期</div>
					  <input class="form-control" type="text" id="query-startDate"/>
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">结束日期</div>
					  <input class="form-control" type="text" id="query-endDate">
				    </div>
				  </div>
				  
				  <button type="button" id="queryActivityBtn" class="btn btn-default">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button type="button" class="btn btn-primary" id="createActivityBtn"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" id="updateBtn"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger" id="deleteActivityBtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				<div class="btn-group" style="position: relative; top: 18%;">
                    <button type="button" class="btn btn-default" data-toggle="modal" data-target="#importActivityModal" ><span class="glyphicon glyphicon-import"></span> 上传列表数据（导入）</button>
                    <button id="exportActivityAllBtn" type="button" class="btn btn-default"><span class="glyphicon glyphicon-export"></span> 下载列表数据（批量导出）</button>
                    <button id="exportActivityXzBtn" type="button" class="btn btn-default"><span class="glyphicon glyphicon-export"></span> 下载列表数据（选择导出）</button>
                </div>
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="checkAll"/></td>
							<td>名称</td>
                            <td>所有者</td>
							<td>开始日期</td>
							<td>结束日期</td>
						</tr>
					</thead>
					<tbody id="tBody">
						<%--<tr class="active">
							<td><input type="checkbox" /></td>
							<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.jsp';">发传单</a></td>
                            <td>zhangsan</td>
							<td>2020-10-10</td>
							<td>2020-10-20</td>
						</tr>
                        <tr class="active">
                            <td><input type="checkbox" /></td>
                            <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.jsp';">发传单</a></td>
                            <td>zhangsan</td>
                            <td>2020-10-10</td>
                            <td>2020-10-20</td>
                        </tr>--%>
					</tbody>
				</table>

				<%--page显示--%>
				<div id="demo_pag1"></div>
			</div>

			<%--原本page显示--%>
			<%--<div style="height: 50px; position: relative;top: 30px;">
				<div>
					<button type="button" class="btn btn-default" style="cursor: default;">共<b id="totalRowsB">50</b>条记录</button>
				</div>
				<div class="btn-group" style="position: relative;top: -34px; left: 110px;">
					<button type="button" class="btn btn-default" style="cursor: default;">显示</button>
					<div class="btn-group">
						<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
							10
							<span class="caret"></span>
						</button>
						<ul class="dropdown-menu" role="menu">
							<li><a href="#">20</a></li>
							<li><a href="#">30</a></li>
						</ul>
					</div>
					<button type="button" class="btn btn-default" style="cursor: default;">条/页</button>
				</div>
				<div style="position: relative;top: -88px; left: 285px;">
					<nav>
						<ul class="pagination">
							<li class="disabled"><a href="#">首页</a></li>
							<li class="disabled"><a href="#">上一页</a></li>
							<li class="active"><a href="#">1</a></li>
							<li><a href="#">2</a></li>
							<li><a href="#">3</a></li>
							<li><a href="#">4</a></li>
							<li><a href="#">5</a></li>
							<li><a href="#">下一页</a></li>
							<li class="disabled"><a href="#">末页</a></li>
						</ul>
					</nav>
				</div>
			</div>--%>

		</div>
		
	</div>
</body>
</html>