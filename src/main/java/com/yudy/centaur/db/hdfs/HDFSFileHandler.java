package com.yudy.centaur.db.hdfs;

import org.apache.hadoop.fs.Path;

public interface HDFSFileHandler {
    void handler(Path path);
}
