package com.mellivorines.codex.database

import com.mellivorines.codex.model.FieldInfo
import java.sql.Connection
import java.sql.ResultSet
import java.sql.Statement
import javax.sql.DataSource

interface DatabaseService {
    fun getConnection(dataSource: DataSource): Connection?
    fun getTableFields(s: String, dataSource: DataSource): List<FieldInfo>?
    fun getAllTables(dataSource: DataSource): List<String>?
    fun close(conn: Connection?, ps: Statement? = null, rs: ResultSet? = null)
}
