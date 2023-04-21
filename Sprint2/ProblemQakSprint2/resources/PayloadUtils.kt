import unibo.comm22.utils.ColorsOut

object PayloadUtils {

    fun getTrolleyState(payload : String) : String {
        if (payload != "") {
            return payload.substring(payload.indexOf('(') + 1,payload.indexOf(')'))
        }

        return payload
    }

    fun getTrolleyPosition(payload : String) : String {
        if (payload != "") {
            return payload.substring(payload.indexOf('(') + 1,payload.indexOf(')'))
        }

        return payload
    }
}