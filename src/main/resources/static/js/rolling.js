function socketConnect() {


    var sid = Date.parse(new Date());

    var socket = new WebSocket("ws://localhost:8081/websocket/" + sid);

    socket.onopen = function () {
        console.log("socket open");
        socket.send("socket open");
    };

    socket.onmessage = function (message) {
        $("#rolling").append(message.data);
        console.log(message.data);
    };

    socket.onclose = function () {
        console.log("closed...");
    };

    socket.onerror = function () {
        console.log(this.readyState);
    };
}

