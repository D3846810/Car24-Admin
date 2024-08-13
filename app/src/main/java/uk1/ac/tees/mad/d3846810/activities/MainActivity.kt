package uk1.ac.tees.mad.d3846810.activities

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.commit
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import uk1.ac.tees.mad.d3846810.App
import uk1.ac.tees.mad.d3846810.R
import uk1.ac.tees.mad.d3846810.databinding.DialogExitBinding
import uk1.ac.tees.mad.d3846810.fragments.AddProductFragment
import uk1.ac.tees.mad.d3846810.fragments.CategoryFragment
import uk1.ac.tees.mad.d3846810.fragments.EditProductFragment
import uk1.ac.tees.mad.d3846810.fragments.ManageLoginFragment
import uk1.ac.tees.mad.d3846810.fragments.NotificationFragment
import uk1.ac.tees.mad.d3846810.fragments.SliderFragment
import uk1.ac.tees.mad.d3846810.fragments.ViewBookingsFragment
import uk1.ac.tees.mad.d3846810.model.ProductModel
import uk1.ac.tees.mad.d3846810.navigations.NavItem
import uk1.ac.tees.mad.d3846810.screens.HomeScreen
import uk1.ac.tees.mad.d3846810.screens.ProductScreen
import uk1.ac.tees.mad.d3846810.theme.c60
import uk1.ac.tees.mad.d3846810.utils.Constants
import uk1.ac.tees.mad.d3846810.utils.Utils

class MainActivity : AppCompatActivity() {
    //    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var navController: NavHostController
    private val productList = mutableStateListOf<ProductModel>()
    private var productLoadingTxt by mutableStateOf("Loading...")
    private lateinit var database: FirebaseDatabase
    private var currentRoute by mutableStateOf(NavItem.Home.title)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(binding.root)
        database = FirebaseDatabase.getInstance()
        getProducts()
        setContent {
            navController = rememberNavController()
            (application as App).navController = navController
            Surface(
                modifier = Modifier.fillMaxSize(), color = c60
            ) {
                Scaffold { paddingValues ->
                    NavHost(
                        navController = navController, startDestination = NavItem.Home.title
                    ) {
                        composable(NavItem.Home.title) {
                            currentRoute = NavItem.Home.title
                            Box(modifier = Modifier.padding(paddingValues)) {
                                HomeScreen(modifier = Modifier, navController = navController)
                            }
                        }
                        composable(NavItem.Category.title) {
                            currentRoute = NavItem.Category.title
                            Box(modifier = Modifier.padding(paddingValues)) {
                                CategoryScreen()
                            }
                        }
                        composable(NavItem.AllProducts.title) {
                            currentRoute = NavItem.AllProducts.title
                            Box(modifier = Modifier.padding(paddingValues)) {
                                ProductScreen(navController = navController, onProductId = {
                                    (application as App).productId = it
                                    if (it.isNotEmpty()) {
                                        navController.navigate(NavItem.EditProduct.title)
                                    }
                                }, onError = {
                                    Utils.showMessage(this@MainActivity, it)
                                })
                            }
                        }
                        composable(NavItem.EditProduct.title) {
                            currentRoute = NavItem.EditProduct.title
                            Box(modifier = Modifier.padding(paddingValues)) {
                                EditProductScreen()
                            }
                        }
                        composable(NavItem.AddProduct.title) {
                            currentRoute = NavItem.AddProduct.title
                            Box(modifier = Modifier.padding(paddingValues)) {
                                AddProductScreen()
                            }
                        }
                        composable(NavItem.AllBookings.title) {
                            currentRoute = NavItem.AllBookings.title
                            Box(modifier = Modifier.padding(paddingValues)) {
                                ViewAllBookingsScreen()
                            }
                        }
                        composable(NavItem.CreateSlider.title) {
                            currentRoute = NavItem.CreateSlider.title
                            Box(modifier = Modifier.padding(paddingValues)) {
                                CreateSliderScreen()
                            }
                        }
                        composable(NavItem.Notifications.title) {
                            currentRoute = NavItem.Notifications.title
                            Box(modifier = Modifier.padding(paddingValues)) {
                                NotificationScreen()
                            }
                        }
                        composable(NavItem.Login.title) {
                            currentRoute = NavItem.Login.title
                            Box(modifier = Modifier.padding(paddingValues)) {
                                LoginScreen()
                            }
                        }
                    }
                }
            }
            BackHandler {
                if (currentRoute == NavItem.Home.title) {
                    showExitDialog()
                } else {
                    navController.navigate(NavItem.Home.title)
                }
            }
        }
        askNotificationPermission()
    }

    @Composable
    fun CategoryScreen() {
        AndroidViewScreen(R.id.fragment_container_view_category, CategoryFragment())
    }

    @Composable
    fun EditProductScreen() {
        AndroidViewScreen(R.id.fragment_container_view_edit_product, EditProductFragment())
    }

    @Composable
    fun AddProductScreen() {
        AndroidViewScreen(R.id.fragment_container_view_add_product, AddProductFragment())
    }

    @Composable
    fun ViewAllBookingsScreen() {
        AndroidViewScreen(R.id.fragment_container_view_bookings, ViewBookingsFragment())
    }

    @Composable
    fun CreateSliderScreen() {
        AndroidViewScreen(R.id.fragment_container_view_slider, SliderFragment())
    }

    @Composable
    fun NotificationScreen() {
        AndroidViewScreen(R.id.fragment_container_view_notifications, NotificationFragment())
    }

    @Composable
    fun LoginScreen() {
        AndroidViewScreen(R.id.fragment_container_view_login, ManageLoginFragment())
    }

    @Composable
    fun AndroidViewScreen(containerId: Int, fragment: Fragment) {
        val context = LocalContext.current
        AndroidView(factory = { ctx ->
            FragmentContainerView(ctx).apply {
                id = containerId
                (context as? FragmentActivity)?.supportFragmentManager?.commit {
                    replace(id, fragment)
                }
            }
        })
    }

    private fun getProducts() {
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
                    productLoadingTxt = "No Products Found"
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Utils.showMessage(this@MainActivity, "Failed")
            }
        })
    }


    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->

    }

    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this, Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                // TODO: display an educational UI explaining to the user the features that will be enabled
                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                //       If the user selects "No thanks," allow the user to continue without notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun showExitDialog() {
        val bottomSheet = BottomSheetDialog(this)
        val layout = DialogExitBinding.inflate(layoutInflater)
        bottomSheet.setContentView(layout.root)
        bottomSheet.setCanceledOnTouchOutside(true)
        layout.btExit.setOnClickListener {
            bottomSheet.dismiss()
            finish()
        }
        layout.btCancel.setOnClickListener {
            bottomSheet.dismiss()
        }
        bottomSheet.show()
    }
}