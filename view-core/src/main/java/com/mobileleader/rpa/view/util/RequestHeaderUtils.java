package com.mobileleader.rpa.view.util;

import javax.servlet.http.HttpServletRequest;

public class RequestHeaderUtils {

    public static boolean isAjaxRequest(HttpServletRequest request) {
        return "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
    }
}
