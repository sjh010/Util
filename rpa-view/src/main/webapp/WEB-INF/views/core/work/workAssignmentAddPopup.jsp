<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<c:set var="_context" value="${(pageContext.request.contextPath eq '/')? '': pageContext.request.contextPath}" />
<script type="text/javascript">
    var GLOBAL = GLOBAL === undefined ? {} : GLOBAL;
    GLOBAL.error_message = GLOBAL.error_message === undefined ? {} : GLOBAL.error_message;
    GLOBAL.error_message["not_defined_rpt_ccl_unt_cd"] = "정의되지 않은 실행 주기입니다.";
    GLOBAL.error_message["not_select_day_of_week"] = "'실행 주기 요일'를 확인 해주세요.";
    GLOBAL.error_message["fail_add_work"] = "할당 업무추가를 실패하였습니다.";
    GLOBAL.error_message["not_select_process_version"] = "대상 프로세스 버전을 확인 해주세요.";
    GLOBAL.error_message["not_select_robots"] = "대상 로봇을 확인 해주세요.";
    
    $(document).ready(function() {
        Ajax.post().url("/work/add/data").success(function(response){

            if(response !== undefined){
                GLOBAL.workAddData = response;
                
                update_process_select();
                update_process_version_select();
                update_robot_select();
            }
        }).failure(function(response){
            alert("<spring:message code="err.m05.0006" />");
        }).execute();
        
        $("#tbody_robot_list").on("click", function(e){
            var target = e.target;
            if(target !== undefined && $(target).hasClass("pop-tb-selectpicker")){
                if($("#select_process option:selected").val() == "0"){
                    alert("프로세스를 선택해 주세요.");
                    return;
                }
                if(get_selected_process_version_sequence() === undefined){
                    alert("프로세스 버전을 선택해 주세요.");
                    return;
                }
            }
        });
        
        $("#tbody_robot_list").on("change", function(e){
            var target = e.target;
            if(target !== undefined && $(target).hasClass("pop-tb-selectpicker")){
                update_all_robot_select_options();
            }
        });

        $("#select_rbt_ccl_unt").change(function(){
            var v = $("#select_rbt_ccl_unt option:selected").val();
            console.log(v);
            
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

                    init_time_select();
                }
                break;
                case "WEEK":{
                    $("#div_ccl_unt_minute").css("display", "none");
                    $("#div_ccl_unt_time").css("display", "none");
                    $("#div_ccl_unt_day").css("display", "block");
                    $("#th_ccl_unit").attr("rowspan", 2);
                    $("#tr_ccl_unit_week").css("display", "table-row");
                    init_time_select();
                }
                break;
                default:{
                    alert(GLOBAL.error_message["not_defined_rpt_ccl_unt_cd"]);
                }
            }
        });

        
        $(".btnClosePopup").click(function() {
            closePopup();
            
        });

        $("#btnAddOk").on('click', function(){
            if(validation_check() == false){
                return;
            }

            var body = build_request_body_add_work();
            Ajax.post().sync()
                .url("/work/add/regist")
                .requestBody(JSON.stringify(body))
                .success(function(response){
                    switch(response.errorCode){
                        case "SUCCESS":{
                            closePopup();
                            refresh(); // 목록 업데이트.
                        }
                        break;
                        case "ROBOT_IS_ASSIGNED":{
                            var robotName = response.otherWorkAssignedRobotNameList.join(", ");
                            var msg = "<spring:message code='err.m05.0001' />";
                            msg = msg.replace('로봇명', robotName);
                            alert(msg);
                        }
                        break;
                        case "PROCESS_IS_INACTIVE":{
                            alert("<spring:message code="err.m05.0002"/>");
                        }
                        break;
                    }
                    
                })
                .failure(function(){
                    alert("<spring:message code="err.m05.0014"/>");
                })
                .execute();
            
        });

        $("#select_process").change(function(){
            update_process_version_select();
            update_robot_select([]);
            
        });
        
        $("#select_process_version").change(function(){
            update_robot_select([]);
             
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

        init_time_select();
    });
    
    function update_process_select(default_processSequence){
        default_processSequence = default_processSequence || 0;
        
        $("#select_process").empty();
        var processSequenceList = [];
        GLOBAL.workAddData.processVersionList.forEach(function(processVersion){
            var exists = $.grep(processSequenceList, function(p){
                return p == processVersion.processSequence;
            });
            if(exists.length == 0){
                processSequenceList.push(processVersion.processSequence);
            }
        });
        processSequenceList.forEach(function(processSequence){
            var processVersionList = $.grep(GLOBAL.workAddData.processVersionList, function(pv){
                return pv.processSequence == processSequence;
            });
             
            if(processVersionList !== undefined && processVersionList.length > 0){
                var processVersion = processVersionList[0];
                //$.each(processVersionList, function(i, processVersion){
                    var v = processVersion.processSequence;
                    var t = processVersion.processName;
                    var option = $("<option>").val(v).text(t);
                    if(default_processSequence == v){
                        option = option.attr("selected", true);
                    }
                    $("#select_process").append(option);
                //});
                    $("#select_process").attr('title', $("#select_process option:selected").text());
            }
        });
    }
    
    // update process version select
    function update_process_version_select(default_processVersionSequence){
        default_processVersionSequence = default_processVersionSequence || 0;
        $("#select_process_version").empty();
        var seq = $("#select_process option:selected").val();
        var l = GLOBAL.workAddData.processVersionList.filter(function(processVersion){
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
 
    // update robot select options.
    function update_robot_select(default_robotList){
        default_robotList = default_robotList || [];
        $("#tbody_robot_list tr").remove();
        
        if(default_robotList !== undefined && default_robotList != null && default_robotList.length > 0){
            default_robotList.forEach(function(assigned_robot){
                append_robot_select(assigned_robot);
            });
            if(has_remains_robot_list() == true){
                append_robot_select();
            }
        }else{
            append_robot_select();
        }
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
    
    function get_table_row_robot_select(assigned_robot){
        //debugger;
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
        
        update_all_robot_select_options();
        update_add_minus_robot_btn();
    }
    
    // 모든 select를 업데이트.
    function update_all_robot_select_options(){
        //debugger;
        $("#tbody_robot_list select").each(function(i, e){
            
            var selected_list = get_selected_robots();
            var robot_name_list = GLOBAL.workAddData.robotNameList || [];
            var selected_robotSequence = parseInt($(e).val());
            
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
            });
            
            $(e).empty();
            append_default_robot_option(e);
            list.forEach(function(robotName){
                
                var v = robotName.robotSequence;
                var t = robotName.robotName;
                var option = $("<option>").val(v).text(t);
                if(v == selected_robotSequence){
                    $(option).attr("selected", true);
                }
                $(e).append(option);
            });
            
            $(e).attr('title', $(e).find('option:selected').text());
        });
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
    
    function init_time_select(){
        $("#div_ccl_unt_day select").empty();
        $("#div_ccl_unt_day .btn_minus_robot").empty();
        var h = 0;
        var m = 0;
        while(true){
            var v = '';
            v += (h < 10 ? '0'+ h : h);
            v += '' + (m < 10 ? '0'+ m : m);
            
            var t = getDisplayTime(h, m);

            $("#div_ccl_unt_day select").append($("<option>").val(v).text(t));

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

    // 하나의 select를 초기화.
    function append_default_robot_option(target){
        $(target).append($("<option>").val("0").text("로봇 선택"));
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
    
    function get_selected_process_version_sequence(){
        var sequence = $("#select_process_version").val();
        if(sequence == null || sequence == '0'){
            return undefined;
        }
        return sequence;
    }
    // 선택한 프로세스 버전에 맞는 로봇 목록.
    function get_robot_name_list(processVerionSequence){
        var list = GLOBAL.workAddData.robotNameList || [];
        return list;
        
    }
    
    
    function validation_check(){
        if(get_selected_process_version_sequence() === undefined){
            alert(GLOBAL.error_message["not_select_process_version"]);
            return false;
        }
        
        var cnt = get_selected_robots().filter(function(seq){
            return seq > 0;
        }).length;
        
        if(cnt == 0){
            alert(GLOBAL.error_message["not_select_robots"]);
            return false;
        }
        
        
        if($("#select_rbt_ccl_unt").val() == "WEEK"){
            var select_count_day_of_week = $("#tr_ccl_unit_week input[type='checkbox']:checked").length;
            if(select_count_day_of_week == 0){
                alert(GLOBAL.error_message["not_select_day_of_week"]);
                return false;
            }
        }
        return true;
    }

    function build_request_body_add_work(){
        var body = {
            robotSequenceList : [],
            processVersionSequence : undefined,
            repeatCycleUnitCd : undefined,
            executeStandardValue : undefined,
            execHourMinute : undefined,
            workActiveYn : undefined
        };
        
        body.robotSequenceList = get_robot_select_list();
        body.processVersionSequence = $("#select_process_version").val();
        body.repeatCycleUnitCd = $("#select_rbt_ccl_unt").val();
        body.executeStandardValue = build_executeStandardValue(body.repeatCycleUnitCd);
        if($("#select_exec_hour_minute").css('display') != 'none'){
            body.execHourMinute = $("#select_exec_hour_minute").val();
        }
        body.workActiveYn = $("#select_work_active").val();
        
        if(body.repeatCycleUnitCd == 'MINUTE'){
            body.repeatCycleUnitCd = 'TIME';
        }
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
                alert(GLOBAL.error_message["not_defined_rpt_ccl_unt_cd"]);
            }
        }
        return executeStandardValue;
    }
    function closePopup(){
        hideBlockLayer("#workAssignmentAddPopup");
        $("#workAssignmentAddPopup").empty();
    }
</script>

    <div class="pop-content">
        <h4 class="pop-title"><spring:message code="tit.m05.0000"/></h4>
        <a href="javascript:;" class="pop-close btnClosePopup"><img src="images/btnclose.png"></a>
        <table class="pop-fixed_headers lowercode_tb robot-2">
            <tbody class="flex">
                <tr>
                    <th style="width: 130px"><spring:message code="ctl.m05.0015"/></th>
                    <td style="width: 320px">
                        <div class="col-xs-3 pop-tb-col">
                            <select id="select_process" class="pop-tb-selectpicker">
                            </select>
                        </div>
                        <div class="col-xs-3 pop-tb-col">
                            <select id="select_process_version" class="pop-tb-selectpicker"></select>
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
                                                    <option>로봇 선택</option>
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
                                    <%-- <option value="MNTH">매월</option>
                                    <option value="YEAR">매년</option> --%>
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
                        <select class="pop-tb-selectpicker" style="width :200px;"></select>
                    </div> 
                    <span class="btn btn-primary tbbtn left tbbtn-2 btn_img btn_add robot-plus" style="margin-left: 10px;" title="대상 로봇 추가"></span>
                </td>
            </tr>
        </tbody>
    </table>
</div>
