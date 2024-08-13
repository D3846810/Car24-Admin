package uk1.ac.tees.mad.d3846810.fragments

import android.app.Activity
import android.app.AlertDialog
import android.net.Uri
import android.os.Bundle
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
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import uk1.ac.tees.mad.d3846810.App
import uk1.ac.tees.mad.d3846810.R
import uk1.ac.tees.mad.d3846810.databinding.DialogDeleteBinding
import uk1.ac.tees.mad.d3846810.model.SliderModel
import uk1.ac.tees.mad.d3846810.screens.CreateSliderScreen
import uk1.ac.tees.mad.d3846810.utils.Constants
import uk1.ac.tees.mad.d3846810.utils.Utils
import java.util.UUID


class SliderFragment : Fragment() {
    //    private val binding by lazy { FragmentSliderBinding.inflate(layoutInflater) }
    private lateinit var database: FirebaseDatabase
    private lateinit var dbStorage: FirebaseStorage
    private lateinit var progress: AlertDialog
    private var sliderList = mutableStateListOf<SliderModel>()
    private var sliderImageUri: Uri? by mutableStateOf(null)

    //    private lateinit var adapter: SliderAdapter
    private lateinit var deleteDialog: AlertDialog
    private lateinit var composeView: ComposeView
    private var loadingText = mutableStateOf("Loading...")

    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val resultCode = result.resultCode
            val data = result.data

            when (resultCode) {
                Activity.RESULT_OK -> {
                    sliderImageUri = data?.data!!
//                    binding.ivSliderImage.setImageURI(sliderImageUri)

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

        database = FirebaseDatabase.getInstance()
        dbStorage = FirebaseStorage.getInstance()
        progress = Utils.showLoading(requireContext())
        deleteDialog = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog).create()

//        binding.apply {

//            sliderRv.layoutManager = LinearLayoutManager(requireContext())
//            adapter = SliderAdapter(requireContext(), this@SliderFragment)

//            ivSliderImage.setOnClickListener {
//                ImagePicker.with(this@SliderFragment)
//                    .crop()
//                    .createIntent { intent ->
//                        startForProfileImageResult.launch(intent)
//                    }
//            }
//
//            btSliderBack.setOnClickListener {
//                findNavController().navigateUp()
//            }

//            btCreateSlider.setOnClickListener {
//                validateSlider()
//            }

//        }

        fetchSliderData()

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
            CreateSliderScreen(imgUri = sliderImageUri,
                sliderList = sliderList,
                loadingText = loadingText.value,
                onBackPressed = {
                    (requireActivity().application as App).navController.navigateUp()
                },
                pickImage = {
                    ImagePicker.with(this@SliderFragment).crop().createIntent { intent ->
                        startForProfileImageResult.launch(intent)
                    }
                },
                onDelete = {
                    showDeleteDialog(it)
                }) {
                validateSlider(it)
            }
        }
    }

    private fun showToast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

    private fun validateSlider(url: String) {
//        binding.apply {
        if (sliderImageUri == null) {
            showToast("Please select Image")
        }
//            else if(etLaunchUrl.text.toString().isEmpty()) {
//                etLaunchUrl.requestFocus()
//                etLaunchUrl.error = "Empty"
//            }
        else {
            uploadSliderImage(sliderImageUri, url)
        }
//        }
    }

    private fun uploadSliderImage(sliderImage: Uri?, url: String) {
        progress.show()
        val fileName = "${UUID.randomUUID()}.jpg"
        dbStorage.reference.child("${Constants.SLIDER_IMAGE_REF}$fileName").putFile(sliderImage!!)
            .addOnSuccessListener {
                it.storage.downloadUrl.addOnSuccessListener { imgUrl ->
                    createSlider(url, imgUrl.toString())
                }
            }.addOnFailureListener {
                progress.dismiss()
                showToast("Something went wrong")
            }

    }

    private fun createSlider(link: String, sliderImgUrl: String) {
        val reference = database.getReference(Constants.SLIDER_DOCUMENT)
        val sliderId = reference.push().key
        val timestamp = System.currentTimeMillis()

        val slider = SliderModel(sliderId!!, sliderImgUrl, link, timestamp)
        reference.child(sliderId).setValue(slider).addOnSuccessListener {
//            binding.etLaunchUrl.text = null
            sliderImageUri = null
//            binding.ivSliderImage.setImageResource(R.drawable.placeholder)
            progress.dismiss()
            showToast("Slider created successfully")
        }.addOnFailureListener {
            showToast("Something went wrong")
        }


    }

    private fun fetchSliderData() {
        progress.show()
        database.getReference(Constants.SLIDER_DOCUMENT).orderByChild("timestamp")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    sliderList.clear()
                    for (childSnapshot in dataSnapshot.children.reversed()) {
                        val sliders = childSnapshot.getValue(SliderModel::class.java)
                        if (sliders != null) {
                            sliderList.add(sliders)
                        }
                    }
                    if (sliderList.isEmpty()) {
                        loadingText.value = "No Slider Created yet"
                    }

//                    if (sliderList.isNotEmpty()) {
//                        adapter.submitList(sliderList)
//                        binding.tvSliderStatus.visibility = View.GONE
//                        binding.sliderRv.visibility = View.VISIBLE
//                        binding.sliderRv.adapter = adapter
//                    } else {
//                        val status = "No Slider Created yet"
//                        binding.tvSliderStatus.visibility = View.VISIBLE
//                        binding.sliderRv.visibility = View.GONE
//                        binding.tvSliderStatus.text = status
//                    }
                    progress.dismiss()
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    showToast("Something went wrong")
                }
            })
    }

    private fun showDeleteDialog(slider: SliderModel) {
        deleteDialog = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog).create()
        val deleteLayout = DialogDeleteBinding.inflate(layoutInflater)
        deleteDialog.setView(deleteLayout.root)
        deleteDialog.setCanceledOnTouchOutside(true)
        deleteLayout.btCancel.setOnClickListener {
            deleteDialog.dismiss()
        }
        deleteLayout.btDelete.setOnClickListener {
            deleteSliderData(slider.sliderId)
        }
        deleteDialog.show()
    }

    private fun deleteSliderData(childKey: String) {
        deleteDialog.dismiss()
        progress.show()
        val sliderReference = database.getReference(Constants.SLIDER_DOCUMENT).child(childKey)
        sliderReference.removeValue().addOnSuccessListener {
            showToast("Slider deleted successfully")
        }.addOnFailureListener {
            showToast("Something went wrong")
        }.addOnCompleteListener {
            progress.dismiss()
        }
    }
}