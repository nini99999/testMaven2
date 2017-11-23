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

    <script src="<%=path%>/fileInput/js/plugins/sortable.js" type="text/javascript"></script>
    <script src="<%=path%>/fileInput/js/fileinput.js" type="text/javascript"></script>
    <script src="<%=path%>/fileInput/js/locales/zh.js" type="text/javascript"></script>
    <script src="<%=path%>/fileInput/themes/explorer/theme.js" type="text/javascript"></script>
    <script src="<%=path%>/bootstrap/bootstrap.min.js" type="text/javascript"></script>

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
                queryQTypeBySubject();
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

            },

            error: function (data) {

                alert("查询年级失败" + data);

            }
        })
    }
    function queryQTypeBySubject() {
        var params = {};

        params.subjectno = $('#subjectno').val();
        $.ajax({
            url: "/esubjectqt/viewEsubjectQT",
// 数据发送方式
            type: "post",
// 接受数据格式
            dataType: "json",
            data: params,

// 回调函数，接受服务器端返回给客户端的值，即result值
            success: function (data) {
                $('#questiontype').empty();

                $('#questiontype').append("<option value='noselected'>请选择题型</option>");
                $.each(data.data, function (i) {

                    $('#questiontype.selectpicker').append("<option value=" + data.data[i].questiontype + ">" + data.data[i].questiontypename + "</option>");

                });

                $('#questiontype').selectpicker('refresh');

            },

            error: function (data) {

                alert("查询试题类型失败" + data);

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
                console.log($('#gradeno').val());
                return {"subjectno": $('#subjectno').val(), "gradeno": $('#gradeno').val(),"questiontype":$('#questiontype').val()};
            }
        }).on("filebatchselected", function (event, files) {
            $(this).fileinput("upload");
        })
            .on("fileuploaded", function (event, data) {
                $("#path").attr("value", data.response);
            });
        ;
    }

    $(function () {
        var path = "/fileUpload/springUpload";
        initFileInput("myfile", path);
        queryEgradeListBYschool();
        getEsubjects();
        queryQTypeBySubject();

    })
</script>
<body>
<div class="container">
    <hr/>
    选择年级：
    <select id="gradeno" name="gradeno" class="selectpicker  fit-width" >

    </select>
    选择学科：
    <select id="subjectno" name="subjectno" class="selectpicker fit-width" onchange="queryQTypeBySubject()">

    </select>
    选择题型：
    <select id="questiontype" name="questiontype" class="selectpicker fit-width">

    </select>
    <hr/>
</div>
<div class="form-group">

    <div class="col-sm-6" style="float: none;display: block;margin-left: auto;margin-right: auto;">
        <label class="col-sm-3 control-label">doc上传:</label>
        <input id="myfile" name="myfile" type="file" data-show-caption="true">
        <p>1).支持doc格式(docx请另存为.doc格式后上传)，文件最大不超过20M;</p>
        <p>2).文档中每道题目必须保证设置唯一的连续题号(非手写，必须由word中的编号生成，如1. 2. 3.等)，并设置题号格式为"标题";</p>
        <p>3).文档中可以包括文字、图片及标准公式（在word中输入的公式），其它格式请转为图片(建议png格式)后再加入至word中;</p>
        <p>4).基础题库每次导入都必须指定一种导入题型，多种题型请分多次进行导入</p>
        <p>5).如试卷中包括答案，应在每道题的末尾（另起一行），以"【答案】"标识，并在"【答案】"后直接填写答案；</p>
    </div>
</div>
</body>
</html>
