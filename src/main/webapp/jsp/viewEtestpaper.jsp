<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>
<html>
<head>
    <title>--试卷管理--</title>
    <!-- Include Twitter Bootstrap and jQuery: -->
    <link rel="stylesheet" href="<%=path%>/bootstrap/bootstrap.min.css" type="text/css"/>
    <script type="text/javascript" src="<%=path%>/bootstrap/jquery.min.js"></script>
    <script type="text/javascript" src="<%=path%>/bootstrap/bootstrap.min.js"></script>
    <script type="text/javascript" src="<%=path%>/bootstrap/bootstrap-select.js"></script>
    <link rel="stylesheet" href="<%=path%>/bootstrap/bootstrap-select.css" type="text/css"/>
    <link href="<%=path%>/bootstrap/font-awesome.min.css" rel="stylesheet">

    <script type="text/javascript" src="<%=path%>/bootstrap/moment.js"></script>

    <link rel="stylesheet" href="<%=path%>/bootstrap/bootstrap-table.css">
    <link rel="stylesheet" href="<%=path%>/css/main.css">
    <script src="<%=path%>/bootstrap/bootstrap-table.js"></script>
    <script src="<%=path%>/bootstrap/bootstrap-table-zh-CN.js"></script>

    <link href="<%=path%>/bootstrap/bootstrap-datetimepicker.css" rel="stylesheet"/>
    <script src="<%=path%>/bootstrap/moment-with-locales.js"></script>
    <script src="<%=path%>/bootstrap/bootstrap-datetimepicker.js"></script>
    <script src="<%=path%>/bootstrap/bootstrap-datetimepicker.zh-CN.js"></script>

    <link rel="stylesheet" type="text/css" media="all" href="<%=path%>/bootstrap/bootstrap-editable.css"/>
    <script src="<%=path%>/bootstrap/bootstrap-editable.min.js"></script>
    <script src="<%=path%>/bootstrap/bootstrap-table-editable.js"></script>

