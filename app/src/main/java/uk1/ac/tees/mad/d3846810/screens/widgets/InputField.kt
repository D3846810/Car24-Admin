package uk1.ac.tees.mad.d3846810.screens.widgets

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import uk1.ac.tees.mad.d3846810.theme.c10


@Composable
fun InputField(
    modifier: Modifier = Modifier,
    value: String,
    label: String,
    fontSize: TextUnit = TextUnit.Unspecified,
    isError: MutableState<Boolean> = mutableStateOf(false),
    errorMessage: String = "Empty",
    onValueChange: (value: String) -> Unit
) {
    OutlinedTextField(modifier = modifier
        .fillMaxWidth()
        .wrapContentHeight(),
        value = value,
        isError = isError.value,
        trailingIcon = {
            if (isError.value) {
                Text(modifier = modifier.padding(14.dp), text = errorMessage, color = Color.Red)
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
        label = { Text(text = label, fontSize = fontSize) })
}

@Preview
@Composable
private fun InputFieldPreview() {
    InputField(value = "", label = "sf", onValueChange = {})
}