<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>
<html>

<head>
    <meta charset="UTF-8">
    <title>ueditor demo</title>
</head>
<body>
<!-- 加载编辑器的容器 -->
<script id="editor" name="content" type="text/plain">

</script>
<!-- 配置文件 -->
<script type="text/javascript" src="ueditor.config.js"></script>
<!-- 编辑器源码文件 -->
<script type="text/javascript" src="ueditor.all.js"></script>
<script type="text/javascript" charset="utf-8" src="<%=path%>/UEditor/kityformula-plugin/addKityFormulaDialog.js"></script>
<script type="text/javascript" charset="utf-8" src="<%=path%>/UEditor/kityformula-plugin/getKfContent.js"></script>
<script type="text/javascript" charset="utf-8" src="<%=path%>/UEditor/kityformula-plugin/defaultFilterFix.js"></script>
<!-- 实例化编辑器 -->
<script type="text/javascript">
    // var ue = UE.getEditor('container');
    var ue = UE.getEditor('editor', {
        toolbars: [[
            'fullscreen', 'source', '|',
            'bold', 'italic', 'underline', '|', 'fontsize', '|', 'kityformula', 'preview'
        ]]
    });
</script>
</body>

</html>
