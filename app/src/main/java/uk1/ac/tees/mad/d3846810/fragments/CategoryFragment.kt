package uk1.ac.tees.mad.d3846810.fragments

import android.app.Activity
import android.app.AlertDialog
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import coil.load
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import uk1.ac.tees.mad.d3846810.App
import uk1.ac.tees.mad.d3846810.R
import uk1.ac.tees.mad.d3846810.databinding.DialogDeleteBinding
import uk1.ac.tees.mad.d3846810.databinding.DialogEditCategoryBinding
import uk1.ac.tees.mad.d3846810.model.CategoryModel
import uk1.ac.tees.mad.d3846810.screens.CategoryScreen
import uk1.ac.tees.mad.d3846810.utils.Constants
import uk1.ac.tees.mad.d3846810.utils.Utils
import java.util.UUID

//category manage
class CategoryFragment : Fragment() {
    //    private val binding by lazy { FragmentCategoryBinding.inflate(layoutInflater) }
    private var coverImgUri: Uri? by mutableStateOf(null)
    private var coverImgUrl: String? by mutableStateOf("")
    private lateinit var dbStorage: FirebaseStorage
    private lateinit var database: FirebaseDatabase
    private lateinit var progressDialog: AlertDialog
    private val categoryList = mutableStateListOf<CategoryModel>()

    //    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var editDialogLayout: DialogEditCategoryBinding
    private lateinit var editCategoryDialog: AlertDialog

    private lateinit var composeView: ComposeView
    private var loadingText by mutableStateOf("Loading...")

    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val resultCode = result.resultCode
            val data = result.data

            when (resultCode) {
                Activity.RESULT_OK -> {
                    coverImgUri = data?.data!!
                    setComposeContent()
                    editDialogLayout.ivCurrentCategory.setImageURI(coverImgUri)
//                    binding.ivCategoryImg.setImageURI(coverImgUri)

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

        return ComposeView(requireContext()).also {
            composeView = it
        }
    }

    private fun setComposeContent() {
        composeView.setContent {
            CategoryScreen(
                imgUri = coverImgUri,
                categoryList = categoryList,
                loadingText = loadingText,
                onBackPressed = {
                    onBack()
                },
                validateCategory = { validateCategory(it) },
                pickImage = {
                    ImagePicker.with(this@CategoryFragment).cropSquare().createIntent { intent ->
                        startForProfileImageResult.launch(intent)
                    }
                },
                onItemEditClick = { onItemClick(it) })
        }
    }

    private fun onBack() {
        (requireActivity().application as App).navController.navigateUp()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setComposeContent()

        database = FirebaseDatabase.getInstance()
        progressDialog = Utils.showLoading(requireContext())
        dbStorage = FirebaseStorage.getInstance()
        editCategoryDialog =
            AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog).create()
        editDialogLayout = DialogEditCategoryBinding.inflate(LayoutInflater.from(requireContext()))

        loadCategories()

//        binding.apply {

//            rvCategory.layoutManager = LinearLayoutManager(requireContext())
//            categoryAdapter = CategoryAdapter(this@CategoryFragment)
//            rvCategory.adapter = categoryAdapter

//            btCategoryBack.setOnClickListener {
//                findNavController().navigateUp()
//            }

//            btCreateCategory.setOnClickListener {
//                validateCategory()
//            }

//            ivCategoryImg.setOnClickListener {
//                ImagePicker.with(this@CategoryFragment)
//                    .cropSquare()
//                    .createIntent { intent ->
//                        startForProfileImageResult.launch(intent)
//                    }
//            }


//        requireActivity().onBackPressedDispatcher.addCallback(
//            viewLifecycleOwner,
//            object : OnBackPressedCallback(true) {
//                override fun handleOnBackPressed() {
//                    findNavController().navigateUp()
//                }
//
//            })
    }

    private fun validateCategory(categoryName: String) {
        if (categoryName.isEmpty()) {
//        binding.etCategoryName.requestFocus()
//        binding.etCategoryName.error = "Empty"
        } else if (coverImgUri == null) {
            Utils.showMessage(requireContext(), "Please Select Image")
        } else {
            progressDialog.show()
            uploadCategoryImg(coverImgUri, categoryName)
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
                            categoryList.add(category)
                        } else {
                            Log.e("Error", "Failed to convert data: $categorySnapshot")
                        }
                    }

