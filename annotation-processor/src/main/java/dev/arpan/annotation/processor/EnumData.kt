package dev.arpan.annotation.processor

import javax.lang.model.element.VariableElement

data class EnumData(
    val field: VariableElement,
    val key: Key
)
