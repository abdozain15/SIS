package com.sedra.sis.view.profile

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.loader.content.CursorLoader
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.sedra.sis.R
import com.sedra.sis.data.model.User
import com.sedra.sis.databinding.DialogComplaintsBinding
import com.sedra.sis.databinding.DialogEditProfileBinding
import com.sedra.sis.databinding.FragmentProfileBinding
import com.sedra.sis.util.GoTo
import com.sedra.sis.util.PREF_PARENT_USER
import com.sedra.sis.util.Status
import com.sedra.sis.util.getUserFromString
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {

    var binding: FragmentProfileBinding? = null

    @Inject
    lateinit var preferences: SharedPreferences
    var currentUser: User? = null
    val viewModel: ProfileViewModel by viewModels()
    private var resultLauncher: ActivityResultLauncher<Intent>? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfileBinding.bind(view)
        currentUser = getUserFromString(preferences.getString(PREF_PARENT_USER, "") ?: "")
        setupUI()
    }

    private fun setupUI() {
        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    // There are no request codes
                    val data: Intent? = result.data
                    updateImage(data)

                }
            }
        binding?.apply {
            editProfile.setOnClickListener {
                openEditDialog()
            }
            complaints.setOnClickListener {
                openComplaintsDialog()
            }
            name.text = currentUser?.name
            logout.setOnClickListener {
                logoutOfApp()
            }
            userImage.setOnClickListener {
                pickImage()
            }
        }
    }

    private fun updateImage(data: Intent?) {
        val uri = data?.data!!
        viewModel.updateImage(
            "Bearer ${currentUser?.api_token}",
            currentUser?.id ?: 0,
            getRealPathFromURI(uri)
        ).observe(viewLifecycleOwner) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let { response ->
                            Toast.makeText(context, response.msg, Toast.LENGTH_SHORT).show()
                            if (response.status) {
                            }
                        }
                    }
                    Status.ERROR -> {
                        Toast.makeText(context, resource.message, Toast.LENGTH_SHORT).show()
                    }
                    Status.LOADING -> {
                        Toast.makeText(context, "Loading", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }

    private fun pickImage() {
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        resultLauncher?.launch(gallery)
    }

    private fun logoutOfApp() {
        val editor = preferences.edit()
        editor.clear()
        editor.apply()
        GoTo.startingActivity(requireContext())
    }

    private fun openEditDialog() {
        val binding: DialogEditProfileBinding = DataBindingUtil.inflate(
            LayoutInflater.from(
                context
            ), R.layout.dialog_edit_profile, null, false
        )
        val dialog = BottomSheetDialog(requireContext()).apply {
            setContentView(binding.root)
            setCancelable(true)
            setCanceledOnTouchOutside(true)
        }
        binding.apply {
            user = currentUser
            saveData.setOnClickListener {
                saveUserData(
                    registerName.text.toString(),
                    registerAge.text.toString(),
                    registerHeight.text.toString(),
                    registerWeight.text.toString(),
                    "female"
                )
                dialog.dismiss()
            }
        }
        dialog.show()
    }


    private fun openComplaintsDialog() {
        val binding: DialogComplaintsBinding = DataBindingUtil.inflate(
            LayoutInflater.from(
                context
            ), R.layout.dialog_complaints, null, false
        )
        val dialog = Dialog(requireContext()).apply {
            setContentView(binding.root)
            setCancelable(true)
            setCanceledOnTouchOutside(true)
        }
        binding.apply {
            sendOpinion.setOnClickListener {
                viewModel.sendOpinion(
                    "Bearer ${currentUser?.api_token}",
                    currentUser!!.id,
                    opinionText.text.toString()
                ).observe(viewLifecycleOwner) {
                    dialog.dismiss()
                }
            }
        }
        dialog.show()
        val window = dialog.window
        window?.setLayout(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        )

    }

    private fun saveUserData(
        name: String,
        age: String,
        height: String,
        weight: String,
        gender: String
    ) {
        viewModel.editProfile(
            "Bearer ${currentUser?.api_token}",
            currentUser?.id ?: 0,
            name,
            gender,
            age.toInt(),
            height.toInt(),
            weight.toInt()
        ).observe(viewLifecycleOwner) {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let { response ->
                            Toast.makeText(context, response.msg, Toast.LENGTH_SHORT).show()
                            if (response.status) {
                                currentUser?.apply {
                                    this.name = name
                                    this.gender = gender
                                    this.age = age.toInt()
                                    this.height = height.toInt()
                                    this.weight = weight.toInt()
                                }
                                saveUser(currentUser)
                            }
                        }
                    }
                    Status.ERROR -> {
                        Toast.makeText(context, resource.message, Toast.LENGTH_SHORT).show()
                    }
                    Status.LOADING -> {
                        Toast.makeText(context, "Loading", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun saveUser(data: User?) {
        val gson = Gson()
        val hashMapString = gson.toJson(data!!)
        val editor = preferences.edit()
        editor.putString(PREF_PARENT_USER, hashMapString)
        editor.apply()
        binding?.name?.text = currentUser?.name
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }


    private fun getRealPathFromURI(contentUri: Uri): String {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val loader = CursorLoader(requireContext(), contentUri, proj, null, null, null)
        val cursor: Cursor = loader.loadInBackground()!!
        val column_index: Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        val result: String = cursor.getString(column_index)
        cursor.close()
        return result
    }
}
