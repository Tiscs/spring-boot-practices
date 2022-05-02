package io.github.tiscs.sbp.openapi

import kotlin.reflect.KClass

@Target()
@Retention(AnnotationRetention.RUNTIME)
annotation class ApiFilter(val name: String, val example: String = "", val description: String = "", val params: Array<KClass<*>> = [])
