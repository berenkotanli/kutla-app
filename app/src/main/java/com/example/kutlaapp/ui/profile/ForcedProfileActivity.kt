package com.example.kutlaapp.ui.profile

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.kutlaapp.data.model.UserProfile
import com.example.kutlaapp.databinding.ActivityForcedProfileBinding
import com.example.kutlaapp.ui.main.MainActivity
import com.example.kutlaapp.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForcedProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForcedProfileBinding
    private val profileViewModel: ProfileViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForcedProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Telefon numarası format doğrulama
        binding.edtPhoneNumber.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val phone = s.toString()
                if (!Patterns.PHONE.matcher(phone).matches()) {
                    binding.edtPhoneNumber.error = "Geçerli bir telefon numarası girin"
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.btnSave.setOnClickListener {
            val name = binding.edtName.text.toString().trim()
            val phone = binding.edtPhoneNumber.text.toString().trim()

            if (name.isEmpty() || phone.isEmpty()) {
                Toast.makeText(this, "Lütfen tüm alanları doldurun!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val userProfile = profileViewModel.getUserId()?.let {
                UserProfile(
                    userId = it,
                    name = name,
                    phone = phone
                )
            }

            if (userProfile != null) {
                profileViewModel.updateUserProfile(userProfile) { success ->
                    if (success) {
                        Toast.makeText(this, "Bilgiler kaydedildi!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, "Güncelleme başarısız", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}