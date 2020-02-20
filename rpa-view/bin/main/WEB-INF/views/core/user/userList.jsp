<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<%@ include file="/WEB-INF/views/include/frame/header.jsp"%>
<title>INZI RPA SUITE MANAGER</title>
<script>
var userCount = 0;

// 체크박스 이벤트
$(document).on("click", ".userCheckbox", function(e) {
    var tr = $(this).parent().parent();
    
    var seq = $(this).data("seq");
    
    if ($(this).prop("checked")) {
        userCount++;   
        tr.addClass("tr-select");
    } else {
        userCount--;
        tr.removeClass("tr-select");
    }

    if(checkSelectedUser(2)) {
        deactivateButton();
    } else {
        activateButton();   
    }
});

$(document).ready(function() {
    // 메뉴 활성화
    setMenuTab("user");

    // 정렬
    $("#userThead").on("click", "th", function() {
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
            
            $("#userThead").find('th').each(function(index, item) {
                $(this).find('span').removeClass();
            });
            
            var sortClass = $("#searchForm input[name=sortOrder]").val() == "desc" ? "sort-down" : "sort-up";
            
            $(this).find('span').addClass(sortClass);
            
            getUserList();
        }
     }).css("cursor", "pointer");
    
    // 체크박스 전체선택
    $(".allCheck").click(function() {
        if ($(".allCheck").prop("checked")) {
            $("input[class=userCheckbox]").each(function() {
                $(this).prop("checked", true);
                $(this).parent().parent().addClass("tr-select");
                userCount++;
            });
            deactivateButton();
        } else {
            $("input[class=userCheckbox]").each(function() {
                $(this).prop("checked", false);
                $(this).parent().parent().removeClass("tr-select");
                userCount = 0;
            });
            activateButton();
        }
    });

    // 검색 X버튼
    $("#searchForm #searchText").keyup(function(e) {
        var searchText = $("#searchForm input[name=searchText]").val();
		
        $("#btn_clear").show();
    });
    
    // 사용자 ID, 이름 검색
    $("#searchForm #searchText").keydown(function(e) {
        if (e.which == 13) {
            e.preventDefault();
            var userInfo = $("#searchForm input[name=searchText]").val();
            $("#searchForm input[name=userInfo]").val(userInfo);
            getUserList();
        }
    });
    
    // 검색 X버튼 클릭
    $("#btn_clear").click(function() {
        $("#searchForm input[name=searchText]").val("");
        $("#searchForm input[name=userInfo]").val(""); 
        $(this).hide();
        
        getUserList();
    });

    // 소속 필터
    $("#selectDepartment").change(function(e) {
        var code = $(this).val();
        $("#searchForm input[name=departmentName]").val(code);
        getUserList();
    });

    // 권한 필터
    $("#selectAuthority").change(function(e) {
        var code = $(this).val();
        $("#authoritySequence").val(code);
        getUserList();
    });
    
    // 사용여부 필터
    $("#selectUseYn").change(function(e) {
        var code = $(this).val();
        $("#searchForm input[name=useYn]").val(code);
        getUserList();
    });

    // 사용자 추가
    $("#btn_add").click(addUser);
    
    // 비밀번호 초기화(2차 개발)
    $("#btn_initPassword").click(initPassword);

    // 사용자 수정(2차 개발)
    $("#btn_modify").click(modifyUser);
    
    initForm();

});

// 비밀번호 초기화
function initPassword() {
    // 2차 개발
}

// 사용자 수정
function modifyUser() {
    // 2차 개발
}

//선택된 사용자 개수 확인
function checkSelectedUser(count) {
    if (userCount >= count) {
        return true;
    } else {
        return false;
    }
}

// 선택된 사용자 순번 리스트 반환
function getSelectedUserList() {
    var userSequenceList = [];

    $(":checkbox[class=userCheckbox]").each(function() {
        if ($(this).prop("checked")) {
            userSequenceList.push($(this).data("seq"));
        }
    });

    return userSequenceList;
}

// 사용자 추가
function addUser() {
    openUserAddPopup();
}

// 필터 가져오기 
function getUserFilter() {
    Ajax.post().sync().url("/user/list/filter")
        .success(createUserFilter)
        .failure(function() {
            alert("<spring:message code="err.m06.0001"/>")
        }).execute();
}

