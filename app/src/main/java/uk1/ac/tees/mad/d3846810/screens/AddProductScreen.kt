package uk1.ac.tees.mad.d3846810.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
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


@Composable
fun AddProductScreen(
    productModel: ProductModel,
    categoryList: List<String>,
    onBackPressed: () -> Unit,
    onPickImage: () -> Unit,
    addProductImages: () -> Unit,
    addProduct: (productModel: ProductModel) -> Unit,
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
                .verticalScroll(rememberScrollState())
        ) {
            CustomToolbar(title = "Add Product", modifier = Modifier) {
                onBackPressed()
            }
            //car image
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                val (cardView) = createRefs()

                Card(modifier = Modifier
                    .constrainAs(cardView) {
                        top.linkTo(parent.top, 16.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .size(200.dp), elevation = 4.dp, shape = RoundedCornerShape(16.dp)) {
                    val url = productModel.coverImg.ifEmpty {
                        R.drawable.placeholder
                    }
                    AsyncImage(modifier = Modifier
                        .clickable {
                            onPickImage()
                        }
                        .fillMaxSize(),
                        model = url,
                        contentDescription = "Car Image",
                        placeholder = painterResource(id = R.drawable.placeholder)

                    )
                }
            }

            Button(
                modifier = Modifier
                    .wrapContentWidth()
                    .height(48.dp)
                    .align(Alignment.End)
                    .padding(end = 16.dp, top = 16.dp),
                onClick = { addProductImages() },
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White, backgroundColor = c10
                ),
            ) {
                Text("Add Product Images")
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
                    .horizontalScroll(rememberScrollState())
            ) {
                if (productModel.carImages.isNotEmpty()) {
                    for (carImage: ProductImageUrlModel in productModel.carImages) {
                        ImageItem(modifier = Modifier, productImageUrlModel = carImage) {
                            val list = productModel.carImages as ArrayList
                            list.remove(carImage)
                            productModel.carImages = list
                        }
                    }
                }
            }

            //text fields
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .wrapContentHeight()
            ) {
                InputField(
                    value = title.value, label = "Car Title", isError = titleError,
                    onValueChange = {
                        titleError.value =false
                        title.value = it
                        productModel.title = title.value
                    },
                    modifier = Modifier,
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    InputField(
                        value = year.value, label = "Year", isError = yearError, onValueChange = {
                            yearError.value =false
                            year.value = it
                            productModel.year = year.value
                        }, modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.size(5.dp))

                    InputField(
                        value = rating.value,
                        label = "Rating",
                        isError = ratingError,
                        onValueChange = {
                            ratingError.value =false
                            rating.value = it
                            productModel.rating = rating.value
                        },
                        modifier = Modifier.weight(1f)
                    )
                }

                InputField(
                    value = description.value,
                    label = "Description",
                    isError = descriptionError,
                    onValueChange = {
                        descriptionError.value =false
                        description.value = it
                        productModel.description = description.value
                    },
                    modifier = Modifier
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {

                    InputField(
                        value = price.value,
                        label = "Price",
                        isError = priceError,
                        onValueChange = {
                            priceError.value =false
                            price.value = it
                            productModel.price = price.value
                        },
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.size(5.dp))

                    InputField(
                        value = location.value,
                        label = "Location",
                        isError = locationError,
                        onValueChange = {
                            locationError.value =false
                            location.value = it
                            productModel.location = location.value
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    InputField(
                        value = brand.value,
                        label = "Brand",
                        isError = brandError,
                        onValueChange = {
                            brandError.value =false
                            brand.value = it
                            productModel.brand = brand.value
                        },
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.size(5.dp))

                    //change to auto complete text view
                    DropDownMenu(
                        value = insurance.value,
                        label = "Insurance",
                        modifier = Modifier.weight(1f),
                        isError = insuranceError,
                        menuList = Constants.insurance
                    ) {
                        insurance.value = it
                        insuranceError.value = false
                        productModel.insurance = insurance.value
                    }
                }

                InputField(
                    value = runKm.value, label = "Run KM", isError = runKmError, onValueChange = {
                        runKm.value = it
                        runKmError.value =false
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
                        isError = engineError,
                        menuList = engineList
                    ) {
                        engineError.value = false
                        engine.value = it
                        productModel.engineType = engine.value
                    }
                    Spacer(modifier = Modifier.size(5.dp))

                    DropDownMenu(
                        value = gear.value,
                        label = "Select Gear",
                        modifier = Modifier.weight(1f),
                        isError = gearError,
                        menuList = gearList
                    ) {
                        gearError.value = false
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
                        value = rtoNum.value,
                        label = "RTO Num",
                        isError = rtoNumError,
                        onValueChange = {
                            rtoNumError.value =false
                            rtoNum.value = it
                            productModel.rtoNum = rtoNum.value
                        },
                        modifier = Modifier.weight(0.6f)
                    )

                    Spacer(modifier = Modifier.size(5.dp))

                    DropDownMenu(
                        value = category.value,
                        label = "Select Category",
                        modifier = Modifier.weight(1.4f),
                        isError = categoryError,
                        menuList = categoryList
                    ) {
                        categoryError.value = false
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
                        isError = seatsError,
                        menuList = seatsList
                    ) {
                        seatsError.value = false
                        seats.value = it
                        productModel.seating = seats.value
                    }
                    Spacer(modifier = Modifier.size(5.dp))

                    DropDownMenu(
                        value = available.value,
                        label = "Select Availability",
                        modifier = Modifier.weight(1.3f),
                        isError = availableError,
                        menuList = availabilityList
                    ) {
                        availableError.value = false
                        available.value = it
                        productModel.available = available.value
                    }
                }

                InputField(
                    value = tag.value, label = "Tag", isError = tagError, onValueChange = {
                        tag.value = it
                        tagError.value =false
                        productModel.tag = tag.value
                    }, modifier = Modifier
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    onClick = {
                        if (productModel.title.isEmpty()) {
                           titleError.value = true
                        } else if (productModel.year.isEmpty()) {
                            yearError.value = true
                        } else if (productModel.rating.isEmpty()) {
                            ratingError.value = true
                        } else if (productModel.description.isEmpty()) {
                            descriptionError.value = true
                        } else if (productModel.price.isEmpty()) {
                            priceError.value = true
                        } else if (productModel.brand.isEmpty()) {
                            brandError.value = true
                        } else if (productModel.runKm.isEmpty()) {
                            runKmError.value = true
                        } else if (productModel.insurance.isEmpty()) {
                            insuranceError.value = true
                        } else if (productModel.rtoNum.isEmpty()) {
                            rtoNumError.value = true
                        } else if (productModel.location.isEmpty()) {
                            locationError.value = true
                        } else if (productModel.engineType == "Engine Type" || productModel.engineType.isEmpty()) {
                            engineError.value = true
                        } else if (productModel.gearType == "Select Gear" || productModel.gearType.isEmpty()) {
                            gearError.value = true
                        } else if (productModel.selectCategory == "Select Category" || productModel.selectCategory.isEmpty()) {
                            categoryError.value = true
                        } else if (productModel.seating == "Seats" || productModel.seating.isEmpty()) {
                            seatsError.value = true
                        } else if (productModel.available == "Car Status" || productModel.available.isEmpty()) {
                            availableError.value = true
                        } else {
                            addProduct(productModel)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.White, backgroundColor = c10
                    ),
                ) {
                    Text("Create Product")
                }

            }
        }
    }
}


@Composable
@Preview
fun AddProductScreenPreview() {
    val arrayList = ArrayList<ProductImageUrlModel>()
    arrayList.add(ProductImageUrlModel("", ""))
    arrayList.add(ProductImageUrlModel("", ""))
    arrayList.add(ProductImageUrlModel("", ""))
    AddProductScreen(productModel = ProductModel(
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
        arrayList
    ),
        arrayListOf("menu 1", "menu 2"),
        onBackPressed = {},
        addProduct = {},
        onImageDelete = {},
        addProductImages = {},
        onPickImage = {})
}