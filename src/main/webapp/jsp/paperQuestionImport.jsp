<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>
<html>
<head>
    <title>fileInput</title>
    <link rel="stylesheet" href="<%=path%>/css/main.css">
    <link href="<%=path%>/bootstrap/bootstrap.min.css" rel="stylesheet"/>
    <link href="<%=path%>/fileInput/css/fileinput.css" media="all" rel="stylesheet" type="text/css"/>
    <link href="<%=path%>/fileInput/themes/explorer/theme.css" media="all" rel="stylesheet" type="text/css"/>
    <script src="<%=path%>/bootstrap/jquery.min.js"></script>
    <!-- Include the plugin's CSS and JS: -->
    <script type="text/javascript" src="<%=path%>/bootstrap/bootstrap-multiselect.js"></script>
    <link rel="stylesheet" href="<%=path%>/bootstrap/bootstrap-multiselect.css" type="text/css"/>

    <script type="text/javascript" src="<%=path%>/bootstrap/bootstrap-select.js"></script>
    <link rel="stylesheet" href="<%=path%>/bootstrap/bootstrap-select.css" type="text/css"/>
    <link rel="stylesheet" type="text/css" media="all" href="<%=path%>/bootstrap/daterangepicker-bs3.css"/>

    <script src="<%=path%>/fileInput/js/plugins/sortable.js" type="text/javascript"></script>
    <script src="<%=path%>/fileInput/js/fileinput.js" type="text/javascript"></script>
    <script src="<%=path%>/fileInput/js/locales/zh.js" type="text/javascript"></script>
    <script src="<%=path%>/fileInput/themes/explorer/theme.js" type="text/javascript"></script>
    <script src="<%=path%>/bootstrap/bootstrap.min.js" type="text/javascript"></script>
    <script src="<%=path%>/utils/xdate.js"></script>
    <script type="text/javascript" src="<%=path%>/bootstrap/moment.js"></script>
    <script type="text/javascript" src="<%=path%>/bootstrap/daterangepicker.js"></script>
</head>
<script type="text/javascript">
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

                $.each(data.data, function (i) {

                    $('#subjectno.selectpicker').append("<option value=" + data.data[i].subjectno + ">" + data.data[i].subjectname + "</option>");

                });

                $('#subjectno').selectpicker('refresh');
//                queryQTypeBySubject();
            },

            error: function (data) {

                alert("查询学科失败" + data);

            }
        })

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
                getTestPaperList();

            },

            error: function (data) {

                alert("查询年级失败" + data);

            }
        })
    }

    function initFileInput(ctrlName, uploadUrl) {
        var control = $('#' + ctrlName);
        control.fileinput({
            language: 'zh', //设置语言
            uploadUrl: uploadUrl,  //上传地址
            showUpload: false, //是否显示上传按钮
            showRemove: true,
            dropZoneEnabled: false,
            showCaption: true,//是否显示标题
            allowedPreviewTypes: ['image'],//必须不设置，否则无法控制doc类型
//            allowedFileTypes: ['application/msword'],//必须不设置，否则无法控制doc类型
            allowedFileExtensions: ['doc'],
            maxFileSize: 20480,
            maxFileCount: 1,
            uploadExtraData: function (previewId, index) {   //额外参数的关键点
//                console.log($('#gradeno').val());
                return {
                    "subjectno": $('#subjectno').val(),
                    "gradeno": $('#gradeno').val(),
                    "paperid": $('#paperid').val()
                };
            }
        }).on("filebatchselected", function (event, files) {
            $(this).fileinput("upload");
        })
            .on("fileuploaded", function (event, data) {
                $("#path").attr("value", data.response);
            });
        ;
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
    $(function () {
        var path = "/fileUpload/paperQuestionUpload";
        initFileInput("myfile", path);
        initDateSelect();

        getEsubjects();
//        queryQTypeBySubject();


    })
</script>
<body>
<div class="container">
    <hr/>

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

            <select id="gradeno" name="gradeno" class="selectpicker  fit-width" onchange="getTestPaperList()"></select>

            <select id="subjectno" name="subjectno" class="selectpicker fit-width"  onchange="getTestPaperList()"></select>
            <%--选择题型：--%>
            <%--<select id="questiontype" name="questiontype" class="selectpicker fit-width"></select>--%>
            <select id="paperid" name="paperid" class="selectpicker fit-width"></select>
        </div>
    </div>
    <hr/>
</div>
<div class="form-group">

    <div class="col-sm-6" style="float: none;display: block;margin-left: auto;margin-right: auto;">
        <label class="col-sm-3 control-label">doc上传:</label>
        <input id="myfile" name="myfile" type="file" data-show-caption="true">
        <p>1).支持doc格式(docx请另存为.doc格式后上传)，文件最大不超过20M;</p>
        <p>2).文档中每道题目必须保证设置唯一的连续题号(非手写，必须由word中的编号生成，如1. 2. 3.等)，并设置题号格式为"标题";</p>
        <p>3).文档中可以包括文字、图片及标准公式（在word中输入的公式），其它格式请转为图片(建议png格式)后再加入至word中;</p>
        <p>4).试卷试题导入必须严格遵照流程及模板，否则无法导入；</p>
        <p>5).文档中其它格式(如pdf等)转换技巧：</p>
        <p>(1)word中复制，</p>
        <p>(2)在"画图"中粘贴，</p>
        <p>(3)"另存为"：文件名.png,</p>
        <p>(4)word中插入"文件名.png"</p>
    </div>

</div>
<p style="text-align: center">
<img src="<%=path%>/fileInput/img/importFlow.jpg" /></p>
</body>
</html>
