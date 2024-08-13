package uk1.ac.tees.mad.d3846810.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import uk1.ac.tees.mad.d3846810.R
import uk1.ac.tees.mad.d3846810.model.ProductModel
import uk1.ac.tees.mad.d3846810.navigations.NavItem
import uk1.ac.tees.mad.d3846810.screens.widgets.CustomToolbar
import uk1.ac.tees.mad.d3846810.theme.c10
import uk1.ac.tees.mad.d3846810.utils.Constants

@Composable
fun ProductScreen(
    navController: NavHostController = rememberNavController(),
    onProductId: (String) -> Unit = {},
    onError: (String) -> Unit = {}
) {
    var loading by remember { mutableStateOf("Loading...") }
    val productList = remember { mutableStateListOf<ProductModel>() }
//    productList.add(ProductModel())
    val database = FirebaseDatabase.getInstance()
    val carRef = database.getReference(Constants.PRODUCTS_REF)
    carRef.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            productList.clear()
            for (carSnapshot in dataSnapshot.children) {
                val product = carSnapshot.getValue(ProductModel::class.java)
                if (product != null) {
                    productList.add(product)
                } else {
                    // Handle the case where data couldn't be converted

                }
            }
            if (productList.isEmpty()) {
                loading = "No Products Found"
            }
        }

        override fun onCancelled(databaseError: DatabaseError) {
            onError("Failed to load products!!!")
        }
    })


    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        val (constraintLayout, column, box) = createRefs()

        CustomToolbar(title = "Available Products",
            modifier = Modifier.constrainAs(constraintLayout) {
                start.linkTo(parent.start)
                top.linkTo(parent.top)
                end.linkTo(parent.end)
            }) {
            navController.navigateUp()
        }

//        list view
        Box(
            modifier = Modifier
                .constrainAs(column) {
                    top.linkTo(constraintLayout.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }
                .fillMaxSize()
                .padding(16.dp),
        ) {
//            ProductItem(productModel = ProductModel(title = "Car", price = "1000", available = "Available", coverImg = "")) {}
            //list view
            if (productList.isEmpty()) {
                Text(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .wrapContentSize()
                        .padding(bottom = 16.dp),
                    text = loading,
                    style = MaterialTheme.typography.h6,
                    color = Color.Black
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                for (product: ProductModel in productList) {
                    ProductItem(productModel = product) {
//                    onItemClick(product.productId)
                        onProductId(product.productId)
                    }
                }
            }
        }

        Box(modifier = Modifier
            .constrainAs(box) {
                bottom.linkTo(parent.bottom)
                end.linkTo(parent.end)
            }
            .fillMaxSize(), contentAlignment = Alignment.BottomEnd) {
            FloatingActionButton(
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.padding(end = 32.dp, bottom = 32.dp),
                onClick = {
                    navController.navigate(NavItem.AddProduct.title)
                },
                backgroundColor = c10,
                contentColor = Color.White
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_add), contentDescription = ""
                )
            }
        }

    }
}

@Composable
fun ProductItem(productModel: ProductModel, onItemClick: (productModel: ProductModel) -> Unit) {
    Card(modifier = Modifier
        .clickable { onItemClick(productModel) }
        .fillMaxWidth()
        .wrapContentHeight()
        .padding(8.dp), elevation = 4.dp, shape = RoundedCornerShape(16.dp)) {
        ConstraintLayout(
            modifier = Modifier.fillMaxWidth()
        ) {
            val (image, title, price, availability) = createRefs()

            AsyncImage(modifier = Modifier
                .constrainAs(image) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .height(160.dp)
                .fillMaxWidth(),
                model = productModel.coverImg,
                contentDescription = "",
                contentScale = ContentScale.Crop)

            Text(
                modifier = Modifier.constrainAs(title) {
                    top.linkTo(image.bottom, 8.dp)
                    start.linkTo(parent.start, 16.dp)
                },
                text = productModel.title,
                style = MaterialTheme.typography.body2,
                color = Color.Black
            )

            Text(
                modifier = Modifier.constrainAs(price) {
                    top.linkTo(title.bottom)
                    start.linkTo(parent.start, 16.dp)
                    bottom.linkTo(parent.bottom, 8.dp)
                },
                text = "${stringResource(id = R.string.currencySymbol)}${productModel.price}",
                style = MaterialTheme.typography.h6,
                color = Color.Black,
                fontSize = 20.sp
            )

            Text(modifier = Modifier
                .constrainAs(availability) {
                    bottom.linkTo(image.bottom, 8.dp)
                    start.linkTo(image.start, 8.dp)
                }
                .background(shape = RoundedCornerShape(6.dp), color = Color.White)
                .border(1.dp, Color.Gray, RoundedCornerShape(6.dp))
                .padding(horizontal = 8.dp, vertical = 4.dp),
                text = productModel.available,
                style = MaterialTheme.typography.body2,
                color = Color.Black,
                fontSize = 14.sp)
        }
    }
}

@Composable
@Preview
fun ProductScreenPreview() {
    ProductScreen(navController = rememberNavController(), onError = {})
}

@Composable
@Preview
fun itemProductPreview() {
    ProductItem(
        productModel = ProductModel(
            title = "Car", price = "1000", available = "Available", coverImg = ""
        )
    ) {

    }
}