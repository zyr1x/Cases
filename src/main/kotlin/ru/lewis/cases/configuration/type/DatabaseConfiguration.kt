package ru.lewis.cases.configuration.type

import org.spongepowered.configurate.objectmapping.ConfigSerializable

@ConfigSerializable
data class DatabaseConfiguration(
    val address: String = "database",
    val port: Int = 3306,
    val database: String = "testbase",
    val user: String = "root",
    val password: String = "iUi1DY-Sy87rw-8zlVlr",
    val parameters: List<String> = listOf("useServerPrepStmts=true")
)