function socketConnect(sid) {

    var socket = new WebSocket("ws://localhost:8080/websocket/" + sid);

    socket.onopen = function () {
        console.log("socket open");
        socket.send("socket open");
    };

    socket.onmessage = function (message) {
        console.log(message.data);
    };

    socket.onclose = function () {
        console.log("closed...");
    };

    socket.onerror = function () {
        console.log(this.readyState);
    };
}