// 사용자 필터 생성
function createUserFilter(filter) {
    var departmentNameFilter = filter.departmentNameList;
    var authorityFilter = filter.authorityList;
    
    var selectedDepartmentName = $("#selectDepartment").val();
    var selectedAuthority = $("#selectAuthority").val();
    var exist = false;
    
    $("#selectDepartment").html('<option value="">' + '<spring:message code="ctl.m06.0000"/>' + '</option>');
    $.each(departmentNameFilter, function(index, name) {
        if(name == null) {
            $("#selectDepartment").append('<option value="none">' + '<spring:message code="ctl.m06.0003"/>' + '</option>');
        } else {
            $("#selectDepartment").append('<option value="' + name + '">' + name + '</option>');
        }
        
        if(selectedDepartmentName == name || selectedDepartmentName == "none") {
            exist = true;
            $("#selectDepartment").val(selectedDepartmentName);
        }
    });
    
    if(!exist) {
        $("#selectDepartment").val("");
    }
    
    exist = false;
    
    $("#selectAuthority").html('<option value=-1>' + '<spring:message code="ctl.m06.0001"/>' + '</option>');
    $.each(authorityFilter, function(index, authority) {
       $("#selectAuthority").append('<option value="' + authority.authoritySequence + '">' + authority.authorityName + '</option>');
       
       if(selectedAuthority == authority.authoritySequence) {
           exist = true;
           $("#selectAuthority").val(selectedAuthority);
       }
    });
    
    if(!exist) {
        $("#selectedAuthority").val(-1);
    }
}

// 사용자 목록 가져오기
function getUserList() {
    userCount = 0;
    Ajax.post().url("/user/list").requestBodyByForm("searchForm")
               .success(createTable)
               .failure(function() {
                   alert("<spring:message code="err.m06.0000"/>");
               }).execute();
}

// 사용자 테이블 생성
function createTable(response) {
    $("#userTbody").empty();
    $("input[name='checkboxAll']").prop("checked", false);
    activateButton();
    userCount = 0;
    
    var userList = response.userList;
    userMap = {};

    if (userList.length > 0) {
        $.each(userList, function(index, item) {
            var useYnName = (item.useYn == "Y") ? "<spring:message code="ctl.m06.0004"/>" : (item.useYn == "N") ? "<spring:message code="ctl.m06.0005"/>" : "";
            var departmentName = !(item.departmentName === undefined) ? item.departmentName : "";

            var htmlStr = '';
            htmlStr += '<tr>';
            htmlStr += '    <td style="width: 60px; min-width: 60px;"><input data-seq=' + item.userSequence + ' type="checkbox" class="userCheckbox"></td>';
            htmlStr += '    <td style="width: 18%; min-width: 120px;">' + item.userId + '</td>';
            htmlStr += '    <td style="width: 18%; min-width: 120px;">' + item.userName + '</td>';
            htmlStr += '    <td style="width: 18%; min-width: 160px;">' + departmentName + '</td>';
            htmlStr += '    <td style="width: 18%; min-width: 220px;">'+ item.authorityName + '</td>';
            htmlStr += '    <td style="width: 18%; min-width: 180px;">' + new Date(item.registerDateTime).format("yyyy-MM-dd HH:mm") + '</td>';
            htmlStr += '    <td style="width: 18%; min-width: 100px;">' + useYnName + '</td>';
            htmlStr += '</tr>';
            $("#userTbody").append(htmlStr);
        });
    } else {
        var htmlStr = '<tr><td class="empty-tb" colspan="7">' + "<spring:message code="msg.com.0001"/>" + '</td><td></td></tr>';
        $("#userTbody").append(htmlStr);
    }

    if(response.count) {
        var count = response.count;
        var pageRowCount = $("#searchForm input[name=pageRowCount]").val();
        var pageNo = response.form.pageNo;
        $("#searchForm input[name=pageNo]").val(pageNo);
        setPaging("pagingUser", pageNo, count, pageRowCount);
        var pagingText = getPagingText(pageNo, count, pageRowCount);
        $("#totalCount").text("<spring:message code="msg.com.0002" arguments='" + pagingText + "'/>");
        $("#userTfoot").removeClass("empty-tf");
    } else {
        $("#userTfoot").addClass("empty-tf");
    }
}

// 페이지 이동
function movePage(pageNo) {
    $("#searchForm input[name=pageNo]").val(Number(pageNo));
    getUserList();
}

// 검색 폼 초기화
function initForm() {
    $("#userThead #sortRegisterDateTime").find('span').addClass("sort-down");
    getUserList();
    getUserFilter();
}

// 버튼 비활성화
function deactivateButton() {
    $("#btn_initPassword").addClass("btn-disabled").unbind("click");
    $("#btn_modify").addClass("btn-disabled").unbind("click");
}

