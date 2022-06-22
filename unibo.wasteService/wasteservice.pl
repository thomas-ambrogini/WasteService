%====================================================================================
% wasteservice description   
%====================================================================================
mqttBroker("broker.hivemq.com", "1883", "unibo/ambrogini/wasteService").
context(ctxwasteservice, "localhost",  "TCP", "8049").
 qactor( led, ctxwasteservice, "it.unibo.led.Led").
  qactor( wastetruck, ctxwasteservice, "it.unibo.wastetruck.Wastetruck").
  qactor( wasteservice, ctxwasteservice, "it.unibo.wasteservice.Wasteservice").
  qactor( transportrolley, ctxwasteservice, "it.unibo.transportrolley.Transportrolley").
  qactor( pathfinder, ctxwasteservice, "it.unibo.pathfinder.Pathfinder").
