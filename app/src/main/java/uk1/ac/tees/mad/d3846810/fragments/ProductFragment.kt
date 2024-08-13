package uk1.ac.tees.mad.d3846810.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import uk1.ac.tees.mad.d3846810.model.ProductModel
import uk1.ac.tees.mad.d3846810.screens.ProductScreen
import uk1.ac.tees.mad.d3846810.utils.Constants
import uk1.ac.tees.mad.d3846810.utils.Utils

//main product list
class ProductFragment : Fragment() {
    //    private val binding by lazy { FragmentProductBinding.inflate(layoutInflater) }
//    private lateinit var productAdapter: ProductAdapter
    private val productList = mutableStateListOf<ProductModel>()
    private var loadingTxt by mutableStateOf("Loading...")
    private lateinit var database: FirebaseDatabase

    private lateinit var composeView: ComposeView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        return ComposeView(requireContext()).also {
            composeView = it
        }
//        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setComposeContent()

        database = FirebaseDatabase.getInstance()
//        productAdapter = ProductAdapter()


//        binding.apply {
//            productRv.layoutManager = LinearLayoutManager(requireContext())
//
//            btBack.setOnClickListener {
//                findNavController().navigateUp()
//            }
//
//            fabAddProduct.setOnClickListener {
//                findNavController().navigate(R.id.action_productFragment_to_addProductFragment)
//            }
//        }

        getProducts()


        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigateUp()
                }

            })

    }

    private fun setComposeContent() {
        composeView.setContent {
            ProductScreen()
        }
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
                    loadingTxt = "No Products Found"
                }
//
//                if(productList.isNotEmpty()) {
//                    binding.productRv.visibility = View.VISIBLE
//                    binding.tvStatus.visibility = View.GONE
//                    productAdapter.submitList(productList)
//                    binding.productRv.adapter = productAdapter
//                } else {
//                    binding.productRv.visibility = View.GONE
//                    binding.tvStatus.visibility = View.VISIBLE
//                    val status = "No Products Found"
//                    binding.tvStatus.text = status
//                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Utils.showMessage(requireContext(), "Failed")
            }
        })
    }


}