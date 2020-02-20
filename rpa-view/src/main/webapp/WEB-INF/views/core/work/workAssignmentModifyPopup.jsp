<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<c:set var="_context" value="${(pageContext.request.contextPath eq '/')? '': pageContext.request.contextPath}" />
<script type="text/javascript">
var GLOBAL = GLOBAL === undefined ? {} : GLOBAL;
GLOBAL.workModifyData = {}; // 응답.
GLOBAL.workModifyData.work = {}; // 업무.
GLOBAL.workModifyData.processNameVersion = {}; // 프로세스/버전 정보.
GLOBAL.workModifyData.workAssignedRobotList = []; // 대상로봇(수정이전).

$(document).ready(function() {
    
    update_time_select();
    
    Ajax.post().url("/work/modify/data/" + "<c:out value="${workSequence}"/>").success(function(response){
        if(response !== undefined){
            GLOBAL.workModifyData = response;
            
            set_process_version_span();
            //update_process_select();
            update_process_version_select();
            update_robot_select();
            update_work_options();
        }
    }).failure(function(response){
        alert("<spring:message code="err.m05.0009"/>");
        closePopup();
        refresh();
    }).execute();
    
    // 대상 로봇 select change event handler.
    $("#tbody_robot_list").on("change", function(e){
        var target = e.target;
        if($(target).is("select") && $(target).hasClass("pop-tb-selectpicker")){
            update_all_robot_select_options();
        }
    });

    // 실행주기 단위코드
    $("#select_rbt_ccl_unt").change(select_rbt_ccl_unt_changed);

    // 닫기 버튼.
    $(".btnClosePopup").click(function() {
        closePopup();
        refresh();
    });

    // 추가/삭제 버튼.
    $("#tbody_robot_list").on("click", function(e){
        if($(e.target).hasClass("btn-primary") 
            || $(e.target).hasClass("robot-plus")
            || $(e.target).hasClass("robot-minus")){
            if($(e.target).hasClass("robot-plus") || $(e.target).find('.robot-plus').length > 0){
                if(has_remains_robot_list() == false){
                    alert("추가 가능한 로봇이 없습니다.");
                    return;
                }
                append_robot_select();
                
                var y = $("#div_robot_table").scrollTop();
                y += $("#div_robot_table tr:last").height();
                $("#div_robot_table").scrollTop(y);
            }else if($(e.target).hasClass("robot-minus") || $(e.target).find('.robot-minus').length > 0){
                $(e.target).closest("tr").remove();
                
                update_all_robot_select_options();
                update_add_minus_robot_btn();
            }
        }
    });
    
    $("#btnAddOk").on('click', function(){
        if(validation_check() == false){
            return;
        }

        var body = build_request_body_modify_work();
        Ajax.post().sync()
            .url("/work/modify")
            .requestBody(JSON.stringify(body))
            .success(function(response){
                if(response.result == true){
                    
                }else{ // result is false
                    switch(response.errorCode){
                        case 'SUCCESS':{
                            closePopup();
                            refresh(); // 목록 업데이트.    
                        }
                        break;
                        case 'ROBOT_IS_WORKING':{
                            alert("<spring:message code='err.m05.0000' />");
                        }
                        break;
                        case 'ROBOT_IS_ASSIGNED':{
                            var robotName = response.otherWorkAssignedRobotNameList.join(", ");
                            var msg = "<spring:message code='err.m05.0001' />";
                            msg = msg.replace('로봇명', robotName);
                            alert(msg);
                        }
                        break;
                        default:{
                            
                        }
                        break;
                    }
                }
                
            })
            .failure(function(){
                alert("<spring:message code="err.m05.0010"/>");
            })
            .execute();
        
    });
    
    
});
function select_rbt_ccl_unt_changed(){
    
    var v = $("#select_rbt_ccl_unt option:selected").val();
    
    switch(v){
        case "MINUTE":{
            $("#div_ccl_unt_minute").css("display", "block");
            $("#div_ccl_unt_time").css("display", "none");
            $("#div_ccl_unt_day").css("display", "none");
            $("#th_ccl_unit").attr("rowspan", 1);
            $("#tr_ccl_unit_week").css("display", "none");
        }break;
        case "TIME":{
            $("#div_ccl_unt_minute").css("display", "none");
            $("#div_ccl_unt_time").css("display", "block");
            $("#div_ccl_unt_day").css("display", "none");
            $("#th_ccl_unit").attr("rowspan", 1);
            $("#tr_ccl_unit_week").css("display", "none");
        }
        break;
        case "DAY":{
            $("#div_ccl_unt_minute").css("display", "none");
            $("#div_ccl_unt_time").css("display", "none");
            $("#div_ccl_unt_day").css("display", "block");
            $("#th_ccl_unit").attr("rowspan", 1);
            $("#tr_ccl_unit_week").css("display", "none");

            update_time_select();
        }
        break;
        case "WEEK":{
            $("#div_ccl_unt_minute").css("display", "none");
            $("#div_ccl_unt_time").css("display", "none");
            $("#div_ccl_unt_day").css("display", "block");
            $("#th_ccl_unit").attr("rowspan", 2);
            $("#tr_ccl_unit_week").css("display", "table-row");
            update_time_select();
        }
        break;
        default:{
            alert("<spring:message code="msg.m05.0002"/>");
        }
    }
}

