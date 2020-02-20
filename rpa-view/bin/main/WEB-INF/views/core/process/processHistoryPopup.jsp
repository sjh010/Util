<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html>
<style>
.history_content {
    max-width: 210px; 
    overflow:hidden; 
    text-overflow:ellipsis; 
    white-space: nowrap;
}
</style>
<script type="text/javascript">
$(document).ready(function() {
    $("#btn_closeTop, #btn_closeBottom").click(function() {
        hideBlockLayer("#processHistoryPopup");
    });
    
    // 날짜(변경일시) 조회
    $("#datepicker1").datepicker({
        onSelect : function(dateText) {
            $("#historySearchForm input[name=changeDate]").val(dateText);
            $("#btn_clearDate").show();
            getProcessHistoryList();
        }
    });
    
    $(".ui-datepicker-trigger").attr("title","<spring:message code="tit.com.0000" />");
    $(".ui-datepicker-trigger>img").removeAttr("alt");
    $('[title]').colorTip({color:'black'});
    
    $("#datepicker1").keyup(function(e) {
        var dateText = $("#datepicker1").val();
        
        $("#btn_clearDate").show();
        
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
                $("#historySearchForm input[name=changeDate]").val(searchDate);
                getProcessHistoryList();
            } else {
                alert("<spring:message code="msg.com.0003"/>");
            } 
        } 
    });
    
    $("#btn_clearDate").click(function() {
        $("#historySearchForm input[name=changeDate]").val("");
        $("#datepicker1").val("");
        $("#btn_clearDate").hide();
        getProcessHistoryList();
    });

    // 상태 필터
    $("#selectActivationYnHistory").change(function(e) {
        var code = $(this).val();
        $("#historySearchForm input[name=activationYn]").val(code);
        getProcessHistoryList();
    });
});

function openProcessHistoryPopup(processVersionSequence, processName) {
    initPopup();
    $("#processNameHistory").text(processName);
    $("#historySearchForm input[name=processVersionSequence]").val(Number(processVersionSequence));
    getProcessHistoryList();
    
    showBlockLayer("#processHistoryPopup");
}

function getProcessHistoryList() {    
    Ajax.post().url("/process/history").requestBodyByForm("historySearchForm")
               .success(createHistoryTable)
               .failure(function() {
                   alert("<spring:message code="err.m04.0004"/>");
               }).execute();
}

function createHistoryTable(response) {
    $("#historyTbody").empty();
    var historyList = response.processHistoryList;
    
    if (historyList.length) {
        $.each(historyList, function(index, item) {
            var seq = item.processVersionSequence;
            
            var version = item.majorVersion + "." + item.minorVersion;
            
            var activationYnName = "";
            if (item.removeYn == 'Y') {
                activationYnName = "<spring:message code="ctl.m04.0004" />";
            } else if (item.activationYn == 'Y') {
                activationYnName = "<spring:message code="ctl.m04.0002" />";
            } else {
                activationYnName = "<spring:message code="ctl.m04.0003" />";
            }
            
            var datetime = new Date(item.registerDateTime).format("yyyy-MM-dd HH:mm")
            
            var remarksContent = (item.remarksContent) ? item.remarksContent : "-";
            
            var remarksContent = "-";
            
            if(item.remarksContent) {
                remarksContent = '<a href="#" style="text-decoration: underline" onClick="javascript:showContent(' + seq +')">' + item.remarksContent + '</a>';
            }
            
            var htmlStr = '';
            htmlStr += '<tr>';
            htmlStr += '    <td style="width: 70px">' + version + '</td>';
            htmlStr += '    <td style="width: 90px">' + item.registerId + '</td>';
            htmlStr += '    <td style="width: 180px">' + datetime + '</td>';
            htmlStr += '    <td style="width: 90px">' + activationYnName + '</td>';
            htmlStr += '    <td class="history_content" style="width: 210px">' + remarksContent +'</td>';
            htmlStr += '</tr>';

            $("#historyTbody").append(htmlStr);
        });
    } else {
        var htmlStr = '<tr><th style="width : 640px">' + "<spring:message code="msg.com.0001"/>" + '</th></tr>';
        $("#historyTbody").append(htmlStr);
    }
    
    if(response.count) {
        var count = response.count;
        var pageRowCount = $("#historySearchForm input[name=pageRowCount]").val();
        var pageNo = response.form.pageNo; 
        $("#historySearchForm input[name=pageNo]").val(pageNo);
        setPopupPaging("pagingHistory", pageNo, count, pageRowCount);
        var pagingText = getPagingText(pageNo, count, pageRowCount);
        $("#totalHistoryCount").text("<spring:message code="msg.com.0002" arguments='" + pagingText + "'/>");   
    } else {
        $("#totalHistoryCount").text("");
        $("#pagingHistory").html("");
    }
}

