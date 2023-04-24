/*
ioutils.js
*/

const typeSelect              = document.getElementById("type")
const quantityText            = document.getElementById("quantity")

const replyDisplay            = document.getElementById("reply")
const loadStateDisplay            = document.getElementById("loadState")


function setMessageToWindow(outfield, message) {
    let output = message.replace("\n","<br/>")
    outfield.innerHTML = `<tt>${output}</tt>`
}

// $(function () {
//     $( "#depositButton").click(function () { callServerUsingAjax(typeSelect.value, quantityText.value)})
// });

function callServerUsingAjax(type, quantity) {
    //alert("callServerUsingAjax "+message)
    $.ajax({
        //imposto il tipo di invio dati (GET O POST)
        type: "POST",
        //Dove  inviare i dati
        url: "deposit",
        //Dati da inviare
        data: {
            type: type,
            quantity: quantity
        },
        dataType: "html",

        //Visualizzazione risultato o errori
        success: function(msg){  //msg ha tutta la pagina ...
            console.log("call msg="+msg);
            setMessageToWindow(replyDisplay,msg)
        },
        error: function(){
            alert("Chiamata fallita, si prega di riprovare...");
            //sempre meglio impostare una callback in caso di fallimento
        }
    });
}

