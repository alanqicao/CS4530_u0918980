package com.example.assignment_2_course

import android.app.Application
import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import com.example.assignment_2_course.room.CourseDatabase

class CourseApplication: Application() {
    val scope = CoroutineScope(SupervisorJob())

    val database by lazy {
        Room.databaseBuilder(applicationContext,
            CourseDatabase::class.java,
            "course_database").build()
    }
    val repository by lazy { CourseRepository(scope, database.courseDao()) }

}