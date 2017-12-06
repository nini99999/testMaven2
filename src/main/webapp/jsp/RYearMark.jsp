<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>
<html>
<head>
    <title>--年度教学成绩统计--</title>

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
//        getTableHead();
        queryEgradeListBYschool();
        getEsubjects();
//        queryYearMark();
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
        var PySheDingID = row.bs_rowid;
// 利用 row 获取点击这行的ID

        return [
            "<button type='button' class='btn btn-info';' id='m-callback-this-start' onclick='btnEntry(" + row.bs_rowid + ")'  > <span class='glyphicon glyphicon-th-list' onclick='queryEgradeList()'></span>  </button>"].join('');

    }
    function getTableHead() {//动态生成表头，并调用数据进行绑定
        var questionColumns = [];


//                console.log(data);
                var t0 = {field: "0", title: "班级", align: "center"};
                var t1 = {field: "1", title: "教师", align: "center"};

//                var num = data.length + 1;
                questionColumns.push(t0);
                questionColumns.push(t1);
                for (var i = 0; i < 12; i++) {

                    var temp = {field: i + 2, title: (i+1).toString()+'月', align: "center"};//手动拼接columns
                    questionColumns.push(temp);
                }
        var t2 = {field: 14, title: "期中", align: "center"};
//                var t2 = {field: 15, title: "平均排名", align: "center"};
                questionColumns.push(t2);
        var t3 = {field: 15, title: "期末", align: "center"};
        questionColumns.push(t3);
        var t4 = {field: 16, title: "全年平均", align: "center"};
        questionColumns.push(t4);
                bandData(questionColumns)
//        $('#ds_table').bootstrapTable({
//            columns: questionColumns
//        });

    }
    function bandData(questionColumns) {//查询，并绑定数据至table
        var params = {};
        params.gradeno = $('#gradeno').val();
        params.year=myear.value;
        params.subjectno = $('#subjectno').val();//赋空值--取总成绩
        params.studentno='';
        params.studentname='';
        $.ajax({
            url: "/report/queryYearMark",         //请求后台的URL（*）
            type: "post",                    //请求方式（*）
//            contentType : 'application/json;charset=utf-8', //设置请求头信息

            dataType: "json",
// 要传递的数据
//            data: JSON.stringify(params),    //将Json对象序列化成Json字符串，JSON.stringify()原生态方法
            data: params,
            success: function (data) {
//                console.log(data);
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
//    function queryYearMark() {
//        var params = {};
//        params.year = myear.value;
//        params.subjectno = $('#subjectno').val();
//        params.gradeno = $('#gradeno').val();
//        $.ajax({
//            url: "/report/queryYearMark",
//// 数据发送方式
//            type: "get",
//// 接受数据格式
//            dataType: "json",
//// 要传递的数据
//            data: params,
//// 回调函数，接受服务器端返回给客户端的值，即result值
//            success: function (data) {
//
//                $("#ds_table").bootstrapTable('destroy');
//                $("#ds_table").bootstrapTable({data: data.data});//刷新ds_table的数据
//                if (null != data.data) {
////                    console.log(data);
//                    initMarkChart(data.data);
//                }
//            },
//
//            error: function (data) {
//                alert("查询失败" + data);
//            }
//        })
//    }

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
    function initMarkChart(data) {//各科成绩图表
        var classData = [];
        var markData = [];
        var serie = [];
        console.log(data);
        for (var i = 0; i < data.length; i++) {
            classData.push(data[i][0]);

            var item = {
                name: data[i][0],
                type: 'line',
                data: [data[i][2], data[i][3], data[i][4], data[i][5], data[i][6], data[i][7], data[i][8], data[i][9], data[i][10], data[i][11], data[i][12], data[i][13],data[i][14],data[i][15],data[i][16]]
            }
            serie.push(item);
        }
//        console.log(classData);
        var markChart = echarts.init(document.getElementById('charts'));
        option = {
            title: {
//                text: '',
                subtext: '年度教学成绩统计--By MingLu I.T. Co., Ltd.',
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
//                    data: subjectNames
                    data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月','期中','期末','全年平均']
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
</script>

<body>


<div class="container" style="float:center;width: 99%">
    <div class="well">

        <div class="input-prepend input-group">

            <div style="float: left">
                <select id="subjectno" name="subjectno" class="selectpicker">

                </select>

                <select id="gradeno" name="gradeno" class="selectpicker">

                </select>

                <input id="myear" name="myear" class="form-control" style="width: 200px;" placeholder="统计年度："/>

                <button class="btn btn-primary" type="button" onclick="getTableHead()"><span
                        class="glyphicon glyphicon-eye-open"></span>查询
                </button>
                <%--<button class="btn btn-primary" type="button" onclick="queryYearMark()"><span--%>
                        <%--class="glyphicon glyphicon-export"></span>导出--%>
                <%--</button>--%>
            </div>
        </div>
    </div>
    <div style="float: none;display: block;margin-left: auto;margin-right: auto;">
        <div id="bs_t" style="float: none;display: block;margin-left: auto;margin-right: auto;">
            <table class="table table-striped" id="ds_table" align="center"
                   striped="true" data-height="300">
                <thead>

                <tr>
                    <%--<th data-field="classno" data-align="center">班级</th>--%>
                    <%--<th data-field="teachername" data-align="center">教师</th>--%>
                    <%--<th data-field="markone" data-align="center">1月</th>--%>
                    <%--<th data-field="marktwo" data-align="center">2月</th>--%>
                    <%--<th data-field="markthree" data-align="center">3月</th>--%>
                    <%--<th data-field="markfour" data-align="center">4月</th>--%>
                    <%--<th data-field="markfive" data-align="center">5月</th>--%>
                    <%--<th data-field="marksix" data-align="center">6月</th>--%>
                    <%--<th data-field="markseven" data-align="center">7月</th>--%>
                    <%--<th data-field="markeight" data-align="center">8月</th>--%>
                    <%--<th data-field="marknine" data-align="center">9月</th>--%>
                    <%--<th data-field="markten" data-align="center">10月</th>--%>
                    <%--<th data-field="markeleven" data-align="center">11月</th>--%>
                    <%--<th data-field="marktwevle" data-align="center">12月</th>--%>

                    <%--<th data-field="markmidterm" data-align="center">期中</th>--%>
                    <%--<th data-field="markfinal" data-align="center">期末</th>--%>
                    <%--<th data-field="avemark" data-align="center">平均分</th>--%>
                    <%--<th data-field="avepositon" data-align="center">平均排名</th>--%>
                </tr>
                </thead>
            </table>
        </div>

    </div>
    <div id="charts" style="width:100%;height:46%;float: left;"></div>
</div>
</body>
</html>