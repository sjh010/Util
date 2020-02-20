<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<%@ include file="/WEB-INF/views/include/frame/header.jsp"%>
<title>INZI RPA SUITE MANAGER</title>

<script>
var authorityCount = 0;

$(document).on("click", ".authorityCheckbox", function(e) {
var tr = $(this).parent().parent();
    
    var seq = $(this).data("seq");
    
    if ($(this).prop("checked")) {
        authorityCount++;   
        tr.addClass("tr-select");
    } else {
        authorityCount--;
        tr.removeClass("tr-select");
    }

    if(checkSelectedAuthority(2)) {
        deactivateButton();
    } else {
        activateButton();   
    }
});

$(document).ready(function() {
    // 메뉴 활성화
    setMenuTab("authority");

    // 정렬
    $("#authorityThead").on("click", "th", function() {
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
            
            $("#authorityThead").find('th').each(function(index, item) {
                $(this).find('span').removeClass();
            });
            
            var sortClass = $("#searchForm input[name=sortOrder]").val() == "desc" ? "sort-down" : "sort-up";
            
            $(this).find('span').addClass(sortClass);
            
            getAuthorityList();
        }
     }).css("cursor", "pointer");
    
    // 체크박스 전체선택
    $(".allCheck").click(function() {
        if ($(".allCheck").prop("checked")) {
            $("input[class=authorityCheckbox]").each(function() {
                $(this).prop("checked", true);
                $(this).parent().parent().addClass("tr-select");
                authorityCount++;
            });
            deactivateButton();
        } else {
            $("input[class=authorityCheckbox]").each(function() {
                $(this).prop("checked", false);
                $(this).parent().parent().removeClass("tr-select");
                authorityCount = 0;
            });
            activateButton();
        }
    });

    $("#searchForm #searchText").keyup(function(e) {
        var searchText = $("#searchForm input[name=searchText]").val();
        
        $("#btn_clear").show();
    });
    
    // 권한명 검색
    $("#searchForm #searchText").keydown(function(e) {
        if (e.which == 13) {
            e.preventDefault();
            var authorityName = $("#searchForm input[name=searchText]").val();
            $("#searchForm input[name=authorityName]").val(authorityName);
            getAuthorityList();
        }
    });
    
    $("#btn_clear").click(function() {
        $("#searchForm input[name=searchText]").val("");
        $("#searchForm input[name=authorityName]").val(""); 
        $(this).hide();
        
        getAuthorityList(); 
    });

    // 사용여부 필터
    $("#selectUseYn").change(function(e) {
        var code = $(this).val();
        $("#searchForm input[name=useYn]").val(code);
        getAuthorityList();
    });
    
    // 권한 수정(2차 개발)
    $("#btn_modify").click(modifyAuthority);

    initForm();

});

function modifyAuthority() {
    // 2차 개발
}

//선택된 권한 개수 확인
function checkSelectedAuthority(count) {
    if (authorityCount >= count) {
        return true;
    } else {
        return false;
    }
}

// 선택된 프로세스 버전 시퀀스 리스트 반환
function getSelectedAuthorityList() {
    var authoritySequenceList = [];

    $(":checkbox[class=authorityCheckbox]").each(function() {
        if ($(this).prop("checked")) {
            authoritySequenceList.push($(this).data("seq"));
        }
    });

    return authoritySequenceList;
}

// 권한 목록 가져오기
function getAuthorityList() {
    authorityCount = 0;
    Ajax.post().url("/authority/list").requestBodyByForm("searchForm")
        .success(createTable)
        .failure(function() {
            alert("<spring:message code="err.m07.0000"/>");
        }).execute();
}

// 프로세스 테이블 생성
function createTable(response) {
    $("#authorityTbody").empty();
    $("input[name='checkboxAll']").prop("checked", false);
    activateButton();
    authorityCount = 0;

    var authorityList = response.authorityList;
    
    if (authorityList.length > 0) {
        $.each(authorityList, function(index, item) {

            var registerName = (item.registerName) ? item.registerName : "";
            
            var modifyDateTime = new Date(item.modifyDateTime).format("yyyy-MM-dd HH:mm");
            var registerDateTime = new Date(item.registerDateTime).format("yyyy-MM-dd HH:mm");
            
            var useYnName = "";
            if(item.useYn == "Y") {
                useYnName = "<spring:message code="ctl.m07.0001"/>";
            } else if(item.useYn == "N") {
                useYnName = "<spring:message code="ctl.m07.0002"/>"
            }
            
            var htmlStr = '';
            htmlStr += '<tr>';
            htmlStr += '    <td style="width: 60px; min-width: 60px;"><input data-seq=' + item.authoritySequence + ' type="checkbox" class="authorityCheckbox"></td>';
            htmlStr += '    <td style="width: 13%; min-width: 240px" title="' + item.authorityName + '">' + item.authorityName + '</td>';
            htmlStr += '    <td style="width: 13%; min-width: 150px">' + item.registerId + '</td>';
            htmlStr += '    <td style="width: 33%; min-width: 180px">' + modifyDateTime + '</td>';
            htmlStr += '    <td style="width: 33%; min-width: 180px">' + registerDateTime + '</td>';
            htmlStr += '    <td style="width: 13%; min-width: 150px">' + useYnName + '</td>';
            htmlStr += '</tr>';
            $("#authorityTbody").append(htmlStr);
        });
    } else {
        var htmlStr = '<tr><td class="empty-tb" colspan="6">' + "<spring:message code="msg.com.0001"/>" + '</td><td></td></tr>';
        $("#authorityTbody").append(htmlStr);
    }

    if(response.count) {
        var count = response.count;
        var pageRowCount = $("#searchForm input[name=pageRowCount]").val();
        var pageNo = response.form.pageNo;
        $("#searchForm input[name=pageNo]").val(pageNo);
        setPaging("pagingAuthority", pageNo, response.count, pageRowCount);
        var pagingText = getPagingText(pageNo, count, pageRowCount);
        $("#totalCount").text("<spring:message code="msg.com.0002" arguments='" + pagingText + "'/>");
        
        $("#authorityTfoot").removeClass("empty-tf");
    } else {
        $("#authorityTfoot").addClass("empty-tf");
    }
}

