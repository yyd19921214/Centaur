package com.yudy.centaur.db.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URI;
//import java.nio.file.FileSystem;

public class HDFSUtil {

    private HDFSUtil() {

    }

    private static Logger LOG = Logger.getLogger(HDFSUtil.class);

    private static Configuration conf;

    static {
        conf = new Configuration();
        //TODO
    }

    public static boolean isDirExist(String fileName) throws IOException {
        FileSystem fs = FileSystem.get(URI.create("/"), conf);
        Path p = new Path(fileName);
        try {
            // 存在并且是文件夹
            if (fs.exists(p) && fs.isDirectory(p)) {
                return true;
            } else {
                return false;
            }
        } finally {
            fs.close();
        }
    }

    public static boolean isFileExist(String fileName) throws Exception {
        FileSystem fs = FileSystem.get(URI.create("/"), conf);
        Path p = new Path(fileName);
        try {
            // 存在并且不是文件夹
            if (fs.exists(p) && !fs.isDirectory(p)) {
                return true;
            } else {
                return false;
            }
        } finally {
            fs.close();
        }
    }

    public static boolean createFile(String file, String content, boolean overwrite) throws IOException {
        boolean flag;
        FileSystem fs;
        FSDataOutputStream os = null;
        try {
            fs = FileSystem.get(URI.create("/"), conf);
            byte[] buff = content.getBytes();
            os = fs.create(new Path(file), overwrite);
            os.write(buff, 0, buff.length);
            flag = true;
        } finally {
            if (os != null)
                os.close();
        }
        return flag;
    }

    public void traverse(FileSystem fs, String file, PathFilter pathFilter, HDFSFileHandler hdfsFileHandler) {
        Path path = new Path(file);

    }

    private void _traverse(FileSystem fs, Path path, PathFilter pathFilter, HDFSFileHandler hdfsFileHandler) throws IOException {
        if (fs.isFile(path))
            hdfsFileHandler.handler(path);
        else {
            FileStatus[] list = fs.listStatus(path, pathFilter);
            for (FileStatus status : list)
                _traverse(fs, status.getPath(), pathFilter, hdfsFileHandler);
        }

    }
}
