package com.tuhoc.dreamtunes.ui.edit_profile

import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.github.dhaval2404.imagepicker.ImagePicker
import com.tuhoc.dreamtunes.bases.BaseActivity
import com.tuhoc.dreamtunes.databinding.ActivityEditProfileBinding
import com.tuhoc.dreamtunes.manager.LoginManager

class EditProfileActivity : BaseActivity<ActivityEditProfileBinding>(ActivityEditProfileBinding::inflate) {

    private lateinit var editProfileViewModel: EditProfileViewModel
    private var selectedImageUri: Uri? = null

    override fun observerData() {
        super.observerData()
        editProfileViewModel = ViewModelProvider(this)[EditProfileViewModel::class.java]
    }

    override fun handleEvent() {
        super.handleEvent()

        val name = binding.edtName.text
        val newPass = binding.edtNewPassword.text
        val oldPass = binding.edtOldPassword.text

        val user = LoginManager.getCurrentUser(this)

        binding.btnCamera.setOnClickListener {
            ImagePicker.with(this)
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .start()
        }

        binding.btnSave.setOnClickListener {
            if (selectedImageUri != null) {
                editProfileViewModel.editPhoto(user?.userId!!.toInt(), selectedImageUri!!) { message, check ->
                    if (check) {
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    }
                }
            }

            if (name.isNotEmpty()) {
                editProfileViewModel.editUserName(user?.userId!!.toInt(), name.toString()) { message, check ->
                    if (check) {
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    }
                }
            }

            if (newPass.isNotEmpty() || oldPass.isNotEmpty()) {
                if (checkPasswordField()) {
                    editProfileViewModel.editPassword(user?.userId!!.toInt(), oldPass.toString(), newPass.toString()) { message, check ->
                        if (check) {
                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        binding.btnExit.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            val uri: Uri = data?.data!!
            selectedImageUri = uri
            binding.imgImage.setImageURI(uri)
        }
    }

    private fun checkPasswordField(): Boolean {
        val newPass = binding.edtNewPassword.text.toString()
        val oldPass = binding.edtOldPassword.text.toString()
        val confirm = binding.edtConfirm.text.toString()

        if (newPass.length < 6 || oldPass.length < 6) {
            Toast.makeText(this, "Please enter the correct format", Toast.LENGTH_SHORT).show()
            return false
        }

        if (newPass != confirm) {
            Toast.makeText(this, "Reconfirm", Toast.LENGTH_SHORT).show()
            return false
        }

        if (newPass == oldPass) {
            Toast.makeText(this, "Matched the old password", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
}