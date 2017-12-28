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
    <script type="text/javascript" src="<%=path%>/bootstrap/treeView/bootstrap-treeview-master.js"></script>
    <link rel="stylesheet" href="<%=path%>/bootstrap/treeView/bootstrap-treeview.min.css" type="text/css"/>

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

    <link rel="stylesheet" href="<%=path%>/bootstrap/dialog/bootstrap-dialog.min.css" type="text/css"/>
    <script type="text/javascript" src="<%=path%>/bootstrap/dialog/bootstrap-dialog.min.js"></script>
</head>
<script type="text/javascript">
    function indexFormatter(value, row, index) {
        return index + 1;
    }

    function operateFormatter(value, row, index) {//row 获取这行的值 ，index 获取索引值
        return [
            '<a class="edit"  href="javascript:void(0)" title="edit">',
            '<i class="glyphicon glyphicon-edit"></i>',
            '</a>'
        ].join('');
    }

    function answerFormatter(value, row, index) {
        return [
            '<a class="answer"  href="javascript:void(0)" title="答案">',
            '<i class="glyphicon glyphicon-info-sign"></i>',
            '</a>'
        ].join('');
    }

    function saveAnswer() {
        var params = {};
        params.id = $('#qid').val();
        params.answer = $('#answer').val();
        $.ajax({
            url: "/paperQuestion/saveAnswer",
// 数据发送方式
            type: "post",
// 接受数据格式
            dataType: "json",
// 要传递的数据
            data: params,
// 回调函数，接受服务器端返回给客户端的值，即result值
            success: function (data) {
                queryQuestions();
                alert("记录已保存！");
            }
            ,
            error: function (data) {
                console.log("错误信息 :", data);
                alert("保存失败" + data);
            }
        })
    }

    var ue;
    window.operateEvent = {
        'click .edit': function (e, value, row, index) {

            //init修改操作
            $("#knowledgeText").val('');
            mqueryQTypeBySubject(row.subjectno, row.questiontype);//生成试题类型，并设置默认选中
            $("#myModal").modal('show');
            $('#sid').val(row.id);
            $('#squestionid').val(row.questionid);

            $('#mgradeno').selectpicker('val', row.gradeno);
            $('#msubjectno').selectpicker('val', row.subjectno);

            ue = UE.getEditor('editor', {
                toolbars: [[
                    'source', '|', 'bold', 'italic', 'underline', '|', 'fontsize', '|', 'fontfamily', '|', 'kityformula', 'simpleupload', 'preview'
                ]]
            });
            ue.ready(function () {
                ue.setContent(row.question);
                ue.setHeight(250);
//               console.log(ue.getContent()) ;
            });

            var count = 0;
            $('#myModal').on('shown.bs.modal', function () {
                if (++count == 1) {
//                console.log('questionid', $('#squestionid').val());
                    getOrCreateRoot(row.questionid);//当modal对用户完全可见后，创建树并设置隐藏，当点击选择知识点时再打开
//                getQuestionKnowledge(row.id);//当modal对用户完全可见后，创建树并设置隐藏，当点击选择知识点时再打开
                    hideDIV();
                }
            })
        },
        'click .answer': function (e, value, row, index) {
//            console.log('rrrr',row);
            //查询指定id的答案并赋值
            var params = {};
            params.questionid = row.questionid;
            $.ajax({
                url: "/paperQuestion/getAnswer",
// 数据发送方式
                type: "get",
// 接受数据格式
                dataType: "json",
// 要传递的数据
                data: params,
// 回调函数，接受服务器端返回给客户端的值，即result值
                success: function (data) {
//                    console.log('3sd',data);
                    $('#answer').val(data.questionAnswer);
                    $("#answerModal").modal('show');
                    $('#qid').val(row.questionid);
                }
                ,
                error: function (data) {
                    console.log("错误信息 :", data);
                    alert("保存失败" + data);
                }
            })
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

        if ('noselected' == $('#mquestiontype').val() || $('#mdifficulty').val() > 1) {
//            alert('请选择题型！(难度系数应<=1)');
            BootstrapDialog.show({
                type: BootstrapDialog.TYPE_DANGER,
                title: '不符合规则：',
                message: '请选择题型！(难度系数应<=1)'
            });
        } else {
            addSelected();
            var params = {};
            params.id = $('#sid').val();
            params.question = content;
            params.answer = '';
            params.questionid = $('#squestionid').val();
            params.questiontype = $('#mquestiontype').val();
            params.creator = '';
            params.gradeno = $('#mgradeno').val();
            params.subjectno = $('#msubjectno').val();
            params.difficulty = $('#mdifficulty').val();
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
                        BootstrapDialog.show({
                            type: BootstrapDialog.TYPE_SUCCESS,
                            title: '维护题目及信息_操作提示：',
                            message: data.msg
                        });
                    } else {
                        BootstrapDialog.show({
                            type: BootstrapDialog.TYPE_DANGER,
                            title: '维护题目及信息_错误信息：',
                            message: data.msg
                        });
                    }
                }
                ,
                error: function (data) {
                    BootstrapDialog.show({
                        type: BootstrapDialog.TYPE_DANGER,
                        title: '维护题目及信息_错误信息：',
                        message: data.msg
                    });
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
        params.fileName = fileName;
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
                console.log("下载成功！" + data);
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

    function mqueryQTypeBySubject(subj, questionType) {

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
                $('#mquestiontype').selectpicker('val', questionType);
            },
            error: function (data) {
                alert("查询试题类型失败" + data);
            }
        })
    }

    function getOrCreateRoot(questionID) {
//        console.log('questionID', questionID);
        var params = {};

        params.gradeno = $('#mgradeno').val();
        params.subjectno = $('#msubjectno').val();

        $.ajax({

            url: "/knowledge/getOrCreatRoot",

// 数据发送方式
            type: "post",
// 接受数据格式
            dataType: "json",
// 要传递的数据
            data: params,
// 回调函数，接受服务器端返回给客户端的值，即result值
            success: function (data) {//调用url成功时有效
                $('#hideDiv').innerHTML = "";
                getTree(data.data.id, questionID);
            },

            error: function (data) {//仅调用url失败时有效
            }
        })
    }

    function getTree(strid, questionID) {
        var params = {};
        params.id = strid;
        params.questionID = questionID;
        $.ajax({
            url: "/knowledge/getTree", // 请求的URL
            dataType: 'json',
            type: "get",
            data: params,
            success: function (data) {
                var str = $('#mgradeno').find('option:selected').text() + $('#subjectno').find('option:selected').text();
                var tree = {text: str, id: strid, nodes: []};
                $("#knowledgeText").val('');//置input为空，重新生成已选中的text值
                $("#knowledgeids").val('');//置input为空，重新生成已选中的id 值
                buildTree(tree, data.data);
                $('#knowledgeTree').treeview({
                    color: "#428bca",
                    data: [tree],
                    showCheckbox: true,
                    multiSelect: true,
                    onNodeChecked: function (event, data) {
//                        console.log('checkData',data);

                        var str = $("#knowledgeText").val();

                        if (str.length > 0) {
                            $("#knowledgeText").val(str + data.text + ',');
                        } else {
                            $("#knowledgeText").val(data.text + ',');
                        }
                        ;

                        var cid = $("#knowledgeids").val();

                        if (cid.length > 0) {
                            $("#knowledgeids").val(cid + data.id + ',');
                        } else {
                            $("#knowledgeids").val(data.id + ',');
                        }
                    },
                    onNodeUnchecked: function (event, data) {
                        var str = $("#knowledgeText").val();
                        $("#knowledgeText").val(str.replace(data.text + ",", ""));
                        var cid = $("#knowledgeids").val();
                        $("#knowledgeids").val(cid.replace(data.id + ",", ""));
                    }
                });
            }
        });
    }

    function buildTree(parentNode, datas) {

        var str;
        var cid;
        for (var key in datas) {

            var data = datas[key];
//            console.log('data.id', data.id);
//            console.log('onchecked', data.onChecked);
            if (data.parentid == parentNode.id) {

                str = $("#knowledgeText").val();
                cid = $("#knowledgeids").val();
                var node = {};
                if (data.onChecked == true) {
                    $("#knowledgeText").val(str + data.knowledgeText + ',');
                    $("#knowledgeids").val(cid + data.id + ',');
                    var node = {
                        text: data.knowledgeText,
                        id: data.id,
                        nodes: [],
                        selectable: false,
                        state: {checked: true}
                    };
                } else {
                    node = {
                        text: data.knowledgeText,
                        id: data.id,
                        nodes: [],
                        selectable: false,
                        state: {checked: false}
                    };
                }


                parentNode.nodes.push(node);
                buildTree(node, datas);
            }
        }

        if (parentNode.nodes.length == 0) {
            delete parentNode.nodes;
        }
    }

    function hideDIV() {
        $("#hideDiv").hide();
    }


    function addSelected() {
        var params = {};
        console.log('questionid', $('#squestionid').val());
        params.questionID = $('#squestionid').val();
        params.knowledgeIDS = $('#knowledgeids').val();
        $.ajax({
            url: "/QuestionKnowledge/addSelected",
            // 数据发送方式
            type: "post",
            // 接受数据格式
            dataType: "json",
            // 要传递的数据
            data: params,
            // 回调函数，接受服务器端返回给客户端的值，即result值
            success: function (data) {

//                BootstrapDialog.show({
//                    type: BootstrapDialog.TYPE_SUCCESS,
//                    title: '维护知识点_操作提示：',
//                    message: data.msg
//                });
                hideDIV();
            }
            ,
            error: function (data) {
                BootstrapDialog.show({
                    type: BootstrapDialog.TYPE_DANGER,
                    title: '维护知识点_错误信息：',
                    message: data.msg
                });
            }
        })
    }

    $(function () {

        queryEgradeListBYschool();
        getsubjectList();
        getTermList();
        queryQuestions();
        var form = document.getElementById('mForm');
        form.onsubmit = function () {
            kfSubmit();
            return false;
        };

//        $("#myModal").on("hidden.bs.modal", function () {
//            $(this).removeData("bs.modal");
//            console.log('ccccc');
//        });
    })
