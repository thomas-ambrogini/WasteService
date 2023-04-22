/* Generated by AN DISI Unibo */ 
package it.unibo.wasteservice

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Wasteservice ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "s0"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		val interruptedStateTransitions = mutableListOf<Transition>()
		
				var Type                 = ""
				var Accepted             = "False"
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						updateResourceRep( "trolleyPosition(home)"  
						)
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t03",targetState="handleTruck",cond=whenRequest("storeload"))
				}	 
				state("handleTruck") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						if( checkMsgContent( Term.createTerm("storeload(TYPE,LOAD)"), Term.createTerm("storeload(TYPE,LOAD)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								
												Type   = payloadArg(0)
												val Weight = payloadArg(1).toLong()	
								request("storeRequest", "storeRequest($Type,$Weight)" ,"storage_manager" )  
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t14",targetState="handleStorageReply",cond=whenReply("storeRequestReply"))
				}	 
				state("handleStorageReply") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						if( checkMsgContent( Term.createTerm("storeRequestReply(ANS)"), Term.createTerm("storeRequestReply(ANS)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								if(  payloadArg(0).equals("accepted")  
								 ){ Accepted = "True" 
								answer("storeload", "loadaccepted", "loadaccepted(_,_)"   )  
								}
								else
								 { Accepted = "False" 
								 answer("storeload", "loadrejected", "loadrejected(_,_)"   )  
								 }
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="moveTrolleyIndoor", cond=doswitchGuarded({ Accepted.equals("True")  
					}) )
					transition( edgeName="goto",targetState="s0", cond=doswitchGuarded({! ( Accepted.equals("True")  
					) }) )
				}	 
				state("moveTrolleyIndoor") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						
									val X_Destination = positionUt.getCordX("indoor")
									val Y_Destination = positionUt.getCordY("indoor")
						request("moveToDestination", "info($X_Destination,$Y_Destination)" ,"transporttrolley" )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t25",targetState="handlepickup",cond=whenReply("destinationReached"))
				}	 
				state("handlepickup") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						updateResourceRep( "trolleyPosition(indoor)"  
						)
						request("pickup", "pickup(true)" ,"transporttrolley" )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t116",targetState="handle_pickupDone",cond=whenReply("pickupDone"))
				}	 
				state("handle_pickupDone") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="moveTrolleyContainer", cond=doswitch() )
				}	 
				state("moveTrolleyContainer") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						
									val X_Destination = positionUt.getCordX(Type)
									val Y_Destination = positionUt.getCordY(Type)
						request("moveToDestination", "info($X_Destination,$Y_Destination)" ,"transporttrolley" )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t47",targetState="handleDeposit",cond=whenReply("destinationReached"))
				}	 
				state("handleDeposit") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						updateResourceRep( "trolleyPosition($Type)"  
						)
						request("deposit", "deposit(true)" ,"transporttrolley" )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t118",targetState="handle_depositDone",cond=whenReply("depositDone"))
				}	 
				state("handle_depositDone") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						forward("updateWeights", "updateWeights(_)" ,"storage_manager" ) 
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
				 	 		stateTimer = TimerActor("timer_handle_depositDone", 
				 	 					  scope, context!!, "local_tout_wasteservice_handle_depositDone", 10.toLong() )
					}	 	 
					 transition(edgeName="t59",targetState="moveTrolleyHome",cond=whenTimeout("local_tout_wasteservice_handle_depositDone"))   
					transition(edgeName="t510",targetState="handleTruck",cond=whenRequest("storeload"))
				}	 
				state("moveTrolleyHome") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						
									val X_Destination = positionUt.getCordX("home")
									val Y_Destination = positionUt.getCordY("home")
						request("moveToDestination", "info($X_Destination,$Y_Destination)" ,"transporttrolley" )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t1211",targetState="s0",cond=whenReply("destinationReached"))
				}	 
			}
		}
}
