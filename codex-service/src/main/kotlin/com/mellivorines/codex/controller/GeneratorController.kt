package com.mellivorines.codex.controller

import com.mellivorines.codex.model.Table
import com.mellivorines.codex.model.TableField
import com.mellivorines.codex.service.GeneratorService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import javax.sql.DataSource


@RestController
@RequestMapping("/api")
class GeneratorController(private var generatorService: GeneratorService) {

    @Autowired
    lateinit var dataSource: DataSource

    @GetMapping("/generator")
    fun getTableField(): List<TableField>? {
        return generatorService.generateModule()
    }

}
