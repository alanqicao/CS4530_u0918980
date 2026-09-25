package com.example.assignment_2_course

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.collections.plus

/**
 * Stores the course list and handles changes to course data.
 */
class CourseViewModel : ViewModel() {

    private val courses = MutableStateFlow(listOf<Course>())
    val coursesReadOnly: StateFlow<List<Course>> = courses

    /**
     * Adds a new course to the course list.
     *
     * @param newCourse the course to add
     */
    fun addCourse(newCourse: Course) {
        val currentCourses = courses.value
        courses.value = currentCourses + newCourse
    }

    /**
     * Removes a course from the course list.
     *
     * @param selectedCourse the course to remove
     */
    fun deleteCourse(selectedCourse: Course) {
        val currentCourses = courses.value
        courses.value = currentCourses - selectedCourse
    }

    /**
     * Replaces an existing course with an edited course.
     *
     * @param oldCourse the course before editing
     * @param newCourse the updated course
     */
    fun editCourse(oldCourse: Course, newCourse: Course) {
        val currentCourses = courses.value
        courses.value = currentCourses.map {
            if (it == oldCourse) newCourse else it
        }
    }
}
