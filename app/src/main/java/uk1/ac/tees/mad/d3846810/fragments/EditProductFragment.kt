package uk1.ac.tees.mad.d3846810.fragments

import android.app.Activity
import android.app.AlertDialog
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import uk1.ac.tees.mad.d3846810.App
import uk1.ac.tees.mad.d3846810.R
import uk1.ac.tees.mad.d3846810.databinding.DialogDeleteBinding
import uk1.ac.tees.mad.d3846810.model.CategoryModel
import uk1.ac.tees.mad.d3846810.model.ProductImageUrlModel
import uk1.ac.tees.mad.d3846810.model.ProductModel
import uk1.ac.tees.mad.d3846810.screens.EditProductScreen
import uk1.ac.tees.mad.d3846810.utils.Constants
import uk1.ac.tees.mad.d3846810.utils.Utils
import java.util.UUID

//edit product
class EditProductFragment : Fragment() {
    private lateinit var composeView: ComposeView

    //    private val binding by lazy { FragmentEditProductBinding.inflate(layoutInflater) }
//    private lateinit var productUrl: ProductImageUrlAdapter
    private lateinit var database: FirebaseDatabase
    private lateinit var dbStorage: FirebaseStorage
    private var categoryList = mutableStateListOf<String>()
    private var coverImgUri: Uri? by mutableStateOf(null)
    private lateinit var progress: AlertDialog
    private var newImageList = mutableStateListOf<ProductImageUrlModel>()
    private var product = ""

    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val resultCode = result.resultCode
            val data = result.data

            when (resultCode) {
                Activity.RESULT_OK -> {
                    coverImgUri = data?.data!!
                    productModel.coverImg = coverImgUri.toString()
//                    binding.ivCoverImg.setImageURI(coverImgUri)
                }

                ImagePicker.RESULT_ERROR -> {
                    Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT)
                        .show()
                }

