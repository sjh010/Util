<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<%@ include file="/WEB-INF/views/include/frame/header.jsp"%>
<title>INZI RPA SUITE MANAGER</title>
<style>
.pieChart {
    margin: auto;
    max-width: 500px;
    max-height: 250px;
    height: 250px;
}

.horizontalChart {
    margin: auto;
    max-width: 800px !important;
    min-width: 400px;
    min-height: 200px !important;
    height: 250px !important;
}
</style>
<script>
var registedRobotStatusChart = {};
var processExecuteCountChart = {};
var processExecuteResultChart = {};
var robotErrorChart = {};

$(document).ready(function() {
    // 메뉴 활성화
    setMenuTab("dashboard");

    // 차트 기본 폰트 크기
    Chart.defaults.global.defaultFontSize = 15;

    getDashboardInfo();
});

// 등록 로봇 상태 차트
function createRegistedRobotStatusChart(robotInfo) {
    var data = [robotInfo.waitCount, robotInfo.workCount, robotInfo.disconnectCount, robotInfo.stopCount];

    var totalCount = 0;
    $.each(data, function(index, value) {
        totalCount += value;
    })

    registedRobotStatusChart.chartType = "pie";
    registedRobotStatusChart.chartData = {};
    registedRobotStatusChart.chartData.labels = ["대기", "작업중", "연결 해제", "중지"];
    registedRobotStatusChart.chartData.datasets = [{
        "data": data,
        "backgroundColor": ["#1abc9c", "#f2c40f", "#9b59b6", "#3498db"]
    }];
    registedRobotStatusChart.chartOptions = {};
    registedRobotStatusChart.chartOptions.maintainAspectRatio = false;
    registedRobotStatusChart.chartOptions.legend = {
        position: "bottom"
    };
    registedRobotStatusChart.chartOptions.tooltips = {
                    xPadding : 10,
                    yPadding : 10,
                    callbacks: {
                        label: function(tooltipItem, data) {
                            var label = data.datasets[0].data[tooltipItem.index] || 0;
                            var count = label;
            
                            if (label) {
                                var percentage = label / totalCount * 100;
                                if ((percentage - Math.floor(percentage)) != 0) {
                                    label = percentage.toFixed(1);
                                } else {
                                    label = percentage;
                                }
                                label += "%, " + count + "건";
                            }
                            return label;
                        }
                    }
    }

    createCanvasChart("pie1", registedRobotStatusChart);
}

// 프로세스 실행 현황 차트
function createProcessExecuteCountChart(processInfo) {
    var processNameList = [];
    var data = [];

    $.each(processInfo, function(index, item) {
        // y축 프로세스명 최대길이 15
        var processName = (item.processName.length <= 10) ? item.processName : item.processName.substring(0,9) + "...";
        processNameList.push(processName + "(" + item.majorVersion + "." + item.minorVersion + ")");
        data.push(item.executeCount);
    });

    processExecuteCountChart.chartType = "horizontalBar";
    processExecuteCountChart.chartData = {};
    processExecuteCountChart.chartData.labels = processNameList;
    processExecuteCountChart.chartData.datasets = [{
        "data": data,
        "backgroundColor": "rgb(75, 192, 192)"
    }];
    processExecuteCountChart.chartOptions = {};
    processExecuteCountChart.chartOptions.maintainAspectRatio = false;
    processExecuteCountChart.chartOptions.legend = {
        display: false,
        labels: {
            fontSize: 12
        }
    }
    processExecuteCountChart.chartOptions.tooltips = {
                    xPadding : 10,
                    yPadding : 10,
                    callbacks: {
                        title : function(tooltipItem, data) {
                            return "";
                        },
                        label: function(tooltipItem, data) {
                            var process = processInfo[tooltipItem.index];
                            var processFullName = process.processName + "(" + process.majorVersion + "." + process.minorVersion + ")"
                            var label = processFullName;
                            var count = tooltipItem.xLabel + "건";
                           
                            return label + ", " + count;
                        }
                    }
    }
    
    var max = 0;
    if(data.length > 0){
        max = Math.max.apply(null, data);
    }
    if(max % 2 != 0) {
        max = max + 1;
    }
    
    var stepSize = (Math.floor(max / 20) + 1) * 2;
    
    while(max % stepSize != 0) {
        max += 1;
    }
    
    processExecuteCountChart.chartOptions.scales = {
        xAxes: [{
            ticks: {
                min: 0,
                max: max,
                stepSize : stepSize
            },
        }],
        yAxes: [{
            barPercentage: 0.7
        }]
    }
    createCanvasChart("bar", processExecuteCountChart);
}

