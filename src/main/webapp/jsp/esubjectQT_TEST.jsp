<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>
<html>
<head>
    <title>--题型管理--</title>
    <!-- Include Twitter Bootstrap and jQuery: -->
    <link rel="stylesheet" href="<%=path%>/bootstrap/bootstrap.min.css" type="text/css"/>
    <script type="text/javascript" src="<%=path%>/bootstrap/jquery.min.js"></script>
    <script type="text/javascript" src="<%=path%>/bootstrap/bootstrap.min.js"></script>
    <script type="text/javascript" src="<%=path%>/bootstrap/bootstrap-select.js"></script>
    <link rel="stylesheet" href="<%=path%>/bootstrap/bootstrap-select.css" type="text/css"/>
    <link href="<%=path%>/bootstrap/font-awesome.min.css" rel="stylesheet">

    <script type="text/javascript" src="<%=path%>/bootstrap/moment.js"></script>

    <link rel="stylesheet" href="<%=path%>/bootstrap/bootstrap-table.css">
    <link rel="stylesheet" href="<%=path%>/css/main.css">
    <script src="<%=path%>/bootstrap/bootstrap-table.js"></script>
    <script src="<%=path%>/bootstrap/bootstrap-table-zh-CN.js"></script>


</head>
<title>ttsssdddf</title>
</head>
<script type="text/javascript">
    $(document).ready(function () {
        initTable();
    })
    function initTable() {
        $('#bs_table').bootstrapTable('destroy');
        //初始化Table
        $('#bs_table').bootstrapTable({
            url: '/esubjectqt/viewEsubjectQT',         //请求后台的URL（*）

            method: 'get',                      //请求方式（*）
            toolbar: '#toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
//            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）

//            queryParams: oTableInit.queryParams,//传递参数（*）
            sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
            pageNumber: 1,                       //初始化加载第一页，默认第一页
            pageSize: 10,                       //每页的记录行数（*）
            pageList: [10, 25],        //可供选择的每页的行数（*）

            clickToSelect: true,                //是否启用点击选中行
            height: 500,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
//            uniqueId: "ID",                     //每一行的唯一标识，一般为主键列
            responseHandler: function (res) {
                //远程数据加载之前,处理程序响应数据格式,对象包含的参数: 我们可以对返回的数据格式进行处理
                //在ajax后我们可以在这里进行一些事件的处理
//                console.log(res);
//                datatype:"json";
                var bsdata = {total: res.total, rows: res.rows};
                return bsdata;
            },
//            queryParamsType : "limit",
            queryParams: function queryParams(params) {   //设置查询参数
                var param = {
                    //这里是在ajax发送请求的时候设置一些参数 params有什么东西，自己看看源码就知道了
                    pageNumber: params.offset+1,
                    pageSize: params.limit,
                    subjectno: params.subjectno
                };
                return param;
            },
            onLoadSuccess: function (data) {  //加载成功时执行
//                $('#bs_table').bootstrapTable()
//                alert("加载成功"+data);
//var bsdata={total:data.length,rows:JSON.stringify(data)};
                $('#bs_table').bootstrapTable({data: data});

                console.log(data);
            },
            onLoadError: function (data) {  //加载失败时执行
                alert("cccssss");
            },
            columns: [{
                field: 'estate',
                title: '',
                checkbox: true
            }, {
                field: 'index',
                title: '序号',
                formatter: indexFormatter
            }, {
                field: 'subjectno',
                title: '学科编码'
            }, {
                field: 'questiontype',
                title: '题型编码'
            }, {
                field: 'questiontypename',
                title: '题型名称'
            }, {
                field: 'operate',
                title: '操作',
                align: 'center',
                events: operateEvents,
                formatter: operateFormatter
            }]
        });
    }
    function indexFormatter(value, row, index) {
        return index + 1;
    }
    function operateFormatter(value, row, index) {
        return [
            '<a class="like" href="javascript:void(0)" title="Like">',
            '<i class="glyphicon glyphicon-edit"></i>',
            '</a>  ',
            '<a class="remove" href="javascript:void(0)" title="Remove">',
            '<i class="glyphicon glyphicon-remove"></i>',
            '</a>'
        ].join('')
    }
    window.operateEvents = {
        'click .like': function (e, value, row, index) {
            alert('You click like action, row: ' + JSON.stringify(row));
        },
        'click .remove': function (e, value, row, index) {
            $table.bootstrapTable('remove', {
                field: 'id',
                values: [row.id]
            });
        }
    }
</script>
<body>
<div class="container">
    <table id="bs_table" data-url="/esubjectqt/viewEsubjectQT" data-pagination="true" sidePagination="server">
    </table>
</div>
</body>
</html>
