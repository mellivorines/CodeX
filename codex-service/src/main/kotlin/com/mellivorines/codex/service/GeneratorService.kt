package com.mellivorines.codex.service

import com.mellivorines.codex.model.TableField


interface GeneratorService {
    fun generateModule():List<TableField>
}