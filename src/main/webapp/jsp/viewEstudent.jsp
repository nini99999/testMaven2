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
    <title>--学生管理--</title>
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
    function queryEstudentByDTO() {
        var params = {};

        params.schoolno = $('#schoolno').val();
        params.gradeno = $('#gradeno').val();
        params.classno = $('#classno').val();
        $.ajax({
            url: "/estudent/viewEstudentByDTO",
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

                alert("查询学生失败" + data);

            }
        })
    }
    function validatAddEstudent() {
        var str_school = $('#schoolno').val();
        var str_grade = $('#gradeno').val();
        var str_class = $('#classno').val();
        if (str_school == ("noselected") || str_grade == ("noselected") || str_class == "noselected") {
            alert("请选择学校、年级、班级!");
        } else {
            addEstudent();
        }

    }
    function addEstudent() {

        var params = {};
        params.gradeno = $('#gradeno').val();
        params.classno = $('#classno').val();
        params.chinaid = chinaid.value;
        params.studentname = studentname.value;
        params.localid = localid.value;
        params.countryid = countryid.value;
        params.nation = $('#nation').val();
        params.birthday = $('#bidate').val();
        params.admissiondate = $('#asdate').val();
        params.schoolstate = $('#schoolstate').val();
        params.studystate = $('#studystate').val();


        $.ajax({

            url: "/estudent/addEstudent",

// 数据发送方式
            type: "post",
// 接受数据格式
            dataType: "json",
// 要传递的数据
            data: params,
// 回调函数，接受服务器端返回给客户端的值，即result值
            success: function (data) {//调用url成功时有效
                if (data.success == true) {

//                    $("#ds_table").bootstrapTable('refresh', data);//刷新ds_table的数据

                    queryEstudentByDTO();

                    $("#chinaid").val("");
                    $("#studentname").val("");
                    $("#localid").val("");
                    $("#countryid").val("");
                    $("#nation").val("");
                    $("#birthday").val("");
                    $("#admissiondate").val("");
                    $("#schoolstate").val("");
                    $("#stydystate").val("");

                    //重置输入框为空
                    alert("保存成功，请继续添加");

                } else {
                    alert(data.msg);
                }

            },

            error: function (data) {//仅调用url失败时有效

                alert("新增失败" + data);

            }
        })
    }
    function delEstudent() {
        var selects = $("#ds_table").bootstrapTable('getSelections');
        //alert(JSON.stringify(selects));

        $.ajax({

            url: "/estudent/delEstudent",
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
                    queryEstudentByDTO();
                    alert("记录已删除!");
//
                } else {
                    alert(data.msg);
                }

            },

            error: function (data) {//仅调用url失败时有效

                alert("删除失败" + JSON.stringify(data));

            }
        })
    }
    function getNation() {
        $.ajax({
            url: "/estudent/getNation",
// 数据发送方式
            type: "get",
// 接受数据格式
            dataType: "json",
// 要传递的数据
            data: 'data',
// 回调函数，接受服务器端返回给客户端的值，即result值
            success: function (data) {
//alert(data.data);
                /**
                 * 初始化民族下拉列表**/
                $('#nation').empty();
//                $('#nation').append("<option value='noselected'>请选择民族</option>");
                $.each(data.nation, function (i) {
                    $('#nation.selectpicker').append("<option value=" + data.nation[i] + ">" + data.nation[i] + "</option>");
                });
                $('#nation').selectpicker('refresh');
                /**
                 * 初始化学习状态**/
                $('#studystate').empty();
                $.each(data.studystate, function (i) {
                    console.log(i+"be");
                    $('#studystate.selectpicker').append("<option value=" + [i] + ">" + data.studystate[i] + "</option>");
                });
                $('#studystate').selectpicker('refresh');
                /**
                 * 初始化学籍状态**/
                $('#schoolstate').empty();
                $.each(data.schoolstate, function (i) {
                    $('#schoolstate.selectpicker').append("<option value=" + [i] + ">" + data.schoolstate[i] + "</option>");
                });
                $('#schoolstate').selectpicker('refresh');
            },

            error: function (data) {
                alert("查询学校失败" + data);
            }
        })
    }
</script>

<body>
<div class="container">
    <hr/>