// 프로세스 실행 결과 차트
function createProcessExecuteResultChart(processResultInfo) {
    var successCount = 0;
    var failCount = 0;

    $.each(processResultInfo, function(index, item) {
        if (item.taskStatusCode == "SCCS") {
            successCount = item.count;
        } else if (item.taskStatusCode == "FAIL") {
            failCount = item.count;
        }
    });

    var data = [successCount, failCount];

    var totalCount = successCount + failCount;

    processExecuteResultChart.chartType = "pie";
    processExecuteResultChart.chartData = {};
    processExecuteResultChart.chartData.labels = ["성공", "실패"];
    processExecuteResultChart.chartData.datasets = [{
        "data": data,
        "backgroundColor": ["#1abc9c", "#f2c40f"]
    }];

    processExecuteResultChart.chartOptions = {};
    processExecuteResultChart.chartOptions.maintainAspectRatio = false;
    processExecuteResultChart.chartOptions.legend = {
        position: "bottom"
    }
    processExecuteResultChart.chartOptions.tooltips = {
        callbacks: {
            xPadding : 10,
            yPadding : 10,
            label: function(tooltipItem, data) {
                var label = data.datasets[0].data[tooltipItem.index] || 0;
                var count = label;

                if (label) {
                    var percentage = label / totalCount * 100;
                    if ((percentage - Math.floor(percentage)) != 0) {
                        label = percentage.toFixed(1);
                    } else {
                        label = percentage;
                    }
                    label += "%, " + count + "건";
                }
                return label;
            }
        }
    }

    createCanvasChart("pie2", processExecuteResultChart);
}

// 오류 발생 현황(로봇) 차트
function createRobotErrorChart(robotErrorInfo) {
    var labels = [];
    var values = [];
    
    var today = new Date();
    var currentHour = today.getHours();

    for (var i = 0; i <= 24; i++) {
        var time = i;
        time = (time >= 10) ? time : "0" + time;
        labels.push(time + "시");

        if(i <= currentHour) {
            values.push(0);    
        }
    }

    for (var i = 0; i < robotErrorInfo.length; i++) {
        var hour = Number(robotErrorInfo[i].hour);
        var count = robotErrorInfo[i].count;

        values[hour] = count;
    }

    robotErrorChart.chartType = "line";
    robotErrorChart.chartData = {};
    robotErrorChart.chartData.labels = labels;
    robotErrorChart.chartData.datasets = [{
        "data": values,
        "fill": false,
        "borderColor": "rgb(75, 192, 192)",
        "backgroundColor" : "rgb(75, 192, 192)",
        "lineTension": 0.1
    }];

    robotErrorChart.chartOptions = {};
    robotErrorChart.chartOptions.maintainAspectRatio = false;
    robotErrorChart.chartOptions.legend = {
        display: false
    }
    
    var maxValue = 0;
    if(values.length > 0){
        maxValue = Math.max.apply(null, values);
    }
    
    maxValue = (maxValue == 0) ? 5 : maxValue;
   
    if(maxValue % 2 != 0) {
        maxValue = maxValue + 1;
    }

    var stepSize = (Math.floor(maxValue / 10) + 1) * 2;

    while(maxValue % stepSize != 0) {
        maxValue += 1;
    }

    robotErrorChart.chartOptions.scales = {
        yAxes: [{
            ticks: {
                beginAtZero: 0,
                min: 0,
                max : maxValue,
                stepSize : stepSize
            }
        }]
    }

    robotErrorChart.chartOptions.tooltips = {
                    xPadding : 10,
                    yPadding : 10,
                    callbacks: {
                        title : function(tooltipItem, data) {
                            return "";
                        },
                        label: function(tooltipItem, data) {
                            var label = data.labels[tooltipItem.index];
                            var count = tooltipItem.yLabel;
                            
                            return label + ", " + count + "건";
                           
                        }
                    }
    }
    
    createCanvasChart("line", robotErrorChart);
}

