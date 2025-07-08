import io.javalin.Javalin
import io.javalin.http.Context
import model.UnsStatusDao
import java.sql.SQLException

class UnsStatusController {
    private val unsStatusDao = UnsStatusDao()

    fun registerRoutes(app: Javalin) {
        app.get("/statuses") { ctx: Context ->
            try {
                ctx.json(unsStatusDao.allStatuses)
            } catch (e: SQLException) {
                ctx.status(500).result("Database error")
                println("Database error: $e")
            }
        }
    }
}
