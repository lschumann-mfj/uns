import io.javalin.Javalin
import java.sql.DriverManager

/**
 * curl http://localhost:8001/events
 * curl -d '{"agencyID":"ca-yolo-da","status":"broken"}' http://localhost:8001/events
 */
object App {
    private const val APP_PORT = 8001
    private const val POSTGRES_PORT = 8765
    private const val JDBC_URL = "jdbc:postgresql://localhost:$POSTGRES_PORT/postgres"
    private const val JDBC_USER = "postgres"
    private const val JDBC_PASSWORD = "flashmob"

    fun getConnection() = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)

    @JvmStatic
    fun main(args: Array<String>) {
        println("Starting application: localhost:$APP_PORT/events")
        val app = Javalin.create().start(APP_PORT)

        val unsEventController = UnsEventController()
        unsEventController.registerRoutes(app)

        val unsStatusController = UnsStatusController()
        unsStatusController.registerRoutes(app)
    }
}
