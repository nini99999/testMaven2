<%@ page language="java" pageEncoding="UTF-8"%>

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

		<title>模块1_1</title>

		<link rel="stylesheet" type="text/css" href="extjs/resources/css/ext-all.css" />
		<link rel="stylesheet" type="text/css" href="extjs/resources/css/ItemSelector.css">
		<script type="text/javascript" src="extjs/examples/shared/include-ext.js"></script>
		<!-- 	<script type="text/javascript" src="extjs/examples/shared/options-toolbar.js"></script> -->
		<script type="text/javascript" src="extjs/examples/shared/states.js"></script>
		<script type="text/javascript" src="extjs/locale/ext-lang-zh_CN.js"></script>
		
	</head>

	<body>
     模块1_1
	</body>
</html>
