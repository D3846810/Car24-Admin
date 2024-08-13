package uk1.ac.tees.mad.d3846810.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import uk1.ac.tees.mad.d3846810.model.BookingModel
import uk1.ac.tees.mad.d3846810.model.ProductModel
import uk1.ac.tees.mad.d3846810.screens.listItems.BookingItem
import uk1.ac.tees.mad.d3846810.screens.widgets.CustomToolbar

@Composable
fun ViewBookingScreen(
    bookingList: List<BookingModel> = arrayListOf(),
    productList: List<ProductModel> = arrayListOf(),
    loadingText: String,
    onBackPressed: () -> Unit,
    onItemClick: (bookingModel: BookingModel) -> Unit,
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        val (constraintLayout, column, loadingTxt) = createRefs()

        CustomToolbar(title = "All Bookings", modifier = Modifier.constrainAs(constraintLayout) {
            start.linkTo(parent.start)
            top.linkTo(parent.top)
            end.linkTo(parent.end)
        }) {
            onBackPressed()
        }

        if (bookingList.isEmpty() || productList.isEmpty()) {
            Text(modifier = Modifier
                .constrainAs(loadingTxt) {
                    top.linkTo(constraintLayout.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
                .wrapContentSize()
                .padding(bottom = 16.dp),
                text = loadingText,
                style = MaterialTheme.typography.h6,
                color = Color.Gray)
        } else {
            LazyColumn(modifier = Modifier
                .constrainAs(column) {
                    top.linkTo(constraintLayout.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }
                .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {
                //list view
                items(items = bookingList) {
                    val product = getProductData(it.productId, productList)
                    product?.let { p ->
                        BookingItem(booking = it, productModel = p) {
                            onItemClick(it)
                        }
                    }
                }
            }
        }
    }
}

fun getProductData(productId: String, productList: List<ProductModel>): ProductModel? {
    for (productModel: ProductModel in productList) {
        if (productModel.productId == productId) {
            return productModel
        }
    }
    return null
}

//private fun getProductData(productId: String, onProductLoad: @Composable (ProductModel)-> Unit)  {
//    val productRef = Firebase.database.getReference(Constants.PRODUCTS_REF).child(productId)
//    productRef.get().addOnSuccessListener {
//        val productModel = it.getValue(ProductModel::class.java)
//        if(productModel != null) {
//            onProductLoad(productModel)
//        }
//
//    }
//}

@Preview
@Composable
private fun ViewBookingScreenPreview() {
    val bookingList = ArrayList<BookingModel>()
    val productList = ArrayList<ProductModel>()
    bookingList.add(BookingModel())
//    bookingList.add(BookingModel())
    productList.add(ProductModel())
//    productList.add(ProductModel())
    ViewBookingScreen(bookingList = bookingList,
        productList = productList,
        loadingText = "Bookings Loading",
        onBackPressed = { },
        onItemClick = {})
}