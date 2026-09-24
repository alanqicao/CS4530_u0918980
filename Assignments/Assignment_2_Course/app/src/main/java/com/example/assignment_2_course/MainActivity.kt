package com.example.assignment_2_course

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.assignment_2_course.ui.theme.Assignment_2_CourseTheme

import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


data class Course(
    val department: String,
    val courseNumber: String,
    val location: String
)

class CourseViewModel : ViewModel() {
    private val courses = MutableStateFlow(listOf<Course>())
    val coursesReadOnly: StateFlow<List<Course>> = courses

    fun addCourse(newCourse: Course) {
        val currentCourses = courses.value
        courses.value = currentCourses + newCourse
    }

    fun deleteCourse(selectedCourse: Course) {
        val currentCourses = courses.value
        courses.value = currentCourses - selectedCourse
    }

    fun editCourse(oldCourse: Course, newCourse: Course) {
        val currentCourses = courses.value
        courses.value = currentCourses.map {
            if (it == oldCourse) newCourse else it
        }
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Assignment_2_CourseTheme {
                val myVMObj: CourseViewModel = viewModel()
                Column(
                    Modifier
                        .fillMaxWidth()
                        .statusBarsPadding(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Course(myVMObj)
                    CoursesItemRow(myVMObj)
                }
            }
        }
    }
}

@Composable
fun Course(myVMObj: CourseViewModel) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        var departmentTest by remember {
            mutableStateOf("")
        }

        var courseNumberTest by remember {
            mutableStateOf("")
        }

        var locationTest by remember {
            mutableStateOf("")
        }

        Row(
            Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(top = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            OutlinedTextField(
                value = departmentTest,
                onValueChange = { departmentTest = it },
                label = { Text("Department") },
                modifier = Modifier.weight(1f)
            )

            OutlinedTextField(
                value = courseNumberTest,
                onValueChange = { courseNumberTest = it },
                label = { Text("Course Number") },
                modifier = Modifier.weight(1f)
            )

            OutlinedTextField(
                value = locationTest,
                onValueChange = { locationTest = it },
                label = { Text("Location") },
                modifier = Modifier.weight(1f)
            )
        }

        Row {
            Button(onClick = {

                val newCourse = Course(
                    department = departmentTest,
                    courseNumber = courseNumberTest,
                    location = locationTest
                )
                myVMObj.addCourse(newCourse)
                departmentTest = ""
                courseNumberTest = ""
                locationTest = ""
            }) {
                Text("Add Course")
            }
        }
        Spacer(Modifier.height(20.dp))
    }
}

@Composable
fun CoursesItemRow(myVMObj: CourseViewModel) {
    val observableCourses by myVMObj.coursesReadOnly.collectAsStateWithLifecycle()
    var selectedCourse by remember { mutableStateOf<Course?>(null) }
    var isEditing by remember { mutableStateOf(false) }

    var departmentTest by remember {
        mutableStateOf("")
    }

    var courseNumberTest by remember {
        mutableStateOf("")
    }

    var locationTest by remember {
        mutableStateOf("")
    }
    Column(

    ) {
        Text("Courses", fontSize = 25.sp, fontWeight = FontWeight.ExtraBold, color = Color.Blue)
        LazyColumn {
            items(observableCourses) { course ->
                Text(
                    course.department + " " + course.courseNumber,
                    fontSize = 20.sp,
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Bold,
                    color = if (selectedCourse == course) Color.White else Color.Black,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            if (selectedCourse == course) Color.DarkGray else Color.Transparent
                        )
                        .clickable {
                            selectedCourse = course
                            isEditing = false
                        }
                        .padding(10.dp)
                )

            }
        }

        selectedCourse?.let { course ->
            Text(
                "Selected Course: ${course.department} ${course.courseNumber} ${course.location}",
                fontSize = 20.sp,
                color = Color.Magenta
            )


            if (isEditing) {

                Column() {
                    Row() {
                        OutlinedTextField(
                            value = departmentTest,
                            onValueChange = { departmentTest = it },
                            label = { Text("Department") },
                            modifier = Modifier.weight(1f)
                        )

                        OutlinedTextField(
                            value = courseNumberTest,
                            onValueChange = { courseNumberTest = it },
                            label = { Text("Course Number") },
                            modifier = Modifier.weight(1f)
                        )

                        OutlinedTextField(
                            value = locationTest,
                            onValueChange = { locationTest = it },
                            label = { Text("Location") },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Row() {
                        Button(
                            onClick = {
                                val newCourse = Course(
                                    department = departmentTest,
                                    courseNumber = courseNumberTest,
                                    location = locationTest
                                )
                                selectedCourse = newCourse
                                myVMObj.editCourse(course, newCourse)
                                isEditing = false
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Blue
                            ),
                        ) { Text("Save") }
                        Button(
                            onClick = {
                                isEditing = false
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Red
                            ),
                        ) { Text("Cancel") }
                    }

                }
            } else {
                Button(
                    onClick = {
                        isEditing = true
                        departmentTest = course.department
                        courseNumberTest = course.courseNumber
                        locationTest = course.location
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Gray
                    ),
                ) { Text("Edit Course") }

                Button(
                    onClick = {
                        myVMObj.deleteCourse(course)
                        selectedCourse = null
                    },
                    colors = ButtonDefaults.buttonColors(

                        containerColor = Color.Red
                    ),
                    modifier = Modifier.height(36.dp),
                    contentPadding = PaddingValues(
                        horizontal = 12.dp,
                        vertical = 4.dp
                    )

                )
                {
                    Text("Delete Course")
                }
            }


        }
    }
}

