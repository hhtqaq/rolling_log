package com.ecjtu.hht.rolling_log.util;

import ch.ethz.ssh2.Session;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * @author hht
 * @date 2020/3/27 10:44
 */
@SpringBootTest
public class SshUtilTest {

    @Test
    public void testConnect() throws IOException {
        Session session = SshUtil.getSession();
    }

    @Test
    public void testTailLog() throws IOException {
        Session session = SshUtil.getSession();
        String logHome = "/ms_log/configserver-1/";
        String cmd = "tail -f " + logHome + "log_2020-03-27.0.log";
        session.execCommand(cmd);
        InputStream stdout = session.getStdout();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stdout, StandardCharsets.UTF_8));
        String log = null;
        while ((log = reader.readLine()) != null) {
            System.out.println(log);
        }
    }
}
