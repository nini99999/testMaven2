<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>
<html>
<head>
    <title>试题库</title>
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
    function indexFormatter(value, row, index) {
        return index + 1;
    }
    function operateFormatter(value, row, index)
//row 获取这行的值 ，index 获取索引值
    {

        return [

            '<a class="edit"  href="javascript:void(0)" title="edit">',
            '<i class="glyphicon glyphicon-edit"></i>',
            '</a>'
        ].join('');
    }

    var ue;

    window.operateEvent = {
        'click .edit': function (e, value, row, index) {
            //init修改操作
            mqueryQTypeBySubject(row.subjectno);
            $("#myModal").modal('show');
            $('#sid').val(row.id);
            $('#squestionid').val(row.questionid);
            $('#mgradeno').selectpicker('val', row.gradeno);
            $('#msubjectno').selectpicker('val', row.subjectno);
//            $('#mquestiontype').selectpicker('val',row.questiontype);
//
//                        console.log('ss',row.questiontype);
//            $('#mquestiontype').selectpicker('refresh');
//            $('#mquestiontype').selectpicker('render');
//console.log('yy',$('#mquestiontype').val());
            ue = UE.getEditor('editor', {
                toolbars: [[
                    'fullscreen', 'source', '|',
                    'bold', 'italic', 'underline', '|', 'fontsize', '|', 'fontfamily', '|', 'kityformula', 'simpleupload', 'preview'
                ]]
            });
            ue.ready(function () {
                ue.setContent(row.question);
//               console.log(ue.getContent()) ;
            });

        }
    }
    function moidfQuestion() {
//        console.log('SSS');
        kfSubmit();
    }

    function kfSubmit() {

        ue.getKfContent(function (content) {

//            console.log('sb:', content);
            doModif(content);
//            form.submit();
        })
    }
    function doModif(content) {
        console.log($('#mquestiontype').val());
        if ('noselected' == $('#mquestiontype').val() || $('#mdifficulty').val() > 1) {

            alert('请选择题型！(难度系数应<=1)');
        } else {
            var params = {};
            params.id = $('#sid').val();
            params.question = content;
            params.answer = '';
            params.questionid = $('#squestionid').val();
            params.questiontype = $('#mquestiontype').val();
            params.creator = '';
//        params.createdate=null;
            params.gradeno = $('#mgradeno').val();
            params.subjectno = $('#msubjectno').val();
            params.difficulty = $('#mdifficulty').val();
//        params.schoolno = '';
            $.ajax({
                url: "/equestions/modifQuestion",
// 数据发送方式
                type: "post",
// 接受数据格式
                dataType: "json",
// 要传递的数据
                data: params,
// 回调函数，接受服务器端返回给客户端的值，即result值
                success: function (data) {
                    if (data.success == true) {
                        queryQuestions();
                        alert("保存成功！");
                    } else {
                        alert("保存失败：" + data.msg);
                    }
                }
                ,

                error: function (data) {
                    console.log("错误信息 :", data.msg);
//                    alert("保存失败"+data.msg);
                }
            })
        }
    }
    function queryQuestions() {
        var questionColumns = [];
        var params = {};
        params.gradeno = $('#gradeno').val();
        params.subjectno = $('#subjectno').val();
        params.questiontype = $('#questiontype').val();
        params.difficulty = $('#difficulty').val();
//        params.konwledge = $('#konwledge').val();
        $.ajax({
            url: "/equestions/viewQuestionList",
// 数据发送方式
            type: "get",
// 接受数据格式
            dataType: "json",
// 要传递的数据
            data: params,
// 回调函数，接受服务器端返回给客户端的值，即result值
            success: function (data) {
//                console.log(data.data);
                $('#ds_table').bootstrapTable('destroy');
                $('#ds_table').bootstrapTable({data: data.data});//刷新ds_table的数据
            },
            error: function (data) {
                alert("查询失败" + data);
            }
        })
    }
    function downloadFile(fileName) {
        var params = {};
        params.fileName=fileName;
        $.ajax({
            url: "/equestions/downloadFile",
// 数据发送方式
            type: "post",
// 接受数据格式
            dataType: "json",
// 要传递的数据
            data: params,
// 回调函数，接受服务器端返回给客户端的值，即result值
            success: function (data) {
//                console.log(data.data);
//                $('#ds_table').bootstrapTable('destroy');
//                $('#ds_table').bootstrapTable({data: data.data});//刷新ds_table的数据
                console.log("下载成功！"+data);
            },

            error: function (data) {
                console.log("System Error", data);
            }
        })

    }
    function exportQuestions() {
        document.getElementById("exportForm").submit();
    }

    function queryEgradeListBYschool() {
//        var params = {};
//
//        params.schoolno = $('#schoolno').val();

        $.ajax({
            url: "/egrade/viewEgradeListByschoolno",
// 数据发送方式
            type: "post",
// 接受数据格式
            dataType: "json",
// 要传递的数据
//            data: params,
// 回调函数，接受服务器端返回给客户端的值，即result值
            success: function (data) {
                $('#gradeno').empty();
                $('#mgradeno').empty();
                $('#gradeno').append("<option value='noselected'>请选择年级</option>");
                $('#mgradeno').append("<option value='noselected'>请选择年级</option>");
                $.each(data.data, function (i) {

                    $('#gradeno.selectpicker').append("<option value=" + data.data[i].gradeno + ">" + data.data[i].gradename + "</option>");
                    $('#mgradeno.selectpicker').append("<option value=" + data.data[i].gradeno + ">" + data.data[i].gradename + "</option>");

                });


//                $('#schoolno').selectpicker('render');
                $('#gradeno').selectpicker('refresh');
                $('#mgradeno').selectpicker('refresh');

            },

            error: function (data) {

                alert("查询年级失败" + data);

            }
        })
    }
    function getsubjectList() {//获取下拉学科列表
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
//                console.log(data);
//            $('#subjectno.selectpicker').empty();
                $.each(data.data, function (i) {

                    $('#subjectno.selectpicker').append("<option value=" + data.data[i].subjectno + ">" + data.data[i].subjectname + "</option>");
                    $('#msubjectno.selectpicker').append("<option value=" + data.data[i].subjectno + ">" + data.data[i].subjectname + "</option>");
                });

                $('#subjectno').selectpicker('refresh');
                $('#msubjectno').selectpicker('refresh');
                queryQTypeBySubject($('#subjectno').val());
            },

            error: function (data) {

                alert("查询学科失败" + data);

            }
        })
    }

    function getTermList() {
        $.ajax({
            url: "/etestpaper/getTermList",
// 数据发送方式
            type: "get",
// 接受数据格式
            dataType: "json",
// 要传递的数据
            data: 'data',
// 回调函数，接受服务器端返回给客户端的值，即result值
            success: function (data) {
//alert(data.data);
                /**
                 * 初始化学期下拉列表**/
                $('#term').empty();
                $.each(data.term, function (i) {
                    $('#term.selectpicker').append("<option value=" + [i] + ">" + data.term[i] + "</option>");
                });
                $('#term').selectpicker('refresh');
            },

            error: function (data) {
                alert("初始化学期失败" + data);
            }
        })
    }
    function queryQTypeBySubject(subj) {
//        console.log('subj',subj);
        var params = {};

        params.subjectno = subj;
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
    function mqueryQTypeBySubject(subj) {
//        console.log('subj',subj);
        var params = {};

        params.subjectno = subj;
        $.ajax({
            url: "/esubjectqt/viewEsubjectQT",
// 数据发送方式
            type: "post",
// 接受数据格式
            dataType: "json",
            data: params,

// 回调函数，接受服务器端返回给客户端的值，即result值
            success: function (data) {


                $('#mquestiontype').empty();


                $('#mquestiontype').append("<option value='noselected'>请选择题型</option>");


                $.each(data.data, function (i) {

                    $('#mquestiontype.selectpicker').append("<option value=" + data.data[i].questiontype + ">" + data.data[i].questiontypename + "</option>");

                });

                $('#mquestiontype').selectpicker('refresh');

            },


            error: function (data) {

                alert("查询试题类型失败" + data);

            }
        })
    }
    $(function () {
//        getschoolList();
        queryEgradeListBYschool();
        getsubjectList();
//        queryQTypeBySubject( $('#subjectno').val());
        getTermList();
        queryQuestions();
//        queryQTypeBySubject();
        var form = document.getElementById('mForm');
        //        var ue = UE.getEditor('editor');
        //        console.log(ue);

        form.onsubmit = function () {
            kfSubmit();
            return false;
        };

    })
</script>
<body>

<!-- 模态框（Modal） -->
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog" style="width: 85%">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                    &times;
                </button>
                <h4 class="modal-title" id="myModalLabel">
                    题目维护
                </h4>
            </div>
            <div class="modal-body">
                <form id="mForm" method="post">
                    <script id="editor" name="content" type="text/plain"></script>
                    <select id="mgradeno" name="mgradeno" class="selectpicker fit-width">
                    </select>
                    <select id="msubjectno" name="msubjectno" class="selectpicker fit-width"
                            onchange="mqueryQTypeBySubject( $('#msubjectno').val())">
                    </select>
                    <%--<select id="term" name="subjectno" class="selectpicker fit-width">--%>
                    <%--</select>--%>
                    <select id="mquestiontype" name="mquestiontype" class="selectpicker fit-width">
                    </select>

                    <input id="mdifficulty" type="text" style="width: auto" class="form-control" placeholder="难度系数：">


                    <input id="sid" name="sid" class="btn-default" hidden></input>
                    <input id="squestionid" name="squestionid" class="btn-default" hidden></input>

                </form>
            </div>
            <div class="modal-footer">

                <button class="btn btn-primary" type="button" onclick="moidfQuestion();">
                    <span class="glyphicon glyphicon-floppy-save"></span> 保存
                </button>
                <button class="btn btn-primary" type="button" data-dismiss="modal">
                    <span class="glyphicon glyphicon-log-out"></span> 关闭
                </button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal -->
</div>
<!--modal end-->
<form action="/equestions/exportQuestions" method="post" id="exportForm" name="exportForm">

<div class="container" style="float:center;width: 99%;">
    <div class="well">
        <div class="input-prepend input-group">


            <%--<select id="schoolno" name="schoolno" class="selectpicker fit-width"--%>
            <%--onchange="queryEgradeListBYschool()">--%>
            <%--</select>--%>

            <select id="gradeno" name="gradeno" class="selectpicker fit-width">
            </select>
            <select id="subjectno" name="subjectno" class="selectpicker fit-width"
                    onchange="queryQTypeBySubject($('#subjectno').val())">
            </select>
            <%--<select id="term" name="subjectno" class="selectpicker fit-width">--%>
            <%--</select>--%>
            <select id="questiontype" name="questiontype" class="selectpicker fit-width">
            </select>


            <div style="float: left">
                <input id="konwledge" type="text" style="width: auto" class="form-control" placeholder="知识点：">

                <input id="difficulty" type="text" style="width: auto" class="form-control" placeholder="难度系数：">
                <button class="btn btn-primary" type="button" onclick="queryQuestions()">
                    <span class="glyphicon glyphicon-eye-open"></span>查询
                </button>

                <button class="btn btn-primary" type="button" onclick="exportQuestions();"><span
                        class="glyphicon glyphicon-export"></span>导出word
                </button>

            </div>
        </div>
    </div>
</form>

    <div id="bs_t" style="float: none;display: block;margin-left: auto;margin-right: auto;">
        <table class="table table-striped" id="ds_table" align="center"
               striped="true"
               data-pagination="true" sidePagination="server" data-click-to-select="true">
            <thead>
            <tr>

                <%--<th data-field="estate" data-checkbox="true"></th>--%>
                <th data-field="index" data-align="center" data-formatter="indexFormatter">序号</th>

                <%--<th data-field="questionid" hidden>题目ID</th>--%>
                <th data-field="question">题目</th>
                <th data-field="id" data-align="center" data-formatter="operateFormatter" data-events="operateEvent">编辑
                </th>
            </tr>
            </thead>
        </table>
    </div>
</div>
</body>
</html>