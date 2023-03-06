package com.mellivorines.codex.controller

import com.mellivorines.codex.database.DatabaseService
import com.mellivorines.codex.model.TableField
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import javax.sql.DataSource

@RestController
@RequestMapping("/api")
class DatabaseController(private var databaseService: DatabaseService) {

    @Autowired
    lateinit var dataSource: DataSource

    @GetMapping("/tableField")
    fun getTableField(@RequestParam("table")table:String): List<TableField>? {
        return databaseService.getTableFields(table, dataSource)
    }

    @GetMapping("/allTable")
    fun getAllTable(): List<String>? {
        return databaseService.getAllTables(dataSource)
    }
}
