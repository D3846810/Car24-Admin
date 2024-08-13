package uk1.ac.tees.mad.d3846810.screens.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uk1.ac.tees.mad.d3846810.theme.c10
import uk1.ac.tees.mad.d3846810.utils.onClick

@Composable
fun HomeButton(modifier: Modifier = Modifier, text: String = "Button", onClick: () -> Unit = {}) {
    Box(modifier = modifier
        .onClick {
            onClick()
        }
        .width(160.dp)
        .height(160.dp)
        .clip(shape = RoundedCornerShape(8.dp))
        .background(color = c10, RoundedCornerShape(8.dp)), contentAlignment = Alignment.Center) {
        CustomText(
            modifier = modifier,
            text = text,
            color = Color.White,
            textAlign = TextAlign.Center,
            fontSize = 18.sp
        )
    }
}

@Preview
@Composable
private fun PreviewHomeButton() {
    HomeButton()
}