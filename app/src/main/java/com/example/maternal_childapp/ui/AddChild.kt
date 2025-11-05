package com.example.maternal_childapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.maternal_childapp.R
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign


@Composable
fun AddChild() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.background1),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                color = Color.White,
                modifier = Modifier.fillMaxWidth().height(60.dp),
            ) {

                Text(
                    text = "Add Child",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(15.dp)
                )

            }
                    Spacer(modifier = Modifier.height(30.dp))
                    ChildForm()
                }
            }
        }


@Composable
fun ChildForm(){
    var babyFirstName by remember { mutableStateOf("") }
    var babyLastName by remember { mutableStateOf("") }
    var babyDateOfBirth by remember { mutableStateOf("") }
    var birthWeight by remember { mutableStateOf("") }
    var birthLength by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    Column( modifier = Modifier
        .padding(16.dp)
        .fillMaxWidth()){
        TextField(
            value = babyFirstName,
            onValueChange = { babyFirstName = it },
            label = { Text("First Name")},
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))

        TextField(
            value = babyLastName,
            onValueChange = { babyLastName = it },
            label = { Text("Last Name") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        TextField(
            value = babyDateOfBirth,
            onValueChange = { babyDateOfBirth = it },
            label = { Text("Date of Birth") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))

        TextField(
            value = birthWeight,
            onValueChange = { birthWeight = it },
            label = { Text("Birth Weight (lbs)") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        TextField(
            value = birthLength,
            onValueChange = {birthLength = it },
            label = { Text("Birth Length (inches)") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        TextField(
            value = gender,
            onValueChange = { gender  = it },
            label = { Text("Gender") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))
        SaveChildButton("Save Changes", Modifier.fillMaxWidth(), { println{ "Profile Saved!"} })

    }
}

@Composable
private fun SaveChildButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
)
{
    Button(
        onClick = onClick,
        modifier = modifier.height(52.dp),
        shape = RoundedCornerShape(28.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(R.color.white),
            contentColor = colorResource(R.color.black)
        )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )
        )
    }
}
@Preview(showBackground = true)
@Composable
fun AddChildPreiew() {
    AddChild()
}