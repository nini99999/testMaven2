<%--
  Created by IntelliJ IDEA.
  User: jenny
  Date: 2017/3/24
  Time: 下午11:48
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
    <title>--学生错题管理--</title>
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
    <script src="<%=path%>/bootstrap/moment-with-locales.js"></script>

    <link href="<%=path%>/bootstrap/bootstrap-datetimepicker.css" rel="stylesheet"/>
    <script src="<%=path%>/bootstrap/bootstrap-datetimepicker.js"></script>
    <script src="<%=path%>/bootstrap/bootstrap-datetimepicker.zh-CN.js"></script>

</head>
<script type="text/javascript">


    function indexFormatter(value, row, index) {
        return index + 1;
    }
    function operateFormatter(value, row, index) {
        var PySheDingID = row.bs_rowid;
// 利用 row 获取点击这行的ID

        return [
            "<button type='button' class='btn btn-info';' id='m-callback-this-start' onclick='btnEntry(" + row.bs_rowid + ")'  > <span class='glyphicon glyphicon-pencil' onclick='queryEgradeList()'></span>  </button>"].join('');

    }
    function getSumQuestionNum() {


        var params = {};
        params.tpno = tpno.value;
        params.countryid = countryid.value;
        params.testdate = '';
        $.ajax({
            url: "/ewrongStudent/getQuestionNumList",
// 数据发送方式
            type: "get",
// 接受数据格式
            dataType: "json",
// 要传递的数据
            data: params,
// 回调函数，接受服务器端返回给客户端的值，即result值
            success: function (data) {
                console.log(data);
                $("#aaa").bootstrapTable('destroy');
                $("#aaa").bootstrapTable({data: data.data});//刷新ds_table的数据

            },

            error: function (data) {

                alert("查询学生错题失败" + data);

            }
        })

    }

    function queryWrongStudent() {
        var params = {};

        params.countryid = countryid.value;
        params.tpno = tpno.value;

        $.ajax({
            url: "/ewrongStudent/viewWrongStudent",
// 数据发送方式
            type: "get",
// 接受数据格式
            dataType: "json",
// 要传递的数据
            data: params,
// 回调函数，接受服务器端返回给客户端的值，即result值
            success: function (data) {
                $("#ds_table").bootstrapTable('destroy');
                $("#ds_table").bootstrapTable({data: data.data});//刷新ds_table的数据

            },

            error: function (data) {

                alert("查询学生错题失败" + data);

            }
        })
    }

    function addWrongStudent() {
        delWrongStudent();

        var selects = $("#aaa").bootstrapTable('getSelections');


        $.ajax({

            url: "/ewrongStudent/modifWrongStudent",

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

                    queryWrongStudent();
                    $('#myModal').modal('hide');
                    alert("保存成功!");
                } else {
                    alert(data.msg);
                }

            },

            error: function (data) {//仅调用url失败时有效
                alert("新增失败" + data.msg);
            }
        })
    }
    function delWrongStudent() {
        var params = {};
        params.countryid = countryid.value;
        params.tpno = tpno.value;

        $.ajax({

            url: "/ewrongStudent/delWrongStudent",
// 数据发送方式
            type: "post",
// 接受数据格式
            dataType: "json",
// 要传递的数据
            data: params,
// 回调函数，接受服务器端返回给客户端的值，即result值
            success: function (data) {//调用url成功时有效
                if (data.success == true) {
                } else {
                    alert(data.msg);
                }

            },
            error: function (data) {//仅调用url失败时有效
                alert("删除失败" + JSON.stringify(data));
            }
        })
    }
    function openAddModal() {
        if (tpno.value.length == 0 || countryid.value.length == 0) {
            alert('请填写试卷编号和学籍号');
        } else {
            getSumQuestionNum();
            $("#myModal").modal('show');
        }
    }
</script>

<body>
<div class="container">
    <hr/>
</div>
<div id="bs_toolbar">
    <div class="input-prepend input-group">
        <input id="countryid" name="countryid" class="form-control" style="width: 200px;"
               placeholder="学籍号："/>
        <input id="tpno" name="tpno" class="form-control" style="width: 200px;"
               placeholder="试卷编号："/>
        <div style="float: left">
            <button class="btn btn-primary" type="button" onclick="queryWrongStudent();">
                <span class="glyphicon glyphicon-eye-open"></span> 查询错题
            </button>
            <!-- 按钮触发模态框 -->
            <button class="btn btn-primary" type="button" onclick="openAddModal();">
                <span class="glyphicon glyphicon-plus-sign"></span> 维护错题
            </button>
            <button class="btn btn-primary" type="button" onclick="queryWrongStudent();"><span
                    class="glyphicon glyphicon-export"></span>导出word
            </button>
        </div>
    </div>
</div>

<div id="bs_t" style="float: none;display: block;margin-left: auto;margin-right: auto;width: 90%">
    <table class="table table-striped" width="80%" id="ds_table" align="center"
           striped="true"
           data-height="566" data-toolbar="#bs_toolbar"
           data-pagination="true" sidePagination="server"
           data-click-to-select="true">
        <thead>
        <tr>
            <th data-field="index" data-align="center" data-formatter="indexFormatter">序号</th>
            <th data-field="countryid" data-align="center">学籍号</th>
            <th data-field="studentname" data-align="center">学生姓名</th>
            <th data-field="testpaperno" data-align="center">试卷编号</th>
            <th data-field="testpapername" data-align="center">试卷名称</th>
            <th data-field="questionno" data-align="center">错题号</th>
            <th data-field="testdate" data-align="center">考试日期</th>
            <th data-field="testpoint" data-align="center">知识点</th>
        </tr>
        </thead>
    </table>
</div>


<!-- 模态框（Modal） -->
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog" style="width: 70%">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                    &times;
                </button>
                <h4 class="modal-title" id="myModalLabel">
                    学生错题维护
                </h4>
            </div>
            <div class="modal-body">
                <div id="bbb" style="float: none;display: block;margin-left: auto;margin-right: auto;">
                    <table class="table table-striped" id="aaa" align="center"
                           striped="true" data-height="496" data-click-to-select="true">
                        <thead>
                        <tr>
                            <th data-field="estate" data-align="center" data-checkbox="true">选择</th>
                            <th data-field="questionno" data-align="center">题号</th>
                            <th data-field="countryid" data-align="center">学籍号</th>
                            <th data-field="testpaperno" data-align="center">试卷编码</th>
                            <th data-field="testdate" data-align="center">考试日期</th>
                            <th data-field="testpoint" data-align="center">知识点</th>
                        </tr>
                        </thead>
                    </table>
                    <hr/>
                </div>
            </div>
            <div class="modal-footer">
                <button class="btn btn-primary" type="button" onclick="addWrongStudent();">
                    <span class="glyphicon glyphicon-floppy-save"></span> 保存
                </button>
                <button class="btn btn-primary" type="button" data-dismiss="modal">
                    <span class="glyphicon glyphicon-log-out"></span> 关闭
                </button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal -->
</div>
</body>
</html>
<script type="text/javascript">
    $(function () {
        queryWrongStudent();

    });
</script>