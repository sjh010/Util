<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<%@ include file="/WEB-INF/views/include/frame/header.jsp"%>

<title>INZI RPA SUITE MANAGER</title>
<script type="text/javascript">
var processCount = 0;
var processMap = {};

// 체크박스 선택 이벤트
$(document).on("click", ".processCheckbox", function(e) {

    var tr = $(this).parent().parent();
    
    var seq = $(this).data("seq");
    
    if ($(this).prop("checked")) {
        processCount++;   
        tr.addClass("tr-select");
    } else {
        processCount--;
        tr.removeClass("tr-select");
    }
    
    if(checkSelectedProcess(2)) {
        deactivateButton();
    } else {
        activateButton();    
    }
});

$(document).ready(function() {
    // 메뉴 활성화
    setMenuTab("process");
    
    // 정렬
    $("#processThead").on("click", "th", function() {
        var id = $(this).attr("id");
        
        if(id) {
            var newSortKey = $(this).attr("id");
            var sortKey = $("#searchForm input[name=sortKey]").val();
            
            $("#searchForm input[name=sortKey]").val($(this).attr("id"));
            
            var sortOrder = $("#searchForm input[name=sortOrder]").val();
            
            if(newSortKey != sortKey) {
                $("#searchForm input[name=sortOrder]").val("desc");
            } else {
                if(sortOrder == "desc") {
                    $("#searchForm input[name=sortOrder]").val("asc");
                } else {
                    $("#searchForm input[name=sortOrder]").val("desc");
                }
            }
            
            $("#processThead").find('th').each(function(index, item) {
                $(this).find('span').removeClass();
            });
            
            var sortClass = $("#searchForm input[name=sortOrder]").val() == "desc" ? "sort-down" : "sort-up";
            
            $(this).find('span').addClass(sortClass);
            
            getProcessList();
        }
     }).css("cursor", "pointer");
    
    // 체크박스 전체선택
    $(".allCheck").click(function() {
        if ($(".allCheck").prop("checked")) {
            $("input[class=processCheckbox]").each(function() {
                $(this).prop("checked", true);
                $(this).parent().parent().addClass("tr-select");
                processCount++;
            });
            deactivateButton();
        } else {
            $("input[class=processCheckbox]").each(function() {
                $(this).prop("checked", false);
                $(this).parent().parent().removeClass("tr-select");
                processCount = 0;
                
            });
            activateButton();
        }
    });
    
    // 검색 X버튼
    $("#searchForm #searchText").keyup(function(e) {
        var searchText = $("#searchForm #searchText").val();
        
        $("#btn_clear").show();
    });

    // 프로세스 검색
    $("#searchForm #searchText").keydown(function(e) {
        if (e.which == 13) {
            e.preventDefault();
            var processName = $("#searchForm #searchText").val();
            $("#searchForm input[name=processName]").val(processName);
            getProcessList();
        }
    });
    
    // 검색 X버튼 클릭
    $("#btn_clear").click(function() {
        $("#searchForm #searchText").val("");

        
        $("#searchForm input[name=processName]").val("");
        $(this).hide();
        getProcessList();
    });

    // 형상관리 필터
    $("#selectConfigManagementStatusCode").change(function(e) {
        var code = $(this).val();
        
        $("#searchForm input[name=configManagementStatusCode]").val(code);
        getProcessList();
    });

    // 상태 필터
    $("#selectActivationYn").change(function(e) {
        var code = $(this).val();
        
        $("#searchForm input[name=activationYn]").val(code);
        getProcessList();
    });

    // 이력보기
    $("#btn_processHistory").click(showHistory);

    // 체크인
    $("#btn_checkIn").click(checkInProcess);
    
    // 상태변경
    $("#btn_statusChange").click(changeStatus);

    // 다운로드
    $("#btn_download").click(downloadProcess);

    // 삭제
    $("#btn_delete").click(deleteProcess);

    // 새로고침
    $("#btn_refresh").click(refresh);

    initForm();
});

//선택된 프로세스 개수 확인
function checkSelectedProcess(count) {
    if (processCount >= count) {
        return true;
    } else {
        return false;
    }
}

