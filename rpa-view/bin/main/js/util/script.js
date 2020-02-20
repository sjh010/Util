$(document).ready(function () {
    $('#sidebarCollapse').on('click', function () {
        $('#sidebar').toggleClass('active');
    });
    
    $('tbody').on('click', 'tr', function(event) {
        if (event.target.type !== 'checkbox') {
            $(':checkbox', this).trigger('click');
        }
    });

    $(".notification").on('click',function(){
        $(".message-pop").toggle();
        $(".set-pop").hide();
    });

    $(".setup").on('click',function(){
        $(".set-pop").toggle();
        $(".message-pop").hide();
    });
    
    $(".sidebarbtn").on('click', function(){
        $(".sidebar").toggle();
        $(".sidebarbtn").toggleClass('side-left');
        $("body").toggleClass('no-bg');
        $(".content").toggleClass("content-flip");
        $(".fixed_headers tfoot").toggleClass("tfoot-flip");
    });

    $.datepicker.setDefaults({
        dateFormat: 'yy-mm-dd',
        prevText: '이전 달',
        nextText: '다음 달',
        monthNames: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
        monthNamesShort: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
        dayNames: ['일', '월', '화', '수', '목', '금', '토'],
        dayNamesShort: ['일', '월', '화', '수', '목', '금', '토'],
        dayNamesMin: ['일', '월', '화', '수', '목', '금', '토'],
        showMonthAfterYear: true,
        yearSuffix: '년',
        showOn: 'button',
        //buttonText: '날짜 선택',
        buttonImageOnly: false,
        buttonImage: './images/img_calendar.svg'
    });
    
    $(".ui-datepicker-trigger").attr("title","날짜 선택");
    $(".ui-datepicker-trigger>img").removeAttr("alt");

    $('[title]').colorTip({color:'black'});
});

/*table in table - line */

$(function(){
  if ($('.tb-robot-list-body>tr').length == 1){
    $('.tb-robot-list-body>tr>td').css('border-top','none');
    $('.tb-robot-list-body>tr>td').css('border-bottom','none');
  } else {
    $('.tb-robot-list-body>tr>td').css('border-top','none');
    $('.tb-robot-list-body>tr:last-child td').css('border-bottom','none');
  }
})

/* selector ellipsis */
$(document).ready(function () {
  $('.select-container').each(function(){
   var $select = $(this).find('select'),
   $view = $(this).find('.select-viewport');

   $select.on('change',function(){
    $view.text( $(this).find('option:selected').text() )
  }).trigger('change');

 });        
});