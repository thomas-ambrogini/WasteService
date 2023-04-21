package unibo.rasp;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapObserveRelation;
import org.eclipse.californium.core.CoapResponse;
import unibo.comm22.utils.ColorsOut;

import java.util.ArrayList;
import java.util.List;

public class LedObserver {
    private CoapObserveRelation relation  = null;
    private CoapClient client    = null;
    private ActionFunction      callback;
    private String state;

    public LedObserver(ActionFunction callback) {
        client = new CoapClient("coap://localhost:8056/ctx_rasp/led");
        System.out.println("Led observer created");
        this.callback = callback;
    }

    public LedObserver() {
        client = new CoapClient("coap://localhost:8056/ctx_rasp/led");
        System.out.println("Led observer created");
        callback = null;
    }

    public void observe() {
        relation = client.observe(
                new CoapHandler() {
                    @Override
                    public void onLoad(CoapResponse response) {
                        String content = response.getResponseText();
                        ColorsOut.outappl("LedObserver | value=" + content, ColorsOut.GREEN);
                        state = content;

                        if (callback != null) {
                            callback.run();
                        }
                    }

                    @Override
                    public void onError() {
                        ColorsOut.outerr("Observing Failed (press enter to exit)");
                    }
                }
        );
    }

    public String getState() {
        return this.state;
    }
}
