<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<script type="text/javascript">
$(document).ready(function() {
    $(".btnClosePopup").click(function() {
        hideBlockLayer("#robotAddPopup");
    });
    $("#btnAddRobot").click(function() {
        addRobotProcess();
    });
});

function openRobotAddPopup() {
    $(".reset").each(function() {
        this.value = "";
    });
    showBlockLayer("#robotAddPopup");
}

function addRobotProcess() {
    // check validation 
    if ($.trim($("#robotAddForm input[name='robotName']").val()) == "") {
        alert("<spring:message code="NotEmpty.robotName" />");
        $("#robotAddForm input[name='robotName']").focus();
        return false;
    }
    
    if ($.trim($("#robotAddForm input[name='pcIpAddress']").val()) == "") {
        alert("<spring:message code="NotEmpty.pcIpAddress" />");
        $("#robotAddForm input[name='pcIpAddress']").focus();
        return false;
    }
    
    if ($.trim($("#robotAddForm input[name='pcName']").val()) == "") {
        alert("<spring:message code="NotEmpty.pcName" />");
        $("#robotAddForm input[name='pcName']").focus();
        return false;
    }

    if(!(validateName($("#robotAddForm input[name=robotName]").val(), 20))) {
        alert("<spring:message code="Valid.robotName" />");
        $("#robotAddForm input[name=robotName]").focus();
        return false;
    }
    
    if(!validateIpAddress($("#robotAddForm input[name=pcIpAddress]").val())) {
        alert("<spring:message code="Valid.pcIpAddress" />");
        $("#robotAddForm input[name=pcIpAddress]").focus();
        return false;
    }
    
    if(!(validateName($("#robotAddForm input[name=pcName]").val(), 20))) {
        alert("<spring:message code="Valid.pcName" />");
        $("#robotAddForm input[name=pcName]").focus();
        return false;
    }

    var exist = false;
    Ajax.post().sync().url("/robot/add/check").requestBodyByForm("robotAddForm").success(function(response) {
        if(response.robotExistYn) {
           exist = true; 
           alert("<spring:message code="Duplication.robotName" />");
        } else if(response.pcExistYn) {
            exist = true;
            alert("<spring:message code="Duplication.pc" />");
        }
    }).execute();
    
    // request
    if(!exist) {
        Ajax.post().url("/robot/add/process").requestBodyByForm("robotAddForm").success(function(response) {
            if (response && response == 1) {
                getPcNameList();
                getRobotList();
                hideBlockLayer("#robotAddPopup");
            }
        }).failure(function(response) {
            alert("<spring:message code="err.m03.0004"/>");
        }).execute();    
    }
}

function onlyNumber(event) {
    event = event || window.event;
    
    var keyId = (event.which) ? event.which : event.keyCode;
    
    if((keyId >= 48 && keyId <=57) || (keyId >= 96 && keyId <= 105) 
                    || keyId == 8 || keyId == 46 || keyId == 37 || keyId == 39 
                    || keyId == 9 || keyId == 190 || keyId == 110) {
        return;
    } else {
        return false;
    }
}

function removeChar(obj, event) {
    event = event || window.event;
    
    var keyId = (event.which) ? event.which : event.keyCode;
    
    if(keyId == 8 || keyId == 46 || keyId == 37 || keyId == 39 || keyId == 190 || keyId == 110) {
        return;
    } else {
        event.target.value = event.target.value.replace(/[^0-9|'.']/g, "");
    }
}
</script>
<div id="robotAddPopup" class="pop-body" style="display:none; width: 490px; height: 270px;">
    <div class="pop-content">
        <h4 class="pop-title"><spring:message code="tit.m03.0013"/></h4>
        <a href="#" class="pop-close btnClosePopup"><img src="<c:url value="/images/btnclose.png" />"></a>
        <form:form id="robotAddForm" commandName="robotAddForm">
        <table class="pop-fixed_headers lowercode_tb robot">
            <tr>
                <th style="width: 130px"><spring:message code="tit.m03.0000"/></th>
                <td style="width: 320px">
                    <input type="text" name="robotName" class="pop-table-edit-input-robot reset" maxlength="20" 
                        placeholder="<spring:message code="tit.m03.0011"/>">
                </td>
            </tr>
            <tr>
                <th style="width: 130px"><spring:message code="tit.m03.0001"/></th>
                <td style="width: 320px">
                    <input type="text" name="pcIpAddress" class="pop-table-edit-input-robot reset" maxlength="15" 
                        onkeydown="return onlyNumber(event)" onkeyup="removeChar(event)" placeholder="<spring:message code="tit.m03.0012"/>">
                </td>
            </tr>
            <tr>
                <th style="width: 130px"><spring:message code="tit.m03.0002"/></th>
                <td style="width: 320px">
                    <input type="text" name="pcName" class="pop-table-edit-input-robot reset"  maxlength="20" 
                        placeholder="<spring:message code="tit.m03.0011"/>">
                </td>
            </tr>
        </table>
        </form:form>
        <div class="right btns pop-btns clear pop-btns-notf">
            <span id="btnAddRobot" class="btn btn-primary"><spring:message code="btn.com.0000"/></span> 
            <span class="btn btn-primary btnClosePopup"><spring:message code="btn.com.0001"/></span>
        </div>
    </div>
</div>