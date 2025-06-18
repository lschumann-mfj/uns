package model

import java.sql.DriverManager
import java.sql.SQLException

class UnsEventDao {
    private fun getConnection() = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)
    private val tableNameEvent = "events"
    private val createTableEvent = """
        create table $tableNameEvent
        (
            id        integer                  default nextval('courses_id_seq'::regclass) not null
                constraint ${tableNameEvent}_pk primary key,
            modified  timestamp with time zone default now()                               not null,
            agency_id text                                                                 not null,
            status    text                                                                 not null
        );
        
        alter table $tableNameEvent owner to postgres;
    """.trimIndent()

    @Throws(SQLException::class)
    fun createEvent(unsEvent: UnsEvent) {
        getConnection().use { conn ->
            conn.prepareStatement(
                "INSERT INTO $tableNameEvent (modified, agency_id, status) VALUES (now(), ?, ?)"
            ).use { stmt ->
                stmt.setString(1, unsEvent.agencyID)
                stmt.setString(2, unsEvent.status)
                println(stmt)
                stmt.executeUpdate()
            }
        }
    }

    @Throws(SQLException::class)
    fun getEvent(id: Int): UnsEvent? {
        var unsEvent: UnsEvent? = null
        getConnection().use { conn ->
            conn.prepareStatement("SELECT * FROM $tableNameEvent WHERE id = ?").use { stmt ->
                stmt.setInt(1, id)
                val rs = stmt.executeQuery()
                if (rs.next()) {
                    unsEvent = UnsEvent(
                        rs.getInt("id"),
                        rs.getString("modified"),
                        rs.getString("agency_id"),
                        rs.getString("status")
                    )
                }
            }
        }
        return unsEvent
    }

    @get:Throws(SQLException::class)
    val allEvents: List<UnsEvent>
        get() {
            val events: MutableList<UnsEvent> = ArrayList()
            getConnection().use { conn ->
                conn.createStatement().use { stmt ->
                    stmt.executeQuery("SELECT * FROM $tableNameEvent").use { rs ->
                        while (rs.next()) {
                            events.add(
                                UnsEvent(
                                    rs.getInt("id"),
                                    rs.getString("modified"),
                                    rs.getString("agency_id"),
                                    rs.getString("status")
                                )
                            )
                        }
                    }
                }
            }
            return events
        }

    @Throws(SQLException::class)
    fun updateEvent(unsEvent: UnsEvent) {
        getConnection().use { conn ->
            conn.prepareStatement(
                "UPDATE $tableNameEvent SET agency_id = ?, status = ? WHERE id = ?"
            ).use { stmt ->
                stmt.setString(1, unsEvent.agencyID)
                stmt.setString(2, unsEvent.status)
                stmt.setInt(3, unsEvent.id)
                stmt.executeUpdate()
            }
        }
    }

    companion object {
        private const val JDBC_URL = "jdbc:postgresql://localhost:8765/postgres"
        private const val JDBC_USER = "postgres"
        private const val JDBC_PASSWORD = "flashmob"
    }
}