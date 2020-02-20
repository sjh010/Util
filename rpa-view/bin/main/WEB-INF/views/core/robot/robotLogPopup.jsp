<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>

<script type="text/javascript">
$(document).ready(function() {
    $("#btn_closeTop, #btn_closeBottom").click(function() {
        hideBlockLayer("#robotLogPopup");
    });
    
    // 날짜(등록일시) 조회
    $("#datepicker1").datepicker({
        onSelect : function(dateText) {
            
            $("#logSearchForm input[name=registerDateTime]").val(dateText);
            $("#btn_clearDate").show();
            getRobotLogList();
        }
    });
    
    $(".ui-datepicker-trigger").attr("title", "<spring:message code="tit.com.0000"/>");
    $(".ui-datepicker-trigger>img").removeAttr("alt");
    $('[title]').colorTip({color:'black'});
    
    $("#datepicker1").keyup(function(e) {
        var dateText = $("#datepicker1").val();
        
        $("#btn_clearDate").show()
        
        var key = event.which || event.keyCode
        if (!(key == 8 || key == 46)) {
            maskISODate(this);
        }
    });
    
    $("#datepicker1").keydown(function(e) {
        if (e.which == 13) {
            e.preventDefault();
            
            var searchDate = $("#datepicker1").val();
            
            if(searchDate == "" || validateISODate(searchDate)) {
                $("#logSearchForm input[name=registerDateTime]").val(searchDate);
                getRobotLogList();
            } else {
                alert("<spring:message code="msg.com.0003"/>");
            } 
        } 
    });
    
    $("#btn_clearDate").click(function() {
       
       $("#logSearchForm input[name=registerDateTime]").val("");
       $("#datepicker1").val("");
       $("#btn_clearDate").hide();
       getRobotLogList();
    });

    // 로봇 로그 유형 필터
    $("#selectRobotLogType").change(function(e) {
        var code = $(this).val();
        
        $("#logSearchForm input[name=robotLogTypeCode]").val(code);
        getRobotLogList();
    });
    
    // 로봇 로그 상태 필터
    $("#selectRobotLogStatus").change(function(e) {
        var code = $(this).val();
        
        $("#logSearchForm input[name=robotLogStatusCode]").val(code);
        getRobotLogList();
    });
    
    // 엑셀 다운로드
    $("#btn_excel").click(function() {
        Ajax.post().url("/robot/log/excel").requestBodyByForm("logSearchForm")
        .success(function(response) {
            location.href = "${_context}/robot/log/excel/download?uuid=" + response;
        }).execute();
    });
});

function openRobotLogPopup(robotSequence, robotName) {
    initPopup();
    
    if(robotName) {
        $("#robotNameLog").text(robotName);
        $("#logSearchForm input[name=robotName]").val(robotName);
    } else {
        $("#robotNameLog").text("");
        $("#logSearchForm input[name=robotName]").val("");
    }
    
    $("#logSearchForm input[name=robotSequence]").val(Number(robotSequence));
    
    getRobotLogFilter(robotSequence);
    getRobotLogList();
    
    showBlockLayer("#robotLogPopup");
}

function getRobotLogFilter(robotSequence) {
    Ajax.post().url("/robot/log/filter").queryString("robotSequence=" + robotSequence)
        .success(createFilter)
        .failure(function() {
            alert("<spring:message code="err.m03.0003"/>");
        }).execute();
}

function getRobotLogList() {    
    Ajax.post().url("/robot/log").requestBodyByForm("logSearchForm")
        .success(createRobotLogTable)
        .failure(function() {
            alert("<spring:message code="err.m03.0002"/>");
        }).execute();
}

function createFilter(response) {
    $("#selectRobotLogType").html('<option value="">' + "<spring:message code="ctl.m03.0002"/>" + '</option>');
    $("#selectRobotLogStatus").html('<option value="">' + "<spring:message code="ctl.m03.0001"/>" + '</option>');
    
    var robotLogTypeList = response.robotLogTypeList;
    var robotLogStatusList = response.robotLogStatusList;
    
    if(robotLogTypeList.length) {
        $.each(robotLogTypeList, function(index, item) {
            var html = '<option value="' + item.code + '">' + item.codeName + '</option>';
            $("#selectRobotLogType").append(html);
        });
    }
    
    if(robotLogStatusList.length) {
        $.each(robotLogStatusList, function(index, item) {
            var html = '<option value="' + item.code + '">' + item.codeName + '</option>';
            $("#selectRobotLogStatus").append(html);
        });
    }
}

