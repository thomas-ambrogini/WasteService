%====================================================================================
% wasteservice_sprint2 description   
%====================================================================================
context(ctxpro_sonar_stop, "localhost",  "TCP", "8050").
 qactor( sonarshim, ctxpro_sonar_stop, "it.unibo.sonarshim.Sonarshim").
  qactor( sonarinterrupter, ctxpro_sonar_stop, "it.unibo.sonarinterrupter.Sonarinterrupter").
  qactor( trolley, ctxpro_sonar_stop, "it.unibo.trolley.Trolley").
  qactor( echo, ctxpro_sonar_stop, "it.unibo.echo.Echo").
