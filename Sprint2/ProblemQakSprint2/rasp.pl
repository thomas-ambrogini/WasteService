%====================================================================================
% rasp description   
%====================================================================================
context(ctx_transporttrolley, "transporttrolley",  "TCP", "8051").
context(ctx_rasp, "localhost",  "TCP", "8056").
 qactor( sonarsimulator, ctx_rasp, "sonarSimulator").
  qactor( sonardatasource, ctx_rasp, "sonarHCSR04Support2021").
  qactor( datacleaner, ctx_rasp, "dataCleaner").
  qactor( ledsimulator, ctx_rasp, "ledSimulator").
  qactor( ledconcrete, ctx_rasp, "ledConcrete").
  qactor( led, ctx_rasp, "it.unibo.led.Led").
  qactor( sonarqak22, ctx_rasp, "it.unibo.sonarqak22.Sonarqak22").
  qactor( ledcontroller, ctx_rasp, "it.unibo.ledcontroller.Ledcontroller").
