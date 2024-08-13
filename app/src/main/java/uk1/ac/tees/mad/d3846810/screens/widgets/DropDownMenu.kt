package uk1.ac.tees.mad.d3846810.screens.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import uk1.ac.tees.mad.d3846810.theme.c10

@Composable
fun DropDownMenu(
    menuList: List<String>,
    modifier: Modifier =  Modifier,
    label: String,
    value: String,
    fontSize: TextUnit = TextUnit.Unspecified,
    isError: MutableState<Boolean> = mutableStateOf(false),
    onMenuItem: (selectedText: String) -> Unit
) {
    // Declaring a boolean value to store
    // the expanded state of the Text Field
    var mExpanded by remember { mutableStateOf(false) }

    // Create a string value to store the selected city
    var mSelectedText by remember { mutableStateOf(value) }

    var mTextFieldSize by remember { mutableStateOf(Size.Zero) }

    // Up Icon when expanded and down icon when collapsed
    val icon = if (mExpanded) Icons.Filled.KeyboardArrowUp
    else Icons.Filled.KeyboardArrowDown

    Column(
        modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {

        // Create an Outlined Text Field
        // with icon and not expanded
        OutlinedTextField(value = mSelectedText, isError = isError.value, onValueChange = {
            mSelectedText = it
        }, modifier = Modifier
            .fillMaxWidth()
            .onGloballyPositioned { coordinates ->
                // This value is used to assign to
                // the DropDown the same width
                mTextFieldSize = coordinates.size.toSize()
            }, label = { Text(label) }, colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.White,
            focusedIndicatorColor = c10,
            unfocusedIndicatorColor = Color.Gray,
            cursorColor = c10,
            focusedLabelColor = c10
        ), trailingIcon = {
            Icon(icon, "contentDescription", Modifier.clickable { mExpanded = !mExpanded })
        })

        // Create a drop-down menu with list of cities,
        // when clicked, set the Text Field text as the city selected
        DropdownMenu(expanded = mExpanded,
            onDismissRequest = { mExpanded = false },
            modifier = Modifier.width(with(LocalDensity.current) { mTextFieldSize.width.toDp() })
        ) {
            menuList.forEach { label ->
                DropdownMenuItem(onClick = {
                    mSelectedText = label
                    onMenuItem(mSelectedText)
                    mExpanded = false
                }) {
                    Text(text = label, fontSize = fontSize)
                }
            }
        }
    }
}