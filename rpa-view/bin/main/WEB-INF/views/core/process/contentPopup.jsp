<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!DOCTYPE html>
<style>

</style>
<script type="text/javascript">
$(document).ready(function() {
   $("#btn_closeContentTop, #btn_closeContentBottom").click(function() {
       hideBlockLayer("#contentPopup");
   }); 
});

function openContentPopup(processVersionSequence) {
    getProcessVersion(processVersionSequence);
    showBlockLayer("#contentPopup");
}

function getProcessVersion(processVersionSequence) {
    Ajax.post().url("/process").queryString("processVersionSequence=" + processVersionSequence)
          .success(setProcessInfo).execute();
}

function setProcessInfo(process) {
    var version = process.majorVersion + "." + process.minorVersion;
    var content = (process.remarksContent) ? process.remarksContent : "-";
    $("#changeContent").html(content);
    $("#contentPopupTitle").text("프로세스 변경 내역 | 버전  " + version);
}
</script>

<div id="contentPopup" class="pop-body" style="display: none; width: 580px; height: 570px;">
    <div id="content" class="pop-content" style="width: 580px;">
        <h4 id="contentPopupTitle" class="pop-title" style="min-width: 270px;"></h4>
        <a href="#" id="btn_closeContentTop" class="pop-close"><img src="images/btnclose.png"></a>
        <div class="pop-message clear" style="overflow: auto">
          <p id="changeContent" style="max-height: 400px; overflow: auto; white-space: pre-wrap"></p>
        </div>
        <div class="right btns pop-btns pop-btns-notf clear">
          <span id="btn_closeContentBottom" class="btn btn-primary message-btn"><spring:message code="btn.com.0000"/></span>
        </div>
    </div>
</div>