<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>
<html>
<head>
    <title>--学生年度成绩统计--</title>

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

        queryEgradeListBYschool();

    });

    function indexFormatter(value, row, index) {
        return index + 1;
    }

    function operateFormatter(value, row, index) {
        var PySheDingID = row.bs_rowid;
// 利用 row 获取点击这行的ID

        return [
            "<button type='button' class='btn btn-info';' id='m-callback-this-start' onclick='btnEntry(" + row.bs_rowid + ")'  > <span class='glyphicon glyphicon-th-list' onclick='queryEgradeList()'></span>  </button>"].join('');

    }


    function getTableHead() {//动态生成表头，并调用数据进行绑定
        var questionColumns = [];


//                console.log(data);
        var t0 = {field: "0", title: "学科", align: "center"};

        questionColumns.push(t0);

        for (var i = 0; i < 12; i++) {

            var temp = {field: i + 1, title: (i + 1).toString() + '月', align: "center"};//手动拼接columns
            questionColumns.push(temp);
        }
        var t1 = {field: 13, title: "期中", align: "center"};
        questionColumns.push(t1);
        var t2 = {field: 14, title: "期末", align: "center"};
        questionColumns.push(t2);
        var t3 = {field: 15, title: "全年平均", align: "center"};
        questionColumns.push(t3);
        bandData(questionColumns)
    }

    function bandData(questionColumns) {//查询，并绑定数据至table
        var params = {};

        params.year = myear.value;
        params.studentID = $('#studentID').val();

        $.ajax({
            url: "/report/queryYearMarkStudent",         //请求后台的URL（*）
            type: "get",                    //请求方式（*）
            dataType: "json",
// 要传递的数据
            data: params,
            success: function (data) {
                $('#ds_table').bootstrapTable('destroy');
                $('#ds_table').bootstrapTable({
                    columns: questionColumns,
                    data: data.data
                });
                if (data.data != null) {
                    initMarkChart(data.data);
                }

            }
        })
    }

    function beforeRadarChart(subjectData, studentAvgData) {
        var params = {};
        params.year = myear.value;
        params.studentID = studentID.value;
        //获取班级平均成绩
        $.ajax({
            url: "/report/getAvgMarkByClass",
// 数据发送方式
            type: "get",
// 接受数据格式
            dataType: "json",

// 要传递的数据
            data: params,
            success: function (classData) {
                $.ajax({ //获取年级平均成绩
                    url: "/report/getAvgMarkByGrade",
// 数据发送方式
                    type: "get",
// 接受数据格式
                    dataType: "json",
// 要传递的数据
                    data: params,
                    success: function (gradeData) {
                        initRadarChart(subjectData, studentAvgData, classData.data, gradeData.data);
                    }
                });
            }
        })
    }

    function initRadarChart(subjectData, studentAvgData, classAvgData, gradeAvgData) {

        var radarChart = echarts.init(document.getElementById('radarChart'));
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

                var subjectsData=[];
                for (var i = 0; i < data.data.length; i++) {

                    var item = {
                        name: data.data[i].subjectname, max: data.data[i].totalscore
                    }
                    subjectsData.push(item);
                };
                option = {
                    title: {
                        subtext: '学生综合能力评定',
                        x: 'right',
                        y: 'top',
                        textAlign: 'center'
                    },
                    tooltip: {},
                    legend: {
                        orient: 'vertical',
                        x: 'left',
                        y: 'center',
                        data: ['当前学生', '班级平均', '年级平均']
                    },
                    radar: {
                        // shape: 'circle',
                        indicator: subjectsData
                    },
                    series: [{

                        type: 'radar',
                        // areaStyle: {normal: {}},
                        data: [
                            {
                                value: studentAvgData,
//                        value: [110, 120, 130, 120, 120, 240, 256],
                                name: '当前学生'
                            },
                            {
                                value: classAvgData,
//                        value: [78, 110, 110],
                                name: '班级平均'
                            },
                            {
                                value: gradeAvgData,
//                        value: [78, 110, 110],
                                name: '年级平均'
                            }
                        ]
                    }]
                };
                radarChart.setOption(option);
            },

            error: function (data) {

                alert("查询学科失败" + data);

            }
        })
