<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>
<html>
<head>
    <title>--Tree测试--</title>

    <link rel="stylesheet" href="<%=path%>/css/main.css">

    <!-- Include Twitter Bootstrap and jQuery: -->
    <link rel="stylesheet" href="<%=path%>/bootstrap/bootstrap.min.css" type="text/css"/>
    <script type="text/javascript" src="<%=path%>/bootstrap/jquery.min.js"></script>
    <script type="text/javascript" src="<%=path%>/bootstrap/bootstrap.min.js"></script>
    <script type="text/javascript" src="<%=path%>/bootstrap/treeView/bootstrap-treeview-master.js"></script>
    <link rel="stylesheet" href="<%=path%>/bootstrap/treeView/bootstrap-treeview.min.css" type="text/css"/>
    <link rel="stylesheet" href="<%=path%>/bootstrap/dialog/bootstrap-dialog.min.css" type="text/css"/>
    <script type="text/javascript" src="<%=path%>/bootstrap/dialog/bootstrap-dialog.min.js"></script>
    <!-- Include the plugin's CSS and JS: -->
    <script type="text/javascript" src="<%=path%>/bootstrap/bootstrap-multiselect.js"></script>
    <link rel="stylesheet" href="<%=path%>/bootstrap/bootstrap-multiselect.css" type="text/css"/>

    <script type="text/javascript" src="<%=path%>/bootstrap/bootstrap-select.js"></script>
    <link rel="stylesheet" href="<%=path%>/bootstrap/bootstrap-select.css" type="text/css"/>

    <link href="<%=path%>/bootstrap/font-awesome.min.css" rel="stylesheet">

    <script type="text/javascript" src="<%=path%>/bootstrap/moment.js"></script>


    <link rel="stylesheet" href="<%=path%>/bootstrap/bootstrap-table.css">
    <script src="<%=path%>/bootstrap/bootstrap-table.js"></script>
    <script src="<%=path%>/bootstrap/bootstrap-table-zh-CN.js"></script>


    <link rel="stylesheet" type="text/css" media="all" href="<%=path%>/bootstrap/bootstrap-editable.css"/>
    <script src="<%=path%>/bootstrap/bootstrap-editable.min.js"></script>
    <script src="<%=path%>/bootstrap/bootstrap-table-editable.js"></script>

    <style>
        .list-group .list-group-item {
            white-space: nowrap;
            word-break: break-all;
            text-overflow: ellipsis;
            overflow: hidden;
        }
    </style>
