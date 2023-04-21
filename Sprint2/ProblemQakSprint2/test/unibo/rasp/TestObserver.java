package unibo.rasp;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapObserveRelation;
import org.eclipse.californium.core.CoapResponse;
import unibo.comm22.utils.ColorsOut;

import java.util.ArrayList;
import java.util.List;

public class TestObserver {
    private CoapObserveRelation relation  = null;
    private CoapClient client    = null;
    private ActionFunction      callback;
    List<String> updates;
    private String actor;

    public TestObserver(String host, int port, String ctx, String actor, ActionFunction callback) {
        this.actor = actor;
        client = new CoapClient("coap://"+ host + ":"+port+"/"+ ctx + "/"+actor);
        System.out.println(actor + "observer created");
        this.callback = callback;
        updates = new ArrayList();
    }

    public TestObserver(String host, int port, String ctx, String actor) {
        this.actor = actor;
        client = new CoapClient("coap://"+ host + ":"+port+"/"+ ctx + "/"+actor);
        System.out.println(actor + "observer created");
        this.callback = null;
        updates = new ArrayList();
    }


    public void observe() {
        relation = client.observe(
                new CoapHandler() {
                    @Override
                    public void onLoad(CoapResponse response) {
                        String content = response.getResponseText();
                        ColorsOut.outappl(actor + "Observer | value=" + content, ColorsOut.GREEN);

                        updates.add(content);

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

    public List<String> getUpdates () {
        return this.updates;
    }
}
