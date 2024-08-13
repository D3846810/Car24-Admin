package uk1.ac.tees.mad.d3846810.screens.listItems

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import uk1.ac.tees.mad.d3846810.R
import uk1.ac.tees.mad.d3846810.model.NotificationModel
import uk1.ac.tees.mad.d3846810.theme.lightGray

@Composable
fun NotificationItem(modifier: Modifier = Modifier, notificationModel: NotificationModel, onClick: (NotificationModel) -> Unit) {
    Box (modifier = modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .padding(8.dp)
    ){
        ConstraintLayout(
            modifier = modifier
                .clickable {
                    onClick(notificationModel)
                }
                .fillMaxWidth()
                .wrapContentHeight()
                .background(color = Color.White, shape = RoundedCornerShape(8.dp))
                .border(.75.dp, lightGray, RoundedCornerShape(8.dp))
        ) {
            Column(modifier = modifier) {
                if (notificationModel.notificationImg.isNotEmpty()) {
                    Card(
                        modifier = modifier
                            .fillMaxWidth()
                            .background(
                                lightGray,
                                RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)
                            )
                    ) {
                        AsyncImage(
                            modifier = modifier
                                .fillMaxWidth()
                                .height(180.dp),
                            contentScale = ContentScale.Crop,
                            model = notificationModel.notificationImg,
                            contentDescription = "",
                            placeholder = painterResource(id = R.drawable.placeholder),
                            error = painterResource(id = R.drawable.placeholder)
                        )
                    }
                }
                Text(
                    modifier = modifier
                        .padding(top = 16.dp, start = 8.dp),
                    text = notificationModel.notificationTitle,
                    fontSize = 18.sp, fontFamily = INTER, fontWeight = FontWeight.SemiBold,
                )
                Text(
                    modifier = modifier
                        .padding(start = 8.dp, bottom = 8.dp)
                        .alpha(0.8.toFloat()),
                    text = notificationModel.notificationDescription,
                    fontSize = 14.sp, fontFamily = INTER, fontWeight = FontWeight.SemiBold,
                )
            }
        }
    }
}

@Preview
@Composable
private fun previewNotificationItem() {
    NotificationItem(
        modifier = Modifier, NotificationModel(
            "a",
            "a",
            "Notification",
            "Description",
        )
    ){

    }
}