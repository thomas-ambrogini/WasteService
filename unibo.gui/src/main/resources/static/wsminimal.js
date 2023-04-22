/*
wsminimal.js
*/

var socket;

function sendMessage(message) {
    var jsonMsg = JSON.stringify( {'name': message});
    socket.send(jsonMsg);
    console.log("Sent Message: " + jsonMsg);
}

function connect() {
    var host = document.location.host;
    var pathname = "/"                   //document.location.pathname;
    var addr = "ws://" + host + pathname + "socket";
    //alert("connect addr=" + addr   );

    // Assicura che sia aperta un unica connessione
    if (socket !== undefined && socket.readyState !== WebSocket.CLOSED) {
        alert("WARNING: Connessione WebSocket già stabilita");
    }
    socket = new WebSocket(addr);

    socket.onopen = function (event) {
        //console.log("Connected to " + addr);
        setMessageToWindow(infoDisplay, "Connected to " + addr);
    };

    socket.onmessage = function (event) {
        //alert(`Got Message: ${event.data}`);
        msg = event.data;
        //alert(`Got Message: ${msg}`);
        console.log("ws-status:" + msg);

        handlePayload(msg)
//        else setMessageToWindow(infoDisplay, msg); //""+`${event.data}`*/
    };
}


function handlePayload(msg) {
    payload = msg.substring(msg.indexOf('(') + 1, msg.indexOf(')'))
    if (msg.includes("trolleyState")) setMessageToWindow(trolleyStateDisplay, payload);
    else if (msg.includes("ledState")) setMessageToWindow(ledStateDisplay, payload);
    else if (msg.includes("trolleyPosition")) setMessageToWindow(trolleyPositionDisplay, payload);
    else if (msg.includes("glass")) {
        setMessageToWindow(glassBoxDisplay, payload);
        payload_plastic = msg.substring(msg.indexOf(')') + 1)
        payload_plastic = payload_plastic.substring(payload_plastic.indexOf('(') + 1, payload_plastic.indexOf(')'))
        setMessageToWindow(plasticBoxDisplay, payload_plastic);
    }


}


