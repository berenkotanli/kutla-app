package com.example.kutlaapp.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.kutlaapp.databinding.FragmentProfileBinding
import com.example.kutlaapp.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import com.example.kutlaapp.R
import com.example.kutlaapp.ui.extensions.toFormattedDate
import com.google.firebase.auth.FirebaseAuth
import android.app.AlertDialog
import com.example.kutlaapp.ui.auth.LoginActivity

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val profileViewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userId = profileViewModel.getUserId()

        if (userId != null) {
            profileViewModel.loadUserProfile(userId)
        }

        profileViewModel.userProfile.observe(viewLifecycleOwner) { userProfile ->
            userProfile?.let {
                binding.tvName.text = it.name
                binding.tvPhone.text = formatText("Telefon: ", it.phone)

                val formattedBirthDate = it.birthDate.toFormattedDate()
                binding.tvBirthdate.text = formatText("Doğum Tarihi: ", formattedBirthDate)

                binding.tvJob.text = formatText("Meslek: ", it.job)
                binding.tvHobbies.text = formatText("Hobiler: ", it.hobbies)
                binding.tvGender.text = formatText("Cinsiyet: ", it.gender)

                val defaultAvatar = if (it.gender == "Kadın") R.drawable.avatar_women else R.drawable.avatar_man

                Glide.with(this)
                    .load(it.profileImageUrl)
                    .placeholder(defaultAvatar)
                    .error(defaultAvatar)
                    .into(binding.profileImage)
            }
        }

        binding.btnEdit.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, EditProfileFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.btnLogout.setOnClickListener {
            showLogoutDialog()
        }
    }

    private fun showLogoutDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("Çıkış yapmak istediğinize emin misiniz?")
            .setPositiveButton("Evet") { _, _ ->
                // Firebase çıkış işlemi
                FirebaseAuth.getInstance().signOut()

                val intent = Intent(requireContext(), LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                requireActivity().finish()
            }
            .setNegativeButton("İptal") { dialog, _ ->
                dialog.dismiss()
            }

        builder.create().show()
    }

    private fun formatText(label: String, value: String): SpannableString {
        val spannable = SpannableString("$label$value")
        spannable.setSpan(StyleSpan(Typeface.BOLD), 0, label.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return spannable
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}