function update_time_select(defaultValue){
    $("#div_ccl_unt_day select").empty();
    $("#div_ccl_unt_day .btn_minus_robot").empty();
    var h = 0;
    var m = 0;
    while(true){
        var v = '';
        v += (h < 10 ? '0'+ h : h);
        v += '' + (m < 10 ? '0'+ m : m);
        
        var t = getDisplayTime(h, m);
        
        var option = $("<option>").val(v).text(t);
        if(v == defaultValue){
            $(option).attr("selected", true);
        }
        $("#div_ccl_unt_day select").append(option);

        m += 10;
        if (m >= 60){
            m = 0;
            h += 1;
        }

        if(h >= 24) {
            break;
        }
    }
}

// init robot select options.
function update_robot_select(default_robotList){
    default_robotList = default_robotList || GLOBAL.workModifyData.workAssignedRobotList || [];
    $("#tbody_robot_list tr").remove();
    
    if(default_robotList !== undefined && default_robotList != null){
        default_robotList.forEach(function(assigned_robot){
            append_robot_select(assigned_robot);
        });
        if(has_remains_robot_list() == true){
            append_robot_select();
        }
        update_all_robot_select_options();
    }else{
        append_robot_select();
    }
}

function set_process_version_span(){
    var pnv = GLOBAL.workModifyData.processNameVersion;
     $("#processName").text(pnv.processName);
     var processVersion = pnv.majorVersion + '.' + pnv.minorVersion;
     $("#processVersion").text(processVersion);
};

// init process select 
function update_process_select(default_processSequence){
    default_processSequence = default_processSequence || GLOBAL.workModifyData.processSequence;
    
    $("#select_process").empty();
    var processSequenceList = [];
    GLOBAL.workModifyData.processVersionList.forEach(function(processVersion){
        var exists = $.grep(processSequenceList, function(p){
            return p == processVersion.processSequence;
        });
        if(exists.length == 0){
            processSequenceList.push(processVersion.processSequence);
        }
    });
    processSequenceList.forEach(function(processSequence){
        var processVersionList = $.grep(GLOBAL.workModifyData.processVersionList, function(pv){
            return pv.processSequence == processSequence;
        });
         
        if(processVersionList !== undefined && processVersionList.length > 0){
            
            $.each(processVersionList, function(i, processVersion){
                var v = processVersion.processSequence;
                var t = processVersion.processName;
                var option = $("<option>").val(v).text(t);
                if(default_processSequence == v){
                    option = option.attr("selected", true);
                    $("#select_process").append(option);
                }
            });
        }
    });
}

