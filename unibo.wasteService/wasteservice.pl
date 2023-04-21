%====================================================================================
% wasteservice description   
%====================================================================================
context(ctx_basicrobot, "robot",  "TCP", "8020").
context(ctx_rasp, "rasp",  "TCP", "8056").
context(ctx_wasteservice, "localhost",  "TCP", "8049").
 qactor( pathexec, ctx_basicrobot, "external").
  qactor( led, ctx_rasp, "external").
  qactor( transporttrolley, ctx_wasteservice, "it.unibo.transporttrolley.Transporttrolley").
  qactor( alarmemitter, ctx_wasteservice, "it.unibo.alarmemitter.Alarmemitter").
  qactor( transporttrolley_mover, ctx_wasteservice, "it.unibo.transporttrolley_mover.Transporttrolley_mover").
  qactor( storage_manager, ctx_wasteservice, "it.unibo.storage_manager.Storage_manager").
  qactor( wasteservice, ctx_wasteservice, "it.unibo.wasteservice.Wasteservice").
