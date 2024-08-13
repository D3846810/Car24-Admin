package uk1.ac.tees.mad.d3846810.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import uk1.ac.tees.mad.d3846810.R
import uk1.ac.tees.mad.d3846810.screens.listItems.INTER
import uk1.ac.tees.mad.d3846810.screens.widgets.CustomButton
import uk1.ac.tees.mad.d3846810.screens.widgets.InputField

@Composable
fun ManageLoginScreen(
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit,
    onSave: (String, String, String) -> Unit,
    onLogout: () -> Unit
) {
    val email = remember { mutableStateOf("") }
    val emailError = remember { mutableStateOf(false) }
    val pass = remember { mutableStateOf("") }
    val passError = remember { mutableStateOf(false) }
    val confirmPass = remember { mutableStateOf("") }
    val confirmPassError = remember { mutableStateOf(false) }

    ConstraintLayout(
        modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        val (backBtn, inputLayout, logoutBtn) = createRefs()
        Box(
            modifier = modifier
                .clickable {
                    onBackPressed()
                }
                .constrainAs(backBtn) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
                .size(48.dp)
                .background(Color.Transparent, RoundedCornerShape(8.dp))
                .border(1.dp, Color.Gray, RoundedCornerShape(8.dp)),
        ) {
            Image(
                modifier = modifier
                    .align(Alignment.Center)
                    .size(30.dp),
                painter = painterResource(id = R.drawable.ic_back_arrow),
                contentDescription = "",
                colorFilter = ColorFilter.tint(Color.Gray)

            )
        }
        Column(
            modifier
                .fillMaxWidth()
                .constrainAs(inputLayout) {
                    top.linkTo(backBtn.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(logoutBtn.top)
                },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                modifier = modifier.size(120.dp),
                painter = painterResource(id = R.drawable.icon),
                contentDescription = ""
            )
            Text(
                modifier = modifier.padding(top = 8.dp),
                text = "Manage Admin Login",
                fontSize = 20.sp,
                fontFamily = INTER,
                fontWeight = FontWeight.Bold
            )
            Text(
                modifier = modifier.alpha(0.8.toFloat()),
                text = "Please Enter Email and Password to\n" + "Register as Admin Login",
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                fontFamily = INTER,
                fontWeight = FontWeight.SemiBold
            )
            InputField(value = email.value,
                label = "Enter Email",
                isError = emailError,
                onValueChange = {
                    emailError.value = false
                    email.value = it
                })
            InputField(value = pass.value,
                label = "Enter Password",
                isError = passError,
                onValueChange = {
                    passError.value = false
                    pass.value = it
                })
            InputField(value = confirmPass.value,
                label = "Confirm Password",
                isError = confirmPassError,
                errorMessage = "Password Not Match",
                onValueChange = {
                    confirmPassError.value = false
                    confirmPass.value = it
                })
            Spacer(modifier = modifier.height(16.dp))
            CustomButton(label = "Save") {
                if (email.value.isEmpty()) {
                    emailError.value = true
                } else if (pass.value.isEmpty()) {
                    passError.value = true
                } else if (confirmPass.value.isEmpty()) {
                    confirmPassError.value = true
                }else {
                    onSave(email.value, pass.value, confirmPass.value)
                }
            }
        }
        CustomButton(modifier = modifier.constrainAs(logoutBtn) {
            bottom.linkTo(parent.bottom)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }, label = "Logout") {
            onLogout()
        }
    }
}

@Preview
@Composable
private fun PreviewManageLoginScreen() {
    ManageLoginScreen(modifier = Modifier, {}, { email, pass, confirmPass ->
    }, {})
}