package com.example.assignment_2_course.room

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [CourseEntity::class], version = 1, exportSchema = false)
abstract class CourseDatabase: RoomDatabase() {
    abstract fun courseDao(): CourseDaoInterface
}