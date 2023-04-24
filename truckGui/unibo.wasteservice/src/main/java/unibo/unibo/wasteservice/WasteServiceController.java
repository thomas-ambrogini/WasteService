package unibo.unibo.wasteservice;


import alice.tuprolog.interfaces.PrologFactory;
import it.unibo.kactor.IApplMessage;
import it.unibo.kactor.MsgUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import unibo.comm22.interfaces.Interaction2021;
import unibo.comm22.tcp.TcpClientSupport;
import unibo.comm22.utils.ColorsOut;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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


}
