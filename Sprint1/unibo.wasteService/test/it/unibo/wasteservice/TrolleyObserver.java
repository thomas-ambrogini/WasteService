package it.unibo.wasteservice;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapObserveRelation;
import org.eclipse.californium.core.CoapResponse;
import org.junit.Assert;
import unibo.comm22.utils.ColorsOut;

import java.util.ArrayList;
import java.util.List;

public class TrolleyObserver {
    private CoapObserveRelation relation  = null;
    private CoapClient          client    = null;
    private List<String>        positions = null;
    private ActionFunction      callback;

    public TrolleyObserver(ActionFunction callback) {
        client = new CoapClient("coap://localhost:8050/ctxdepositrequirementanalysis/transporttrolley");
        System.out.println("Trolley observer created");
        positions = new ArrayList<String>();
        this.callback = callback;
    }

    public void observe() {
        relation = client.observe(
                new CoapHandler() {
                    @Override
                    public void onLoad(CoapResponse response) {
                        String content = response.getResponseText();
                        ColorsOut.outappl("TrolleyObserver | value=" + content, ColorsOut.GREEN);
                        positions.add(getPayload(content));

                        callback.run();
                    }

                    @Override
                    public void onError() {
                        ColorsOut.outerr("Observing Failed (press enter to exit)");
                    }
                }
        );
    }

    public List<String> getPositions () {
        return this.positions;
    }

    private String getPayload(String response){
        return response.subSequence(9, response.length() - 1).toString();
    }

}
