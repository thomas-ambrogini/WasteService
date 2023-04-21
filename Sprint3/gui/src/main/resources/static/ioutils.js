/*
ioutils.js
*/

const trolleyPositionDisplay  = document.getElementById("trolleyPosition");
const ledStateDisplay         = document.getElementById("ledStatus");
const trolleyStateDisplay     = document.getElementById("trolleyStatus");

const glassBoxDisplay         = document.getElementById("glassBox");
const plasticBoxDisplay       = document.getElementById("plasticBox");


function setMessageToWindow(outfield, message) {
    let output = message.replace("\n","<br/>")
    outfield.innerHTML = `<tt>${output}</tt>`
}

