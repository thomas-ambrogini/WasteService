 /*
 pathutil.kt
 */

import org.json.JSONObject
import it.unibo.kactor.*
import java.util.Scanner
import alice.tuprolog.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

import java.io.PrintWriter
import java.io.FileWriter
import java.io.ObjectOutputStream
import java.io.FileOutputStream
import java.io.ObjectInputStream
import java.io.FileInputStream
import unibo.planner22.model.*
import unibo.kotlin.*

object positionUt {
	var pathDone = ""
	var curMove  = "unknown"	
	var curPath  = ""
	var wenvAddr = "localhost"
	lateinit var  master: ActorBasicFsm
	lateinit var  owner:  String
	val positionMap : Map<String, Pair<Int, Int>>
	
	init {
		positionMap = mutableMapOf<String, Pair<Int, Int>>()
		positionMap.put("glass", Pair(6,4))
		positionMap.put("plastic", Pair(5,0))
		positionMap.put("indoor", Pair(1,4))
		positionMap.put("home", Pair(0,0))
		
	}
	
	
	fun getCordX( position : String ) : String {
		val coord = positionMap.get(position)
		
		return coord!!.first.toString()
	}
	
	fun getCordY( position : String ) : String {
		val coord = positionMap.get(position)
		
		return coord!!.second.toString()
	}
	
}