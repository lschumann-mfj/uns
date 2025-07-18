import io.javalin.Javalin
import java.sql.DriverManager

object App {
    private const val API_PORT = 8001
    private const val JDBC_URL = "jdbc:postgresql://localhost:8765/postgres"
    private const val JDBC_USER = "postgres"
    private const val JDBC_PASSWORD = "flashmob"

    fun getConnection() = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)!!

    @JvmStatic
    fun main(args: Array<String>) {
        println("Starting application: localhost:$API_PORT/")
        val app = Javalin.create().start(API_PORT)

        val unsApiController = UnsApiController()
        unsApiController.registerRoutes(app)
    }
}
