package unibo.gui;


import it.unibo.kactor.IApplMessage;
import it.unibo.kactor.MsgUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import unibo.comm22.interfaces.Interaction2021;
import unibo.comm22.tcp.TcpClientSupport;
import unibo.comm22.utils.ColorsOut;
import unibo.comm22.utils.CommUtils;
import unibo.gui.utils.UtilsGUI;


@Controller
public class GuiController {

    protected String mainPage = "gui";

    @Value("${waste.resources.ledstate}")
    String ledStateResourceURL;
    @Value("${waste.resources.position}")
    String positionResourceURL;
    @Value("${waste.resources.weight}")
    String weightResourceURL;
    @Value("${waste.resources.trolleystate}")
    String trolleyStateResourceURL;


    public String buildThePage(Model viewmodel){

        return mainPage;
    }

    @GetMapping("/")
    public String entry(Model viewmodel){
        UtilsGUI.connectWithUtilsUsingCoap(ledStateResourceURL);
        UtilsGUI.connectWithUtilsUsingCoap(positionResourceURL);
        UtilsGUI.connectWithUtilsUsingCoap(weightResourceURL);
        UtilsGUI.connectWithUtilsUsingCoap(trolleyStateResourceURL);

        return buildThePage(viewmodel);
    }


    @PostMapping("/sonar")
    public String changeSonarState(Model viewmodel  , @RequestParam String state ){
        ColorsOut.outappl("SonarController | state:" + state, ColorsOut.BLUE);
        //WebSocketConfiguration.wshandler.sendToAll("RobotController | doMove:" + move); //disappears
        try {
           sendMsg("sonarqak22",state);
        } catch (Exception e) {
            ColorsOut.outerr("SonarController | changeState ERROR:"+e.getMessage());
        }
        //return mainPage;
        return buildThePage(viewmodel);
    }

    public static void sendMsg(String sonarName, String cmd){

        Interaction2021 connToSonar = null;

        try {
            connToSonar = TcpClientSupport.connect("192.168.1.11", 8056, 10);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            IApplMessage msg;

            switch (cmd) {
                case "activate" :  msg = MsgUtil.buildDispatch("gui", "sonaractivate", "info("+cmd+")", sonarName); break;
                case "deactivate" : msg = MsgUtil.buildDispatch("gui","sonardeactivate", "info("+cmd+")", sonarName); break;
                default: msg = null;
            }

            ColorsOut.outappl("SonarUtils | sendMsg msg:" + msg + " conn=" + connToSonar, ColorsOut.BLUE);

            connToSonar.forward( msg.toString() );

            connToSonar.close();
        } catch (Exception e) {
            ColorsOut.outerr("SonarUtils | sendMsg on:" + connToSonar + " ERROR:"+e.getMessage());
        }


    }

}
