<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>
<html>
<head>
    <title>--错题统计top10--</title>

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
    <!-- UEditor配置文件 -->
    <script type="text/javascript" src="<%=path%>/UEditor/ueditor.config.js"></script>
    <!-- UEditor编辑器源码文件 -->
    <script type="text/javascript" src="<%=path%>/UEditor/ueditor.all.js"></script>
    <script type="text/javascript" charset="utf-8"
            src="<%=path%>/UEditor/kityformula-plugin/addKityFormulaDialog.js"></script>
    <script type="text/javascript" charset="utf-8" src="<%=path%>/UEditor/kityformula-plugin/getKfContent.js"></script>
    <script type="text/javascript" charset="utf-8"
            src="<%=path%>/UEditor/kityformula-plugin/defaultFilterFix.js"></script>
</head>
<script type="text/javascript">

    $(function () {
        initDateSelect();
        queryEgradeListBYschool();
        getEsubjects();
        queryWrongQuestion();
        var form = document.getElementById('mForm');
        form.onsubmit = function () {
            kfSubmit();
            return false;
        };
    });

    function wrongPercent(value, row, index) {
        var res = 100 * row.wrongnums / row.testnums;
        return ["<div class='progress'> <div class='progress-bar' role='progressbar' aria-valuenow='50' aria-valuemin='0' aria-valuemax='100' style='width:" + res.toFixed(2) + "%'>" + res.toFixed(2) + "</div> </div>"];
//        return res.toFixed(2);
    }

    function indexFormatter(value, row, index) {
        return index + 1;
    }

    function operateFormatter(value, row, index) {
        return [
            '<a class="viewinfo"  href="javascript:void(0)" title="题目信息">',
            '<i class="glyphicon glyphicon-info-sign"></i>',
            '</a>'
        ].join('');
    }

    var ue;
    window.operateEvent = {
        'click .viewinfo': function (e, value, row, index) {
            var params = {};
            params.questionID = row.id;

            $.ajax({
                url: "/equestions/viewOneQuestion",
// 数据发送方式
                type: "get",
// 接受数据格式
                dataType: "json",
// 要传递的数据
                data: params,
                contentType: 'application/json',
// 回调函数，接受服务器端返回给客户端的值，即result值
                success: function (data) {
//                    console.log('sssssss',data);
                    $("#myModal").modal('show');

                    ue = UE.getEditor('editor', {
                        toolbars: [[
                            'fullscreen','preview'
                        ]]
                    });
                    ue.ready(function () {
                        ue.setContent(data.data);
                        ue.setHeight(250);
                    })
                },

                error: function (data) {
                    alert("查询失败" + data);
                    console.log("data",data);
                }
            })
        }
    }

    function queryWrongQuestion() {
        var params = {};
        params.gradeno = $('#gradeno').val();
        params.subjectno = $('#subjectno').val();

        params.beginDate = $('#reservation').val().replace(/-/g, "");
        params.endDate = $('#reservation').val().replace(/-/g, "");
        console.log('sbb', params);
        $.ajax({
            url: "/report/queryWrongQuestion",
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
                alert("查询失败" + data);
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
                console.log(start.toISOString(), end.toISOString(), label);
            });
        });
        document.getElementById("reservation").value = getCurrentMonth();
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
//                $('#subjectno').append("<option value='noselected'>请选择学科</option>");
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

            <span class="add-on input-group-addon">
                <i class="glyphicon glyphicon-calendar fa fa-calendar"></i>
            </span>
            <input type="text" readonly
                   style="width: 190px"
                   name="reservation"
                   id="reservation"
                   class="form-control"/>
            <div style="float: left">
                <select id="subjectno" name="subjectno" class="selectpicker fit-width">

                </select>

                <select id="gradeno" name="gradeno" class="selectpicker fit-width">

                </select>

                <div style="float: right">

                    <button class="btn btn-primary" type="button" onclick="queryWrongQuestion()"><span
                            class="glyphicon glyphicon-eye-open"></span>查询
                    </button>
                    <%--<button class="btn btn-primary" type="button" onclick="queryWrongQuestion()"><span--%>
                            <%--class="glyphicon glyphicon-export"></span>导出--%>
                    <%--</button>--%>
                </div>
            </div>
        </div>
    </div>


    <div id="bs_t" style="float: none;display: block;margin-left: auto;margin-right: auto;">
        <table class="table table-striped" id="ds_table" align="center"
               striped="true" data-toolbar="#bs_toolbar"
               data-pagination="true" sidePagination="server" data-click-to-select="true">
            <thead>

            <tr>

                <th data-field="index" data-align="center" data-formatter="indexFormatter">序号</th>

                <th data-field="tpno" data-align="center" data-sortable="true">试卷</th>
                <th data-field="questionno" data-align="center" data-sortable="true">题号</th>
                <th data-field="wrongnums" data-align="center" data-sortable="true">错误数</th>
                <th data-field="testnums" data-align="center">考生数</th>
                <th data-field="wrongpercent" data-align="center" data-formatter="wrongPercent">错误率%</th>

                <th data-field="id" data-align="center" data-formatter="operateFormatter" data-events="operateEvent">题目信息</th>
            </tr>
            </thead>
        </table>
    </div>
</div>
<!--题目信息模态框（Modal） -->
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog" style="width: 90%">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                    &times;
                </button>
                <h4 class="modal-title" id="myModalLabel">
                    题目信息
                </h4>
            </div>
            <div class="modal-body">
                <form id="mForm" method="post" class="form-inline">
                    <script id="editor" name="content" type="text/plain"></script>

                </form>
            </div>
            <div class="modal-footer">
                <button class="btn btn-primary" type="button" data-dismiss="modal">
                    <span class="glyphicon glyphicon-log-out"></span> 关闭
                </button>
            </div>
        </div>
    </div>
</div>
<!--modal end-->
</body>
</html>