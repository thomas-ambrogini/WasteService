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
		return { //this:ActionBasciFsm
				state("init") { //this:State
					action { //it:State
						CoapObserverSupport(myself, "localhost","8051","ctx_transporttrolley","transporttrolley_mover")
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
					 transition(edgeName="t07",targetState="handleStatus",cond=whenDispatch("coapUpdate"))
				}	 
				state("handleStatus") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						if( checkMsgContent( Term.createTerm("coapUpdate(RESOURCE,VALUE)"), Term.createTerm("coapUpdate(RESOURCE,VALUE)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								println("Led Controller | Received update from ${payloadArg(0)}, is ${payloadArg(1)} ; currently AtHome $AtHome Stopped $Stopped")
								 var NextLedState = ""  
								if(  payloadArg(0) == "transporttrolley_mover"  
								 ){ 
													Stopped = PayloadUtils.getTrolleyState(payloadArg(1)) == "stopped" 
								if(  Stopped  
								 ){ 
														NextLedState = "off"
								}
								else
								 {if(  !AtHome  
								  ){ NextLedState = "blinking"  
								 }
								 else
								  { NextLedState = "on"  
								  }
								 }
								}
								if(  NextLedState == "on"  
								 ){println("Led Controller | Setting led to $NextLedState")
								forward("turnon", "turnon(on)" ,"led" ) 
								}
								else
								 {if(  NextLedState == "off"  
								  ){forward("turnoff", "turnoff(off)" ,"led" ) 
								 }
								 else
								  {if(  NextLedState == "blinking"  
								   ){forward("blink", "blink(blink)" ,"led" ) 
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
