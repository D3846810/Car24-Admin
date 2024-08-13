package uk1.ac.tees.mad.d3846810.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import uk1.ac.tees.mad.d3846810.R
import uk1.ac.tees.mad.d3846810.model.ProductModel
import uk1.ac.tees.mad.d3846810.navigations.NavItem
import uk1.ac.tees.mad.d3846810.screens.widgets.CustomText
import uk1.ac.tees.mad.d3846810.screens.widgets.HomeButton
import uk1.ac.tees.mad.d3846810.theme.c10
import uk1.ac.tees.mad.d3846810.theme.semiTransparent
import uk1.ac.tees.mad.d3846810.utils.Constants
import uk1.ac.tees.mad.d3846810.utils.Utils

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier, navController: NavHostController = rememberNavController()
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        Column(modifier = modifier.fillMaxSize()) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .background(c10)
                    .height(190.dp)
            ) {
                Box(
                    modifier = modifier
                        .fillMaxSize()
                        .background(color = semiTransparent)
                ) {
                    Column(
                        modifier = modifier
                            .fillMaxSize()
                            .padding(start = 16.dp, bottom = 16.dp),
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        Image(
                            modifier = modifier.size(80.dp),
                            painter = painterResource(id = R.drawable.icon),
                            contentDescription = ""
                        )
                        Spacer(modifier = modifier.height(8.dp))
                        CustomText(
                            text = stringResource(id = R.string.app_name),
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                        CustomText(
                            modifier = modifier.alpha(0.6.toFloat()),
                            text = "Control full Application from here... ",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = modifier.height(16.dp))
                Row(
                    modifier = modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    HomeButton(
                        text = "Create\nCategory"
                    ) {
                        navController.navigate(NavItem.Category.title)
                    }
                    HomeButton(
                        text = "Manage\nAll Products"
                    ) {
                        navController.navigate(NavItem.AllProducts.title)
                    }
                }
                Spacer(modifier = modifier.height(16.dp))
                Row(
                    modifier = modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    HomeButton(text = "View All\nBookings") {
                        navController.navigate(NavItem.AllBookings.title)
                    }
                    HomeButton(text = "Create\nSlider Images") {
                        navController.navigate(NavItem.CreateSlider.title)
                    }
                }
                Spacer(modifier = modifier.height(16.dp))
                Row(
                    modifier = modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    HomeButton(text = "Create\nNotifications") {
                        navController.navigate(NavItem.Notifications.title)
                    }
                    HomeButton(text = "Manage\nLogin") {
                        navController.navigate(NavItem.Login.title)
                    }
                }
                Spacer(modifier = modifier.height(16.dp))
            }
        }
    }
}


@Preview
@Composable
private fun PreviewHomeScreen() {
    HomeScreen()
}