</div>
<div id="bs_toolbar">

    <select id="gradeno" name="gradeno" class="selectpicker" onchange="getEclassList()">

    </select>

    <select id="classno" name="classno" class="selectpicker">

    </select>

    <button class="btn btn-primary" type="button" onclick="queryEstudentByDTO();">
        <span class="glyphicon glyphicon-eye-open"></span> 查询
    </button>
    <!-- 按钮触发模态框 -->
    <button class="btn btn-primary" type="button" data-toggle="modal" data-target="#myModal">
        <span class="glyphicon glyphicon-plus-sign"></span> 添加
    </button>
    <button class="btn btn-primary" type="button" onclick="delEstudent();">
        <span class="glyphicon glyphicon-minus-sign"></span> 删除
    </button>


</div>

<!-- 模态框（Modal） -->
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog" style="width: 800px">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                    &times;
                </button>
                <h4 class="modal-title" id="myModalLabel">
                    添加学生
                </h4>
            </div>
            <div class="modal-body">
                学生姓名：<input id="studentname" name="studentname" class="btn-default"></input>
                所属民族：
                <select id="nation" name="nation" class="selectpicker"></select></td>
                <hr/>
                <div class="row">
                    <span class="col-xs-2">入学日期：</span>
                    <div class="input-group date col-xs-6" id="admissiondate">
                        <input class="form-control" id="asdate" name="asdate" readonly/>
                        <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                    </div>
                </div>
                <hr/>
                <div class="row">
                    <span class="col-xs-2">出生日期：</span>
                    <div class="input-group date col-xs-6" id="birthday">
                        <input class="form-control" id="bidate" name="bidate" readonly/>
                        <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                    </div>
                </div>
                <hr/>
                <div class="row">
                    <span class="col-xs-2">身份证号：</span>
                    <input id="chinaid" name="chinaid" class="col-xs-6"/></div>
                <hr/>
                <div class="row">
                    <span class="col-xs-2">学籍辅号：</span><input id="localid" name="localid" class="col-xs-6"></input>
                </div>
                <hr/>
                <div class="row">
                    <span class="col-xs-2">学籍编号：</span><input id="countryid" name="countryid" class="col-xs-6"></input>
                </div>
                <hr/>
                学籍状态：
                <select id="schoolstate" name="schoolstate" class="selectpicker"></select>
                学习状态：<select id="studystate" name="studystate" class="selectpicker"></select>
            </div>

            <div class="modal-footer">

                <button class="btn btn-primary" type="button" onclick="validatAddEstudent();">
                    <span class="glyphicon glyphicon-floppy-save"></span> 保存
                </button>
                <button class="btn btn-primary" type="button" data-dismiss="modal">
                    <span class="glyphicon glyphicon-log-out"></span> 关闭
                </button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal -->
</div>
<div id="bs_t" style="float: none;display: block;margin-left: auto;margin-right: auto;width: 99%">
    <table class="table table-striped" width="80%" id="ds_table" align="center"
           striped="true"
           data-height="566" data-toolbar="#bs_toolbar"
           data-pagination="true" sidePagination="server"
           data-click-to-select="true">
        <thead>

        <tr>

            <th data-field="estate" data-checkbox="true"></th>
            <th data-field="index" data-align="center" data-formatter="indexFormatter">序号</th>
            <th data-field="studentname" data-align="center">学生姓名</th>

            <th data-field="nation" data-align="center">民族</th>
            <th data-field="birthday" data-align="center">生日</th>
            <th data-field="admissiondate" data-align="center">入学日期</th>


            <th data-field="schoolstate" data-align="center">学籍状态</th>
            <th data-field="studystate" data-align="center">学习状态</th>
            <th data-field="localid" data-align="center" data-sortable="true">学籍辅号</th>
            <th data-field="countryid" data-align="center">全国学籍号</th>
            <th data-field="chinaid" data-align="center">身份证号</th>

            <th data-field="bs_rowid" data-align="center" data-formatter="operateFormatter">编辑

            </th>

        </tr>
        </thead>
    </table>
</div>
</body>
</html>
<script type="text/javascript">
    $(document).ready(function () {

        //$('#schoolno').multiselect();


    });
    $(function () {

        queryEgradeListBYschool();//填充年级下拉列表
//        getEclassList();//填充班级下拉列表
        getNation();//填充民族
        queryEstudentByDTO();
        $('#birthday').datetimepicker({
            format: 'yyyy-mm-dd',
            weekStart: 1,
            autoclose: true,
            startView: 2,
            minView: 2,
            forceParse: false,
            language: 'zh-CN'
        });
        $('#admissiondate').datetimepicker({
            format: 'yyyy-mm-dd',
            weekStart: 1,
            autoclose: true,
            startView: 2,
            minView: 2,
            forceParse: false,
            language: 'zh-CN'
        });
    });
</script>