                else -> {
                    Toast.makeText(requireContext(), "Task Cancelled", Toast.LENGTH_SHORT).show()
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        product = (requireActivity().application as App).productId

//        return binding.root
        return ComposeView(requireContext()).also {
            composeView = it
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        productUrl = ProductImageUrlAdapter(this@EditProductFragment)
        database = FirebaseDatabase.getInstance()
        dbStorage = FirebaseStorage.getInstance()
        progress = Utils.showLoading(requireContext())

        loadCategories()
        getProductData(product)


//        binding.ivCoverImg.setOnClickListener {
//            ImagePicker.with(this@EditProductFragment).crop().createIntent { intent ->
//                    startForProfileImageResult.launch(intent)
//                }
//        }

//        binding.apply {
//            btBack.setOnClickListener {
//                findNavController().navigateUp()
//            }

//            btDelete.setOnClickListener {
//                showDeleteConfirmationDialog(product.productId)
//            }

//            btUpdate.setOnClickListener {
//                updateProduct(product.productId)
//            }

//        }

//        requireActivity().onBackPressedDispatcher.addCallback(
//            viewLifecycleOwner,
//            object : OnBackPressedCallback(true) {
//                override fun handleOnBackPressed() {
//                    findNavController().navigateUp()
//                }
//
//            })

    }

    private fun setComposeContent() {
        composeView.setContent {
            EditProductScreen(productModel = productModel,
                categoryList = categoryList,
                onBackPressed = {
                    (requireActivity().application as App).navController.navigateUp()
                },
                onPickImage = {
                    ImagePicker.with(this@EditProductFragment).crop().createIntent { intent ->
                        startForProfileImageResult.launch(intent)
                    }
                },
                onSaveChanges = { updatedProduct ->
                    productModel = updatedProduct
                    updateProduct(productModel.productId)
                },
                onProductDelete = {
                    showDeleteConfirmationDialog(product)
                },
                onImageDelete = { productImageUrlModel ->
                    onItemClick(productImageUrlModel)
                })
        }
    }

    private var productModel by mutableStateOf(ProductModel())

    private fun getProductData(productId: String) {
        val productRef = database.getReference(Constants.PRODUCTS_REF).child(productId)
        productRef.get().addOnSuccessListener {
            it.getValue(ProductModel::class.java)?.let { p ->
                productModel = p
                setProductData(productModel)
                setComposeContent()
            }

        }
    }

    private fun updateProduct(productId: String?) {
        progress.show()
        val productRef = database.getReference(Constants.PRODUCTS_REF).child(productId!!)
//        val updatedTitle = binding.etCarTitle.text.toString()
//        val updatedBrand = binding.etBrand.text.toString()
//        val updatedDescription = binding.etDescription.text.toString()
//        val updatedPrice = binding.etPrice.text.toString()
//        val updatedInsurance = binding.etInsurence.text.toString()
//        val updatedRating = binding.etRating.text.toString()
//        val updatedRtoNum = binding.etRto.text.toString()
//        val updatedTag = binding.etTag.text.toString()
//        val updatedLocation = binding.etLocation.text.toString()
//        val updatedYear = binding.etYear.text.toString()
//        val updatedRunKm = binding.etRunKm.text.toString()
//        val updatedEngineType = binding.etEngine.text.toString()
//        val updatedGearType = binding.etGear.text.toString()
//        val updatedCategory = binding.etCategory.text.toString()
//        val updatedSeating = binding.etSeats.text.toString()
//        val updatedAvailable = binding.etavialability.text.toString()

        val productData = mutableMapOf(
            "title" to productModel.title,
            "brand" to productModel.brand,
            "description" to productModel.description,
            "price" to productModel.price,
            "location" to productModel.location,
            "insurance" to productModel.insurance,
            "rating" to productModel.rating,
            "rtoNum" to productModel.rtoNum,
            "tag" to productModel.tag,
            "year" to productModel.year,
            "runKm" to productModel.runKm,
            "engineType" to productModel.engineType,
            "gearType" to productModel.gearType,
            "selectCategory" to productModel.selectCategory,
            "seating" to productModel.seating,
            "available" to productModel.available,
            "carImages" to newImageList
        )

        if (coverImgUri != null) {
            val fileName = "$${UUID.randomUUID()}.jpg"
            dbStorage.reference.child("${Constants.CATEGORY_IMAGE_REF}$fileName")
                .putFile(coverImgUri!!).addOnSuccessListener {
                    it.storage.downloadUrl.addOnSuccessListener { imgUrl ->
                        productData["coverImg"] = imgUrl.toString()
                        productRef.updateChildren(productData).addOnSuccessListener {
                            progress.dismiss()
                            Utils.showMessage(
                                requireContext(), "Product data updated successfully"
                            )
                            findNavController().navigateUp()
                        }.addOnFailureListener {
                            progress.dismiss()
                            Utils.showMessage(requireContext(), "Something went wrong")
                        }

                    }
                }.addOnFailureListener {
                    progress.dismiss()
                    Utils.showMessage(requireContext(), "Something went wrong")

                }


        } else {
            productRef.updateChildren(productData).addOnSuccessListener {
                progress.dismiss()
                Utils.showMessage(requireContext(), "Car data updated successfully")
                findNavController().navigateUp()

            }.addOnFailureListener {
                progress.dismiss()
                Utils.showMessage(requireContext(), "Something went wrong")
            }
        }
    }

    private fun setProductData(productData: ProductModel) {
        newImageList.addAll(productData.carImages)
//        binding.ivCoverImg.load(productData?.coverImg) {
//            placeholder(R.drawable.placeholder)
//            error(R.drawable.placeholder)
//        }
//        binding.etCarTitle.setText(productData?.title)
//        binding.etBrand.setText(productData?.brand)
//        binding.etDescription.setText(productData?.description)
//        binding.etPrice.setText(productData?.price)
//        binding.etLocation.setText(productData?.location)
//        binding.etInsurence.setText(productData?.insurance)
//        binding.etRating.setText(productData?.rating)
//        binding.etRto.setText(productData?.rtoNum)
//        binding.etTag.setText(productData?.tag)
//        binding.etYear.setText(productData?.year)
//        binding.etRunKm.setText(productData?.runKm)
//        binding.etGear.setText(productData?.gearType)
//        binding.etEngine.setText(productData?.engineType)
//        binding.etCategory.setText(productData?.selectCategory)
//        binding.etSeats.setText(productData?.seating)
//        binding.etavialability.setText(productData?.available)

//        showSpinnerList(Constants.availabilityList, binding.etavialability)
//        showSpinnerList(Constants.seatsList, binding.etSeats)
//        showSpinnerList(Constants.engineList, binding.etEngine)
//        showSpinnerList(Constants.gearList, binding.etGear)
//        showSpinnerList(categoryList, binding.etCategory)
//        showSpinnerList(Constants.insurance, binding.etInsurence)

//        productUrl.submitList(newImageList)
//        binding.carImagesRV.adapter = productUrl


    }

    private fun onItemClick(productImageUrlModel: ProductImageUrlModel) {
        newImageList.remove(productImageUrlModel)
//        productUrl.submitList(newImageList)
//        binding.carImagesRV.adapter = productUrl
    }

    private fun showDeleteConfirmationDialog(productId: String) {
        val deleteDialog = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog).create()
        val dialogLayout = DialogDeleteBinding.inflate(LayoutInflater.from(requireContext()))
        deleteDialog.setView(dialogLayout.root)
        deleteDialog.setCanceledOnTouchOutside(true)
        deleteDialog.show()

        dialogLayout.btCancel.setOnClickListener {
            deleteDialog.dismiss()
        }

        dialogLayout.btDelete.setOnClickListener {
            deleteProduct(productId, deleteDialog)
        }
    }


    private fun deleteProduct(productId: String, deleteDialog: AlertDialog) {
        val carReference = database.getReference(Constants.PRODUCTS_REF).child(productId)

        carReference.removeValue().addOnSuccessListener {
            deleteDialog.dismiss()
            findNavController().navigateUp()
            Utils.showMessage(requireContext(), "Product Deleted Successfully")
        }.addOnFailureListener {
            deleteDialog.dismiss()
            Utils.showMessage(requireContext(), "Something went wrong")
        }
    }

    private fun loadCategories() {
        database.reference.child(Constants.CATEGORY_REF)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    categoryList.clear()
                    for (categorySnapshot in dataSnapshot.children) {
                        val category = categorySnapshot.getValue(CategoryModel::class.java)
                        if (category != null) {
                            categoryList.add(category.categoryName)

                        } else {
                            Log.e("Error", "Failed to convert data: $categorySnapshot")
                        }
                    }

                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Utils.showMessage(requireContext(), "Failed")
                }
            })
    }

    private fun showSpinnerList(list: List<String>, autoCompleteTextView: AutoCompleteTextView) {
        val arrayAdopter = ArrayAdapter(requireContext(), R.layout.drop_down_menu, list)
        autoCompleteTextView.setAdapter(arrayAdopter)
    }
}


