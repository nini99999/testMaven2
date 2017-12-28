<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <base href="<%=basePath%>">

    <title>TreePanel示例</title>

    <link rel="stylesheet" type="text/css" href="<%=path%>/extjs/resources/css/ext-all-gray.css"/>
    <link rel="stylesheet" type="text/css" href="<%=path%>/extjs/resources/css/ItemSelector.css">
    <script type="text/javascript" src="<%=path%>/extjs/examples/shared/include-ext.js"></script>
    <!-- 	<script type="text/javascript" src="extjs/examples/shared/options-toolbar.js"></script> -->
    <script type="text/javascript" src="<%=path%>/extjs/examples/shared/states.js"></script>
    <script type="text/javascript" src="<%=path%>/extjs/locale/ext-lang-zh_CN.js"></script>
    <script type="text/javascript" src="<%=path%>/js/demo.js"></script>
</head>

<body>
<div style="display: none;position: absolute;height: 0px;width: 0px">
    <input style="display:none;" name="userRealName" id="userRealName" value="<sec:authentication property="name"/>">
</div>
</body>
</html>
