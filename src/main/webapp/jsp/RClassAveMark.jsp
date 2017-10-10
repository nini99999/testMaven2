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

        queryEgradeListBYschool();//填充年级下拉列表

    });
    function queryRClassMark() {//动态生成表头，并调用数据进行绑定
        var questionColumns = [];
        $.ajax({
            url: "/esubjects/viewEsubjectsList",
// 数据发送方式

            type: "get",
// 接受数据格式
            dataType: "json",
// 要传递的数据
            data: 'data',
            success: function (data) {
                var subjectnames = [];
                var mdata = data.data;
                var t0 = {field: "0", title: "班级", align: "center"};
                var num = mdata.length + 1;
                console.log(mdata.subjectname);
                questionColumns.push(t0);
                for (var i = 0; i < mdata.length; i++) {

                    var temp = {field: i + 1, title: mdata[i].subjectname, align: "center"};//手动拼接columns
                    questionColumns.push(temp);
                    subjectnames.push(mdata[i].subjectname);
                }
//                console.log(subjectnames);
                var t1 = {field: num, title: "总分", align: "center"};
                questionColumns.push(t1);
                bandData(questionColumns, subjectnames)
            }
        })
    }
    function bandData(questionColumns, subjectNames) {//查询班级成绩表，并绑定数据至table
        var params = {};
        params.gradeno = $('#gradeno').val();
        $.ajax({
            url: "/report/queryRClassMark",         //请求后台的URL（*）
            type: "post",                    //请求方式（*）
            dataType: "json",
            data: params,

            success: function (data) {

                $('#class_mark').bootstrapTable('destroy');
                $('#class_mark').bootstrapTable({
                    columns: questionColumns,
                    data: data.data
                });
                if (data.data != null) {
                    intiEcharts(data.data);
                    initMarkChart(data.data, subjectNames);
                }
//                console.log(data.data);
            }
        })
    }
    function initMarkChart(data, subjectNames) {//各科成绩图表
        var classData = [];
        var markData = [];
        var serie = [];
        for (var i = 0; i < data.length; i++) {
            classData.push(data[i][0]);
            markData = data[i];
            markData.splice(0, 1);
            markData.splice(data[i].length - 1, 1);
            console.log(markData);
            var item = {
                name: classData[i],
                type: 'line',
                data: markData
            }
            serie.push(item);
        }
//        console.log(classData);
        var markChart = echarts.init(document.getElementById('charts'));
        option = {
            title: {
//                text: '',
                subtext: '学科平均成绩对比--By MingLu I.T. Co., Ltd.',
                x: 'right',
                y: 'top',
                textAlign: 'center'
            },
            tooltip: {
                trigger: 'axis',
                axisPointer: {            // 坐标轴指示器，坐标轴触发有效
                    type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                }
            },
            legend: {
                orient: 'vertical',
                x: 'left',
                y: 'center',
                data: classData
//                data:['高一','高二','高三']
            },
            grid: {
                left: '16%',
                right: '10%',
                top: '20%',
                bottom: '25%',
                containLabel: true
            },
            xAxis: [
                {
                    type: 'category',
                    data: subjectNames
//                    data: ['数学', '物理', '化学', '英语', '语文', '文综', '理综']
                }
            ],
            yAxis: [
                {
                    type: 'value'
                }
            ],
            series: serie
        };   // 使用刚指定的配置项和数据显示图表。
        markChart.setOption(option);
    }
    function intiEcharts(data) {//展现图表
        var strdata = [];
        var ydata = [];
        var num = 0;
        for (var i = 0; i < data.length; i++) {
            strdata.push(data[i][0]);
            num = data[i].length - 1;
            ydata.push(data[i][num]);
        }
//        console.log(ydata);
        var myChart = echarts.init(document.getElementById('main'));

        option = {
            title: {
//                text: '',
                subtext: '班级平均成绩排名--By MingLu I.T. Co., Ltd.',
                x: 'right',
                y: 'top',
                textAlign: 'center'
            },
            color: ['#3398DB'],
            tooltip: {
                trigger: 'axis',
                axisPointer: {            // 坐标轴指示器，坐标轴触发有效
                    type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                }
            }
            ,
            grid: {
                left: '3%',
                right: '4%',
                bottom: '8%',
                containLabel: true
            }
            ,
            xAxis: [
                {
                    type: 'category',
                    axisLabel:{
                        interval:0,
                        rotate:60//倾斜度 -90 至 90 默认为0
                    },
                    data: strdata,
//                    data: ['一班', '二班', '三班', '四班', '五班', '六班', '七班', '八班', '九班',],
                    axisTick: {
                        alignWithLabel: true
                    }
                }
            ],
            yAxis: [
                {
                    type: 'value'
                }
            ],
            series: [
                {
                    name: '总分',
                    type: 'bar',
                    barWidth: '28%',
                    data: ydata
//                    data: [555, 552, 576, 500, 461, 568, 534, 600, 465]
                },

            ]
        }
        ;

        // 使用刚指定的配置项和数据显示图表。
        myChart.setOption(option);
    }
    function initSelect() {
        $(document).ready(function () {
            $('#reservation').daterangepicker(null, function (start, end, label) {
                console.log(start.toISOString(), end.toISOString(), label);
            });
            document.getElementById("reservation").value = getCurrentMonth();
        });
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
    function getCurrentMonth() {
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
            <div style="float: left">

                <select id="gradeno" name="gradeno" class="selectpicker">

                </select>

                <button class="btn btn-primary" type="button" onclick="queryRClassMark()"><span
                        class="glyphicon glyphicon-eye-open"></span>查询
                </button>
            </div>
        </div>
    </div>
</div>
</div>
<div style="float: none;display: block;margin-left: auto;margin-right: auto;">
    <div style="height: 45%">
        <table class="table table-striped" id="class_mark" align="center"
               striped="true">
        </table>
    </div>
    <div id="main" style="width: 40%;height:37%;float:left;"></div>
    <div id="charts" style="width: 60%;height:49%;float: left;"></div>
</div>
</body>
</html>