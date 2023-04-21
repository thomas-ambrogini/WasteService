/* Generated by AN DISI Unibo */ 
package it.unibo.navigator

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Navigator ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

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
					 transition(edgeName="t06",targetState="findCord",cond=whenRequest("getCord"))
				}	 
				state("findCord") { //this:State
					action { //it:State
						
									var Destination = ""
						if( checkMsgContent( Term.createTerm("getCord(DESTINATION)"), Term.createTerm("getCord(DESTINATION)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								
												Destination = payloadArg(0)
												val X_Cord = positionUt.getCordX(Destination)
												val Y_Cord = positionUt.getCordY(Destination)
								answer("getCord", "cord", "cord($X_Cord,$Y_Cord)"   )  
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="s0", cond=doswitch() )
				}	 
			}
		}
}
