package com.example.rxsamplesearch.database


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "countries", indices = [Index(value = ["name"], unique = true)])
data class Country(@PrimaryKey(autoGenerate = true) var id: Long = 0,
                   @ColumnInfo(name = "name") var name: String)