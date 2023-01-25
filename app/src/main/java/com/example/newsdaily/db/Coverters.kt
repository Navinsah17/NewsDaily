package com.example.newsdaily.db

import androidx.room.TypeConverter
import com.example.newsdaily.models.Source

class Coverters {

    @TypeConverter
    fun fromsource(source: Source): String{

        return source.name
    }

    @TypeConverter
    fun toSource(name: String): Source {
        return Source(name,name)
    }
}