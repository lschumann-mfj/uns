import io.javalin.Javalin
import io.mfj.mfjkext.logger

object App {
    private const val port = 8001
    private val log = logger()

    @JvmStatic
    fun main(args: Array<String>) {
        log.debug("Starting application: 127.0.0.1:$port")
        val app = Javalin.create().start(port)
        val unsEventController = UnsEventController()
        unsEventController.registerRoutes(app)
    }
}
