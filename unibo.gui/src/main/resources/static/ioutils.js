/*
ioutils.js
*/

const trolleyPositionDisplay  = document.getElementById("trolleyPosition");
const ledStateDisplay         = document.getElementById("ledStatus");
const trolleyStateDisplay     = document.getElementById("trolleyStatus");

const glassBoxDisplay         = document.getElementById("glassBox");
const plasticBoxDisplay       = document.getElementById("plasticBox");

const sonarDisplay            = document.getElementById("sonarStatus")


function setMessageToWindow(outfield, message) {
    let output = message.replace("\n","<br/>")
    outfield.innerHTML = `<tt>${output}</tt>`
}

$(function () {
    $( "#activateSonar" ).click(function() { callServerUsingAjax("activate"); setMessageToWindow(sonarDisplay, "On") });  //callServerUsingAjax is in wsminimal
    $( "#deactivateSonar" ).click(function() { callServerUsingAjax("deactivate"); setMessageToWindow(sonarDisplay,"Off")});  //callServerUsingAjax is in wsminimal
});

function callServerUsingAjax(message) {
    //alert("callServerUsingAjax "+message)
    $.ajax({
        //imposto il tipo di invio dati (GET O POST)
        type: "POST",
        //Dove  inviare i dati
        url: "sonar",
        //Dati da inviare
        data: "state=" + message,
        dataType: "html",
        //Visualizzazione risultato o errori
        success: function(msg){  //msg ha tutta la pagina ...
            console.log("call msg="+msg);
            //setMessageToWindow(infoDisplay,message+" done")
        },
        error: function(){
            alert("Chiamata fallita, si prega di riprovare...");
            //sempre meglio impostare una callback in caso di fallimento
        }
    });
}