function update_work_options(work){
    work = work || GLOBAL.workModifyData.work;
    
    switch(work.repeatCycleUnitCode){
        case "TIME":{
            var minute = parseInt(work.executeStandardValue);
            if(minute < 60){
                $("#select_rbt_ccl_unt").val("MINUTE");
                $("#select_ccl_unt_minute").val(minute);
                select_rbt_ccl_unt_changed();
            }else{
                $("#select_rbt_ccl_unt").val("TIME");
                $("#select_ccl_unt_time").val(minute);
                select_rbt_ccl_unt_changed();
            }
        }
        break;
        case "DAY":{
            $("#select_rbt_ccl_unt").val("DAY");
            select_rbt_ccl_unt_changed();
            update_time_select(work.executeHourminute);
        }
        break;
        case "WEEK":{
            $("#select_rbt_ccl_unt").val("WEEK");
            select_rbt_ccl_unt_changed();
            update_time_select(work.executeHourminute);
            
            for(var i=0; i<work.executeStandardValue.length; i++){
                var check = work.executeStandardValue.substr(i, 1);
                var v = [0,0,0,0,0,0,0];
                v[i] = "1";
                v = v.join("");
                
                var checked = false;
                if(check == "1"){
                    checked = true;
                }$("#tr_ccl_unit_week input[type='checkbox'][value='"+ v +"']").attr('checked', checked);
            }
            
        }
        break;
        default:{
            console.log("실행주기 코드 오류.");
        }
        
        
    }
    $("#select_work_active").val(work.activationYn);
}

function has_remains_robot_list(){
    var robot_max_count = get_robot_name_list().length;
    var select_count = get_robot_select_box_count();
    if(robot_max_count <= select_count){
        return false;
    }
    return true;
}

function get_robot_select_box_count(){
    return $("#tbody_robot_list select").length;
}

function get_robot_select_list(){
    var select_list = [];
    $("#tbody_robot_list select").each(function(i, e){
        var robotSequence = parseInt($(e).val());
        if(robotSequence > 0){
            select_list.push(robotSequence);
        }
    });
    return select_list;
}

// 하나의 select를 초기화.
function append_default_robot_option(target){
    $(target).append($("<option>").val("0").text("로봇 선택"));
}

function get_table_row_robot_select(assigned_robot){
    
    var tr = $("#tmpl_tr_select_robot tr").clone();
    var option = $("<option>");
    if(assigned_robot != undefined && assigned_robot != null){
        var v = assigned_robot.robotSequence;
        var t = assigned_robot.robotName;
        $(option).val(v).text(t).attr("selected", true);
    }else{
        var select = $(tr).find("select");
        append_default_robot_option(select);
    }
    $(tr).find("select").append(option);
    return tr;
}

function append_robot_select(assigned_robot){
    var tr = get_table_row_robot_select(assigned_robot);

    $("#tbody_robot_list").append(tr);
    //$(tr).find(".img_alarm").colorTip({color:'black'});
    
    update_all_robot_select_options();
    update_add_minus_robot_btn();
}

function update_add_minus_robot_btn(){
    $("#tbody_robot_list .btn-primary").not(":last").each(function(i, e){
        $(e).removeClass('robot-plus btn_add');
        $(e).addClass('robot-minus btn_del');
        $(e).attr('title', '대상 로봇 삭제');
    });
    $("#tbody_robot_list .btn-primary:last").each(function(i, e){
        $(e).addClass('robot-plus btn_add');
        $(e).removeClass('robot-minus btn_del');
        $(e).attr('title', '대상 로봇 추가');
    });
}
// get selected robots
function get_selected_robots(){
    var selected_robot = [];
    $("#tbody_robot_list select option:selected").each(function(i, e){
        var seq = $(e).val(); 
        selected_robot.push(parseInt(seq));
    });
    return selected_robot;
}

