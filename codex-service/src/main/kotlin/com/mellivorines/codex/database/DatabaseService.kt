package com.mellivorines.codex.database

import com.mellivorines.codex.model.TableField
import java.sql.Connection
import java.sql.ResultSet
import java.sql.Statement
import javax.sql.DataSource

interface DatabaseService {
    fun getConnection(dataSource: DataSource): Connection?
    fun getTableFields(s: String, dataSource: DataSource): List<TableField>?
    fun getAllTables(dataSource: DataSource): List<String>?
    fun close(conn: Connection?, ps: Statement? = null, rs: ResultSet? = null)
}
