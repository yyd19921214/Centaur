package com.yudy.centaur.http;

//import org.slf4j.Logger;

import com.yudy.centaur.util.Constance;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class HTTPUtils {

    private static Logger LOG=Logger.getLogger(HTTPUtils.class);

    public static void process(String request, OutputStream out){
        String uri=parseUri(request);
        if (StringUtils.isEmpty(uri)){
//
        }

    }

    public static void send404(OutputStream out) throws IOException {
        if (out==null)
            return;
        String errorMsg= "HTTP/1.1 404 File Not Found\r\n"
                + "Content-Type: text/html\r\n" + "Content-Length: 23\r\n"
                + "\r\n" + "<h1>Nothing Found! 404 </h1>";
        out.write(errorMsg.getBytes());
    }

    public static void send404(SocketChannel socketChannel) throws IOException {
        if (socketChannel==null)
            return;;
        String errorMsg= "HTTP/1.1 404 File Not Found\r\n"
                + "Content-Type: text/html\r\n" + "Content-Length: 23\r\n"
                + "\r\n" + "<h1>Nothing Found! 404 </h1>";

        socketChannel.write(ByteBuffer.wrap(errorMsg.getBytes()));

    }





    public static String parseUri(String requestString){
        if (StringUtils.isEmpty(requestString))
            return null;
        int idx1,idx2;
        idx1=requestString.indexOf(' ');
        if (idx1!=-1){
            idx2 = requestString.indexOf(' ',idx1+1);
            if (idx2>idx1)
                return requestString.substring(idx1+1,idx2);
        }
        return null;
    }

    public static void sendHTTPHeader(OutputStream outputStream) throws IOException {
        if (outputStream==null)
            return;
        StringBuffer sb=new StringBuffer("HTTP/1.1 200 OK\r\n");
        sb.append("Content-Type:text/html;UTF-8\r\n");
        sb.append("\r\n");

        byte[] httpHeader=sb.toString().getBytes();
        outputStream.write(httpHeader);
    }

    public static void sendStaticResource(OutputStream outputStream,String uri) throws IOException {
        if (StringUtils.isEmpty(uri)){
            send404(outputStream);
            return;
        }
        File file=new File(Constance.WEB_ROOT,uri);
        FileInputStream fis=null;
        byte[] bytes=new byte[1024];
        try {
            fis=new FileInputStream(file);
            sendHTTPHeader(outputStream);
            int ch=fis.read(bytes,0,1024);
            while (ch!=-1){
                outputStream.write(bytes,0,ch);
                ch=fis.read(bytes,0,1024);
            }

        }finally {
            if (fis!=null)
                fis.close();
        }



    }

    public static void process(String uri,SocketChannel socket) throws IOException {
        if (StringUtils.isEmpty(uri)){
            send404(socket);
            return;
        }

        String servletName=uri.substring(uri.lastIndexOf("/")+1);
        LOG.info("客户端请求地址是:" + servletName);

//        if (uri.equals("/monitor/html"))
            








    }


}
