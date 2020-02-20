/**
 * 문자열을 정수로 변환
 * 
 * @param str	문자열
 * @returns	변환된 정수
 */
function toInt(str) {
	var n = null;
	try {
		n = parseInt(str, 10);
	} catch (e) {
	}
	return n;
}

/**
 * ID 유효성 검사(영문, 숫자 포함 8~15자리)
 * 
 * @param idValue
 * @returns
 */
function validateId(idValue) {
    var regExp = /^[a-z|A-Z|0-9|\*]+$/;
    
    if(idValue.length >= 8 && idValue.length <= 15) {
        if(regExp.test(idValue)) {
            return true;
        } else {
            return false;
        }
    } else {
        return false;
    }
}

/**
 * ~명(이름) 유효성 검사(한글, 영어, 숫자, 특수문자('_', '-'))
 * 
 * @param nameValue
 * @param maxLength 최대 문자 수
 * @returns
 */
function validateName(nameValue, maxLength) {
    var namePattern = /^[ㄱ-ㅎ|가-힣|a-z|A-Z|0-9|'\-'|'_'\*]+$/;
    
    if(nameValue.length <= maxLength) {
        if(namePattern.test(nameValue)) {
            return true;
        } else {
            return false;
        }
    } else {
        return false;
    }
}

/**
 * IP 주소 유효성 검사
 * 
 * @param ipValue
 * @returns
 */
function validateIpAddress(ipValue) {
    var ipPattern = /^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$/;
    
    if(ipValue.length <= 15) {    
        if(ipPattern.test(ipValue)) {
            return true;
        } else {
            return false;
        }
    }
}

/**
 * ISO 날짜 유효성 검사 (yyyy-MM-dd)
 */
function validateISODate(yyyy_MM_dd){
    var str = '' + yyyy_MM_dd;
    if(str.length != 10){
        return false;
    }
    if('-' != str.substr(4,1) || '-' != str.substr(7, 1)){
        return false;
    }
    
    var year = parseInt(str.substr(0, 4));
    var month = parseInt(str.substr(5, 2));
    var day = parseInt(str.substr(8, 2));
    
    if(isNaN(year) || isNaN(month) || isNaN(day)){
        return false;
    }

    var d = new Date(yyyy_MM_dd);

    if(year != d.getFullYear()){
        return false;
    }
    if(month != d.getMonth()+1){
        return false;
    }
    if(day != d.getDate()){
        return false;
    }
    return true;
}

/*
 * ISO 날짜형식 마스크
 */
function maskISODate(input){
    var text = $(input).val();
    
    text = text.replace(/-/gi, '');
    text = text.substr(0, 8);
    if(text.length >= 6){
        // yyyymmd
        text = text.substr(0, 4) + '-' + text.substr(4, 2) + '-' + text.substring(6);
    }else if(text.length >= 4){
        // yyyym
        text = text.substr(0, 4) + '-' + text.substring(4);
        
    } 
    $(input).val(text);
}