                    if (categoryList.isEmpty()) {
                        loadingText = "No Categories Found"
                    }

//                if(categoryList.isNotEmpty()) {
//                    binding.rvCategory.visibility = View.VISIBLE
//                    binding.tvStatus.visibility = View.GONE
//                    categoryAdapter.submitList(categoryList)
//                    binding.rvCategory.adapter = categoryAdapter
//                } else {
//                    binding.rvCategory.visibility = View.GONE
//                    binding.tvStatus.visibility = View.VISIBLE
//                    val status = "No Categories Found"
//                    binding.tvStatus.text = status
//                }

                }


                override fun onCancelled(databaseError: DatabaseError) {
                    Utils.showMessage(requireContext(), "Failed")
                }
            })
    }

    private fun uploadCategoryImg(coverImgUri: Uri?, name: String) {
        val fileName = "${name}_${UUID.randomUUID()}.jpg"
        dbStorage.reference.child("${Constants.CATEGORY_IMAGE_REF}$fileName").putFile(coverImgUri!!)
            .addOnSuccessListener {
                it.storage.downloadUrl.addOnSuccessListener { imgUrl ->
                    coverImgUrl = imgUrl.toString()
                    createCategory(name, coverImgUrl)
                }
            }.addOnFailureListener {
                progressDialog.dismiss()
                Utils.showMessage(requireContext(), "Something went wrong")

            }

    }

    private fun createCategory(name: String, coverImgUrl: String?) {
        val categoryId = database.reference.child(Constants.CATEGORY_REF).push().key
        categoryId?.let { id ->

            val category = CategoryModel(id, coverImgUrl!!, name)

            database.reference.child(Constants.CATEGORY_REF).child(id).setValue(category)
                .addOnSuccessListener {
//                    binding.etCategoryName.clearFocus()
                    progressDialog.dismiss()
//                    binding.etCategoryName.text = null
//                    binding.ivCategoryImg.setImageResource(R.drawable.placeholder)
                    coverImgUri = null
                    Utils.showMessage(requireContext(), "Category Created Successfully")

                }.addOnFailureListener { exception ->
                    progressDialog.dismiss()
                    Utils.showMessage(
                        requireContext(), exception.localizedMessage ?: "Something went wrong"
                    )

                }
        } ?: run {
            progressDialog.dismiss()
            Utils.showMessage(requireContext(), "Failed to create category")

        }
    }

    private fun onItemClick(categoryModel: CategoryModel) {
        editCategoryDialog =
            AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog).create()
        editDialogLayout = DialogEditCategoryBinding.inflate(LayoutInflater.from(requireContext()))
        editCategoryDialog.setView(editDialogLayout.root)
        editDialogLayout.ivCurrentCategory.load(categoryModel.imgUrl)
        editDialogLayout.etCategoryNamez.setText(categoryModel.categoryName)
        editCategoryDialog.setCanceledOnTouchOutside(true)
        editCategoryDialog.show()
        editDialogLayout.btDeleteCategory.setOnClickListener {
            showDeleteConfirmationDialog(categoryModel.categoryId)
        }
        editDialogLayout.btCancel.setOnClickListener {
            editCategoryDialog.dismiss()
        }
        editDialogLayout.btCategoryUpdate.setOnClickListener {
            val newName = editDialogLayout.etCategoryNamez.text.toString()
            if (newName.isNotEmpty()) {
                if (coverImgUri != null) {
                    editCategoryDialog.dismiss()
                    uploadCategoryImageAndName(
                        coverImgUri,
                        categoryModel.categoryId,
                        editDialogLayout.etCategoryNamez.text.toString()
                    )
                } else {
                    editCategoryDialog.dismiss()
                    updateCategoryName(categoryModel.categoryId, newName)
                }
                editCategoryDialog.dismiss()
            }
        }
        editDialogLayout.ivCurrentCategory.setOnClickListener {
            ImagePicker.with(this).cropSquare().createIntent { intent ->
                startForProfileImageResult.launch(intent)
            }
        }
    }

    private fun uploadCategoryImageAndName(
        coverImgUri: Uri?, categoryId: String, name: String
    ) {
        progressDialog.show()
        val fileName = "${name}_${UUID.randomUUID()}.jpg"
        val storageRef = dbStorage.reference.child("${Constants.CATEGORY_IMAGE_REF}$fileName")
        coverImgUri?.let { uri ->
            storageRef.putFile(uri).addOnSuccessListener { _ ->
                storageRef.downloadUrl.addOnSuccessListener { imgUrl ->
                    val newImageUrl = imgUrl.toString()
                    val update = mapOf<String, Any>(
                        "categoryName" to name, "imgUrl" to newImageUrl
                    )
                    database.reference.child(Constants.CATEGORY_REF).child(categoryId)
                        .updateChildren(update).addOnSuccessListener {
                            progressDialog.dismiss()
//                            binding.etCategoryName.clearFocus()
//                            binding.etCategoryName.text = null
//                            binding.ivCategoryImg.setImageResource(R.drawable.placeholder)
                            Utils.showMessage(requireContext(), "Category Created Successfully")
                        }.addOnFailureListener {
                            progressDialog.dismiss()
                            Utils.showMessage(requireContext(), "Something went wrong")
                        }
                }
            }.addOnFailureListener {
                progressDialog.dismiss()
                Utils.showMessage(requireContext(), "Something went wrong")
            }
        }
    }

    private fun updateCategoryName(categoryId: String, categoryName: String) {
        progressDialog.show()
        val newName = mapOf<String, Any>("categoryName" to categoryName)
        database.reference.child(Constants.CATEGORY_REF).child(categoryId).updateChildren(newName)
            .addOnSuccessListener {
                progressDialog.dismiss()
                Utils.showMessage(requireContext(), "Category Name Updated Successfully")
                editCategoryDialog.dismiss()
            }.addOnFailureListener {
                progressDialog.dismiss()
                Utils.showMessage(requireContext(), "Something went wrong")
            }
    }

    private fun showDeleteConfirmationDialog(categoryId: String) {
        editCategoryDialog.dismiss()
        val deleteDialog = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog).create()
        val dialogLayout = DialogDeleteBinding.inflate(LayoutInflater.from(requireContext()))
        deleteDialog.setView(dialogLayout.root)
        deleteDialog.setCanceledOnTouchOutside(true)
        deleteDialog.show()
        dialogLayout.btCancel.setOnClickListener {
            deleteDialog.dismiss()
        }
        dialogLayout.btDelete.setOnClickListener {
            deleteCategory(categoryId, deleteDialog)
        }
    }

    private fun deleteCategory(categoryId: String, deleteDialog: AlertDialog) {
        val categoryReference = database.reference.child(Constants.CATEGORY_REF).child(categoryId)
        categoryReference.removeValue().addOnSuccessListener {
            deleteDialog.dismiss()
            Utils.showMessage(requireContext(), "Category Deleted Successfully")
        }.addOnFailureListener {
            deleteDialog.dismiss()
            Utils.showMessage(requireContext(), "Something went wrong")
        }
    }
}