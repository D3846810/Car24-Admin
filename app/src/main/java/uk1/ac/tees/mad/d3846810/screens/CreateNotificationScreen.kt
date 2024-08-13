package uk1.ac.tees.mad.d3846810.screens

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import uk1.ac.tees.mad.d3846810.R
import uk1.ac.tees.mad.d3846810.model.NotificationModel
import uk1.ac.tees.mad.d3846810.screens.widgets.CustomToolbar
import uk1.ac.tees.mad.d3846810.screens.widgets.InputField
import uk1.ac.tees.mad.d3846810.screens.listItems.NotificationItem
import uk1.ac.tees.mad.d3846810.theme.c10

@Composable
fun CreateNotificationScreen(
    imgUri: Uri?,
    notificationList: List<NotificationModel>,
    loadingText: String,
    onBackPressed: () -> Unit,
    pickImage: () -> Unit,
    onDelete: (NotificationModel) -> Unit,
    validateNotification: (String, String, String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var notificationUrl by remember { mutableStateOf("") }
    var isErrorTitle: MutableState<Boolean> = remember { mutableStateOf(false) }
    var isErrorDescription: MutableState<Boolean> = remember { mutableStateOf(false) }
    ConstraintLayout(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize()
    ) {
        val (toolbar, createColumn, loading, lazyColumn) = createRefs()
        CustomToolbar(title = "Manage Notification", modifier = Modifier.constrainAs(toolbar) {
            start.linkTo(parent.start)
            top.linkTo(parent.top)
            end.linkTo(parent.end)
        }) {
            onBackPressed()
        }

        ConstraintLayout(modifier= Modifier
            .fillMaxSize()
            .constrainAs(createColumn) {
                start.linkTo(parent.start)
                top.linkTo(toolbar.bottom)
                end.linkTo(parent.end)
            }
            .verticalScroll(rememberScrollState())){
            Column(modifier = Modifier
                .constrainAs(createColumn) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                }
                .fillMaxWidth()
                .padding(16.dp)) {
                Column {
                    Card(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Color.White)
                            .border(0.dp, Color.Transparent, CircleShape), elevation = 0.dp
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            AsyncImage(modifier = Modifier
                                .clickable {
                                    pickImage()
                                }
                                .size(120.dp)
                                .clip(CircleShape),
                                model = imgUri ?: R.drawable.placeholder,
                                contentDescription = "",
                                contentScale = ContentScale.Crop)
                        }

                    }

                    val colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.White,
                        focusedIndicatorColor = c10,
                        unfocusedIndicatorColor = Color.Gray,
                        cursorColor = c10,
                        focusedLabelColor = c10
                    )

                    InputField(value = title, label = "Title", isError = isErrorTitle, onValueChange = {
                        title = it
                        isErrorTitle.value = false
                    })
                    InputField(value = description,
                        label = "Description",
                        isError = isErrorDescription,
                        onValueChange = {
                            description = it
                            isErrorDescription.value = false
                        })
                    InputField(value = notificationUrl, label = "Notification Url", onValueChange = {
                        notificationUrl = it
                    })
                }

                Button(modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                    shape = RoundedCornerShape(4.dp),
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.White, backgroundColor = c10
                    ),
                    onClick = {
                        if (title.isEmpty()) {
                            isErrorTitle.value = true
                        } else if (description.isEmpty()) {
                            isErrorDescription.value = true
                        } else {
                            validateNotification(title, description, notificationUrl)
                        }
                    }) {
                    Text("Create Notification", color = Color.White)
                }
            }
            if (notificationList.isEmpty()) {
                Text(modifier = Modifier
                    .constrainAs(loading) {
                        start.linkTo(parent.start)
                        top.linkTo(createColumn.bottom)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    }
                    .wrapContentSize()
                    .padding(bottom = 16.dp),
                    text = loadingText,
                    style = MaterialTheme.typography.h6,
                    color = Color.Black)
            } else {
                Column(modifier = Modifier
                    .constrainAs(lazyColumn) {
                        start.linkTo(parent.start)
                        top.linkTo(createColumn.bottom)
                        end.linkTo(parent.end)
                    }
                    .padding(16.dp)) {
                    for(notification in notificationList) {
                        NotificationItem(
                            modifier = Modifier,
                            notificationModel = notification,
                            onClick = { onDelete(it) }
                        )
                    }
                }
            }
        }
    }
}


@Composable
@Preview
fun CreateNotificationScreenPreview() {
    val imgUri: Uri? = null
    val notificationList = arrayListOf<NotificationModel>()
    CreateNotificationScreen(imgUri = imgUri,
        notificationList = notificationList,
        loadingText = "Loading...",
        onBackPressed = {},
        pickImage = {},
        onDelete = {}) { title, description, url ->

    }
}

//@Composable
//@Preview
//fun itemCategoryPreview() {
//}