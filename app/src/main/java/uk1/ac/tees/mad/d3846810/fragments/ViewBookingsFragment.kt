package uk1.ac.tees.mad.d3846810.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import uk1.ac.tees.mad.d3846810.App
import uk1.ac.tees.mad.d3846810.R
import uk1.ac.tees.mad.d3846810.databinding.DialogUpdateStatusBinding
import uk1.ac.tees.mad.d3846810.model.BookingModel
import uk1.ac.tees.mad.d3846810.model.ProductModel
import uk1.ac.tees.mad.d3846810.screens.ViewBookingScreen
import uk1.ac.tees.mad.d3846810.utils.Constants
import uk1.ac.tees.mad.d3846810.utils.Utils


class ViewBookingsFragment : Fragment() {
    //    private val binding by lazy { FragmentViewBookingsBinding.inflate(layoutInflater) }
    private lateinit var database: FirebaseDatabase
    private val productList = mutableStateListOf<ProductModel>()
    private lateinit var composeView: ComposeView
    private val bookingList = mutableStateListOf<BookingModel>()
    private val loadingTxt = mutableStateOf("Bookings Loading...")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
//        return binding.root
        return ComposeView(requireContext()).also {
            composeView = it
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setComposeContent()
        database = FirebaseDatabase.getInstance()

        getAvailableBookings()

//        binding.ivBookingBack.setOnClickListener {
//            findNavController().navigateUp()
//        }

//        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
//            object : OnBackPressedCallback(true) {
//                override fun handleOnBackPressed() {
//                    findNavController().navigateUp()
//                }
//
//            })
    }


    private fun getProductData(productId: String) {
        val productRef = Firebase.database.getReference(Constants.PRODUCTS_REF).child(productId)
        productRef.get().addOnSuccessListener {
            val productModel = it.getValue(ProductModel::class.java)
            if (productModel != null) {
                productList.add(productModel)
            }
            Log.d("TAG_test", "onDataChange: product ${productList.size}")

        }
    }

    private fun getAvailableBookings() {
        database.getReference(Constants.BOOKING_REF)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    bookingList.clear()
                    for (userSnapshot in snapshot.children) {
                        for (bookingSnapshot in userSnapshot.children) {
                            val booking = bookingSnapshot.getValue(BookingModel::class.java)
                            if (booking != null) {
                                bookingList.add(booking)
                            }
                        }
                    }

                    Log.d("TAG_test", "onDataChange: booking ${bookingList.size}")
                    if (bookingList.isEmpty()) {
                        loadingTxt.value = "No Booking Found"
                    } else {
                        for (booking: BookingModel in bookingList) {
                            getProductData(booking.productId)
                        }
                    }

//                if(bookingList.isEmpty()) {
//                    binding.tvBookingStatus.visibility = View.VISIBLE
//                    binding.tvBookingStatus.text = "No Bookings Found"
//                    binding.bookingRv.visibility = View.GONE
//                } else {
//                    binding.tvBookingStatus.visibility = View.GONE
//                    binding.bookingRv.visibility = View.VISIBLE
//                    val bookingAdapter =
//                        BookingProductAdapter(requireContext(), this@ViewBookingsFragment)
//                    bookingAdapter.submitList(bookingList)
//                    binding.bookingRv.adapter = bookingAdapter
//                }

                }


                override fun onCancelled(error: DatabaseError) {

                }

            })

    }


    private fun setComposeContent() {
        composeView.setContent {
            ViewBookingScreen(bookingList = bookingList,
                productList = productList,
                loadingText = loadingTxt.value,
                onBackPressed = {
                    (requireActivity().application as App).navController.navigateUp()
                }) {
                onItemClick(it)
            }
        }
    }

    private fun updateStatus(item: BookingModel, editLayout: DialogUpdateStatusBinding) {

        val statusText = editLayout.etTitle.text.toString()
        val status = mapOf<String, Any>(
            "status" to statusText
        )

        val bookingRef =
            database.getReference(Constants.BOOKING_REF).child(item.userId).child(item.bookingId)



        bookingRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    bookingRef.updateChildren(status).addOnSuccessListener {

                        Utils.showMessage(requireContext(), "Status Updated Successfully")
                    }.addOnFailureListener {

                        Utils.showMessage(
                            requireContext(), "Error updating status: ${it.localizedMessage}"
                        )
                    }
                } else {
                    Utils.showMessage(
                        requireContext(), "Booking does not exist at the specified path."
                    )
                }
            }

            override fun onCancelled(error: DatabaseError) {

                Utils.showMessage(requireContext(), "Database error: ${error.message}")
            }
        })

    }

    private fun onItemClick(bookingModel: BookingModel) {
        val editDialog = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog).create()
        val updateLayout = DialogUpdateStatusBinding.inflate(layoutInflater)
        editDialog.setView(updateLayout.root)

        updateLayout.etTitle.setText(bookingModel.status)
        val genderAdapter =
            ArrayAdapter(requireContext(), R.layout.drop_down, Constants.BOOKING_STATUS)
        updateLayout.etTitle.setAdapter(genderAdapter)

        editDialog.setCanceledOnTouchOutside(true)

        updateLayout.btCancel.setOnClickListener {
            editDialog.dismiss()
        }

        updateLayout.btUpdate.setOnClickListener {
            if (updateLayout.etTitle.text.isNotEmpty()) {
                updateStatus(bookingModel, updateLayout)
                editDialog.dismiss()
            } else {
                Utils.showMessage(requireContext(), "Something went wrong")
            }
        }

        editDialog.show()
    }

}