<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>
<html>
<head>
    <title>--班级学生成绩分析--</title>


    <link rel="stylesheet" href="<%=path%>/css/main.css">

    <!-- Include Twitter echarts -->
    <script src="<%=path%>/echarts/echarts.js"></script>


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
        initSelect();
    });

    function initSelect() {
        $(document).ready(function () {
            $('#reservation').daterangepicker(null, function (start, end, label) {
                console.log(start.toISOString(), end.toISOString(), label);
            });
            $('#testpapers').multiselect();
        });
        document.getElementById("reservation").value = getTimeArea();
    }

    function getTimeArea() {
        var firstDate = new Date();
        firstDate.setDate(1); //第一天
        var endDate = new Date(firstDate);
        endDate.setMonth(firstDate.getMonth() + 1);
        endDate.setDate(0);
        return new XDate(firstDate).toString('yyyy-MM-dd') + " - " + new XDate(endDate).toString('yyyy-MM-dd');
//        alert("第一天：" + new XDate(firstDate).toString('yyyy-MM-dd') + " 最后一天：" + new XDate(endDate).toString('yyyy-MM-dd'));
    }

</script>
<body>
<div class="container" style="float:center">
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
            <div style="float: left">


                <button class="btn btn-primary" type="button" onclick="getTimeArea()"><span
                        class="glyphicon glyphicon-eye-open"></span>查询
                </button>
            </div>
        </div>
    </div>
</div>
</div>
<div style="float: none;display: block;margin-left: auto;margin-right: auto;width: 90%;">
    <div style="height: 43%">
        <table class="table table-striped" id="class_mark" align="center"
               striped="true">
        </table>
    </div>
    <div id="main" style="width: 40%;height:37%;float:left;"></div>
    <div id="charts" style="width: 60%;height:49%;float: left;"></div>
</div>

</body>
</html>