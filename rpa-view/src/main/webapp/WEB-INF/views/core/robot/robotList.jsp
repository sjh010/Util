<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.mobileleader.rpa.data.type.RobotStatusCode"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<%@ include file="/WEB-INF/views/include/frame/header.jsp"%>
<title>INZI RPA SUITE MANAGER</title>
<script type="text/javascript">
$(document).ready(function() {
    setMenuTab("robot");
    
    // 로봇 검색 버튼 event
    $("#btnRobotSearch").click(function() {
        searchRobot();
    });
    
    $("#inputRobotName").keyup(function(e){
        var text = $(this).val();
        $("#btn_clear").show();
    });
    
    $("#inputRobotName").keydown(function(e) {
        if(e.which == 13){
            e.preventDefault();
            var searchText = $("#inputRobotName").val();
            searchRobot();
        }
    });
    
    $("#btn_clear").click(function(){
        $("#inputRobotName").val("");
        $("#btn_clear").hide();
        searchRobot();
    });
    
    // 로그보기 버튼 event
    $("#btnRobotLog").click(showRobotLogPopup)
    
    // 페이지 리프레쉬 버튼 event
    $("#btnRefresh").click(function() {
        refresh(); 
    });
    
    // 로봇 추가 버튼 event
    $("#btnRobotAdd").click(function() {
        openRobotAddPopup("#robotAddPopup");
    })
    
    // 로봇 삭제 버튼 event
    $("#btnRobotDelete").click(function() {
        if ($("input[name='checkbox']:checked").length > 0) {
            deleteRobot();
        } else {
            alert("<spring:message code="msg.com.0000"/>");
        }
    });
    
    // 로봇 PC명 selectbox event
    $("#selectPcName").change(function() {
        getRobotList();
    });
    
    // 로봇 상태 selectbox event
    $("#selectRobotStatusCode").change(function() {
        getRobotList();
    });
    
    // checkboxAll event
    $("input[name='checkboxAll']").click(function() {
        if ($(this).is(":checked")) {
            $("input[name='checkbox']").prop("checked", true);
            $("input[name='checkbox']").each(function() {
                $(this).parent().parent().addClass("tr-select");
            });
        } else {
            $("input[name='checkbox']").prop("checked", false);
            $("input[name='checkbox']").each(function() {
                $(this).parent().parent().removeClass("tr-select");
            });
        }
        setButtonActiveState();
    });
    
    // sort button event
    $("#robotThead p").click(function() {
        $("#sortKey").val($(this).data("sort-key"));
        if ($(this).find("span").hasClass("sort-down")) {
            $(this).find("span").removeClass("sort-down");
            $(this).find("span").addClass("sort-up");
            $("#sortOrder").val("ASC");
        } else if ($(this).find("span").hasClass("sort-up")) {
            $(this).find("span").removeClass("sort-up");
            $(this).find("span").addClass("sort-down");
            $("#sortOrder").val("DESC");
        } else {
            $("#robotThead p > span").removeClass("sort-down");
            $("#robotThead p > span").removeClass("sort-up");
            $(this).find("span").addClass("sort-down");
            $("#sortOrder").val("DESC");
        }
        getRobotList();
    }).css("cursor", "pointer");
    
    // 로봇 관리 목록 조회
    initialize();
    getRobotList();
    getPcNameList();
});

function initialize() {
    $("#inputRobotName").val("<c:out value="${robotSearchForm.robotName }" />");
    $("#selectPcName").val("<c:out value="${robotSearchForm.pcName }" />");
    $("#selectRobotStatusCode").val("<c:out value="${robotSearchForm.robotStatusCode }" />");
}

function showRobotLogPopup() {
    if ($("input[name='checkbox']:checked").length == 1) {
        var selectedRobot = $("input[name=checkbox]:checked").parent().parent();
         openRobotLogPopup(selectedRobot.data("seq"), selectedRobot.data("name"));
    } else {
        if (!$(this).hasClass("disabled")) {
            alert("<spring:message code="msg.com.0000"/>");
        }
    }
}

