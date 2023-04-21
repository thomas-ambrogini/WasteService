package unibo.rasp;

import static org.junit.Assert.assertTrue;


import it.unibo.ctx_rasp.MainCtx_raspKt;
import org.junit.*;

import it.unibo.kactor.ActorBasic;
import it.unibo.kactor.QakContext;
import unibo.comm22.utils.ColorsOut;
import unibo.comm22.utils.CommUtils;

import java.util.List;


public class TestLed {
	final static String HOST = "localhost";
	final static int 	PORT_LED = 8056;
	final static int 	PORT_TROLLEY = 8051;
	final static String CTX_LED  = "ctx_rasp";
	final static String CTX_TROLLEY = "ctx_trolley";
	static ConnTcp connTcp	 = null;

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

	@Before
	public void createObservers() {
		ledObserver = new TestObserver(HOST, PORT_LED, CTX_LED, "led");
		ledObserver.observe();
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

	protected static void createConn() {
		try {
			connTcp = new ConnTcp(HOST, PORT_LED);
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
		ColorsOut.outappl("TestRequest ENDS" , ColorsOut.BLUE);
	}


//	@Test
	public void testLed() throws Exception {
		try {
			ActionFunction unlockThreadFunction = () -> {unlockTestThread();};
			trolleyObserver = new TestObserver(HOST, PORT, CTX, "trolley", unlockThreadFunction);
			trolleyObserver.observe();



			boolean updated = false;
			while (!updated) {

				synchronized (this) {
					wait();
				}

				String state = trolleyObserver.getUpdates().get(trolleyObserver.getUpdates().size() - 1);
				String ledState = ledObserver.getUpdates().get(ledObserver.getUpdates().size() - 1);

				updated = true;

				if (state.equalsIgnoreCase("work")) {
					assertTrue(ledState.contains("blink"));
				} else if (state.equalsIgnoreCase("stopped")) {
					assertTrue(ledState.contains("off"));
				} else if (state.equalsIgnoreCase("home")) {
					assertTrue(ledState.contains("on"));
				} else
					updated = false;
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