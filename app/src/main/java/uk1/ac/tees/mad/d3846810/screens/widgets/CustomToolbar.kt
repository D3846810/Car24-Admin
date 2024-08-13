package uk1.ac.tees.mad.d3846810.screens.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import uk1.ac.tees.mad.d3846810.R
import uk1.ac.tees.mad.d3846810.theme.c10

@Composable
fun CustomToolbar(modifier: Modifier = Modifier, title: String, onBackPressed: () -> Unit) {
    ConstraintLayout(modifier = modifier
        .fillMaxWidth()
        .height(56.dp)
        .background(c10)
    ) {
        val (backButton, titleText) = createRefs()

        Image(
            modifier = Modifier
                .clickable { onBackPressed() }
                .constrainAs(backButton) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
                .size(48.dp)
                .padding(8.dp),
            painter = painterResource(id = R.drawable.ic_arrow),
            colorFilter = ColorFilter.tint(Color.White),
            contentDescription = "",
        )

        Text(
            modifier = Modifier.constrainAs(titleText) {
                start.linkTo(backButton.end, 16.dp)
                top.linkTo(backButton.top)
                bottom.linkTo(backButton.bottom)
            },
            text = title,
            style = MaterialTheme.typography.h6,
            color = Color.White,
            fontSize = 20.sp,
            fontFamily = FontFamily(Font(R.font.inter_semibold))
        )
    }
}