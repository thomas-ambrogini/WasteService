package it.unibo.wasteservice;

import static org.junit.Assert.assertTrue;


import it.unibo.ctxwasteservice_prototypenotruck.MainCtxwasteservice_prototypenotruckKt;
import org.junit.*;

import it.unibo.kactor.ActorBasic;
import it.unibo.kactor.QakContext;
import unibo.comm22.utils.ColorsOut;
import unibo.comm22.utils.CommUtils;


public class TestRequest {
	final static String HOST = "localhost";
	final static int 	PORT = 8050;

	static ConnTcp connTcp	 = null;

	@BeforeClass
	public static void up() {
		new Thread(){
			public void run(){
				MainCtxwasteservice_prototypenotruckKt.main();
//				MainCtxrequestrequirementanalysisKt.main();
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
		ColorsOut.outappl("TestRequest ENDS" , ColorsOut.BLUE);
	}

	@Test
	public void testRequestOk() {
		ColorsOut.outappl("testRequestOk STARTS" , ColorsOut.BLUE);
		String answer = sendRequest("glass", 30);
		ColorsOut.outappl("testRequestOk answer=" + answer , ColorsOut.GREEN);
		assertTrue(answer.contains("loadaccept"));
	}

	@Test
	public void testRequestFail() {
		ColorsOut.outappl("testRequestFail STARTS" , ColorsOut.BLUE);
		String answer = sendRequest("glass", 300);
		ColorsOut.outappl("testRequestFail answer=" + answer , ColorsOut.GREEN);
		assertTrue(answer.contains("loadrejected"));
	}

	protected String sendRequest( String type, int quantity ) {
		String answer = "";
		String truckRequestStr = "msg(storeload, request, python, wasteservice, storeload(" + type +"," + quantity + "),1)";
		try {
			answer = connTcp.request(truckRequestStr);
		} catch (Exception e) {
			ColorsOut.outerr("sendRequest ERROR:" + e.getMessage());
			e.printStackTrace();
		}

		return answer;
	}
}
