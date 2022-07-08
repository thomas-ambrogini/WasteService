%====================================================================================
% wasteservice description   
%====================================================================================
mqttBroker("broker.hivemq.com", "1883", "unibo/ambrogini/wasteService").
context(ctxtransporttrolleyprototipo1, "127.0.0.1",  "TCP", "8051").
context(ctxwasteserviceprototipo1, "localhost",  "TCP", "8049").
 qactor( transporttrolley, ctxtransporttrolleyprototipo1, "external").
  qactor( container, ctxtransporttrolleyprototipo1, "it.unibo.container.Container").
  qactor( wasteservice, ctxwasteserviceprototipo1, "it.unibo.wasteservice.Wasteservice").
  qactor( wastetruck, ctxwasteserviceprototipo1, "it.unibo.wastetruck.Wastetruck").
