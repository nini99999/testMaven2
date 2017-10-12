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
	<script type="text/javascript" src="<%=path%>/bootstrap/jquery.min.js"></script>
</head>
<body>
	<script type="text/javascript" >
        function toDelete(value){
            if (confirm('确认删除该用户？')==true){
                $.ajax({
                    url: "user/deleteUser?userId="+value,
// 数据发送方式
                    type: "post",
// 接受数据格式
                    dataType: "json",
// 要传递的数据
                    data: 'data',
// 回调函数，接受服务器端返回给客户端的值，即result值
                    success: function (data) {
                        refreshGrid()

                    },
                    error: function (data) {
                        alert("删除失败" + data);
                    }
                })
            }else{
                return false;
            }
        }
		Ext.Loader.setConfig({enabled: true});
		Ext.require([
			'Ext.form.Panel',
			'Ext.ux.form.MultiSelect',//store不能为空
			'Ext.ux.form.ItemSelector',
			'Ext.tip.QuickTipManager',
		/*'Ext.ux.ajax.JsonSimlet',
		'Ext.ux.ajax.SimManager'*/
		]);
		Ext.Loader.setPath('Ext.ux', 'extjs/src/ux');
		Ext.QuickTips.init();
		var grid;

	var UserStore;

	function toEdit(UserId){
		
		window.open("<%=request.getContextPath()%>/permission/viewUserEdit?UserId="+UserId,"","top=50,left=195,width=800,height=650,toolbar=yes,scrollbars=yes");
	}
	
 	function refreshGrid(){
 		
 		UserStore.load();//更新gird数据显示
	} 
	
	Ext.onReady(function(){
		var lockedStateStore = Ext.create('Ext.data.Store', 
				{
					// 指定读取数据的name、id字段
					fields: ['dictKey' , 'dictValue'],
					proxy: 
					{
						type: 'ajax',
						url: '<%=request.getContextPath()%>/dict/findDictListByParentKey?parentKey=YHSDZT',// 向该URL发送Ajax请求
						reader: { // 使用Ext.data.reader.Json读取服务器数据
							type: 'json',
							root: 'data' // 直接读取服务器响应的data数据
						}
					}
				});
        function editTo(value, p, record) {
            return Ext.String.format(
                '<a href="javascript:toEdit(\'{0}\');">编11辑</a>&nbsp;&nbsp;<a href="javascript:toDelete(\'{0}\');">删除</a>',
                value
            );
        }
		var UserGradeStore = Ext.create('Ext.data.Store', 
				{
					// 指定读取数据的name、id字段
					fields: ['id' , 'schoolname'],
                    autoLoad:this.autoLoad,
                    proxy:
					{
						type: 'ajax',
						url: '<%=request.getContextPath()%>/dict/findDictListByParentKey?parentKey=QYMGJB',// 向该URL发送Ajax请求
						reader: { // 使用Ext.data.reader.Json读取服务器数据
							type: 'json',
							root: 'data' // 直接读取服务器响应的data数据
						}
					}
				});
        var userSchoolStore = Ext.create('Ext.data.Store',
            {
                // 指定读取数据的name、id字段
                fields: ['id' , 'name'],
                proxy: {
                    type: 'ajax',
                    url: '<%=request.getContextPath()%>/user/querySchool',// 向该URL发送Ajax请求
                    reader: { // 使用Ext.data.reader.Json读取服务器数
                        type: 'json',
                        root: 'data' // 直接读取服务器响应的data数据
                    }
                },
                autoLoad : true
            });
		var roleStore = Ext.create('Ext.data.Store', 
				{
					// 指定读取数据的name、id字段
					fields: ['VALUE','TEXT'],
					proxy: 
					{
						type: 'ajax',
						url: 'queryRoleListForSearch',// 向该URL发送Ajax请求
						reader: { // 使用Ext.data.reader.Json读取服务器数据
							type: 'json',
							root: 'data' // 直接读取服务器响应的data数据
						}
					}
				});
		
		var groupStore = Ext.create('Ext.data.Store', 
				{
					// 指定读取数据的name、id字段
					fields: ['VALUE','TEXT'],
					proxy: 
					{
						type: 'ajax',
						url: 'queryGroupListForSearch',// 向该URL发送Ajax请求
						reader: { // 使用Ext.data.reader.Json读取服务器数据
							type: 'json',
							root: 'data' // 直接读取服务器响应的data数据
						}
					}
				});
		var ds = Ext.create('Ext.data.Store', {
			fields: ['roleId','roleName'],
			proxy: {
				type: 'ajax',
				url: 'user/viewRoleList',
				reader: {
					type:'json',
					root:'data'
				}
			},
			autoLoad: true,
			sortInfo: {
				field: 'roleId',
				direction: 'ASC'
			}

		});

		var searchForm = Ext.create('Ext.form.Panel', {
	//		title: '订单查询',
			bodyPadding: 5,
			width: '100%', // 指定表单宽度
			// 当提交表单时自动提交Ajax请求。
			//url: 'fillUserParames',
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

			dockedItems: [{
			    xtype: 'toolbar',
			    dock: 'bottom',
			    ui: 'footer',
			    items: [
			        { 
			          xtype: 'button', 
			          text: ' 新增用户信息 ',
			          handler: function(){
						  showWin();
					  }
			        }
			    ]
			
			}
			]
		});

		function showWin(record){
			//			        	  Ext.Ajax.request({
//				        	    url: 'UserAdd',
//				        	    params: {
//				        	        orderId:'11258393681'
//				        	    },
//				        	    success: function(response){
//				        	        var text = Ext.JSON.decode(response.responseText);;
//				        	        console.log(text);
//				        	        console.log(text.success);
//				        	        if(text.success){
//				        	        	Ext.Msg.alert("成功",text.msg);
//				        	        }else{
//				        	        	Ext.Msg.alert("失败",text.msg);
//				        	        }
//				        	    },
//								failure:function(response,options){
//									Ext.Msg.alert(response.status);
//								}
//				        	});
			//2.定义一个window.Window
			var editFormPanel = Ext.create('Ext.form.Panel',{
				url: 'user/addUser',
				frame: true,
				height: '100%',
				width: 640,
				bodyPadding: 5,

				fieldDefaults: {
					labelAlign: 'left',
					labelWidth: 120,
					anchor: '100%'
				},
				layout:'form',

				items: [
					{
						xtype: 'textfield',
						name: 'userName',
						fieldLabel: '用户名*',
						allowBlank: false // 输入校验：不允许为空
					},
					{
						xtype: 'textfield',
						inputType: 'password',
						name:'userPassword',
						//name: 'userNames',
						fieldLabel: '密码*',
						allowBlank: false // 输入校验：不允许为空
					},
					{
						xtype: 'textfield',
						inputType: 'password',
						name: 'userPassWord',
						fieldLabel: '确认密码*',
						allowBlank: false ,// 输入校验：不允许为空
						validator: function(value) {
							var password = this.previousSibling('[name=userPassword]');
							return (value === password.getValue()) ? true : '密码不匹配!';
						}
					},
					/*			  {
					 name: 'groupIds',
					 fieldLabel: '用户运营组*',
					 allowBlank: false ,// 输入校验：不允许为空
					 xtype: 'combobox',
					 //					store: groupStore,
					 valueField: 'VALUE',
					 id:'groupIds_',
					 displayField: 'TEXT',
					 typeAhead: true,
					 emptyText: '请选择用户运营组...'
					 },*/
					{
						xtype: 'combobox',
						name:'userGrade',
						id:'userGrade_',
						fieldLabel: '用户级别*',
//					store: userGradeStore, // 使用bookStore数据提供下拉列表项的数据
						valueField: 'dictKey',
						displayField: 'dictValue',
						allowBlank: false ,// 输入校验：不允许为空
						editable : false,//可否允许输入
						//emptyText: '请选择用户级别...'
						valueNotFoundText:'1级',
						value:'1'
					},
					{
						xtype: 'textfield',
						name: 'userRealName',
						fieldLabel: '姓名'
					},
                    {
                        xtype: 'combobox',
                        name:'userSchool',
                        id:'userSchool',
                        fieldLabel: '用户所属学校*',
				        store: userSchoolStore, // 使用bookStore数据提供下拉列表项的数据
                        valueField: 'id',
                        displayField: 'name',
                        allowBlank: false ,// 输入校验：不允许为空
                        editable : false,//可否允许输入
                        forceSelection : true,
                        typeAhead : true,
                        emptyText:'请选择..',

                        triggerAction:'all',
                        mode:"local"
                    },
					{
						xtype: 'textfield',
						name: 'email',
						regex : /^(\w-*\.*)+@(\w-?)+(\.\w{2,})+$/, //正则表达式在/...../之间. [\一-\龥] : 只能输入中文.
						regexText:'邮箱格式不正确', //正则表达式错误提示
						fieldLabel: '邮箱'
					},
					{
						xtype: 'numberfield',
						name: 'phone',
						minValue: 10000000000,
						maxValue: 99999999999,
						fieldLabel: '手机号'
					},
					{
						xtype: 'textareafield',
						name: 'userDescription',
						fieldLabel: '描述'
					},
					{
						xtype: 'itemselector',
						name: 'userRoleIds',
						id: 'userRoleIds_',
						anchor: '100%',
						fieldLabel: '用户-角色关联*',
						store:ds,
						buttons: [ "add", "remove"],
						displayField: 'roleName',
						valueField: 'roleId',
						msgTarget: 'side',
						fromTitle: '所有角色',
						toTitle: '关联角色',
						minHeight: 100,
						maxHeight: 100,
						allowBlank: false // 输入校验：不允许为空
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
							var record=form.getRecord();
							console.log(record);
							var values=form.getFieldValues();
							console.log(values);
//										  record.set(values);

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
//											window.opener.refreshGrid();//父页面gird刷新显示
												grid.getStore().reload();
												win.close();
											}

										});
										this.disabled = false;
									},
									failure: function(form, action) {
										Ext.Msg.show({
											title:'操作失败',
											msg: action.result.msg,
											buttons: Ext.Msg.YES,
											icon: Ext.MessageBox.QUESTION,
											closable:false,
											fn: function(){
//											window.opener.refreshGrid();//父页面gird刷新显示
												grid.getStore().load();
												win.close();
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
							grid.getStore().load();
							win.close();
						}
					}
				]
			});
			if(record!=null||record!=undefined){
			    var form=editFormPanel.getForm();
				var userPassWord=form.findField('userPassWord');
				userPassWord.setVisible(false);
				editFormPanel.loadRecord(record);

			}
			var win;
			if(!win) {
				var win = Ext.create('Ext.window.Window', {
					title: '新增用户',
					height: 500,
					width: 640,
					modal: true,
					//frame: true,
					hidden: false,
					layout: 'fit',
					closable: true,
					draggable: true,
					floating: true,
					renderTo: Ext.getBody(),
					//animateTarget:this,
					items: [editFormPanel]

				});
			}
			win.show();
		}
		
		
		
		Ext.define('User', {
			extend: 'Ext.data.Model',
			fields: [
                {name: 'userId' , type: 'string'},
				{name: 'userName' , type: 'string'},
				{name: 'userPassword' , type: 'string'},
				{name: 'userState', type: 'string'},
				{name: 'userRoleIds', type:'string'} ,
				{name: 'userRoleNames', type: 'string'},
			]
		});
		
		// 创建一个Ext.data.Store对象
		 UserStore = Ext.create('Ext.data.Store', 
		{
			model: 'User',
			pageSize: 15, // 设置每页显示15条记录
			// 使用proxy指定加载远程数据
			proxy: 
			{
				type: 'ajax',
				url: 'user/viewUserList',// 向该URL发送Ajax请求
				reader: { // 使用Ext.data.reader.Json读取服务器数据
					type: 'json',
					totalProperty: 'total',
					root: 'data' // 直接读取服务器响应的data数据
				}
			},
			autoLoad:true
		});
		
		
		 UserStore.on('beforeload', function (store, options) {
//				var orderId = Ext.getCmp('orderId_').getValue(); //获取文本框值
//				var startTime = Ext.getCmp('startTime_').getValue(); //获取文本框值
//				var endTime = Ext.getCmp('endTime_').getValue(); //获取文本框值
			/* 	var roleId = Ext.getCmp('roleId_').getValue(); //获取文本框值
				var groupId = Ext.getCmp('groupId_').getValue(); //获取文本框值
				var UserRealName = Ext.getCmp('UserRealName_').getValue(); //获取文本框值
				var lockedState = Ext.getCmp('lockedState_').getValue(); //获取文本框值
				var UserGrade = Ext.getCmp('UserGrade_').getValue(); //获取文本框值 */
				var new_params = {start: 0, limit: 15};
				Ext.apply(store.proxy.extraParams, new_params);
				});
		
		
		 grid=Ext.create('Ext.grid.Panel', {
			title: '用户列表',
		//	width: 550, // 指定表单宽度
			renderTo: Ext.getBody(),
             tbar:[searchForm],
			viewConfig:{  
				   enableTextSelection:true  
				},
			// 定义该表格包含的所有数据列
			columns: [
	            { text: '用户名', dataIndex: 'userName',flex: 1},
				{ text: '密码', dataIndex: 'userPassword',flex: 1},
				{ text: '确认密码', dataIndex: 'userPassWord',flex: 1},
				{ text: '用户状态', dataIndex: 'userState' , flex: 1},
				{ text: '角色', dataIndex: 'userRoleNames',flex: 1},
                { text: '操作', dataIndex: 'userId' ,renderer:editTo ,width:150 ,resizable:true}
			],
			store: UserStore,
			 /*  listeners: {
		            'selectionchange': function(view, records) {
		                Ext.alert('1111');
		            }
		       }, */
			// 定义分页工具条
			bbar:{
				xtype: 'pagingtoolbar',
				store: UserStore, 
				displayInfo: true,
				refreshText:'aa'
			}
		});

	});
		
			
	</script>
</body>

</html>