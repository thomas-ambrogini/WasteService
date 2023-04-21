import unibo.comm22.utils.ColorsOut

object PayloadUtils {

    fun getTrolleyState(payload : String) : String {
        if (payload != "") {
            ColorsOut.outappl(payload.substring(payload.indexOf('('),payload.indexOf(')') - 1), ColorsOut.ANSI_PURPLE)
            return payload.substring(payload.indexOf('('),payload.indexOf(')') - 1)
        }

        return payload
    }
}