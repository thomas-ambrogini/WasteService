%====================================================================================
% transporttrolley description   
%====================================================================================
mqttBroker("broker.hivemq.com", "1883", "unibo/ambrogini/wasteService").
context(ctxbasicrobot, "127.0.0.1",  "TCP", "8020").
context(ctxtransporttrolleyprototipo1, "localhost",  "TCP", "8051").
 qactor( pathexec, ctxbasicrobot, "external").
  qactor( transporttrolley, ctxtransporttrolleyprototipo1, "it.unibo.transporttrolley.Transporttrolley").
  qactor( pathfinder, ctxtransporttrolleyprototipo1, "it.unibo.pathfinder.Pathfinder").
