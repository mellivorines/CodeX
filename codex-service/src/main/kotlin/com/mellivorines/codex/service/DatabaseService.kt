package com.mellivorines.codex.service

import com.mellivorines.codex.model.Table
import com.mellivorines.codex.model.TableField
import java.sql.Connection
import java.sql.ResultSet
import java.sql.Statement
import javax.sql.DataSource

interface DatabaseService {
    fun getConnection(dataSource: DataSource): Connection?
    fun getTableFields(tableName: String): List<TableField>?
    fun getAllTables(): List<Table>?
    fun close(connection: Connection?, ps: Statement? = null, rs: ResultSet? = null)
}
