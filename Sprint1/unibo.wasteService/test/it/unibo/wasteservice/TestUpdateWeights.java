package it.unibo.wasteservice;

import it.unibo.ctxDepositRequirementAnalysis.MainCtxDepositRequirementAnalysisKt;
import it.unibo.kactor.ActorBasic;
import it.unibo.kactor.QakContext;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import unibo.comm22.utils.ColorsOut;
import unibo.comm22.utils.CommUtils;

import java.util.List;

import static org.junit.Assert.assertTrue;

public class TestUpdateWeights {
	final static String HOST = "localhost";
	final static int 	PORT = 8051;
	static ConnTcp connTcp	 = null;


	@BeforeClass
	public static void up() {
		new Thread(){
			public void run(){
				MainCtxDepositRequirementAnalysisKt.main();
			}
		}.start();
		waitForApplStarted();
		createConn();
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
		ActionFunction unlockThreadFunction = () -> { unlockTestThread();};

		WeightsObserver weightsObserver = new WeightsObserver(unlockThreadFunction);
		weightsObserver.observe();


		try {
			String pickupMessageStr = "msg(pickup, dispatch, python, wasteservice, deposit(glass, 50),1)";

			int startGlassWeight 		 = weightsObserver.getGlassWeight();
			int supposedFinalGlassWeight = startGlassWeight + 50;

			connTcp.forward(pickupMessageStr);

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
	public void testDepositAnotherFail() {
		ActionFunction unlockThreadFunction = () -> { unlockTestThread();};

		WeightsObserver weightsObserver = new WeightsObserver(unlockThreadFunction);
		weightsObserver.observe();


		try {
			String pickupMessageStr = "msg(pickup, dispatch, python, wasteservice, deposit(glass, 50),1)";

			int startGlassWeight 		 = weightsObserver.getGlassWeight();
			int supposedFinalGlassWeight = startGlassWeight + 40;

			connTcp.forward(pickupMessageStr);

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
	

	private void unlockTestThread( ) {
		synchronized (this) { //The wait-notify is necessary for the test Thread
			notify();
		}
	}



}
