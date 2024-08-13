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
import uk1.ac.tees.mad.d3846810.model.NotificationModel
import uk1.ac.tees.mad.d3846810.screens.CreateNotificationScreen
import uk1.ac.tees.mad.d3846810.utils.Constants
import uk1.ac.tees.mad.d3846810.utils.Utils
import java.util.UUID


class NotificationFragment : Fragment() {
    //    private val binding by lazy { FragmentNotificationBinding.inflate(layoutInflater) }
    private lateinit var database: FirebaseDatabase

    private lateinit var dbStorage: FirebaseStorage
    private var notificationImgUri: Uri? by mutableStateOf(null)
    private var notificationCoverImg: String? by mutableStateOf(null)
    private lateinit var progressDialog: AlertDialog

    //    private lateinit var notificationAdapter: NotificationAdapter
    private lateinit var composeView: ComposeView
    private val loadingText = mutableStateOf("Loading...")
    private val notificationList = mutableStateListOf<NotificationModel>()
    private var title = ""
    private var description = ""
    private var notificationUrl = ""


    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val resultCode = result.resultCode
            val data = result.data

            when (resultCode) {
                Activity.RESULT_OK -> {
                    notificationImgUri = data?.data!!
//                    binding.ivNotificationImg.setImageURI(notificationImgUri)
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
        progressDialog = Utils.showLoading(requireContext())
//        notificationAdapter = NotificationAdapter(this@NotificationFragment)

        getAvailableNotifications()

//        binding.apply {
//            btBack.setOnClickListener {
//                findNavController().navigateUp()
//            }

//            btCreateNotification.setOnClickListener {
//                validateNotification()
//            }

//            ivNotificationImg.setOnClickListener {
//                ImagePicker.with(this@NotificationFragment).crop().createIntent { intent ->
//                        startForProfileImageResult.launch(intent)
//                    }
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

    private fun setComposeContent() {
        composeView.setContent {
            CreateNotificationScreen(imgUri = notificationImgUri,
                notificationList = notificationList,
                loadingText = loadingText.value,
                onBackPressed = {
                    (requireActivity().application as App).navController.navigateUp()
                },
                pickImage = {
                    ImagePicker.with(this@NotificationFragment).crop().createIntent { intent ->
                        startForProfileImageResult.launch(intent)
                    }
                },
                onDelete = { onItemClick(it) }) { title, description, url ->
                this.title = title
                this.description = description
                this.notificationUrl = url
                validateNotification()
            }
        }
    }

    private fun getAvailableNotifications() {
        database.getReference(Constants.NOTIFICATION_REF)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    notificationList.clear()
                    for (snapshot in dataSnapshot.children) {
                        val notification = snapshot.getValue(NotificationModel::class.java)
                        notification?.let {
                            notificationList.add(it)
                        }
                    }

                    if (notificationList.isEmpty()) {
                        loadingText.value = "No Notifications"
                    }
//                    if (notificationList.isNotEmpty()) {
//                        binding.tvStatus.visibility = View.GONE
//                        binding.notificationRv.visibility = View.VISIBLE
//                        notificationAdapter.submitList(notificationList)
//                        binding.notificationRv.adapter = notificationAdapter
//                    } else {
//                        binding.tvStatus.visibility = View.VISIBLE
//                        binding.notificationRv.visibility = View.GONE
//                        val status = "No Notifications"
//                        binding.tvStatus.text = status
//                    }

                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle onCancelled
                }
            })
    }

    private fun validateNotification() {
//        val title = binding.etTitle.text.toString()
//        val description = binding.etDescription.text.toString()

//        if(title.isEmpty()) {
//            binding.etTitle.requestFocus()
//            binding.etTitle.error = "Empty"
//        } else if(description.isEmpty()) {
//            binding.etDescription.requestFocus()
//            binding.etDescription.error = "Empty"
//        } else
        if (notificationImgUri != null) {
            progressDialog.show()
            uploadNotificationImg(title)
        } else {
            uploadNotification("")
        }
    }

    private fun uploadNotificationImg(title: String) {
        val fileName = "$title-${UUID.randomUUID()}.jpg"
        dbStorage.reference.child(Constants.NOTIFICATION_IMAGE_REF + fileName)
            .putFile(notificationImgUri!!).addOnSuccessListener {
                it.storage.downloadUrl.addOnSuccessListener { nUrl ->
                    notificationCoverImg = nUrl.toString()
                    uploadNotification(notificationCoverImg)
                }
            }
    }

    private fun uploadNotification(notificationImgUrl: String?) {
        val notificationId = database.getReference(Constants.PRODUCTS_REF).push().key

        val notification = NotificationModel(
            notificationId!!, notificationImgUrl.toString(), title, description, notificationUrl
//            binding.etTitle.text.toString(),
//            binding.etDescription.text.toString(),
//            binding.etUrl.text.toString()
        )

        database.getReference(Constants.NOTIFICATION_REF).child(notificationId)
            .setValue(notification).addOnSuccessListener {
//                binding.ivNotificationImg.setImageResource(R.drawable.placeholder)
                notificationImgUri = null
//                binding.etTitle.text = null
//                binding.etDescription.text = null
//                binding.etUrl.text = null
                progressDialog.dismiss()
                Utils.showMessage(requireContext(), "Notification created")
            }.addOnFailureListener {
                Utils.showMessage(requireContext(), "Something went wrong")
            }
    }

    private fun onItemClick(notificationModel: NotificationModel) {
        val deleteDialog = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog).create()
        val dialogLayout = DialogDeleteBinding.inflate(LayoutInflater.from(requireContext()))
        deleteDialog.setView(dialogLayout.root)
        deleteDialog.setCanceledOnTouchOutside(true)
        deleteDialog.show()

        dialogLayout.btCancel.setOnClickListener {
            deleteDialog.dismiss()
        }

        dialogLayout.btDelete.setOnClickListener {
            deleteCategory(notificationModel.notificationId, deleteDialog)
        }
    }

    private fun deleteCategory(productId: String, deleteDialog: AlertDialog) {
        val ref = database.reference.child(Constants.NOTIFICATION_REF).child(productId)
        ref.removeValue().addOnSuccessListener {
            deleteDialog.dismiss()
            Utils.showMessage(requireContext(), "Deleted Successfully")
        }.addOnFailureListener {
            deleteDialog.dismiss()
            Utils.showMessage(requireContext(), "Something went wrong")

        }
    }
}

