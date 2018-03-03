package com.yudy.centaur.http;

import com.yudy.centaur.util.Constance;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.IOException;

public class HTTPResult {

    public static byte[] get404Result(){
        String errorMessage = "HTTP/1.1 404 File Not Found\r\n"
                + "Content-Type: text/html\r\n" + "Content-Length: 23\r\n"
                + "\r\n" + "<h1>Nothing Found! 404 </h1>";
        return errorMessage.getBytes();
    }

    public static byte[] getHTTPHeader(){
        StringBuffer sb = new StringBuffer("HTTP/1.1 200 OK\r\n");
        sb.append("Content-Type:text/html;UTF-8\r\n");
        sb.append("\r\n");
        return sb.toString().getBytes();
    }

    public static byte[] getUriResult(String uri) throws IOException {
        if (StringUtils.isEmpty(uri))
            return get404Result();
        File file=new File(Constance.WEB_ROOT,uri);
        byte[] fileBytes=FileUtils.readFileToByteArray(file);
        return fileBytes;
    }


}
