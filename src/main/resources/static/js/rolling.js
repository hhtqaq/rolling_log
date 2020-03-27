/**
 * 页面加载的时候建立socket连接
 */
$(function () {

    let sid = localStorage.getItem("sid");
    if (!sid) {
        sid = Date.parse(new Date());
        localStorage.setItem("sid", sid);
    }
    const socket = new WebSocket("ws://localhost:8081/websocket/" + sid);

    socket.onopen = function () {
        console.log("socket open");
        // socket.send("socket open");
    };
    socket.onmessage = function (message) {
        let $container = $("#container");
        let $rolling = $("#rolling");
        $container.append(message.data).append("</br>");
        $container.scrollTop($container.height() + $container.scrollTop());
        console.log(message.data);
    };

    socket.onclose = function () {
        console.log("closed...");
    };

    socket.onerror = function () {
        console.log(this.readyState);
    };
});

const rolling = {};

/**
 * 执行
 */
rolling.execute = function () {
    let sid = localStorage.getItem("sid");
    $.get("/execute/" + sid);
};



