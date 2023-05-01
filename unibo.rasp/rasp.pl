%====================================================================================
% rasp description   
%====================================================================================
context(ctx_basicrobot, "robot",  "TCP", "8020").
context(ctx_wasteservice, "wasteservice",  "TCP", "8049").
context(ctx_transporttrolley, "transporttrolley",  "TCP", "8051").
context(ctx_rasp, "localhost",  "TCP", "8056").
 qactor( sonarsimulator, ctx_rasp, "sonarSimulator").
  qactor( sonardatasource, ctx_rasp, "sonarHCSR04Support2021").
  qactor( datacleaner, ctx_rasp, "dataCleaner").
  qactor( ledsimulator, ctx_rasp, "ledSimulator").
  qactor( ledconcrete, ctx_rasp, "ledConcrete").
  qactor( basicrobot, ctx_basicrobot, "it.unibo.basicrobot.Basicrobot").
  qactor( pathexec, ctx_basicrobot, "it.unibo.pathexec.Pathexec").
  qactor( transporttrolley, ctx_transporttrolley, "it.unibo.transporttrolley.Transporttrolley").
  qactor( transporttrolley_mover, ctx_transporttrolley, "it.unibo.transporttrolley_mover.Transporttrolley_mover").
  qactor( alarmemitter, ctx_transporttrolley, "it.unibo.alarmemitter.Alarmemitter").
  qactor( storage_manager, ctx_wasteservice, "it.unibo.storage_manager.Storage_manager").
  qactor( wasteservice, ctx_wasteservice, "it.unibo.wasteservice.Wasteservice").
  qactor( led, ctx_rasp, "it.unibo.led.Led").
  qactor( sonarqak22, ctx_rasp, "it.unibo.sonarqak22.Sonarqak22").
  qactor( ledcontroller, ctx_rasp, "it.unibo.ledcontroller.Ledcontroller").