// 모든 select를 업데이트.
function update_all_robot_select_options(){
    
    $("#tbody_robot_list select").each(function(i, e){
        
        var selected_list = get_selected_robots();
        var robot_name_list = GLOBAL.workModifyData.robotNameList || [];
        var selected_robotSequence = parseInt($(e).find('option:selected').val());
        var selected_robotName = $(e).find('option:selected').text();
        
        var list = robot_name_list.filter(function(robotName){
            if(selected_robotSequence == robotName.robotSequence){
                return true;
            }
            var exists = $.grep(selected_list, function(e){
                return e == robotName.robotSequence;
            });
            if(exists.length > 0){
                return false;
            }
            return true;
        })
        
        $(e).empty();
        append_default_robot_option(e);
        var selected = false;
        list.forEach(function(robotName){
            
            var v = robotName.robotSequence;
            var t = robotName.robotName;
            var option = $("<option>").val(v).text(t);
            if(v == selected_robotSequence){
                $(option).attr("selected", true);
                selected = true;
            }
            $(e).append(option);
        });
        
        if(selected == false){
            if(selected_robotSequence != 0){
                var option = $("<option>").val(selected_robotSequence).text(selected_robotName).attr("selected", true);
                $(e).append(option);
                $(e).closest("td").find('.img_alarm').show()
            }
        }else{
            $(e).closest("td").find('.img_alarm').hide()
        }
        
        $(e).attr('title', $(e).find('option:selected').text());
    });
}


// update process version select
function update_process_version_select(default_processVersionSequence){
    default_processVersionSequence = default_processVersionSequence || GLOBAL.workModifyData.work.processVersionSequence;
    $("#select_process_version").empty();
    //$("#select_process_version").append($("<option>").val("0").text("버전 선택"));
    var seq = $("#select_process option:selected").val();
    var l = GLOBAL.workModifyData.processVersionList.filter(function(processVersion){
        return processVersion.processSequence == seq;
    });

    l.forEach(function(processVersion){
        var v = processVersion.processVersionSequence;
        var t = processVersion.majorVersion + "." + processVersion.minorVersion;
        
        var option = $("<option>").val(v).text(t);
        if(v == default_processVersionSequence){
            option.attr("selected", true);
        }
        $("#select_process_version").append(option);
    });
}


function get_selected_process_version_sequence(){
    var sequence = $("#select_process_version").val();
    if(sequence == null || sequence == '0'){
        return undefined;
    }
    return sequence;
}
// 선택한 프로세스 버전에 맞는 로봇 목록.
function get_robot_name_list(processVerionSequence){
    var list = GLOBAL.workModifyData.robotNameList || [];
    return list;
}


function validation_check(){
    var cnt = get_selected_robots().filter(function(seq){
        return seq > 0;
    }).length;
    
    if(cnt == 0){
        alert("<spring:message code="msg.m05.0004"/>");
        return false;
    }
    
    
    if($("#select_rbt_ccl_unt").val() == "WEEK"){
        var select_count_day_of_week = $("#tr_ccl_unit_week input[type='checkbox']:checked").length;
        if(select_count_day_of_week == 0){
            alert("<spring:message code="msg.m05.0003"/>");
            return false;
        }
    }
    return true;
}

/*
 * 실행주기(단위코드,주기값,시간) 또는 활성여부가 변경 체크
 */
function is_changed(body, work){
    var attr_changed = false;
    if(body.repeatCycleUnitCd != work.repeatCycleUnitCode){
        attr_changed = true;
    }else if(1 != work.repeatCycle){
        attr_changed = true;
    }else if(body.executeStandardValue != work.executeStandardValue){
        attr_changed = true;
    }else if(body.execHourMinute != work.executeHourminute){
        attr_changed = true;
    }else if(body.workActiveYn != work.activationYn){
        attr_changed = true;
    }
    return attr_changed;
}

