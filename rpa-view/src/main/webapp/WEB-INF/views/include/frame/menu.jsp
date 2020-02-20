<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<!-- side bar holder -->
<div class="sidebar" id="sidebar">
    <ul class="list-unstyled components">
        <sec:authorize access="hasRole('ROLE_DSBD_READ')">
            <li data-menu="dashboard"><a href="<c:url value="/dashboard" />">대시보드</a></li>
        </sec:authorize>
        <sec:authorize access="hasRole('ROLE_RBT_READ')">
            <li data-menu="robot"><a href="<c:url value="/robot" />">로봇 관리</a></li>
        </sec:authorize>
        <sec:authorize access="hasRole('ROLE_PRCS_READ')">
            <li data-menu="process"><a href="<c:url value="/process" />">프로세스 관리</a></li>
        </sec:authorize>
        <sec:authorize access="hasRole('ROLE_WRK_ASGN_READ')">
            <li data-menu="work"><a href="<c:url value="/work" />">업무할당 관리</a></li>
        </sec:authorize>
        <sec:authorize access="hasRole('ROLE_QUE_READ')">
            <li><a href="#">대기열 관리</a></li>
        </sec:authorize>
        <sec:authorize access="hasRole('ROLE_LOG_READ')">
            <li><a href="#">로그관리</a></li>
        </sec:authorize>
        <sec:authorize access="hasRole('ROLE_STAT_READ')">
            <li><a href="#">통계</a></li>
        </sec:authorize>
        <sec:authorize
            access="hasAnyRole('ROLE_CFG_READ', 'ROLE_USR_READ', 'ROLE_ATH_READ', 'ROLE_COM_CD_READ')">
            <li><a href="#homeSubmenu" class="homeSubmenu" data-toggle="collapse" aria-expanded="false">설정</a>
                <ul class="collapse list-unstyled" id="homeSubmenu">
                    <sec:authorize access="hasRole('ROLE_CFG_READ')">
                        <li><a href="#">공용 변수 관리</a></li>
                    </sec:authorize>
                    <sec:authorize access="hasRole('ROLE_USR_READ')">
                        <li data-menu="user"><a href="<c:url value="/user" />">사용자 관리</a></li>
                    </sec:authorize>
                    <sec:authorize access="hasRole('ROLE_ATH_READ')">
                        <li data-menu="authority"><a href="<c:url value="/authority" />">권한 관리</a></li>
                    </sec:authorize>
                    <sec:authorize access="hasRole('ROLE_COM_CD_READ')">
                        <li><a href="#">공통 코드 관리</a></li>
                    </sec:authorize>
                </ul>
            </li>
        </sec:authorize>
    </ul>
    <ul class="list-unstyled CTAs">
<!--         <li><a href="#" class="robotdown">INZI-RPA Robot 다운로드</a></li> -->
<!--         <li><a href="#" class="studiodown">INZI-RPA Studio 다운로드</a></li> -->
    </ul>
</div>