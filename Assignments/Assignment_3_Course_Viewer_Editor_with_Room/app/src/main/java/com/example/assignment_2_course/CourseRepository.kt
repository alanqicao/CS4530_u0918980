package com.example.assignment_2_course

import com.example.assignment_2_course.room.CourseDaoInterface
import com.example.assignment_2_course.room.CourseEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class CourseRepository(val scope: CoroutineScope, private val courseDao: CourseDaoInterface) {

    val allCourses = courseDao.getAllCourses()

    fun addCourse(course: CourseEntity) {
        scope.launch {
            courseDao.insertCourse(course)
        }
    }

    fun deleteCourse(course: CourseEntity) {
        scope.launch {
            courseDao.deleteCourse(course)
        }
    }

    fun updateCourse(course: CourseEntity) {
        scope.launch {
            courseDao.updateCourse(course)
        }
    }

}