package com.ecjtu.hht.rolling_log.util;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;

import java.io.IOException;

/**
 * ssh 工具类
 *
 * @author hht
 * @date 2020/3/26 18:13
 */
public class SshUtil {
    /**
     * 远程机器IP
     */
    private static String hostname = "10.10.1.86";
    /**
     * 登录用户名
     */
    private static String username = "root";
    /**
     * 登录密码
     */
    private static String password = "123456";

    /**
     * 获取会话
     *
     * @return
     * @throws IOException
     */
    public static Session getSession() throws IOException {
        Connection connection = new Connection(hostname, 22);
        connection.connect();
        boolean isAuthenticated = connection.authenticateWithPassword(username, password);
        ///是否登录成功
        if (!isAuthenticated) {
            throw new IOException("Authentication failed.");
        }
        return connection.openSession();
    }
}
