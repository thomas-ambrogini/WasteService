package unibo.unibo.wasteservice;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import unibo.comm22.utils.ColorsOut;

@Controller
public class WasteServiceController {
    protected String mainPage = "truck";

    public String buildThePage(Model viewmodel){
        return mainPage;
    }


    @GetMapping("/")
    public String entry(Model viewmodel){
        return buildThePage(viewmodel);
    }


    @PostMapping("/deposit")
    public String depositRequest(Model viewmodel, @RequestParam String type, @RequestParam int quantity) {
        ColorsOut.outappl("WasteService | type:" + type + " quantity:"+ quantity, ColorsOut.BLUE);

//        try {
//            sendMsg("sonarqak22",state);
//        } catch (Exception e) {
//            ColorsOut.outerr("SonarController | changeState ERROR:"+e.getMessage());
//        }


        return buildThePage(viewmodel);
    }


}
