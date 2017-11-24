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

    <script src="<%=path%>/bootstrap/tableExport.js"></script>
    <script src="<%=path%>/bootstrap/bootstrap-table-export.js"></script>
    <%--<link rel="stylesheet" type="text/css" media="all" href="<%=path%>/bootstrap/bootstrap-editable.css"/>--%>
    <%--<script src="<%=path%>/bootstrap/bootstrap-editable.min.js"></script>--%>
    <%--<script src="<%=path%>/bootstrap/bootstrap-table-editable.js"></script>--%>

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
//    function getSumQuestionNum() {
//
//
//        var params = {};
//        params.tpno = $('#paperid').val();
////        params.studentid = $('#student').val();
////        params.countryid = countryid.value;
////        params.testdate = '';
//        $.ajax({
//            url: "/ewrongStudent/getQuestionNumList",
//// 数据发送方式
//            type: "get",
//// 接受数据格式
//            dataType: "json",
//// 要传递的数据
//            data: params,
//// 回调函数，接受服务器端返回给客户端的值，即result值
//            success: function (data) {
//                console.log(data);
//                $("#aaa").bootstrapTable('destroy');
//                $("#aaa").bootstrapTable({data: data.data});//刷新ds_table的数据
//
//            },
//
//            error: function (data) {
//
//                alert("查询学生错题失败" + data);
//
//            }
//        })
//
//    }
    function queryQuestions() {
        if (!$('#paperid').val()) {
            alert('请选择试卷！');

        } else {
            var params = {};

            params.paperid = $('#paperid').val();
            params.studentid = $('#student').val();
            $.ajax({
                url: "/ewrongStudent/getQuestionListWithState",
// 数据发送方式
                type: "get",
// 接受数据格式
                dataType: "json",
// 要传递的数据
                data: params,
// 回调函数，接受服务器端返回给客户端的值，即result值
                success: function (data) {
//                    console.log(data.data);
                    $('#aaa').bootstrapTable('destroy');
                    $('#aaa').bootstrapTable({data: data.data});//刷新ds_table的数据
                },
                error: function (data) {
                    alert("查询失败" + data);
                    console.log(data.data);
                }
            })
        }
    }
    function queryWrongStudent() {
        var params = {};

//        params.countryid = countryid.value;
        params.tpno = $("#paperid").val();
        params.student = 'forStudent';

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
        params.studentid = $('#student').val();
//        params.countryid = countryid.value;
        params.tpno = $('#paperid').val();

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
                console.log($('#student').val() + $('#paperid').val() + "记录已删除！");

            },
            error: function (data) {//仅调用url失败时有效
                console.log("删除记录失败" + data.msg);
                alert("删除失败" + JSON.stringify(data));
            }
        })
    }
    function delSelections() {
        var selects = $("#ds_table").bootstrapTable('getSelections');
        console.log(selects);
        $.ajax({

            url: "/ewrongStudent/delSelects",
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
                    alert('记录已删除');
                } else {
                    alert('删除失败！');
                }
            },
            error: function (data) {//仅调用url失败时有效
                console.log("删除记录失败" + data.msg);
                alert("删除失败" + JSON.stringify(data));
            }
        })
    }
    function openAddModal() {
        if (paperid.value.length == 0) {
            alert('请选择试卷编号和年级');
        } else {

            $("#myModal").modal('show');
            queryQuestions();
        }
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
    function getTestPaperList() {
        var params = {};
        params.gradeno = $('#gradeno').val();
        params.subjectno = $('#subjectno').val();
        params.creator = 'All';//不根据创建人查询，即查询满足条件的所有试卷
        params.startDate = $('#reservation').val().replace(/-/g, "");
        params.endDate = $('#reservation').val().replace(/-/g, "");
        $.ajax({
            url: "/etestpaper/viewTestPaper",
// 数据发送方式
            type: "get",
// 接受数据格式
            dataType: "json",
// 要传递的数据
            data: params,
// 回调函数，接受服务器端返回给客户端的值，即result值
            success: function (data) {
                $('#paperid').empty();
                $.each(data.data, function (i) {
                    $('#paperid.selectpicker').append("<option value=" + data.data[i].tpno + ">" + data.data[i].tpname + "</option>");
                });
                $('#paperid').selectpicker('refresh');
            },
            error: function (data) {
                alert("查询失败" + data);
                console.log("error", data);
            }
        })

    }
    function getCurrentMonth() {
        var firstDate = new Date();
        firstDate.setDate(1); //第一天
        var endDate = new Date(firstDate);
        endDate.setMonth(firstDate.getMonth() + 1);
        endDate.setDate(0);
        return new XDate(firstDate).toString('yyyy-MM-dd') + " - " + new XDate(endDate).toString('yyyy-MM-dd');
//        alert("第一天：" + new XDate(firstDate).toString('yyyy-MM-dd') + " 最后一天：" + new XDate(endDate).toString('yyyy-MM-dd'));
    }
    function initDateSelect() {
        $(document).ready(function () {
            $('#reservation').daterangepicker(null, function (start, end, label) {
//                console.log(start.toISOString(), end.toISOString(), label);
                getTestPaperList();
            });
        });
        document.getElementById("reservation").value = getCurrentMonth();
        queryEgradeListBYschool();
    }
    function exportWrongQuestions() {
        if (paperid.value.length == 0 || classno.value.length == 0) {
            alert('请选择试卷编号和年级');
        } else {
            var form = document.getElementById("exportForm");
            form.submit();
        }
    }

</script>

<body>
<div class="container" style="float:center;width: 99%;">
    <div class="well">

        <div class="input-prepend input-group">

               <span class="add-on input-group-addon">
                <i class="glyphicon glyphicon-calendar fa fa-calendar"></i>
                </span>
            <input type="text" readonly
                   style="width: 200px"
                   name="reservation"
                   id="reservation"
                   class="form-control"/>
            <div class="input-prepend input-group">
                <div style="float: left">
                    <select id="subjectno" name="subjectno" class="selectpicker fit-width"
                            onchange="getTestPaperList()">
                    </select>
                    <select id="gradeno" name="gradeno" class="selectpicker fit-width"
                            onchange="getTestPaperList()">

                    </select>
                    <select id="paperid" name="paperid" class="selectpicker fit-width"></select>
                    <%--<input id="tpname" name="tpname" class="form-control" style="width: 200px;" placeholder="试卷名称"/>--%>
                </div>
            </div>

        </div>
    </div>

    <div id="b_toobar">

        <button class="btn btn-primary" type="button" onclick="queryWrongStudent();">
            <span class="glyphicon glyphicon-eye-open"></span> 查询错题
        </button>
        <!-- 按钮触发模态框 -->
        <button class="btn btn-primary" type="button" onclick="openAddModal();">
            <span class="glyphicon glyphicon-plus-sign"></span> 维护错题
        </button>
        <button class="btn btn-primary" type="button" onclick="delSelections();"><span
                class="glyphicon glyphicon-export"></span>删除已选
        </button>
        <button class="btn btn-primary" type="button" onclick="exportWrongQuestions();"><span
                class="glyphicon glyphicon-export"></span>导出错题
        </button>
    </div>

    <table class="table table-striped" width="95%" id="ds_table" align="center"
           striped="true" data-show-export="true"
           data-export-types="['json','xml','png','csv','txt','sql','doc','excel','xlsx','pdf']"
           data-export-datatype="all"
           data-pagination="true" sidePagination="server" data-pagelist="10,50"
           data-click-to-select="true">
        <thead>
        <tr>
            <th data-field="estate" data-checkbox="true"></th>
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
                <form class="form-inline" role="form">
                    <%--<div class="form-group">--%>
                        <%--<label for="student">请选择学生：</label> <select id="student" name="student"--%>
                                                                    <%--onchange="getSumQuestionNum();"--%>
                                                                    <%--class="selectpicker fit-width"></select>--%>
                    <%--</div>--%>
                    <div id="bbb" style="float: none;display: block;margin-left: auto;margin-right: auto;">
                        <table class="table table-striped" id="aaa" align="center"
                               striped="true" data-height="496" data-click-to-select="true">
                            <thead>
                            <tr>
                                <th data-field="estate" data-align="center" data-checkbox="true">选择</th>
                                <th data-field="questionid" data-align="center">ID</th>
                                <th data-field="questionno" data-align="center">题号</th>

                            </tr>
                            </thead>
                        </table>
                    </div>
                </form>
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
        getEsubjects();
        initDateSelect();
        queryWrongStudent();
    });
</script>