</head>
<script type="text/javascript">
    function indexFormatter(value, row, index) {
        return index + 1;
    }
    function buildTree(parentNode, datas) {
//        console.log('parentnode', parentNode);
        var iconStr = '';
        for (var key in datas) {
            var data = datas[key];
            if (data.parentid == parentNode.id) {

                console.log('错误率：', data.grasping);
                if (data.grasping == 0) {
                    iconStr = 'glyphicon glyphicon-ban-circle';
                } else if (data.grasping <= 0.1) {
                    iconStr = 'glyphicon glyphicon-ok-circle';
                } else if (0.1 < data.grasping && data.grasping <= 0.2) {
                    iconStr = 'glyphicon glyphicon-thumbs-up';
                } else if (0.2 < data.grasping && data.grasping <= 0.4) {
                    iconStr = 'glyphicon glyphicon-adjust';
                } else if (data.grasping > 0.4) {
                    iconStr = 'glyphicon glyphicon-thumbs-down';
                }

                console.log('icon', iconStr);
                var node = {

//                    icon:"glyphicon glyphicon-certificate",
                    icon: iconStr,
                    text: data.knowledgeText,
                    id: data.id,
                    nodes: [],
                    selectable: true
                };
                parentNode.nodes.push(node);
                buildTree(node, datas);
            }
        }

        if (parentNode.nodes.length == 0) {
            delete parentNode.nodes;
        }
    }

    function getTree(strid) {
//        console.log('dsdsfsfsfsd', strid);
        var params = {};
        params.rootID = strid;
        params.studentID = $('#studentID').val();
//        params.id=strid;
        $.ajax({
            url: "/knowledge/getTreeWithGrasping", // 请求的URL
//            url: "/knowledge/getTree", // 请求的URL
            dataType: 'json',
            type: "get",
            data: params,
            success: function (data) {
                console.log(data.data);
//                var tree = {text: 'root', id: 1, nodes: []};
                var str = $('#gradeno').find('option:selected').text() + $('#subjectno').find('option:selected').text();


                var tree = {
                    text: str,
                    id: strid,
                    nodes: []
                };
                buildTree(tree, data.data);
                $('#knowledgeTree').treeview({
                    color: "#428bca",
                    data: [tree],
//                    showCheckbox: true,
                    onNodeSelected: function (event, mdata) {
                        getQuestions(mdata.id);
                    }
                });


            }
        });
    }

    function getQuestions(id) {
        var params = {};
        params.knowledgeID = id;
        params.studentID = $('#studentID').val();
        $("#ds_table").bootstrapTable('destroy');
        $("#ds_table").bootstrapTable({
            queryParams: params,
            url: "/wrongKnowledge/getWrongKnowledges", // 请求的URL
            dataType: 'json',
            type: "get",
            data: params,
            striped: true,
            clickToSelect: true,
            pagination: true, //是否显示分页（*）
            sidePagination: "server", //分页方式：client客户端分页，server服务端分页（*）
            pageNumber: 1, //初始化加载第一页，默认第一页
            pageSize: 10, //每页的记录行数（*）
            columns: [{
                field: "index",
                title: "序号",
                align: "center",
                formatter:"indexFormatter"
            }, {
                field: "question",
                title: "历史错题",
                align: "center"
            }]
        })
    }

    function getOrCreateRoot() {
        var params = {};

        params.gradeno = $('#gradeno').val();
        params.subjectno = $('#subjectno').val();

        $.ajax({

            url: "/knowledge/getOrCreatRoot",

// 数据发送方式
            type: "post",
// 接受数据格式
            dataType: "json",
// 要传递的数据
            data: params,
// 回调函数，接受服务器端返回给客户端的值，即result值
            success: function (data) {//调用url成功时有效
//                console.log('ddddfff', data);
                getTree(data.data.id);

                $("#ds_table").bootstrapTable('destroy');

            },

            error: function (data) {//仅调用url失败时有效

                alert("新增失败" + data.msg);

            }
        })
    }

    function queryEgradeListBYschool() {//年级下拉列表加载

        $.ajax({
            url: "/egrade/viewEgradeListByschoolno",
// 数据发送方式
            type: "post",
// 接受数据格式
            dataType: "json",

// 回调函数，接受服务器端返回给客户端的值，即result值
            success: function (data) {
                $('#gradeno').empty();

//                $('#gradeno').append("<option value='noselected'>请选择年级</option>");
                $.each(data.data, function (i) {

                    $('#gradeno.selectpicker').append("<option value=" + data.data[i].gradeno + ">" + data.data[i].gradename + "</option>");

                });

                $('#gradeno').selectpicker('refresh');
                getOrCreateRoot();
                getEclassList();
            },

            error: function (data) {

                alert("查询年级失败" + data);

            }
        })
    }

    function getStudentList() {

        var params = {};
        params.classno = $('#classno').val();
        $.ajax({
            url: "/estudent/viewEstudentByDTO",
// 数据发送方式
            type: "get",
// 接受数据格式
            dataType: "json",
// 要传递的数据
            data: params,
// 回调函数，接受服务器端返回给客户端的值，即result值
            success: function (data) {
                $('#studentID').empty();

                $('#classno').selectpicker();
//                console.log(data.data);
                $.each(data.data, function (i) {

                    $('#studentID.selectpicker').append("<option value=" + data.data[i].id + ">" + data.data[i].studentname + "</option>");


                });
                if (params.classno != 'noselected') {
                    $('#studentID').selectpicker('refresh');
                } else {
//                    $('#classno').selectpicker('destroy');
                    $('#studentID').val('noselected');
                }
            },

            error: function (data) {

                alert("查询班级失败" + data);

            }
        })
    }

    function getsubjectList() {//获取下拉学科列表
        $.ajax({
            url: "/esubjects/viewEsubjectsList",
// 数据发送方式
            type: "get",
// 接受数据格式
            dataType: "json",
// 要传递的数据
            data: 'data',
// 回调函数，接受服务器端返回给客户端的值，即result值
            success: function (data) {
//                console.log(data);
//            $('#subjectno.selectpicker').empty();
                $.each(data.data, function (i) {

                    $('#subjectno.selectpicker').append("<option value=" + data.data[i].subjectno + ">" + data.data[i].subjectname + "</option>");
                });

                $('#subjectno').selectpicker('refresh');
                queryEgradeListBYschool();
            },

            error: function (data) {

                alert("查询学科失败" + data);

            }
        })
    }

    function getEclassList() {

        var params = {};
        params.gradeno = $('#gradeno').val();
        $.ajax({
            url: "/eclass/viewEclassByDTO",
// 数据发送方式
            type: "post",
// 接受数据格式
            dataType: "json",
// 要传递的数据
            data: params,
// 回调函数，接受服务器端返回给客户端的值，即result值
            success: function (data) {
                $('#classno').empty();

                $('#classno').selectpicker();
                $('#classno').append("<option value='noselected'>请选择班级</option>");
//                console.log(data.data);
                $.each(data.data, function (i) {

                    $('#classno.selectpicker').append("<option value=" + data.data[i].classno + ">" + data.data[i].classname + "</option>");


                });
                if (params.gradeno != 'noselected') {
                    $('#classno').selectpicker('refresh');
                } else {
//                    $('#classno').selectpicker('destroy');
                    $('#classno').val('noselected');
                }
                getStudentList();

            },

            error: function (data) {

                alert("查询班级失败" + data);

            }
        })
    }

    $(function () {
        getsubjectList();


    })
