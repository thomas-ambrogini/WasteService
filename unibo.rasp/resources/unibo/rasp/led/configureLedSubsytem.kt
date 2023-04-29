package ledConfig

import DistanceObserver
import it.unibo.kactor.ActorBasic
import it.unibo.radarSystem22.domain.interfaces.ILed
import java.io.File
import java.nio.charset.StandardCharsets
import org.json.JSONObject
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig


lateinit var Led : ILed


fun configureTheLed(simulate : Boolean=true, ledActorName : String = "led"){
    configureTheLedUsingDomain(simulate, ledActorName)
}

fun configureTheLedUsingDomain(simulate : Boolean, ledActorName : String ){
    configureDomainSystem()
}

fun configureDomainSystem() {
	var config = File("DomainSystemConfig.json").readText(StandardCharsets.UTF_8)
	var jsonObj = JSONObject(config)
	
	DomainSystemConfig.simulation 		= jsonObj.getBoolean("SIMULATION")
	DomainSystemConfig.testing			= jsonObj.getBoolean("TESTING")
	
	println(DomainSystemConfig.simulation)
	
}
