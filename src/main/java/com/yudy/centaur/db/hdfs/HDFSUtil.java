package com.yudy.centaur.db.hdfs;

import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.fs.FileSystem;
import org.apache.log4j.Logger;

import java.io.*;
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

    public void traverse(FileSystem fs, String file, PathFilter pathFilter, HDFSFileHandler hdfsFileHandler) throws IOException {
        Path path = new Path(file);
        _traverse(fs, path, pathFilter, hdfsFileHandler);
        fs.close();

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

    public static boolean mkdirs(String dir) throws IOException {
        boolean flag = false;
        FileSystem fs;
        Path path;
        fs = FileSystem.get(URI.create("/"), conf);
        path = new Path(dir);
        if (!fs.exists(path)) {
            if (!fs.mkdirs(path)) {
                LOG.error("新建文件夹 " + path.toString() + "失败!");
                return flag;
            }
        } else {
            LOG.info("文件夹" + dir + "已经存在");
            return flag;
        }
        flag = true;
        fs.close();
        return flag;
    }

    public static boolean rm(String file, boolean recursive) throws IOException {
        boolean flag = false;
        FileSystem fs;
        Path path;
        fs = FileSystem.get(URI.create("/"), conf);
        path = new Path(file);
        if (!fs.delete(path, recursive)) {
            LOG.error("删除文件" + file + "失败！");
            return flag;
        }
        flag = true;
        fs.close();
        return flag;
    }


    public static String cat(String file) throws IOException {
        FileSystem fs = FileSystem.get(URI.create("/"), conf);
        Path path = new Path(file);
        if (!fs.exists(path))
            return null;
        if (fs.isFile(path))
            return null;
        FSDataInputStream is = null;
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {

            is = fs.open(path);
            IOUtils.copy(is, os);
        } finally {
            fs.close();
            is.close();
            os.close();
        }
        return os.toString();
    }

    public static boolean copyFromLocal(String src, String dst, boolean delSrc, boolean overwrite) throws IOException {
        boolean flag = false;
        FileSystem fs = null;
        try {
            fs = FileSystem.get(URI.create("/"), conf);
            File file = new File(src);
            if (!file.exists()) {
                LOG.error(src + "源文件不存在!");
                return flag;
            }
            Path dstPath = new Path(dst);
            fs.copyFromLocalFile(delSrc, overwrite, new Path(src), dstPath);
            flag = true;
        } finally {
            fs.close();
        }
        return flag;
    }

    public static boolean appendStr2File(String srcFile, String appendStr) throws IOException {
        conf.set("dfs.client.block.write.replace-datanode-on-failure.policy", "NEVER");
        conf.set("dfs.client.block.write.replace-datanode-on-failure.enable", "true");

        FileSystem fs = null;
        FSDataOutputStream out = null;
        InputStream in = null;
        try {
            fs = FileSystem.get(URI.create("/"), conf);
            out = fs.append(new Path(srcFile));
            appendStr += "\r\n";
            in = new BufferedInputStream(new ByteArrayInputStream(appendStr.getBytes()));
            IOUtils.copy(in, out);

        } finally {
            out.close();
            in.close();
            fs.close();

        }
        return true;
    }

}
