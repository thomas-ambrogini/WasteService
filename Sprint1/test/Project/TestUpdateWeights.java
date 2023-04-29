package it.unibo.wasteservice;

import it.unibo.ctx_wasteservice.MainCtx_wasteserviceKt;
import it.unibo.kactor.ActorBasic;
import it.unibo.kactor.QakContext;
import org.junit.*;
import unibo.comm22.utils.ColorsOut;
import unibo.comm22.utils.CommUtils;


import java.util.List;

import static org.junit.Assert.assertTrue;

public class TestUpdateWeights {
    final static String HOST = "localhost";
    final static int 	PORT = 8049;
    static ConnTcp connTcp	 = null;

    int startGlassWeight;

    TestObserver weightsObserver;


    @BeforeClass
    public static void up() {
        createConn();
    }


    protected static void createConn() {
        try {
            connTcp = new ConnTcp(HOST, PORT);
            ColorsOut.outappl("TCPConnection created", ColorsOut.YELLOW);
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    @AfterClass
    public static void closeConn() {
        try {
            connTcp.close();
            ColorsOut.outappl("TCPConnection closed", ColorsOut.YELLOW);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @After
    public void down() {
        ColorsOut.outappl("Test ENDS" , ColorsOut.BLUE);
    }


    @Test
    public void testUpdateOk() {
        ActionFunction unlockThreadFunction = () -> { unlockTestThread();};

        weightsObserver = new TestObserver(HOST, PORT, "ctx_wasteservice", "storage_manager", unlockThreadFunction);
        weightsObserver.observe();


        try {
            String storageRequestStr = "msg(storeRequest, request, python, storage_manager, storeRequest(" + "glass" +"," + 50 + "),1)";
            String wasteServiceRequestStr = "msg(startDeposit, request, python, wasteservice, startDeposit(" + "glass" + "),1)";

            startGlassWeight 		 = askStorageWeight("glass");
            int supposedFinalGlassWeight = startGlassWeight + 50;

            connTcp.forward(storageRequestStr);
            Thread.sleep(100);
            connTcp.forward(wasteServiceRequestStr);

            synchronized (this) {
                wait();
            }

            int actualFinalGlassWeight   = askStorageWeight("glass");

            assertTrue( supposedFinalGlassWeight == actualFinalGlassWeight );

        }catch(Exception e ) {
            e.printStackTrace();
            ColorsOut.outappl("Exception", ColorsOut.RED);
        }

    }


    @Test
    public void testUpdateFail() {

        ActionFunction unlockThreadFunction = () -> { unlockTestThread();};

        weightsObserver = new TestObserver(HOST, PORT, "ctx_wasteservice", "storage_manager", unlockThreadFunction);
        weightsObserver.observe();


        try {
            String storageRequestStr = "msg(storeRequest, request, python, storage_manager, storeRequest(" + "glass" +"," + 50 + "),1)";
            String wasteServiceRequestStr = "msg(startDeposit, request, python, wasteservice, startDeposit(" + "glass" + "),1)";

            startGlassWeight 		 = askStorageWeight("glass");
            int supposedFinalGlassWeight = startGlassWeight + 40;

            connTcp.forward(storageRequestStr);
            Thread.sleep(100);
            connTcp.forward(wasteServiceRequestStr);

            synchronized (this) {
                wait();
            }

            int actualFinalGlassWeight   = askStorageWeight("glass");

            assertTrue( supposedFinalGlassWeight != actualFinalGlassWeight );

        }catch(Exception e ) {
            e.printStackTrace();
            ColorsOut.outappl("Exception", ColorsOut.RED);
        }

    }

    private int askStorageWeight(String mat) {
        int result = 0;

        try {
            ConnTcp storageAskConnTcp = new ConnTcp(HOST, PORT);
            String storageAskStr = "msg(storageAsk, request, python, storage_manager, storageAsk(" + mat + "),1)";

            String response = storageAskConnTcp.request(storageAskStr);

            response = response.substring(response.indexOf('(', 10) + 1, response.indexOf(')', 10));

            result = (int) Float.parseFloat(response.substring(response.indexOf(',') + 1));

        }catch(Exception e ) {
            e.printStackTrace();
            ColorsOut.outappl("Exception", ColorsOut.RED);
        }

        return result;
    }

    private static int getWeightFromPayload(String weightUpdate, String mat) {
        int [] weights = new int[2];

        int index_par = weightUpdate.indexOf('(');
        int index_end_par = weightUpdate.indexOf(')');

        weights[0] = (int) Float.parseFloat(weightUpdate.substring(index_par + 1,index_end_par));
        weights[1] = (int) Float.parseFloat(weightUpdate.substring(weightUpdate.indexOf('(',index_end_par) + 1, weightUpdate.indexOf(')', index_end_par + 1)));

        if (mat.equalsIgnoreCase("glass")) {
            return weights[0];
        } else {
            return weights[1];
        }
    }


    private void unlockTestThread( ) {
        if(weightsObserver.updates.get(weightsObserver.updates.size() - 1) != "") {
            int glass_weight = getWeightFromPayload(weightsObserver.updates.get(weightsObserver.updates.size() - 1), "glass");

            if (startGlassWeight != glass_weight) {
                synchronized (this) { //The wait-notify is necessary for the test Thread
                    notify();
                }
            }
        }
    }



}