<%--
  Created by IntelliJ IDEA.
  User: jenny
  Date: 2017/4/12
  Time: 下午2:13
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
<script src="<%=path%>/echarts/echarts.js"></script>
<script type="text/javascript" src="<%=path%>/bootstrap/jquery.min.js"></script>
<head>
    <title>Title</title>
</head>
<script type="text/javascript">
    $(document).ready(function () {
        intiEcharts();
    });
    function intiEcharts() {
        var myChart = echarts.init(document.getElementById('main'));
//        app.title = '坐标轴刻度与标签对齐';

        option = {
            title: {
                text: '班级平均成绩排名',
                subtext: 'Minglu technology.Inc'
            },
            color: ['#3398DB'],
            tooltip : {
                trigger: 'axis',
                axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                    type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                }
            },
            grid: {
                left: '3%',
                right: '4%',
                bottom: '3%',
                containLabel: true
            },
            xAxis : [
                {
                    type : 'category',
                    data : ['一班', '二班', '三班', '四班', '五班', '六班', '七班', '八班', '九班',],
                    axisTick: {
                        alignWithLabel: true
                    }
                }
            ],
            yAxis : [
                {
                    type : 'value'
                }
            ],
            series : [
                {
                    name:'总分',
                    type:'bar',
                    barWidth:40,
                    data:[555, 552, 576, 500, 461, 568, 534,600,465]
                },

            ]
        };

        // 使用刚指定的配置项和数据显示图表。
        myChart.setOption(option);
    }
</script>
<body>
<div id="main" style="width: 90%;height:40%;margin: auto"></div>
</body>
</html>
