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
	var grid;
	var roleStore;

	function toEdit(value){

		console.log('value:'+value);
		<%--window.open("<%=request.getContextPath()%>/permission/viewRoleEdit?roleId="+roleId,"","top=100,left=195,width=800,height=550,toolbar=yes,scrollbars=yes");--%>
		showWin(value);
	}

	function refreshGrid(){

		roleStore.load();//更新gird数据显示
	}

	Ext.onReady(function(){
		var dataStateStore = Ext.create('Ext.data.Store',
				{
					// 指定读取数据的name、id字段
					fields: ['dictKey' , 'dictValue'],
                    data: [
                        {  dictKey: 1, dictValue: "有效"},
                        {  dictKey: 0, dictValue: "无效"},
                    ]
				});


		function editTo(value, p, record) {
			return Ext.String.format(
					'<a href="javascript:toEdit(\'{0}\');">编辑</a>',
					value
			);
		}


		var searchForm = Ext.create('Ext.form.Panel', {
			//		title: '订单查询',
			bodyPadding: 5,
			width: '100%', // 指定表单宽度
			// 当提交表单时自动提交Ajax请求。
			//url: 'fillRoleParames',
			method: 'POST', // 指定以POST方式提交请求
			// 默认使用anchor布局方式
			layout: 'absolute',
			// 设置表单控件默认占满整个容器
			/* defaults: {
			 anchor: '100%'
			 }, */
			// 设置表单组件的默认类型
			defaultType: 'textfield',
			// 为所有表单控件设置默认属性
			fieldDefaults: {
				labelAlign: 'left',
				labelWidth: 120,
				width: 250
			},
			items: [
				{
					x: 10,
					y: 10,
					fieldLabel: '角色名', // 表单控件的Label
					name: 'roleName', // 表单控件的名称
					//			allowBlank: false // 输入校验：不允许为空
					xtype: 'textfield',
					id:'roleName_'
				},
				{
					x: 575,
					y: 10,
					fieldLabel: '状态', // 表单控件的Label
					name: 'roleState', // 表单控件的名称
					allowBlank: true, // 输入校验：不允许为空
					xtype: 'combobox',
					id:'roleState_',
					store: dataStateStore,
					valueField: 'dictKey',
					displayField: 'dictValue',
					typeAhead: true,
					emptyText: '请选择数据状态...'
				},
			],
			// 为表单设置按钮
			buttons: [{

				//style : 'margin-left:970px;float:left',//离左边一个按钮的距离多加100px
				// 提交按钮
				text: '查询',
				formBind: true, // 只有当整个表单输入校验通过时，该按钮才可用
				disabled: true, // 设置该按钮默认不可用
				// 单击该按钮的事件处理函数
				handler: function()
				{

					roleStore.currentPage =1;
					roleStore.loadPage(1);


				}
			}
			],
			dockedItems: [{
				xtype: 'toolbar',
				dock: 'bottom',
				ui: 'footer',
				items: [
					{
						xtype: 'button',
						text: '新增角色',
						handler: function()
						{
							<%--window.open("<%=request.getContextPath()%>/permission/viewRoleAdd","","top=100,left=195,width=800,height=550,toolbar=yes,scrollbars=yes");--%>
							showWin();
						}
					}
				]

			}
			]
		});



		Ext.define('role', {
			extend: 'Ext.data.Model',
			fields: [
				{name: 'roleId' , type: 'string'},
				{name: 'roleName', type: 'string'},
				{name: 'roleState', type: 'string'},
				{name: 'roleDescription', type: 'string'},
				{name: 'menuIds', type: 'string'},

			]
		});

		// 创建一个Ext.data.Store对象
		roleStore = Ext.create('Ext.data.Store',
				{
					model: 'role',
					pageSize: 15, // 设置每页显示15条记录
					// 使用proxy指定加载远程数据
					proxy:
					{
						type: 'ajax',
						url: 'user/viewRoleList',// 向该URL发送Ajax请求
						reader: { // 使用Ext.data.reader.Json读取服务器数据
							type: 'json',
							totalProperty: 'total',
							root: 'data' // 直接读取服务器响应的data数据
						}
					},
					autoLoad:true
				});


		roleStore.on('beforeload', function (store, options) {
			var roleName = Ext.getCmp('roleName_').getValue(); //获取文本框值
			var roleState = Ext.getCmp('roleState_').getValue(); //获取文本框值
			var new_params = {start: 0, limit: 15,roleName:encodeURIComponent(roleName),roleState:roleState};
			Ext.apply(store.proxy.extraParams, new_params);
		});





		grid=Ext.create('Ext.grid.Panel', {
			title: '角色列表',
			//	width: 550, // 指定表单宽度
			renderTo: Ext.getBody(),

			tbar:[searchForm],
			viewConfig:{
				enableTextSelection:true
			},
			// 定义该表格包含的所有数据列
			columns: [


				{ text: '角色名', dataIndex: 'roleName', width: 300,resizable:true},
				{ text: '描述', dataIndex: 'roleDescription', flex:1},
				{ text: '状态', dataIndex: 'roleState' , width: 150,resizable:true},
				{ text: '菜单', dataIndex: 'menuIds' , width: 150,resizable:true},
				{ text: '操作', dataIndex: 'roleId' ,renderer:editTo ,width: 70 ,resizable:true},
			],
			store: roleStore,

			/*  listeners: {
			 'selectionchange': function(view, records) {
			 Ext.alert('1111');
			 }
			 }, */
			// 定义分页工具条
			bbar:{
				xtype: 'pagingtoolbar',
				store: roleStore,
				displayInfo: true,
				refreshText:'aa'
			}

		});
	});

	function showWin(value){
		var dataStateStore = Ext.create('Ext.data.Store',
				{
					// 指定读取数据的name、id字段
					fields: ['dictKey' , 'dictValue'],
                    data: [
                        {  dictKey: 1, dictValue: "有效"},
                        {  dictKey: 0, dictValue: "无效"},
                    ]
				});
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
				load : function(node, records,successful, eOpts)
				{
					if(records==null){
						location.href="<%=request.getContextPath()%>/login.jsp";
						return false;
					}
					if(value!=null&&value!=undefined) {

						var rootNode = treeFormPanel.getRootNode();
						var nodes = [];
						nodes = findchildnode(rootNode);
						console.log('' + nodes.length)
						var record = grid.getStore().findRecord("roleId", value)
						var menuIds = record.get('menuIds');
						var menus = [];
						menus = menuIds.split(",");
						Ext.Array.each(nodes, function (node) {
							var index = menus.indexOf(node.data.id);
							if (index != -1) {
								node.set('checked', true);
								node.parentNode.set('checked',true);
							}


						})
					}

				}
			}
		});

