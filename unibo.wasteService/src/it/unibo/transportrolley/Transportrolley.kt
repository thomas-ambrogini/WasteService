/* Generated by AN DISI Unibo */ 
package it.unibo.transportrolley

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Transportrolley ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "s0"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		
				var CurPosition = "home";
				var Container   = "";
				var StoppedPos  = "";
				var Status      = "working";
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						println("Waiting activation....")
					}
					 transition(edgeName="t04",targetState="findpathtoindoor",cond=whenDispatch("activate"))
				}	 
				state("findpathtoindoor") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						if( checkMsgContent( Term.createTerm("activate(TYPE)"), Term.createTerm("activate(TYPE)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								
												Container = payloadArg(0)
						}
						request("retrievePath", "retrievePath($CurPosition,indoor)" ,"pathfinder" )  
						println("Asking for a path to INDOOR")
					}
					 transition(edgeName="t15",targetState="goindoor",cond=whenReply("path"))
				}	 
				state("goindoor") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						if( checkMsgContent( Term.createTerm("path(PATH)"), Term.createTerm("path(PATH)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 val P = payloadArg(0) 
								println("The path to go to INDOOR is $P")
								println("Going INDOOR")
						}
						request("retrievePath", "retrievePath(indoor,$Container)" ,"pathfinder" )  
					}
					 transition(edgeName="t26",targetState="gocontainer",cond=whenReply("path"))
				}	 
				state("gocontainer") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						
									CurPosition = Container;	
						if( checkMsgContent( Term.createTerm("path(PATH)"), Term.createTerm("path(PATH)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 val PathToContainer = payloadArg(0) 
								println("The path to go to $Container is $PathToContainer")
								println("Going $Container container")
						}
						request("hasNext", "hasNext(next)" ,"wasteservice" )  
					}
					 transition( edgeName="goto",targetState="findpathtohome", cond=doswitch() )
				}	 
				state("findpathtohome") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						request("retrievePath", "retrievePath($CurPosition,home)" ,"pathfinder" )  
						println("Asking for a path to HOME")
					}
					 transition(edgeName="t47",targetState="gohome",cond=whenReply("path"))
				}	 
				state("gohome") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						
									CurPosition = "home";	
						if( checkMsgContent( Term.createTerm("path(PATH)"), Term.createTerm("path(PATH)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 val PathToHome = payloadArg(0) 
								println("The path to go to home is $PathToHome")
								println("Going HOME")
						}
					}
					 transition( edgeName="goto",targetState="s0", cond=doswitch() )
				}	 
			}
		}
}
