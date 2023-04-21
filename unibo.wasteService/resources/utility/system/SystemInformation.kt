package utility.system

import org.json.JSONObject
import java.io.File
import java.nio.charset.StandardCharsets

enum class Material {
    PLASTIC, GLASS
}


data class SystemInformation(
    private val boxMaxWeight : MutableMap<Material, Double>
) {


    companion object {
        private val boxMaxWeight : MutableMap<Material, Double> = Material.values().associateWith {0.0}.toMutableMap()

        init {
            val config = File("wasteServiceConfig.json").readText(StandardCharsets.UTF_8)
            val jsonObject = JSONObject(config)
            val maxPb = jsonObject.getDouble("MAXPB")
            val maxGb = jsonObject.getDouble("MAXGB")
            boxMaxWeight[Material.PLASTIC] = boxMaxWeight[Material.PLASTIC]!!.plus(maxPb)
            boxMaxWeight[Material.GLASS]   = boxMaxWeight[Material.GLASS]!!.plus(maxGb)
        }

        fun getMaxBoxWeights() : MutableMap<Material, Double> {
            return boxMaxWeight
        }
    }
}