</script>
<body>

<%--<!-- 模态框（Modal） -->--%>
<%--<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">--%>
<%--<div class="modal-dialog">--%>
<%--<div class="modal-content">--%>
<%--<div class="modal-header">--%>
<%--<button type="button" class="close" data-dismiss="modal" aria-hidden="true">--%>
<%--&times;--%>
<%--</button>--%>
<%--<h4 class="modal-title" id="myModalLabel">--%>
<%--添加知识点--%>
<%--</h4>--%>
<%--</div>--%>
<%--<div class="modal-body">--%>
<%--知识点：<input id="mknowledge" name="mknowledge" class="form-control"></input>--%>
<%--</div>--%>
<%--<div class="modal-footer">--%>

<%--<button class="btn btn-primary" type="button" onclick="addKnowledge();">--%>
<%--<span class="glyphicon glyphicon-floppy-save"></span> 保存--%>
<%--</button>--%>
<%--<button class="btn btn-primary" type="button" data-dismiss="modal">--%>
<%--<span class="glyphicon glyphicon-log-out"></span> 关闭--%>
<%--</button>--%>
<%--</div>--%>
<%--</div><!-- /.modal-content -->--%>
<%--</div><!-- /.modal -->--%>
<%--</div>--%>
<%--<!--endModal-->--%>
<div>
    <%--<div><input id="test" placeholder="ccc" class="form-control"/></div>--%>
    <div style="padding:8px">
        <p>图例说明：</p>
        <span class="glyphicon glyphicon-ban-circle" style="color: rgb(168, 34, 209);"> 未知，无数据</span>
        <span class="glyphicon glyphicon-ok-circle" style="color: rgb(168, 34, 209);"> 熟练掌握,错误率<10%</span>
        <span class="glyphicon glyphicon-thumbs-up" style="color: rgb(168, 34, 209);"> 掌握，错误率<20%</span>
        <span class="glyphicon glyphicon-adjust" style="color: rgb(168, 34, 209);"> 及格，错误率<40%</span>
        <span class="glyphicon glyphicon-thumbs-down" style="color: rgb(168, 34, 209);"> 不及格，错误率>40%</span>
    </div>
    <div style="padding:8px">
        <div style="float: left;width:40%;padding: 6px;">
            <div style="padding: 6px;">

                <select id="gradeno" name="gradeno" class="selectpicker fit-width" onchange="getEclassList()">
                </select>
                <select id="classno" name="classno" class="selectpicker fit-width" onchange="getStudentList()">

                </select>
                <select id="studentID" name="studentID" class="selectpicker show-tick fit-width"
                        data-live-search="true">

                </select>

                <select id="subjectno" name="subjectno" class="selectpicker fit-width">
                </select>
                <button class="btn btn-danger" type="button" onclick="getOrCreateRoot();">
                    <span class="glyphicon glyphicon-eye-open"></span> 查询
                </button>
            </div>
            <div id="knowledgeTree" style="padding: 6px;">
            </div>
        </div>
        <div id="grid" style="float:right;width: 60%;padding: 6px;">
            <div id="forTable" style="float: right;padding: 6px;">

            </div>
            <div style="padding: 6px;">
                <table id="ds_table"></table>
            </div>
        </div>

    </div>
</div>
</div>
</body>
</html>
