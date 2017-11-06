<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>
<html>
<head>
    <title>试卷试题管理</title>
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
            console.log('row', row.questiontype);
            $("#myModal").modal('show');
            $('#sid').val(row.id);
            $('#spaperid').val(row.paperid);
            $('#spaperquestionno').val(row.paperquestionno);
            $('#squestionpoints').val(row.questionpoints);

            $('#mdifficulty').val(row.difficulty);
            $('#squestionid').val(row.questionid);
            $('#mgradeno').selectpicker('val', row.gradeno);
            $('#msubjectno').selectpicker('val', row.subjectno);

//            $('#mquestiontype').selectpicker('val',row.questiontype);
////
//                        console.log('ss',row.gradeno);
//            console.log('questiontype',row.questiontype);
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
                ue.setHeight(250);
//               console.log(ue.getContent()) ;
            });
            $('#myModal').on('shown.bs.modal', function () {
                // 执行一些动作...
                $('#mquestiontype').selectpicker('val', row.questiontype);
            })
        }
    }
    function add() {
        $("#myModal").modal('show');
        $('#sid').val('');
        $('#spaperid').val($('#paperid').val());
        $('#spaperquestionno').val('');
        $('#squestionpoints').val('');

        $('#mdifficulty').val('');
        $('#squestionid').val('');
        $('#mgradeno').selectpicker('val', $('#gradeno').val());
        $('#msubjectno').selectpicker('val', $('#subjectno').val());
        mqueryQTypeBySubject($('#msubjectno').val());
        ue = UE.getEditor('editor', {
            toolbars: [[
                'fullscreen', 'source', '|',
                'bold', 'italic', 'underline', '|', 'fontsize', '|', 'fontfamily', '|', 'kityformula', 'simpleupload', 'preview'
            ]]
        });
        ue.ready(function () {
            ue.setContent('');
            ue.setHeight(250);
//               console.log(ue.getContent()) ;
        });
        $('#myModal').on('shown.bs.modal', function () {
            // 执行一些动作...
            $('#mquestiontype').selectpicker('val', 'noselected');
        })
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
            params.paperid = $('#spaperid').val();
            params.spaperquestionno = $('#spaperquestionno').val();

            params.questionid = $('#squestionid').val();
            params.questiontype = $('#mquestiontype').val();
            params.creator = '';
            params.questionpoints = $('#squestionpoints').val();
            params.gradeno = $('#mgradeno').val();
            params.subjectno = $('#msubjectno').val();
            params.difficulty = $('#mdifficulty').val();
            params.paperquestionno = $('#spaperquestionno').val();
//        params.schoolno = '';
            $.ajax({
                url: "/paperQuestion/mergePaperQuestion",
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
        if (!$('#paperid').val()) {
            alert('请选择试卷！');

        } else {
            var params = {};
            params.gradeno = $('#gradeno').val();
            params.subjectno = $('#subjectno').val();
//            console.log("gradeno", $('#gradeno').val());
//            console.log("subj", $('#subjectno').val());
            params.paperid = $('#paperid').val();
            params.startDate = $('#reservation').val().replace(/-/g, "");
            params.endDate = $('#reservation').val().replace(/-/g, "");
//        console.log("sss", $('#reservation').val().replace(/-/g, ""));

            $.ajax({
                url: "/paperQuestion/getPaperQuestionList",
// 数据发送方式
                type: "get",
// 接受数据格式
                dataType: "json",
// 要传递的数据
                data: params,
// 回调函数，接受服务器端返回给客户端的值，即result值
                success: function (data) {
                    console.log(data.data);
                    $('#ds_table').bootstrapTable('destroy');
                    $('#ds_table').bootstrapTable({data: data.data});//刷新ds_table的数据
                },
                error: function (data) {
                    alert("查询失败" + data);
                    console.log(data.data);
                }
            })
        }
    }
    function getTestPaperList() {
        var params = {};
        params.gradeno = $('#gradeno').val();
        params.subjectno = $('#subjectno').val();
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
                    $('#paperid.selectpicker').append("<option value=" + data.data[i].tpno + ">" + data.data[i].tpname + "</option>");
                });
                $('#paperid').selectpicker('refresh');
                queryQuestions();
            },
            error: function (data) {
                alert("查询失败" + data);
                console.log("error", data);
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
    function queryEgradeListBYschool() {
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
//                getTestPaperList();
//                var res=$('#gradeno').val();
//                return res;
                getsubjectList();
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
//                queryQTypeBySubject($('#subjectno').val());
//                getTestPaperList();
                getTestPaperList();
            },
            error: function (data) {
                alert("查询学科失败" + data);
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
    function exportQuestions() {
//        console.log('val',$('#paperid').val());
//        if ($('#paperid').val().length == 0 || 'noselected' == $('#paperid').val()) {
        if (!$('#paperid').val()) {
            alert('请选择试卷！');

        } else {

//
            var form = document.getElementById("exportForm");

            form.submit();
        }
    }

    $(function () {
        initDateSelect();
//        console.log($('#reservation').val());

//       alert(gradeno);

//        console.log("gradeno", $('#gradeno').val());
//        console.log("subj", $('#subjectno').val());

//        queryQuestions();
        var form = document.getElementById('mForm');
        //        var ue = UE.getEditor('editor');
        //        console.log(ue);
        form.onsubmit = function () {
            kfSubmit();
            return false;
        };


//        $('reservation').on('input propertychange', function () {
//            getTestPaperList();
//        });
    })
</script>
<body>

<!-- 模态框（Modal） -->
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog" style="width: 90%">
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
                <form id="mForm" method="post" class="form-inline">

                    <script id="editor" name="content" type="text/plain"></script>

                    <hr/>
                    <select id="mgradeno" name="mgradeno" class="selectpicker fit-width">
                    </select>
                    <select id="msubjectno" name="msubjectno" class="selectpicker fit-width"
                            onchange="mqueryQTypeBySubject( $('#msubjectno').val())">
                    </select>
                    <select id="mquestiontype" name="mquestiontype" class="selectpicker fit-width">
                    </select>
                    <hr/>
                    <label for="mdifficulty">难度系数：</label><input id="mdifficulty" class="form-control"
                                                                 placeholder="难度系数：">
                    <label for="squestionpoints">分值：</label><input id="squestionpoints" name="squestionpoints"
                                                                   class="form-control" placeholder="分值：">

                    <input id="sid" name="sid" class="btn-default" hidden>
                    <input id="squestionid" name="squestionid" class="btn-default" hidden>
                    <input id="spaperid" name="spaperid" class="btn-default" hidden>
                    <label for="spaperquestionno">题号：</label><input id="spaperquestionno"
                                                                    name="spaperquestionno" type="text"
                                                                    style="width: auto" class="form-control"
                                                                    placeholder="题号：">
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
        </div>
    </div>
</div>
<!--modal end-->
<form action="/paperQuestion/exportQuestions" method="post" id="exportForm" name="exportForm">
    <input id="startDate" hidden/>
    <input id="endDate" hidden/>
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
                    <select id="gradeno" name="gradeno" class="selectpicker fit-width" onchange="getTestPaperList()">
                    </select>
                    <select id="subjectno" name="subjectno" class="selectpicker fit-width"
                            onchange="getTestPaperList()">
                    </select>
                    <select id="paperid" name="paperid" class="selectpicker fit-width" onchange="queryQuestions()">
                    </select>

                    <button class="btn btn-primary" type="button" onclick="queryQuestions()">
                        <span class="glyphicon glyphicon-eye-open"></span>查询
                    </button>

                    <button class="btn btn-primary" type="button" onclick="add()">
                        <span class="glyphicon glyphicon-floppy-save"></span>添加
                    </button>

                    <button class="btn btn-primary" type="button" onclick="exportQuestions();"><span
                            class="glyphicon glyphicon-export"></span>导出
                    </button>

                </div>
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