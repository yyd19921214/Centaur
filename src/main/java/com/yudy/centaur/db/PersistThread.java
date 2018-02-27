package com.yudy.centaur.db;

import com.alibaba.fastjson.JSON;
import com.yudy.centaur.log.LogConfig;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;


public class PersistThread<T> extends Thread{
    private static Logger LOG=Logger.getLogger(PersistThread.class);
    private final BlockingQueue<T> mq;
    private final List<T> tempList;
    private final LogConfig config;
    private final AtomicInteger logIndex=new AtomicInteger(0);
    private int logMaxSize;

    public PersistThread(BlockingQueue<T> mq,List<T> tempList,LogConfig logConfig){
        this.mq=mq;
        this.tempList=tempList;
        this.config=logConfig;
        this.logMaxSize=config.getMaxSize();
    }

    @Override
    public void run() {
        File file=null;
        OutputStream out=null;
        try {
            file=new File(config.getDir()).getCanonicalFile();
            out=new FileOutputStream(file,true);
            for(int i=0;i<tempList.size();i++){
                out=isFile2Big(file,logMaxSize,out);
                T logData=tempList.get(i);
                byte[] bys= JSON.toJSONBytes(logData);
                out.write(bys);
                tempList.remove(i);
            }
            tempList.clear();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
            LOG.error("将mq中日志写入到文件出错，正在将剩下的日志，大小为:" + tempList.size() + "写回到mq"
                    + e.getMessage(), e);
            mq.addAll(tempList);
        }
    }

    private OutputStream isFile2Big(File file,int logMaxSize,OutputStream out){
        long fileLength=file.length();
        long maxFileSize=1024L*1024*1024*logMaxSize;
        if(fileLength>=maxFileSize){
            String filePath=file.getAbsolutePath()+logIndex.getAndIncrement();
            file=new File(filePath);
            LOG.warn("持久化日志文件" + file.getAbsolutePath() + "过大，生成新的日志文件"
                    + filePath);
            OutputStream output=null;
            try {
                output=new FileOutputStream(file,true);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }finally {
                if(out!=null)
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
            return output;
        }else{
            return out;
        }
    }
}