</head>
<script type="text/javascript">


    function indexFormatter(value, row, index) {
        return index + 1;
    }

    function operateFormatter(value, row, index) {

        return [

            '<a class="edit"  href="javascript:void(0)" title="编辑试卷">',
            '<i class="glyphicon glyphicon-edit"></i>',
            '</a> '
        ].join('');
    }

    function childFormatter(value, row, index) {
        return [
            '<a class="editchild"  href="javascript:void(0)" title="编辑题型信息">',
            '<i class="glyphicon glyphicon-edit"></i>',
            '</a> ',
            '<a class="delchild"  href="javascript:void(0)" title="删除该题型">',
            '<i class="glyphicon glyphicon-Remove-circle"></i>',
            '</a>'
        ].join('');
    }

    function maximumFomatter(value, row, index) {
        return [
            '<a class="editMaximum"  href="javascript:void(0)" title="编辑每题分数">',
            '<i class="glyphicon glyphicon-tasks"></i>',
            '</a>'
        ].join('');
    }

    window.operateEvent = {
        'click .edit': function (e, value, row, index) {
            //修改操作

            $('#subjectno').selectpicker('val', (row.subjectno));
            $('#gradeno').selectpicker('val', (row.gradeno));
            $('#schoolno').selectpicker('val', (row.schoolno));
            $('#term').selectpicker('val', (row.term));
            $('#sid').val(row.id);
            $('#tpno').val(row.tpno);
            $('#c_tpname').val(row.tpname);
            $('#c_examtype').selectpicker('val', row.examtype);
            $('#testdate').val(row.testdate);
//            $('#testdate').datetimepicker('val', row.testdate);
            console.log('testdate', row.testdate);
            $("#myModal").modal('show');

        },
        'click .editchild': function (e, value, row, index) {
            //维护题型明细,赋值
//            console.log(row);
            $('#l_tpno').val(row.tpno);
            $('#questiontype').val(row.questiontype);
            $('#questiontypename').val(row.questiontypename);
            $('#l_id').val(row.id);
            $('#questionnum').val(row.questionnum);
            $('#description').val(row.description);
            $('#mark').val(row.mark);
            $("#qtModal").modal('show');


        },
        'click .editMaximum': function (e, value, row, index) {
            //维护试题分值
            $('#h_tpno').val(row.tpno);
            $('#h_qtype').val(row.questiontype);
            $('#c_questionNum').val(row.questionnum);
            $("#maximum").modal('show');
            init_maximumTable(row.tpno, row.questiontype);

        }
    }

    function init_maximumTable(p_paperId, p_questionTypeId) {
        $("#maximumTable").bootstrapTable('destroy');
        $("#maximumTable").bootstrapTable({
            method: "get",
            catch: false,
            idField: "id",

            queryParams: function (params) {

                return {
                    paperId: p_paperId,
                    questionTypeId: p_questionTypeId,
                }
            },

            url: "/maximum/getMaximum",
            striped: true,
            clickToSelect: true,
            pagination: false, //是否显示分页（*）
            //  sidePagination: "server", //分页方式：client客户端分页，server服务端分页（*）
            // // showColumns:true,
            //  pageNumber: 1, //初始化加载第一页，默认第一页
            //  pageSize: 10, //每页的记录行数（*）
            //  pageList: [10, 20, 50, 100],
            columns: [{
                checkbox: true
            }, {
                field: "paperId",
                title: "试卷编码",
                align: "center"
            }, {
                field: "questionTypeId",
                title: "试题类型",
                align: "center"
            }, {
                field: "questionIndex",
                title: "序号",
                align: "center"
            }, {
                field: "maximum",
                title: "每题分数",
                align: "center",
                editable: {
                    type: 'text',
                    title: '每题分数',
                    validate: function (v) {
                        if (isNaN(v)) return '必须为数字';
                    }
                }
            }, {
                field: "creator",
                title: "创建日期",
                align: "center"
            }, {
                field: "createdate",
                title: "创建日期",
                align: "center"
            }],

            onEditableSave: function (field, row, oldValue, $el) {
//                console.log(row);
                var params = {};

                params.id = row.id;
                params.maximum = row.maximum;

                $.ajax({
                    type: "post",
                    url: "/maximum/updateMaximum",
                    data: params,
                    dataType: 'JSON',
                    success: function (data) {

                        if (data.success) {
                            $("#child_table").bootstrapTable('refresh');
                            alert('提交数据成功');
                        }

                    },
                    error: function (data) {
                        alert('编辑失败' + data.msg);
                    },
                    complete: function () {

                    }

                });
            }
        });
    }

    function addMaximums(p_questionNum, p_maximum) {
        var params = {};
        params.paperId = $('#h_tpno').val();

        params.questionTypeId = $('#h_qtype').val();

        params.questionNum = p_questionNum;
        params.maximum = p_maximum;
        console.log(params);

        $.ajax({

            url: "/maximum/addMaximums",

// 数据发送方式
            type: "post",
// 接受数据格式
            dataType: "json",
// 要传递的数据
            data: params,
// 回调函数，接受服务器端返回给客户端的值，即result值
            success: function (data) {//调用url成功时有效
                if (data.success == true) {
                    init_maximumTable($('#h_tpno').val(), $('#h_qtype').val());
                    $("#child_table").bootstrapTable('refresh');
                    // console.log($('#h_tpno').val());
                    // console.log($('#h_qtype').val());
                    alert("保存成功！");
                } else {
                    alert("保存失败:" + data.msg);
                }
            },

            error: function (data) {//仅调用url失败时有效

                alert("系统错误：调用方法失败！" + data.msg);

            }
        })
    }

    function getExamType() {
        $.ajax({
            url: "/etestpaper/getExamType",
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
                 * 初始化考试类型下拉列表**/
                $('#examtype').empty();
                $.each(data.examtype, function (i) {
                    $('#examtype.selectpicker').append("<option value=" + [i] + ">" + data.examtype[i] + "</option>");
                });
                $('#examtype').selectpicker('refresh');
                $('#c_examtype').empty();
                $.each(data.examtype, function (i) {
                    $('#c_examtype.selectpicker').append("<option value=" + [i] + ">" + data.examtype[i] + "</option>");
                });
                $('#c_examtype').selectpicker('refresh');
            },

            error: function (data) {
                alert("初始化学期失败" + data);
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
                });

                $('#subjectno').selectpicker('refresh');
            },

            error: function (data) {

                alert("查询学科失败" + data);

            }
        })
    }

    function openAddModal() {
        var str_grade = $('#gradeno').val();
        if ("noselected" == str_grade) {
            alert("请选择年级！");
        } else {

            $("#myModal").modal('show');
            $('#sid').val(null);
            $('#tpno').val(null);
            $('#c_tpname').val(null);

        }
    }

    function init_table() {

        $("#ds_table").bootstrapTable('destroy');
        $("#ds_table").bootstrapTable({
            method: "get",
            catch: false,
            // toolbar: "#bs_t",
            idField: "id",

            queryParams: function (params) {

                return {
                    schoolno: $('#schoolno').val(),
                    gradeno: $('#gradeno').val(),
                    subjectno: $('#subjectno').val(),
                    term: $('#term').val(),
                    tpname: $('#tpname').val(),
                    examtype: $('#examtype').val(),
                    pageno: (params.offset / params.limit) + 1,
                    pagesize: params.limit

                }
            },

            url: "/etestpaper/viewTestPaper",
            striped: true,
            clickToSelect: true,
            pagination: true, //是否显示分页（*）
            sidePagination: "server", //分页方式：client客户端分页，server服务端分页（*）
            pageNumber: 1, //初始化加载第一页，默认第一页
            pageSize: 10, //每页的记录行数（*）
            pageList: [10, 20, 50, 100],
            detailView: true,
            detailFormatter: getChildTable,
//            showColumns:true,
            columns: [{
                checkbox: true
            }, {
                field: "index",
                title: "序号",
                align: "center",
                formatter: function (value, row, index) {

                    return index + 1;
                }
            }, {
                field: "tpno",
                title: "试卷编码",
                align: "center"
            }, {
                field: "tpname",
                title: "试卷名称",
                align: "center"
            }, {
                field: "testdate",
                title: "考试日期",
                align: "center"
            }, {
                field: "examtypename",
                title: "考试类型",
                align: "center"
            }, {
                field: "creator",
                title: "创建日期",
                align: "center"
            }, {
                field: "createdate",
                title: "创建日期",
                align: "center"
            }, {
                field: "edit",
                title: "编辑",
                align: "center",
                formatter: operateFormatter,
                events: operateEvent
            }]
        });
    }

    function getChildTable(index, row, $detail) {

        var parentid = row.tpno;
//        console.log(row);
        var cur_table = $detail.html('<table id="child_table"></table>').find('table');
        $(cur_table).bootstrapTable({
            url: '/etestpaper/getPaperQType',
            method: 'get',
            queryParams: {strParentID: parentid},
            ajaxOptions: {strParentID: parentid},
            showFooter: true,
            columns: [
                {
                    field: 'questiontype',
                    title: '题型编号',
                    align: 'center'
                }, {
                    field: 'questiontypename',
                    title: '题型名称',
                    align: 'center',
                    footerFormatter: function () {
                        return '合计：';
                    }
                }, {
                    field: 'questionnum',
                    title: '小题数量',
                    align: 'center',
                    footerFormatter: function (value) {
                        var count = 0;
                        for (var i in value) {
                            count += value[i].questionnum;
                        }
                        return count;
                    }
                }, {
                    field: 'mark',
                    title: '题型总分',
                    align: 'center',
                    footerFormatter: function (value) {

                        var count = 0;
                        for (var i in value) {
//                            console.log(value[i].mark);
                            count += value[i].mark;
                        }
                        return count;
//                        console.log(count);
                    }
                }, {
                    field: 'description',
                    title: '题型说明',
                    align: 'center'
                }, {
                    title: '操作',
                    field: 'c_id',
                    align: 'center',
                    formatter: childFormatter(),
                    events: operateEvent
                }, {
                    title: '每题分数',
                    field: 'c_maximum',
                    align: 'center',
                    formatter: maximumFomatter(),
                    events: operateEvent
                },
            ],
            //无线循环取子表，直到子表里面没有记录
            onExpandRow: function (index, row, $Subdetail) {
                getChildTalbe(index, row, $Subdetail);
            }
        });
    }

    //     function queryEtestpaper() {
    //         var params = {};
    //         params.schoolno = $('#schoolno').val();
    //         params.gradeno = $('#gradeno').val();
    //         params.subjectno = $('#subjectno').val();
    //         params.term = $('#term').val();
    //         params.tpname = $('#tpname').val();
    //         params.examtype = $('#examtype').val();
    //         $.ajax({
    //             url: "/etestpaper/viewTestPaper",
    // // 数据发送方式
    //             type: "post",
    // // 接受数据格式
    //             dataType: "json",
    // // 要传递的数据
    //             data: params,
    // // 回调函数，接受服务器端返回给客户端的值，即result值
    //             success: function (data) {
    //
    // //alert(data.total);
    //                 //$("#ds_table").bootstrapTable('refresh', data);//刷新ds_table的数据
    //                 $("#ds_table").bootstrapTable('destroy');
    //                 $("#ds_table").bootstrapTable({data: data.data});//刷新ds_table的数据
    // //                $("#ds_table").bootstrapTable('refresh', data.data);
    //             },
    //
    //             error: function (data) {
    // //                console.log(data);
    //                 alert("查询失败" + data);
    //
    //             }
    //         })
    //     }

    function addEtestpaper() {
        var params = {};

        params.schoolno = $('#schoolno').val();
        params.subjectno = $('#subjectno').val();
        params.gradeno = $('#gradeno').val();
        params.term = $('#term').val();
        params.tpname = c_tpname.value;
        params.tpno = tpno.value;
        params.examtype = $('#c_examtype').val();
        params.id = sid.value;
        params.testdate = $('#testdate').val();
//        console.log('add', $('#testdate').val());

        $.ajax({

            url: "/etestpaper/addTestPaper",

// 数据发送方式
            type: "post",
// 接受数据格式
            dataType: "json",
// 要传递的数据
            data: params,
// 回调函数，接受服务器端返回给客户端的值，即result值
            success: function (data) {//调用url成功时有效
                if (data.success == true) {
                    // queryEtestpaper();
                    init_table();
//重置输入框为空
//                    $('#schoolno').val(null);
//                    $('#subjectno').val(null);
//                    $('#gradeno').val(null);
//                    $('#term').val(null);
                    $('#sid').val(null);
                    tpno.value = null;
                    c_tpname.value = null;

                    alert("保存成功!");
                } else {
                    alert(data.msg);
                }
            },
            error: function (data) {//仅调用url失败时有效
//                console.log(data);
                alert("新增失败" + data);
            }
        })
    }

    function addPaperQT() {
        var params = {};
        params.id = $('#l_id').val();
        params.mark = parseInt($('#mark').val());
        params.questiontype = $('#questiontype').val();
        params.questionnum = parseInt($('#questionnum').val());
        params.tpno = $('#l_tpno').val();
        params.description = $('#description').val();
//        console.log(params);
        $.ajax({

            url: "/etestpaper/modifPaperQT",

// 数据发送方式
            type: "post",
// 接受数据格式
            dataType: "json",
// 要传递的数据
            data: params,
// 回调函数，接受服务器端返回给客户端的值，即result值
            success: function (data) {//调用url成功时有效

//                console.log(data);
                if (data.success == true) {
                    $('#qtModal').modal('hide');
//                    queryEtestpaper();
//                    console.log( $("#cur_table"));
//                    $("#child_table").bootstrapTable('destroy');
                    $("#child_table").bootstrapTable('refresh', data.data);

                    alert("保存成功！");


                } else {
                    alert(data.msg);
                }

            },

            error: function (data) {//仅调用url失败时有效

                alert("保存失败" + msg);

            }
        })
    }

    function delEtestpaper() {
        var selects = $("#ds_table").bootstrapTable('getSelections');
//        console.log(JSON.stringify(selects));
        $.ajax({

            url: "/etestpaper/delTestPaper",
// 数据发送方式
            type: "post",
// 接受数据格式
            dataType: "json",
            contentType: 'application/json',
// 要传递的数据
            data: JSON.stringify(selects),

// 回调函数，接受服务器端返回给客户端的值，即result值
            success: function (data) {//调用url成功时有效
                if (data.success == true) {
                    // queryEtestpaper();
                    init_table();
                } else {
                    alert(data.msg);
                }
            },

            error: function (data) {//仅调用url失败时有效

                alert("删除失败" + JSON.stringify(data));

            }
        })
    }

    function delMaximums() {

        var selects = $("#maximumTable").bootstrapTable('getSelections');
        if (selects.length > 0) {
//        console.log(JSON.stringify(selects));
            $.ajax({

                url: "/maximum/deleteMaximums",
// 数据发送方式
                type: "post",
// 接受数据格式
                dataType: "json",
                contentType: 'application/json',
// 要传递的数据
                data: JSON.stringify(selects),

// 回调函数，接受服务器端返回给客户端的值，即result值
                success: function (data) {//调用url成功时有效
                    if (data.success == true) {

                        init_maximumTable(data.paperId, data.questionTypeId);
                    } else {
                        alert(data.msg);
                    }
                },

                error: function (data) {//仅调用url失败时有效

                    alert("删除失败" + JSON.stringify(data));

                }
            })
        }else{
            alert("请选择数据！");
        }
    }