function movePopupPage(pageNo) {
    $("#historySearchForm input[name=pageNo]").val(Number(pageNo));
    getProcessHistoryList();
}

function initPopup() {
    $(".reset").each(function() {
       this.value = "";
    });
    $("#historySearchForm input[name=pageNo]").val(1);
}

function showContent(seq) {
    openContentPopup(seq);
}
</script>

<form:form id="historySearchForm" name="historySearchForm">
    <input type="hidden" class="reset" name="pageNo" id="pageNo" value=1 />
    <input type="hidden" name="pageRowCount" value=7 />
    <input type="hidden" class="reset" name="processVersionSequence" id="processVersionSequence" />
    <input type="hidden" class="reset" name="activationYn" id="activtionYn" />
    <input type="hidden" class="reset" name="changeDate" id="changeDate" />
</form:form>

<div id="processHistoryPopup" class="pop-body" style="display: none; width: 680px; height: 570px;">
    <div class="pop-content">
        <h4 class="pop-title" style="width:600px;"><spring:message code="tit.m04.0010" /> <span id="processNameHistory"></span></h4>
        <a href="#" id="btn_closeTop" class="pop-close"><img src="${_context }/images/btnclose.png"></a>
        <div class="pop-search-wrap clear">
            <div class="pop-row">
                <div class="img-ico img-filter"></div>
                <div class="col-xs-3 xs-first date-group" style="width: 170px !important;">
                    <input type="text" id="datepicker1" class="filter-input reset" placeholder="YYYY-MM-DD"  maxlength="10" />
                    <span id="btn_clearDate" class="filter-clear" style="display:none;"></span>
                </div>
                <div class="col-xs-3 xs-first">
                    <div class="select-container selectpicker-p">
                        <div class="select-viewport"></div>
                        <select id="selectActivationYnHistory" class="selectpicker">
                            <option value=""><spring:message code="ctl.m04.0001" /></option>
                            <option value="Y"><spring:message code="ctl.m04.0002" /></option>
                            <option value="N"><spring:message code="ctl.m04.0003" /></option>
                            <option value="R"><spring:message code="ctl.m04.0004" /></option>
                        </select>
                    </div>
                </div>
            </div>
        </div>
        <table class="pop-fixed_headers clear">
            <thead>
                <tr>
                    <th style="width: 70px"><spring:message code="tit.m04.0001"/></th>
                    <th style="width: 90px"><spring:message code="tit.m04.0007"/></th>
                    <th style="width: 180px"><spring:message code="tit.m04.0008"/></th>
                    <th style="width: 90px"><spring:message code="tit.m04.0006"/></th>
                    <th style="width: 210px"><spring:message code="tit.m04.0009"/></th>
                </tr>
            </thead>
            <tbody id="historyTbody">
            </tbody>
            <tfoot>
                <tr>
                    <td class="pop-tfoot-total">
                        <span id="totalHistoryCount"></span>
                    </td>
                    <td id="pagingHistory" colspan="6">
                    </td>
                </tr>
            </tfoot>
        </table>
        <div class="right btns pop-btns clear pop-btns-notf">
            <span id="btn_closeBottom" class="btn btn-primary"><spring:message code="btn.com.0002"/></span>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/views/core/process/contentPopup.jsp"%>