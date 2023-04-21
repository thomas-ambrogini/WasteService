package it.unibo.wasteservice;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapObserveRelation;
import org.eclipse.californium.core.CoapResponse;
import unibo.comm22.utils.ColorsOut;

import java.util.ArrayList;
import java.util.List;

public class WeightsObserver {
    private CoapObserveRelation relation  = null;
    private CoapClient          client    = null;
    private ActionFunction      callback;

    private int                 glassWeight;
    private int                 plasticWeight;

    public WeightsObserver() {
        client = new CoapClient("coap://localhost:8050/ctxwasteservice_prototypenotruck/wasteservice");
        System.out.println("WasteService observer created");
        glassWeight = 0;
        plasticWeight = 0;
    }

    public WeightsObserver(ActionFunction callback) {
        client = new CoapClient("coap://localhost:8050/ctxwasteservice_prototypenotruck/wasteservice");
        System.out.println("WasteService observer created");
        this.callback = callback;

        glassWeight = 0;
        plasticWeight = 0;
    }

    public void observe() {
        relation = client.observe(
                new CoapHandler() {
                    @Override
                    public void onLoad(CoapResponse response) {
                        String content = response.getResponseText();
                        getWeightsFromPayload(content);
                        ColorsOut.outappl("TrolleyObserver | value=" + content, ColorsOut.GREEN);


                        callback.run();
                    }

                    @Override
                    public void onError() {
                        ColorsOut.outerr("Observing Failed (press enter to exit)");
                    }
                }
        );
    }

    public int getGlassWeight() {
        return glassWeight;
    }

    public int getPlasticWeight() {
        return plasticWeight;
    }

    public void setCallback(ActionFunction callback) {
        this.callback = callback;
    }

    private void getWeightsFromPayload(String content) {
        if(content.contains("glass(")) {
            int index_close_bracket = content.indexOf(')');
            String glassWeightString = content.subSequence(6, index_close_bracket).toString();

            glassWeight = Integer.parseInt(glassWeightString);
            ColorsOut.outappl("Glass = " + glassWeight, ColorsOut.ANSI_PURPLE);

            String plasticContent = content.subSequence(index_close_bracket, content.length() - 1).toString();
            int index_open_bracket = plasticContent.indexOf('(');
            index_close_bracket = plasticContent.indexOf(')');
            String plasticWeightString = plasticContent.substring(index_open_bracket, index_close_bracket);
            plasticWeight = Integer.parseInt(plasticWeightString);
            ColorsOut.outappl("Plastic = " + plasticWeight, ColorsOut.ANSI_PURPLE);



        }
    }

    private String getPayload(String response){
        return response.subSequence(9, response.length() - 1).toString();
    }

}
