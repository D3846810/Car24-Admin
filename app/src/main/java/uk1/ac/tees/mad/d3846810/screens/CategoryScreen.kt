package uk1.ac.tees.mad.d3846810.screens

import android.net.Uri
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import uk1.ac.tees.mad.d3846810.R
import uk1.ac.tees.mad.d3846810.model.CategoryModel
import uk1.ac.tees.mad.d3846810.screens.widgets.CustomToolbar
import uk1.ac.tees.mad.d3846810.theme.c10

@Composable
fun CategoryScreen(
    imgUri: Uri?,
    categoryList: List<CategoryModel>,
    loadingText: String,
    onBackPressed: () -> Unit,
    validateCategory: (String) -> Unit,
    pickImage: () -> Unit,
    onItemEditClick: (categoryModel: CategoryModel) -> Unit
) {
    var categoryName by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }
    ConstraintLayout (modifier = Modifier
        .background(Color.White)
        .fillMaxSize()){
        val (toolbar, createColumn, loading, lazyColumn) = createRefs()
        CustomToolbar(title = "Create Category", modifier = Modifier.constrainAs(toolbar) {
                start.linkTo(parent.start)
                top.linkTo(parent.top)
                end.linkTo(parent.end)
            }) {
            onBackPressed()
        }

        Column(modifier = Modifier
            .constrainAs(createColumn) {
                start.linkTo(parent.start)
                top.linkTo(toolbar.bottom)
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

                OutlinedTextField(
                    value = categoryName,
                    isError = isError,
                    onValueChange = {
                        categoryName = it
                        isError = false
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, bottom = 8.dp),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
                    placeholder = {
                        Text(text = "Category Name")
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.White,
                        focusedIndicatorColor = c10,
                        unfocusedIndicatorColor = Color.Gray,
                        cursorColor = c10,
                        focusedLabelColor = c10
                    )
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
                    if (categoryName.isEmpty()) {
                        isError = true
                        return@Button
                    }
                    validateCategory(categoryName)
                }) {
                Text("Create Category", color = Color.White)
            }
        }
        if (categoryList.isEmpty()) {
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
            LazyColumn(modifier = Modifier.constrainAs(lazyColumn) {
                start.linkTo(parent.start)
                top.linkTo(createColumn.bottom)
                end.linkTo(parent.end)
            }) {
                items(items = categoryList) {
                    CategoryItem(categoryModel = it) {
                        onItemEditClick(it)
                    }
                }
            }
        }
    }
}


@Composable
fun CategoryItem(
    categoryModel: CategoryModel, onEditClick: (categoryModel: CategoryModel) -> Unit
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(8.dp)
    ) {
        val (cardView, categoryName, editButton, divider) = createRefs()

        Card(modifier = Modifier
            .constrainAs(cardView) {
                start.linkTo(parent.start)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            }
            .size(80.dp)
            .clip(CircleShape)
            .background(Color.White)
            .border(0.dp, Color.Transparent, CircleShape), elevation = 0.dp) {
            AsyncImage(
                model = categoryModel.imgUrl,
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape),
                placeholder = painterResource(id = R.drawable.placeholder),
//                painter = painterResource(R.drawable.placeholder),
                contentDescription = "",
                contentScale = ContentScale.Crop
            )
        }

        Text(
            modifier = Modifier.constrainAs(categoryName) {
                start.linkTo(cardView.end, 16.dp)
                top.linkTo(cardView.top)
                bottom.linkTo(cardView.bottom)
            },
            text = categoryModel.categoryName,
            style = MaterialTheme.typography.body2,
            color = Color.Black
        )

        Image(modifier = Modifier
            .clickable {
                onEditClick(categoryModel)
            }
            .constrainAs(editButton) {
                end.linkTo(parent.end)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            }
            .size(48.dp)
            .background(shape = RectangleShape, color = Color.White)
            .border(1.dp, Color.Black, RoundedCornerShape(percent = 10))
            .padding(10.dp),
            painter = painterResource(id = R.drawable.ic_edit),
            contentDescription = "")

        Divider(modifier = Modifier
            .constrainAs(divider) {
                start.linkTo(categoryName.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
            }
            .fillMaxWidth()
            .height(1.dp)
            .padding(start = 32.dp, end = 50.dp),
            color = Color.Gray)
    }
}

@Composable
@Preview
fun CategoryScreenPreview() {
    CategoryScreen(imgUri = null,
        categoryList = arrayListOf(),
        loadingText = "Loading...",
        onBackPressed = {},
        validateCategory = {},
        pickImage = {},
        onItemEditClick = {})
}

//@Composable
//@Preview
//fun itemCategoryPreview() {
//}