// 버튼 활성화
function activateButton() {
    $("#btn_initPassword").removeClass("btn-disabled").unbind("click").click(initPassword);
    $("#btn_modify").removeClass("btn-disabled").unbind("click").click(modifyUser);    
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
                <input type="hidden" name="userInfo" id="userInfo" />
                <input type="hidden" name="departmentName" id="departmentName" />
                <input type="hidden" name="authoritySequence" id="authoritySequence" value=-1 />
                <input type="hidden" name="useYn" id="useYn" />
                <input type="hidden" name="sortOrder" id="sortOrder" value="desc" />
                <input type="hidden" name="sortKey" id="sortKey" value="sortRegisterDateTime" />
                <div class="row">
                    <div class="input-group input-group-code-o">
                        <div class="img-ico img-search"></div>
                        <input name="searchText" id="searchText" type="text" class="filter-input filter-input-code-o" 
                            placeholder="<spring:message code="btn.m06.0004"/>">
                        <span id="btn_clear" class="filter-clear" style="display: none;"></span>
                    </div>
                    <div class="img-ico img-filter"></div>
                    <div class="col-xs-5 xs-5-diff-1">
                        <div class="select-container">
                            <div class="select-viewport"></div>
                            <select id="selectDepartment" class="selectpicker">
                                <option value=""><spring:message code="ctl.m06.0000"/></option>
                            </select>
                        </div>
                        
                        <div class="select-container">
                            <div class="select-viewport"></div>
                            <select id="selectAuthority" class="selectpicker">
                                <option value=-1><spring:message code="ctl.m06.0001"/></option>
                            </select>
                        </div>
                        
                        <div class="select-container">
                            <div class="select-viewport"></div>
                            <select id="selectUseYn" class="selectpicker">
                                <option value=""><spring:message code="ctl.m06.0002"/></option>
                                <option value="Y"><spring:message code="ctl.m06.0004"/></option>
                                <option value="N"><spring:message code="ctl.m06.0005"/></option>
                            </select>
                        </div>
                    </div>

                    <div class="right btns">
                        <sec:authorize access="hasRole('ROLE_USR_MOD')">
                            <!-- 2차 개발 항목 -->
                            <%-- <span id="btn_initPassword" class="btn btn-primary btn_img btn_pswrdreset" 
                                title="<spring:message code="btn.m06.0000"/>"></span>
                            <span id="btn_modify" class="btn btn-primary btn_img btn_refine" 
                                title="<spring:message code="btn.m06.0001"/>"></span>
                            <span id="btn_delete" class="btn btn-primary btn_img btn_trash" 
                                title="<spring:message code="btn.m06.0002"/>"></span> --%>
                        </sec:authorize>
                        <sec:authorize access="hasRole('ROLE_USR_MOD') and hasRole('ROLE_ATH_READ')">
                            <span class="btn-blank">|</span>
                            <span id="btn_add" class="btn btn-primary btn_img btn_adduser" 
                                title="<spring:message code="btn.m06.0003"/>"></span>
                        </sec:authorize>
                    </div>
                </div>
                </form:form>
            </div>

            <table class="fixed_headers">
                <thead id="userThead">
                    <tr>
                        <th style="width: 60px; min-width: 60px">
                            <input type="checkbox" name="checkboxAll" class="allCheck">
                        </th>
                        <th id="sortUserId" style="width: 18%; min-width: 120px;"><p><spring:message code="tit.m06.0000"/><span></span></p></th>
                        <th id="sortUserName" style="width: 18%; min-width: 120px;"><p><spring:message code="tit.m06.0001"/><span></span></p></th>
                        <th id="sortDepartmentName" style="width: 18%; min-width: 160px;"><p><spring:message code="tit.m06.0002"/><span></span></p></th>
                        <th id="sortAuthority" style="width: 18%; min-width: 220px;"><p><spring:message code="tit.m06.0003"/><span></span></p></th>
                        <th id="sortRegisterDateTime" style="width: 18%; min-width: 180px;"><p><spring:message code="tit.m06.0004"/><span></span></p></th>
                        <th id="sortUseYn" style="width: 18%; min-width: 100px;"><p><spring:message code="tit.m06.0005"/><span></span></p></th>
                    </tr>
                </thead>
                <tbody id="userTbody">
                </tbody>
                <tfoot id="userTfoot">
                    <tr>
                        <td>
                            <span id="totalCount"></span>
                        </td>
                        <td id="pagingUser" colspan="6">
                        </td>
                    </tr>
                </tfoot>
            </table>
        </div>
    </div>

    <%@ include file="/WEB-INF/views/core/user/userAddPopup.jsp"%>
    <%@ include file="/WEB-INF/views/include/frame/bottom.jsp"%>
</body>
</html>