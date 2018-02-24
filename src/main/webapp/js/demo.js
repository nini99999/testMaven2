Ext.Loader.setConfig({enabled: true});
Ext.Loader.setPath('Ext.ux', 'extjs/src/ux');
Ext.require([
    'Ext.tab.*',
    'Ext.ux.TabCloseMenu'
]);
window.onload = function () {
    //alert(userRealName.value);
    userName.innerHTML = '&nbsp;&nbsp;' + userRealName.value + '&nbsp;&nbsp;';
    //getUserRealName(userName);
}

//点击退出链接跳转登陆
function loginOut() {
    Ext.MessageBox.confirm("提示", "您确定退出吗？", function (result) {
        if (result == 'yes') {
            window.location = 'j_spring_security_logout';
        }
    });
}

//function getUserRealName(userName){
//	Ext.Ajax.request({
//		url:'user/getUserRealName',
//		method:'POST',
//		success:function(response){
//			var responseText=Ext.decode(response.responseText);
//			userName.innerHTML='欢迎你!&nbsp&nbsp'+responseText.data;
//		},
//		failure:function(response){
//			window.location.href='loginlogout/toLogin';
//		}
//	});
//}

Ext.onReady(function () {
    //数据模型
    Ext.define('menuInfo', {
        extend: 'Ext.data.Model',
        fields: [
            {name: 'id', type: 'string'},
            {name: 'text', type: 'string'},
            {name: 'leaf', type: 'boolean'},
            {name: 'url', type: 'string'},
            {name: 'description', type: 'string'},
            {name: 'expanded', type: 'boolean'}
        ]
    });
    // 创建一个TreeStore，TreeStore负责为Tree提供数据
    var store = Ext.create('Ext.data.TreeStore', {
        model: 'menuInfo',
        proxy: {
            type: 'ajax',
            url: 'menu/toUserMenu'
        },
        listeners: {
            load: function (store, record, records, eOpts) {
                if (records == null) {
                    location.href = "/loginlogout/toLogin";
                    return false;
                }
                console.log('length:' + records.length);
                if (records.length > 0) {
                    for (var i = 0; i < records.length; i++) {
                        if (records[i].data.children[0].leaf) {
                            //var frame = Ext.getDom('mainFrame');
                            //frame.src = '<%=request.getContextPath()%>' + records[i].data.children[0].url;
                            var tabpanel = Ext.getCmp('Main_MasterPage_TabMain');
                            var n = tabpanel.getComponent(record.data.id);
                            if (!n) {
                                n = tabpanel.add({
                                    id: records[i].data.children[0].id,
                                    title: records[i].data.children[0].text,
                                    closable: true,
                                    html: '<iframe id="reporter-iframe" src="' + records[i].data.children[0].url + '" frameborder="0" scrolling="auto"  width="100%" height="100%" style="overflow-y:auto;" ></iframe>'
                                });
                                tabpanel.setActiveTab(n);
                            }
                            return false;//相当于break
                        }
                    }
                }
            }
        }
    });

    Ext.create('Ext.container.Viewport',
        {
            layout: 'border',
            bodyStyle: "background-color:#b0b0b0",
            items: [
                // 上面区域的内容
                {
                    region: 'north',
                    html: '<div style="background: url(img/banner.jpg);background-size: 100%;height: 100%;"><div style="font-family:helvetica, arial, verdana, sans-serif;font-size:13px; position: absolute;top: 50%;transform: translateY(-50%);"><strong><span style="color:#b0b0b0;font-size:16px;font-family:&quot;">&nbsp&nbsp&nbsp&nbsp&nbsp&nbspILeans-精准教学管理系统</span></strong></div><div style="display:inline;float:right;"><a href=javascript:changePassword()  style="color: #b0b0b0">修改密码</a><font color="#b0b0b0">&nbsp&nbsp|&nbsp&nbsp</font><a href=javascript:loginOut()  style="color: #b0b0b0">安全退出</a></div><div style="float:right;" id="clock" ></div><div style="float:right;color: #b0b0b0" id="userName" ></div></div>',
                    // html: '<div style="background: #333f49;background-size: 100%;height: 100%;"><div >LOGO</div><div style="display:inline;float:right;"><a href=javascript:changePassword()  style="color: #bce8f1">修改密码</a><font color="#bce8f1">&nbsp&nbsp|&nbsp&nbsp</font><a href=javascript:loginOut()  style="color: #bce8f1">安全退出</a></div><div style="float:right;" id="clock" ></div><div style="float:right;color: #bce8f1" id="userName" ></div></div>',
                    height: 40,
                    border: false,
                    margins: '0 0 0 0'
                },
                // 左边区域的内容
                {
                    region: 'west',
                    border: false,
                    xtype: 'treepanel', // 表明这是Ext.tree.Panel
                    title: '功能菜单',
                    listeners: {
                        // 为itemclick事件添加监听器
                        itemclick: function (view, record, item) {

                            // 如果是叶子节点
                            if (record.data.leaf) {
                                // 获取页面中my_center组件，该组件是Ext.tab.Panel组件
                                //			var tabPanel = Ext.getCmp('my_center');
                                //var frame = Ext.getDom('mainFrame');
                                // 如果页面上没有该图书id对应的组件
                                //alert(!Ext.getCmp(record.data.id));
                                //if(!Ext.getCmp(record.data.id))
                                //{
                                // 向Ext.tab.Panel组件中插入一个新的Tab页面
                                // 主显示区
                                var tabpanel = Ext.getCmp('Main_MasterPage_TabMain');
                                if (tabpanel.items.getCount() >= 10) {//tab最多限制10个
                                    Ext.Msg.alert('提示', "系统最多允许打开10个tab窗体，请关闭其他窗体再打开！");
                                    return;
                                }
                                var n = tabpanel.getComponent(record.data.id);
                                if (!n) {
                                    n = tabpanel.add({
                                        id: record.data.id,
                                        title: record.data.text,
                                        closable: true,
                                        html: '<iframe id="reporter-iframe" src="' + record.data.url + '" frameborder="0" scrolling="auto"  width="100%" height="100%" style="overflow-y:auto;" ></iframe>'
                                    });
                                    tabpanel.setActiveTab(n);
                                } else {
                                    tabpanel.setActiveTab(n);
                                }
                                // alert(tabpanel.items.getCount());

                                /* var tab = tabPanel.add(
                                 {
                                 // 设置新Tab页面的属性
                                 id:record.data.id,
                                 title: record.data.text,
                                 frame:true,
                                 closable:true,
                                 contentEl:'mainFrame'
                                 //		items:[Cmp]
                                 }
                                 ); */
                                //}
                                //	tab.doLayout();
                                // 激活正在查看的功能页
                                //		tabPanel.setActiveTab(record.data.id);
                                // 向下边区域的Ext.panel.Panel组件中插入内容
                                /* 	Ext.getCmp('info').add({html:'正在进行'
                                 + record.data.text + '操作'}); */
                            }
                        }

                    },
                    width: 200,
                    store: store,
                    rootVisible: false,
                    collapsible: true,
                    split: true

                },
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
                 },
                 */
                // 中间面板的内容：使用一个Ext.tab.Panel
                {
                    region: 'center',
                    style:'padding:0px',
                    id: 'my_center',
                    xtype: 'tabpanel',
                    activeTab: 0,
                    tabMargin:0,
                    frame: false,
                    //contentEl:tabpanel,
                    //items:tabpanel,
                    id: "Main_MasterPage_TabMain",
                    //region: "center",
                    autoScroll: true,
                    enableTabScroll: true//如果Tab过多会出现滚动条
                }

            ]
        });
});