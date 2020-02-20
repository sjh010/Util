/**
 * Popup Layer Load
 * 
 * @param obj		
 * @param options 	{left:pixel top:pixel}
 * @returns
 */
function showBlockLayer(obj, options) {
	options = options || {};

	var width = $(obj).width();
	var height = $(obj).height();
	var windowHeight = window.document.body.offsetHeight;
	var scrollPos = $(document).scrollTop();

	var left = 0;
	if (options.left) {
		left = options.left;
	} else {
		left = ($(document).width() - width) / 2;
		left = (left > 0) ? left : 0;
	}
	
	var top = 0;
	if (options.top) {
		top = options.top;
	} else {
		top = (windowHeight - height) / 2 + scrollPos;
	}
	
	if (options.addTop) {
		top -= options.addTop
	}
	
	$(obj).bPopup({
		modalClose: false,
	    opacity: 0.6,
	    followSpeed: 'fast', //can be a string ('slow'/'fast') or int
	    follow: [false, false], 
	    position: [left, top],
	    escClose: false
	});
}

/**
 * URL을 로드한 후, 레이어 팝업을 생성
 * @param obj
 * @param url
 * @param options
 */
function showBlockLayerWithUrl(obj, url, options) {
	$(obj).empty();
	$(obj).load(url, function() {
		options = options || {};

		var width = $(obj).width();
		var height = $(obj).height();
		var windowHeight = window.document.body.offsetHeight;
		var scrollPos = 0;//$(document).scrollTop();
		
		var left = 0;
		if (options.left) {
			left = options.left;
		} else {
			left = ($(document).width() - width) / 2;
			left = (left > 0) ? left : 0;
		}
		
		var top = 0;
		if (options.top) {
			top = options.top;
		} else {
			top = (windowHeight - height) / 2 + scrollPos;
		}
		
		if (options.addTop) {
			top -= options.addTop
		}
		
		$(obj).bPopup({
			modalClose: false,
		    opacity: 0.6,
		    followSpeed: 'fast', //can be a string ('slow'/'fast') or int
		    follow: [false, false], 
		    position: [left, top],
		    escClose: false
		});
	});
}

/**
 * 화면 중앙에 레이어 팝업을 생성
 * 
 * @param obj
 * @returns
 */
function showFixedBlockLayer(obj) {
    $(obj).bPopup({
        modalClose: false,
        opacity: 0.6,
        followSpeed: 'fast', //can be a string ('slow'/'fast') or int
        follow: [false, false], 
        position: ['auto', 'auto'],
        escClose: false
    });
}

/**
 * URL을 로드한 후, 화면 중앙에 레이어 팝업을 생성
 * 
 * @param obj
 * @returns
 */
function showFixedBlockLayerWithUrl(obj, url) {
    $(obj).empty();
    $(obj).load(url, function() {
        $(obj).bPopup({
            modalClose: false,
            opacity: 0.6,
            followSpeed: 'fast', //can be a string ('slow'/'fast') or int
            follow: [false, false], 
            position: ['auto', 'auto'],
            escClose: false
        });
    });
}

/**
 * Popup Layer Close
 * 
 * @param obj
 * @returns
 */
function hideBlockLayer(obj) {
	$(obj).bPopup().close();
}

function showOverlayDiv() {
    
}


function hideOverlayDiv() {
    
}

/**
 * 페이지 넘버링 설정
 * @param pageNo:현재페이지
 * @param totalCount:전체 항목 수
 * @param pageSize:페이지당 항목 수
 * @returns
 */
function setPaging(id, pageNo, totalCount, pageSize) { 
    var _pageSize = pageSize ? pageSize : 10;
    makePageNumbering(pageNo, totalCount, _pageSize, 10, "movePage", id);
} 

/**
 * 페이지 넘버링 설정
 * @param pageNo:현재페이지
 * @param totalCount:전체 항목 수
 * @param pageSize:페이지당 항목 수
 * @returns
 */
function setPopupPaging(id, pageNo, totalCount, pageSize) { 
    var _pageSize = pageSize ? pageSize : 10;
    makePageNumbering(pageNo, totalCount, _pageSize, 10, "movePopupPage", id, "popup");
} 

/**
 * 페이지 넘버링 설정 :  < 11 12 13 14 15 16 17 18 19 20 > 
 * 
 * @param pageNo : 현재페이지
 * @param totalCount : 전체 항목 수
 * @param pageSize : 페이지당 항목 수
 * @param pageBlock : 한번에 보여지는 최대 페이지 수
 * @param fn : 페이징 처리 함수명
 * @param id : 페이지 표시 div id
 * @param type : 화면타입 메인화면or팝업레이어
 * @returns
 */
function makePageNumbering(pageNo, totalCount, pageSize, pageBlock, fn, id, type) { 
	var totalPageCount = toInt(totalCount / pageSize) + (totalCount % pageSize > 0 ? 1 : 0);
	var totalBlockCount = toInt(totalPageCount / pageBlock) + (totalPageCount % pageBlock > 0 ? 1 : 0);
	var blockNo = toInt(pageNo / pageBlock) + (pageNo % pageBlock > 0 ? 1 : 0);
	var startPageNo = (blockNo - 1) * pageBlock + 1;
	var endPageNo = blockNo * pageBlock;
	//console.log(totalPageCount + " / " + totalBlockCount + " / " + blockNo + " / " + startPageNo + " / " + endPageNo);

	if (endPageNo > totalPageCount) {
		endPageNo = totalPageCount;
	}
	var prevBlockPageNo = (blockNo - 1) * pageBlock;
	var nextBlockPageNo = blockNo * pageBlock + 1;
	
	var htmlStr = '';
	if (type && type == "popup") {
	    htmlStr = '<ul class="pop-tfoot">';
	} else {
	    htmlStr = '<ul class="tfoot">';
	}
	if (prevBlockPageNo > 0) {
	    htmlStr += '<li><a href="javascript:' + fn + '(' + prevBlockPageNo + ');"><span class="glyphicon glyphicon-chevron-left"></span></a></li>';
	}
	for (var i = startPageNo; i <= endPageNo; i++) {
		if (i == pageNo) {
		    htmlStr += '<li style="color: #ffffff;">'+ i +'</li>';
		} else {
		    htmlStr += '<li><a href="javascript:' + fn + '(' + i + ');">' + i + '</a></li>';
		}
	}
	if (nextBlockPageNo <= totalPageCount) {
	    htmlStr += '<li><a href="javascript:' + fn + '(' + nextBlockPageNo + ');"><span class="glyphicon glyphicon-chevron-right"></span></a></li>';
	}
	htmlStr += '</ul>';
	
	$('#'+id).html(htmlStr);
}

function getPagingText(pageNo, count, pageRowCount){
    pageNo = parseInt(pageNo);
    pageRowCount = parseInt(pageRowCount);
    
    
    var indexStart = ((pageNo - 1) * pageRowCount) + 1;
    var indexEnd = Math.min(pageNo * pageRowCount, count);
    var text = indexStart + '-' +indexEnd + '/' + count;
    return text;
}

function setMenuTab(menu) {
    $("#sidebar .components li").removeClass("active");
    $("#sidebar .components li").filter(function() {
        if($(this).data("menu") == menu) {
            $(this).addClass("active");
            if($(this).parent().attr("id") == "homeSubmenu") {
                $("#homeSubmenu").collapse();
            }
            $("#navbar .nav-title").text($(this).find("a").text());
        }
    });
}
