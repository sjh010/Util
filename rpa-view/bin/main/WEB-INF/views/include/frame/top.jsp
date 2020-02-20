<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/tld/rpa-user-details.tld" prefix="ud" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<!--message popup-->
<!-- <div class="message-pop">
    <div class="arrow-up"></div>
    <ul class="message-pop-ul">
        <a href="#">
            <li class="message-pop-ul-li not">
                <div class="message-pop-ul-li-div">
                    <h5>제어팀</h5>
                    <p class="pop-date">2018-08-20</p>
                </div>
                <p class="message-content">가나다라마바사아자차카타파하가나다라마바사아자차카타파하</p>
            </li>
        </a>
        <a href="#">
            <li class="message-pop-ul-li">
                <div class="message-pop-ul-li-div">
                    <h5>제어팀</h5>
                    <p class="pop-date">2018-08-20</p>
                </div>
                <p class="message-content">가나다라마바사아자차카타파하가나다라마바사아자차카타파하</p>
            </li>
        </a>
        <a href="#">
            <li class="message-pop-ul-li">
                <div class="message-pop-ul-li-div">
                    <h5>제어팀</h5>
                    <p class="pop-date">2018-08-20</p>
                </div>
                <p class="message-content">가나다라마바사아자차카타파하가나다라마바사아자차카타파하</p>
            </li>
        </a>
        <a href="#">
            <li class="message-pop-ul-li">
                <div class="message-pop-ul-li-div">
                    <h5>제어팀</h5>
                    <p class="pop-date">2018-08-20</p>
                </div>
                <p class="message-content">가나다라마바사아자차카타파하가나다라마바사아자차카타파하</p>
            </li>
        </a>
    </ul>
</div> -->
<!--message popup end-->

<!--set popup-->
<div class="set-pop">
    <div class="arrow-up set-arrow"></div>
    <ul class="set-pop-ul">
        <!-- 2차개발 -->
        <!-- <li class="set-pop-ul-li">
            <a class="set-content" href="#">비밀번호 변경</a>
        </li> -->

        <li class="set-pop-ul-li">
            <a class="set-content" onclick="document.getElementById('logoutForm').submit();">로그아웃</a>
        </li>
    </ul>
</div>
<form:form name="logoutForm" id="logoutForm" method="post" action="${pageContext.request.contextPath}/logout"></form:form>
<!-- set popup end -->

<nav id="navbar" class="navbar navbar-default">
    <div class="container-fluid">
        <div class="collapse navbar-collapse">
            <ul class="nav navbar-nav navbar-left">
                <li><a class="titlelogo" href="<c:url value="/dashboard" />"></a></li>
            </ul>
            <p class="nav-title"></p>
            <ul class="nav navbar-nav navbar-right">
                <sec:authorize access="hasRole('ROLE_NTC_READ')">
                    <li><a href="#" class="notification"><span class="glyphicon glyphicon-bell"></span><div class="circle">12</div></a></li>
                </sec:authorize>
                <li><a>${ud:getUserId()}<span class="nav-info">&#10072;</span><span class="nav-info">${ud:getUserName()}님</span></a></li>
                <li><a href="#" class="setup"><span class="glyphicon glyphicon-option-vertical"></span></a></li>
            </ul>
        </div>
    </div>
</nav>
