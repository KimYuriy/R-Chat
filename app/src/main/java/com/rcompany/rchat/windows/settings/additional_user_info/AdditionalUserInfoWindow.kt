package com.rcompany.rchat.windows.settings.additional_user_info

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import com.rcompany.rchat.databinding.AdditionalUserInfoWindowBinding
import com.rcompany.rchat.utils.databases.user.UserDB
import com.rcompany.rchat.utils.databases.user.UserRepo
import com.rcompany.rchat.windows.settings.additional_user_info.viewmodel.AdditionalUserInfoViewModel
import com.rcompany.rchat.windows.settings.additional_user_info.viewmodel.AdditionalUserInfoViewModelFactory

class AdditionalUserInfoWindow : AppCompatActivity() {
    private lateinit var b: AdditionalUserInfoWindowBinding
    private lateinit var vm: AdditionalUserInfoViewModel
    private var avatar: String? = null

    private val cropImage = registerForActivityResult(CropImageContract()) {
        if (it.isSuccessful) {
            b.ivAvatar.setImageURI(it.uriContent)
        } else {
            AlertDialog.Builder(this).apply {
                setTitle("Ошибка!")
                setMessage("Произошла ошибка загрузки изображения")
                setCancelable(true)
            }.create().show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = AdditionalUserInfoWindowBinding.inflate(layoutInflater)
        setContentView(b.root)

        val factory = AdditionalUserInfoViewModelFactory(
            UserRepo.getInstance(UserDB.getInstance(applicationContext))
        )
        vm = ViewModelProvider(this, factory)[AdditionalUserInfoViewModel::class.java]

        b.tvSkip.setOnClickListener {
            vm.onSkipClicked(this@AdditionalUserInfoWindow)
        }

        b.btnSaveData.setOnClickListener {
            vm.onSaveDataClicked(this@AdditionalUserInfoWindow)
        }

        b.ivAvatar.setOnClickListener {
            Log.d("App", "Avatar clicked")
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                val cropOptions = CropImageOptions().apply {
                    guidelines = CropImageView.Guidelines.ON
                    cropShape = CropImageView.CropShape.OVAL
                    aspectRatioY = 1
                    aspectRatioX = 1
                    fixAspectRatio = true
                }
                cropImage.launch(CropImageContractOptions(null, cropOptions))
            }
        }
    }
}