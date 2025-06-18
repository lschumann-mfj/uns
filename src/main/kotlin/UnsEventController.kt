import io.javalin.Javalin
import io.javalin.http.Context
import model.UnsEvent
import model.UnsEventDao
import java.sql.SQLException

class UnsEventController {
    private val unsEventDao = UnsEventDao()

    fun registerRoutes(app: Javalin) {
        app.post("/events") { ctx: Context ->
            try {
                val unsEvent = ctx.bodyAsClass(UnsEvent::class.java)
                unsEventDao.createEvent(unsEvent)
                ctx.status(201).result("Event created")
            } catch (e: SQLException) {
                ctx.status(500).result("Database error")
                println("Database error: $e")
            }
        }

        app.get("/events/{id}") { ctx: Context ->
            try {
                val id = ctx.pathParam("id").toInt()
                val event = unsEventDao.getEvent(id)
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

        app.get("/events") { ctx: Context ->
            try {
                ctx.json(unsEventDao.allEvents)
            } catch (e: SQLException) {
                ctx.status(500).result("Database error")
                println("Database error: $e")
            }
        }

        app.put("/events/{id}") { ctx: Context ->
            try {
                val id = ctx.pathParam("id").toInt()
                val unsEvent = ctx.bodyAsClass(UnsEvent::class.java)
                unsEvent.id = id
                unsEventDao.updateEvent(unsEvent)
                ctx.result("Event updated")
            } catch (e: SQLException) {
                ctx.status(500).result("Database error")
                println("Database error: $e")
            }
        }
    }
}
