package uk1.ac.tees.mad.d3846810.fragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.google.firebase.database.FirebaseDatabase
import uk1.ac.tees.mad.d3846810.App
import uk1.ac.tees.mad.d3846810.activities.LoginActivity
import uk1.ac.tees.mad.d3846810.model.LoginModel
import uk1.ac.tees.mad.d3846810.screens.ManageLoginScreen
import uk1.ac.tees.mad.d3846810.utils.Constants
import uk1.ac.tees.mad.d3846810.utils.SharedPref
import uk1.ac.tees.mad.d3846810.utils.Utils


class ManageLoginFragment : Fragment() {
    //    private val binding by lazy { FragmentManageLoginBinding.inflate(layoutInflater) }
    private lateinit var progress: AlertDialog
    private lateinit var database: FirebaseDatabase
    private lateinit var composeView: ComposeView

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
        progress = Utils.showLoading(requireContext())

//        binding.apply {
//            btBack.setOnClickListener {
//                findNavController().navigateUp()
//            }
//
//            btLogout.setOnClickListener {
//                SharedPref.clearData(requireContext())
//                Utils.showMessage(requireContext(), "Logout Successfully")
//                startActivity(Intent(requireActivity(), LoginActivity::class.java))
//            }

//            binding.btSave.setOnClickListener {
//                if (binding.etEmail.text.toString().isEmpty()) {
//                    binding.etEmail.error = "Empty"
//                } else if (binding.etPassword.text.toString().isEmpty()) {
//                    binding.etPassword.error = "Empty"
//                } else if (binding.etPassword.text.toString() != binding.etConfirmPassword.text.toString()) {
//                    Utils.showMessage(requireContext(), "Password Not Match")
//                } else {
//                    createLogin()
//                }
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
            ManageLoginScreen(onBackPressed = {
                (requireActivity().application as App).navController.navigateUp()
            }, onSave = { email, pass, confirmPass ->
                if (pass != confirmPass) {
                    Utils.showMessage(requireContext(), "Password Not Match")
                } else {
                    createLogin(email, pass)
                }
            }, onLogout = {
                SharedPref.clearData(requireContext())
                Utils.showMessage(requireContext(), "Logout Successfully")
                startActivity(Intent(requireActivity(), LoginActivity::class.java))
                requireActivity().finish()
            })
        }
    }

    private fun createLogin(email: String, pass: String) {
        progress.show()

        val login = LoginModel(
            Constants.LOGIN_REF, email, pass
        )

        // Set a new value under "Login" node in Realtime Database
        database.reference.child("Login").setValue(login).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                progress.dismiss()
                Utils.showMessage(requireContext(), "Login Updated")
            } else {
                progress.dismiss()
                Utils.showMessage(requireContext(), "Something went wrong")
            }
        }
    }

}