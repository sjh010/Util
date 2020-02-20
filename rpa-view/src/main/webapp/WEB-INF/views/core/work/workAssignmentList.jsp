<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<%@ include file="/WEB-INF/views/include/frame/header.jsp"%>
<title>INZI RPA SUITE MANAGER</title>

<script type="text/javascript">
var GLOBAL = GLOBAL === undefined ? {} : GLOBAL;
GLOBAL.list = []; // 업무할당(work) 목록 
$(document).ready(function() {
    
    setMenuTab("work");
    
    // 프로세스 검색
    $("#inputProcessName").keyup(function(e) {
        e.preventDefault();
    
        var searchText = $("#inputProcessName").val();
        
        $("#clearProcessName").show();
        
        var key = event.which || event.keyCode;
        if (key == 13) {
            searchProcess();
        }
    });
    
    // 프로세스 검색
    $("#inputProcessName").keydown(function(e) {
        if(e.which == 13) {
            e.preventDefault();
            var processName = $("#inputProcessName").val();
            $("#workAssignmentSearchForm input[name='processName']").val(processName);
            searchProcess();
        }
    });
    
    // 프로세스 검색 x 버튼
    $("#clearProcessName").click(function(){
        $("#inputProcessName").val("");
        $("#workAssignmentSearchForm input[name='processName']").val("");
        $("#clearProcessName").hide();
        searchProcess();
    });
    
    // 등록일자 event
    $("#inputRegisterDateTime").datepicker({
        onSelect: function(dateText) {
            $("#registerDateTime").val(dateText);
            $("#clearRegisterDateTime").show();
            getWorkAssignmentList();
        }
    });
    
    $(".ui-datepicker-trigger").attr("title","날짜 선택");
    $(".ui-datepicker-trigger>img").removeAttr("alt");
    $('[title]').colorTip({color:'black'});
    
    $("#inputRegisterDateTime").keyup(function(e) {
        var dateText = $("#inputRegisterDateTime").val();
        
        $("#clearRegisterDateTime").show();
        
        var key = event.which || event.keyCode
        if (!(key == 8 || key == 46)) {
            maskISODate(this);
        }
    });
    
    $("#inputRegisterDateTime").keydown(function(e){
        if (e.which == 13) {
            e.preventDefault();
            
            var searchDate = $("#inputRegisterDateTime").val();
            
            if(searchDate == "" || validateISODate(searchDate)) {
                $("#registerDateTime").val(searchDate);
                getWorkAssignmentList();
            } else {
                alert("<spring:message code="msg.com.0003"/>");
            } 
        }
    });
    
    $("#clearRegisterDateTime").click(function(){
        $("#pageNo").val(1);
        $("#clearRegisterDateTime").hide();
        $("#inputRegisterDateTime, #registerDateTime").val("");
        
        getWorkAssignmentList();
    });
 
    // 업무 활성/비활성 상태 selectbox event
    $("#selectWorkActivationYn").change(function() {
        $("#pageNo").val(1);
        getWorkAssignmentList();
    });
    
    // 작업 내역 보기 버튼 event
    $("#btnWorkAssignmentHistory").click(showWorkAssignmentHistoryPopup);
    
    // 업무 할당 추가 버튼 event
    $("#btnWorkAssignmentAdd").click(function() {
        
        Ajax.post().sync().url("/work/add/check").success(function(response){
            switch(response.resultCode){
                case "SUCCESS":{
                    showFixedBlockLayerWithUrl("#workAssignmentAddPopup", "${_context}/work/add");
                }
                break;
                
                case "NO_ASSIGNALBE_PROCESS":{
                    alert("<spring:message code="err.m05.0004" />");
                }
                break;
                default:{
                    
                }
            }
        }).failure(function(){
            alert("<spring:message code="err.m05.0006" />");
        }).execute();
    });
    
    // 업무 할당 수정 버튼 event
    $("#btnWorkAssignmentModify").click(showWorkAssignmentModifyPopup);
    
    // 업무 할당 삭제 버튼 event
    $("#btnWorkAssignmetnDelete").click(function() {
        if(confirm("<spring:message code="msg.m05.0005"/>")){
            var workSequenceList = get_selected_work_sequence();
            if(workSequenceList.length > 0){
                var body = {
                    workSequenceList : workSequenceList
                };
                Ajax.post().sync().url("/work/delete").requestBody(JSON.stringify(body))
                    .success(function(response){
                        switch(response.resultCode){
                            case "SUCCESS":{
                                
                            }
                            break;
                            case "ROBOT_IS_WORKING":{
                                alert("<spring:message code="err.m05.0012"/>");
                            }
                        }
                        refresh();
                    })
                    .failure(function(){
                        alert("<spring:message code="err.m05.0011"/>");
                        refresh();
                    })
                    .execute();
            }
            else {
                alert("<spring:message code="msg.com.0000"/>");
            }
        }
    });
    
    // 업무 활성 버튼 event
    $("#btnWorkActivation").click(function() {
        if ($("input[name='checkbox']:checked").length == 0) {
            alert("<spring:message code="msg.com.0000"/>");
        } else {
            setWorkActivation("Y");
        }
    });
 
    // 업무 비활성 버튼 event
    $("#btnWorkNonActivation").click(function() {
        if ($("input[name='checkbox']:checked").length == 0) {
            alert("<spring:message code="msg.com.0000"/>");
        } else {
            setWorkActivation("N");
        }
    });
    
    // 새로 고침 버튼 event
    $("#btnRefresh").click(function() {
        refresh(); 
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
    $("#workThead p").click(function() {
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
            $("#workThead p > span").removeClass("sort-down");
            $("#workThead p > span").removeClass("sort-up");
            $(this).find("span").addClass("sort-down");
            $("#sortOrder").val("DESC");
        }
        getWorkAssignmentList();
    }).css("cursor", "pointer");
    
    initialize();
    getWorkAssignmentList();
});

function initialize() {
    $("#inputProcessName").val("<c:out value="${workAssignmentSearchForm.processName }" />");
    $("#selectWorkActivationYn").val("<c:out value="${workAssignmentSearchForm.activationYn }" />");
}

function showWorkAssignmentHistoryPopup() {
    var selected_list = get_selected_work_sequence();
    if (selected_list.length == 1) {
        var workSequence = selected_list[0];
        var workList = $.grep(GLOBAL.list, function(item){
            return item.workSequence == workSequence;
        });
        var processName = "프로세스 이름";
        var processVersion = "프로세스 버전";
        if(workList.length > 0){
            processName = workList[0].processName;
            processVersion = workList[0].majorVersion + '.' + workList[0].minorVersion;
        }
        
        openTaskQueueListPopup(workSequence, processName, processVersion);
    } else {
        alert("<spring:message code="msg.com.0000"/>");
    }
}

function showWorkAssignmentModifyPopup() {
    var selected_list = get_selected_work_sequence();
    if (selected_list.length == 1) {
        var workSequence = selected_list[0];
        showFixedBlockLayerWithUrl("#workAssignmentModifyPopup", "${_context}/work/modify/" + workSequence);
    } else {
        alert("<spring:message code="msg.com.0000"/>");
    }
}

function getWorkAssignmentList() {
    GLOBAL.list = [];
    Ajax.post().url("/work/list").requestBodyByForm("workAssignmentSearchForm").success(function(response) {
        if (response && response.list && response.list.length > 0) {
            $("#workTbody").empty();
            GLOBAL.list = response.list;
            for (idx in response.list) {
                var item = response.list[idx];
                var htmlStr = '';
                var executeCycle = getExecuteCycle(item.repeatCycleUnitCode, item.repeatCycle, item.executeStandardValue, item.executeHourminute);
                htmlStr += '<tr data-seq="'+item.workSequence+'" data-work-activation="'+item.activationYn+'">';
                htmlStr += '    <td style="width: 60px; min-width:60px;"><input type="checkbox" name="checkbox"></td>';
                htmlStr += '    <td style="width: 30%; min-width:130px">'+item.processName+'</td>';
                htmlStr += '    <td style="width: 10%; min-width:130px">'+item.majorVersion+'.'+item.minorVersion+'</td>';
                htmlStr += '    <td style="width: 12%; min-width:130px">'+item.robotCount+'</td>';
                htmlStr += '    <td style="width: 12%; min-width:130px">'+executeCycle+'</td>';
                htmlStr += '    <td style="width: 30%; min-width:130px">'+setDateTimeFormatFromDate(new Date(item.registerDateTime))+'</td>';
                htmlStr += '    <td style="width: 12%; min-width:130px">'+item.registerId+'</td>';
                htmlStr += '    <td style="width: 12%; min-width:130px">'+(item.activationYn == "Y" ? "활성" : "비활성")+'</td>';
                htmlStr += '</tr>';
                $("#workTbody").append(htmlStr);
            }
            var count = response.totalCount;
            var pageNo = response.form.pageNo;
            $("#pageNo").val(pageNo);
            var pageRowCount = $("#pageRowCount").val();
            setPaging("pagingTd", pageNo, count, pageRowCount);
            var pagingText = getPagingText(pageNo, count, pageRowCount);
            
            $("#totalCount").text("<spring:message code="msg.com.0002" arguments='" + pagingText + "'/>");
            $("#pagingTfoot").removeClass("empty-tf");
            
            // checkbox event
            $("input[name='checkbox']").click(function() {
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
            $("#workTbody").empty();
            $("#workTbody").append('<tr><td class="empty-tb" colspan="8">조회 결과가 없습니다.</td><td></td></tr>');
            $("#pagingTfoot").addClass("empty-tf");
        }
        setButtonActiveState();
        $("input[name='checkboxAll']").prop("checked", false);
    }).failure(function(response) {
        alert("<spring:message code="err.m05.0007"/>");
    }).execute();
}

function getExecuteCycle(repeatCycleUnitCode, repeatCycle, executeStandardValue, executeHourminute) {
    //console.log(repeatCycleUnitCode, repeatCycle, executeStandardValue, executeHourminute);
    var executeCycle = "";
    switch(repeatCycleUnitCode){
        case "TIME":{
            var min = parseInt(executeStandardValue);
            var hour = parseInt(min / 60);
            var minute = min % 60;
            if(hour > 0){
                executeCycle = hour + '시간';
            }
            if(minute > 0){
                if(executeCycle.length > 0){
                    executeCycle += ' ';
                }
                executeCycle += minute + '분';
            }
            executeCycle += ' 간격';

        }
        break;
        case "DAY":{
            executeCycle = '매일 ';
            var h = executeHourminute.substr(0, 2);
            var m = executeHourminute.substr(2, 2);
            executeCycle += getDisplayTime(h, m);

        }
        break;
        case "WEEK":{
            executeCycle = '';
            var day_of_week = [];
            var dow = ['일','월','화','수','목','금','토'];
            for(var i=0; i<7; i++){
                if(executeStandardValue.substr(i, 1) == 1){
                    day_of_week.push(dow[i]);
                }
            }
            executeCycle += day_of_week.join(',') + "요일";
            
            var h = executeHourminute.substr(0, 2);
            var m = executeHourminute.substr(2, 2);
            executeCycle += ' ' + getDisplayTime(h, m);


        }
        break;
        default:{
            executeCycle += (repeatCycleUnitCode?repeatCycleUnitCode:"");
            executeCycle += "/";
            executeCycle += (repeatCycle?repeatCycle:"-");
            executeCycle += "/";
            executeCycle += (executeStandardValue?executeStandardValue:"-");
            executeCycle += "/";
            executeCycle += (executeHourminute?executeHourminute:"-");
        }
    }

    
    
    return executeCycle;
}

function setButtonActiveState() {
    if ($("input[name='checkbox']:checked").length > 1) {
        $("#btnWorkAssignmentHistory").addClass("btn-disabled").unbind("click");
        $("#btnWorkAssignmentModify").addClass("btn-disabled").unbind("click");
        
    } else {
        $("#btnWorkAssignmentHistory").removeClass("btn-disabled").unbind("click").click(showWorkAssignmentHistoryPopup);
        $("#btnWorkAssignmentModify").removeClass("btn-disabled").unbind("click").click(showWorkAssignmentModifyPopup);
    }
}

function movePage(pageNo){
    $('#workAssignmentSearchForm [name=pageNo]').val(pageNo);
    getWorkAssignmentList();
}

function setWorkActivation(workActivationYn) {
    var hasActivationY = false;
    var hasActivationN = false;
    $("input[name='checkbox']:checked").each(function(index, item) {
        var workActivationYn = $(this).parent().parent().data("work-activation");
        if (workActivationYn == "Y") {
            hasActivationY = true;
        } else if (workActivationYn == "N") {
            hasActivationN = true;
        }
    });
    
    if($("input[name='checkbox']:checked").length == 1){
        if (workActivationYn == "Y" && hasActivationY) {
            alert("이미 활성화된 업무입니다.");
            return;
        } else if (workActivationYn == "N" && hasActivationN) {
            alert("이미 비활성 상태의 업무입니다.");
            return;
        }
    }
    
    if (confirm("선택한 항목의 업무 상태를 변경하시겠습니까?")) {
        var $form = $('<form></form>');
        //$form.appendTo('body');
        $form.append('<input type="hidden" value="'+workActivationYn+'" name="activationYn">');
        $("#workTbody input[type='checkbox']:checked").each(function(index, item) {
            $form.append('<input type="hidden" value="'+$(item).parent().parent().data("seq")+'" name="workSequence">');
        });
        
        Ajax.get().url("/work/activation/"+workActivationYn).queryString($form.serialize()).success(function(response) {
            switch(response.errorCode){
                case "ROBOT_IS_WORKING":{
                    alert("<spring:message code="err.m05.0003"/>");
                }
                break;
                case "ROBOT_IS_ASSIGNED":{
                    alert("<spring:message code="err.m05.0013"/>");
                }
                break;
                default:{
                    
                }
            }
            
            switch(response.resultCode){
                case "ACTIVATE_ONLY_INACTIVED":{
                    alert("<spring:message code="msg.m05.0000"/>");
                }
                break;
                
                case "INACTIVATE_ONLY_ACTIVED":{
                    alert("<spring:message code="msg.m05.0001"/>");
                }
                break;
                default:{
                    
                }
            
            }
            getWorkAssignmentList();
        }).failure(function(response) {
            alert("<spring:message code="err.m05.0008"/>");
        }).execute();
    }
    
}

function searchProcess() {
    getWorkAssignmentList();
}

function refresh() {
    getWorkAssignmentList();   
}

function get_selected_work_sequence(){
    var select_list = [];
    $("input[name='checkbox']:checked").each(function(i, e){
        var workSequence = $(e).closest('tr').attr('data-seq');
        select_list.push(workSequence);
    });
    return select_list;
}
</script>
</head>
<body>
    <div class="wrapper">
        <%@ include file="/WEB-INF/views/include/frame/top.jsp"%>
        <%@ include file="/WEB-INF/views/include/frame/menu.jsp"%>

        <!-- content start -->
        <div class="content">
            <form:form id="workAssignmentSearchForm" commandName="workAssignmentSearchForm">
                <div class="search-wrap">
                    <div class="row">
                        <div class="input-group">
                            <div class="img-ico img-search"></div>
                            <input type="hidden" name="processName" />
                            <input id="inputProcessName" type="text" class="filter-input"
                                placeholder="<spring:message code="ctl.m05.0017"/>" />
                            <span id="clearProcessName" class="filter-clear" style="display: none;"></span>
                        </div>
                        <div class="img-ico img-filter"></div>
                        <div class="col-xs-3 xs-first date-group" style="width: 170px !important">
                            <input id="inputRegisterDateTime" type="text" class="datepicker filter-input"
                                placeholder="<spring:message code="ctl.m05.0018"/>" maxlength="10">
                                <span id="clearRegisterDateTime" class="filter-clear" style="display: none;"></span>
                        </div>
                        <div class="col-xs-3">
                            <div class="select-container">
                                <div class="select-viewport"></div>
                                <select id="selectWorkActivationYn" class="selectpicker" name="activationYn">
                                    <option value="">상태: 전체</option>
                                    <option value="Y">활성</option>
                                    <option value="N">비활성</option>
                                </select>
                                
                            </div>
                            
                        </div>
                        <div class="right btns">
                            <span id="btnWorkAssignmentHistory" class="btn btn-primary btn_img btn_wrkhistory" title="<spring:message code="too.m05.0000"/>"></span>
                            <span class="btn-blank">|</span>
<sec:authorize access="hasRole('ROLE_WRK_ASGN_MOD')">
                            <span id="btnWorkActivation" class="btn btn-primary btn_img btn_active" title="<spring:message code="too.m05.0001"/>"></span>
                            <span id="btnWorkNonActivation" class="btn btn-primary btn_img btn_inactive" title="<spring:message code="too.m05.0002"/>"></span>
                            <span class="btn-blank">|</span> 
                            <span id="btnWorkAssignmentModify" class="btn btn-primary btn_img btn_refine" title="<spring:message code="too.m05.0003"/>"></span> 
                            <span id="btnWorkAssignmetnDelete" class="btn btn-primary btn_img btn_trash" title="<spring:message code="too.m05.0004"/>"></span>
                            <span class="btn-blank">|</span>
</sec:authorize>
                            <span id="btnRefresh" class="btn btn-primary btn_img btn_refresh"  title="<spring:message code="too.m05.0005"/>"></span>
<sec:authorize access="hasRole('ROLE_WRK_ASGN_MOD')">
                            <span class="btn-blank">|</span> 
                            <span id="btnWorkAssignmentAdd" class="btn btn-primary btn_img btn_add" title="<spring:message code="too.m05.0006"/>"></span>
</sec:authorize>
                        </div>
                    </div>
                </div>

                <table class="fixed_headers">
                    <thead id="workThead">
                        <tr>
                            <th style="width: 60px; min-width: 60px;"><input type="checkbox" name="checkboxAll" class="allCheck">
                            </th>
                            <th style="width: 30%; min-width: 130px">
                                <p data-sort-key="processName">
                                    <spring:message code="tbl.m05.0000"/><span></span>
                                </p>
                            </th>
                            <th style="width: 10%; min-width: 130px">
                                <p data-sort-key="majorVersion">
                                    <spring:message code="tbl.m05.0001"/><span></span>
                                </p>
                            </th>
                            <th style="width: 12%; min-width: 130px">
                                <p data-sort-key="robotCount">
                                    <spring:message code="tbl.m05.0002"/><span></span>
                                </p>
                            </th>
                            <th style="width: 12%; min-width: 130px">
                                <p data-sort-key="repeatCycleUnitCode">
                                    <spring:message code="tbl.m05.0003"/><span></span>
                                </p>
                            </th>
                            <th style="width: 30%; min-width: 130px">
                                <p data-sort-key="registerDateTime">
                                    <spring:message code="tbl.m05.0004"/><span class="sort-down"></span>
                                </p>
                                </th>
                            <th style="width: 12%; min-width: 130px">
                                <p data-sort-key="registerId">
                                    <spring:message code="tbl.m05.0005"/><span></span>
                                </p>
                            </th>
                            <th style="width: 12%; min-width: 130px">
                                <p data-sort-key="activationYn">
                                    <spring:message code="tbl.m05.0006"/><span></span>
                                </p>
                            </th>
                        </tr>
                    </thead>
                    <tbody id="workTbody"></tbody>
                    <tfoot id="pagingTfoot" class="empty-tf">
                        <tr>
                            <td><span id="totalCount"></span>
                            </td>
                            <td id="pagingTd" colspan="6"></td>
                        </tr>
                    </tfoot>
                </table>

                <input type="hidden" id="registerDateTime" name="registerDateTime"
                    value="<c:out value="${workAssignmentSearchForm.registerDateTime}" />">
                <input type="hidden" id="sortKey" name="sortKey"
                    value="<c:out value="${workAssignmentSearchForm.sortKey}" />">
                <input type="hidden" id="sortOrder" name="sortOrder"
                    value="<c:out value="${workAssignmentSearchForm.sortOrder}" />">
                <input type="hidden" id="pageNo" name="pageNo"
                    value="<c:out value="${workAssignmentSearchForm.pageNo}" />">
                <input type="hidden" id="pageRowCount" name="pageRowCount"
                    value=15>
            </form:form>

        </div>

        <%@ include file="/WEB-INF/views/include/frame/bottom.jsp"%>
    </div>


    <%@ include file="/WEB-INF/views/core/work/taskQueueListPopup.jsp"%>

    <!-- 업무 할당 추가 팝업 -->
    <div id="workAssignmentAddPopup" class="pop-body" style="display: none; width: 500px; height: 470px;"></div>

    <!-- 업무 할당 수정 팝업 -->
    <div id="workAssignmentModifyPopup" class="pop-body" style="display: none; width: 500px; height: 470px;"></div>

    <!-- 작업 내역 보기 팝업 -->
    <div id="workAssignmentHistoryPopup" class="pop-body" style="display: none; width: 680px; height: 570px"></div>

</body>
</html>