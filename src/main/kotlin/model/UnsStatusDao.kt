package model

import App.getConnection
import java.sql.SQLException

class UnsStatusDao {
    private val tableNameStatuses = "statuses"
    private val createTableStatuses = """
        create table $tableNameStatuses
        (
            agency_id text                                   not null
            constraint ${tableNameStatuses}_pk
            primary key,
            modified  timestamp with time zone default now() not null,
            status    text                                   not null
        );

        alter table $tableNameStatuses owner to postgres;
    """.trimIndent()

    @Throws(SQLException::class)
    fun upsertStatus(unsEvent: UnsEvent) {
        getConnection().use { conn ->
            conn.prepareStatement(
                "INSERT INTO $tableNameStatuses (id, modified, agency_id, status) VALUES (gen_random_uuid(), now(), ?, ?)"
            ).use { stmt ->
                stmt.setString(1, unsEvent.agencyID)
                stmt.setString(2, unsEvent.status)
                println(stmt)
                stmt.executeUpdate()
            }
        }
    }

    @get:Throws(SQLException::class)
    val allStatuses: List<UnsStatus>
        get() {
            val statuses: MutableList<UnsStatus> = ArrayList()
            getConnection().use { conn ->
                conn.createStatement().use { stmt ->
                    stmt.executeQuery("SELECT agency_id, modified, status FROM $tableNameStatuses").use { rs ->
                        while (rs.next()) {
                            statuses.add(
                                UnsStatus(
                                    rs.getString("agency_id"),
                                    rs.getString("modified"),
                                    rs.getString("status")
                                )
                            )
                        }
                    }
                }
            }
            return statuses
        }
}
