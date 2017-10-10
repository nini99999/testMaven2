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
	<%--设置编辑按钮的背景图片--%>
	<style type="text/css">
		.grid {
			background-image:url(<%=path%>/extjs/examples/shared/icons/fam/grid.png) !important;
		}
	</style>
<link rel="stylesheet" type="text/css" href="<%=path%>/extjs/resources/css/ext-all.css" />
<link rel="stylesheet" type="text/css" href="<%=path%>/extjs/resources/css/ItemSelector.css">
<script type="text/javascript" src="<%=path%>/extjs/examples/shared/include-ext.js"></script>
<!-- 	<script type="text/javascript" src="extjs/examples/shared/options-toolbar.js"></script> -->
<script type="text/javascript" src="<%=path%>/extjs/examples/shared/states.js"></script>
<script type="text/javascript" src="<%=path%>/extjs/locale/ext-lang-zh_CN.js"></script>
</head>
<body>
<script type="text/javascript" >
	var store;
	function refreshGrid(){
		store.load();//更新gird数据显示
	}


	Ext.onReady(function(){
		//数据模型
		Ext.define('menuInfo', {
			extend: 'Ext.data.Model',
			fields: [
				{name: 'id', type: 'string'},
				{name: 'text', type: 'string'},
				{name: 'leaf', type: 'boolean'},
				{name: 'url', type: 'string'},
				{name: 'description', type: 'string'},
				{name: 'expanded', type: 'boolean'},
				{name: 'checked', type: 'boolean'}
			]
		});
		// 创建一个TreeStore，TreeStore负责为Tree提供数据
		store = Ext.create('Ext.data.TreeStore',{
			model : 'menuInfo',
			proxy : {
				type : 'ajax',
				url : 'menu/toMenu'
			},
			listeners:{
				load : function(store, record, records, eOpts)
				{
					if(records==null){
						parent.location.href="<%=request.getContextPath()%>/login.jsp";
						return false;
					}
				}
			}
		});


		Ext.create('Ext.container.Viewport',
				{
					layout: 'border',
					items: [
						// 左边区域的内容
						{
							region: 'west',
							xtype: 'treepanel', // 表明这是Ext.tree.Panel
							id: 'treepanel_',
							title: '菜单列表',
							listeners:{
								// 为itemclick事件添加监听器 使菜单单选
								itemclick : function(view , record, item)
								{
									var records = view.getChecked();
									Ext.Array.each(records, function(rec){
										rec.set("checked",false);
									});
									record.set("checked",true);
								}

							},
							width: '100%',
							store: store,
							rootVisible: false,
							split: true,
							//viewConfig: {
							//    plugins: {
							//        ptype: 'treeviewdragdrop'
							//    }
							//},
							dockedItems: [
								{
									xtype: 'toolbar',
									items: [
										{
											text: '新增菜单',
											handler: function(view , record, item){
												var records = Ext.getCmp("treepanel_").getChecked();

												var parentMenuId = "";
												var num = 0;
												Ext.Array.each(records, function(rec){
													parentMenuId = rec.get('id');
													num++;
												});
												if(num==0){
													Ext.Msg.alert('提示', '亲~您还未勾选菜单节点!');
													return false;
												}
												<%--window.open("<%=request.getContextPath()%>/permission/viewMenuAdd?parentMenuId="+parentMenuId,"","top=100,left=195,width=800,height=550,toolbar=yes,scrollbars=yes");--%>
												showWin(record);

											}
										},
										{
											text: '编辑菜单',
											handler: function(){
												var records = Ext.getCmp("treepanel_").getChecked();

												var menuId = "";
												var num = 0;
												Ext.Array.each(records, function(rec){
													menuId = rec.get('id');
													num++;
												});
												if(num==0){
													Ext.Msg.alert('提示', '亲~您还未勾选菜单节点!');
													return false;
												}
												if(menuId==undefined){
													Ext.Msg.alert('提示', '亲~您不能勾选菜根单节点操作!');
													return false;
												}
												window.open("<%=request.getContextPath()%>/permission/viewMenuEdit?menuId="+menuId,"","top=100,left=195,width=800,height=550,toolbar=yes,scrollbars=yes");
											}},
										{
											text: '删除菜单',
											handler: function(view , record, item){
												var records = Ext.getCmp("treepanel_").getChecked();

												var menuId = "";
												var num = 0;
												Ext.Array.each(records, function(rec){
													menuId = rec.get('id');
													num++;
												});
												if(num==0){
													Ext.Msg.alert('提示', '亲~您还未勾选菜单节点!');
													return false;
												}
												if(menuId==undefined){
													Ext.Msg.alert('提示', '亲~您不能勾选菜根单节点操作!');
													return false;
												}
												Ext.MessageBox.confirm("提示","您确定删除该菜单吗？（关联子菜单同删除）",function(result){
													if(result=='yes'){
														Ext.Ajax.request({
															url: 'menu/delMenu?menuId='+menuId,
															method: 'post',
															success: function (response, options) {
//																Ext.Msg.alert('成功', response.responseText);
																console.log(response.responseText);
																var responseText=Ext.decode(response.responseText);
																if(responseText.success){
																	Ext.Msg.alert('成功',responseText.msg);
																}else{
																	Ext.Msg.alert('失败',responseText.msg);
																}
																store.load();//更新gird数据显示
															},
															failure: function (response, options) {
																Ext.MessageBox.alert('失败', '请求超时或网络故障,错误编号：' + response.status);
															}
														});
													}
												});


											}},
									]},

							]},
						// 下边区域的内容，使用一个普通Ext.panel.Panel
						// 没有指定xtype，默认是Ext.panel.Panel
						/* {
						 id:'info',
						 region: 'south',
						 title: '操作信息',
						 collapsible: true,
						 split: true,
						 height: 100,
						 minHeight: 100
						 }, */
						// 右边区域的内容，使用一个普通的Ext.panel.Panel
						/* 	{
						 region: 'east',
						 title: '公告栏',
						 collapsible: true,
						 split: true,
						 width: 200
						 }, */
						// 中间面板的内容：使用一个Ext.tab.Panel
					]
				});


		function showWin(record){

			var dataStateStore = Ext.create('Ext.data.Store',
					{
						// 指定读取数据的name、id字段
						fields: ['dictKey' , 'dictValue'],
						proxy:
						{
							type: 'ajax',
							url: '<%=request.getContextPath()%>/dict/findDictListByParentKey?parentKey=JFZT',// 向该URL发送Ajax请求
							reader: { // 使用Ext.data.reader.Json读取服务器数据
								type: 'json',
								root: 'data' // 直接读取服务器响应的data数据
							}
						}
//						autoLoad: true
					});

			var editFormPanel = Ext.create('Ext.form.Panel',{
				url: 'menu/addMenu',
				frame: true,
				width: 640,
				bodyPadding: 5,

				fieldDefaults: {
					labelAlign: 'left',
					labelWidth: 120,
					anchor: '100%'
				},
				items: [
					{
						xtype: 'textfield',
						name: 'menuTitle',
						fieldLabel: '菜单名',
						allowBlank: false // 输入校验：不允许为空
					},
					{

						name: 'openFlag',
						fieldLabel: '状态',
						allowBlank: false ,// 输入校验：不允许为空
						xtype: 'combobox',
						store: dataStateStore,
						valueField: 'dictKey',
						displayField: 'dictValue',
						//typeAhead: true,
						emptyText: '请选择数据状态...',
						valueNotFoundText:'有效',
						value:'1'
						//editable : false,//可否允许输入
						//   emptyText: '请选择数据状态...'
					},
					{
						xtype: 'displayfield',
						fieldLabel: '父菜单名',
						name:'parentMenuName'
					},
					{
						xtype: 'hiddenfield',
						name: 'parentMenuId'
					},
					{
						xtype: 'textfield',
						name: 'menuUrl',
						fieldLabel: '菜单URL'
					},
					{
						xtype: 'textareafield',
						name: 'menuDescription',
						fieldLabel: '描述'
					}

				],
				buttons: [

					{
						formBind: true, // 只有当整个表单输入校验通过时，该按钮才可用
						disabled: true, // 设置该按钮默认不可用
						text: '保存',
						handler: function() {
							var form = this.up('form').getForm();
							//var enterpriseId = Ext.getCmp('enterpriseId_').getValue(); //获取文本框值

							if (form.isValid()) {
								this.disabled = true;
								form.submit({
									success: function(form, action) {
										Ext.Msg.show({
											title:'操作完成',
											msg: action.result.msg,
											buttons: Ext.Msg.YES,
											icon: Ext.MessageBox.QUESTION,
											closable:false,
											fn: function(){
//												window.opener.refreshGrid();//父页面gird刷新显示
//												window.close();
												win.close();
												store.reload();
											}

										});
										this.disabled = false;
									}
								});
							}
						}
					},
					{
						text: '关闭',
						formBind: false, // 只有当整个表单输入校验通过时，该按钮才可用
						disabled: false, // 设置该按钮默认不可用
						// 单击该按钮的事件处理函数
						handler: function() {
//							window.close();
//							window.opener.refreshGrid();//父页面gird刷新显示
							store.reload();
							win.close();
						}
					}
				]
			});

			var records = Ext.getCmp("treepanel_").getChecked();
			console.log(records[0]);
			editFormPanel.loadRecord(records[0]);
			var form=editFormPanel.getForm();
			var parentMenuName=form.findField('parentMenuName');
			var parentMenuId=form.findField('parentMenuId');
			parentMenuName.setValue(records[0].get('text'));
			parentMenuId.setValue(records[0].get('id'))

			//2.定义一个window.Window
			var win = Ext.create('Ext.window.Window',{
				title: '新增菜单',
				hight: 800,
				width:640,
				//frame: true,
				hidden: false,
				layout: 'fit',
				closable: true,
				draggable:false,
				renderTo: Ext.getBody(),
				//animateTarget:this,
				items: [editFormPanel]

			});
			win.show();
		}

	});


</script>
<div id="target"></div>
</body>
</html>