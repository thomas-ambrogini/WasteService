package utility.storage_manager

import utility.system.Material
import utility.system.SystemInformation

class StorageManagerUtility {

    private var currentWeights : MutableMap<Material, Double> = Material.values().associateWith { 0.0 }.toMutableMap()
    private var maxWeights     = mutableMapOf<Material, Double>()
    private val offsetWeights  : MutableMap<Material, Double> = Material.values().associateWith { 0.0 }.toMutableMap()

    init {
        maxWeights = SystemInformation.getMaxBoxWeights()
    }

    fun getWeight(material: Material) : Double {
        return currentWeights[material]!!
    }

    fun getWeight(material: String) : Double {
        return currentWeights[Material.valueOf(material.uppercase())]!!
    }

    fun getMax(material: Material) : Double {
        return maxWeights[material]!!
    }

    fun checkDeposit(material: Material, depositAmount: Double) : Boolean {
        val currentAmount = currentWeights[material]!!
        val maxAmount     = maxWeights[material]!!
        val offsetAmount  = offsetWeights[material]!!


        if (currentAmount + depositAmount + offsetAmount <= maxAmount) {
            offsetWeights[material] = offsetAmount + depositAmount
            return true
        }
        return false
    }

    fun checkDeposit(material: String, depositAmount: Double) : Boolean {
        val currentAmount = currentWeights[Material.valueOf(material.uppercase())]!!
        val maxAmount     = maxWeights[Material.valueOf(material.uppercase())]!!
        val offsetAmount  = offsetWeights[Material.valueOf(material.uppercase())]!!


        if (currentAmount + depositAmount + offsetAmount <= maxAmount) {
            offsetWeights[Material.valueOf(material.uppercase())] = offsetAmount + depositAmount
            return true
        }
        return false
    }

    fun deposit(): Unit {
        for (material in Material.values() ) {
            currentWeights[material] = currentWeights[material]!! + offsetWeights[material]!!
            offsetWeights[material] = 0.0
        }
    }
}