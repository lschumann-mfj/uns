package model

import App.getConnection
import java.sql.SQLException

data class UnsEvent(
    var id: String = "",
    var modified: String = "",
    var agencyID: String = "",
    var status: String = "",
) {
    fun toStatus() = UnsStatus(agencyID, modified, status)
}

object UnsEventDao {
    private const val tableNameEvent = "events"
    private val createTableEvent = """
        create table $tableNameEvent
        (
            id        uuid                     default gen_random_uuid() not null
                constraint ${tableNameEvent}_pk primary key,
            modified  timestamp with time zone default now()             not null,
            agency_id text                                               not null,
            status    text                                               not null
        );
        
        alter table $tableNameEvent owner to postgres;
    """.trimIndent()

    @Throws(SQLException::class)
    fun createEvent(unsEvent: UnsEvent) {
        getConnection().use { conn ->
            conn.prepareStatement(
                "INSERT INTO $tableNameEvent (id, modified, agency_id, status) VALUES (gen_random_uuid(), now(), ?, ?)"
            ).use { stmt ->
                stmt.setString(1, unsEvent.agencyID)
                stmt.setString(2, unsEvent.status)
                println(stmt)
                stmt.executeUpdate()
            }
            UnsStatusDao.upsertStatus(unsEvent.toStatus())
        }
    }

    @Throws(SQLException::class)
    fun getEvent(id: String): UnsEvent? {
        var unsEvent: UnsEvent? = null
        getConnection().use { conn ->
            conn.prepareStatement("SELECT * FROM $tableNameEvent WHERE id::text = ?").use { stmt ->
                stmt.setString(1, id)
                val rs = stmt.executeQuery()
                if (rs.next()) {
                    unsEvent = UnsEvent(
                        rs.getString("id"),
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
                    stmt.executeQuery("SELECT id::text AS id, modified, agency_id, status FROM $tableNameEvent").use { rs ->
                        while (rs.next()) {
                            events.add(
                                UnsEvent(
                                    rs.getString("id"),
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
}
