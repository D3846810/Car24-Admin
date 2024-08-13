package uk1.ac.tees.mad.d3846810.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uk1.ac.tees.mad.d3846810.R
import uk1.ac.tees.mad.d3846810.theme.c10
import uk1.ac.tees.mad.d3846810.utils.Utils

class InternetActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InternetContent()
        }
//        setContentView(binding.root)
//        binding.apply {
//
//            btRetry.setOnClickListener {
//                retry()
//            }
//
//
//        }


    }

    private fun retry() {
        if(Utils.isConnected(this@InternetActivity)) {
            startActivity(Intent(this@InternetActivity, MainActivity::class.java))
            finish()
        } else {
            Toast.makeText(this@InternetActivity, "No Internet Found", Toast.LENGTH_SHORT).show()
        }
    }

    @Composable
    private fun InternetContent() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_wifi),
                    contentDescription = null,
                    modifier = Modifier
                        .size(120.dp)
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = 8.dp)
                )
                Text(
                    text = "Checking Internet Connection!",
                    style = androidx.compose.material3.MaterialTheme.typography.titleMedium,
                    fontSize = 18.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Text(
                    text = "This app required internet Connection. Please check your internet connection...",
                    style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 4.dp, bottom = 16.dp)
                )
                Button(
                    onClick = { retry() },
                    colors = ButtonColors(
                        contentColor = Color.White, containerColor = c10, disabledContentColor = Color.White, disabledContainerColor = c10
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Text("Retry")
                }
            }
        }
    }
    @Preview
    @Composable
    private fun InternetContentPreview() {
        InternetContent()
    }
}
