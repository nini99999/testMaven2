<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>
<html>
<head>
    <title>--学生成绩登记表--</title>

    <link rel="stylesheet" href="<%=path%>/css/main.css">

    <!-- Include Twitter Bootstrap and jQuery: -->
    <link rel="stylesheet" href="<%=path%>/bootstrap/bootstrap.min.css" type="text/css"/>
    <script type="text/javascript" src="<%=path%>/bootstrap/jquery.min.js"></script>
    <script type="text/javascript" src="<%=path%>/bootstrap/bootstrap.min.js"></script>

    <!-- Include the plugin's CSS and JS: -->
    <script type="text/javascript" src="<%=path%>/bootstrap/bootstrap-multiselect.js"></script>
    <link rel="stylesheet" href="<%=path%>/bootstrap/bootstrap-multiselect.css" type="text/css"/>

    <script type="text/javascript" src="<%=path%>/bootstrap/bootstrap-select.js"></script>
    <link rel="stylesheet" href="<%=path%>/bootstrap/bootstrap-select.css" type="text/css"/>

    <link href="<%=path%>/bootstrap/font-awesome.min.css" rel="stylesheet">

    <link rel="stylesheet" type="text/css" media="all" href="<%=path%>/bootstrap/daterangepicker-bs3.css"/>

    <script type="text/javascript" src="<%=path%>/bootstrap/moment.js"></script>
    <script type="text/javascript" src="<%=path%>/bootstrap/daterangepicker.js"></script>

    <link rel="stylesheet" href="<%=path%>/bootstrap/bootstrap-table.css">
    <script src="<%=path%>/bootstrap/bootstrap-table.js"></script>
    <script src="<%=path%>/bootstrap/bootstrap-table-zh-CN.js"></script>
    <script src="<%=path%>/utils/xdate.js"></script>
</head>
<script type="text/javascript">

    $(function () {
        getEsubjects();
        queryEgradeListBYschool();
        queryStudentMark();
    });

    function indexFormatter(value, row, index) {
        return index + 1;
    }
    function operateFormatter(value, row, index) {
        var PySheDingID = row.bs_rowid;
// 利用 row 获取点击这行的ID

        return [
            "<button type='button' class='btn btn-info';' id='m-callback-this-start' onclick='btnEntry(" + row.bs_rowid + ")'  > <span class='glyphicon glyphicon-th-list' onclick='queryStudentMark()'></span>  </button>"].join('');

    }
    function queryStudentMark() {
        var params = {};
        params.gradeno = $('#gradeno').val();
        params.classno = $('#classno').val();
        params.tpname = $('#tpname').val();
        params.subjectno = $('#subjectno').val();
        $.ajax({
            url: "/studentMark/queryEstudentMark",
// 数据发送方式
            type: "post",
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
                alert("查询失败" + data);
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

                $('#gradeno').append("<option value='noselected'>请选择年级</option>");
                $.each(data.data, function (i) {

                    $('#gradeno.selectpicker').append("<option value=" + data.data[i].gradeno + ">" + data.data[i].gradename + "</option>");

                });

                $('#gradeno').selectpicker('refresh');

            },

            error: function (data) {

                alert("查询年级失败" + data);

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


            },

            error: function (data) {

                alert("查询班级失败" + data);

            }
        })
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
                $('#subjectno').append("<option value='noselected'>请选择学科</option>");
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
</script>

<body>


<div class="container" style="float:center;width: 99%;">
    <div class="well">

        <div class="input-prepend input-group">
            <div style="float: left">
                <select id="subjectno" name="subjectno" class="selectpicker fit-width">

                </select>
                <select id="gradeno" name="gradeno" class="selectpicker fit-width" onchange="getEclassList()">

                </select>
                <select id="classno" name="classno" class="selectpicker fit-width">

                </select>

                <input id="tpname" name="tpname" class="form-control" style="width: 200px;" placeholder="试卷名称"/>
                <button class="btn btn-primary" type="button" onclick="queryStudentMark()"><span
                        class="glyphicon glyphicon-eye-open"></span>查询
                </button>

                </button>
            </div>
        </div>
    </div>


    <div id="bs_t" style="float: none;display: block;margin-left: auto;margin-right: auto;">
        <table class="table table-striped" id="ds_table" align="center"
               striped="true" data-toolbar="#bs_toolbar"
               data-pagination="true" sidePagination="server" data-click-to-select="true">
            <thead>

            <tr>

                <th data-field="index" data-align="center" data-formatter="indexFormatter">名次</th>

                <th data-field="tpname" data-align="center" data-sortable="true">试卷</th>
                <th data-field="studentname" data-align="center" data-sortable="true">考生</th>
                <th data-field="mark" data-align="center" data-sortable="true">总分</th>
                <th data-field="markone" data-align="center">客观题分</th>
                <th data-field="marktwo" data-align="center">主观题分</th>
                <th data-field="creator" data-align="center" data-sortable="true">创建人</th>
                <th data-field="createdate" data-align="center" data-sortable="true">创建日期</th>
                <th data-field="bs_rowid" data-align="center" data-formatter="operateFormatter">操作</th>
            </tr>
            </thead>
        </table>
    </div>
</div>
</body>
</html>