// 선택된 프로세스 버전 시퀀스 리스트 반환
function getSelectedProcessList() {
    var processVersionSequenceList = [];
    
    $(":checkbox[class=processCheckbox]").each(function() {
       if($(this).prop("checked")) {
           processVersionSequenceList.push($(this).data("seq"));
       } 
    });
    
    return processVersionSequenceList;
}

// 프로세스 체크인
function checkInProcess() {
    if (checkSelectedProcess(1)) {
        var processList = getSelectedProcessList();

        var checkable = true;
        
        $.each(processList, function(index, seq) {
            if(processMap[seq].configManagementStatusCode === undefined) {
                checkable = false;
                return false;
            }
        });
        
        var checkoutProcessList = [];
        processList.forEach(function(seq) {
            if (processMap[seq].configManagementStatusCode == "CHKO") {
                checkoutProcessList.push(seq);
            }
        });
        
        if (!checkoutProcessList.length) {
            alert("<spring:message code="msg.m04.0000"/>");
        } else if(checkable) {
            if (confirm("<spring:message code="cfm.m04.0000"/>")) {
                Ajax.post().url("/process/modify/checkIn")
                    .requestBody(JSON.stringify(checkoutProcessList))
                    .success(function(response) {
                                if (response) {
                                    if (response == processList.length) {
                                        alert("<spring:message code="inf.m04.0000"/>");
                                    } else {
                                        alert("<spring:message code="inf.m04.0001"/>");
                                    }
                                    getProcessList();
                                } else {
                                    alert("<spring:message code="err.m04.0000"/>");
                                }
                            })
                    .failure(function() {
                        alert("<spring:message code="err.m04.0001"/>");
                    })
                    .execute();
            }
        } else {
            alert("<spring:message code="msg.m04.0001"/>");
        }
    } else {
        alert("<spring:message code="msg.com.0000"/>");
    }
}

// 프로세스 삭제
function deleteProcess() {
    if (checkSelectedProcess(1)) {
        var processList = getSelectedProcessList();
        var deleteProcessList = [];
        var processSequenceList = []
        
        processList.forEach(function(seq) {
            if(processMap[seq].workAssignmentYn == "N") {
                deleteProcessList.push(seq);
                
                var processSequence = processMap[seq].processSequence;
                var exist = false;
                $.each(processSequenceList, function(index, value) {
                    if(value == processSequence) {
                        exist = true;
                    } 
                });
                
                if(!exist) {
                    processSequenceList.push(processSequence)
                }
            }
        });
        
        if (!deleteProcessList.length || deleteProcessList.length != processList.length) {
            alert("<spring:message code="msg.m04.0002"/>");
        } else if(confirm("<spring:message code="cfm.m04.0003"/>")) {
            var data = {};
            data.processVersionSequenceList = deleteProcessList;
            data.processSequenceList = processSequenceList;
            Ajax.post().url("/process/delete")
            .requestBody(JSON.stringify(data))
            .success(
                function(response) {
                    getProcessList();
                })
            .failure(
                function() {
                    alert("<spring:message code="err.m04.0001"/>");
            })
            .execute();   
        }
    } else {
        alert("<spring:message code="msg.com.0000"/>");
    }

}

// 프로세스 다운로드
function downloadProcess() {
    if (checkSelectedProcess(1)) {
        var data = "";
        var processList = getSelectedProcessList();
        
        if(processList.length > 15) {
            alert("<spring:message code="msg.m04.0003"/>");
        } else {
            processList.forEach(function(seq) {
                data += seq + ","; 
             });
             
             location.href = "${_context}/process/download?sequenceList=" + data.slice(0,-1);    
        }
    } else {
        alert("<spring:message code="msg.com.0000"/>");
    }
}

// 이력보기
function showHistory() {
    if (checkSelectedProcess(1)) {
        var processVersionSequence = getSelectedProcessList()[0];
        openProcessHistoryPopup(getSelectedProcessList()[0], processMap[processVersionSequence].processName);
    } else {
        alert("<spring:message code="msg.com.0000"/>");
    }
}

