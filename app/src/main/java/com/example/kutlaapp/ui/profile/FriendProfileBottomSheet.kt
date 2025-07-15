package com.example.kutlaapp.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.kutlaapp.R
import com.example.kutlaapp.data.model.UserProfile
import com.example.kutlaapp.databinding.BottomSheetFriendProfileBinding
import com.example.kutlaapp.ui.extensions.toFormattedBirthDate
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.Calendar

class FriendProfileBottomSheet(private val friend: UserProfile) : BottomSheetDialogFragment() {

    private var _binding: BottomSheetFriendProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetFriendProfileBinding.inflate(inflater, container, false)
        setupUI()
        return binding.root
    }

    private fun setupUI() {
        binding.tvFriendName.text = friend.name
        binding.tvPhoneNumber.text = friend.phone
        binding.tvFriendJob.text = "Meslek: ${friend.job}"
        binding.tvFriendHobbies.text = "Hobiler: ${friend.hobbies}"
        binding.tvFriendBirthday.text = "Doğum Günü: ${friend.birthDate.toFormattedBirthDate()}"

        val zodiacSign = getUserZodiac(friend.birthDate)
        binding.tvZodiacSign.text = "Burcu: ${zodiacSign}"


        Glide.with(requireContext())
            .load(friend.profileImageUrl.ifEmpty { getAvatar(friend.gender) })
            .into(binding.ivFriendProfile)

        binding.btnClose.setOnClickListener { dismiss() }
    }

    private fun getAvatar(gender: String): Int {
        return if (gender == "Kadın") R.drawable.avatar_women else R.drawable.avatar_man
    }

    private fun getZodiacSign(day: Int, month: Int): String {
        return when {
            (month == 1 && day >= 20) || (month == 2 && day <= 18) -> "Kova"
            (month == 2 && day >= 19) || (month == 3 && day <= 20) -> "Balık"
            (month == 3 && day >= 21) || (month == 4 && day <= 19) -> "Koç"
            (month == 4 && day >= 20) || (month == 5 && day <= 20) -> "Boğa"
            (month == 5 && day >= 21) || (month == 6 && day <= 20) -> "İkizler"
            (month == 6 && day >= 21) || (month == 7 && day <= 22) -> "Yengeç"
            (month == 7 && day >= 23) || (month == 8 && day <= 22) -> "Aslan"
            (month == 8 && day >= 23) || (month == 9 && day <= 22) -> "Başak"
            (month == 9 && day >= 23) || (month == 10 && day <= 22) -> "Terazi"
            (month == 10 && day >= 23) || (month == 11 && day <= 21) -> "Akrep"
            (month == 11 && day >= 22) || (month == 12 && day <= 21) -> "Yay"
            else -> "Oğlak"
        }
    }

    private fun getUserZodiac(birthDate: Long): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = birthDate

        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH) + 1

        return getZodiacSign(day, month)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
