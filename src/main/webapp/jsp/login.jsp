<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<base href="<%=basePath%>">
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>管理平台</title>
	<link rel="stylesheet" type="text/css" href="<%=path%>/extjs/resources/css/ext-all.css" />
	<link rel="stylesheet" type="text/css" href="<%=path%>/extjs/resources/css/ItemSelector.css">
	<script type="text/javascript" src="<%=path%>/extjs/examples/shared/include-ext.js"></script>
	<!-- 	<script type="text/javascript" src="extjs/examples/shared/options-toolbar.js"></script> -->
	<script type="text/javascript" src="<%=path%>/extjs/examples/shared/states.js"></script>
	<script type="text/javascript" src="<%=path%>/extjs/locale/ext-lang-zh_CN.js"></script>
</head>
<body>
<script type="text/javascript">
	Ext.onReady(function(){

		// 创建表单容器
		Ext.create('Ext.window.Window',
				{

					title: '管理平台',
					width: 360,
					height: 140,
					closable:false,
					draggable:false,
					layout: 'absolute',
					// 设置表单组件的默认类型
					defaultType: 'textfield',
					items: {
						xtype:'form',
						url: 'loginlogout/login',
						height: 100,
						method: 'POST', // 指定以POST方式提交请求
						// 默认使用anchor布局方式
						layout: 'absolute',
						// 设置表单组件的默认类型
						//		defaultType: 'textfield',
						// 为所有表单控件设置默认属性
						items: [
							{
								x: 10,
								y: 10,
								fieldLabel: '用户名', // 表单控件的Label
								xtype:'textfield',
								name: 'name', // 表单控件的名称
								allowBlank: false // 输入校验：不允许为空
							},
							{
								x: 10,
								y: 40,
								inputType: 'password',
								fieldLabel: '密码', // 表单控件的Label
								xtype:'textfield',
								name: 'passwd', // 表单控件的名称
								allowBlank: false, // 输入校验：不允许为空
								listeners : {
									specialkey : function(field, e) {
										if (e.getKey() == Ext.EventObject.ENTER) {
											// 获取表单，实际返回的是Ext.form.Basic对象
											var form = this.up('form').getForm();
											// 如果表单输入校验通过
											if (form.isValid())
											{
												form.submit(
														{
//															提交成功，第一个参数是Ext.form.Basic对象
//															第二个参数是Ext.action.Action对象
															success: function(form, action)
															{
																//			Ext.Msg.alert('登录成功！', action.result.msg);
																window.location.href =  'menu/toDemo';

															},
															// 提交失败
															failure: function(form, action)
															{
																Ext.Msg.alert('登录失败！', action.result.msg);
															}
														});
											}
										}
									}
								}
							},
						],
						// 为表单设置按钮
						buttons: [
							{
								// 重设按钮
								text: '重设',
								// 单击该按钮时重设表单
								handler: function()
								{
									this.up('form').getForm().reset();
								}
							},
							{
								// 提交按钮
								text: '提交',
								formBind: true, // 只有当整个表单输入校验通过时，该按钮才可用
								disabled: true, // 设置该按钮默认不可用
								// 单击该按钮的事件处理函数
								handler: function()
								{
									// 获取表单，实际返回的是Ext.form.Basic对象
									var form = this.up('form').getForm();
									// 如果表单输入校验通过
									if (form.isValid())
									{
										// 以Ajax方式提交表单
										form.submit(
												{
													// 提交成功，第一个参数是Ext.form.Basic对象
													// 第二个参数是Ext.action.Action对象
													success: function(form, action)
													{
														//			Ext.Msg.alert('登录成功！', action.result.msg);
														window.location.href =  'menu/toDemo';

													},
													// 提交失败
													failure: function(form, action)
													{
														Ext.Msg.alert('登录失败！', action.result.msg);
													}
												});
									}
								}
							}
						]
					}
				}).show();

	});
</script>
</body>
</html>