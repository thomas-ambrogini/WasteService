package it.unibo.wasteservice;

import it.unibo.ctxwasteservice_prototypenotruck.MainCtxwasteservice_prototypenotruckKt;
import it.unibo.kactor.ActorBasic;
import it.unibo.kactor.QakContext;
import org.junit.*;
import unibo.comm22.utils.ColorsOut;
import unibo.comm22.utils.CommUtils;

import java.util.List;

import static org.junit.Assert.assertTrue;

public class TestUpdateWeights {
	final static String HOST = "localhost";
	final static int 	PORT = 8050;
	static ConnTcp connTcp	 = null;

	int startGlassWeight;
	static WeightsObserver weightsObserver;


	@BeforeClass
	public static void up() {
		new Thread(){
			public void run(){
				MainCtxwasteservice_prototypenotruckKt.main();
			}
		}.start();
		waitForApplStarted();
		createConn();
		createWeightObserver();

	}

	protected static void waitForApplStarted(){
		ActorBasic wasteservice = QakContext.Companion.getActor("wasteservice");
		while( wasteservice == null ){
			ColorsOut.outappl("TestRequest waits for appl ... " , ColorsOut.GREEN);
			CommUtils.delay(200);
			wasteservice = QakContext.Companion.getActor("wasteservice");
		}
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

		try {
			String truckRequestStr = "msg(storeload, request, python, wasteservice, storeload(" + "glass" +"," + 50 + "),1)";
			String pickupMessageStr = "msg(pickup, dispatch, python, wasteservice, deposit(glass, 50),1)";

			startGlassWeight 		 = weightsObserver.getGlassWeight();
			int supposedFinalGlassWeight = startGlassWeight + 50;

			connTcp.forward(truckRequestStr);

			synchronized (this) { //Wait for the observer to see 3 positions and check if they are in the right order
				wait();
			}

			int actualFinalGlassWeight   = weightsObserver.getGlassWeight();

			assertTrue( supposedFinalGlassWeight == actualFinalGlassWeight );

		}catch(Exception e ) {
			e.printStackTrace();
			ColorsOut.outappl("Exception", ColorsOut.RED);
		}

	}


	@Test
	public void testUpdateFail() {

		try {
			String truckRequestStr = "msg(storeload, request, python, wasteservice, storeload(" + "glass" +"," + 50 + "),1)";
			String pickupMessageStr = "msg(pickup, dispatch, python, wasteservice, deposit(glass, 50),1)";


			startGlassWeight 		 = weightsObserver.getGlassWeight();
			int supposedFinalGlassWeight = startGlassWeight + 40;

			connTcp.forward(truckRequestStr);

			synchronized (this) { //Wait for the observer to see 3 positions and check if they are in the right order
				wait();
			}

			int actualFinalGlassWeight   = weightsObserver.getGlassWeight();

			assertTrue( supposedFinalGlassWeight != actualFinalGlassWeight );

		}catch(Exception e ) {
			e.printStackTrace();
			ColorsOut.outappl("Exception", ColorsOut.RED);
		}

	}

	@Before
	public void addCallback() {
		ActionFunction unlockThreadFunction = () -> { unlockTestThread();};
		weightsObserver.setCallback(unlockThreadFunction);
	}


	public static void createWeightObserver() {
		weightsObserver = new WeightsObserver();
		weightsObserver.observe();
	}
	

	private void unlockTestThread( ) {
		int glass_weight = weightsObserver.getGlassWeight();

		if(startGlassWeight != glass_weight ) {
			synchronized (this) { //The wait-notify is necessary for the test Thread
				notify();
			}
		}
	}



}
