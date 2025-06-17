package model

import java.sql.DriverManager
import java.sql.SQLException

class UnsEventDao {
    private fun getConnection() = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)
    private val tableNameEvent = "events"

    @Throws(SQLException::class)
    fun createCourse(unsEvent: UnsEvent) {
        getConnection().use { conn ->
            conn.prepareStatement(
                "INSERT INTO $tableNameEvent (agency_id, courseDuration, courseFee) VALUES (?, ?, ?)"
            ).use { stmt ->
                stmt.setString(1, unsEvent.agencyID)
                stmt.setString(2, unsEvent.courseDuration)
                stmt.setInt(3, unsEvent.courseFee)
                println(stmt)
                stmt.executeUpdate()
            }
        }
    }

    @Throws(SQLException::class)
    fun getCourse(id: Int): UnsEvent? {
        var unsEvent: UnsEvent? = null
        getConnection().use { conn ->
            conn.prepareStatement("SELECT * FROM $tableNameEvent WHERE id = ?").use { stmt ->
                stmt.setInt(1, id)
                val rs = stmt.executeQuery()
                if (rs.next()) {
                    unsEvent = UnsEvent(
                        rs.getInt("id"),
                        rs.getString("agency_id"),
                        rs.getString("courseDuration"),
                        rs.getInt("courseFee")
                    )
                }
            }
        }
        return unsEvent
    }

    @get:Throws(SQLException::class)
    val allCours: List<UnsEvent>
        get() {
            val cours: MutableList<UnsEvent> = ArrayList()
            getConnection().use { conn ->
                conn.createStatement().use { stmt ->
                    stmt.executeQuery("SELECT * FROM $tableNameEvent").use { rs ->
                        while (rs.next()) {
                            cours.add(
                                UnsEvent(
                                    rs.getInt("id"),
                                    rs.getString("agency_id"),
                                    rs.getString("courseDuration"),
                                    rs.getInt("courseFee")
                                )
                            )
                        }
                    }
                }
            }
            return cours
        }

    @Throws(SQLException::class)
    fun updateCourse(unsEvent: UnsEvent) {
        getConnection().use { conn ->
            conn.prepareStatement(
                "UPDATE $tableNameEvent SET agency_id = ?, courseDuration = ?, courseFee = ? WHERE id = ?"
            ).use { stmt ->
                stmt.setString(1, unsEvent.agencyID)
                stmt.setString(2, unsEvent.courseDuration)
                stmt.setInt(3, unsEvent.courseFee)
                stmt.setInt(4, unsEvent.id)
                stmt.executeUpdate()
            }
        }
    }

    @Throws(SQLException::class)
    fun deleteCourse(id: Int) {
        getConnection().use { conn ->
            conn.prepareStatement("DELETE FROM $tableNameEvent WHERE id = ?").use { stmt ->
                stmt.setInt(1, id)
                stmt.executeUpdate()
            }
        }
    }

    companion object {
        private const val JDBC_URL = "jdbc:postgresql://127.0.0.1:8765/postgres"
        private const val JDBC_USER = "postgres"
        private val JDBC_PASSWORD = "flashmob"
    }
}