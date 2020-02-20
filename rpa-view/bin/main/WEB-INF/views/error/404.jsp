<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="_context" value="${(pageContext.request.contextPath eq '/')? '': pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="ko">
<head>
<title>INZI RPA SUITE MANAGER</title>
</head>
<style>
body {
    text-align: center;
}

.frame {  
    height: 800px;
    width: 800px;
    position: relative;
}

img {  
    max-height: 100%;  
    max-width: 100%; 
    width: auto;
    height: auto;
    position: absolute;  
    top: 0;  
    bottom: 0;  
    left: 0;  
    right: 0;  
    margin: auto;
}
</style>
<body>
    <div class="wrapper">
        <img src="${_context }/images/img_404.svg">
    </div>
</body>
</html>