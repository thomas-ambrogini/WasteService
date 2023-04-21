/* Generated by AN DISI Unibo */ 
package it.unibo.led

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Led ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "start"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		val interruptedStateTransitions = mutableListOf<Transition>()
		
			   val simulate       = true
			   val ledActorName = "led"
		return { //this:ActionBasciFsm
				state("start") { //this:State
					action { //it:State
						ledConfig.configureTheLed( simulate, ledActorName  )
						updateResourceRep( "ledState(off)"  
						)
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="listen", cond=doswitch() )
				}	 
				state("listen") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t00",targetState="handle_on",cond=whenDispatch("turnon"))
					transition(edgeName="t01",targetState="handle_off",cond=whenDispatch("turnoff"))
					transition(edgeName="t02",targetState="handle_blink_on",cond=whenDispatch("blink"))
				}	 
				state("handle_on") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						if(  `it.unibo`.radarSystem22.domain.utils.DomainSystemConfig.simulation  
						 ){forward("turnon", "turnon(on)" ,"ledsimulator" ) 
						}
						else
						 {forward("turnon", "turnon(on)" ,"ledconcrete" ) 
						 }
						updateResourceRep( "ledState(on)"  
						)
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="listen", cond=doswitch() )
				}	 
				state("handle_off") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						if(  `it.unibo`.radarSystem22.domain.utils.DomainSystemConfig.simulation  
						 ){forward("turnoff", "turnoff(on)" ,"ledsimulator" ) 
						}
						else
						 {forward("turnoff", "turnoff(on)" ,"ledconcrete" ) 
						 }
						updateResourceRep( "ledState(off)"  
						)
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="listen", cond=doswitch() )
				}	 
				state("handle_blink_on") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						if(  `it.unibo`.radarSystem22.domain.utils.DomainSystemConfig.simulation  
						 ){forward("turnon", "turnon(blink)" ,"ledsimulator" ) 
						}
						else
						 {forward("turnon", "turnon(blink)" ,"ledconcrete" ) 
						 }
						updateResourceRep( "ledState(blink)"  
						)
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
				 	 		stateTimer = TimerActor("timer_handle_blink_on", 
				 	 					  scope, context!!, "local_tout_led_handle_blink_on", 500.toLong() )
					}	 	 
					 transition(edgeName="t03",targetState="handle_blink_off",cond=whenTimeout("local_tout_led_handle_blink_on"))   
					transition(edgeName="t04",targetState="handle_on",cond=whenDispatch("turnon"))
					transition(edgeName="t05",targetState="handle_off",cond=whenDispatch("turnoff"))
				}	 
				state("handle_blink_off") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						if(  `it.unibo`.radarSystem22.domain.utils.DomainSystemConfig.simulation  
						 ){forward("turnoff", "turnoff(blink)" ,"ledsimulator" ) 
						}
						else
						 {forward("turnoff", "turnoff(blink)" ,"ledconcrete" ) 
						 }
						updateResourceRep( "ledState(blink)"  
						)
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
				 	 		stateTimer = TimerActor("timer_handle_blink_off", 
				 	 					  scope, context!!, "local_tout_led_handle_blink_off", 500.toLong() )
					}	 	 
					 transition(edgeName="t06",targetState="handle_blink_on",cond=whenTimeout("local_tout_led_handle_blink_off"))   
					transition(edgeName="t07",targetState="handle_on",cond=whenDispatch("turnon"))
					transition(edgeName="t08",targetState="handle_off",cond=whenDispatch("turnoff"))
				}	 
			}
		}
}