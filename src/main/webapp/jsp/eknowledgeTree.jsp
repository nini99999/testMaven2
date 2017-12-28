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

    function buildTree(parentNode, datas) {
        console.log('parentnode',parentNode);
        for (var key in datas) {
            var data = datas[key];
            if (data.parentid == parentNode.id) {
                var node = {text: data.knowledgeText, id: data.id, nodes: [], selectable: true};
                parentNode.nodes.push(node);
                buildTree(node, datas);
            }
        }

        if (parentNode.nodes.length == 0) {
            delete parentNode.nodes;
        }
    }

    function getTree(strid) {
        var params = {};
        params.id = strid;
        $.ajax({
            url: "/knowledge/getTree", // 请求的URL
            dataType: 'json',
            type: "get",
            data: params,
            success: function (data) {
//                var tree = {text: 'root', id: 1, nodes: []};
                var str = $('#gradeno').find('option:selected').text() + $('#subjectno').find('option:selected').text();

                console.log('nnn', str);
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
                        getKnowledgeByID(mdata.id);
                        $('#add').removeAttr("disabled");
                        $('#del').removeAttr("disabled");
                    }
                });


            }
        });
    }

    function getKnowledgeByID(id) {
        var params = {};
        params.id = id;
        $("#ds_table").bootstrapTable('destroy');
        $("#ds_table").bootstrapTable({
            queryParams: params,
            url: "/knowledge/getKnowledge", // 请求的URL
            dataType: 'json',
            type: "get",
            data: params,
            striped: true,
            clickToSelect: true,
            columns: [{
                field: "knowledgeText",
                title: "知识点（点击可编辑）",
                align: "center",
                editable: {
                    validate: function (str) {
                        if (str.length == 0) return '不能为空';
                    }
                }
            }],
            onEditableSave: function (field, row, oldValue, $el) {
                console.log(row);
                var params = {};
                params.id = row.id;
                params.knowledgeText = row.knowledgeText;
                $.ajax({
                    type: "post",
                    url: "/knowledge/saveKnowledgeText",
                    data: params,
                    dataType: 'JSON',
                    success: function (data) {
                        var node = $('#knowledgeTree').treeview('getSelected');
                        var newNode = {
                            text: row.knowledgeText,
                            id: row.id
                        };

                        $('#knowledgeTree').treeview('updateNode', [node, newNode]);
                    },
                    error: function (data) {
                        alert('保存失败' + data.msg);
                    },
                    complete: function () {
                    }

                });
            }

        })
    }

    function addKnowledge() {
        var parentNode = $('#knowledgeTree').treeview('getSelected');
        console.log(parentNode);
        var params = {};

        params.gradeno = $('#gradeno').val();
        params.subjectno = $('#subjectno').val();
        params.parentid = parentNode[0].id;
        params.knowledgeText = mknowledge.value;

        $.ajax({

            url: "/knowledge/addKnowledge",

// 数据发送方式
            type: "post",
// 接受数据格式
            dataType: "json",
// 要传递的数据
            data: params,
// 回调函数，接受服务器端返回给客户端的值，即result值
            success: function (data) {//调用url成功时有效
                var node = {
                        id: data.data.id,
                        text: data.data.knowledgeText
                    }
                ;
                $('#knowledgeTree').treeview('addNode', [node, parentNode]);
            },

            error: function (data) {//仅调用url失败时有效

                alert("新增失败" + data.msg);

            }
        })
    }

    function delKnowledge() {
        var node = $('#knowledgeTree').treeview('getSelected');
        BootstrapDialog.confirm({
            title: '重要提示：',
            message: '本操作将删除当前节点及下属所有节点，确认删除？',
//            type: BootstrapDialog.TYPE_WARNING,
            closable: true,
            btnCancelLabel: '取消',
            btnOKLabel: '确定',
            callback: function (result) {
                if (result) {
                    del();
                    $('#add').attr("disabled",true);
                    $('#del').attr("disabled",true);
                    $("#ds_table").bootstrapTable('destroy');
                }
            }
        });

        function del() {
            var params = {};
            params.id = node[0].id;
            $.ajax({

                url: "/knowledge/delKnowledge",

// 数据发送方式
                type: "post",
// 接受数据格式
                dataType: "json",
// 要传递的数据
                data: params,
// 回调函数，接受服务器端返回给客户端的值，即result值
                success: function (data) {//调用url成功时有效

//                $('#knowledgeTree').treeview('addNode', [node, parentNode]);
                    $('#knowledgeTree').treeview('removeNode', [node, {silent: true}]);
                },

                error: function (data) {//仅调用url失败时有效
                    alert("删除失败");
                }
            })
        }
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
                console.log('ddddfff',data);
             getTree(data.data.id);
                $('#add').attr("disabled",true);
                $('#del').attr("disabled",true);
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

            },

            error: function (data) {

                alert("查询年级失败" + data);

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

    $(function () {
        getsubjectList();


    })
</script>
<body>

<!-- 模态框（Modal） -->
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                    &times;
                </button>
                <h4 class="modal-title" id="myModalLabel">
                    添加知识点
                </h4>
            </div>
            <div class="modal-body">
                知识点：<input id="mknowledge" name="mknowledge" class="form-control"></input>
            </div>
            <div class="modal-footer">

                <button class="btn btn-primary" type="button" onclick="addKnowledge();">
                    <span class="glyphicon glyphicon-floppy-save"></span> 保存
                </button>
                <button class="btn btn-primary" type="button" data-dismiss="modal">
                    <span class="glyphicon glyphicon-log-out"></span> 关闭
                </button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal -->
</div>
<!--endModal-->
<div>
    <%--<div><input id="test" placeholder="ccc" class="form-control"/></div>--%>
    <div style="padding:8px">
        <div style="float: left;width:40%;padding: 6px;">
            <div style="padding: 6px;">
                <label for="gradeno"> <font color="#00008b">年级：</font> </label>
                <select id="gradeno" name="gradeno" class="selectpicker fit-width">
                </select>
                <label for="subjectno"><font color="#00008b"> 学科：</font></label>
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
                <button id="add" class="btn btn-primary" type="button" data-toggle="modal" data-target="#myModal"
                        disabled>
                    <span class="glyphicon glyphicon-plus-sign"></span> 新增
                </button>
                <button id="del" class="btn btn-primary" type="button" onclick="delKnowledge();" disabled>
                    <span class="glyphicon glyphicon-minus-sign"></span> 删除
                </button>
            </div>
            <div style="padding: 6px;">
                <table id="ds_table" data-height="300"></table>
            </div>
        </div>

    </div>
</div>
</div>
</body>
</html>