function getDashboardInfo() {
    Ajax.post().url("/dashboard").success(createDashboard).execute();
}

// 대시보드 그리기
function createDashboard(response) {
    $("#robotCount").text(response.robotInfo.totalCount);
    $("#processCount").text(response.processCount);
    $("#workCount").text(response.activationWorkCount);

    var robotInfo = response.robotInfo;
    
    if(robotInfo.totalCount && 
                    (robotInfo.disconnectCount > 0 || robotInfo.stopCount > 0 || robotInfo.waitCount > 0 || workCount > 0)) {
        createRegistedRobotStatusChart(response.robotInfo);    
    } else {
        showErrorText("robotStatusInfo");
    }
    
    if(response.processList.length) {
        createProcessExecuteCountChart(response.processList);    
    } else {
        showErrorText("processExecuteCount");
    }
    
    if(response.processResultList.length) {
        createProcessExecuteResultChart(response.processResultList);    
    } else {
        showErrorText("processExecuteResult");
    }
    
    if(response.robotErrorList.length) {
        createRobotErrorChart(response.robotErrorList);    
    } else {
        showErrorText("robotErrorInfo")
    }
}

// 차트 생성
function createCanvasChart(canvasId, chartMap) {
    var myChart = $("#" + canvasId);

    var chart = new Chart(myChart, {
        type: chartMap.chartType,
        data: chartMap.chartData,
        options: chartMap.chartOptions
    });
}

// 조회 결과 없음
function showErrorText(selector) {
    $("#" + selector).find('canvas').remove();
    $("#" + selector).append('<p style="text-align: center;">' + "<spring:message code="msg.com.0001"/>" + '</p>');
}
</script>
</head>

<body>
    <div class="wrapper">
        <%@ include file="/WEB-INF/views/include/frame/top.jsp"%>
        <%@ include file="/WEB-INF/views/include/frame/menu.jsp"%>

        <!-- content start -->
        <div class="content">
            <div class="container">
                <span class="update-date">${today} 기준</span>
                <div class="row">
                    <div class="col-xs-2">
                        <div>
                            <h5>로봇 등록 수</h5>
                            <p id="robotCount" class="count"></p>
                        </div>
                    </div>
                    <div class="col-xs-2">
                        <div>
                            <h5>등록 프로세스</h5>
                            <p id="processCount" class="count"></p>
                        </div>
                    </div>
                    <div class="col-xs-2">
                        <div>
                            <h5>프로세스 업무 할당</h5>
                            <p id="workCount" class="count"></p>
                        </div>
                    </div>
                    <!-- 2차 개발 -->
                    <!-- <div class="col-xs-2">
                        <div>
                            <h5>대기열</h5>
                            <p class="count"></p>
                        </div>
                    </div> -->
                </div>
                <div class="row">
                    <div class="col-xs-6">
                        <div id="robotStatusInfo">
                            <h5>
                                <span class="glyphicon glyphicon-record"></span>
                                                                        등록 로봇 상태
                            </h5>
                            <canvas id="pie1" class="pieChart"></canvas>
                        </div>
                    </div>
                    <div class="col-xs-6">
                        <div id="processExecuteCount">
                            <h5>
                                <span class="glyphicon glyphicon-record"></span>
                                                                        프로세스 실행 현황
                            </h5>
                            <canvas id="bar" class="horizontalChart"></canvas>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-xs-6">
                        <div id="processExecuteResult">
                            <h5>
                                <span class="glyphicon glyphicon-record"></span>
                                                                        프로세스 실행 결과
                            </h5>
                            <canvas id="pie2" class="pieChart"></canvas>
                        </div>
                    </div>
                    <div class="col-xs-6">
                        <div id="robotErrorInfo">
                            <h5>
                                <span class="glyphicon glyphicon-record"></span>
                                                                         오류 발생 현황
                            </h5>
                            <canvas id="line" class="horizontalChart"></canvas>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
<%@ include file="/WEB-INF/views/include/frame/bottom.jsp"%>
</html>