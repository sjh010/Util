/**
 * 날짜 형식 변환
 * yyyyMMdd -> yyyy-MM-dd
 * yyyy-MM-dd hh:mm:ss.SSS -> yyyy-MM-dd
 * yyyyMM -> yyyy-MM
 * 
 * @param str YYYYMMDD 형태의 날짜
 * @return YYYY-MM-DD 형태로 변환된 날짜
 */
function setDateFormat(str) {
	if (str == null) {
		return "";
	} else if (str.length == 6) {
		return str.substring(0, 4) + "-" + str.substring(4, 6);
	} else if (str.length == 8) {
		return str.substring(0, 4) + "-" + str.substring(4, 6) + "-" + str.substring(6, 8);
	} else if (str.length >= 10) {
		return str.substring(0, 4) + "-" + str.substring(5, 7) + "-" + str.substring(8, 10);
	} else {
		return str;
	}
}

/**
 * 날짜(시간) 형식 변환
 * 
 * @param str yyyy-MM-dd HH:mm:ss
 * @return yyyy-MM-dd HH시 mm분
 */
function setDateHMFormat(str) {
	if (str == null) {
		return "";
	} else if (str.length == 19) {
		var date = str.substr(0, 10);
		var hour = str.substr(11, 2);
		var minute = str.substr(14,2);
		
		return date + " " + hour + '시 ' + minute + '분 ';
	} else {
		return str;
	}
}


function getToday() {
	return setDateFormatFromDate(new Date());
}

function getNow() {
	return setDateTimeFormatFromDate(new Date());
}
/**
 * YYYY-MM-DD 또는 YYYYMMDD에서 YYYY를 추출
 * @param str YYYY-MM-DD or YYYY-MM-DD 
 * @return int YYYY
 */
function getYearFromString(date){
	date = removeDash(date);
	return parseInt(date.substring(0,4));
}

/**
 * YYYY-MM-DD 또는 YYYYMMDD에서 MM를 추출
 * @param str YYYY-MM-DD
 * @return int MM
 */
function getMonthFromString(date){
	date = removeDash(date);
	return parseInt(date.substring(4,6));
}
/**
 * YYYY-MM-DD 또는 YYYYMMDD에서 DD를 추출
 * @param str YYYY-MM-DD
 * @return int DD
 */
function getDateFromString(date){
	date = removeDash(date);
	return parseInt(date.substring(6,8));
}
/**
 * Date 를 YYYY-MM-DD형태로 변환
 * 
 * @param Date
 * @return YYYY-MM-DD 형태로 변환된 날짜
 */
function setDateFormatFromDate(d) {
	var year = d.getFullYear();
	var month = "" + (d.getMonth()+1);
	var date = "" + d.getDate();
	if (month.length == 1)
		month = "0" + month;
	if (date.length == 1)
		date = "0" + date;
	
	return year + "-" +  month + "-" + date;
}

/**
* Date 를 YYYY-MM-DD hh:mm 형태로 변환
* 
* @param Date
* @return YYYY-MM-DD hh:mm 형태로 변환된 날짜/시간
*/
function setDateTimeFormatFromDate(d) {
	var year = d.getFullYear();
	var month = "" + (d.getMonth()+1);
	var date = "" + d.getDate();
	var hour = "" + d.getHours();
	var minute = "" + d.getMinutes();
	var second = "" + d.getSeconds();
	if (month.length == 1)
		month = "0" + month;
	if (date.length == 1)
		date = "0" + date;
	if (hour.length == 1)
		hour = "0" + hour;
	if (minute.length == 1)
		minute = "0" + minute;
	if (second.length == 1)
		second = "0" + second;
	//return year + "-" +  month + "-" + date + " " + hour + ":" + minute + ":" + second;
	return year + "-" +  month + "-" + date + " " + hour + ":" + minute;
}

/**
 * YYYY-MM-DD형태 또는 YYYYMMDD인지 확인
 * 
 * @param YYYY-MM-DD 형태 또는 YYYYMMDD형태 로 변환된 날짜
 * @return true/false
 */
function checkDateFormat(formatStr,trueEmpty) {

	if (trueEmpty == true && !formatStr){
			return true;
	}
		
	var regex = /^[0-9][0-9][0-9][0-9]-[0-1][0-9]-[0-3][0-9]$/;
	if (regex.test(formatStr) == true)
		return true;
	var regex2 = /^[0-9][0-9][0-9][0-9][0-1][0-9][0-3][0-9]$/;
	if (regex2.test(formatStr) == true)
		return true;
	return false;
}
/** 
 * YYYY-MM-DD 또는 YYYY-MM형태의 날짜를 YYYYMMDD 또는 YYYYMM로 변환한다.
 * 
 * @param YYYY-MM-DD 형태 
 * @param int
 * @return YYYYMMDD
 */
function removeDash(dateStr) {
	if (dateStr == null || !(dateStr.length == 8 || dateStr.length == 10 || dateStr.length == 6 || dateStr.length == 7)) {
		return "";
	} else if (dateStr.length == 10) {
		dateStr = dateStr.replace(/-/gi, '');
	}else if (dateStr.length == 7) {
		dateStr = dateStr.replace(/-/gi, '');
	}
	return dateStr;
}
/** 
 * 주어진 날짜에서 달을 더하고 뺀 월을 얻는다.
 * 
 * @param YYYY-MM-DD 형태 또는 YYYYMMDD형태 로 변환된 날짜
 * @param int
 * @param options			{delimiter:"-" //날짜 구분자, }
 * @return YYYY-MM-DD
 */
