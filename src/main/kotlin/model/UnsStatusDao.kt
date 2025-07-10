package model

import App.getConnection
import java.sql.SQLException

data class UnsStatus(
    var agencyID: String = "",
    var modified: String = "",
    var status: String = "",
)

object UnsStatusDao {
    private const val tableNameStatuses = "statuses"
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
    fun upsertStatus(unsStatus: UnsStatus) {
        getConnection().use { conn ->
            conn.prepareStatement(
                """
                    INSERT INTO $tableNameStatuses (agency_id, modified, status) VALUES (?, now(), ?)
                    ON CONFLICT (agency_id) DO UPDATE SET modified = now(), status = ?
                """.trimIndent()
            ).use { stmt ->
                stmt.setString(1, unsStatus.agencyID)
                stmt.setString(2, unsStatus.status)
                stmt.setString(3, unsStatus.status)
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
