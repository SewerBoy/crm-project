<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <base href="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/">
    <meta charset="UTF-8">
    <title>演示文件下载</title>
    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
    <script type="text/javascript" >
        $(function() {
            $("#fileDownloadBtn").click(function() {
                window.location.href="workbench/activity/fileDownload.do";
            });
        });
    </script>
</head>
<body>
    <input type="button" value="下载" id="fileDownloadBtn">
</body>
</html>
