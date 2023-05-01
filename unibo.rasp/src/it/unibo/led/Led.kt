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
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi			
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		
			   val simulate       = true
			   val ledActorName = "led"
		return { //this:ActionBasciFsm
				state("start") { //this:State
					action { //it:State
						ledConfig.configureTheLed( simulate, ledActorName  )
						updateResourceRep( "ledState(off)"  
						)
					}
					 transition( edgeName="goto",targetState="listen", cond=doswitch() )
				}	 
				state("listen") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
					}
					 transition(edgeName="t040",targetState="handle_on",cond=whenDispatch("turnon"))
					transition(edgeName="t041",targetState="handle_off",cond=whenDispatch("turnoff"))
					transition(edgeName="t042",targetState="handle_blink_on",cond=whenDispatch("blink"))
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
						stateTimer = TimerActor("timer_handle_blink_on", 
							scope, context!!, "local_tout_led_handle_blink_on", 500.toLong() )
					}
					 transition(edgeName="t043",targetState="handle_blink_off",cond=whenTimeout("local_tout_led_handle_blink_on"))   
					transition(edgeName="t044",targetState="handle_on",cond=whenDispatch("turnon"))
					transition(edgeName="t045",targetState="handle_off",cond=whenDispatch("turnoff"))
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
						stateTimer = TimerActor("timer_handle_blink_off", 
							scope, context!!, "local_tout_led_handle_blink_off", 500.toLong() )
					}
					 transition(edgeName="t046",targetState="handle_blink_on",cond=whenTimeout("local_tout_led_handle_blink_off"))   
					transition(edgeName="t047",targetState="handle_on",cond=whenDispatch("turnon"))
					transition(edgeName="t048",targetState="handle_off",cond=whenDispatch("turnoff"))
				}	 
			}
		}
}
