/* Generated by AN DISI Unibo */ 
package it.unibo.basicrobot

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Basicrobot ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "s0"
	}
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi			
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		
			  var StepTime      = 0L
			  var StartTime     = 0L     
			  var Duration      = 0L  
			  var RobotType     = "" 
			  var CurrentMove   = "unkknown"
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						discardMessages = false
						println("basicrobot | START")
						delay(5000) 
						unibo.robot.robotSupport.create(myself ,"basicrobotConfig.json" )
						 RobotType = unibo.robot.robotSupport.robotKind  
						delay(1000) 
						if(  RobotType != "virtual"  
						 ){ var robotsonar = context!!.hasActor("realsonar")  
						        	   if(robotsonar != null) unibo.robot.robotSupport.createSonarPipe(robotsonar) 
						}
						unibo.robot.robotSupport.move( "a"  )
						unibo.robot.robotSupport.move( "d"  )
						updateResourceRep( "basicrobot(start)"  
						)
					}
					 transition( edgeName="goto",targetState="work", cond=doswitch() )
				}	 
				state("work") { //this:State
					action { //it:State
						println("basicrobot  | waiting .................. ")
					}
					 transition(edgeName="t10",targetState="execcmd",cond=whenDispatch("cmd"))
					transition(edgeName="t11",targetState="doStep",cond=whenRequest("step"))
					transition(edgeName="t12",targetState="handleObstacle",cond=whenDispatch("obstacle"))
					transition(edgeName="t13",targetState="endwork",cond=whenDispatch("end"))
				}	 
				state("execcmd") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						if( checkMsgContent( Term.createTerm("cmd(MOVE)"), Term.createTerm("cmd(MOVE)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 CurrentMove = payloadArg(0)  
								unibo.robot.robotSupport.move( payloadArg(0)  )
								updateResourceRep( "moveactivated(${payloadArg(0)})"  
								)
						}
					}
					 transition( edgeName="goto",targetState="work", cond=doswitch() )
				}	 
				state("handleObstacle") { //this:State
					action { //it:State
						updateResourceRep( "obstacle(${CurrentMove})"  
						)
					}
					 transition( edgeName="goto",targetState="work", cond=doswitch() )
				}	 
				state("handleSonar") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
					}
					 transition( edgeName="goto",targetState="work", cond=doswitch() )
				}	 
				state("doStep") { //this:State
					action { //it:State
						println("$name in ${currentState.stateName} | $currentMsg")
						if( checkMsgContent( Term.createTerm("step(TIME)"), Term.createTerm("step(T)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
									StepTime     = payloadArg(0).toLong()  	 
								updateResourceRep( "step(${StepTime})"  
								)
						}
						StartTime = getCurrentTime()
						println("basicrobot | doStep StepTime =$StepTime  ")
						unibo.robot.robotSupport.move( "w"  )
						stateTimer = TimerActor("timer_doStep", 
							scope, context!!, "local_tout_basicrobot_doStep", StepTime )
					}
					 transition(edgeName="t04",targetState="stepDone",cond=whenTimeout("local_tout_basicrobot_doStep"))   
					transition(edgeName="t05",targetState="stepFail",cond=whenDispatch("obstacle"))
				}	 
				state("stepDone") { //this:State
					action { //it:State
						unibo.robot.robotSupport.move( "h"  )
						updateResourceRep( "stepDone($StepTime)"  
						)
						answer("step", "stepdone", "stepdone(ok)"   )  
						println("basicrobot | stepDone reply done")
					}
					 transition( edgeName="goto",targetState="work", cond=doswitch() )
				}	 
				state("stepFail") { //this:State
					action { //it:State
						unibo.robot.robotSupport.move( "h"  )
						Duration = getDuration(StartTime)
						 var TunedDuration   =  ((Duration * 0.80)).toLong()    
						println("basicrobot | stepFail duration=$Duration  TunedDuration=$TunedDuration")
						unibo.robot.robotSupport.move( "s"  )
						delay(TunedDuration)
						unibo.robot.robotSupport.move( "h"  )
						updateResourceRep( "stepFail($Duration)"  
						)
						answer("step", "stepfail", "stepfail($Duration,obst)"   )  
					}
					 transition( edgeName="goto",targetState="work", cond=doswitch() )
				}	 
				state("endwork") { //this:State
					action { //it:State
						updateResourceRep( "basicrobot(end)"  
						)
						terminate(1)
					}
				}	 
			}
		}
}
