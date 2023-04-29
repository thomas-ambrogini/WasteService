package unibo.rasp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


import it.unibo.ctx_rasp.MainCtx_raspKt;
import org.checkerframework.checker.units.qual.C;
import org.junit.*;

import it.unibo.kactor.ActorBasic;
import it.unibo.kactor.QakContext;
import unibo.comm22.utils.ColorsOut;
import unibo.comm22.utils.CommUtils;

import java.util.List;
import java.util.Random;


public class TestLed {
	final static String HOST_LED = "localhost";
	final static String HOST_TROLLEY = "localhost";
	final static int 	PORT_LED = 8056;
	final static int 	PORT_TROLLEY = 8051;
	final static String CTX_LED  = "ctx_rasp";
	final static String CTX_TROLLEY = "ctx_transporttrolley";
	static ConnTcp connTcpLed	 = null;
	static ConnTcp connTcpTrolley_mover = null;
	static ConnTcp connTcpWasteservice = null;

	TestObserver ledObserver = null;
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
			connTcpLed.close();
			connTcpTrolley_mover.close();
			connTcpWasteservice.close();
			ColorsOut.outappl("TCPConnection closed", ColorsOut.YELLOW);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected static void createConn() {
		try {
			connTcpLed = new ConnTcp(HOST_LED, PORT_LED);
			connTcpTrolley_mover = new ConnTcp("localhost", PORT_TROLLEY);
			connTcpWasteservice = new ConnTcp("localhost", 8049);
			ColorsOut.outappl("TCPConnection created", ColorsOut.YELLOW);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	protected static void waitForApplStarted(){
		ActorBasic  led = QakContext.Companion.getActor("led");
		while( led == null ){
			ColorsOut.outappl("TestRequest waits for appl ... " , ColorsOut.GREEN);
			CommUtils.delay(200);
			led = QakContext.Companion.getActor("led");
		}
	}


	@After
	public void down() {
		ColorsOut.outappl("TestLed ENDS" , ColorsOut.BLUE);
	}


	@Test
	public void testLed() throws Exception {
		boolean end = false;
		int no_home = 0;
		String previous_position = "";

		try {
			ActionFunction unlockThreadFunction = () -> {unlockTestThread();};
			trolleyObserver = new TestObserver(HOST_TROLLEY, PORT_TROLLEY, CTX_TROLLEY, "transporttrolley_mover");
			trolleyObserver.observe();

			ledObserver = new TestObserver(HOST_LED, PORT_LED, CTX_LED, "led", unlockThreadFunction);
			ledObserver.observe();

			TestObserver wasteserviceObserver = new TestObserver(HOST_TROLLEY, 8049, "ctx_wasteservice", "wasteservice");
			wasteserviceObserver.observe();

			String truckReq = "msg(startDeposit, request, test_unit, wasteservice, startDeposit(glass), 2)";

			connTcpWasteservice.forward(truckReq);


			while (!end) {
				synchronized (this) {
					wait();

				}

				Thread.sleep(10);

				String wasteState   = wasteserviceObserver.getUpdates().get(wasteserviceObserver.getUpdates().size() - 1);
				String trolleyState = trolleyObserver.getUpdates().get(trolleyObserver.getUpdates().size() - 1);
				String ledState = ledObserver.getUpdates().get(ledObserver.getUpdates().size() - 1);

				if (wasteState.contains("home") && !previous_position.contains("home")) {
					if(++no_home >= 2) {
						end = true;
					}
					ColorsOut.outappl("Numero di volte visto home:" + no_home, ColorsOut.ANSI_PURPLE);
				}

				ColorsOut.outappl("Trolley: " + trolleyState + " led: " + ledState + " waste:" + wasteState, ColorsOut.ANSI_PURPLE);


				if ( trolleyState.contains("stop") ) {
					assertTrue(ledState.contains("on"));
				}
				else if( wasteState.contains("home") && trolleyState.contains("idle") ) {
					assertTrue(ledState.contains("off"));
				} else if ( trolleyState.contains("work")) {
					assertTrue(ledState.contains("blink"));
				}
				previous_position = wasteState;
			}

		}catch(Exception e ) {
			e.printStackTrace();
			ColorsOut.outappl("Exception", ColorsOut.RED);
		}
	}

	@Test
	public void testLedStop() throws Exception {

		try {
			ActionFunction unlockThreadFunction = () -> {unlockTestThread();};
			trolleyObserver = new TestObserver(HOST_TROLLEY, PORT_TROLLEY, CTX_TROLLEY, "transporttrolley_mover", unlockThreadFunction);
			trolleyObserver.observe();

			ledObserver = new TestObserver(HOST_LED, PORT_LED, CTX_LED, "led", unlockThreadFunction);
			ledObserver.observe();

			TestObserver wasteserviceObserver = new TestObserver(HOST_TROLLEY, 8049, "ctx_wasteservice", "wasteservice");
			wasteserviceObserver.observe();

			String stop = "msg(stop, dispatch, test_unit, transporttrolley_mover, stop(_), 2)";
			String resume = "msg(resume, dispatch, test_unit, transporttrolley_mover, resume(_), 3)";

			String truckReq = "msg(startDeposit, request, test_unit, wasteservice, startDeposit(glass), 2)";

			connTcpWasteservice.forward(truckReq);

			Random rand = new Random();
			int waitTime = rand.nextInt(1500);

			Thread.sleep(waitTime);

			connTcpTrolley_mover.forward(stop);

			Thread.sleep(100);

			String ledState = ledObserver.getUpdates().get(ledObserver.getUpdates().size() - 1);
			String trolleyState = trolleyObserver.getUpdates().get(trolleyObserver.getUpdates().size() - 1);

			ColorsOut.outappl("Trolley: " + trolleyState + " led: " + ledState, ColorsOut.ANSI_PURPLE);
			assertTrue(trolleyState.contains("stop") && ledState.contains("on"));

			Thread.sleep(4000);

			connTcpTrolley_mover.forward(resume);

			Thread.sleep(100);

			ledState = ledObserver.getUpdates().get(ledObserver.getUpdates().size() - 1);
			trolleyState = trolleyObserver.getUpdates().get(trolleyObserver.getUpdates().size() - 1);

			ColorsOut.outappl("Trolley: " + trolleyState + " led: " + ledState, ColorsOut.ANSI_PURPLE);
			assertTrue(!trolleyState.contains("stop"));

			Thread.sleep(20000);



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