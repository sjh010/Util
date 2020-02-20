<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.HashMap"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@ taglib uri="/WEB-INF/tld/rpa-user-details.tld" prefix="ud" %>

<c:set var="_context" value="${(pageContext.request.contextPath eq '/')? '': pageContext.request.contextPath}" />

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta name="_csrf" content="${_csrf.token}"/>
<meta name="_csrf_header" content="${_csrf.headerName}"/>
<meta name="viewport" content="width=device-width, initial-scale=1">

<link rel="stylesheet" type="text/css" href="<c:url value="/css/bootstrap/bootstrap.min.css" />" />
<link rel="stylesheet" type="text/css" href="<c:url value="/css/add.css?v=20180327" />" />
<link rel="stylesheet" type="text/css" href="<c:url value="/css/style.css?v=201803211555" />" />
<link rel="stylesheet" type="text/css" href="<c:url value="/css/jquery/jquery-ui.min.css" />" />

<!-- IE8 에서 HTML5 요소와 미디어 쿼리를 위한 HTML5shiv와 Respond.js -->
<!--[if lt IE 9]><-->
<script type="text/javascript" src="<c:url value="/js/util/html5shiv.min.js" />"></script>
<script type="text/javascript" src="<c:url value="/js/util/respond.min.js" />"></script>

<!--[endif]><-->
<script type="text/javascript" src="<c:url value="/js/jquery/jquery.min.js" />"></script>
<script type="text/javascript" src="<c:url value="/js/jquery/jquery-ui.min.js" />"></script>
<script type="text/javascript" src="<c:url value="/js/jquery/jquery.blockUI.js" />"></script>
<script type="text/javascript" src="<c:url value="/js/jquery/jquery.fileupload.js" />"></script>
<script type="text/javascript" src="<c:url value="/js/jquery/jquery.bpopup.min.js" />"></script>
<script type="text/javascript" src="<c:url value="/js/jquery/jquery.serialize-object.js" />"></script>
<script type="text/javascript" src="<c:url value="/js/jquery/colortip-1.0-jquery.js" />"></script>
<script type="text/javascript" src="<c:url value="/js/bootstrap/bootstrap.min.js" />"></script>
<script type="text/javascript" src="<c:url value="/js/util/script.js?v=201803211555" />"></script>
<script type="text/javascript" src="<c:url value="/js/util/tableHeadFixer.js" />"></script>
<script type="text/javascript" src="<c:url value="/js/chart/Chart.min.js" />"></script>
<script type="text/javascript" src="<c:url value="/js/common/com.ajax.js" />"></script>
<script type="text/javascript" src="<c:url value="/js/common/com.page.js?v=201803261710" />"></script>
<script type="text/javascript" src="<c:url value="/js/common/com.util.js?v=201803301620" />"></script>
<script type="text/javascript" src="<c:url value="/js/common/com.date.js?v=201803281440" />"></script>

<script type="text/javascript">
    /**
    *   context setting
    */
    Ajax.setContext("${_context}");
    
    /**
    * Ajax CSRF Token Set.
    */
    $(function () {
        var xsrfToken = $("meta[name='_csrf']").attr("content");
        var xsrfHeader = $("meta[name='_csrf_header']").attr("content");
        $(document).ajaxSend(function(e, xhr, options) {
            xhr.setRequestHeader(xsrfHeader, xsrfToken);
        });
    });
    
    /**
    * 백스페이스 뒤로가기 방지
    */
    $(document).keydown(function(e) {
        if (e.target.nodeName != "INPUT" && e.target.nodeName != "TEXTAREA") {
            if (e.keyCode === 8) {
                return false;
            }
        }
    });
</script>