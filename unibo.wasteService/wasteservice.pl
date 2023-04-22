%====================================================================================
% wasteservice description   
%====================================================================================
context(ctx_transporttrolley, "transporttrolley",  "TCP", "8051").
context(ctx_wasteservice, "localhost",  "TCP", "8049").
 qactor( transporttrolley, ctx_transporttrolley, "external").
  qactor( storage_manager, ctx_wasteservice, "it.unibo.storage_manager.Storage_manager").
  qactor( wasteservice, ctx_wasteservice, "it.unibo.wasteservice.Wasteservice").
