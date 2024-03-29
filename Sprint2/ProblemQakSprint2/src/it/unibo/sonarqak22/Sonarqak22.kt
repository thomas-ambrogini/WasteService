/* Generated by AN DISI Unibo */ 
package it.unibo.sonarqak22

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Sonarqak22 ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "s0"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		val interruptedStateTransitions = mutableListOf<Transition>()
		 
			   val simulate       = true
			   val sonarActorName = "sonarqak22"
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						sonarConfig.configureTheSonar( simulate, sonarActorName  )
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="idle", cond=doswitch() )
				}	 
				state("idle") { //this:State
					action { //it:State
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t09",targetState="activateTheSonar",cond=whenDispatch("sonaractivate"))
					transition(edgeName="t010",targetState="deactivateTheSonar",cond=whenDispatch("sonardeactivate"))
				}	 
				state("activateTheSonar") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						if(   `it.unibo`.radarSystem22.domain.utils.DomainSystemConfig.simulation  
						 ){forward("sonaractivate", "info(ok)" ,"sonarsimulator" ) 
						}
						else
						 {forward("sonaractivate", "info(ok)" ,"sonardatasource" ) 
						 }
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t011",targetState="handleSonarData",cond=whenEvent("sonar"))
					transition(edgeName="t012",targetState="deactivateTheSonar",cond=whenDispatch("sonardeactivate"))
				}	 
				state("deactivateTheSonar") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="end", cond=doswitch() )
				}	 
				state("handleSonarData") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						if( checkMsgContent( Term.createTerm("distance(V)"), Term.createTerm("distance(D)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 val D = payloadArg(0)  
								println("%%%%%%%%%%%%%%%%%%%%%%%%%%% emit %%%%%%%%%%%%%%% ${D}")
								emit("sonardata", "distance($D)" ) 
								updateResourceRep( "$D"  
								)
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t013",targetState="handleSonarData",cond=whenEvent("sonar"))
					transition(edgeName="t014",targetState="deactivateTheSonar",cond=whenDispatch("sonardeactivate"))
				}	 
				state("end") { //this:State
					action { //it:State
						println("sonarqak22 Deactivated")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="idle", cond=doswitch() )
				}	 
			}
		}
}
