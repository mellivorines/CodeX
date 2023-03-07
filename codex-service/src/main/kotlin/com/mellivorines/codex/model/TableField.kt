package com.mellivorines.codex.model

data class TableField(
    var tableName:String,
    var fieldName: String,
    var fieldComment: String,
    var fieldType: String,
    var fieldSize: Int
)
