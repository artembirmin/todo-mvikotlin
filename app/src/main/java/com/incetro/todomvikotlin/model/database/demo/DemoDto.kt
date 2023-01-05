/*
 * todomvikotlin
 *
 * Created by artembirmin on 5/1/2023.
 */

package com.incetro.todomvikotlin.model.database.demo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = DemoDto.TABLE_NAME
)
data class DemoDto(
    @PrimaryKey
    val id: String,
    val name: String,
) {
    companion object {
        const val TABLE_NAME = "demo"
    }
}