// 프로세스 상태변경
function changeStatus() {
    if (checkSelectedProcess(1)) {
        var processVersionSequence = getSelectedProcessList()[0];

        if (processMap[processVersionSequence].configManagementStatusCode == "CHKO") {
            alert("<spring:message code="msg.m04.0004"/>");
            return;
        }
        
        if (processMap[processVersionSequence].workAssignmentYn == "Y") {
            alert("<spring:message code="msg.m04.0005"/>")
        } else {
            if (processMap[processVersionSequence].activationYn == "Y") {
                if (confirm("<spring:message code="cfm.m04.0001"/>")) {
                    Ajax.post().url("/process/status/off")
                        .queryString("processVersionSequence=" + processVersionSequence)
                        .success(getProcessList)
                        .failure(function() {
                            alert("<spring:message code="err.m04.0002"/>");
                        }).execute();
                }
            } else {
                if (confirm("<spring:message code="cfm.m04.0002"/>")) {
                    Ajax.post().url("/process/status/on")
                    .queryString("processVersionSequence=" + processVersionSequence)
                    .success(getProcessList)
                    .failure(function() {
                        alert("<spring:message code="err.m04.0002"/>");
                    }).execute();
                }
            }
        }
    } else {
        alert("<spring:message code="msg.com.0000"/>");
    }
}

// 프로세스 목록 가져오기
function getProcessList() {
    processCount = 0;
    activateButton();
    Ajax.post().url("/process/list").requestBodyByForm("searchForm")
               .success(createTable)
               .failure(function() {
                   alert("<spring:message code="err.m04.0003"/>");
               }).execute();
}

// 프로세스 테이블 생성
function createTable(response) {
    $("#processTbody").empty();
    $("input[name='checkboxAll']").prop("checked", false);
    processCount = 0;
    activateButton();
    processMap = {};
    
    var processList = response.processList;
    var lastProcessVersionMap = searchProcessLastVersion(processList);
    
    if (processList.length > 0) {
        $.each(processList, function(index, item) {
            processMap[item.processVersionSequence] = {
                "activationYn": item.activationYn,
                "workAssignmentYn" : item.workAssignmentYn,
                "processName" : item.processName,
                "processSequence" : item.processSequence
            }
            
            var dataSeq = item.processVersionSequence;
            
            var configManagementStatusCodeName = "";
            
            if(lastProcessVersionMap[item.processSequence].processVersionSequence == item.processVersionSequence) {
                if (item.configManagementStatusCode == "CHKO") {
                    configManagementStatusCodeName = "<spring:message code="ctl.m04.0006" />";
                    processMap[item.processVersionSequence].configManagementStatusCode = item.configManagementStatusCode;
                }
            }
            
            var registerDateTime = new Date(item.registerDateTime).format("yyyy-MM-dd HH:mm")

            var htmlStr = '';
            htmlStr += '<tr>';
            htmlStr += '    <td style="width: 60px; min-width: 60px;">';
            htmlStr += '        <input type="checkbox" class="processCheckbox" data-seq=' + item.processVersionSequence + '>';
            htmlStr += '    </td>';
            htmlStr += '    <td style="width: 20%; min-width: 180px">' + item.processName + '</td>';
            htmlStr += '    <td style="width: 6%; min-width: 60px">' + item.majorVersion + "." + item.minorVersion + '</td>';
            htmlStr += '    <td style="width: 15%; min-width: 150px">' + item.registerId + '</td>';
            htmlStr += '    <td style="width: 15%; min-width: 140px">' + configManagementStatusCodeName + '</td>';
            htmlStr += '    <td style="width: 18%; min-width: 170px">' + registerDateTime + '</td>';
            htmlStr += '    <td style="width: 15%; min-width: 140px">' + item.workAssignmentYnName + '</td>'
            htmlStr += '    <td style="width: 8%; min-width: 80px">' + item.activationYnName + '</td>';
            htmlStr += '</tr>';
            
            $("#processTbody").append(htmlStr);
        });
    } else {
        var htmlStr = '<tr colspan="8"><td class="empty-tb" colspan="8">' + "<spring:message code="msg.com.0001"/>" + '</td><td></td></tr>';
        $("#processTbody").append(htmlStr);
    }

    if(response.count) {
        var count = response.count;
        var pageRowCount = $("#searchForm input[name=pageRowCount]").val();
        var pageNo = response.form.pageNo; 
        $("#searchForm input[name=pageNo]").val(pageNo);
        setPaging("pagingProcess", pageNo, count, pageRowCount);
        var pagingText = getPagingText(pageNo, count, pageRowCount);
        $("#totalCount").text("<spring:message code="msg.com.0002" arguments='" + pagingText + "'/>");
        $("#processTfoot").removeClass("empty-tf");
    } else {
        $("#processTfoot").addClass("empty-tf");
    }
       
}

