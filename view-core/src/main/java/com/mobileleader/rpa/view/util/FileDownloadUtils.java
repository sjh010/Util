package com.mobileleader.rpa.view.util;

import com.mobileleader.rpa.view.exception.RpaViewError;
import com.mobileleader.rpa.view.exception.RpaViewException;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FileDownloadUtils {

    /**
     * 파일 다운로드 헤더 설정.
     *
     * @param request request
     * @param response response
     * @param fileName 파일명
     */
    public static void setFileDownloadHeader(HttpServletRequest request, HttpServletResponse response,
            String fileName) {

        String browser = getBrowser(request);

        String encodedFilename = null;

        try {
            if (browser.equals("MSIE")) {
                encodedFilename = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
            } else if (browser.equals("Firefox")) {
                encodedFilename = "\"" + new String(fileName.getBytes("UTF-8"), "8859_1") + "\"";
            } else if (browser.equals("Opera")) {
                encodedFilename = "\"" + new String(fileName.getBytes("UTF-8"), "8859_1") + "\"";
            } else if (browser.equals("Chrome")) {
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < fileName.length(); i++) {
                    char c = fileName.charAt(i);
                    if (c > '~') {
                        sb.append(URLEncoder.encode("" + c, "UTF-8"));
                    } else {
                        sb.append(c);
                    }
                }
                encodedFilename = sb.toString();
            } else {
                throw new RpaViewException(RpaViewError.INTERNAL_SERVER_ERROR, "Not Suppoted Browser");
            }
        } catch (UnsupportedEncodingException e) {
            throw new RpaViewException(RpaViewError.INTERNAL_SERVER_ERROR, "Not Supported Encoding");
        }

        response.setHeader("Content-Disposition", "attachment;filename=\"" + encodedFilename + "\"");
    }

    /**
     * 파일 write.
     *
     * @param response response
     * @param data 파일(byte array)
     */
    public static void writeBinaryResponse(HttpServletResponse response, byte[] data) {
        try (OutputStream os = response.getOutputStream()) {
            os.write(data);
            os.flush();
        } catch (IOException e) {
            throw new RpaViewException(RpaViewError.INTERNAL_SERVER_ERROR, "File I/O Exception");
        }
    }

    private static String getBrowser(HttpServletRequest request) {

        String userAgent = request.getHeader("User-Agent");

        if (userAgent.indexOf("MSIE") > -1 || userAgent.indexOf("Trident") > -1) {
            // IE 버전 별 체크 >> Trident/7.0(IE 11), Trident/6.0(IE 10) , Trident/5.0(IE 9) , Trident/4.0(IE 8)
            return "MSIE";
        } else if (userAgent.indexOf("Chrome") > -1) {
            return "Chrome";
        } else if (userAgent.indexOf("Opera") > -1) {
            return "Opera";
        } else if (userAgent.indexOf("Firefox") > -1) {
            return "Firefox";
        }
        return "Safari";
    }
}
