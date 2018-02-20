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

    <link rel="stylesheet" type="text/css" media="all" href="<%=path%>/bootstrap/bootstrap-editable.css"/>
    <script src="<%=path%>/bootstrap/bootstrap-editable.min.js"></script>
    <script src="<%=path%>/bootstrap/bootstrap-table-editable.js"></script>
</head>
<script type="text/javascript">

    $(function () {
        initDateSelect();
        getEsubjects();
//        queryEgradeListBYschool();
        initBT();
//        queryStudentMark();

    });
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

    function getTestPaperList() {
        var params = {};
        params.gradeno = $('#gradeno').val();
        params.subjectno = $('#subjectno').val();
//        params.creator = 'All';//不根据创建人查询，即查询满足条件的所有试卷
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

    function initBT() {
        $("#ds_table").bootstrapTable('destroy');
        $("#ds_table").bootstrapTable({
            method: "get",
            catch: false,
            toolbar: "#bs_toolbar",
            idField: "id",

            queryParams: function (params) {

                return {
                    pageno: (params.offset / params.limit) + 1,
                    pagesize: params.limit,
                    gradeno: $('#gradeno').val(),
                    classno: $('#classno').val(),
                    tpno: $('#paperid').val(),
                    subjectno: $('#subjectno').val()
                }
//                var params = {};
//                params.pagesize = param.limit;
//                params.pageno = param.offset;
//                console.log('limit:',param.limit);
//                console.log('pageNumber:',param.pageNumber);
//                params.gradeno = $('#gradeno').val();
//                params.classno = $('#classno').val();
//                params.tpno = $('#paperid').val();
//                params.subjectno = $('#subjectno').val();
//                return params;
            },
            url: "/studentMark/queryEstudentMark",
            striped: true,
            clickToSelect: true,
            pagination: true, //是否显示分页（*）
            sidePagination: "server", //分页方式：client客户端分页，server服务端分页（*）
            pageNumber: 1, //初始化加载第一页，默认第一页
            pageSize: 10, //每页的记录行数（*）
            pageList: [10, 20, 50, 100],
//            showColumns:true,
            columns: [{
                checkbox: true
            }, {
                field: "index",
                title: "序号",
                align: "center",
                formatter: function (value, row, index) {

                    return index + 1;
                }
            }, {
                field: "classno",
                title: "班级",
                align: "center"
            }, {
                field: "tpname",
                title: "试卷",
                align: "center"
            }, {
                field: "studentname",
                title: "学生",
                align: "center"
            }, {
                field: "mark",
                title: "总分",
                align: "center"
            }, {
                field: "markone",
                title: "客观分",
                align: "center",
                editable: {
                    type: 'text',
                    title: '客观分',
                    validate: function (v) {
                        if (v < 0) return '不能小于0';
                    }
                }
            }, {
                field: "marktwo",
                title: "主观分",
                align: "center",
                editable: {
                    type: 'text',
                    title: '主观分',
                    align: "center",
                    validate: function (v) {
                        if (v < 0) return '不能小于0';
                    }
                }
            }, {
                field: "testdate",
                title: "测试日期",
                align: "center"
//            }, {
//                field: "bs_rowid",
//                title: "操作",
//                align: "center",
//                formatter: function (value, row, index) {
//                    return [
//                        "<button type='button' class='btn btn-info';' id='m-callback-this-start' onclick='btnEntry(" + row.bs_rowid + ")'  > <span class='glyphicon glyphicon-th-list' onclick='queryStudentMark()'></span>  </button>"].join('');
//
//                }
            }],

            onEditableSave: function (field, row, oldValue, $el) {
//                console.log(row);
                var params = {};

                params.id = row.id;
                params.markone = row.markone;
                params.marktwo = row.marktwo;
                $.ajax({
                    type: "post",
                    url: "/studentMark/modifOnlyMark",
                    data: params,
                    dataType: 'JSON',
                    success: function (data) {

                        if (data.success) {
                            alert('提交数据成功');
                        }
                        initBT();
                    },
                    error: function (data) {
                        alert('编辑失败' + data.msg);
                    },
                    complete: function () {

                    }

                });
            }
        });
//        queryStudentMark();
    }
    function validateModal() {
        var a = $("#classno").val();
        var b = $("#paperid").val();
        if (null == a || null == b) {
            alert("请在主界面中选择班级和试卷！");
        }
        else {
            initModal();
        }
    }
    function initModal() {
//        $("#mclassno").val($("#classno").val());
        $("#myModal").modal('show');
        $("#mclassno").val($("#classno").val());
        $("#mpaperid").val($("#paperid").val());
        $("#aaa").bootstrapTable('destroy');
        $("#aaa").bootstrapTable({
            method: "get",
            catch: false,
//            toolbar: "#bs_toolbar",
            idField: "id",
//            pagination: true,
            queryParams: function (param) {
                var params = {};
                params.gradeno = $('#gradeno').val();
                params.classno = $('#classno').val();
                params.tpno = $('#mpaperid').val();
//                params.subjectno = $('#subjectno').val();
                return params;
            },
            url: "/estudent/getStudentByClassAndTpno",
            striped: true,
            clickToSelect: true,
            columns: [{
                field: "index",
                title: "序号",
                align: "center",
                formatter: function (value, row, index) {

                    return index + 1;
                }
            }, {
                field: "studentname",
                title: "姓名",
                align: "center"
            }, {
                field: "localid",
                title: "学籍辅号",
                halign: "center",
                align: "left"
            }, {
                field: "countryid",
                title: "全国学籍号",
                halign: "center",
                align: "left"
            }, {
                field: "markone",
                title: "客观分",
                align: "center",
                editable: {
                    type: 'text',
                    title: '客观分',
                    validate: function (v) {
                        if (v < 0) return '不能小于0';
                    }
                }
            }, {
                field: "marktwo",
                title: "主观分",
                align: "center",
                editable: {
                    type: 'text',
                    title: '主观分',
                    align: "center",
                    validate: function (v) {
                        if (v < 0) return '不能小于0';
                    }
                }
            }],

            onEditableSave: function (field, row, oldValue, $el) {
                console.log(row);
                var params = {};

                params.studentno = row.id;
                params.studentname = row.studentname;
                params.markone = row.markone;
                params.marktwo = row.marktwo;
                params.classno = $("#mclassno").val();
                params.tpno = $("#mpaperid").val();
                params.subjectno = $("#subjectno").val();
                $.ajax({
                    type: "post",
                    url: "/studentMark/saveOrUpdateEstudentMark",
                    data: params,
                    dataType: 'JSON',
                    success: function (data) {

                        if (data.success) {
                            alert('提交数据成功');
                        }
                        initBT();
                    },
                    error: function (data) {
                        alert('编辑失败' + data.msg);
                    },
                    complete: function () {
                    }

                });
            }
        });
//        queryStudentMark();
//            }
//        });
    }
    function delMark() {
        var selects = $("#ds_table").bootstrapTable('getSelections');
//        console.log(selects);
        $.ajax({

            url: "/studentMark/delEstudentMark",
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
                    initBT();
                    alert("记录已删除!");

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
                            onchange="getEclassList();getTestPaperList()">

                    </select>
                    <select id="classno" name="classno" class="selectpicker fit-width">

                    </select>
                    <select id="paperid" name="paperid" class="selectpicker fit-width"></select>
                    <%--<input id="tpname" name="tpname" class="form-control" style="width: 200px;" placeholder="试卷名称"/>--%>
                </div>
            </div>
        </div>
    </div>
    <div id="bs_toolbar">
        <button class="btn btn-primary" type="button" onclick="initBT()"><span
                class="glyphicon glyphicon-eye-open"></span>查询
        </button>
        <!-- 按钮触发模态框 -->
        <button class="btn btn-primary" type="button" onclick="validateModal();">
            <span class="glyphicon glyphicon-plus-sign"></span> 添加
        </button>
        <button class="btn btn-primary" type="button" onclick="delMark();">
            <span class="glyphicon glyphicon-minus-sign"></span> 删除
        </button>
    </div>
    <table id="ds_table"></table>
</div>
<!-- 模态框（Modal） -->
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog" style="width: 85%">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                    &times;
                </button>
                <h4 class="modal-title" id="myModalLabel">
                    成绩登记
                </h4>
            </div>
            <div class="modal-body">
                <form class="form-inline" role="form">
                    <div class="form-group">
                        <label for="mclassno">当前年级：</label><input type="text" id="mclassno" style="width: auto"
                                                                  class="form-control" readonly/>
                    </div>
                    <div class="form-group">
                        <label for="mpaperid">当前试卷：</label><input type="text" id="mpaperid" style="width: auto"
                                                                  class="form-control" readonly/>
                    </div>
                    <hr/>
                    <div id="bbb" style="float: none;display: block;margin-left: auto;margin-right: auto;">

                        <table id="aaa"></table>
                        <hr/>
                    </div>
                </form>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal -->
</div>
</body>
</html>