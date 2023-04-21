import org.json.JSONObject
import java.io.File
import java.nio.charset.StandardCharsets

object utils {
    fun read_host() : String {
        val config = File("raspConfig.json").readText(StandardCharsets.UTF_8)
        val jsonObject = JSONObject(config)
        val host = jsonObject.getString("Host")

        return host
    }
}