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
<script type="text/javascript">


    function indexFormatter(value, row, index) {
        return index + 1;
    }
    function operateFormatter(value, row, index)
//row 获取这行的值 ，index 获取索引值
    {

        return [

            '<a class="edit"  href="javascript:void(0)" title="edit">',
            '<i class="glyphicon glyphicon-edit"></i>',
            '</a>'
        ].join('');
    }
    window.operateEvent = {
        'click .edit': function (e, value, row, index) {
            //修改操作
//            alert('You click like action, row: ' + JSON.stringify(row));
//            console.log(row);
            $('#subjectno').selectpicker('val', (row.subjectno));
            $("#myModal").modal('show');
            $('#sid').val(row.id);
            $('#questiontype').val(row.questiontype);
            $('#questiontypename').val(row.questiontypename);

        }
    }
    function openAddModal() {
        $("#myModal").modal('show');
        $('#sid').val(null);
        $('#questiontype').val(null);
        $('#questiontypename').val(null);
    }

    function getEsubjects() {//获取下拉学科列表
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

                $.each(data.data, function (i) {

                    $('#subjectno.selectpicker').append("<option value=" + data.data[i].subjectno + ">" + data.data[i].subjectname + "</option>");

                });

                $('#subjectno').selectpicker('refresh');

            },

            error: function (data) {

                alert("查询学科失败" + data);

            }
        })

    }


    function queryEsubjectQT() {
        var params = {};

        params.subjectno = $('#subjectno').val();
        $.ajax({
            url: "/esubjectqt/viewEsubjectQT",
// 数据发送方式
            type: "post",
// 接受数据格式
            dataType: "json",
// 要传递的数据
            data: params,
// 回调函数，接受服务器端返回给客户端的值，即result值
            success: function (data) {

//alert(data.total);
                //$("#ds_table").bootstrapTable('refresh', data);//刷新ds_table的数据
                $("#ds_table").bootstrapTable('destroy');
                $("#ds_table").bootstrapTable({data: data.data});//刷新ds_table的数据
//                $("#ds_table").bootstrapTable('refresh', data.data);
            },

            error: function (data) {

                alert("查询失败" + data);

            }
        })
    }
    function addEsubjectQT() {
        var params = {};

        params.subjectno = $('#subjectno').val();
        params.questiontype = questiontype.value;
        params.questiontypename = questiontypename.value;
        params.id = sid.value;
        $.ajax({

            url: "/esubjectqt/addEsubjectQT",

// 数据发送方式
            type: "post",
// 接受数据格式
            dataType: "json",
// 要传递的数据
            data: params,
// 回调函数，接受服务器端返回给客户端的值，即result值
            success: function (data) {//调用url成功时有效
                if (data.success == true) {

                    queryEsubjectQT();

                    $("#questiontype").val("");
                    $("#questiontypename").val("");//重置输入框为空
                    alert("保存成功！");

                } else {
                    alert(data.msg);
                }

            },

            error: function (data) {//仅调用url失败时有效

                alert("保存失败" + msg);

            }
        })
    }
    function delEsubjectQT() {
        var selects = $("#ds_table").bootstrapTable('getSelections');
//console.log(selects);
        $.ajax({

            url: "/esubjectqt/delEsubjectQT",
// 数据发送方式
            type: "post",
// 接受数据格式
            dataType: "json",
            contentType: 'application/json',
// 要传递的数据
            data: JSON.stringify(selects),
// 回调函数，接受服务器端返回给客户端的值，即result值
            success: function (data) {//调用url成功时有效
                if (data.success == true) {
                    queryEsubjectQT();
                    alert("记录已删除！");
                } else {
                    alert(data.msg);
                }

            },

            error: function (data) {//仅调用url失败时有效

                alert("删除失败" + JSON.stringify(data));

            }
        })
    }
</script>

<body>
<div id="bs_toolbar">
    选择学科--：
    <select id="subjectno" name="subjectno" class="selectpicker">

    </select>

    <button class="btn btn-primary" type="button" onclick="queryEsubjectQT();">
        <span class="glyphicon glyphicon-eye-open"></span> 查询题型
    </button>
    <!-- 按钮触发模态框 -->
    <button class="btn btn-primary" type="button" onclick="openAddModal();">
        <span class="glyphicon glyphicon-plus-sign"></span> 添加记录
    </button>
    <button class="btn btn-primary" type="button" onclick="delEsubjectQT();">
        <span class="glyphicon glyphicon-minus-sign"></span> 删除选中
    </button>


</div>

<!-- 模态框（Modal） -->
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                    &times;
                </button>
                <h4 class="modal-title" id="myModalLabel">
                    题型维护
                </h4>
            </div>
            <div class="modal-body">

                <hr/>


                <input id="sid" name="sid" class="btn-default" hidden></input>
                题型编码：
                <input id="questiontype" name="questiontype" class="btn-default"></input>
                题型名称：
                <input id="questiontypename" name="questiontypename" class="btn-default"></input>


            </div>
            <script type="text/javascript">
                $(document).ready(function () {

                    //$('#schoolno').multiselect();
                    getEsubjects();
                    queryEsubjectQT();

                });
            </script>
            <div class="modal-footer">

                <button class="btn btn-primary" type="button" onclick="addEsubjectQT();">
                    <span class="glyphicon glyphicon-floppy-save"></span> 保存
                </button>
                <button class="btn btn-primary" type="button" data-dismiss="modal">
                    <span class="glyphicon glyphicon-log-out"></span> 关闭
                </button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal -->
</div>
<div id="bs_t" style="float: none;display: block;margin-left: auto;margin-right: auto;width: 90%">
    <table class="table table-striped" width="80%" id="ds_table" align="center"
           striped="true"
           data-height="566" data-toolbar="#bs_toolbar"
           data-pagination="true" sidePagination="server"
           data-show-columns="true" data-click-to-select="true">
        <thead>
        <tr>
            <th data-field="estate" data-checkbox="true"></th>
            <th data-field="index" data-align="center" data-formatter="indexFormatter">序号</th>

            <th data-field="subjectname" data-align="center" data-sortable="true">学科名称</th>
            <th data-field="questiontype" data-align="center" data-sortable="true">题型编码</th>
            <th data-field="questiontypename" data-align="center" data-sortable="true">题型名称</th>
            <th data-field="creator" data-align="center">创建人</th>
            <th data-field="createdate" data-align="center">创建日期</th>
            <th data-field="bs_rowid" data-align="center" data-formatter="operateFormatter" data-events="operateEvent">
                编辑
            </th>
        </tr>
        </thead>
    </table>
</div>
</body>
</html>
