	var tree = new Ext.tree.TreePanel({
		title : '主菜单',
		width : 200,
		autoScroll : true,
		singleExpand : true,
		rootVisible : true,
		animate : true,
		//树加载器
		loader : new Ext.tree.TreeLoader({
					dataUrl : 'tree/treeNodeAction_listTree.action'
				})
	});
	
	
	
		