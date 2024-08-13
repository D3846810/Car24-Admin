package uk1.ac.tees.mad.d3846810.screens.widgets

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import uk1.ac.tees.mad.d3846810.theme.c10

@Composable
fun CustomButton(modifier: Modifier = Modifier,label: String, onClick: () -> Unit) {
    Button(modifier = modifier
        .fillMaxWidth()
        .height(48.dp),
        shape = RoundedCornerShape(4.dp),
        colors = ButtonDefaults.buttonColors(
            contentColor = Color.White, backgroundColor = c10
        ),
        onClick = {
            onClick()
        }) {
        Text(label, color = Color.White)
    }
}