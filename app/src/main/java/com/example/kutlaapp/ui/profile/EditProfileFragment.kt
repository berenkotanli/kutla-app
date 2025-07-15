package com.example.kutlaapp.ui.profile

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.kutlaapp.R
import com.example.kutlaapp.data.model.UserProfile
import com.example.kutlaapp.databinding.FragmentEditProfileBinding
import com.example.kutlaapp.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class EditProfileFragment : Fragment() {

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!

    private val profileViewModel: ProfileViewModel by viewModels()

    private var selectedImageUri: Uri? = null
    private var selectedGender: String = ""

    private val imagePickerLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                selectedImageUri = data?.data
                binding.imgProfile.setImageURI(selectedImageUri)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileViewModel.userProfile.observe(viewLifecycleOwner) { profile ->
            profile?.let {
                binding.edtName.setText(it.name)
                binding.edtPhoneNumber.setText(it.phone)

                val formattedDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(it.birthDate))
                binding.edtBirthDay.setText(formattedDate)

                binding.edtBirthDay.tag = it.birthDate

                binding.edtGender.setText(it.gender)
                binding.edtJob.setText(it.job)
                binding.edtHobbies.setText(it.hobbies)
                selectedGender = it.gender

                val defaultAvatar = if (it.gender == "Kadın") R.drawable.avatar_women else R.drawable.avatar_man

                // Glide ile profil fotoğrafını yükleme
                Glide.with(this)
                    .load(it.profileImageUrl)
                    .placeholder(defaultAvatar)
                    .error(defaultAvatar)
                    .into(binding.imgProfile)
            }
        }

        // Kullanıcı profilini yükle
        profileViewModel.getUserId()?.let { profileViewModel.loadUserProfile(it) }

        // Profil resmi seçme
        binding.imgProfile.setOnClickListener {
            pickImageFromGallery()
        }

        // Telefon numarası doğrulama
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

        // Doğum günü seçimi
        binding.edtBirthDay.setOnClickListener {
            showDatePicker()
        }

        // Cinsiyet seçimi
        binding.edtGender.setOnClickListener {
            showGenderPicker()
        }

        binding.imgProfile.setOnClickListener {
            pickImageFromGallery()
        }

        // Kaydet butonu
        binding.btnSave.setOnClickListener {
            saveUserProfile()
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
        }
        imagePickerLauncher.launch(intent)
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val datePicker = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                val selectedCalendar = Calendar.getInstance().apply {
                    set(year, month, dayOfMonth)
                }

                val selectedDateInMillis = selectedCalendar.timeInMillis

                val formattedDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(selectedCalendar.time)
                binding.edtBirthDay.setText(formattedDate)

                binding.edtBirthDay.tag = selectedDateInMillis
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.show()
    }

    private fun showGenderPicker() {
        val options = arrayOf("Kadın", "Erkek")
        AlertDialog.Builder(requireContext())
            .setTitle("Cinsiyet Seçiniz")
            .setItems(options) { _, which ->
                selectedGender = options[which]
                binding.edtGender.setText(selectedGender)
            }
            .show()
    }

    private fun saveUserProfile() {
        val name = binding.edtName.text.toString()
        val phoneNumber = binding.edtPhoneNumber.text.toString()
        val birthDateInMillis = binding.edtBirthDay.tag as? Long ?: 0L
        val job = binding.edtJob.text.toString()
        val hobbies = binding.edtHobbies.text.toString()
        val gender = binding.edtGender.text.toString()

        if (name.isEmpty() || phoneNumber.isEmpty() || birthDateInMillis == 0L || gender.isEmpty()) {
            Toast.makeText(requireContext(), "Lütfen tüm alanları doldurun", Toast.LENGTH_SHORT).show()
            return
        }

        val userProfile = profileViewModel.getUserId()?.let {
            UserProfile(
                userId = it,
                name = name,
                phone = phoneNumber,
                birthDate = birthDateInMillis,
                gender = selectedGender,
                job = job,
                hobbies = hobbies
            )
        }

        if (userProfile != null) {
            profileViewModel.updateUserProfile(userProfile) { success ->
                if (success) {
                    Toast.makeText(requireContext(), "Profil güncellendi", Toast.LENGTH_SHORT).show()
                    parentFragmentManager.popBackStack()
                } else {
                    Toast.makeText(requireContext(), "Güncelleme başarısız", Toast.LENGTH_SHORT).show()
                }
            }
        }

        selectedImageUri?.let { uri ->
            profileViewModel.getUserId()?.let {
                profileViewModel.uploadProfileImage(it, uri) { imageUrl ->
                    if (imageUrl != null) {
                        Toast.makeText(requireContext(), "Profil resmi yüklendi", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}