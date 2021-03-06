<%--
  Created by IntelliJ IDEA.
  User: olga
  Date: 06.05.15
  Time: 6:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${localeValue}"/>
<fmt:setBundle basename="com.epam.task6.resource.Resource" var="msg"/>
<html>
<head>
    <title>
        <fmt:message key="jsp.404.page.title" bundle="${msg}"/>
    </title>
    <!-- Styles -->
    <link href="css/bootstrap.min.css" rel="stylesheet">
    <link href="css/error.css" rel="stylesheet">
</head>
<body>
<div class="container">
    <div class="row">
        <div class="col-md-12">
            <div class="error-template center">
                <h1>
                    <fmt:message key="jsp.404.h1" bundle="${msg}"/>
                </h1>
                <h2>
                    <fmt:message key="jsp.404.h2" bundle="${msg}"/>
                </h2>
                <div class="error-details">
                    <fmt:message key="jsp.404.details" bundle="${msg}"/>
                </div><br />
                <div class="error-actions">
                    <a href="controller?executionCommand=REDIRECT" class="btn btn-primary btn-lg">
                        <span class="glyphicon glyphicon-home"></span>
                        <fmt:message key="jsp.404.button.home.value" bundle="${msg}"/>
                    </a>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