//		var nodevalue = "";//定义一个全局变量，保存节点的id或值
//		function getAllRoot(value){
//			var rootNode = value.getRootNode();//获取根节点
//			findchildnode(rootNode); //开始递归
//			nodevalue= temp.join(",");
//			//alert(nodevalue);
//			return nodevalue;
//		}

		var temp = [];
       //获取所有的子节点
		function findchildnode(node){

			var childnodes = node.childNodes;
			Ext.each(childnodes, function (){ //从节点中取出子节点依次遍历
				var nd = this;
//				console.log(nd.data.id);
				temp.push(nd);
				if(nd.hasChildNodes()){ //判断子节点下是否存在子节点
					findchildnode(nd); //如果存在子节点 递归
				}
			});
			return temp;
		}


		var treeFormPanel = Ext.create('Ext.tree.Panel', {
			id:'treeFormPanel_',
			width: 240,
			store: store,
			rootVisible: false,
			listeners:{
				//添加监听 设置树的节点选择的级联关系
				checkchange: function(node, checked) {
					listenerCheck(node, checked);
				}

			}
		});

		var editFormPanel = Ext.create('Ext.form.Panel',{
			url: 'role/addRole',
			frame: true,
			width: 400,
			bodyPadding: 5,

			fieldDefaults: {
				labelAlign: 'left',
				labelWidth: 120,
				anchor: '100%'
			},
			items: [
				{
					xtype: 'textfield',
					name: 'roleName',
					fieldLabel: '角色名',
					allowBlank: false // 输入校验：不允许为空
				},
				{
					name: 'roleState',
					fieldLabel: '状态',
					allowBlank: false ,// 输入校验：不允许为空
					xtype: 'combobox',
					store: dataStateStore,
					id:'dataState_',
					//typeAhead: true,
					valueField: 'dictKey',
					displayField: 'dictValue',
					editable : false,//可否允许输入
					//emptyText: '请选择数据状态...'
					valueNotFoundText:'有效',
					value:'1'


				},
				{
					xtype: 'textareafield',
					name: 'roleDescription',
					fieldLabel: '描述'
				},
				{
					xtype: 'hiddenfield',
					name: 'menuIds',
					id: 'menuIds_'
				},
			],
			buttons: [

				{
					formBind: true, // 只有当整个表单输入校验通过时，该按钮才可用
					disabled: true, // 设置该按钮默认不可用
					text: '保存',
					handler: function() {
						var form = this.up('form').getForm();
						//var enterpriseId = Ext.getCmp('enterpriseId_').getValue(); //获取文本框值
						var records = Ext.getCmp("treeFormPanel_").getChecked();

						var menuId = "";
						var num = 0;
						Ext.Array.each(records, function(rec){
							if(rec.get('id')!=undefined&&rec.get('id')!="root"){
							menuId += rec.get('id') + ",";
							num++;
							}
						});
						if(num==0){
							Ext.Msg.alert('提示', '亲~您还未勾选关联权限功能菜单!');
							return false;
						}
						Ext.getCmp('menuIds_').setValue(menuId);
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
											win.close();
											grid.getStore().reload();
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
//						window.opener.refreshGrid();//父页面gird刷新显示
//						window.close();
						win.close();
						grid.getStore().reload();
					}
				}
			]
		});


		//2.定义一个window.Window
		var win = Ext.create('Ext.window.Window',{
			title: '新增角色',
			height:'80%',
			width:600,
			//frame: true,
			hidden: false,
			layout: 'hbox',
			closable: true,
			draggable:false,
			modal:true,
			//autoScroll:true,
			overflowX:'hidden',
			overflowY:'auto',
			renderTo: Ext.getBody(),
			//animateTarget:this,
			items: [editFormPanel,treeFormPanel]

		});

		if(value!=null&&value!=undefined){
			win.setTitle("编辑角色");
			var record=grid.getStore().findRecord("roleId",value)
			var menuIds=record.get('menuIds');
//			var menus=[];
//			menus=menuIds.split(",");

			editFormPanel.loadRecord(record);
		}

		win.show();

	}

	//添加监听 设置树的节点选择的级联关系
	var listenerCheck = function(node, checked) {
		childHasChecked(node,checked);
		var parentNode = node.parentNode;
		if(parentNode != null) {
			parentCheck(parentNode,checked);
		}
	};
	//级联选中父节点
	var parentCheck = function(node ,checked){
		var childNodes = node.childNodes;
		var boolCHK = false;
		for (var i = 0; i < childNodes.length; i++) {
			if (childNodes[i].get('checked')) {
				boolCHK = true;
				break;
			}
		}
		if(boolCHK){
			node.set('checked',true);
		}
		var parentNode = node.parentNode;
		if (parentNode != null ) {
			parentCheck(parentNode,checked);
		}
	};
	//级联选择子节点
	var childHasChecked = function (node, checked) {
		node.cascadeBy(function (child) {
			//alert(checked);
			child.set("checked",checked);
		});
	};


</script>
</body>
</html>