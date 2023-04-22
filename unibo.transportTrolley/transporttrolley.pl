%====================================================================================
% transporttrolley description   
%====================================================================================
context(ctx_basicrobot, "robot",  "TCP", "8020").
context(ctx_transporttrolley, "localhost",  "TCP", "8051").
 qactor( pathexec, ctx_basicrobot, "external").
  qactor( transporttrolley, ctx_transporttrolley, "it.unibo.transporttrolley.Transporttrolley").
  qactor( transporttrolley_mover, ctx_transporttrolley, "it.unibo.transporttrolley_mover.Transporttrolley_mover").
  qactor( alarmemitter, ctx_transporttrolley, "it.unibo.alarmemitter.Alarmemitter").
