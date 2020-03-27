package com.ecjtu.hht.rolling_log.service;

/**
 * @author hht
 * @date 2020/3/26 17:22
 */
public interface ExecuteService {
    /**
     * 执行脚本
     */
    void execute(String sid) throws Exception;

    /**
     * 查看最新日志
     */
    void tailLog(String sid) throws Exception;

    /**
     * 查看windows最新日志
     */
    void tailWindowsLog(String sid) throws Exception;
}
