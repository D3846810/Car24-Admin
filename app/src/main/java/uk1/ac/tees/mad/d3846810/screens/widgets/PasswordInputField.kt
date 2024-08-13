package uk1.ac.tees.mad.d3846810.screens.widgets

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uk1.ac.tees.mad.d3846810.theme.c10


@Composable
fun PasswordInputField(
    value: String,
    label: String,
    keyboardType: KeyboardType = KeyboardType.Password,
    isError: MutableState<Boolean> = mutableStateOf(false),
    errorMessage: String = "Empty",
    onValueChange: (value: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    OutlinedTextField(modifier = modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .padding(vertical = 8.dp),
        value = value,
        isError = isError.value,
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = {
            val image = if (passwordVisible) Icons.Filled.Visibility
            else Icons.Filled.VisibilityOff

            // Please provide localized description for accessibility services
            val description = if (passwordVisible) "Hide password" else "Show password"

            IconButton(onClick =  { passwordVisible = !passwordVisible }) {
                Icon(imageVector = image, description)
            }
        },
        onValueChange = {
            onValueChange(it)
        },
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.White,
            focusedIndicatorColor = c10,
            unfocusedIndicatorColor = Color.Gray,
            cursorColor = c10,
            focusedLabelColor = c10
        ),
        label = { Text(label) })
}


@Preview
@Composable
private fun PasswordInputFieldPreview() {
    PasswordInputField(value = "", label = "sf", onValueChange = {})
}