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

    function indexFormatter(value, row, index) {
        return index + 1;
    }
    function operateFormatter(value, row, index) {
        var PySheDingID = row.bs_rowid;
// 利用 row 获取点击这行的ID

        return [
            "<button type='button' class='btn btn-info';' id='m-callback-this-start' onclick='btnEntry(" + row.bs_rowid + ")'  > <span class='glyphicon glyphicon-th-list' onclick='queryEgradeList()'></span>  </button>"].join('');

    }
    function queryYearMarkStudent() {
        var params = {};
        params.year = myear.value;
//        params.subjectno = $('#subjectno').val();
        params.studentname = studentname.value;
        params.studentno = studentno.value;
        $.ajax({
            url: "/report/queryYearMarkStudent",
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
                if (null != data.data) {
                    $.ajax({
                        url: "/esubjects/viewEsubjectsList",
// 数据发送方式
                        type: "get",
// 接受数据格式
                        dataType: "json",
// 要传递的数据
                        success: function (mdata) {//查询所有学科
//                            console.log(data);
                            $.ajax({
                                url: "/report/queryYearMark",
// 数据发送方式
                                type: "get",
// 接受数据格式
                                dataType: "json",
// 要传递的数据
                                data:params,
                                success: function (ydata) {//查询班级平均成绩
                            console.log(ydata);
                                    initRadarChart(mdata.data, data.data,ydata.data);
                                }
                            });
//                            initRadarChart(mdata.data, data.data);
                        }
                    });
//                    console.log(data);
                    initMarkChart(data.data);

                }
            },

            error: function (data) {
                alert("查询失败" + data);
            }
        })
    }
    function initRadarChart(mdata, data,ydata) {
        var radarChart = echarts.init(document.getElementById('radarChart'));
        var subjectData = [];
        var aveData = [];
        var classAveData=[];
        for (var i = 0; i < mdata.length; i++) {

            var item = {
                name: mdata[i].subjectname, max: mdata[i].totalscore
            }
            subjectData.push(item);
        }
        for (var i = 0; i < data.length; i++) {//当前学生平均分设定
            aveData.push(data[i].markave);
        }
        for (var i = 0; i < ydata.length; i++) {//所在班级平均分设定
            classAveData.push(ydata[i].avemark);
        }
        console.log(aveData);
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
                data: ['当前学生', '班级平均']
            },
            radar: {
                // shape: 'circle',
                indicator: subjectData
            },
            series: [{

                type: 'radar',
                // areaStyle: {normal: {}},
                data: [
                    {
                        value: aveData,
//                        value: [110, 120, 130, 120, 120, 240, 256],
                        name: '当前学生'
                    },
                    {
                        value:classAveData,
//                        value: [78, 110, 110],
                        name: '班级平均'
                    }
                ]
            }]
        };
        radarChart.setOption(option);
    }
    function initMarkChart(data) {
        var subjectData = [];
        var markData = [];
        var serie = [];
//        console.log(data);
        for (var i = 0; i < data.length; i++) {
            subjectData.push(data[i].subjectno);

            var item = {
                name: data[i].subjectno,
                type: 'line',
                data: [data[i].markone, data[i].marktwo, data[i].markthree, data[i].markfour, data[i].markfive, data[i].marksix, data[i].markseven, data[i].markeight, data[i].marknine, data[i].markten, data[i].markeleven, data[i].marktwelve, data[i].markmidterm, data[i].markfinal, data[i].markave]
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
    }
    function getSum(value) {
        var count = 0;
        for (var i in value) {
            count += value[i].markone;
        }
        return count;
    }
    function getSum2(value) {
        var count = 0;
        for (var i in value) {
            count += value[i].marktwo;
        }
        return count;
    }
    function getSum3(value) {
        var count = 0;
        for (var i in value) {
            count += value[i].markthree;
        }
        return count;
    }
    function getSum4(value) {
        var count = 0;
        for (var i in value) {
            count += value[i].markfour;
        }
        return count;
    }
    function getSum5(value) {
        var count = 0;
        for (var i in value) {
            count += value[i].markfive;
        }
        return count;
    }
    function getSum6(value) {
        var count = 0;
        for (var i in value) {
            count += value[i].marksix;
        }
        return count;
    }
    function getSum7(value) {
        var count = 0;
        for (var i in value) {
            count += value[i].markseven;
        }
        return count;
    }
    function getSum8(value) {
        var count = 0;
        for (var i in value) {
            count += value[i].markeight;
        }
        return count;
    }
    function getSum9(value) {
        var count = 0;
        for (var i in value) {
            count += value[i].marknine;
        }
        return count;
    }
    function getSum10(value) {
        var count = 0;
        for (var i in value) {
            count += value[i].markten;
        }
        return count;
    }
    function getSum11(value) {
        var count = 0;
        for (var i in value) {
            count += value[i].markeleven;
        }
        return count;
    }
    function getSum12(value) {
        var count = 0;
        for (var i in value) {
            count += value[i].marktwelve;
        }
        return count;
    }
    function getSumAve(value) {
        var count = 0;
        for (var i in value) {
            count += value[i].markave;
        }
        return count;
    }
    function getSumMidterm(value) {
        var count = 0;
        for (var i in value) {
            count += value[i].markmidterm;
        }
        return count;
    }
    function getSumFinal(value) {
        var count = 0;
        for (var i in value) {
            count += value[i].markfinal;
        }
        return count;
    }
</script>
<body>

<div class="container" style="float:center;width: 99%">
    <div class="well">

        <div class="input-prepend input-group">

            <div style="float: left">


                <input id="myear" name="myear" class="form-control" style="width: 200px;" placeholder="统计年度："/>
                <input id="studentname" name="studentname" class="form-control" style="width: 200px;"
                       placeholder="学生姓名："/>
                <input id="studentno" name="studentno" class="form-control" style="width: 200px;"
                       placeholder="学籍号："/>
                <button class="btn btn-primary" type="button" onclick="queryYearMarkStudent()"><span
                        class="glyphicon glyphicon-eye-open"></span>查询
                </button>

            </div>
        </div>
    </div>
    <div style="float: none;display: block;margin-left: auto;margin-right: auto;">
        <div id="bs_t" style="float: none;display: block;margin-left: auto;margin-right: auto;">
            <table class="table table-striped" id="ds_table" align="center" data-show-footer="true"
                   striped="true" data-height="300">
                <thead>

                <tr>
                    <th data-field="studentno" data-align="center">学籍号</th>
                    <th data-field="studentname" data-align="center">学生</th>
                    <th data-field="subjectno" data-align="center" data-footer-formatter="总分">学科</th>
                    <th data-field="markone" data-align="center" data-footer-formatter="getSum">1月</th>
                    <th data-field="marktwo" data-align="center" data-footer-formatter="getSum2">2月</th>
                    <th data-field="markthree" data-align="center" data-footer-formatter="getSum3">3月</th>
                    <th data-field="markfour" data-align="center" data-footer-formatter="getSum4">4月</th>
                    <th data-field="markfive" data-align="center" data-footer-formatter="getSum5">5月</th>
                    <th data-field="marksix" data-align="center" data-footer-formatter="getSum6">6月</th>
                    <th data-field="markseven" data-align="center" data-footer-formatter="getSum7">7月</th>
                    <th data-field="markeight" data-align="center" data-footer-formatter="getSum8">8月</th>
                    <th data-field="marknine" data-align="center" data-footer-formatter="getSum9">9月</th>
                    <th data-field="markten" data-align="center" data-footer-formatter="getSum10">10月</th>
                    <th data-field="markeleven" data-align="center" data-footer-formatter="getSum11">11月</th>
                    <th data-field="marktwelve" data-align="center" data-footer-formatter="getSum12">12月</th>

                    <th data-field="markmidterm" data-align="center" data-footer-formatter="getSumMidterm">期中</th>
                    <th data-field="markfinal" data-align="center" data-footer-formatter="getSumFinal">期末</th>
                    <th data-field="markave" data-align="center" data-footer-formatter="getSumAve">平均分</th>

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
