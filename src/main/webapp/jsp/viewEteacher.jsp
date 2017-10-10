<%--
  Created by IntelliJ IDEA.
  User: jenny
  Date: 2017/3/27
  Time: 下午12:44
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
    <title>--教师管理--</title>
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
//        console.log(row);

        return [
            '<a href="javascript:void(0)" title="编辑教师信息">',
            '<i class="glyphicon glyphicon-pencil" onclick="queryEteacherBYschool()"></i>',
            '<i>  </i>',
            '</a>',

            '<a href="javascript:void(0)" title="维护教师授课班级">',
            '<i class="glyphicon glyphicon-list"   data-toggle="modal" data-target="#classModal" onclick="queryEclass(\'' + row.teacherid + '\')"></i>',
            '</a>'
        ].join('');
    }

    function getschoolList() {//获取下拉学校列表
        $.ajax({
            url: "/eschool/viewEschoolList",
// 数据发送方式
            type: "get",
// 接受数据格式
            dataType: "json",
// 要传递的数据
            data: 'data',
// 回调函数，接受服务器端返回给客户端的值，即result值
            success: function (data) {
//alert(data.data);

                $.each(data.data, function (i) {
//                    alert(i);

                    $('#schoolno.selectpicker').append("<option value=" + data.data[i].schoolno + ">" + data.data[i].schoolname + "</option>");

                });

                $('#schoolno').selectpicker('refresh');
                queryEteacherBYschool();
            },

            error: function (data) {

                alert("查询学校失败" + data);

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
            },

            error: function (data) {

                alert("查询学科失败" + data);

            }
        })
    }
    function queryEclass(strid) {
        console.log(strid);
        var params = {};

        params.schoolno = $('#schoolno').val();
        params.teacherno = strid;
        $.ajax({
            url: "/eclass/viewEclassEstate",
// 数据发送方式
            type: "post",
// 接受数据格式
            dataType: "json",
// 要传递的数据
            data: params,
// 回调函数，接受服务器端返回给客户端的值，即result值
            success: function (data) {


                $("#class_table").bootstrapTable('destroy');
                $("#class_table").bootstrapTable({data: data.data});//刷新ds_table的数据

                $("#c_teacherid").val(strid);

            },

            error: function (data) {

                alert("查询班级失败" + data);

            }
        })
    }

    function getChildTable(index, row, $detail) {

        var parentid = row.teacherid;
//        console.log(row);
        var cur_table = $detail.html('<table></table>').find('table');
        $(cur_table).bootstrapTable({
            url: '/eteacher/getEteacherClass',
            method: 'get',
            queryParams: {strParentID: parentid},
            ajaxOptions: {strParentID: parentid},
//            clickToSelect: true,
//            detailView: true,//父子表
//            uniqueId: "id",
//            pageSize: 10,
//            pageList: [10, 25],
            columns: [
                {
                    field: 'classno',
                    title: '授课班级编码'
                }, {
                    field: 'classname',
                    title: '授课班级名称'
                },],
            //无线循环取子表，直到子表里面没有记录
            onExpandRow: function (index, row, $Subdetail) {
                getChildTalbe(index, row, $Subdetail);
            }
        });
    }

    function queryEteacherBYschool() {
        var params = {};

        params.schoolno = $('#schoolno').val();
        params.subjectno = $('#subjectno').val();

        $.ajax({
            url: "/eteacher/viewEteacherByDTO",
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
//                console.log(data);
                alert("查询失败" + data);

            }
        })
    }
    function addEteacher() {
        var params = {};

        params.schoolno = $('#schoolno').val();
        params.subjectno = $('#subjectno').val();
        params.teacherid = teacherid.value;
        params.teachername = teachername.value;
        params.tel = tel.value;


        $.ajax({

            url: "/eteacher/addEteacher",

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

                    queryEteacherBYschool();
//重置输入框为空
                    $("#teacherid").val("");
                    $("#teachername").val("");
                    $("#tel").val("");

                    alert("保存成功，请继续添加");

                } else {
                    alert(data.msg);
                }

            },

            error: function (data) {//仅调用url失败时有效
                console.log(data);
                alert("新增失败" + data);

            }
        })
    }
    function addEteacherClass() {
        var params = {};
        params.teacherid = $('#c_teacherid').val();
        params.classno = [];
        var selects = $("#class_table").bootstrapTable('getSelections');

        for (var i = 0; i < selects.length; i++) {

            params.classno[i] = selects[i].classno;

        }
        console.log(params);
        $.ajax({

            url: "/eteacher/addEteacherClass",
// 数据发送方式
            type: "post",
// 接受数据格式
            dataType: "json",
            contentType: 'application/json',
// 要传递的数据
            data: JSON.stringify(params),
// 回调函数，接受服务器端返回给客户端的值，即result值
            success: function (data) {//调用url成功时有效
                if (data.success == true) {
                    alert("数据已保存！");
                    queryEteacherBYschool();
//                    $("#ds_table").bootstrapTable('refresh', data);//刷新ds_table的数据

                } else {
                    alert("系统错误：" + data.msg);
                }

            },

            error: function (data) {//仅调用url失败时有效

                alert("添加失败" + JSON.stringify(data));

            }
        })
    }
    function delEteacher() {
        var selects = $("#ds_table").bootstrapTable('getSelections');

        $.ajax({

            url: "/eteacher/delEteacher",
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
                    queryEteacherBYschool();
//                    $("#ds_table").bootstrapTable('refresh', data);//刷新ds_table的数据

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
    选择学校：
    <select id="schoolno" name="schoolno" class="selectpicker">
    </select>
    选择学科：
    <select id="subjectno" name="subjectno" class="selectpicker">
    </select>
    <button class="btn btn-primary" type="button" onclick="queryEteacherBYschool();">
        <span class="glyphicon glyphicon-eye-open"></span> 查询
    </button>
    <!-- 按钮触发模态框 -->
    <button class="btn btn-primary" type="button" data-toggle="modal" data-target="#myModal">
        <span class="glyphicon glyphicon-plus-sign"></span> 添加
    </button>
    <button class="btn btn-primary" type="button" onclick="delEteacher();">
        <span class="glyphicon glyphicon-minus-sign"></span> 删除
    </button>


</div>

<!-- 模态框-添加教师（Modal） -->
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                    &times;
                </button>
                <h4 class="modal-title" id="myModalLabel">
                    添加教师
                </h4>
            </div>
            <div class="modal-body">


                教职工号：<input id="teacherid" name="teacherid" class="form-control"></input>
                <hr/>
                教师姓名：<input id="teachername" name="teachername" class="form-control"></input>
                <hr/>
                手机号码：<input id="tel" name="tel" class="form-control"></input>


            </div>

            <div class="modal-footer">

                <button class="btn btn-primary" type="button" onclick="addEteacher();">
                    <span class="glyphicon glyphicon-floppy-save"></span> 保存
                </button>
                <button class="btn btn-primary" type="button" data-dismiss="modal">
                    <span class="glyphicon glyphicon-log-out"></span> 关闭
                </button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal -->
</div>

<!-- 模态框-维护教师授课班级（Modal） -->
<div class="modal fade" id="classModal" tabindex="-1" role="dialog" aria-labelledby="classModalLabel"
     aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                    &times;
                </button>
                <h4 class="modal-title" id="classModalLabel">
                    教师授课班级维护
                </h4>
            </div>
            <div class="modal-body">
                当前选择教职工号：<input id="c_teacherid" name="c_teacherid" class="btn-default" disabled/>
                <table class="table table-striped" width="80%" id="class_table" align="center"
                       striped="true" data-height="360" data-click-to-select="true">
                    <thead>

                    <tr>

                        <th data-field="estate" data-checkbox="true"></th>
                        <th data-field="index" data-align="center" data-formatter="indexFormatter">序号</th>
                        <th data-field="classno" data-align="center" data-sortable="true">班级编码</th>
                        <th data-field="classname" data-align="center" data-sortable="true">班级名称</th>
                        <th data-field="schoolno" data-align="center" data-sortable="true">学校编码</th>
                        <th data-field="gradeno" data-align="center" data-sortable="true">年级编码</th>


                    </tr>
                    </thead>
                </table>
                <hr/>
            </div>

            <div class="modal-footer">

                <button class="btn btn-primary" type="button" onclick="addEteacherClass();">
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
           data-height="566" data-toolbar="#bs_toolbar" data-detail-view="true" data-detail-formatter="getChildTable"
           data-pagination="true" sidePagination="server"
           data-show-columns="true" data-click-to-select="true">
        <thead>

        <tr>

            <th data-field="estate" data-checkbox="true"></th>
            <th data-field="index" data-align="center" data-formatter="indexFormatter">序号</th>

            <th data-field="teacherid" data-align="center" data-sortable="true">教职工号</th>
            <th data-field="teachername" data-align="center" data-sortable="true">教师姓名</th>
            <th data-field="tel" data-align="center" data-sortable="true">tel</th>
            <th data-field="creator" data-align="center">创建人</th>
            <th data-field="createdate" data-align="center">创建日期</th>
            <th data-field="id" data-align="center" data-formatter="operateFormatter" onclick="">编辑
            </th>

        </tr>
        </thead>
    </table>
</div>
</body>
</html>
<script type="text/javascript">
    $(document).ready(function () {
        getschoolList();
        getsubjectList();


    });
</script>