</script>
<body>

<!-- 答案模态框（Modal） -->
<div class="modal fade" id="answerModal" tabindex="-1" role="dialog" aria-labelledby="answerModalModalLabel"
     aria-hidden="true">
    <div class="modal-dialog" style="width: 85%">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                    &times;
                </button>
                <h4 class="modal-title" id="answerModalModalLabel">
                    答案维护
                </h4>
            </div>
            <div class="modal-body">
                <form role="form">
                    <div class="form-group">
                        <input id="qid" name="qid" class="form-control" type="hidden"/>
                        <label for="answer">本题答案</label>
                        <textarea id="answer" name="answer" class="form-control" rows="6"></textarea>
                    </div>
                </form>
            </div>
            <div class="modal-footer">

                <button class="btn btn-primary" type="button" onclick="saveAnswer();">
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
<!-- 题目模态框（Modal） -->
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog" style="width: 99%">
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
                    <div style="width:96%;height: 250px">
                        <script id="editor" name="content" type="text/plain"></script>
                    </div>
                    <hr/>
                    <div style="margin-top: 50px">
                        <select id="mgradeno" name="mgradeno" class="selectpicker fit-width">
                        </select>
                        <select id="msubjectno" name="msubjectno" class="selectpicker fit-width"
                                onchange="mqueryQTypeBySubject( $('#msubjectno').val())">
                        </select>
                        <select id="mquestiontype" name="mquestiontype" class="selectpicker fit-width"
                                style="position: relative;z-index:999">
                        </select>
                        <label for="mdifficulty">难度系数：</label>
                        <input id="mdifficulty" type="text" style="width: auto" class="form-control"
                               placeholder="难度系数："/>

                        <input id="knowledgeids" name="knowledgeids" hidden/>
                        <input id="sid" name="sid" class="btn-default" hidden/>
                        <input id="squestionid" name="squestionid" class="btn-default" hidden/>
                    </div>


                </form>
                <div class="form-group">
                    <label for="knowledgeText">所属知识点：</label>
                    <input type="text" id="knowledgeText" name="knowledgeText" class="form-control"
                           value=""
                           onclick="$('#hideDiv').show()" placeholder="点击选择" readonly/>
                </div>
                <div id="hideDiv" style="display: none;">
                    <div id="knowledgeTree"></div>
                    <button class="btn btn-danger" type="button" onclick="addSelected()">
                        <span class="glyphicon glyphicon-eye-open"></span> 确定
                    </button>
                </div>
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
<form action="/equestions/exportQuestions" method="post" id="exportForm" name="exportForm">

    <div class="container" style="float:center;width: 99%;">
        <div class="well">
            <div class="input-prepend input-group">


                <select id="gradeno" name="gradeno" class="selectpicker fit-width">
                </select>
                <select id="subjectno" name="subjectno" class="selectpicker fit-width"
                        onchange="queryQTypeBySubject($('#subjectno').val())">
                </select>
                <select id="questiontype" name="questiontype" class="selectpicker fit-width">
                </select>


                <div style="float: left">
                    <input id="konwledge" type="text" placeholder="知识点：" hidden/>

                    <input id="difficulty" type="text" placeholder="难度系数：" hidden/>
                    <button class="btn btn-primary" type="button" onclick="queryQuestions()">
                        <span class="glyphicon glyphicon-eye-open"></span>查询
                    </button>

                    <button class="btn btn-primary" type="button" onclick="exportQuestions();"><span
                            class="glyphicon glyphicon-export"></span>导出
                    </button>

                </div>
            </div>
        </div>
    </div>
</form>

<div id="bs_t" style="float: none;display: block;margin-bottom:0;margin-left: auto;margin-right: auto;width: 99%">
    <table class="table table-striped" id="ds_table" align="center"
           striped="true"
           data-pagination="true" sidePagination="server" data-click-to-select="true">
        <thead>
        <tr>

            <th data-field="index" data-align="center" data-formatter="indexFormatter">序号</th>
            <th data-field="question">题目</th>
            <th data-field="id" data-align="center" data-formatter="operateFormatter" data-events="operateEvent">编辑
            </th>
            <th data-field="answer" data-align="center" data-formatter="answerFormatter" data-events="operateEvent">答案
            </th>
        </tr>
        </thead>
    </table>
</div>
</div>
</body>
</html>