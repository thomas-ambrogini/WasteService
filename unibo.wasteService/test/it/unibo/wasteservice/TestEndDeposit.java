package it.unibo.wasteservice;

import it.unibo.ctxwasteservice_prototypenotruck.MainCtxwasteservice_prototypenotruckKt;
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

public class TestEndDeposit {
	final static String HOST = "localhost";
	final static int 	PORT = 8050;
	static ConnTcp connTcp	 = null;

	private List<String> positions = null;


	@BeforeClass
	public static void up() {
		new Thread(){
			public void run(){
				MainCtxwasteservice_prototypenotruckKt.main();
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
	public void testDepositReturnHome() {
		String [] finalPositions = {"home", "indoor", "glass", "home"};
		ActionFunction unlockThreadFunction = () -> { unlockTestThread(finalPositions);};

		TrolleyObserver trolleyObserver = new TrolleyObserver(unlockThreadFunction);
		trolleyObserver.observe();
		positions = trolleyObserver.getPositions();

		try {
			String truckRequestStr = "msg(storeload, request, python, wasteservice, storeload(" + "glass" +"," + 30 + "),1)";
			String pickupMessageStr = "msg(pickup, dispatch, python, transporttrolley, pickup(glass),1)";

			Thread.sleep(1000);

			connTcp.forward(truckRequestStr);

			Thread.sleep(1000);


			synchronized (this) { //Wait for the observer to see 3 positions and check if they are in the right order
				wait();
			}

			assertTrue( checkPositions(positions, finalPositions) );

		}catch(Exception e ) {
			e.printStackTrace();
			ColorsOut.outappl("Exception", ColorsOut.RED);
		}

	}


	@Test
	public void testDepositAnotherRequest() {
		String [] finalPositions = {"home", "indoor", "glass", "indoor"}; //the last element is plastic when it should be glass
		ActionFunction unlockThreadFunction = () -> { unlockTestThread(finalPositions);};

		TrolleyObserver trolleyObserver = new TrolleyObserver(unlockThreadFunction);
		trolleyObserver.observe();
		positions = trolleyObserver.getPositions();

		try {
			String truckRequestStr1 = "msg(storeload, request, python, wasteservice, storeload(" + "glass" +"," + 30 + "),1)";
			String truckRequestStr2 = "msg(storeload, request, python, wasteservice, storeload(" + "glass" +"," + 30 + "),2)";
			String pickupMessageStr = "msg(pickup, dispatch, python, transporttrolley, pickup(glass),1)";

			connTcp.forward(truckRequestStr1);


			connTcp.forward(truckRequestStr2);

			synchronized (this) { //Wait for the observer to see 3 positions and check if they are in the right order
				wait();
			}

			assertTrue( checkPositions(positions, finalPositions) );

		}catch(Exception e ) {
			e.printStackTrace();
			ColorsOut.outappl("Exception", ColorsOut.RED);
		}

	}

	private boolean checkPositions(List<String> positions, String [] finalPositions) {

		for( int i = 0; i < finalPositions.length; i++ ) {
			if(!(positions.get(i).equals(finalPositions[i]))) {
				return false;
			}
		}
		return true;
	}

	private void unlockTestThread( String [] finalPositions ) {

		if(positions.size() == finalPositions.length ) {
			synchronized (this) { //The wait-notify is necessary for the test Thread
				notify();
			}
		}
	}



}
