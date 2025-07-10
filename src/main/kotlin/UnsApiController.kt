import io.javalin.Javalin
import io.javalin.http.Context
import model.UnsEvent
import model.UnsEventDao
import model.UnsStatusDao
import java.sql.SQLException

class UnsApiController {
    fun registerRoutes(app: Javalin) {
        app.post("/events") { ctx: Context ->
            try {
                val unsEvent = ctx.bodyAsClass(UnsEvent::class.java)
                UnsEventDao.createEvent(unsEvent)
                ctx.status(201).result("Event created")
            } catch (e: SQLException) {
                ctx.status(500).result("Database error")
                println("Database error: $e")
            }
        }

        app.get("/events") { ctx: Context ->
            try {
                ctx.json(UnsEventDao.allEvents)
            } catch (e: SQLException) {
                ctx.status(500).result("Database error")
                println("Database error: $e")
            }
        }

        app.get("/events/{id}") { ctx: Context ->
            try {
                val id = ctx.pathParam("id")
                val event = UnsEventDao.getEvent(id)
                if (event != null) {
                    ctx.json(event)
                } else {
                    ctx.status(404).result("Event not found")
                }
            } catch (e: SQLException) {
                ctx.status(500).result("Database error")
                println("Database error: $e")
            }
        }

        app.get("/statuses") { ctx: Context ->
            try {
                ctx.json(UnsStatusDao.allStatuses)
            } catch (e: SQLException) {
                ctx.status(500).result("Database error")
                println("Database error: $e")
            }
        }
    }
}
