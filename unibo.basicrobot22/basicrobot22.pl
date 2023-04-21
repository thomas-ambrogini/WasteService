%====================================================================================
% basicrobot22 description   
%====================================================================================
context(ctx_basicrobot, "localhost",  "TCP", "8020").
 qactor( basicrobot, ctx_basicrobot, "it.unibo.basicrobot.Basicrobot").
  qactor( pathexec, ctx_basicrobot, "it.unibo.pathexec.Pathexec").
