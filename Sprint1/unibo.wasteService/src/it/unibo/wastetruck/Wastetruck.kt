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
		val interruptedStateTransitions = mutableListOf<Transition>()
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="requestdischarge", cond=doswitch() )
				}	 
				state("requestdischarge") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						request("storeload", "storeload(glass,15)" ,"wasteservice" )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t11",targetState="accepted",cond=whenReply("loadaccepted"))
					transition(edgeName="t12",targetState="rejected",cond=whenReply("loadrejected"))
				}	 
				state("rejected") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						println("Request rejected")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
				}	 
				state("accepted") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						println("Request accepted")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
				}	 
			}
		}
}