/**
 * 추가해야할 로봇(스케줄 목록 생성)
 * 실행주기(단위코드,주기값,시간) 또는 활성여부가 변경되면 새로추가 함. ex) ABC -> CF 이면 CF
 * 변경이 없으면 추가된 로봇만 추가함. ex) ABC -> CF 이면 F
 * 
 * @param body 요청
 * @param work 기존 데이터
 */
function get_robot_added_list(body, work){
    var attr_changed = is_changed(body, work);
    
    if(attr_changed){
        return get_robot_select_list();
    }else{
        var init_list = [];
        GLOBAL.workModifyData.workAssignedRobotList.forEach(function(e){
            init_list.push(e.robotSequence);
        });
        
        var added_list = [];
        var selected_list = get_robot_select_list();
        selected_list.forEach(function(e) {
            var exist = $.grep(init_list, function(i){
               return i == e; 
            });
            if(exist.length > 0){
                ;
            }else{
                added_list.push(e);
            }
        });
        
        return added_list;
    }
}

/**
 * 삭제해야할 로봇(스케줄 목록 생성)
 * 실행주기(단위코드,주기값,시간) 또는 활성여부가 변경되면 모두 삭제목록으로 함. ex) ABC -> CF 이면 ABC
 * 변경이 없으면 제거된 로봇만 제거함. ex) ABC -> CF 이면 AB
 *
 * @param body 요청
 * @param work 기존 데이터
 */
 function get_robot_removed_list(body, work){
    
    var attr_changed = is_changed(body, work);
    
    var init_list = [];
    GLOBAL.workModifyData.workAssignedRobotList.forEach(function(e){
        init_list.push(e.robotSequence);
    });
    
    if(attr_changed){
        return init_list;
    }else {
        // 수정하여 제외된 로봇 목록만.
        var added_list = get_robot_select_list();
        var removed_list = [];
        
        init_list.forEach(function(e){
            var exist = $.grep(added_list, function(a){
                return a == e;
            });
            if(exist.length > 0){
                ;
            }else{
                removed_list.push(e);
            }
        });
        
        return removed_list;  
    }
}

function build_request_body_modify_work(){
    
    var pnv = GLOBAL.workModifyData.processNameVersion;
    var work = GLOBAL.workModifyData.work;
    var body = {
                    workSequence : work.workSequence,
                    robotSequenceList : [],
                    robotSequenceAddList : [],
                    robotSequenceRemoveList : [],
                    processVersionSequence : pnv.processVersionSequence,
                    repeatCycleUnitCd : undefined,
                    executeStandardValue : undefined,
                    execHourMinute : undefined,
                    workActiveYn : undefined
                    };
    
    body.robotSequenceList = get_robot_select_list();
    
    body.repeatCycleUnitCd = $("#select_rbt_ccl_unt").val();
    body.executeStandardValue = build_executeStandardValue(body.repeatCycleUnitCd);
    if($("#select_exec_hour_minute").css('display') != 'none'){
        body.execHourMinute = $("#select_exec_hour_minute").val();
    }
    body.workActiveYn = $("#select_work_active").val();
    
    if(body.repeatCycleUnitCd == 'MINUTE'){
        body.repeatCycleUnitCd = 'TIME';
    }
    
    // 팝업에서 수정하면서, 추가/삭제된 로봇 목록 
    body.robotSequenceAddList = get_robot_added_list(body, work);
    body.robotSequenceRemoveList = get_robot_removed_list(body, work);
    
    return body;
}