function createRobotLogTable(response) {
    $("#robotLogTbody").empty();
    var logList = response.robotLogList;
    
    if (response.count && logList.length) {
        $.each(logList, function(index, item) {
            var datetime = new Date(item.registerDateTime).format("yyyy-MM-dd HH:mm");
            
            var downloadLink = "";
            if(item.robotLogStatusCode == "FAIL" && item.logFileGroupSequence) {
                var address = "${_context}/robot/log/error/download?";
                address += "fileGroupSequence=" + item.logFileGroupSequence;
                address += "&robotName=" + $("#logSearchForm input[name=robotName]").val();
                downloadLink = '<a href="' + address + '">' + "<spring:message code="btn.m03.0005"/>" + '</a>';
            }
            
            var htmlStr = '';
            htmlStr += '<tr>';
            htmlStr += '    <td style="width: 160px">' + datetime + '</td>';
            htmlStr += '    <td style="width: 200px">' + item.robotLogTypeCodeName + '</td>';
            htmlStr += '    <td style="width: 140px">' + item.robotLogStatusCodeName + '</td>';
            htmlStr += '    <td style="width: 140px">' + downloadLink + '</td>';
            htmlStr += '</tr>';

            $("#robotLogTbody").append(htmlStr);
            
        });
    } else {
        var htmlStr = '<tr><th style="width : 640px">' + "<spring:message code="msg.com.0001"/>" + '</th></tr>';
        $("#robotLogTbody").append(htmlStr);
    }
    
    if(response.count) {
        var count = response.count;
        var pageRowCount = $("#logSearchForm input[name=pageRowCount]").val();
        var pageNo = response.form.pageNo;
        $("#logSearchForm input[name=pageNo]").val(pageNo);
        setPopupPaging("pagingLog", $("#logSearchForm input[name=pageNo]").val(), response.count, pageRowCount);
        var pagingText = getPagingText(pageNo, count, pageRowCount);
        $("#totalLogCount").text("<spring:message code="msg.com.0002" arguments='" + pagingText + "'/>"); 
    } else {
        $("#totalLogCount").text("");
        $("#pagingLog").html("");
    }
}

function movePopupPage(pageNo) {
    $("#logSearchForm input[name=pageNo]").val(Number(pageNo));
    getRobotLogList();
}

function initPopup() {
    $(".reset").each(function() {
       this.value = "";
    });
    $("#logSearchForm input[name=pageNo]").val(1);
}
</script>

<form:form name="logSearchForm" id="logSearchForm">
    <input type="hidden" class="reset" name="pageNo" id="pageNo" value=1 />
    <input type="hidden" name="pageRowCount" value=7 />
    <input type="hidden" class="reset" name="robotName" id="robotName" />
    <input type="hidden" class="reset" name="robotSequence" id="robotSequence" />
    <input type="hidden" class="reset" name="robotLogTypeCode" id="robotLogTypeCode" />
    <input type="hidden" class="reset" name="robotLogStatusCode" id="robotLogStatusCode" />
    <input type="hidden" class="reset" name="registerDateTime" id="registerDateTime" />
</form:form>

<div id="robotLogPopup" class="pop-body" style="display: none; width: 680px; height: 570px;">
    <div class="pop-content">
        <h4 class="pop-title" style="width:600px;"><spring:message code="tit.m03.0009"/> <span id="robotNameLog"></span></h4>
        <a id="btn_closeTop" href="#" class="pop-close"><img src="<c:url value="/images/btnclose.png" />"></a>
        <div class="pop-search-wrap clear">
            <div class="pop-row">
                <div class="img-ico img-filter"></div>
                <div class="col-xs-3 xs-first date-group" style="width: 170px !important;">
                    <input type="text" id="datepicker1" class="filter-input reset" placeholder="YYYY-MM-DD"  maxlength="10"
                        style="margin-right: 5px;" />
                    <span id="btn_clearDate" class="filter-clear" style="display:none;"></span>
                </div>
                <div class="col-xs-3 xs-first">
                    <div class="select-container selectpicker-p">
                        <div class="select-viewport"></div>
                        <select id="selectRobotLogType" class="selectpicker">
                            <option value=""><spring:message code="ctl.m03.0002"/></option>
                        </select>
                    </div>
                </div>
                <div class="col-xs-3">
                    <div class="select-container selectpicker-p">
                        <div class="select-viewport"></div>
                        <select id="selectRobotLogStatus" class="selectpicker">
                            <option value=""><spring:message code="ctl.m03.0002"/></option>
                        </select>
                    </div>
                </div>
                <sec:authorize access="hasRole('ROLE_RBT_READ')">
                    <div class="right btns" id="btn_excel">
                        <span class="btn btn-primary btn_img btn_excel" 
                            title="<spring:message code="btn.m03.0004"/>" ></span>
                    </div>
                </sec:authorize>
            </div>
        </div>

        <table class="pop-fixed_headers clear">
            <thead>
                <tr>
                    <th style="width: 160px"><spring:message code="tit.m03.0006"/></th>
                    <th style="width: 200px"><spring:message code="tit.m03.0007"/></th>
                    <th style="width: 140px"><spring:message code="tit.m03.0005"/></th>
                    <th style="width: 140px"><spring:message code="tit.m03.0008"/></th>
                </tr>
            </thead>
            <tbody id="robotLogTbody">
            </tbody>
            <tfoot>
                <tr>
                    <td class="pop-tfoot-total" id="totalLogCount"></td>
                    <td id="pagingLog" colspan="6"></td>
                </tr>
            </tfoot>
        </table>
        <div class="right btns pop-btns pop-btns-notf clear">
            <span id="btn_closeBottom" class="btn btn-primary"><spring:message code="btn.com.0002"/></span>
        </div>
    </div>
</div>   