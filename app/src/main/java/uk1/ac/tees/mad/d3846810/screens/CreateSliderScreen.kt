package uk1.ac.tees.mad.d3846810.screens

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import uk1.ac.tees.mad.d3846810.R
import uk1.ac.tees.mad.d3846810.model.SliderModel
import uk1.ac.tees.mad.d3846810.screens.listItems.SliderItem
import uk1.ac.tees.mad.d3846810.screens.widgets.CustomToolbar
import uk1.ac.tees.mad.d3846810.screens.widgets.InputField
import uk1.ac.tees.mad.d3846810.theme.c10

@Composable
fun CreateSliderScreen(
    imgUri: Uri?,
    sliderList: List<SliderModel>,
    loadingText: String,
    onBackPressed: () -> Unit,
    pickImage: () -> Unit,
    onDelete: (SliderModel) -> Unit,
    validateSlider: (String) -> Unit
) {
    var sliderUrl by remember { mutableStateOf("") }
    val isError = remember { mutableStateOf(false) }
    ConstraintLayout(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize()
    ) {
        val (toolbar, createColumn,column, loading, lazyColumn) = createRefs()
        CustomToolbar(title = "Manage Slider", modifier = Modifier.constrainAs(toolbar) {
            start.linkTo(parent.start)
            top.linkTo(parent.top)
            end.linkTo(parent.end)
        }) {
            onBackPressed()
        }

        ConstraintLayout(modifier= Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .constrainAs(createColumn) {
                start.linkTo(parent.start)
                top.linkTo(toolbar.bottom)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
                width = androidx.constraintlayout.compose.Dimension.fillToConstraints
                height = androidx.constraintlayout.compose.Dimension.fillToConstraints
            }
            .verticalScroll(rememberScrollState())){
            Column(modifier = Modifier
                .constrainAs(column) {
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
                                contentScale = ContentScale.Crop,
                                placeholder = painterResource(id = R.drawable.placeholder),
                                error = painterResource(id = R.drawable.placeholder))

                        }

                    }

                    InputField(
                        value = sliderUrl,
                        label = "Enter Launch Url",
                        isError = isError,
                        onValueChange = {
                            sliderUrl = it
                            isError.value = false
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp, bottom = 8.dp),
                    )
                }

                Button(modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                    shape = RoundedCornerShape(4.dp),
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.White, backgroundColor = c10
                    ),
                    onClick = {
                        if (sliderUrl.isEmpty()) {
                            isError.value = true
                            return@Button
                        }
                        validateSlider(sliderUrl)
                    }) {
                    Text("Create Slider", color = Color.White)
                }
            }
            if (sliderList.isEmpty()) {
                Text(modifier = Modifier
                    .constrainAs(loading) {
                        start.linkTo(parent.start)
                        top.linkTo(column.bottom)
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
                        top.linkTo(column.bottom)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    }
                    .padding(16.dp)) {
                    for(it in sliderList) {
                        SliderItem(modifier = Modifier, it) {
                            onDelete(it)
                        }
                    }
                }
            }
        }
    }
}


@Composable
@Preview
fun CreateSliderScreenPreview() {
    val imgUri: Uri? = null
    val sliderList = arrayListOf<SliderModel>()
//    sliderList.add(SliderModel())
//    sliderList.add(SliderModel())
//    sliderList.add(SliderModel())
    CreateSliderScreen(imgUri = imgUri,
        sliderList = sliderList,
        loadingText = "Loading...",
        onBackPressed = {},
        pickImage = {},
        onDelete = {}) {

    }
}

//@Composable
//@Preview
//fun itemCategoryPreview() {
//}