function getRobotList() {
    Ajax.post().url("/robot/list").requestBodyByForm("robotSearchForm").success(function(response) {
        if (response && response.list && response.list.length > 0) {
            $("#robotTbody").empty();
            for (idx in response.list) {
                var item = response.list[idx];
                var processName = (item.lastExecuteProcessSequence != null) ? (item.processName ? item.processName : "-") : "-";
                var robotStatusCodeName = (item.robotStatusCodeName !== undefined) ? item.robotStatusCodeName : "-";
                var htmlStr = '';
                htmlStr += '<tr data-seq="'+item.robotSequence+'" data-name="' + item.robotName + '" data-status="'+item.robotStatusCode+'">';
                htmlStr += '    <td style="width:60px; min-width:60px"><input type="checkbox" name="checkbox"></td>';
                htmlStr += '    <td style="width:18%; min-width:110px">' + item.robotName + '</td>';
                htmlStr += '    <td style="width:18%; min-width:130px">' + item.pcIpAddress + '</td>';
                htmlStr += '    <td style="width:18%; min-width:150px">' + item.pcName + '</td>';
                htmlStr += '    <td style="width:18%; min-width:160px">' + (item.lastActionDateTime != null ? setDateTimeFormatFromDate(new Date(item.lastActionDateTime)) : "-") + '</td>';
                htmlStr += '    <td style="width:18%; min-width:170px">' + processName + '</td>';
                htmlStr += '    <td style="width:18%; min-width:120px">' + robotStatusCodeName + '</td>';
                htmlStr += '</tr>';
                $("#robotTbody").append(htmlStr);
            }
            // 페이징 영역
            var count = response.totalCount;
            var pageRowCount = $("#pageRowCount").val();
            var pageNo = response.form.pageNo;
            $("#pageNo").val(pageNo);
            setPaging("pagingTd", pageNo, count, pageRowCount);
            var pagingText = getPagingText(pageNo, count, pageRowCount);
            $("#totalCount").text("<spring:message code="msg.com.0002" arguments='" + pagingText + "'/>");
            
            $("#pagingTfoot").removeClass("empty-tf");
            
            // checkbox event
            $("input[name='checkbox']").click(function(e) {
                if($(this).prop("checked")) {
                    $(this).parent().parent().addClass("tr-select");
                } else {
                    $(this).parent().parent().removeClass("tr-select");
                }
                
                if ($("input[name='checkbox']").length == $("input[name='checkbox']:checked").length) {
                    $("input[name='checkboxAll']").prop("checked", true);
                } else {
                    $("input[name='checkboxAll']").prop("checked", false);
                }
                setButtonActiveState();
            });
        } else {
            $("#robotTbody").empty();
            $("#robotTbody").append('<tr><td class="empty-tb" colspan="7">' + "<spring:message code="msg.com.0001"/>" + '</td><td></td></tr>');
            $("#pagingTfoot").addClass("empty-tf");
        }
        $("input[name='checkboxAll']").prop("checked", false);
        setButtonActiveState();
    }).failure(function(response) {
        alert("<spring:message code="err.m03.0000"/>");
    }).execute();
}

function setButtonActiveState() {
    if ($("input[name='checkbox']:checked").length > 1) {
        $("#btnRobotLog").addClass("btn-disabled").unbind("click");
    } else {
        $("#btnRobotLog").removeClass("btn-disabled").unbind("click").click(showRobotLogPopup);
    }
}

function movePage(pageNo) {
    $("#pageNo").val(pageNo);
    getRobotList();
}

function searchRobot() {
    getRobotList();
}

function deleteRobot() {
    var checkWorking = false;
    $.each($("input[name='checkbox']:checked"), function(index, item) {
        var robotStatusCode = $(this).parent().parent().data("status");
        if (robotStatusCode == "WRKG") {
            checkWorking = true;
            alert("<spring:message code="msg.m03.0000"/>");
        }
    });
    
    if (!checkWorking) {
        if (confirm("<spring:message code="cfm.m03.0000"/>")) {
            var $form = $('<form></form>');
            $form.appendTo('body');
            $.each($("input[name='checkbox']:checked"), function(index, item) {
                $form.append('<input type="hidden" value="'+$(this).parent().parent().data("seq")+'" name="robotSequence">');
            });
             
            // request
            Ajax.get().url("/robot/delete/process").queryString($form.serialize()).success(function(response) {
                if (response && response == $("input[name='checkbox']:checked").length) {
                    // do nothing
                } else {
                    // 삭제 당시에 실행 중인 로봇이 있었을 경우
                }
                getPcNameList();
                getRobotList();
            }).failure(function(response) {
                if(response.responseJSON && response.responseJSON.exceptionMessage){
                    alert(response.responseJSON.exceptionMessage);
                }else{
                    alert("<spring:message code="err.m03.0001"/>");
                }
                
            }).execute();
        }
    }
}

function refresh() {
    getPcNameList();
    getRobotList();
}

function getPcNameList() {
    Ajax.post().sync().url("/robot/list/pcName")
        .success(createPcNameFilter)
        .failure(function() {
            alert("<spring:message code="err.m03.0005"/>");  
        }).execute();
}

