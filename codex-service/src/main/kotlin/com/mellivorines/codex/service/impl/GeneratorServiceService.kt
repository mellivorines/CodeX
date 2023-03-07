package com.mellivorines.codex.service.impl

import com.mellivorines.codex.model.TableField
import com.mellivorines.codex.service.GeneratorService
import com.mellivorines.codex.utils.TemplateUtils.getTemplateFromJson
import org.springframework.stereotype.Service

@Service
class GeneratorServiceService(private var databaseService: DatabaseService) :
    GeneratorService {

    override fun generateModule(): List<TableField> {
        var allTableField = ArrayList<TableField>()
        var allTables = databaseService.getAllTables()
        if (allTables != null) {
            for (table in allTables) {
                var tableFields = databaseService.getTableFields(table.tableName)
                if (tableFields != null) {
                    allTableField.addAll(tableFields)
                }
            }
        }
        var templateFromJson = getTemplateFromJson("/template/template.json")
        println(templateFromJson)
        return allTableField

    }
}