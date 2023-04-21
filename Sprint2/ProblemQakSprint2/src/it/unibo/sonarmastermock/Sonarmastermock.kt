/* Generated by AN DISI Unibo */ 
package it.unibo.sonarmastermock

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Sonarmastermock ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "s0"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		val interruptedStateTransitions = mutableListOf<Transition>()
		
			
				val SonarTimeout =	DomainSystemConfig.getSonarTimeout()
				
				val SonarEndless =  DomainSystemConfig.isSonarEndless()
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						forward("sonaractivate", "info(ok)" ,"sonarqak22" ) 
						if(  !SonarEndless 
						 ){delay(SonarTimeout)
						forward("sonardeactivate", "info(ok)" ,"sonarqak22" ) 
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
				}	 
			}
		}
}
