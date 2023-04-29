package unibo.rasp.utils

import unibo.comm22.utils.ColorsOut

object PayloadUtils {

    fun getTrolleyState(payload : String) : String {
        if (payload != "") {
            ColorsOut.outappl(payload.substring(payload.indexOf('(') + 1,payload.indexOf(')') ), ColorsOut.ANSI_PURPLE)
            return payload.substring(payload.indexOf('(') + 1,payload.indexOf(')'))
        }

        return payload
    }
}