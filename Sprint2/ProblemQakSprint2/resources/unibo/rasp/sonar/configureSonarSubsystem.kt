package sonarConfig

import DistanceObserver
import it.unibo.kactor.ActorBasic
import it.unibo.kactor.sysUtil
import java.io.File
import java.nio.charset.StandardCharsets
import org.json.JSONObject
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig
import it.unibo.radarSystem22.domain.interfaces.ISonarForObs


lateinit var firstActorInPipe : ActorBasic
lateinit var Sonar : ISonarForObs

fun createThePipe( simulate : Boolean, sonarActorName : String  ){
    if( DomainSystemConfig.simulation  )  firstActorInPipe = sysUtil.getActor("sonarsimulator")!!  //generates simulated data
    else firstActorInPipe           = sysUtil.getActor("sonardatasource")!!  //generates REAL data
    firstActorInPipe.
        subscribeLocalActor("datacleaner"). 		//removes 'wrong' data''
        subscribeLocalActor(sonarActorName)
}


fun configureTheSonar(simulate : Boolean=true, sonarActorName : String = "sonar"){
    configureDomainSystem()
	createThePipe(simulate, sonarActorName)
}

fun configureDomainSystem() {
	var config = File("DomainSystemConfig.json").readText(StandardCharsets.UTF_8)
	var jsonObj = JSONObject(config)
	
	DomainSystemConfig.simulation 		= jsonObj.getBoolean("SIMULATION")
	DomainSystemConfig.testing			= jsonObj.getBoolean("TESTING")
	
//	DomainSystemConfig.sonarType		= jsonObj.getString("SONARTYPE")
	DomainSystemConfig.sonarDelay		= jsonObj.getInt("SONARDELAY")
	DomainSystemConfig.sonarDistanceMax = jsonObj.getInt("SONARDISTANCEMAX")
	DomainSystemConfig.sonarObservable  = jsonObj.getBoolean("SONAROBSERVABLE")
	DomainSystemConfig.testingDistance  = jsonObj.getInt("TESTINGDISTANCE")
}