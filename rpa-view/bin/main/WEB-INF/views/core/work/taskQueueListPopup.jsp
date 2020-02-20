<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<script type="text/javascript">
$(document).ready(function() {

    $("#taskQueueListPopup .btnClosePopup").click(function() {
        hideBlockLayer("#taskQueueListPopup");
    });

    // 로봇 선택 필터
    $("#taskQueueListPopup #selectRobot").change(function(e) {
        
        var name = $('#taskQueueListPopup #selectRobot :selected').text().trim();
        if($(this).val() == ''){
            name = '';
        }
        
        $("#taskQueueSearchForm input[name=robotName]").val(name);
        getTaskQueueList();
    });
    
    // 로그 상태 필터
    $("#taskQueueListPopup #selectStatus").change(function(e) {
        var code = $(this).val();
        
        $("#taskQueueSearchForm input[name=taskStatusCode]").val(code);
        getTaskQueueList();
    });

    // sort button event
    $("#taskQueueListPopup thead p").click(function() {
        $("#taskQueueSearchForm #sortKey").val($(this).data("sort-key"));
        if ($(this).find("span").hasClass("sort-down")) {
            $(this).find("span").removeClass("sort-down");
            $(this).find("span").addClass("sort-up");
            $("#taskQueueSearchForm #sortOrder").val("ASC");
        } else if ($(this).find("span").hasClass("sort-up")) {
            $(this).find("span").removeClass("sort-up");
            $(this).find("span").addClass("sort-down");
            $("#taskQueueSearchForm #sortOrder").val("DESC");
        } else {
            $("#taskQueueListPopup thead p > span").removeClass("sort-down");
            $("#taskQueueListPopup thead p > span").removeClass("sort-up");
            $(this).find("span").addClass("sort-down");
            $("#taskQueueSearchForm #sortOrder").val("DESC");
        }
        getTaskQueueList();
    }).css("cursor", "pointer");
});

function openTaskQueueListPopup(workSequence, processName, processVersion){
    initPopup(workSequence, processName, processVersion);
    showBlockLayer("#taskQueueListPopup");
    
    getTaskQueueFilter(workSequence);
    getTaskQueueList(); 

}

function getTaskQueueFilter(workSequence) {
    
     Ajax.post()
         .url("/taskqueue/list/filter")
         .queryString("workSequence=" + workSequence)
         .success(createFilter)
         .execute();
}
function getTaskQueueList() {    
    Ajax.post()
        .url("/taskqueue/list")
        .requestBodyByForm("taskQueueSearchForm")
        .success(createTaskQueueListTable)
        .execute();
}

function createFilter(response) {
    var select_robot_name = "#taskQueueListPopup #selectRobot";
    $(select_robot_name + ' option').remove();
    $(select_robot_name).html('<option value="">로봇 : 전체</option>');
    
    var robotNameList = response.robotNameList;
    robotNameList.forEach(function(robotName){
        var v = robotName.robotSequence;
        var t = robotName.robotName;
        $(select_robot_name).append($("<option>").val(v).text(t));
    });
}

function createTaskQueueListTable(response) {
    var tbody_selector = "#taskQueueListPopup tbody";
    $(tbody_selector).empty();
    var count = response.count;
    var taskQueueList = response.taskQueueList;

    if(count > 0){
        taskQueueList.forEach(function(taskQueue){
            var lastExecuteDateTime = new Date(taskQueue.lastExecuteDateTime).format("yyyy-MM-dd HH:mm");
            var nextExecuteDateTime = new Date(taskQueue.nextExecuteDateTime).format("yyyy-MM-dd HH:mm");

            var htmlStr = '';
            htmlStr += '<tr>';
            htmlStr += '    <td style="width: 140px" title="'+ taskQueue.robotName +'">' + taskQueue.robotName + '</td>';
            htmlStr += '    <td style="width: 200px">' + lastExecuteDateTime + '</td>';
            htmlStr += '    <td style="width: 200px">' + nextExecuteDateTime + '</td>';
            htmlStr += '    <td style="width: 100px">' + taskQueue.taskStatusName + '</td>';
            htmlStr += '</tr>';
            $(tbody_selector).append(htmlStr);
        });
    }else{
        var htmlStr = '<tr><th style="width : 640px">조회 결과가 없습니다.</th></tr>';
        $(tbody_selector).append(htmlStr);
    }
    
    if(count > 0){
        var pageRowCount = parseInt($("#taskQueueSearchForm #pageRowCount").val());
        var pageNo = response.form.pageNo;
        $("#taskQueueSearchForm input[name=pageNo]").val(pageNo);
        setPopupPaging("pagingLog", pageNo, count, pageRowCount);
        var pagingText = getPagingText(pageNo, count, pageRowCount);
        $("#taskQueueListPopup #totalCount").text("<spring:message code="msg.com.0002" arguments='" + pagingText + "'/>");
    }else{
        $("#taskQueueListPopup #totalCount").text("");
        $("#taskQueueListPopup #pagingLog").html("");
    }
    
}

