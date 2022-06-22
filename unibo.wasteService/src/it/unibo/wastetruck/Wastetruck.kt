/* Generated by AN DISI Unibo */ 
package it.unibo.wastetruck

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Wastetruck ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "s0"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		
				var Type       = "";
				var Weight     = 0L;
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						delay(5000) 
						 val Load = kotlin.random.Random.nextLong(10,100) 
						request("dumpwaste", "dumpwaste(glass,$Load)" ,"wasteservice" )  
					}
					 transition(edgeName="t01",targetState="accepted",cond=whenReply("loadaccepted"))
					transition(edgeName="t02",targetState="rejected",cond=whenReply("loadrejected"))
				}	 
				state("accepted") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						if( checkMsgContent( Term.createTerm("loadaccepted(TYPE,LOAD)"), Term.createTerm("loadaccepted(TYPE,LOAD)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								
												Type   = payloadArg(0);
												Weight = payloadArg(1).toLong()
								println("wasteTruck | accepted load of $Type and Weight $Weight ")
						}
					}
					 transition( edgeName="goto",targetState="s0", cond=doswitch() )
				}	 
				state("rejected") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						if( checkMsgContent( Term.createTerm("loadrejected(TYPE,LOAD)"), Term.createTerm("loadrejected(TYPE,LOAD)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								
												Type   = payloadArg(0);
												Weight = payloadArg(1).toLong();
								println("wasteTruck | rejected load of $Type and Weight $Weight ")
						}
					}
					 transition( edgeName="goto",targetState="s0", cond=doswitch() )
				}	 
			}
		}
}