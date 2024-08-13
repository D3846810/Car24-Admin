package uk1.ac.tees.mad.d3846810.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
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
import uk1.ac.tees.mad.d3846810.screens.widgets.PasswordInputField

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    buttonLoginTxt: String,
    onLogin: (String, String) -> Unit,
) {
    val email = remember { mutableStateOf("") }
    val emailError = remember { mutableStateOf(false) }
    val pass = remember { mutableStateOf("") }
    val passError = remember { mutableStateOf(false) }

    ConstraintLayout(
        modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        val (inputLayout) = createRefs()
        Column(
            modifier
                .fillMaxWidth()
                .constrainAs(inputLayout) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
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
                text = "Please Enter Admin email & password to \nlogin access to Admin app",
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                fontFamily = INTER,
                fontWeight = FontWeight.SemiBold
            )
            InputField(
                value = email.value,
                label = "Enter Email",
                isError = emailError,
                onValueChange = {
                    emailError.value = false
                    email.value = it
                })
            PasswordInputField(
                value = pass.value,
                label = "Enter Password",
                isError = passError,
                onValueChange = {
                    passError.value = false
                    pass.value = it
                })

            CustomButton(label = buttonLoginTxt) {
                if (email.value.isEmpty()) {
                    emailError.value = true
                } else if (pass.value.isEmpty()) {
                    passError.value = true
                }else {
                    onLogin(email.value, pass.value)
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewManageLoginScreen() {
    LoginScreen(modifier = Modifier,"Login", { email, pass ->
    },)
}