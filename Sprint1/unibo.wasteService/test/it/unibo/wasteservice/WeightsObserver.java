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

    public WeightsObserver(ActionFunction callback) {
        client = new CoapClient("coap://localhost:8051/ctxdepositrequirementanalysis/wasteservice");
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

    private String getPayload(String response){
        return response.subSequence(9, response.length() - 1).toString();
    }

}
