%====================================================================================
% wasteservice description   
%====================================================================================
mqttBroker("broker.hivemq.com", "1883", "unibo/ambrogini/wasteService").
context(ctxwasteserviceprototipo1, "localhost",  "TCP", "8049").
 qactor( wasteservice, ctxwasteserviceprototipo1, "it.unibo.wasteservice.Wasteservice").
  qactor( transporttrolley, ctxwasteserviceprototipo1, "it.unibo.transporttrolley.Transporttrolley").
  qactor( wastetruck, ctxwasteserviceprototipo1, "it.unibo.wastetruck.Wastetruck").
