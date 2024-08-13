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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import uk1.ac.tees.mad.d3846810.App
import uk1.ac.tees.mad.d3846810.R
import uk1.ac.tees.mad.d3846810.model.CategoryModel
import uk1.ac.tees.mad.d3846810.model.ProductImageModel
import uk1.ac.tees.mad.d3846810.model.ProductImageUrlModel
import uk1.ac.tees.mad.d3846810.model.ProductModel
import uk1.ac.tees.mad.d3846810.screens.AddProductScreen
import uk1.ac.tees.mad.d3846810.utils.Constants
import uk1.ac.tees.mad.d3846810.utils.Utils
import java.util.UUID

//edit products
class AddProductFragment : Fragment() {
    private lateinit var composeView: ComposeView

    //    private val binding by lazy { FragmentAddProductBinding.inflate(layoutInflater) }
    private val categoryList = mutableListOf<String>()
    private var imgUri: Uri? by mutableStateOf(null)
    private var coverImgUrl by mutableStateOf("")
    private val imgUriList = mutableListOf<ProductImageModel>()
    private val imgUrlList = mutableListOf<ProductImageUrlModel>()

    //    private lateinit var imgListAdapter: ProductImageAdapter
    private lateinit var dbStorage: FirebaseStorage
    private lateinit var database: FirebaseDatabase
    private lateinit var progressDialog: AlertDialog

    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val resultCode = result.resultCode
            val data = result.data

