package uk1.ac.tees.mad.d3846810.screens.listItems

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import uk1.ac.tees.mad.d3846810.R
import uk1.ac.tees.mad.d3846810.model.BookingModel
import uk1.ac.tees.mad.d3846810.model.ProductModel
import uk1.ac.tees.mad.d3846810.screens.widgets.CustomText
import uk1.ac.tees.mad.d3846810.theme.blue
import uk1.ac.tees.mad.d3846810.theme.c10
import uk1.ac.tees.mad.d3846810.theme.lightGray
import uk1.ac.tees.mad.d3846810.theme.textColor
import uk1.ac.tees.mad.d3846810.theme.textColorGray

@Composable
fun BookingItem(booking: BookingModel, productModel: ProductModel, onItemClick: () -> Unit) {
    ConstraintLayout(modifier = Modifier
        .clickable {
            onItemClick()
        }
        .background(color = Color.White, shape = RoundedCornerShape(8.dp))
        .padding(8.dp)) {
        val (imageLayout, midLayout, endLayout) = createRefs()
        Column(modifier = Modifier
            .constrainAs(endLayout) {
                start.linkTo(parent.start)
                top.linkTo(midLayout.bottom, margin = ((-16).dp))
                end.linkTo(parent.end)
            }
            .background(color = Color.White, shape = RoundedCornerShape(8.dp))
            .border(.75.dp, lightGray, RoundedCornerShape(8.dp))) {


            CustomText(
                text = "Booking Details",
                fontSize = 20.sp,
                color = c10,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier
                    .padding(top = 16.dp)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .align(Alignment.CenterHorizontally)
            )

            Column(
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                InfoRow(label = "Name :", info = booking.name)
                InfoRow(label = "Mobile :", info = booking.phoneNumber)
                InfoRow(label = "Email :", info = booking.email)
                InfoRow(label = "Option :", info = booking.reason)
                val date = "This Request booked on ${booking.timestamp}"

                CustomText(
                    text = date,
                    fontSize = 14.sp,
                    color = textColor,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.alpha(0.7f)
                )
                Spacer(modifier = Modifier.height(16.dp))
            }


        }
        Card(modifier = Modifier
            .constrainAs(imageLayout) {
                start.linkTo(parent.start)
                top.linkTo(parent.top)
                end.linkTo(parent.end)
            }
            .background(color = Color.White, shape = RoundedCornerShape(8.dp))
            .border(.75.dp, lightGray, RoundedCornerShape(8.dp))
            .fillMaxWidth()
            .height(190.dp),
            elevation = CardDefaults.cardElevation(0.dp)) {
            Box {
                val url = productModel.coverImg.ifEmpty {
                    R.drawable.placeholder
                }
                AsyncImage(
                    model = url,
                    contentDescription = null,
                    placeholder = painterResource(id = R.drawable.placeholder),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                CustomText(
                    text = productModel.tag,
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .align(Alignment.TopStart)
                        .background(
                            color = blue,
                            shape = RoundedCornerShape(topEnd = 10.dp, bottomEnd = 10.dp)
                        )
                        .padding(start = 4.dp, end = 8.dp),
                    color = Color.White,
                    fontSize = 12.sp
                )
                CustomText(
                    text = booking.status,
                    modifier = Modifier
                        .padding(top = 8.dp, end = 16.dp)
                        .align(Alignment.TopEnd)
                        .background(
                            color = blue, shape = RoundedCornerShape(16.dp)
                        )
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    color = Color.White,
                    fontSize = 16.sp
                )
            }
        }

        Box(modifier = Modifier
            .constrainAs(midLayout) {
                start.linkTo(parent.start)
                top.linkTo(imageLayout.bottom, margin = (-16).dp)
                end.linkTo(parent.end)
            }
            .background(color = Color.White, shape = RoundedCornerShape(8.dp))
            .border(.75.dp, lightGray, RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp))) {
            Column(
                modifier = Modifier
            ) {
                val title = "${productModel.year} ${productModel.title}"

                CustomText(
                    text = title,
                    fontSize = 20.sp,
                    color = textColor,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )
                val subTitle =
                    "${productModel.runKm} km • ${productModel.engineType} • ${productModel.gearType}"

                CustomText(
                    text = subTitle,
                    fontSize = 12.sp,
                    color = textColorGray,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    CustomText(
                        text = stringResource(id = R.string.currencySymbol),
                        color = textColor, fontWeight = FontWeight.ExtraBold,
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    CustomText(
                        text = productModel.price,
                        fontSize = 20.sp,
                        color = textColor,
                        fontWeight = FontWeight.ExtraBold,
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = Color.Gray)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = textColorGray,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    CustomText(
                        text = productModel.location,
                        fontSize = 12.sp,
                        color = textColorGray,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun InfoRow(label: String, info: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 8.dp)
    ) {
        CustomText(
            text = label, fontSize = 16.sp, color = textColor, fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.width(8.dp))
        CustomText(
            text = info,
            fontSize = 16.sp,
            color = textColor,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.weight(1f)
        )
    }
}

val INTER = FontFamily(
    Font(R.font.inter_semibold, FontWeight.SemiBold), Font(R.font.inter_bold, FontWeight.ExtraBold)
)

@Preview
@Composable
private fun previewBookingItem() {
    BookingItem(
        BookingModel(
            "1",
            "1",
            "1",
            "Name",
            "username@gmail.com",
            "121212121212",
            "",
            "Visit",
            "123188238238",
            "Pending",
        ), ProductModel(
            "1",
            "",
            "Mercedes",
            "2016",
            "",
            "",
            "500000",
            "Location",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "New Car",
        )
    ) {

    }
}