package utils

import org.json.JSONObject
import java.io.File

object DomainSystemConfig {

     private var simulation : Boolean = true
     private var sonarTimeout : Long = 10000
	 private var sonarEndless : Boolean = true
	 private var dLimit : Int = 40
     private var wasteServiceAddress : String = "localhost"
     private var wasteServicePort : Int = 8033
    init {


        try {
            val config = File("DomainSystemConfig.json").readText(Charsets.UTF_8)
            val jsonObject   = JSONObject( config )
            simulation = jsonObject.getBoolean("simulation")
            sonarTimeout = jsonObject.getLong("sonarTimeout")
		
            sonarEndless = jsonObject.getBoolean("sonarEndless")
			dLimit = jsonObject.getInt("dLimit")

            wasteServicePort = jsonObject.getInt("wasteServicePort")
            wasteServiceAddress = jsonObject.getString("wasteServiceAddress")
        } catch (e : Exception) {
            println(" ${this.javaClass.name}  | ${e.localizedMessage}, activate simulation by default")
        }
    }


    /*
    *
    *  Come default restituisco simulation=true se non trovo il file
    *
    * */

    fun isSimulation() : Boolean{
        return simulation
    }
	
	 fun isSonarEndless() : Boolean{
        return sonarEndless
    }

    fun getSonarTimeout() : Long {
        return sonarTimeout
    }
	fun getDLimit() : Int {
		
		return dLimit
	}

    fun getWasteServiceAddress() : String {
        return  wasteServiceAddress
    }

    fun  getWasteServicePort() : Int {
        return  wasteServicePort
    }
}