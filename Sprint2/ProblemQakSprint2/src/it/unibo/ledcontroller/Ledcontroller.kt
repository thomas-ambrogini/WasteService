/* Generated by AN DISI Unibo */ 
package it.unibo.ledcontroller

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Ledcontroller ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "init"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		val interruptedStateTransitions = mutableListOf<Transition>()
		
				var AtHome = true
				var Stopped = false
				var CurrentLedState = "off"
				var State   = ""
		return { //this:ActionBasciFsm
				state("init") { //this:State
					action { //it:State
						coapObserverUtil.observe(myself ,"transporttrolley:8051", "ctx_transporttrolley/transporttrolley_mover" )
						coapObserverUtil.observe(myself ,"wasteservice:8049", "ctx_wasteservice/wasteservice" )
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="observe", cond=doswitch() )
				}	 
				state("observe") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t015",targetState="handleStatus",cond=whenDispatch("coapUpdate"))
				}	 
				state("handleStatus") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						if( checkMsgContent( Term.createTerm("coapUpdate(RESOURCE,VALUE)"), Term.createTerm("coapUpdate(RESOURCE,VALUE)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 
												var NextLedState 	= "" 
								println("Led Controller | Received update from ${payloadArg(0)}, is ${payloadArg(1)} ; currently AtHome $AtHome Stopped $Stopped CurrentState $CurrentLedState")
								if(  payloadArg(0) == "ctx_transporttrolley_transporttrolley_mover"  
								 ){ 
													State   = PayloadUtils.getTrolleyState(payloadArg(1))
													Stopped = State == "stopped" 
								if(  Stopped  
								 ){ 
														NextLedState = "on"
								}
								else
								 {if(  State == "work"  
								  ){ NextLedState = "blinking"  
								 }
								 else
								  { NextLedState = "off"  
								  }
								 }
								}
								if(  payloadArg(0) == "ctx_wasteservice_wasteservice"  
								 ){ AtHome = PayloadUtils.getTrolleyPosition(payloadArg(1)) == "home"  
								if(  !Stopped  
								 ){if(  AtHome || State == "idle" 
								 ){ NextLedState = "off"  
								}
								else
								 {if(  State == "work" 
								  ){ NextLedState = "blinking"  
								 }
								 }
								}
								}
								println("Led Controller | Setting led to $NextLedState")
								if(  NextLedState == "on"  
								 ){forward("turnon", "turnon(on)" ,"led" ) 
								
													CurrentLedState = "on"
								}
								else
								 {if(  NextLedState == "off"  
								  ){forward("turnoff", "turnoff(off)" ,"led" ) 
								 
								 						CurrentLedState = "off"
								 }
								 else
								  {if(  NextLedState == "blinking" && CurrentLedState != "blinking"  
								   ){forward("blink", "blink(blink)" ,"led" ) 
								  
								  							CurrentLedState = "blinking"
								  }
								  }
								 }
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="observe", cond=doswitch() )
				}	 
			}
		}
}
