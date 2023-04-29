package unibo.rasp;

import it.unibo.ctx_rasp.MainCtx_raspKt;
import it.unibo.kactor.ActorBasic;
import it.unibo.kactor.QakContext;
import org.junit.*;
import unibo.comm22.utils.ColorsOut;
import unibo.comm22.utils.CommUtils;

import static org.junit.Assert.assertTrue;

public class TestSonar {
    final static String HOST_SONAR = "localhost";
    final static String HOST_TROLLEY = "localhost";
    final static int 	PORT_SONAR = 8056;
    final static int 	PORT_TROLLEY = 8051;
    final static String CTX_SONAR  = "ctx_rasp";
    final static String CTX_TROLLEY = "ctx_transporttrolley";
    final static int DLIMIT = 20;

    static ConnTcp connTcpSonar = null;
    static ConnTcp connTcpTrolley_mover = null;
    static ConnTcp connTcpWasteservice = null;

    TestObserver sonarObserver = null;
    TestObserver trolleyObserver = null;



    @BeforeClass
    public static void up() {
        new Thread(){
            public void run(){
                MainCtx_raspKt.main();
            }
        }.start();
        waitForApplStarted();
        createConn();
    }


    @AfterClass
    public static void closeConn() {
        try {
            connTcpSonar.close();
            connTcpTrolley_mover.close();
            connTcpWasteservice.close();
            ColorsOut.outappl("TCPConnection closed", ColorsOut.YELLOW);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected static void createConn() {
        try {
            connTcpSonar         = new ConnTcp("localhost", PORT_SONAR);
            connTcpTrolley_mover = new ConnTcp("localhost", PORT_TROLLEY);
            connTcpWasteservice = new ConnTcp("localhost", 8049);
            ColorsOut.outappl("TCPConnection created", ColorsOut.YELLOW);
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    protected static void waitForApplStarted(){
        ActorBasic  sonar = QakContext.Companion.getActor("sonar");
        while( sonar == null ){
            ColorsOut.outappl("TestRequest waits for appl ... " , ColorsOut.GREEN);
            CommUtils.delay(200);
            sonar = QakContext.Companion.getActor("led");
        }
    }


    @After
    public void down() {
        ColorsOut.outappl("TestSonar ENDS" , ColorsOut.BLUE);
    }


    @Test
    public void testSonar() throws Exception {
        boolean cont = true;
        int sonarState = 1000;
        try {
            ActionFunction unlockThreadFunction = () -> {unlockTestThread();};
            trolleyObserver = new TestObserver(HOST_TROLLEY, PORT_TROLLEY, CTX_TROLLEY, "transporttrolley_mover");
            trolleyObserver.observe();

            sonarObserver = new TestObserver(HOST_SONAR, PORT_SONAR, CTX_SONAR, "sonar", unlockThreadFunction);
            sonarObserver.observe();

            String truckReq = "msg(startDeposit, request, test_unit, wasteservice, startDeposit(glass), 2)";

            connTcpWasteservice.forward(truckReq);


            while (cont) {

                synchronized (this) {
                    wait();
                }

                Thread.sleep(100);

                String trolleyState = trolleyObserver.getUpdates().get(trolleyObserver.getUpdates().size() - 1);

                try {
                    sonarState = Integer.parseInt(sonarObserver.getUpdates().get(sonarObserver.getUpdates().size() - 1));
                }catch (Exception e) {
                    ColorsOut.outerr("stato sonar non intero " + sonarObserver.getUpdates().get(sonarObserver.getUpdates().size() - 1));
                }

                ColorsOut.outappl("Trolley state: " + trolleyState + " Sonar data: "  + sonarState, ColorsOut.ANSI_PURPLE);

                if (sonarState <= DLIMIT) {
                    assertTrue(trolleyState.contains("stop"));
                    cont = false;
                }

            }

        }catch(Exception e ) {
            e.printStackTrace();
            ColorsOut.outappl("Exception", ColorsOut.RED);
        }
    }

    private void unlockTestThread( ) {

        if(trolleyObserver.getUpdates().size() != 0) {
            synchronized (this) { //The wait-notify is necessary for the test Thread
                notify();
            }
        }
    }
}