            when (resultCode) {
                Activity.RESULT_OK -> {
                    imgUri = data?.data!!
                    productModel.coverImg = imgUri.toString()
                    setComposeContent()
//                    binding.ivCoverImg.setImageURI(imgUri)
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

    private val startForProfileImageResultList =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val resultCode = result.resultCode
            val data = result.data

            when (resultCode) {
                Activity.RESULT_OK -> {
                    val imageUri = ProductImageModel(
                        UUID.randomUUID().toString(), data?.data!!
                    )
                    imgUriList.add(imageUri)
                    imgUrlList.add(
                        ProductImageUrlModel(
                            imageUri.imageId, imageUri.imageUri.toString()
                        )
                    )
                    productModel.carImages = imgUrlList
//                    binding.productImagesRv.visibility = View.VISIBLE
//                    imgListAdapter.submitList(imgUriList)
//                    binding.productImagesRv.adapter = imgListAdapter

                    setComposeContent()

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

//        return binding.root
        return ComposeView(requireContext()).also {
            composeView = it
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setComposeContent()

        coverImgUrl = ""
//        imgListAdapter = ProductImageAdapter(this@AddProductFragment)
        progressDialog = Utils.showLoading(requireContext())
        dbStorage = FirebaseStorage.getInstance()
        database = FirebaseDatabase.getInstance()


        loadCategories()

//        binding.apply {

//            showSpinnerList(Constants.availabilityList, etavialability)
//            showSpinnerList(Constants.seatsList, etSeats)
//            showSpinnerList(Constants.engineList, etEngine)
//            showSpinnerList(Constants.gearList, etGear)
//            showSpinnerList(Constants.insurance, etInsurence)

//            productImagesRv.adapter = imgListAdapter

//            ivCoverImg.setOnClickListener {
//                ImagePicker.with(this@AddProductFragment)
//                    .crop()
//                    .createIntent { intent ->
//                        startForProfileImageResult.launch(intent)
//                    }
//            }

//            btAddImg.setOnClickListener {
//                ImagePicker.with(this@AddProductFragment)
//                    .crop()
//                    .createIntent { intent ->
//                        startForProfileImageResultList.launch(intent)
//                    }
//            }

//            btNewCar.setOnClickListener {
//                val coverImg = imgUri
//                val title = binding.etCarTitle
//                val year = binding.etYear
//                val rating = binding.etRating
//                val description = binding.etDescription
//                val price = binding.etPrice
//                val brand = binding.etBrand
//                val runKm = binding.etRunKm
//                val insurance = binding.etInsurence
//                val rtoNum = binding.etRto
//                val location = binding.etLocation
//                val engineType = binding.etEngine
//                val gearType = binding.etGear
//                val category = binding.etCategory
//                val seat = binding.etSeats
//                val available = binding.etavialability
//                val carsImgList = imgUriList
//
//                validateCarData(
//                    coverImg, title, year, rating, description, price, brand, runKm,
//                    insurance, rtoNum, location, engineType, gearType, category, seat, available, carsImgList
//                )
//            }

//            btBack.setOnClickListener {
//                findNavController().navigateUp()
//            }

//        }

//        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
//            object : OnBackPressedCallback(true) {
//                override fun handleOnBackPressed() {
//                    findNavController().navigateUp()
//                }
//
//            })

    }

    private var productModel by mutableStateOf(createProductModel())

    private fun createProductModel(): ProductModel {
        return ProductModel(
            "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", arrayListOf()
        )
    }

    private fun setComposeContent() {
        composeView.setContent {
            AddProductScreen(productModel = productModel,
                categoryList = categoryList,
                onBackPressed = { (requireActivity().application as App).navController.navigateUp() },
                onPickImage = {
                    ImagePicker.with(this@AddProductFragment).crop().createIntent { intent ->
                        startForProfileImageResult.launch(intent)
                    }
                },
                addProductImages = {
                    ImagePicker.with(this@AddProductFragment).crop().createIntent { intent ->
                        startForProfileImageResultList.launch(intent)
                    }
                },
                addProduct = {
                    productModel = it
                    validateCarData()
                },
                onImageDelete = {

                })
        }
    }

    private fun validateCarData(
    ) {
        if (imgUri == null) {
            Utils.showMessage(requireContext(), "Please Select Cover Image")
        } else if (imgUriList.isEmpty()) {
            Utils.showMessage(requireContext(), "Please add One Car Image")
        } else {
            progressDialog.show()
            uploadCoverImg(productModel.title, imgUri)
        }
    }

    private fun uploadCoverImg(title: String, coverImg: Uri?) {
        coverImg?.let { imgUri ->
            val fileName = "${title}_${UUID.randomUUID()}.jpg"
            dbStorage.reference.child("${Constants.PRODUCT_IMAGE_REF}/$fileName").putFile(imgUri)
                .addOnSuccessListener {
                    it.storage.downloadUrl.addOnSuccessListener { imgUrl ->
                        coverImgUrl = imgUrl.toString()
                        uploadCarImg(title)
                    }

                }.addOnFailureListener {
                    Utils.showMessage(requireContext(), "Something went wrong")

                }
        }
    }

    private var carImagesIndex = 0
    private fun uploadCarImg(title: String) {
        val fileName = "${title}_${UUID.randomUUID()}.jpg"
        if (carImagesIndex < imgUriList.size) {
            imgUriList[carImagesIndex].imageUri?.let { imgUri ->
                dbStorage.reference.child(Constants.PRODUCT_IMAGE_REF + fileName).putFile(imgUri)
                    .addOnSuccessListener {
                        it.storage.downloadUrl.addOnSuccessListener { img ->
                            val productImage = ProductImageUrlModel(
                                UUID.randomUUID().toString(), img.toString()
                            )
                            imgUrlList.add(productImage)
                            if (imgUrlList.size == imgUriList.size) {
                                createCar()
                            } else {
                                carImagesIndex++
                                uploadCarImg(title)
                            }
                        }
                    }
            }
        } else {
            createCar()
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
//                showSpinnerList(categoryList, binding.etCategory)

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

    private fun createCar() {
        val productId = database.getReference(Constants.PRODUCTS_REF).push().key
        productModel.productId = productId!!
        productModel.coverImg = coverImgUrl
//        productModel = ProductModel(
//            productId!!,
//            coverImgUrl,
//            binding.etCarTitle.text.toString(),
//            binding.etYear.text.toString(),
//            binding.etRating.text.toString(),
//            binding.etDescription.text.toString(),
//            binding.etPrice.text.toString(),
//            binding.etLocation.text.toString(),
//            binding.etBrand.text.toString(),
//            binding.etInsurence.text.toString(),
//            binding.etRunKm.text.toString(),
//            binding.etEngine.text.toString(),
//            binding.etGear.text.toString(),
//            binding.etRto.text.toString(),
//            binding.etCategory.text.toString(),
//            binding.etSeats.text.toString(),
//            binding.etavialability.text.toString(),
//            binding.etTag.text.toString(),
//            imgUrlList
//        )

        val carReference = database.getReference(Constants.PRODUCTS_REF).child(productId)
        carReference.setValue(productModel).addOnSuccessListener {
            clearFields()
            progressDialog.dismiss()
            Utils.showMessage(requireContext(), "Car Created successfully")

        }.addOnFailureListener {
            Utils.showMessage(requireContext(), "Something went wrong")
        }
    }

    private fun clearFields() {
        productModel = createProductModel()
//        binding.etCarTitle.text = null
//        binding.etYear.text = null
//        binding.etRating.text = null
//        binding.etDescription.text = null
//        binding.etPrice.text = null
//        binding.etBrand.text = null
//        binding.etRunKm.text = null
//        binding.etInsurence.text = null
//        binding.etRto.text = null
//        binding.etLocation.text = null
//        binding.etGear.text = null
//        binding.etGear.text = null
//        binding.etCategory.text = null
//        binding.etSeats.text = null
//        binding.etavialability.text = null
//        binding.etTag.text = null
        imgUri = null
        imgUriList.clear()
        imgUrlList.clear()
//        imgListAdapter.submitList(imgUriList)
//        binding.productImagesRv.adapter = imgListAdapter
//        binding.ivCoverImg.setImageResource(R.drawable.placeholder)
    }

    fun onItemClick(productImageModel: ProductImageModel) {
        imgUriList.remove(productImageModel)
        productModel = productModel.copy(carImages = imgUrlList)
        //        imgListAdapter.submitList(imgUriList)
//        binding.productImagesRv.adapter = imgListAdapter
    }

}