</script>

<body>
<div class="container" style="width: 99%">
    <hr/>
    <div class="well">

        <div class="input-prepend input-group">
            <div>
                <form class="form-inline">


                    <select id="gradeno" name="gradeno" class="selectpicker fit-width">
                    </select>

                    <select id="term" name="subjectno" class="selectpicker fit-width">
                    </select>

                    <select id="subjectno" name="subjectno" class="selectpicker fit-width">
                    </select>
                    <select id="examtype" name="examtype" class="selectpicker fit-width">
                    </select>
                    <input id="tpname" name="tpname" class="form-control" style="width: 150px;" placeholder="试卷名称："/>
                    <button class="btn btn-primary" type="button" onclick="init_table();">
                        <span class="glyphicon glyphicon-eye-open"></span> 查询
                    </button>
                    <!-- 按钮触发模态框 -->
                    <button class="btn btn-primary" type="button" onclick="openAddModal();">
                        <span class="glyphicon glyphicon-plus-sign"></span> 添加
                    </button>
                    <button class="btn btn-primary" type="button" onclick="delEtestpaper();">
                        <span class="glyphicon glyphicon-minus-sign"></span> 删除
                    </button>
                </form>
            </div>
        </div>

    </div>
    <div id="bs_t" style="float: none;display: block;margin-left: auto;margin-right: auto;">
        <table class="table table-striped" id="ds_table">
        </table>
    </div>
