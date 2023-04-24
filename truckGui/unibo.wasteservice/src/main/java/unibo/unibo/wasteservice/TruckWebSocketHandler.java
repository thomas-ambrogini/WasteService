package unibo.unibo.wasteservice;

import it.unibo.kactor.IApplMessage;
import it.unibo.kactor.MsgUtil;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import unibo.comm22.interfaces.Interaction2021;
import unibo.comm22.tcp.TcpClientSupport;
import unibo.comm22.utils.ColorsOut;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class TruckWebSocketHandler extends TextWebSocketHandler {
    private final List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        ColorsOut.out("WebSocketHandler | Added the session:" + session, ColorsOut.MAGENTA);
        super.afterConnectionEstablished(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
        ColorsOut.out("WebSocketHandler | Removed the session:" + session, ColorsOut.MAGENTA);
        super.afterConnectionClosed(session, status);
    }

    @Override
    public void handleTextMessage (WebSocketSession session, TextMessage textMessage) {
        String msg = textMessage.getPayload();

        String [] parts = msg.split(",");
        String type = parts[0];
        int quantity = Integer.parseInt(parts[1]);

        Interaction2021 connToStorage = null;

        try {
            connToStorage = TcpClientSupport.connect("wasteservice", 8049, 10);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            IApplMessage storeRequest;
            storeRequest = MsgUtil.buildRequest("truckGui", "storeRequest", "storeRequest(" + type +"," + quantity + ")", "storage_manager");
            ColorsOut.outappl("TruckUtils | sendMsg msg:" + storeRequest + " conn=" + connToStorage, ColorsOut.BLUE);

            String response = connToStorage.request(storeRequest.toString());
            ColorsOut.outappl("Response: " + response, ColorsOut.BLUE);

            connToStorage.close();

            if(response.contains("accepted")) {
                session.sendMessage(new TextMessage("reply(Load Accepted)"));
                startDepositAction(session, type, quantity);
            } else {
                session.sendMessage(new TextMessage("reply(Load Rejected)"));
            }


        } catch (Exception e) {
            ColorsOut.outerr("TruckGUI | sendMsg on:" + connToStorage + " ERROR:"+e.getMessage());
        }

    }

    private static void startDepositAction(WebSocketSession session, String type, int quantity) {
        Interaction2021 connToWasteService = null;

        try {
            connToWasteService = TcpClientSupport.connect("wasteservice", 8049, 10);

            IApplMessage depositRequest;
            depositRequest = MsgUtil.buildRequest("truckGui", "startDeposit", "startDeposit(" + type + ")", "wasteservice");
            ColorsOut.outappl("TruckUtils | sendMsg msg:" + depositRequest + " conn=" + connToWasteService, ColorsOut.BLUE);

            String response = connToWasteService.request(depositRequest.toString());
            ColorsOut.outappl("Response: " + response, ColorsOut.BLUE);

            session.sendMessage(new TextMessage("loadState(Picked Up, you can leave the indoor area)"));

            connToWasteService.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
