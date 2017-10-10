<%--
  Created by IntelliJ IDEA.
  User: jenny
  Date: 2017/3/16
  Time: 下午9:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>
<html>
<head>
    <title>--学科管理--</title>
    <!-- Include Twitter Bootstrap and jQuery: -->
    <link rel="stylesheet" href="<%=path%>/bootstrap/bootstrap.min.css" type="text/css"/>
    <script type="text/javascript" src="<%=path%>/bootstrap/jquery.min.js"></script>
    <script type="text/javascript" src="<%=path%>/bootstrap/bootstrap.min.js"></script>


    <script type="text/javascript" src="<%=path%>/bootstrap/moment.js"></script>

    <link rel="stylesheet" href="<%=path%>/bootstrap/bootstrap-table.css">
    <link rel="stylesheet" href="<%=path%>/css/main.css">
    <script src="<%=path%>/bootstrap/bootstrap-table.js"></script>
    <script src="<%=path%>/bootstrap/bootstrap-table-zh-CN.js"></script>

</head>
<body>
<script type="text/javascript">
    function queryEsubjectsList() {


        $.ajax({
            url: "/esubjects/viewEsubjectsList",
// 数据发送方式
            type: "post",
// 接受数据格式
            dataType: "json",
// 要传递的数据
            data: 'data',
// 回调函数，接受服务器端返回给客户端的值，即result值
            success: function (data) {

                $("#ds_table").bootstrapTable('refresh', data);//刷新ds_table的数据

            },

            error: function (data) {

                alert("新增失败");

            }
        })
    }
    function addEsubjects() {
        var params = {};
        params.subjectno = subjectno.value;
        params.subjectname = subjectname.value;
        params.totalscore = totalscore.value;
        // alert(subjectno.value + subjectname.value);
        $.ajax({

            url: "/esubjects/addEsubjects",
// 数据发送方式
            type: "post",
// 接受数据格式
            dataType: "json",
// 要传递的数据
            data: params,
// 回调函数，接受服务器端返回给客户端的值，即result值
            success: function (data) {//调用url成功时有效
                if (data.success == true) {

                    $("#ds_table").bootstrapTable('refresh', data);//刷新ds_table的数据

                    $("#subjectno").val("");
                    $("#subjectname").val("");//重置输入框为空
                    alert("保存成功，请继续添加");

                } else {
                    alert(data.msg);
                }

            },

            error: function (data) {//仅调用url失败时有效

                alert("新增失败");

            }
        })
    }

    function delEsubjects() {
        var selects = $("#ds_table").bootstrapTable('getSelections');
        // alert(JSON.stringify(selects));
        // var newSelects = $.parseJSON(JSON.stringify(selects));
        //alert(JSON.stringify(newSelects));
        $.ajax({

            url: "/esubjects/delsubjects",
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

                    $("#ds_table").bootstrapTable('refresh', data);//刷新ds_table的数据

                } else {
                    alert(data.msg);
                }

            },

            error: function (data) {//仅调用url失败时有效

                alert("删除失败" + JSON.stringify(data));

            }
        })
    }

    function indexFormatter(value, row, index) {
        return index + 1;
    }
    function operateFormatter(value, row, index) {
        //row 获取这行的值 ，index 获取索引值
        var PySheDingID = row.bs_rowid;
// 利用 row 获取点击这行的ID
        return [
            "<button type='button' class='btn btn-info';' id='m-callback-this-start' onclick='btnEntry(" + row.bs_rowid + ")'  > <span class='glyphicon glyphicon-pencil' onclick='queryEsubjectsList()'></span>  </button>"].join('');

    }
</script>
<div class="container">
    <hr/>
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
                    添加学科
                </h4>
            </div>
            <div class="modal-body">
                <label for="subjectno">学科编码：</label>
                <input id="subjectno" name="subjectno" class="form-control"></input>
                <label for="subjectname">学科名称：</label>
                <input id="subjectname" name="subjectname" class="form-control"></input>
                <label for="totalscore">学科总分：</label>
                <input id="totalscore" name="totalscore" class="form-control"></input>

            </div>
            <div class="modal-footer">

                <button class="btn btn-primary" type="button" onclick="addEsubjects();">
                    <span class="glyphicon glyphicon-floppy-save"></span> 保存
                </button>
                <button class="btn btn-primary" type="button" data-dismiss="modal">
                    <span class="glyphicon glyphicon-log-out"></span> 关闭
                </button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal -->
</div>

<div id="bs_toolbar" style="float: none;display: block;margin-left: auto;margin-right: auto">

    <button class="btn btn-primary" type="button" onclick="queryEsubjectsList();">
        <span class="glyphicon glyphicon-eye-open"></span> 查询
    </button>
    <!-- 按钮触发模态框 -->
    <button class="btn btn-primary" type="button" data-toggle="modal" data-target="#myModal">
        <span class="glyphicon glyphicon-plus-sign"></span> 添加
    </button>
    <button class="btn btn-primary" type="button" onclick="delEsubjects();">
        <span class="glyphicon glyphicon-minus-sign"></span> 删除选中
    </button>
</div>
<div id="bs_t" style="float: none;display: block;margin-left: auto;margin-right: auto;width: 90%">
    <table class="table table-striped" id="ds_table" data-url="/esubjects/viewEsubjectsList"
           striped="true"
           data-toggle="table" data-height="566" data-toolbar="#bs_toolbar"
           data-pagination="true" sidePagination="server" data-show-refresh="true" data-show-toggle="true"
           data-show-columns="true" data-search="true" data-click-to-select="true">
        <thead>

        <tr>
            <th data-field="estate" data-checkbox="true"></th>
            <th data-field="index" data-align="center" data-formatter="indexFormatter">序号</th>
            <th data-field="id" data-align="center" data-sortable="true">id</th>
            <th data-field="subjectno" data-align="center" data-sortable="true">学科</th>
            <th data-field="subjectname" data-align="center" data-sortable="true">学科</th>
            <th data-field="totalscore" data-align="center" data-sortable="true">满分</th>
            <th data-field="creator" data-align="center">创建人</th>
            <th data-field="createdate" data-align="center">创建日期</th>
            <th data-field="bs_rowid" data-align="center" data-formatter="operateFormatter">编辑
            </th>

        </tr>
        </thead>
    </table>
</div>


</body>
</html>