function build_executeStandardValue(repeatCycleUnitCd){
    if(repeatCycleUnitCd === undefined){
        repeatCycleUnitCd = $("#select_rbt_ccl_unt").val();
    }
    // 실행기준 값
    var executeStandardValue = undefined;
    switch(repeatCycleUnitCd){
        case "MINUTE":{
            executeStandardValue =  $("#select_ccl_unt_minute").val();
        }
        break;
        case "TIME":{
            executeStandardValue =  $("#select_ccl_unt_time").val();
        }
        break;
        case "DAY":{
            executeStandardValue = null;
        }
        break;
        case "WEEK":{
            var v = 0;
            $("#tr_ccl_unit_week input[type='checkbox']:checked").each(function(i, e){
                var e = parseInt($(e).val());
                v += e;
            });
            v = v.toString();
            executeStandardValue = v;
            for(var i=0; i<7-v.length; i++){
                executeStandardValue = '0' + executeStandardValue;
            }
        }
        break;
        default: {
            alert("<spring:message code="msg.m05.0002"/>");
        }
    }
    return executeStandardValue;
}
function closePopup(){
    hideBlockLayer("#workAssignmentModifyPopup");
    $("#workAssignmentModifyPopup").empty();
}

</script>

    <div class="pop-content">
        <h4 class="pop-title"><spring:message code="tit.m05.0001"/></h4>
        <a href="javascript:;" class="pop-close btnClosePopup"><img src="images/btnclose.png"></a>
        <table class="pop-fixed_headers lowercode_tb robot-2">
            <tbody class="flex">
                <tr>
                    <th style="width: 130px"><spring:message code="ctl.m05.0015"/></th>
                    <td style="width: 320px">
                        <div class="pop-tb-col">
                            <span id="processName"></span>
                            |
                            <span id="processVersion"></span>
                        </div>
                    </td>
                </tr>
                <tr>
                    <th style="width: 130px"><spring:message code="ctl.m05.0016"/></th>
                    <td style="width: 320px">
                        <div id="div_robot_table" style="max-height:160px; overflow-y:auto; overflow-x: hidden">
                            <table class="tb-robot-list">
                                <tbody id="tbody_robot_list" class="tb-robot-list-body">
                                    <tr>
                                        <td>
                                            <div class="col-xs-3 pop-tb-col">
                                                <select id="select_robot" class="pop-tb-selectpicker">
                                                    <option>로봇선택</option>
                                                </select>
                                            </div> 
                                            <span class="btn btn-primary tbbtn left tbbtn-2 btn_img btn_add robot-plus"></span>
                                        </td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </td>
                </tr>
                <tr>
                    <th id="th_ccl_unit" style="width: 130px;" rowspan="1"><spring:message code="ctl.m05.0006"/></th>
                    <td style="width: 320px">
                        <div>
                            <div class="col-xs-3 pop-tb-col">
                                <select id="select_rbt_ccl_unt" class="pop-tb-selectpicker">
                                    <option value="MINUTE"><spring:message code="ctl.m05.0002"/></option>
                                    <option value="TIME"><spring:message code="ctl.m05.0003"/></option>
                                    <option value="DAY"><spring:message code="ctl.m05.0004"/></option>
                                    <option value="WEEK"><spring:message code="ctl.m05.0005"/></option>
                                </select>
                            </div>
                            <div id="div_ccl_unt_minute" class="col-xs-3 pop-tb-col" style="display:block;">
                                <select id="select_ccl_unt_minute" class="pop-tb-selectpicker">
                                    <option value="10"><spring:message code="ctl.m05.0000" arguments="10" /></option>
                                    <option value="20"><spring:message code="ctl.m05.0000" arguments="20" /></option>
                                    <option value="30"><spring:message code="ctl.m05.0000" arguments="30" /></option>
                                    <option value="40"><spring:message code="ctl.m05.0000" arguments="40" /></option>
                                    <option value="50"><spring:message code="ctl.m05.0000" arguments="50" /></option>
                                </select>
                            </div>
                            <div id="div_ccl_unt_time" class="col-xs-3 pop-tb-col" style="display:none;">
                                <select id="select_ccl_unt_time" class="pop-tb-selectpicker">
                                    <option value="60"><spring:message code="ctl.m05.0001" arguments="1" /></option>
                                    <option value="120"><spring:message code="ctl.m05.0001" arguments="2" /></option>
                                    <option value="180"><spring:message code="ctl.m05.0001" arguments="3" /></option>
                                    <option value="240"><spring:message code="ctl.m05.0001" arguments="4" /></option>
                                    <option value="300"><spring:message code="ctl.m05.0001" arguments="5" /></option>
                                    <option value="360"><spring:message code="ctl.m05.0001" arguments="6" /></option>
                                    <option value="420"><spring:message code="ctl.m05.0001" arguments="7" /></option>
                                    <option value="480"><spring:message code="ctl.m05.0001" arguments="8" /></option>
                                    <option value="540"><spring:message code="ctl.m05.0001" arguments="9" /></option>
                                    <option value="600"><spring:message code="ctl.m05.0001" arguments="10" /></option>
                                    <option value="660"><spring:message code="ctl.m05.0001" arguments="11" /></option>
                                    <option value="720"><spring:message code="ctl.m05.0001" arguments="12" /></option>
                                </select>
                            </div>
                            <div id="div_ccl_unt_day" class="col-xs-3 pop-tb-col" style="display:none;">
                                <select id="select_exec_hour_minute" class="pop-tb-selectpicker">
                                </select>
                            </div>
                        </div>
                    </td>
                </tr>
                <tr id="tr_ccl_unit_week" style="display:none;">
                    <td style="width: 320px;">
                        <ul class="daypick">
                            <li>
                                <label for="check_monday"><spring:message code="ctl.m05.0007"/></label>
                                <input id="check_monday" type="checkbox" value="0100000">
                            </li>
                            <li>
                                <label for="check_tuesday"><spring:message code="ctl.m05.0008"/></label>
                                <input id="check_tuesday" type="checkbox" value="0010000">
                            </li>
                            <li>
                                <label for="check_wednesday"><spring:message code="ctl.m05.0009"/></label>
                                <input id="check_wednesday" type="checkbox" value="0001000">
                            </li>
                            <li>
                                <label for="check_thursday"><spring:message code="ctl.m05.0010"/></label>
                                <input id="check_thursday" type="checkbox" value="0000100">
                            </li>
                            <li>
                                <label for="check_friday"><spring:message code="ctl.m05.0011"/></label>
                                <input id="check_friday" type="checkbox" value="0000010">
                            </li>
                            <li>
                                <label for="check_saturday"><spring:message code="ctl.m05.0012"/></label>
                                <input id="check_saturday" type="checkbox" value="0000001">
                            </li>
                            <li>
                                <label for="check_sunday"><spring:message code="ctl.m05.0013"/></label>
                                <input id="check_sunday" type="checkbox" value="1000000">
                            </li>
                        </ul>
                    </td>
                </tr>
                <tr>
                    <th style="width: 130px"><spring:message code="ctl.m05.0014"/></th>
                    <td style="width: 320px">
                        <div class="col-xs-3 pop-tb-col">
                            <select id="select_work_active" class="pop-tb-selectpicker">
                                <option value="Y">활성</option>
                                <option value="N">비활성</option>
                            </select>
                        </div>
                    </td>
                </tr>
            </tbody>
            
        </table>
        <div class="right btns pop-btns clear pop-btns-notf">
            <span id="btnAddOk" class="btn btn-primary"><spring:message code="btn.com.0000"/></span>
            <span id="btnAddCancel" class="btn btn-primary btnClosePopup"><spring:message code="btn.com.0001"/></span>
        </div>
    </div>
<%-- template --%>
<div id="tmpl_tr_select_robot" style="display:none;">
    <table>
        <tbody>
            <tr>
                <td>
                    <div class="col-xs-3 pop-tb-col" style="width :200px;">
                        <select class="pop-tb-selectpicker" style="width: 100%"></select>
                    </div> 
                    <span class="btn btn-primary tbbtn left tbbtn-2 btn_img btn_add robot-plus" style="margin-left: 10px;" title="대상 로봇 추가"></span>
                    <span class="img_alarm" title="<spring:message code='err.m05.0005'/>" style="display:none;"></span>
                </td>
            </tr>
        </tbody>
    </table>
</div>
