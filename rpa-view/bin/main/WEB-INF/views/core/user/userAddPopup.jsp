<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!DOCTYPE html>

<script type="text/javascript">
$(document).ready(function() {

    $("#btn_addCloseTop, #btn_addCloseBottom").click(function() {
        hideBlockLayer("#userAddPopup");
    });
    
    $("#btn_addSave").click(validCheckUser);
    
    $("#selectAuthorityAdd").change(function() {
        var code = $(this).val();
        $("#userAddForm input[name=authoritySequence]").val(code);
    });
    
    $("#selectDepartmentAdd").change(function() {
       var code = $(this).val();
       
       if(code == "") {
           $("#userAddForm input[name=departmentName]").val("");
           $("#userAddForm input[name=departmentName]").prop("readonly", false);
       } else {
           $("#userAddForm input[name=departmentName]").val(code);
           $("#userAddForm input[name=departmentName]").prop("readonly", true);
       }
    });
    
    $("#selectUseYnAdd").change(function() {
       var code = $(this).val();
       $("#userAddForm input[name=useYn]").val(code);
    });
    
});

function openUserAddPopup() {
    initUserAddPopup();
    getDepartmentList();
    getAuthorityList();
    $("#userAddForm input[name=departmentName]").prop("readonly", false);
    showBlockLayer("#userAddPopup");
}

function validCheckUser() {
    
    if ($.trim($("#userAddForm input[name=userId]").val()) == "") {
        alert("<spring:message code="NotEmpty.userId" />");
        $("#userAddForm input[name=userId]").focus();
        return false;
    }
    
    if ($.trim($("#userAddForm input[name=userName]").val()) == "") {
        alert("<spring:message code="NotEmpty.userName" />");
        $("#userAddForm input[name=userName]").focus();
        return false;
    }
    
    if ($.trim($("#userAddForm input[name=departmentName]").val()) == "") {
        alert("<spring:message code="NotEmpty.departmentName" />");
        $("#userAddForm input[name=departmentName]").focus();
        return false;
    }

    
    if(!validateId($("#userAddForm input[name=userId]").val())) {
        alert("<spring:message code="Valid.userId" />");
        $("#userAddForm input[name=userId]").focus();
        return false;
    }
    
    if(!validateName($("#userAddForm input[name=userName]").val(), 20)) {
        alert("<spring:message code="Valid.userName" />");
        $("#userAddForm input[name=userName]").focus();
        return false;
    }
    
    if(!validateName($("#userAddForm input[name=departmentName]").val(), 20)) {
        alert("<spring:message code="Valid.departmentName" />");
        if($("#selectDepartmentAdd").val() == "") {
            $("#userAddForm input[name=departmentName]").focus();
        } else {
            $("#selectDepartmentAdd").focus();
        }
        return false;
    }
    
    var exist = false;
    
    Ajax.post().url("/user/checkId").sync().queryString("userId=" + $("#userAddForm input[name=userId]").val()).success(function(response) {
        if(response) {
            exist = true;
        }
    }).execute();
    
    if(exist) {
        alert("<spring:message code="Duplication.userId" />");
    } else {
        Ajax.post().url("/user/add").requestBodyByForm("userAddForm").success(function(response) {
            if(response) {
                hideBlockLayer("#userAddPopup");
                getUserFilter();
                getUserList();
            }
        }).execute();
    }
}

function getDepartmentList() {
    Ajax.post().url("/user/department/list").success(createDepartmentSelectBox).execute();
}

function createDepartmentSelectBox(response) {
    var selectBox = $("#selectDepartmentAdd");
    selectBox.empty();
    selectBox.append('<option value="">' + "<spring:message code="ctl.m06.0006"/>" + '</option>');

    if(response.length) {
        $.each(response, function(index, item) {
            if(item == null) {
                return true;
            }       
            selectBox.append('<option value="' + item +'">' + item + '</option>'); 
        });
    }
    
}

function getAuthorityList() {
    var data = {};
    Ajax.post().url("/authority/list").requestBody(JSON.stringify(data)).success(createAuthoritySelectBox).execute();
}

