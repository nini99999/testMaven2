<%--
  Created by IntelliJ IDEA.
  User: jenny
  Date: 2017/3/19
  Time: 上午11:51
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
    <title>--班级管理--</title>
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
    function operateFormatter(value, row, index) {
        var PySheDingID = row.bs_rowid;
// 利用 row 获取点击这行的ID

        return [
            "<button type='button' class='btn btn-info';' id='m-callback-this-start' onclick='btnEntry(" + row.bs_rowid + ")'  > <span class='glyphicon glyphicon-pencil' onclick='queryEgradeList()'></span>  </button>"].join('');

    }
    function queryEgradeListBYschool() {
        var params = {};
        $.ajax({
            url: "/egrade/viewEgradeListByschoolno",
// 数据发送方式
            type: "post",
// 接受数据格式
            dataType: "json",
// 要传递的数据
            data: params,
// 回调函数，接受服务器端返回给客户端的值，即result值
            success: function (data) {
                $('#gradeno').empty();
                $('#gradeno').append("<option value='noselected'>请选择</option>");
                $.each(data.data, function (i) {

                    $('#gradeno.selectpicker').append("<option value=" + data.data[i].gradeno + ">" + data.data[i].gradename + "</option>");

                });


//                $('#schoolno').selectpicker('render');
                $('#gradeno').selectpicker('refresh');

            },

            error: function (data) {

                alert("查询年级失败" + data);

            }
        })
    }

    function queryEclassListByDTO() {
        var params = {};

//        params.schoolno = $('#schoolno').val();
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

                    $("#ds_table").bootstrapTable('destroy');
                    $("#ds_table").bootstrapTable({data: data.data});//刷新ds_table的数据

            },

            error: function (data) {

                alert("查询班级失败" + data);

            }
        })
    }
    function validatAddEclass() {
//        var str_school=$('#schoolno').val();
        var str_grade=$('#gradeno').val();
        if(str_grade==("noselected")){
            alert("请选择学校和年级!");
        }else {
            addEclass();
        }

    }
    function addEclass() {
        var params = {};

//        params.schoolno = $('#schoolno').val();
        params.gradeno = $('#gradeno').val();
        params.classno = classno.value;
        params.classname = classname.value;



        $.ajax({

            url: "/eclass/addEclass",

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

                    queryEclassListByDTO();

                    $("#classno").val("");
                    $("#classname").val("");//重置输入框为空
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
    function delEclass() {
        var selects = $("#ds_table").bootstrapTable('getSelections');
        //alert(JSON.stringify(selects));

        $.ajax({

            url: "/eclass/delEclass",
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
                    queryEclassListByDTO();
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
</script>

<body>
<div class="container">
    <hr/>
</div>
<div id="bs_toolbar">

    选择年级--：
    <select id="gradeno" name="gradeno" class="selectpicker" onchange="queryEclassListByDTO();">

    </select>

    <button class="btn btn-primary" type="button" onclick="queryEclassListByDTO();">
        <span class="glyphicon glyphicon-eye-open"></span> 查询
    </button>
    <!-- 按钮触发模态框 -->
    <button class="btn btn-primary" type="button" data-toggle="modal" data-target="#myModal" >
        <span class="glyphicon glyphicon-plus-sign"></span> 添加
    </button>
    <button class="btn btn-primary" type="button" onclick="delEclass();">
        <span class="glyphicon glyphicon-minus-sign"></span> 删除
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
                    添加班级
                </h4>
            </div>
            <div class="modal-body">

                <hr/>


                班级编码：
                <input id="classno" name="classno" class="btn-default"></input>
                班级名称：<input id="classname" name="classname" class="btn-default"></input>


            </div>
            <script type="text/javascript">
                $(document).ready(function () {

                    //$('#schoolno').multiselect();
//                    getschoolList();//填充学校下拉列表

                    queryEgradeListBYschool();//填充年级下拉列表
                    queryEclassListByDTO();//查询年级

                });
            </script>
            <div class="modal-footer">

                <button class="btn btn-primary" type="button" onclick="validatAddEclass();">
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
           data-pagination="true" sidePagination="server" data-show-toggle="true"
           data-show-columns="true"  data-click-to-select="true">
        <thead>

        <tr>

            <th data-field="estate" data-checkbox="true"></th>
            <th data-field="index" data-align="center" data-formatter="indexFormatter">序号</th>
            <th data-field="schoolno" data-align="center" data-sortable="true">学校编码</th>
            <th data-field="gradeno" data-align="center" data-sortable="true">年级编码</th>
            <th data-field="classno" data-align="center" data-sortable="true">班级编码</th>
            <th data-field="classname" data-align="center" data-sortable="true">班级名称</th>
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
