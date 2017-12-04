<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>
<html>
<head>
    <title>--班级成绩统计--</title>


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
        $('#paperid').multiselect();
        initSelect();

        queryEgradeListBYschool();//填充年级下拉列表

    });

    function getCurrentMonth() {
        var firstDate = new Date();
        firstDate.setDate(1); //第一天
        var endDate = new Date(firstDate);
        endDate.setMonth(firstDate.getMonth() + 1);
        endDate.setDate(0);
        return new XDate(firstDate).toString('yyyy-MM-dd') + " - " + new XDate(endDate).toString('yyyy-MM-dd');
//        alert("第一天：" + new XDate(firstDate).toString('yyyy-MM-dd') + " 最后一天：" + new XDate(endDate).toString('yyyy-MM-dd'));
    }

    function queryRMarkArea() {//动态生成表头，并调用数据进行绑定
        var questionColumns = [];
        $.ajax({
            url: "/report/getMarkArea",
// 数据发送方式

            type: "get",
// 接受数据格式
            dataType: "json",
// 要传递的数据
            data: 'data',
            success: function (data) {

//                console.log(data);
                var t0 = {field: "0", title: "班级", align: "center"};
                var num = data.length + 1;
                questionColumns.push(t0);
                for (var i = 0; i < data.length; i++) {

                    var temp = {field: i + 1, title: data[i], align: "center"};//手动拼接columns
                    questionColumns.push(temp);
                }
                var t1 = {field: num, title: "考生数", align: "center"};
                questionColumns.push(t1);
                bandData(questionColumns, data)
            }
        })
    }

    function bandData(questionColumns, paramData) {//查询班级成绩表，并绑定数据至table
        var params = {};
        params.gradeno = $('#gradeno').val();
        params.tpno='';
        params.subjectno = '';//赋空值--取总成绩
        params.tpnoString=JSON.stringify($('#paperid').val());
        console.log('dbb',params.tpnoString);
        console.log("params",params);
        $.ajax({
            url: "/report/queryRMarkArea",         //请求后台的URL（*）
            type: "post",                    //请求方式（*）
//            contentType : 'application/json;charset=utf-8', //设置请求头信息

            dataType: "json",
// 要传递的数据
//            data: JSON.stringify(params),    //将Json对象序列化成Json字符串，JSON.stringify()原生态方法
            data: params,
            success: function (data) {
//                console.log(data);
                $('#class_mark').bootstrapTable('destroy');
                $('#class_mark').bootstrapTable({
                    columns: questionColumns,
                    data: data.data
                });
                if (data.data != null) {
                    initEcharts(data.data, paramData);
                }

            }
        })
    }

    function initSelect() {
        $(document).ready(function () {
            $('#reservation').daterangepicker(null, function (start, end, label) {
//                console.log(start.toISOString(), end.toISOString(), label);
                getTestPaperList();
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

    function initEcharts(data, paramData) {
        var classData = [];

        var areaData = [];
        var serie = [];
        for (var i = 0; i < data.length; i++) {
            classData.push(data[i][0]);
            areaData = data[i];
            areaData.splice(0, 1);
            areaData.splice(data[i].length - 1, 1);

            var item = {
                name: classData[i],
                type: 'bar',
                barWidth: '10%',
                data: areaData
            }
            serie.push(item);
        }
        ;

        var myChart = echarts.init(document.getElementById('main'));
//console.log(data);

        option = {
            title: {
//                text: '',
                subtext: '各班总成绩分布图--By MingLu I.T. Co., Ltd.',
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
            label: {
                normal: {
                    show: true,
                    position: 'top'
                }
            },
            legend: {
                data: classData
//                data:['高1-1','高1-2','高1-3','高1-4','高1-5','高1-6']
            },
            grid: {
                left: '3%',
                right: '4%',
                bottom: '3%',
                containLabel: true
            },
            xAxis: [
                {
                    type: 'category',
                    data: paramData
//                    data : ['500-','500~550','550~600','600~650','650~700','700~750']
                }
            ],
            yAxis: [
                {
                    type: 'value'
                }
            ],

            series: serie
            // 使用刚指定的配置项和数据显示图表。

        }
        myChart.setOption(option);
    }

    function getTestPaperList() {
        var params = {};
        params.gradeno = $('#gradeno').val();
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
//                    $('#paperid.multiselect').append("<option value=" + data.data[i].tpno + ">" + data.data[i].tpname + "</option>");
                    $('#paperid').append($('<option></option>').text(data.data[i].tpname).val(data.data[i].tpno));
                });
                $('#paperid').multiselect('rebuild');
                $('#paperid').multiselect('refresh');
            },
            error: function (data) {
                alert("查询失败" + data);
                console.log("error", data);
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
                   class="form-control"
                   value="2017-1-1 - 2017-12-30"/>
            <div style="float: left">

                <select id="gradeno" name="gradeno" class="selectpicker" onchange="getTestPaperList()">

                </select>
                <select id="paperid" name="paperid" class="form-control" multiple="multiple" style="include">

                </select>

                <button class="btn btn-primary" type="button" onclick="queryRMarkArea()"><span
                        class="glyphicon glyphicon-eye-open"></span>查询
                </button>
            </div>
        </div>
    </div>

    <div style="float: none;display: block;margin-left: auto;margin-right: auto;">
        <table class="table table-striped" id="class_mark" align="center"
               striped="true" data-height="300">
        </table>
    </div>
</div>
<div id="main" style="width: 100%;height:35%;margin: auto"></div>
</body>
</html>