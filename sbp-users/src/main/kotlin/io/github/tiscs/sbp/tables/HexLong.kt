package io.github.tiscs.sbp.tables

import io.github.tiscs.sbp.snowflake.LOWER_HEX_FORMAT
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ColumnType
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.vendors.currentDialect

class HexLongColumnType : ColumnType() {
    override fun sqlType(): String = currentDialect.dataTypeProvider.longType()

    override fun valueFromDB(value: Any): String = when (value) {
        is Long -> LOWER_HEX_FORMAT.format(value)
        is Number -> LOWER_HEX_FORMAT.format(value.toLong())
        is String -> LOWER_HEX_FORMAT.format(value.toLong(16))
        else -> error("Unexpected value of type Long: $value of ${value::class.qualifiedName}")
    }

    override fun notNullValueToDB(value: Any): Any = if (value is String) value.toLong(16) else value
}

fun Table.hexLong(name: String): Column<String> = registerColumn(name, HexLongColumnType())