</div>

<!-- 模态框-添加试卷（Modal） -->
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog" style="width: 85%">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                    &times;
                </button>
                <h4 class="modal-title" id="myModalLabel">
                    添加试卷
                </h4>
            </div>
            <div class="modal-body">
                <input id="sid" name="sid" class="btn-default" hidden>
                <input id="tpno" name="tpno" class="btn-default" hidden>


                <div class="form-group">
                    <label for="testdate">考试日期：</label>
                    <!--指定 date标记-->
                    <div class="input-group date col-xs-6">
                        <input id='testdate' type='text' class="form-control" readonly/>
                        <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                    </div>
                    <hr/>
                    <label for="c_tpname">试卷名称：</label><input id="c_tpname" name="c_tpname" class="form-control"
                                                              style="width: 50%">
                    <hr/>
                    <label for="c_examtype">考试类型：</label>
                    <select id="c_examtype" name="c_examtype" class="selectpicker"></select>
                </div>


            </div>
            <div class="modal-footer">
                <button class="btn btn-primary" type="button" onclick="addEtestpaper();">
                    <span class="glyphicon glyphicon-floppy-save"></span> 保存
                </button>
                <button class="btn btn-primary" type="button" data-dismiss="modal">
                    <span class="glyphicon glyphicon-log-out"></span> 关闭
                </button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal -->