function movePopupPage(pageNo) {
    $("#taskQueueSearchForm input[name=pageNo]").val(Number(pageNo));
    getTaskQueueList();
}

function initPopup(workSequence, processName, processVersion) {
    $("#spanProcessName").text(processName);
    $("#spanProcessVersion").text(processVersion);
    
    $("taskQueueSearchForm .reset").each(function() {
       this.value = "";
    });
    $("#taskQueueSearchForm input[name=pageNo]").val(1);
    $("#taskQueueSearchForm input[name=robotSequence]").val(0);
    $("#taskQueueSearchForm input[name=workSequence]").val(workSequence);
}

</script>


    
<div id="taskQueueListPopup" class="pop-body" style="display: none; width:680px; height: 662px;">
    <form:form name="taskQueueSearchForm" id="taskQueueSearchForm">
        <input type="hidden" name="workSequence" />
        <input type="hidden" class="reset" name="pageNo" id="pageNo" value=1 />
        <input type="hidden" class="reset" name="robotName" id="robotName" />
        <input type="hidden" name="robotSequence" id="robotSequence" />
        <input type="hidden" class="reset" name="taskStatusCode" />
        
        <input type="hidden" id="sortKey" name="sortKey" />
        <input type="hidden" id="sortOrder" name="sortOrder"/>
        <input type="hidden" id="pageNo" name="pageNo" value="1"/>
        <input type="hidden" id="pageRowCount" name="pageRowCount" value="7" />">
    </form:form>
    <div class="pop-content">
        <h4 class="pop-title">작업 내역 보기 | <span id="spanProcessName"></span>-<span id="spanProcessVersion"></span>
        </h4>
        <a href="javascript:;" class="pop-close btnClosePopup"><img src="images/btnclose.png"></a>
        <div class="pop-search-wrap clear">
            <div class="pop-row">
                <div class="img-ico img-filter"></div>
                <div class="col-xs-3 xs-first">
                    <div class="select-container">
                        <div class="select-viewport"></div>
                        <select id="selectRobot" class="selectpicker">
                            <option>로봇: 전체</option>
                        </select>
                    </div>
                </div>
                <div class="col-xs-3 xs-first">
                    <div class="select-container">
                        <div class="select-viewport"></div>
                        <select id="selectStatus" class="selectpicker">
                            <option value="">상태: 전체</option>
                            <option value="WAIT">대기</option>
                            <option value="DLVR">전달</option>
                            <option value="SCCS">성공</option>
                            <option value="FAIL">실패</option>
                            <option value="STOP">작업중지</option>
                            <option value="ERR">오류</option>
                        </select>
                    </div>
                </div>
            </div>
        </div>

        <table class="pop-fixed_headers clear">
            <thead>
                <tr>
                    <th style="width: 140px"><p data-sort-key="robotName">로봇<span></span></p></th>
                    <th style="width: 200px"><p data-sort-key="lastExecuteDateTime">최종 실행 일시<span></span></p></th>
                    <th style="width: 200px"><p data-sort-key="nextExecuteDateTime">다음 실행 일시<span></span></p></th>
                    <th style="width: 100px"><p data-sort-key="taskStatusName">상태<span></span></p></th>
                </tr>
            </thead>
            <tbody>
            </tbody>
            <tfoot>
                <tr>
                    <td id="totalCount" class="pop-tfoot-total"></td>
                    <td id="pagingLog" colspan="6"></td>
                </tr>
            </tfoot>
        </table>
        <div class="right btns pop-btns pop-btns-notf clear">
            <span id="btnClosePopup" class="btn btn-primary btnClosePopup">닫기</span>
        </div>
    </div>
</div>

 