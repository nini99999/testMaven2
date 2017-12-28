<%--
  Created by IntelliJ IDEA.
  User: jenny
  Date: 2017/12/20
  Time: 下午4:42
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
<title>--Tree测试--</title>

<link rel="stylesheet" href="<%=path%>/css/main.css">

<!-- Include Twitter Bootstrap and jQuery: -->
<link rel="stylesheet" href="<%=path%>/bootstrap/bootstrap.min.css" type="text/css"/>
<script type="text/javascript" src="<%=path%>/bootstrap/jquery.min.js"></script>
<script type="text/javascript" src="<%=path%>/bootstrap/bootstrap.min.js"></script>
<script type="text/javascript" src="<%=path%>/bootstrap/treeView/bootstrap-treeview-master.js"></script>
<link rel="stylesheet" href="<%=path%>/bootstrap/treeView/bootstrap-treeview.min.css" type="text/css"/>
<link rel="stylesheet" href="<%=path%>/bootstrap/dialog/bootstrap-dialog.min.css" type="text/css"/>
<script type="text/javascript" src="<%=path%>/bootstrap/dialog/bootstrap-dialog.min.js"></script>
<!-- Include the plugin's CSS and JS: -->
<script type="text/javascript" src="<%=path%>/bootstrap/bootstrap-multiselect.js"></script>
<link rel="stylesheet" href="<%=path%>/bootstrap/bootstrap-multiselect.css" type="text/css"/>

<script type="text/javascript" src="<%=path%>/bootstrap/bootstrap-select.js"></script>
<link rel="stylesheet" href="<%=path%>/bootstrap/bootstrap-select.css" type="text/css"/>

<link href="<%=path%>/bootstrap/font-awesome.min.css" rel="stylesheet">

<script type="text/javascript" src="<%=path%>/bootstrap/moment.js"></script>


<link rel="stylesheet" href="<%=path%>/bootstrap/bootstrap-table.css">
<script src="<%=path%>/bootstrap/bootstrap-table.js"></script>
<script src="<%=path%>/bootstrap/bootstrap-table-zh-CN.js"></script>


<link rel="stylesheet" type="text/css" media="all" href="<%=path%>/bootstrap/bootstrap-editable.css"/>
<script src="<%=path%>/bootstrap/bootstrap-editable.min.js"></script>
<script src="<%=path%>/bootstrap/bootstrap-table-editable.js"></script>
<head>
    <title>Title</title>
</head>
<script type="text/javascript">
    $(function () {
        getTree('1');
    })

    function buildTree(parentNode, datas) {
        for (var key in datas) {
            var data = datas[key];
            if (data.parentid == parentNode.id) {
                var node = {text: data.knowledgeText, id: data.id, nodes: [], selectable: true};
                parentNode.nodes.push(node);
                buildTree(node, datas);
            }
        }

        if (parentNode.nodes.length == 0) {
            delete parentNode.nodes;
        }
    }

    function getTree(strid) {
        var params = {};
        params.id = 'a696ee80-5b26-4a99-b013-ba22bec4d3bb';
        $.ajax({
            url: "/knowledge/getTree", // 请求的URL
            dataType: 'json',
            type: "get",
            data: params,
            success: function (data) {
                var tree = {text: '复读语文', id: 'a696ee80-5b26-4a99-b013-ba22bec4d3bb', nodes: []};
                buildTree(tree, data.data);
                $('#knowledgeTree').treeview({
                    color: "#428bca",
                    data: [tree],
                    showCheckbox: true,
                    multiSelect: true,
                    onNodeChecked: function (event, data) {
                        console.log('dddd', data);
                        var str = $("#knowledgeText").val();
                        console.log('1', data.knowledgeText);
                        if (str.length > 0) {
                            $("#knowledgeText").val(str + data.text + ',');
                        } else {
                            $("#knowledgeText").val(data.text + ',');
                        }
                        console.log('2', str);
                    },
                    onNodeUnchecked: function (event, data) {
                        var str = $("#knowledgeText").val();
                        $("#knowledgeText").val(str.replace(data.text + ",", ""));
//                        console.log('content', str.substring(0, str.indexOf(data.text)));
                        console.log('content', str.replace(data.text + ",", ""));
                    }
                });


            }
        });
    }

    function hideDIV() {
        $("#hideDiv").hide();
    }


</script>
<body>
<input type="text" id="knowledgeText" name="knowledgeText" class="form-control" value=""
       onclick="$('#hideDiv').show()" placeholder="选择知识点" readonly/>
<div id="hideDiv" style="display: none;">
    <div id="knowledgeTree"></div>
    <button class="btn btn-danger" type="button" onclick="hideDIV()">
        <span class="glyphicon glyphicon-eye-open"></span> 确定
    </button>
</div>

</body>
</html>
