/* Generated by AN DISI Unibo */ 
package it.unibo.transporttrolley

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Transporttrolley ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "s0"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		
				var Container = ""
				var Load      = 0L
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
					}
					 transition(edgeName="t03",targetState="handleactivate",cond=whenRequest("activate"))
				}	 
				state("handleactivate") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						if( checkMsgContent( Term.createTerm("activate(TYPE,LOAD)"), Term.createTerm("activate(TYPE,LOAD)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								
												Container = payloadArg(0)
												Load      = payloadArg(1).toLong()
						}
						println("Going INDOOR")
					}
					 transition( edgeName="goto",targetState="pickup", cond=doswitch() )
				}	 
				state("pickup") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						println("Pickup done")
						answer("activate", "pickupDone", "pickupDone(ok)"   )  
					}
					 transition( edgeName="goto",targetState="container", cond=doswitch() )
				}	 
				state("container") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						println("Going to $Container container")
					}
					 transition( edgeName="goto",targetState="loaddeposit", cond=doswitch() )
				}	 
				state("loaddeposit") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						println("Deposit Done")
						emit("loaddeposit", "loaddeposit($Container,$Load)" ) 
						stateTimer = TimerActor("timer_loaddeposit", 
							scope, context!!, "local_tout_transporttrolley_loaddeposit", 5.toLong() )
					}
					 transition(edgeName="t14",targetState="returnhome",cond=whenTimeout("local_tout_transporttrolley_loaddeposit"))   
					transition(edgeName="t15",targetState="handleactivate",cond=whenRequest("activate"))
				}	 
				state("returnhome") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						println("Going HOME")
					}
					 transition( edgeName="goto",targetState="s0", cond=doswitch() )
				}	 
			}
		}
}