function createAuthoritySelectBox(response) {
    var selectBox = $("#selectAuthorityAdd");
    selectBox.empty();
    
    var authList = response.authorityList;
 
    if(authList.length) {
        $.each(authList, function(index, item) {
            if(index == 0) {
                selectBox.append('<option selected value="' + item.authoritySequence + '">' + item.authorityName + '</option>');
                $("#userAddForm input[name=authoritySequence]").val(item.authoritySequence);
            } else {
                selectBox.append('<option value="' + item.authoritySequence + '">' + item.authorityName + '</option>');    
            } 
        });
    }
    selectBox.siblings(".select-viewport").text(authList[0].authorityName);
}

function initUserAddPopup() {
    $(".valid").each(function() {
       $(this).val(""); 
    });
    
    $("#selectUseYnAdd").val('Y').attr("selected", "selected");
    $("#userAddPopup input[name=useYn]").val("Y");
}
</script>

<div id="userAddPopup" class="pop-body" style="display: none; width: 490px; height: 390px;">
    <div class="pop-content">
        <form:form name="userAddForm" id="userAddForm">
        <h4 class="pop-title">사용자 정보 추가</h4>
        <a href="#" id="btn_addCloseTop" class="pop-close"><img src="images/btnclose.png"></a>
        
        <table class="pop-fixed_headers lowercode_tb robot pop-refine2">
            <tr>
                <th style="width: 110px"><spring:message code="tit.m06.0000"/></th>
                <td style="width: 320px">
                    <input type="text" name="userId" id="userId" class="pop-table-edit-input-robot valid" 
                        maxlength="15" placeholder="<spring:message code="tit.m06.0007"/>">
                </td>
            </tr>
            <tr>
                <th style="width: 110px"><spring:message code="tit.m06.0001"/></th>
                <td style="width: 320px">
                    <input type="text" name="userName" id="userName" class="pop-table-edit-input-robot valid" 
                       maxlength="20" placeholder="<spring:message code="tit.m06.0008"/>">
                </td>
            </tr>
            <tr>
                <th style="width: 110px"><spring:message code="tit.m06.0002"/></th>
                <td style="width: 320px">
                    <input type="text" name="departmentName" id="departmentName" 
                        class="pop-table-edit-input-robot useradd-input valid" maxlength="20" placeholder="">
                    <div class="col-xs-3 xs-first pop-tb-col useradd-select">
                        <div class="select-container selectpicker-in-t">
                            <div class="select-viewport"></div>
                            <select id="selectDepartmentAdd" class="selectpicker pop-tb-selectpicker">
                                <option value=""><spring:message code="ctl.m06.0006"/></option>
                            </select>
                        </div>
                    </div>
           
                </td>
            </tr>

            <tr>
                <th style="width: 110px"><spring:message code="tit.m06.0003"/></th>
                <td style="width: 320px">
                    <div class="col-xs-3 xs-first pop-tb-col useradd-select">
                        <input type="hidden" name="authoritySequence" class="valid" id="authoritySequence" />
                        <div class="select-container">
                            <div class="select-viewport"></div>
                            <select id="selectAuthorityAdd" class="selectpicker pop-tb-selectpicker">
                            </select>
                        </div>
                    </div>
                </td>
            </tr>
            <tr>
                <th style="width: 110px"><spring:message code="tit.m06.0005"/></th>
                <td style="width: 320px">
                    <input type="hidden" name="useYn" class="valid" id="useYn" />
                    <div class="col-xs-3 xs-first pop-tb-col useradd-select">
                        <div class="select-container">
                            <div class="select-viewport"></div>
                            <select id="selectUseYnAdd" class="selectpicker pop-tb-selectpicker">
                                <option value="Y"><spring:message code="ctl.m06.0004"/></option>
                                <option value="N"><spring:message code="ctl.m06.0005"/></option>
                            </select>
                        </div>
                    </div>
                </td>
            </tr>
            
        </table>
        <div style="clear: both;"></div>
        <span class="pop-alarm left"><spring:message code="msg.m06.0005"/></span>
        <div class="right btns pop-btns clear pop-btns-notf">
            <sec:authorize access="hasRole('ROLE_USR_MOD')">
                <span id="btn_addSave" class="btn btn-primary"><spring:message code="btn.com.0000"/></span>
            </sec:authorize>
            <span id="btn_addCloseBottom" class="btn btn-primary"><spring:message code="btn.com.0001"/></span>
        </div>
        </form:form>
    </div>
</div>