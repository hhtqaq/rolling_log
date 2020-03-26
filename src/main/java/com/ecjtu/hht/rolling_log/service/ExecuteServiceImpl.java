package com.ecjtu.hht.rolling_log.service;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import com.ecjtu.hht.rolling_log.util.SshUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author hht
 * @date 2020/3/26 17:23
 */
@Service
@Slf4j
public class ExecuteServiceImpl implements ExecuteService {

    @Autowired
    private WebSocketServer webSocketServer;

    /**
     * 执行脚本
     */
    @Override
    public void execute(String sid) throws Exception {
        Session sess = SshUtil.getSession();
        //执行命令
        // String cmd="tail -f "
        sess.execCommand("uname -a && date && uptime && who");

        System.out.println("Here is some information about the remote host:");
        for (int i = 0; i < 100; i++) {
            String resp1 = "我是第" + i + "次打印日志";
            log.info(resp1);
            webSocketServer.sendCustomizeMessage(resp1, sid);
            Thread.sleep(1000);
        }

    }
}
