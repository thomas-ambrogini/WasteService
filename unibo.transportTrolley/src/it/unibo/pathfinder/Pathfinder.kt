/* Generated by AN DISI Unibo */ 
package it.unibo.pathfinder

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Pathfinder ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "s0"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		val interruptedStateTransitions = mutableListOf<Transition>()
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						 
									unibo.kotlin.planner22Util.initAI();
									unibo.kotlin.planner22Util.loadRoomMap("mapRoomEmpty");
									unibo.kotlin.planner22Util.showMap();
									unibo.kotlin.planner22Util.showCurrentRobotState();
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t021",targetState="findThePath",cond=whenRequest("findPath"))
				}	 
				state("findThePath") { //this:State
					action { //it:State
						
									var MovesToDo = ""	
						println("$name in ${currentState.stateName} | $currentMsg")
						if( checkMsgContent( Term.createTerm("findPath(X_DESTINATION,Y_DESTINATION)"), Term.createTerm("findPath(X_DESTINATION,Y_DESTINATION)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								
												val X_Destination = payloadArg(0)
												val Y_Destination = payloadArg(1)
												unibo.kotlin.planner22Util.planForGoal( X_Destination, Y_Destination )
												MovesToDo = unibo.kotlin.planner22Util.getActions().joinToString("")
												pathut.setPath(MovesToDo)
												pathut.updateMap()
						}
						println(MovesToDo)
						request("dopath", "dopath($MovesToDo)" ,"pathexec" )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t122",targetState="pathDone",cond=whenReply("dopathdone"))
					transition(edgeName="t123",targetState="pathFail",cond=whenReply("dopathfail"))
				}	 
				state("pathDone") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						answer("findPath", "dopathdone", "dopathdone(ok)"   )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t224",targetState="findThePath",cond=whenRequest("findPath"))
				}	 
				state("pathFail") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						if( checkMsgContent( Term.createTerm("dopathfail(ARG)"), Term.createTerm("dopathfail(ARG)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								
												val Remaining = payloadArg(0)	
								answer("findPath", "dopathfail", "dopathfail(${payloadArg(0)})"   )  
						}
						println("PATH FAILED")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t225",targetState="findThePath",cond=whenRequest("findPath"))
				}	 
			}
		}
}
