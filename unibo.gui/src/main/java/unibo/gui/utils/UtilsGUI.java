package unibo.gui.utils;

import unibo.comm22.coap.CoapConnection;
import unibo.comm22.utils.ColorsOut;
import unibo.comm22.utils.CommSystemConfig;
import unibo.gui.UtilsCoapObserver;


public class UtilsGUI {
    public static void connectWithUtilsUsingCoap(String addr){
        try{
            CommSystemConfig.tracing = true;
            String ipaddr = addr.substring(0,addr.indexOf(':'));
            addr = addr.substring(addr.indexOf(':') + 1);

            int port = Integer.parseInt(addr.substring(0, addr.indexOf(":")));
            addr = addr.substring(addr.indexOf(':') + 1);

            String ctx   = addr.substring(0, addr.indexOf('/'));
            String actor = addr.substring(addr.indexOf('/') + 1);


            String coapResourceAddressFormat = "%s:%d";
            String coapResourcePathFormat ="%s/%s";
            String address = String.format(coapResourceAddressFormat,ipaddr,port);
            String path = String.format(coapResourcePathFormat,ctx,actor);

            new CoapConnection(address, path).observeResource(new UtilsCoapObserver(actor));

        }catch(Exception e){
            ColorsOut.outerr("UtilsGUI | connectWithUtilsUsingTcp ERROR:"+e.getMessage());
        }
    }
}
