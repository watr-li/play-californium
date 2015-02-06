var ws = new WebSocket("ws://localhost:9000/websocket");
ws.onmessage = function( message ) {
    console.log(message);
    document.getElementById('coapMessages').innerHTML += "<p>" + message.data + "</p>";
};

ws.onopen = function() {
    ws.send('foobar');
}


