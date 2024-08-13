package uk1.ac.tees.mad.d3846810.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import uk1.ac.tees.mad.d3846810.R
import uk1.ac.tees.mad.d3846810.model.ProductImageUrlModel
import uk1.ac.tees.mad.d3846810.model.ProductModel
import uk1.ac.tees.mad.d3846810.screens.widgets.CustomToolbar
import uk1.ac.tees.mad.d3846810.screens.widgets.DropDownMenu
import uk1.ac.tees.mad.d3846810.screens.listItems.ImageItem
import uk1.ac.tees.mad.d3846810.screens.widgets.InputField
import uk1.ac.tees.mad.d3846810.theme.c10
import uk1.ac.tees.mad.d3846810.utils.Constants
import uk1.ac.tees.mad.d3846810.utils.Constants.availabilityList
import uk1.ac.tees.mad.d3846810.utils.Constants.engineList
import uk1.ac.tees.mad.d3846810.utils.Constants.gearList
import uk1.ac.tees.mad.d3846810.utils.Constants.seatsList
import uk1.ac.tees.mad.d3846810.utils.onClick


@Composable
fun EditProductScreen(
    productModel: ProductModel = ProductModel(),
    categoryList: List<String>,
    onBackPressed: () -> Unit,
    onPickImage: () -> Unit,
    onSaveChanges: (productModel: ProductModel) -> Unit,
    onProductDelete: () -> Unit,
    onImageDelete: (productImageUrl: ProductImageUrlModel) -> Unit,
) {

    val title = remember { mutableStateOf(productModel.title) }
    val titleError = remember { mutableStateOf(false) }
    val year = remember { mutableStateOf(productModel.year) }
    val yearError = remember { mutableStateOf(false) }
    val rating = remember { mutableStateOf(productModel.rating) }
    val ratingError = remember { mutableStateOf(false) }
    val description = remember { mutableStateOf(productModel.description) }
    val descriptionError = remember { mutableStateOf(false) }
    val price = remember { mutableStateOf(productModel.price) }
    val priceError = remember { mutableStateOf(false) }
    val location = remember { mutableStateOf(productModel.location) }
    val locationError = remember { mutableStateOf(false) }
    val brand = remember { mutableStateOf(productModel.brand) }
    val brandError = remember { mutableStateOf(false) }
    val insurance = remember { mutableStateOf(productModel.insurance) }
    val insuranceError = remember { mutableStateOf(false) }
    val runKm = remember { mutableStateOf(productModel.runKm) }
    val runKmError = remember { mutableStateOf(false) }
    val engine = remember { mutableStateOf(productModel.engineType) }
    val engineError = remember { mutableStateOf(false) }
    val gear = remember { mutableStateOf(productModel.gearType) }
    val gearError = remember { mutableStateOf(false) }
    val rtoNum = remember { mutableStateOf(productModel.rtoNum) }
    val rtoNumError = remember { mutableStateOf(false) }
    val category = remember { mutableStateOf(productModel.selectCategory) }
    val categoryError = remember { mutableStateOf(false) }
    val seats = remember { mutableStateOf(productModel.seating) }
    val seatsError = remember { mutableStateOf(false) }
    val available = remember { mutableStateOf(productModel.available) }
    val availableError = remember { mutableStateOf(false) }
    val tag = remember { mutableStateOf(productModel.tag) }
    val tagError = remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            CustomToolbar(title = "Edit Product", modifier = Modifier) {
                onBackPressed()
            }

            Column(modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())) {
                //car image and delete button
                ConstraintLayout(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    val (cardView, deleteButton) = createRefs()

                    Card(modifier = Modifier
                        .constrainAs(cardView) {
                            top.linkTo(parent.top, 16.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                        .size(200.dp), elevation = 4.dp, shape = RoundedCornerShape(16.dp)) {
                        AsyncImage(modifier = Modifier
                            .clickable {
                                onPickImage()
                            }
                            .fillMaxSize(),
                            model = productModel.coverImg,
                            contentDescription = "Car Image",
                            placeholder = painterResource(id = R.drawable.placeholder),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Image(
                        modifier = Modifier
                            .onClick { onProductDelete() }
                            .constrainAs(deleteButton) {
                                top.linkTo(parent.top, 16.dp)
                                end.linkTo(parent.end, 16.dp)
                            }
                            .size(40.dp)
                            .clip(shape = RoundedCornerShape(8.dp))
                            .background(color = Color.White)
                            .border(1.dp, color = Color.Gray, shape = RoundedCornerShape(8.dp))
                            .padding(6.dp),
                        contentDescription = stringResource(id = R.string.app_name),
                        painter = painterResource(id = R.drawable.ic_delete),
                    )
                }

                //text fields
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .wrapContentHeight()
                ) {
                    InputField(
                        value = title.value, label = "Car Title", isError = titleError, onValueChange = {
                            title.value = it
                            productModel.title = title.value
                        }, modifier = Modifier
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        InputField(
                            value = year.value, label = "Year", isError = yearError, onValueChange = {
                                year.value = it
                                productModel.year = year.value
                            }, modifier = Modifier.weight(1f)
                        )

                        Spacer(modifier = Modifier.size(5.dp))

                        InputField(
                            value = rating.value, label = "Rating",isError = ratingError, onValueChange = {
                                rating.value = it
                                productModel.rating = rating.value
                            }, modifier = Modifier.weight(1f)
                        )
                    }

                    InputField(
                        value = description.value, label = "Description", isError = descriptionError,onValueChange = {
                            description.value = it
                            productModel.description = description.value
                        }, modifier = Modifier
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {

                        InputField(
                            value = price.value, label = "Price", isError = priceError,onValueChange = {
                                price.value = it
                                productModel.price = price.value
                            }, modifier = Modifier.weight(1f)
                        )

                        Spacer(modifier = Modifier.size(5.dp))

                        InputField(
                            value = location.value, label = "Location", isError = locationError,onValueChange = {
                                location.value = it
                                productModel.location = location.value
                            }, modifier = Modifier.weight(1f)
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        InputField(
                            value = brand.value, label = "Brand", isError = brandError,onValueChange = {
                                brand.value = it
                                productModel.brand = brand.value
                            }, modifier = Modifier.weight(1f)
                        )

                        Spacer(modifier = Modifier.size(5.dp))

                        //change to auto complete text view
                        DropDownMenu(
                            value = insurance.value,
                            label = "Insurance",
                            modifier = Modifier.weight(1f),
                            menuList = Constants.insurance
                        ) {
                            insurance.value = it
                            productModel.insurance = insurance.value
                        }
                    }

                    InputField(
                        value = runKm.value, label = "Run KM", isError = runKmError,onValueChange = {
                            runKm.value = it
                            productModel.runKm = runKm.value
                        }, modifier = Modifier
                    )


                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        //change to auto complete
                        DropDownMenu(
                            value = engine.value,
                            label = "Select Engine",
                            modifier = Modifier.weight(1f),
                            menuList = engineList
                        ) {
                            engine.value = it
                            productModel.engineType = engine.value
                        }
                        Spacer(modifier = Modifier.size(5.dp))

                        DropDownMenu(
                            value = gear.value,
                            label = "Select Gear",
                            modifier = Modifier.weight(1f),
                            menuList = gearList
                        ) {
                            gear.value = it
                            productModel.gearType = gear.value
                        }
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        InputField(
                            value = rtoNum.value, label = "RTO Num", onValueChange = {
                                rtoNum.value = it
                                productModel.runKm = rtoNum.value
                            }, modifier = Modifier.weight(0.6f)
                        )

                        Spacer(modifier = Modifier.size(5.dp))

                        DropDownMenu(
                            value = category.value,
                            label = "Select Category",
                            modifier = Modifier.weight(1.4f),
                            menuList = categoryList
                        ) {
                            category.value = it
                            productModel.selectCategory = category.value
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {

                        DropDownMenu(
                            value = seats.value,
                            label = "Seats",
                            modifier = Modifier.weight(0.7f),
                            menuList = seatsList
                        ) {
                            seats.value = it
                            productModel.seating = seats.value
                        }
                        Spacer(modifier = Modifier.size(5.dp))

                        DropDownMenu(
                            value = available.value,
                            label = "Select Availability",
                            modifier = Modifier.weight(1.3f),
                            menuList = availabilityList
                        ) {
                            available.value = it
                            productModel.available = available.value
                        }
                    }

                    InputField(
                        value = tag.value, label = "Tag", onValueChange = {
                            tag.value = it
                            productModel.tag = tag.value
                        }, modifier = Modifier
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                    ) {
                        if (productModel.carImages.isNotEmpty()) {
                            for (carImage: ProductImageUrlModel in productModel.carImages) {
                                ImageItem(modifier = Modifier, productImageUrlModel = carImage) {
                                    onImageDelete(carImage)
                                }
                            }
                        }
                    }

                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        onClick = { onSaveChanges(productModel) },
                        colors = ButtonDefaults.buttonColors(
                            contentColor = Color.White, backgroundColor = c10
                        ),
                    ) {
                        Text("Save Changes")
                    }

                }
            }
        }
    }
}



@Composable
@Preview
fun EditProductScreenPreview() {
    EditProductScreen(productModel = ProductModel(
        "car",
        "car",
        "car",
        "car",
        "car",
        "car",
        "car",
        "car",
        "car",
        "car",
        "car",
        "car",
        "car",
        "car",
        "car",
        "car",
        "car",
        "car",
    ),
        arrayListOf("menu 1", "menu 2"),
        onBackPressed = {},
        onSaveChanges = {},
        onProductDelete = {},
        onImageDelete = {},
        onPickImage = {})
}