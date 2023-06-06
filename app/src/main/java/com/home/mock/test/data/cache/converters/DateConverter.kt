package com.home.mock.test.data.cache.converters

import androidx.room.TypeConverter
import java.util.*

/**
 * Class that provides type conversion methods for Date objects
 */
class DateConverter {
    // Annotation indicating that this is a type converter method
    // It will be used by Room database to convert between types during database operations
    @TypeConverter
    fun fromTimeStamp(value: Long?): Date {
        return Date(value ?: 0)
        // Method for converting a Long timestamp to a Date object
        // Returns a Date object created from the provided timestamp
        // If the value is null, it uses 0 as the default value
    }

    // Annotation indicating that this is a type converter method
    @TypeConverter
    fun dateToTimeStamp(date: Date?): Long = date?.time ?: 0
    // Method for converting a Date object to a Long timestamp
    // Returns the time value of the Date object as a Long timestamp
    // If the Date object is null, it uses 0 as the default value
}
