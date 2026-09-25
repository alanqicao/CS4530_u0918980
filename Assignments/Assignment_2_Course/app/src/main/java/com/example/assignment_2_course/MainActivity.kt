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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.assignment_2_course.ui.theme.Assignment_2_CourseTheme


/**
 * The main and only activity for the course application.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Assignment_2_CourseTheme {
                val myVMObj: CourseViewModel = viewModel()
                val observableCourses by myVMObj.coursesReadOnly.collectAsStateWithLifecycle()
                Column(
                    Modifier
                        .fillMaxWidth()
                        .statusBarsPadding(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CourseInputForm(myVMObj::addCourse)
                    CoursesList( myVMObj::deleteCourse,observableCourses,myVMObj::editCourse)
                }
            }
        }
    }
}

/**
 * Displays the input fields used to create and add a new course.
 *
 * @param myVMObj the ViewModel used to add courses
 */
@Composable
fun CourseInputForm(addCourse:(Course) -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        var departmentInput by remember {
            mutableStateOf("")
        }

        var courseNumberInput by remember {
            mutableStateOf("")
        }

        var locationInput by remember {
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
                value = departmentInput,
                onValueChange = { departmentInput = it },
                label = { Text("Department") },
                modifier = Modifier.weight(1f)
            )

            OutlinedTextField(
                value = courseNumberInput,
                onValueChange = { courseNumberInput = it },
                label = { Text("Course Number") },
                modifier = Modifier.weight(1f)
            )

            OutlinedTextField(
                value = locationInput,
                onValueChange = { locationInput = it },
                label = { Text("Location") },
                modifier = Modifier.weight(1f)
            )
        }

        Row {
            Button(onClick = {

                val newCourse = Course(
                    department = departmentInput,
                    courseNumber = courseNumberInput,
                    location = locationInput
                )
                addCourse(newCourse)
                departmentInput = ""
                courseNumberInput = ""
                locationInput = ""
            }) {
                Text("Add Course")
            }
        }
        Spacer(Modifier.height(20.dp))
    }
}

/**
 * Displays the course list and allows a course to be selected,
 * edited, or deleted.
 *
 * @param CourseViewModel the ViewModel containing the course data
 */
@Composable
fun CoursesList(deleteCourse:(Course) -> Unit,observableCourses: List<Course>,editCourse:(Course,Course) -> Unit) {

    var selectedCourse by remember { mutableStateOf<Course?>(null) }
    var isEditing by remember { mutableStateOf(false) }

    var editDepartment by remember {
        mutableStateOf("")
    }

    var editNumber by remember {
        mutableStateOf("")
    }

    var editLocation by remember {
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
                            value = editDepartment,
                            onValueChange = { editDepartment = it },
                            label = { Text("Department") },
                            modifier = Modifier.weight(1f)
                        )

                        OutlinedTextField(
                            value = editNumber,
                            onValueChange = { editNumber = it },
                            label = { Text("Course Number") },
                            modifier = Modifier.weight(1f)
                        )

                        OutlinedTextField(
                            value = editLocation,
                            onValueChange = { editLocation = it },
                            label = { Text("Location") },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Row() {
                        Button(
                            onClick = {
                                val newCourse = Course(
                                    department = editDepartment,
                                    courseNumber = editNumber,
                                    location = editLocation
                                )
                                selectedCourse = newCourse
                                editCourse(course, newCourse)
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
                        editDepartment = course.department
                        editNumber = course.courseNumber
                        editLocation = course.location
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Gray
                    ),
                ) { Text("Edit Course") }

                Button(
                    onClick = {
                        deleteCourse(course)
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

