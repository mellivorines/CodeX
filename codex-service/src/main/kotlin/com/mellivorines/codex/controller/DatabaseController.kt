package com.mellivorines.codex.controller

import com.mellivorines.codex.model.database.Table
import com.mellivorines.codex.model.database.TableField
import com.mellivorines.codex.service.DatabaseService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import javax.sql.DataSource

@RestController
@RequestMapping("/api")
class DatabaseController(private var databaseService: DatabaseService) {
    
    @GetMapping("/tableField")
    fun getTableField(@RequestParam("table")table:String): List<TableField>? {
        return databaseService.getTableFields(table)
    }

    @GetMapping("/allTable")
    fun getAllTable(): List<Table>? {
        return databaseService.getAllTables()
    }
}
