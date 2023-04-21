package unibo.gui;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
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

}