// 페이지 이동
function movePage(pageNo) {
    $("#searchForm input[name=pageNo]").val(Number(pageNo));
    getProcessList();
}

// 검색 폼 초기화
function initForm() {
    $("#processThead #lastChangedDate").find('span').addClass("sort-down");
    getProcessList();
}

function refresh() {
    getProcessList();
}

// 버튼 비활성화
function deactivateButton() {
    $("#btn_processHistory").addClass("btn-disabled").unbind("click");
    $("#btn_statusChange").addClass("btn-disabled").unbind("click");
}

// 버튼 활성화
function activateButton() {
    $("#btn_processHistory").removeClass("btn-disabled").unbind("click").click(showHistory);
    $("#btn_statusChange").removeClass("btn-disabled").unbind("click").click(changeStatus);
}

// 프로세스 최종버전 체크
function searchProcessLastVersion(list) {
    var lastProcessVersionMap = {};

    $.each(list, function(index, item) {
        if(lastProcessVersionMap[item.processSequence] !== undefined) {
            var current = lastProcessVersionMap[item.processSequence];
            
            if(item.majorVersion >= current.majorVersion) {
                if(item.minorVersion > current.minorVersion) {
                    lastProcessVersionMap[item.processSequence] = {
                                    "processVersionSequence" : item.processVersionSequence,
                                    "majorVersion" : item.majorVersion,
                                    "minorVersion" : item.minorVersion
                    }
                }
            }
        } else {
            lastProcessVersionMap[item.processSequence] = {
                            "processVersionSequence" : item.processVersionSequence,
                            "majorVersion" : item.majorVersion,
                            "minorVersion" : item.minorVersion
            }
        }
    });
    
    return lastProcessVersionMap;
}
</script>
</head>
<body>
    <div class="wrapper">
        <%@ include file="/WEB-INF/views/include/frame/top.jsp"%>
        <%@ include file="/WEB-INF/views/include/frame/menu.jsp"%>
        
        <!-- content start -->
        <div class="content">
            <div class="search-wrap">
             <form:form name="searchForm" id="searchForm">
                <input type="hidden" name="pageNo" id="pageNo" value="1" />
                <input type="hidden" name="pageRowCount" value="15" />
                <input type="hidden" name="processName" name="processName" />
                <input type="hidden" name="configManagementStatusCode" id="configManagementStatusCode" />
                <input type="hidden" name="activationYn" id="activationYn" />
                <input type="hidden" name="sortOrder" id="sortOrder" value="desc" />
                <input type="hidden" name="sortKey" id="sortKey" value="lastChangedDate" />

                <div class="row">
                    <div class="input-group">
                        <div class="img-ico img-search"></div>
                        <input type="text" id="searchText" class="filter-input" 
                            placeholder="<spring:message code="btn.m04.0006"/>" />
                        <span id="btn_clear" class="filter-clear" style="display: none;"></span>
                    </div>
                    <div class="img-ico img-filter"></div>
                    <div class="col-xs-4">
                        <div class="select-container">
                            <div class="select-viewport"></div>
                            <select id="selectConfigManagementStatusCode" class="selectpicker">
                                <option value=""><spring:message code="ctl.m04.0000"/></option>
                                <c:forEach var="code" items="${configManagementStatusCodeList }" varStatus="status">
                                    <option value="<c:out value="${code.code }" />">
                                        <c:out value="${code.codeName }" />
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
                        
                        <div class="select-container">
                            <div class="select-viewport"></div>
                            <select id="selectActivationYn" class="selectpicker">
                                <option value=""><spring:message code="ctl.m04.0001"/></option>
                                <option value="Y"><spring:message code="ctl.m04.0002" /></option>
                                <option value="N"><spring:message code="ctl.m04.0003" /></option>
                            </select>
                        </div>
                    </div>
                  
                    <div class="right btns">
                        <sec:authorize access="hasRole('ROLE_PRCS_READ')">
                            <span id="btn_processHistory" class="btn btn-primary btn_img btn_wrkhistory" 
                                title="<spring:message code="btn.m04.0000"/>"></span>
                        </sec:authorize>

                        <span class="btn-blank">|</span> 
                        
                        <sec:authorize access="hasRole('ROLE_PRCS_MOD')">
                            <span id="btn_checkIn" class="btn btn-primary btn_img btn_checkin"
                                title="<spring:message code="btn.m04.0001"/>"></span>
                            <span id="btn_statusChange" class="btn btn-primary btn_img btn_change" 
                                title="<spring:message code="btn.m04.0002"/>"></span>
                        </sec:authorize>
                        
                        <sec:authorize access="hasRole('ROLE_PRCS_READ')">
                            <span id="btn_download" class="btn btn-primary btn_img btn_download" 
                                title="<spring:message code="btn.m04.0003"/>"></span>
                        </sec:authorize>
                        
                        <span class="btn-blank">|</span> 
                        
                        <sec:authorize access="hasRole('ROLE_PRCS_MOD')">
                            <span id="btn_delete" class="btn btn-primary btn_img btn_trash" 
                                title="<spring:message code="btn.m04.0004"/>"></span>
                        </sec:authorize>
                         
                        <span class="btn-blank">|</span> 
                        
                        <sec:authorize access="hasRole('ROLE_PRCS_READ')">
                            <span id="btn_refresh" class="btn btn-primary btn_img btn_refresh" 
                                title="<spring:message code="btn.m04.0005"/>"></span>
                        </sec:authorize>
                    </div>
                </div>
            </form:form>
            </div>

            <table class="fixed_headers">
                <thead id="processThead">
                    <tr>
                        <th style="width: 60px; min-width: 60px">
                            <input type="checkbox" name="checkboxAll" class="allCheck">
                        </th>
                        <th id="processName" style="width: 20%; min-width: 180px"><p><spring:message code="tit.m04.0000"/><span></span></p></th>
                        <th id="processVersion" style="width: 6%; min-width: 60px"><p><spring:message code="tit.m04.0001"/><span></span></p></th>
                        <th id="lastRegister" style="width: 15%; min-width: 150px"><p><spring:message code="tit.m04.0002"/><span></span></p></th>
                        <th id="configManagementStatus" style="width: 15%; min-width: 140px"><p><spring:message code="tit.m04.0003"/><span></span></p></th>
                        <th id="lastChangedDate" style="width: 18%; min-width: 170px"><p><spring:message code="tit.m04.0004"/><span></span></p></th>
                        <th id="assignYn" style="width: 15%; min-width: 140px"><p><spring:message code="tit.m04.0005"/><span></span></p></th>
                        <th id="status" style="width: 8%; min-width: 80px"><p><spring:message code="tit.m04.0006"/><span></span></p></th>
                    </tr>
                </thead>
                <tbody id="processTbody">
                </tbody>
                <tfoot id="processTfoot">
                    <tr>
                        <td>
                            <span id="totalCount"></span>
                        </td>
                        <td id="pagingProcess" colspan="6">
                        </td>
                    </tr>  
                </tfoot>
            </table>
        </div>
    </div>

    <%@ include file="/WEB-INF/views/core/process/processHistoryPopup.jsp"%>
</body>
<%@ include file="/WEB-INF/views/include/frame/bottom.jsp"%>
</html>