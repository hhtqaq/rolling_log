package com.ecjtu.hht.rolling_log.service;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import com.ecjtu.hht.rolling_log.util.SshUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author hht
 * @date 2020/3/26 17:23
 */
@Service
@Slf4j
public class ExecuteServiceImpl implements ExecuteService {


    @Autowired
    private WebSocketServer webSocketServer;

    @Value("${log.home}")
    private String logHome;

    /**
     * 执行脚本
     */
    @Override
    public void execute(String sid) throws Exception {

        new Thread() {
            @Override
            @SneakyThrows
            public void run() {
                tailWindowsLog(sid);
            }
        }.start();

        new Thread() {
            @SneakyThrows
            @Override
            public void run() {
                log.info("任务：xxx  start");
                for (int i = 0; i < 1000; i++) {
                    String resp1 = "我是第" + i + "次打印日志";
                    log.info(resp1);
                    Thread.sleep(100);
                }
                log.info("任务：xxx  end");
            }
        }.start();
    }

    /**
     * 查看最新linux日志
     */
    @Async
    @Override
    public void tailLog(String sid) throws Exception {
        Session session = SshUtil.getSession();
        //执行命令
        String cmd = "tail -f " + logHome + "catalina.log";
        session.execCommand(cmd);
        InputStream stdout = session.getStdout();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stdout, StandardCharsets.UTF_8));
        sendMessage(sid, reader);
    }

    /**
     * 查看最新linux日志    需要配合 tail.exe
     */
    @Async
    @Override
    public void tailWindowsLog(String sid) throws Exception {
        String cmd = "tail -f " + logHome + "catalina.log";
        Process process = Runtime.getRuntime().exec(cmd);
        InputStream inputStream = process.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        sendMessage(sid, reader);
        process.waitFor();
        inputStream.close();
    }

    /**
     * 发送websocket信息
     *
     * @param sid
     * @param reader
     * @throws IOException
     */
    private void sendMessage(String sid, BufferedReader reader) throws IOException {
        String log = null;
        boolean task = false;
        while ((log = reader.readLine()) != null && !task) {
            if (log.contains("xxx  start")) {
                task = true;
                webSocketServer.sendCustomizeMessage("xxx  start 正在获取日志信息:", sid);
            }
        }
        while ((log = reader.readLine()) != null && task) {
            webSocketServer.sendCustomizeMessage(log, sid);
            if (log.contains("xxx  end")) {
                task = false;
            }
        }
    }
}