function addMonths(dateStr, months, options) {
	
	options = options || {}; 
	if (options.delimiter == null){
		options.delimiter = "-";
	}
	
	if (dateStr == null || !(dateStr.length == 8 || dateStr.length == 10)) {
		if (dateStr.length == 6){
			dateStr += "01";
		}else{
			return "";
		}
	} else if (dateStr.length == 10) {
		dateStr = dateStr.replace(/-/gi, '');
	}
	
	var year = parseInt(dateStr.substring(0,4));
	var month = parseInt(dateStr.substring(4,6));
	var date = parseInt(dateStr.substring(6,8));
	var d = new Date(year, (month-1)+months, date);
	
	return d.getFullYear() + options.delimiter + ((d.getMonth()+1) < 10 ? "0"+(d.getMonth()+1) : (d.getMonth()+1)) + options.delimiter + (d.getDate() < 10 ? "0"+d.getDate() : d.getDate());
}
/** 
 * 주어진 날짜에서 연도 월 정보를 얻는다.
 * 
 * @param YYYY-MM-DD 형태 또는 YYYYMMDD형태 로 변환된 날짜
 * @return YYYYMM
 */
function getYYYYMM(dateStr) {
	if (dateStr == null || !(dateStr.length == 8 || dateStr.length == 10)) {
		return "";
	} else if (dateStr.length == 10) {
		dateStr = dateStr.replace(/-/gi, '');
	}
	
	var year = parseInt(dateStr.substring(0,4));
	var month = parseInt(dateStr.substring(4,6));
	
	return "" + year + ((month) < 10 ? "0"+(month) : (month));
}

function getDisplayTime(h, m){
    h = parseInt(h);
    m = parseInt(m);
    
    var t = '';
    if(m < 10){
        m = '0' + m;
    }
    
    if(h < 12){
        t = '오전 ' + h + ':' + m;
    }else{
        t = '오후 ' + (h-12) + ':' + m;
    }
    return t;
}
Date.prototype.format = function(f) {
    if (!this.valueOf()) return " ";
 
    var weekFullName = ["일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일"];
    var weekName = ["일", "월", "화", "수", "목", "금", "토"];
    var d = this;
     
    return f.replace(/(yyyy|yy|MM|dd|E|hh|mm|ss|a\/p)/gi, function($1) {
        switch ($1) {
            case "yyyy": return d.getFullYear();
            case "yy": return (d.getFullYear() % 1000).zf(2);
            case "MM": return (d.getMonth() + 1).zf(2);
            case "dd": return d.getDate().zf(2);
            case "EE": return weekFullName[d.getDay()];
            case "E": return weekName[d.getDay()];
            case "HH": return d.getHours().zf(2);
            case "hh": return ((h = d.getHours() % 12) ? h : 12).zf(2);
            case "mm": return d.getMinutes().zf(2);
            case "ss": return d.getSeconds().zf(2);
            case "a/p": return d.getHours() < 12 ? "오전" : "오후";
            default: return $1;
        }
    });
};

String.prototype.string = function(len){var s = '', i = 0; while (i++ < len) { s += this; } return s;};
String.prototype.zf = function(len){return "0".string(len - this.length) + this;};
Number.prototype.zf = function(len){return this.toString().zf(len);};

function getPeriodYearOfDate(year) {
	return year + "-01-01 ~ " + year + "-12-31"; 
}

function getPeriodMonthOfDate(year, month) {
	var lastDay = getLastdayOfMonth(year, month-1);
	return year + "-" + (month < 10 ? "0" + month : month) + "-01 ~ " + year + "-" + (month < 10 ? "0" + month : month) + "-" + (lastDay < 10 ? "0" + lastDay : lastDay);
}

function getLastdayOfMonth(year, month) {
	return(new Date( year, month, 0)).getDate();
}

/**
 * 특정 일자가 속한 일주일 기간 문자열을 가져온다. (월요일 기준)
 * @param year
 * @param month
 * @param date
 */
function getPeriodWeekOfDate(year, month, date) {
	var _currentDate = new Date(year, month-1, date);
	var _day = _currentDate.getDay();
	var _startDate = new Date(year, month-1, date - (_day-1));
	var _endDate = new Date(year, month-1, date + (7 -_day));
	var _startDateStr = "";
	_startDateStr += _startDate.getFullYear();
	_startDateStr += "-";
	_startDateStr += (_startDate.getMonth()+1)<10?"0"+(_startDate.getMonth()+1):(_startDate.getMonth()+1);
	_startDateStr += "-";
	_startDateStr += _startDate.getDate()<10?"0"+_startDate.getDate():_startDate.getDate();
	
	var _endDateStr = "";
	_endDateStr += _endDate.getFullYear();
	_endDateStr += "-";
	_endDateStr += (_endDate.getMonth()+1)<10?"0"+(_endDate.getMonth()+1):(_endDate.getMonth()+1);
	_endDateStr += "-";
	_endDateStr += _endDate.getDate()<10?"0"+_endDate.getDate():_endDate.getDate();
	
	return _startDateStr + " ~ " + _endDateStr;
}