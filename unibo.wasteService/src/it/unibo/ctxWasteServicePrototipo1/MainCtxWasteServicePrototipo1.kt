/* Generated by AN DISI Unibo */ 
package it.unibo.ctxWasteServicePrototipo1
import it.unibo.kactor.QakContext
import it.unibo.kactor.sysUtil
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
	QakContext.createContexts(
	        "localhost", this, "wasteservice.pl", "sysRules.pl","ctxWasteServicePrototipo1"
	)
}