//        var aveData = [];
//        var classAveData = [];
//
//        for (var i = 0; i < data.length; i++) {//当前学生平均分设定
//            aveData.push(data[i].markave);
//        }
//        for (var i = 0; i < ydata.length; i++) {//所在班级平均分设定
//            classAveData.push(ydata[i].avemark);
//        }
//        console.log(aveData);

    }

    function initMarkChart(data) {
        var subjectData = [];
        var studentAvgData = [];
        var serie = [];
//        console.log(data);
        for (var i = 0; i < data.length; i++) {
            subjectData.push(data[i][0]);
            studentAvgData.push(data[i][15]);
            var item = {
                name: data[i][0],
                type: 'line',
                data: [data[i][1], data[i][2], data[i][3], data[i][4], data[i][5], data[i][6], data[i][7], data[i][8], data[i][9], data[i][10], data[i][11], data[i][12], data[i][13], data[i][14], data[i][15]]
            }
            serie.push(item);
        }
//        console.log(classData);
        var markChart = echarts.init(document.getElementById('charts'));
        option = {
            title: {
//                text: '',
                subtext: '学生年度成绩统计--By MingLu I.T. Co., Ltd.',
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
                data: subjectData
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
                    axisLabel: {
                        interval: 0,
                        rotate: 60//倾斜度 -90 至 90 默认为0
                    },
//                    data: subjectNames
                    data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月', '期中', '期末', '平均分']
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
        beforeRadarChart(subjectData, studentAvgData);
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

                $.each(data.data, function (i) {

                    $('#gradeno.selectpicker').append("<option value=" + data.data[i].gradeno + ">" + data.data[i].gradename + "</option>");

                });

                $('#gradeno').selectpicker('refresh');
                getEclassList();

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

                getStudentList();
            },

            error: function (data) {

                alert("查询班级失败" + data);

            }
        })
    }
    function getStudentList() {

        var params = {};
        params.classno = $('#classno').val();
        $.ajax({
            url: "/estudent/viewEstudentByDTO",
// 数据发送方式
            type: "get",
// 接受数据格式
            dataType: "json",
// 要传递的数据
            data: params,
// 回调函数，接受服务器端返回给客户端的值，即result值
            success: function (data) {
                $('#studentID').empty();

                $('#classno').selectpicker();
//                console.log(data.data);
                $.each(data.data, function (i) {

                    $('#studentID.selectpicker').append("<option value=" + data.data[i].id + ">" + data.data[i].studentname + "</option>");


                });
                if (params.classno != 'noselected') {
                    $('#studentID').selectpicker('refresh');
                } else {
//                    $('#classno').selectpicker('destroy');
                    $('#studentID').val('noselected');
                }


            },

            error: function (data) {

                alert("查询班级失败" + data);

            }
        })
    }
</script>
<body>

<div class="container" style="float:center;width: 99%">
    <div class="well">

        <div class="input-prepend input-group">

            <div style="float: left">


                <input id="myear" name="myear" class="form-control" style="width: 200px;" placeholder="统计年度："/>
                <select id="gradeno" name="gradeno" class="selectpicker fit-width"
                        onchange="getEclassList();getStudentList()">

                </select>
                <select id="classno" name="classno" class="selectpicker fit-width" onchange="getStudentList()">

                </select>
                <select id="studentID" name="studentID" class="selectpicker show-tick fit-width" data-live-search="true">

                </select>

                <button class="btn btn-primary" type="button" onclick="getTableHead()"><span
                        class="glyphicon glyphicon-eye-open"></span>查询
                </button>

            </div>
        </div>
    </div>
    <div style="float: none;display: block;margin-left: auto;margin-right: auto;">
        <div id="bs_t" style="float: none;display: block;margin-left: auto;margin-right: auto;">
            <table class="table table-striped" id="ds_table" align="center"
                   striped="true" data-height="300">
                <thead>

                <tr>

                </tr>
                </thead>
            </table>
        </div>

    </div>
    <div id="charts" style="width:60%;height:46%;float: left;"></div>
    <div id="radarChart" style="width:40%;height:46%;float: left;"></div>
</div>
</body>
</html>
