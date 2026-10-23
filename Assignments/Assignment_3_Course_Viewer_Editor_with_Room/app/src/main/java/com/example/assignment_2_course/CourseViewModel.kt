package com.example.assignment_2_course


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.assignment_2_course.room.CourseEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

/**
 * Stores the course list and handles changes to course data.
 */
class CourseViewModel(private val repository: CourseRepository) : ViewModel() {


    val coursesReadOnly: StateFlow<List<CourseEntity>> = repository.allCourses.stateIn(
        scope = repository.scope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = listOf() )

    /**
     * Adds a new course to the course list.
     *
     * @param newCourse the course to add
     */
    fun addCourse(newCourse: CourseEntity) {
        repository.addCourse(newCourse)
    }

    /**
     * Removes a course from the course list.
     *
     * @param selectedCourse the course to remove
     */
    fun deleteCourse(selectedCourse: CourseEntity) {
        repository.deleteCourse(selectedCourse)
    }

    /**
     * Replaces an existing course with an edited course.
     *
     * @param oldCourse the course before editing
     * @param newCourse the updated course
     */
    fun editCourse(oldCourse: CourseEntity, newCourse: CourseEntity) {
        val updatedCourse = newCourse.copy(id = oldCourse.id)
        repository.updateCourse(updatedCourse)
    }
}

object CourseViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            CourseViewModel(
                //fetches the application singleton
                (this[AndroidViewModelFactory.APPLICATION_KEY]
                        //and then extracts the repository in it
                        as CourseApplication).repository
            )
        }
    }
}

