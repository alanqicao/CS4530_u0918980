package com.example.assignment_2_course.room

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a course with its department, course number, and location.
 */
@Entity(tableName = "courses")
data class CourseEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val department: String,
    val courseNumber: String,
    val location: String
)
