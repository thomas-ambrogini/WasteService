package it.unibo.wasteservice;

import it.unibo.ctx_wasteservice.MainCtx_wasteserviceKt;
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
	final static int 	PORT = 8049;
	static ConnTcp connTcp	 = null;

	private List<String> positions = null;


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
	public void testDepositReturnHome() {
		String [] finalPositions = {"home", "indoor", "glass", "home"};
		ActionFunction unlockThreadFunction = () -> { unlockTestThread(finalPositions);};

		TestObserver wasteServiceObserver = new TestObserver(HOST, PORT, "ctx_wasteservice", "wasteservice", unlockThreadFunction);
		wasteServiceObserver.observe();
		positions = wasteServiceObserver.getUpdates();

		try {
			String truckRequestStr = "msg(startDeposit, request, python, wasteservice, startDeposit(" + "glass" + "),1)";

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

		TestObserver wasteServiceObserver = new TestObserver(HOST, PORT, "ctx_wasteservice", "wasteservice", unlockThreadFunction);
		wasteServiceObserver.observe();
		positions = wasteServiceObserver.getUpdates();

		try {
			String truckRequestStr1 = "msg(startDeposit, request, python, wasteservice, startDeposit(" + "glass" + "),1)";
			String truckRequestStr2 = "msg(startDeposit, request, python, wasteservice, startDeposit(" + "glass" + "),2)";

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
			if(!(positions.get(i).contains(finalPositions[i]))) {
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
