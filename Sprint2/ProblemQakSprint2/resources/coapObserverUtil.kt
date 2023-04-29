import it.unibo.kactor.ActorBasic
import it.unibo.kactor.QakContext
import it.unibo.kactor.sysUtil
import unibo.comm22.coap.CoapConnection
import unibo.comm22.interfaces.Interaction2021


object coapObserverUtil {
    private val actorResourceConnections: MutableMap<Pair<ActorBasic, String>, Interaction2021>
        = mutableMapOf()

    fun observe(actor: ActorBasic, resourceHost: String, resourceUri: String) {
        startObserve(actor, resourceHost, resourceUri, resourceUri.replace("/", "_"))
    }

    private fun startObserve(actor: ActorBasic, resourceHost: String, resourceUri: String, resourceName: String) {
        val key = Pair(actor, "$resourceHost/$resourceUri")
        if (actorResourceConnections.containsKey(key)) {
            throw IllegalArgumentException("Connection for $key already exists")
        }

        val connection = CoapConnection(resourceHost, resourceUri)
        connection.observeResource(CoapObserverActor(resourceName, actor))
        actorResourceConnections[key] = connection
    }

    
}