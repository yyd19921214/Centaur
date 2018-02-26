package com.yudy.centaur;

import com.yudy.centaur.server.Server;
import com.yudy.centaur.server.ServerConfig;
import com.yudy.centaur.util.Constance;
import com.yudy.centaur.util.PropertiesUtil;
import org.apache.log4j.Logger;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class Centaur implements Closeable {

    private static final Logger LOG = Logger.getLogger(Centaur.class);

    private Server server;

    private volatile Thread shutdownHook;

    private int port = -1;


    public static void main(String[] args) {
        int argSize = args.length;
        String centaurConfigPath;
        String centaurLogPath;

        if (argSize <= 0 || (argSize == 1 && !args[0].equals("DEFAULT"))) {
            LOG.error("没有配置日志系统配置文件，系统正在退出!!!" + "如要使用默认参数，请使用DEFAULT参数");
            System.out.println("没有配置日志系统配置文件，系统正在退出!!!" + "如要使用默认参数，请使用DEFAULT参数");
            System.exit(1);
        }
        if (argSize == 1 && args[0].equals("DEFAULT")) {
            System.out.println("采用系统默认配置参数，在当前文件夹获取配置文件!!!");
            LOG.warn("采用系统默认配置参数，在当前文件夹获取配置文件!!!");
            centaurConfigPath = Constance.CENTAUR_CONFIG_PATH;
            centaurLogPath = Constance.CENTAUR_LOG4J_PATH;
        } else {
            centaurConfigPath = args[0];
            centaurLogPath = args[1];
            System.out.println("采用自定义配置参数文件!!!" + "centaurConfigPath的路径是:" + centaurConfigPath + "centaurLogPath的路径是:" + centaurLogPath);
            LOG.warn("采用自定义配置参数文件!!!" + "centaurConfigPath的路径是:" + centaurConfigPath + "centaurLogPath的路径是:" + centaurLogPath);
        }

        Centaur centaur = new Centaur();
        try {
            centaur.start(centaurConfigPath, centaurLogPath);

        } catch (IOException e) {
            System.err.println("启动主程序失败，原因如下" + e.getMessage());
            LOG.error("启动主程序失败，原因如下" + e.getMessage());
            e.printStackTrace();
        }
        centaur.awaitshutdow();
        centaur.close();


    }

    private void awaitshutdow() {
        try {
            server.awaitShutDown();
        } catch (InterruptedException e) {
            LOG.warn(e.getMessage(), e);
        }
    }

    private void start(String centaurConfigPath, String centaurLogPath) throws IOException {
        File centaurPropFile = new File(centaurConfigPath).getCanonicalFile();
        File log4jPropFile = new File(centaurLogPath).getCanonicalFile();
        if (!centaurPropFile.exists() || !centaurPropFile.isFile() || !log4jPropFile.exists() || !log4jPropFile.isFile()) {
            System.err.println("centaur.properties或log4j.properties不存在");
            LOG.error("centaur.properties或log4j.properties不存在");
            System.exit(2);
        }
        Properties prop = PropertiesUtil.loadConfigProperties(centaurPropFile);
        final ServerConfig config = new ServerConfig(prop);

        server = new Server(config);

        shutdownHook = new Thread(() -> {
            server.close();
            try {
                server.awaitShutDown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Runtime.getRuntime().addShutdownHook(shutdownHook);

        port = config.getPort();

        server.startup();


    }


    public synchronized void close() {
        if (shutdownHook != null) {
            Runtime.getRuntime().removeShutdownHook(shutdownHook);
            shutdownHook.run();
            shutdownHook = null;
            port = -1;
        }

    }

    public int getPort() {
        return this.port;
    }

    public void flush() {
        //TODO
    }
}
