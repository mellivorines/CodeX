package com.mellivorines.codex.database.impl

import com.mellivorines.codex.database.DatabaseService
import com.mellivorines.codex.model.FieldInfo
import com.zaxxer.hikari.HikariDataSource
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.sql.Connection
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement
import javax.sql.DataSource

@Service
class DatabaseService : DatabaseService {
    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)


    /**
     * 获取数据库全部表
     */
    override fun getAllTables(dataSource: DataSource): List<String>? {
        val databaseName = getDatabaseName(dataSource)
        val schema: String? = getSchema((dataSource as HikariDataSource).driverClassName)
        val connection = getConnection(dataSource) ?: return null
        val result = ArrayList<String>()
        var resultSet: ResultSet? = null
        try {
            connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)
            val meta = connection.metaData
            //目录名称, 数据库名, 表名称, 表类型
            resultSet = meta.getTables(catalog(databaseName), schema, tableNamePattern(), types())
            while (resultSet?.next()!!) {
                result.add(resultSet.getString("TABLE_NAME"))
            }
        } catch (e: SQLException) {
            logger.error("获取数据库全部表:", e)
        } finally {
            close(connection, null, resultSet)
        }
        return result
    }

    private fun getSchema(driverClassName: String?): String? {
        return if (driverClassName.equals("org.postgresql.Driver")) {
            "public"
        } else {
            null
        }
    }

    /**
     * 获取数据库表所包含的字段
     */
    override fun getTableFields(table: String, dataSource: DataSource): List<FieldInfo>? {
        val databaseName = getDatabaseName(dataSource)
        val schema: String? = getSchema((dataSource as HikariDataSource).driverClassName)
        val connection = getConnection(dataSource) ?: return null
        val result = ArrayList<FieldInfo>()
        var resultSet: ResultSet? = null
        try {
            val meta = connection.metaData
            resultSet = meta.getColumns(catalog(databaseName), schema, table, null)
            while (resultSet.next()) {
                val fieldInfo = FieldInfo(
                    resultSet.getString("COLUMN_NAME"),
                    resultSet.getString("REMARKS"),
                    resultSet.getString("TYPE_NAME"),
                    resultSet.getInt("COLUMN_SIZE")
                )
                result.add(fieldInfo)
            }
        } catch (e: Exception) {
            logger.error("获取数据库表所包含的字段：", e)
        } finally {
            close(connection, null, resultSet)
        }
        return result
    }

    /**
     * a catalog name;
     * must match the catalog name as it is stored in the database;
     * "" retrieves those without a catalog; null means that the catalog name should not be used to narrow the search
     */
    fun catalog(databaseName: String): String? {
        return databaseName
    }

    /**
     * a table name pattern;
     * must match the table name as it is stored in the database
     */
    fun tableNamePattern(): String {
        return "%"
    }

    /**
     * a list of table types,
     * which must be from the list of table types returned from DatabaseMetaData,
     * to include; null returns all types
     */
    fun types(): Array<String> {
        return arrayOf("TABLE", "VIEW")
    }

    /**
     * 获取数据库名称
     */
    fun getDatabaseName(dataSource: DataSource): String = (dataSource as HikariDataSource).jdbcUrl.substringBefore("?").substringAfterLast("/")

    override fun getConnection(dataSource: DataSource): Connection? {
        var connection: Connection? = null
        try {
            connection = dataSource.connection
        } catch (e: SQLException) {
            logger.error("数据库连接失败！", e)
        }
        return connection
    }

    override fun close(conn: Connection?, ps: Statement?, rs: ResultSet?) {
        var connection = conn
        var statement = ps
        var resultSet = rs
        //关闭ResultSet
        if (resultSet != null) {
            try {
                resultSet.close()
            } catch (e: SQLException) {
                resultSet = null
                logger.error(e.message)
            }
        }
        //关闭PreparedStatement
        if (statement != null) {
            try {
                statement.close()
            } catch (e: SQLException) {
                statement = null
                logger.error(e.message)
            }
        }
        //关闭Connection
        if (connection != null) {
            try {
                connection.close()
            } catch (e: SQLException) {
                connection = null
                logger.error(e.message)
            }
        }

    }

    /**
     * 生成 DDL 语句
     */
    fun generateDDL(table: String, dataSource: DataSource): String? {
        val fields = getTableFields(table, dataSource)
        return ddl(table, fields)
    }

    fun ddl(table: String, fields: List<FieldInfo>?): String {
        var fieldLines = StringBuilder()
        fields?.forEachIndexed { index, fieldInfo ->
            if (index == 0) {
                val line = "${fieldInfo.fieldName}               STRING COMMENT '${fieldInfo.comment}'"
                fieldLines.append("\n")
                fieldLines.append(line)
                fieldLines.append("\n")
            } else {
                val line = ",${fieldInfo.fieldName}               STRING COMMENT '${fieldInfo.comment}'"
                fieldLines.append(line)
                fieldLines.append("\n")
            }
        }
        return """
                CREATE TABLE IF NOT EXISTS $table(
                $fieldLines 
                )
                COMMENT '' PARTITIONED BY
                (
                  pt STRING COMMENT '时间分区键-yyyymmdd'
                )
                LIFECYCLE 750;
                """.trimIndent()
    }

}