function createPcNameFilter(pcNameList) {
    var selectedPcName = $("#selectPcName").val();
    var exist = false;
    
    $("#selectPcName").empty();
    $("#selectPcName").append('<option value="">' + "<spring:message code="ctl.m03.0000"/>" + '</option>');
    
    $.each(pcNameList, function(index, name) {
       $("#selectPcName").append('<option value="' + name + '">' + name + '</option>'); 
       if(selectedPcName == name) {
           exist = true;
           $("#selectPcName").val(selectedPcName);
       }
    });
    
    if(!exist) {
        $("#selectPcName").val("");
    }
}
</script>
</head>
<body>
    <div class="wrapper">
        <%@ include file="/WEB-INF/views/include/frame/top.jsp"%>
        <%@ include file="/WEB-INF/views/include/frame/menu.jsp"%>

        <!-- content start -->
        <div class="content">
            <form:form id="robotSearchForm" commandName="robotSearchForm">
            <div class="search-wrap">
                <div class="row">
                    <div class="input-group">
                        <div id="btnRobotSearch" class="img-ico img-search"></div>
                        <input id="inputRobotName" name="robotName" type="text" class="filter-input" placeholder="<spring:message code="tit.m03.0010"/>">
                        <span id="btn_clear" class="filter-clear" style="display: none;"></span> 
                    </div>
                    <div class="img-ico img-filter"></div>
                    <div class="col-xs-4">
                        <div class="select-container">
                            <div class="select-viewport"></div>
                            <select id="selectPcName" name="pcName" class="selectpicker">
                                <option value=""><spring:message code="ctl.m03.0000"/></option>
                            </select>
                        </div>
                        <div class="select-container">
                            <div class="select-viewport"></div>
                            <select id="selectRobotStatusCode" name="robotStatusCode" class="selectpicker">
                                <option value=""><spring:message code="ctl.m03.0001"/></option>
                                <c:forEach var="robotStatusCode" items="${robotStatusCodeList }" varStatus="status">
                                    <c:if test="${not empty robotStatusCode.code}"><option value="<c:out value="${robotStatusCode.code }" />"><c:out value="${robotStatusCode.codeName }" /></option></c:if>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                    
                    <div class="right btns">
                        <sec:authorize access="hasRole('ROLE_RBT_READ')">
                            <span id="btnRobotLog" class="btn btn-primary btn_img btn_logview" 
                                title="<spring:message code="btn.m03.0000"/>"></span>
                            <span class="btn-blank">|</span>
                        </sec:authorize>
                        <sec:authorize access="hasRole('ROLE_RBT_MOD')">
                            <span id="btnRobotDelete" class="btn btn-primary btn_img btn_trash" 
                                title="<spring:message code="btn.m03.0001"/>"></span>
                        </sec:authorize>
                        <sec:authorize access="hasRole('ROLE_RBT_READ')">
                            <span id="btnRefresh" class="btn btn-primary btn_img btn_refresh"  
                                title="<spring:message code="btn.m03.0002"/>"></span>
                        </sec:authorize> 
                        <sec:authorize access="hasRole('ROLE_RBT_MOD')">
                            <span id="btnRobotAdd" class="btn btn-primary btn_img btn_add"  
                                title="<spring:message code="btn.m03.0003"/>"></span>
                        </sec:authorize>
                    </div>
                </div>
            </div>

            <table class="fixed_headers">
                <thead id="robotThead">
                    <tr>
                        <th style="width:60px; min-width:60px"><input type="checkbox" name="checkboxAll" class="allCheck"></th>
                        <th style="width:18%; min-width:110px"><p data-sort-key="robotName"><spring:message code="tit.m03.0000"/><span></span></p></th>
                        <th style="width:18%; min-width:130px"><p data-sort-key="pcIpAddress"><spring:message code="tit.m03.0001"/><span></span></p></th>
                        <th style="width:18%; min-width:150px"><p data-sort-key="pcName"><spring:message code="tit.m03.0002"/><span></span></p></th>
                        <th style="width:18%; min-width:160px"><p data-sort-key="lastActionDateTime"><spring:message code="tit.m03.0003"/><span class="sort-down"></span></p></th>
                        <th style="width:18%; min-width:170px"><p data-sort-key="lastExecuteProcessSequence"><spring:message code="tit.m03.0004"/><span></span></p></th>
                        <th style="width:18%; min-width:120px"><p data-sort-key="robotStatusCode"><spring:message code="tit.m03.0005"/><span></span></p></th>
                    </tr>
                </thead>
                <tbody id="robotTbody"></tbody>
                <tfoot id="pagingTfoot" class="empty-tf">
                    <tr>
                        <td><span id="totalCount"></span></td>
                        <td id="pagingTd" colspan="6"></td>
                    </tr>
                </tfoot>
            </table>

            <input type="hidden" id="sortKey" name="sortKey" value="<c:out value="${robotSearchForm.sortKey }" />">
            <input type="hidden" id="sortOrder" name="sortOrder" value="<c:out value="${robotSearchForm.sortOrder }" />">
            <input type="hidden" id="pageNo" name="pageNo" value="<c:out value="${robotSearchForm.pageNo }" />">
            <input type="hidden" id="pageRowCount" name="pageRowCount" value=15>
            </form:form>
            
        </div>
        
        <%@ include file="/WEB-INF/views/include/frame/bottom.jsp"%>
    </div>
    
    <%@ include file="/WEB-INF/views/core/robot/robotLogPopup.jsp" %>
    
    <!-- 로봇 추가 팝업 -->
    <%@ include file="/WEB-INF/views/core/robot/robotAddPopup.jsp" %>
    
</body>
</html>