<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<link rel="stylesheet" type="text/css" href="<c:url value="/css/bootstrap/bootstrap.min.css" />" />
<link rel="stylesheet" type="text/css" href="<c:url value="/css/login.css" />" />
<title>INZI RPA SUITE MANAGER</title>
<script type="text/javascript" src="<c:url value="/js/jquery/jquery.min.js" />"></script>
<script type="text/javascript" src="<c:url value="/js/rsa/rsa.js" />"></script>
<script type="text/javascript" src="<c:url value="/js/rsa/jsbn.js" />"></script>
<script type="text/javascript" src="<c:url value="/js/rsa/prng4.js" />"></script>
<script type="text/javascript" src="<c:url value="/js/rsa/rng.js" />"></script>
<script type="text/javascript">
    $(function() {
        var error = <%=request.getAttribute("error")%> ;
        var errorCode = <%=request.getAttribute("errorCode")%> ;
        if (error) {
            var errorMessage = "";
            if (errorCode == 721) {
                errorMessage = "<spring:message code="err.m01.0000"/>";
            } else if (errorCode == 722) {
                errorMessage = "<spring:message code="err.m01.0001"/>";
            } else if (errorCode == 730) {
                errorMessage = "<spring:message code="err.m01.0004"/>";
            } else {
                errorMessage = "<spring:message code="err.m01.0002"/>";
            }
            $("#errorMessage").text(errorMessage);
        }
    });

    function login() {
        if (checkValidation()) {
            encrypt();
            $("#loginForm").submit();
        }
    }

    function encrypt() {
        var rsa = new RSAKey();
        rsa.setPublic("${RSAModulus}", "${RSAExponent}");
        $("#password").val(rsa.encrypt_b64($("#plainPassword").val()));
    }

    function checkValidation() {
        var _userId = $("#userId");
        var _userPw = $("#plainPassword");
        if (_userId.val() == "") {
            $("#errorMessage").text("<spring:message code="err.m01.0003" arguments="아이디"/>");
            return false;
        }  else if (_userPw.val() == "") {
            $("#errorMessage").text("<spring:message code="err.m01.0003" arguments="비밀번호"/>");
            return false;
        }
        return true;
    }
</script>
</head>
<body>
    <div class="login_wrapper">
        <form:form name="loginForm" class="form-signin" id="loginForm" method="post" action="${pageContext.request.contextPath}/login_process">
            <h2 class="form-signin-heading" style="font-size: 24px; text-align: center">INZI-RPA Suite Manager</h2>
            <p class="login-inputwrap-errortext" id="errorMessage"></p>
            <div class="login-inputwrap">
                <fieldset>
                    <span>아이디</span>
                    <input id="userId" class="form-control" name="userId" type="text" onkeydown="if (event.keyCode == 13) login();" placeholder="아이디" />
                    <span>비밀번호</span>
                    <input id="plainPassword" class="form-control" style="margin-top:20px" type="password" onkeydown="if (event.keyCode == 13) login();" placeholder="비밀번호" />
                    <input id="password" name="password" type="hidden" />
                </fieldset>
            </div>
            <button class="btn btn-lg btn-primary btn-block" style="margin-top:40px" type="button" onclick="javascript:login();" >로그인</button>
        </form:form>
    </div>
</body>
</html>
