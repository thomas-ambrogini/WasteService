%====================================================================================
% transporttrolley description   
%====================================================================================
context(ctx_basicrobot, "127.0.0.1",  "TCP", "8020").
context(ctx_transporttrolley, "localhost",  "TCP", "8051").
context(ctx_rasp, "127.0.0.1",  "TCP", "8056").
 qactor( pathexec, ctx_basicrobot, "external").
  qactor( led, ctx_rasp, "external").
  qactor( transporttrolley, ctx_transporttrolley, "it.unibo.transporttrolley.Transporttrolley").
  qactor( ledcontroller, ctx_transporttrolley, "it.unibo.ledcontroller.Ledcontroller").
  qactor( transporttrolley_mover, ctx_transporttrolley, "it.unibo.transporttrolley_mover.Transporttrolley_mover").
