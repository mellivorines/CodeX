package com.mellivorines.codex.service

import com.mellivorines.codex.model.database.Table


interface GeneratorService {
    fun generateModule(language: String, module: String?): List<Table>?
}
