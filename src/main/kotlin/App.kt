import io.javalin.Javalin

/**
 * curl http://localhost:8001/events
 * curl -d '{"agencyID":"ca-yolo-da","status":"broken"}' http://localhost:8001/events
 */
object App {
    private const val port = 8001

    @JvmStatic
    fun main(args: Array<String>) {
        println("Starting application: localhost:$port/events")
        val app = Javalin.create().start(port)
        val unsEventController = UnsEventController()
        unsEventController.registerRoutes(app)
    }
}