</div>


<!-- 模态框-维护每题分数（Modal） -->
<div class="modal fade" id="maximum" tabindex="-1" role="dialog" aria-labelledby="maximumLabel" aria-hidden="true">
    <div class="modal-dialog" style="width: 85%">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                    &times;
                </button>
                <h4 class="modal-title" id="maximumLabel">
                    试题分值
                </h4>
            </div>
            <div class="modal-body">
                <div class="well">
                    <div>
                        <form class="form-inline">
                            <input id="h_tpno" hidden/>
                            <input id="h_qtype" hidden/>
                            <label for="c_questionNum">该题型试题数量：</label> <input id="c_questionNum"
                                                                               class="form-control" readonly>
                            <label for="c_perMaxi">每题分值：</label> <input id="c_perMaxi" class="form-control">

                            <button class="btn btn-primary" type="button"
                                    onclick="addMaximums($('#c_questionNum').val(), $('#c_perMaxi').val());">
                                <span class="glyphicon glyphicon-floppy-save"></span> 统一设置
                            </button>

                            <button class="btn btn-primary" type="button" onclick="delMaximums();">
                                <span class="glyphicon glyphicon-minus-sign"></span> 删除
                            </button>
                        </form>
                    </div>
                </div>
                <table id="maximumTable"></table>


                <div class="modal-footer">
                    <button class="btn btn-primary" type="button" onclick="addEtestpaper();">
                        <span class="glyphicon glyphicon-floppy-save"></span> 保存
                    </button>
                    <button class="btn btn-primary" type="button" data-dismiss="modal">
                        <span class="glyphicon glyphicon-log-out"></span> 关闭
                    </button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal -->
    </div>
