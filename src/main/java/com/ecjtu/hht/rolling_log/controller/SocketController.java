package com.ecjtu.hht.rolling_log.controller;

import com.ecjtu.hht.rolling_log.service.ExecuteService;
import com.ecjtu.hht.rolling_log.service.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author hht
 * @date 2020/3/24 19:13
 */
@RestController
public class SocketController {

    @Autowired
    private ExecuteService executeService;

    @Autowired
    private WebSocketServer webSocketServer;

    @GetMapping("/socket/{cid}")
    public ModelAndView socket(@PathVariable String cid) {
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("cid", cid);
        return modelAndView;
    }

    @GetMapping("/socket/push/{cid}")
    public String pushToWeb(@PathVariable String cid, String message) {
        webSocketServer.sendCustomizeMessage(message, cid);
        return "推送成功";
    }

    /**
     * 模拟在执行脚本  然后推送日志给前端
     *
     * @return
     */
    @GetMapping("/execute/{sid}")
    public String execute(@PathVariable String sid) throws Exception {
        executeService.execute(sid);
        return "success";
    }
}