function movePage(pageNo) {
    $("#searchForm input[name=pageNo]").val(Number(pageNo));
    getAuthorityList();
}

// 검색 폼 초기화
function initForm() {
    $("#authorityThead #sortModifyDateTime").find('span').addClass("sort-down");
    getAuthorityList();
}

function deactivateButton() {
    $("#btn_modify").addClass("btn-disabled").unbind("click");
}

function activateButton() {
    $("#btn_modify").removeClass("btn-disabled").unbind("click").click(modifyAuthority);
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
            <form:form id="searchForm" name="searchForm">
                <input type="hidden" name="pageNo" id="pageNo" value=1 />
                <input type="hidden" name="pageRowCount" value=15 />
                <input type="hidden" name="authorityName" id="authorityName" />
                <input type="hidden" name="useYn" id="useYn" />
                <input type="hidden" name="sortOrder" id="sortOrder" value="desc" />
                <input type="hidden" name="sortKey" id="sortKey" value="sortModifyDateTime" />
                <div class="row">
                    <div class="input-group">
                        <div class="img-ico img-search"></div>
                        <input type="text" name="searchText" id="searchText" class="filter-input" placeholder="<spring:message code="tit.m07.0005"/>"> 
                        <span id="btn_clear" class="filter-clear" style="display: none;"></span>
                    </div>
                    <div class="img-ico img-filter"></div>
                    <div class="col-xs-4 xs-first">
                        <div class="select-container">
                            <div class="select-viewport"></div>
                            <select id="selectUseYn" class="selectpicker">
                                <option value=""><spring:message code="ctl.m07.0000"/></option>
                                <option value="Y"><spring:message code="ctl.m07.0001"/></option>
                                <option value="N"><spring:message code="ctl.m07.0002"/></option>
                            </select>
                        </div>
                    </div>
                    <div class="right btns">
                        <sec:authorize access="hasRole('ROLE_ATH_MOD')">
                            <!-- 2차 개발 항목 -->
                            <%-- <span id="btn_modify" class="btn btn-primary btn_img btn_refine" 
                                title="<spring:message code="btn.m07.0000"/>"></span>
                            <span id="btn_delete" class="btn btn-primary btn_img btn_trash" 
                                title="<spring:message code="btn.m07.0001"/>"></span>
                            <span class="btn-blank">|</span>
                            <span id="btn_add" class="btn btn-primary btn_img btn_addadmin" 
                                title="<spring:message code="btn.m07.0002"/>"></span> --%>
                        </sec:authorize>
                    </div>
                </div>
                </form:form>
            </div>

            <table class="fixed_headers">
                <thead id="authorityThead">
                    <tr>
                        <th style="width: 60px; min-width: 60px;">
                            <input type="checkbox" name="checkboxAll" class="allCheck">
                        </th>
                        <th id="sortAuthorityName" style="width: 13%; min-width: 240px"><p><spring:message code="tit.m07.0000"/><span></span></p></th>
                        <th id="sortRegisterId" style="width: 13%; min-width: 150px"><p><spring:message code="tit.m07.0001"/><span></span></p></th>
                        <th id="sortRegisterDateTime" style="width: 33%; min-width: 180px"><p><spring:message code="tit.m07.0002"/><span></span></p></th>
                        <th id="sortModifyDateTime" style="width: 33%; min-width: 180px"><p><spring:message code="tit.m07.0003"/><span></span></p></th>
                        <th id="sortUseYn" style="width: 13%; min-width: 150px"><p><spring:message code="tit.m07.0004"/><span></span></p></th>
                    </tr>
                </thead>
                <tbody id="authorityTbody">
                </tbody>
                <tfoot id="authorityTfoot">
                    <tr>
                        <td>
                            <span id="totalCount"></span>
                        </td>
                        <td id="pagingAuthority" colspan="6"></td>
                    </tr>
                </tfoot>
            </table>
        </div>
    </div>

    <%@ include file="/WEB-INF/views/include/frame/bottom.jsp"%>
</body>
</html>