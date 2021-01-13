package dev.arpan.annotation.processor

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.SOURCE)
annotation class Key(
    val value: String,
    val ignoreCase: Boolean = false
)
