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
                unsEventDao.createCourse(unsEvent)
                ctx.status(201).result("Event created")
            } catch (e: SQLException) {
                ctx.status(500).result("Database error")
                println("Database error: $e")
            }
        }

        app.get("/events/{id}") { ctx: Context ->
            try {
                val id = ctx.pathParam("id").toInt()
                val course = unsEventDao.getCourse(id)
                if (course != null) {
                    ctx.json(course)
                } else {
                    ctx.status(404).result("Event not found")
                }
            } catch (e: SQLException) {
                ctx.status(500).result("Database error")
                println("Database error $e")
            }
        }

        app.get("/events") { ctx: Context ->
            try {
                val courses = unsEventDao.allCours
                ctx.json(courses)
            } catch (e: SQLException) {
                println("Database error: $e")
                ctx.status(500).result("Database error")
            }
        }

        app.put("/events/{id}") { ctx: Context ->
            try {
                val id = ctx.pathParam("id").toInt()
                val unsEvent = ctx.bodyAsClass(UnsEvent::class.java)
                unsEvent.id = id
                unsEventDao.updateCourse(unsEvent)
                ctx.result("Event updated")
            } catch (e: SQLException) {
                ctx.status(500).result("Database error")
                println("Database error: $e")
            }
        }

        app.delete("/events/{id}") { ctx: Context ->
            try {
                val id = ctx.pathParam("id").toInt()
                unsEventDao.deleteCourse(id)
                ctx.result("Event deleted")
            } catch (e: SQLException) {
                ctx.status(500).result("Database error")
                println("Database error: $e")
            }
        }
    }
}