</div>
<!-- 模态框-维护题型明细（Modal） -->
<div class="modal fade" id="qtModal" tabindex="-1" role="dialog" aria-labelledby="qtModalLabel"
     aria-hidden="true">
    <div class="modal-dialog" style="width: 90%">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                    &times;
                </button>
                <h4 class="modal-title" id="qtModalLabel">
                    维护题型明细
                </h4>
            </div>

            <div class="modal-body" style="height: 88%">
                <form class="form-inline">
                    <input id="l_id" name="l_id" hidden/>
                    <label for="l_tpno">已选试卷：</label>
                    <input id="l_tpno" name="l_tpno" class="form-control" disabled/>
                    <hr/>
                    <label for="questiontype">题型编号：</label>
                    <input id="questiontype" name="questiontype" class="form-control" disabled/>
                    <label for="questiontypename">题型名称：</label>
                    <input id="questiontypename" name="questiontypename" class="form-control" disabled/>
                    <hr/>
                    <label for="mark">题型总分：</label>
                    <input id="mark" placeholder="由每题分数自动汇总" name="l_mark" class="form-control" readonly/>
                    <label for="questionnum">小题数量：</label>
                    <input id="questionnum" name="questionnum" class="form-control"/>
                    <hr/>
                    <label for="description">题型说明：</label>
                    <textarea class="form-control" rows="3" id="description" name="description"
                              style="width:70%;"></textarea>
                </form>

            </div>

            <div class="modal-footer">
                <button class="btn btn-primary" type="button" onclick="addPaperQT();">
                    <span class="glyphicon glyphicon-floppy-save"></span> 保存
                </button>
                <button class="btn btn-primary" type="button" data-dismiss="modal">
                    <span class="glyphicon glyphicon-log-out"></span> 关闭
                </button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal -->
</div>

</body>
</html>
<script type="text/javascript">
    $(document).ready(function () {

        queryEgradeListBYschool();
        getsubjectList();
        getTermList();
        getExamType();
        $('#testdate').datetimepicker({
            format: 'yyyy-mm-dd',
            weekStart: 1,
            autoclose: true,
            startView: 2,
            minView: 2,
            forceParse: false,
            language: 'zh-CN'
        });
//        queryEgradeListBYschool();
//        queryEteacherBYschool();

    });
</script>