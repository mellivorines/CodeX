package com.mellivorines.codex.model.template


data class Templates(
    var java: List<TemplateInfo>,
    var kotlin: List<TemplateInfo>
)

data class TemplateInfo(
    var className: String,
    var outPath: String,
    var templateName: String
)

