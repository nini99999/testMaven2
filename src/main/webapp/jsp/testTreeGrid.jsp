<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="<%=path%>/bootstrap/bootstrap.min.css" type="text/css"/>
    <link href="<%=path%>/bootstrap/treeGrid/css/jquery.treegrid.css" rel="stylesheet"/>
    <script type="text/javascript" src="<%=path%>/bootstrap/jquery.min.js"></script>
    <%--<script type="text/javascript" src="<%=path%>/jquery/jquery-1.10.2.min.js"></script>--%>
    <script type="text/javascript" src="<%=path%>/bootstrap/bootstrap.min.js"></script>

    <script src="<%=path%>/bootstrap/treeGrid/js/jquery.treegrid.js"></script>
    <script src="<%=path%>/bootstrap/treeGrid/js/jquery.treegrid.bootstrap3.js"></script>
    <script src="<%=path%>/bootstrap/treeGrid/extension/extension.js"></script>
    <script src="<%=path%>/bootstrap/bootstrap-table.js"></script>
    <script src="<%=path%>/bootstrap/bootstrap-table-zh-CN.js"></script>
</head>
<script type="text/javascript">
    $(function () {
        $.ajax({
            url: "/knowledge/testTree",
// 数据发送方式
            type: "post",
// 接受数据格式
            dataType: "json",
// 要传递的数据
            data: 'data',
// 回调函数，接受服务器端返回给客户端的值，即result值
            success: function (data) {

               createTree(data.data);

            },

            error: function (data) {

                alert("查询失败" + data);

            }
        })
    });
    function createTree(data) {
        $('#tb').treegridData({
            id: 'id',
            parentColumn: 'parentid',
            type: "GET", //请求数据的ajax类型
            url: '/knowledge/testTree',   //请求数据的ajax的url
            data:data,
            ajaxParams: {}, //请求数据的ajax的data属性
            expandColumn: null,//在哪一列上面显示展开按钮
            striped: true,   //是否各行渐变色
            bordered: true,  //是否显示边框
            //expandAll: false,  //是否全部展开
            columns: [
                {
                    title: '知识点',
                    field: 'knowledgeText'
                },
                {
                    title: '父节点',
                    field: 'parentid'
                }
            ]
        });
    }
</script>
<body>
<table id="tb"></table>
</body>
</html>
