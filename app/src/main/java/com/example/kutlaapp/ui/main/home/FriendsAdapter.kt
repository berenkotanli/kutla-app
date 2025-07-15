package com.example.kutlaapp.ui.main.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kutlaapp.R
import com.example.kutlaapp.data.model.UserProfile
import com.example.kutlaapp.databinding.ItemFriendBinding
import com.example.kutlaapp.ui.extensions.calculateDaysUntilBirthday

class FriendsAdapter(
    private var friendsList: List<UserProfile>,
    private val onFriendClick: (UserProfile) -> Unit
) : RecyclerView.Adapter<FriendsAdapter.PeopleViewHolder>() {

    inner class PeopleViewHolder(private val binding: ItemFriendBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(friend: UserProfile) {
            binding.tvPersonName.text = friend.name

            val defaultAvatar = if (friend.gender == "Kadın") R.drawable.avatar_women else R.drawable.avatar_man

            Glide.with(binding.root.context)
                .load(defaultAvatar)
                .error(defaultAvatar)
                .into(binding.ivAvatar)

            val remainingDays = friend.birthDate.calculateDaysUntilBirthday()
            if (remainingDays > 0) {
                binding.tvInfo.text = "Doğum gününe $remainingDays gün kaldı"
            } else if(remainingDays == 0) {
                binding.tvInfo.text = "Bugün doğum günü!"
            } else {
                binding.tvInfo.text = "Doğum günü geçti"
            }
            binding.root.setOnClickListener { onFriendClick(friend) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeopleViewHolder {
        val binding = ItemFriendBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PeopleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PeopleViewHolder, position: Int) {
        holder.bind(friendsList[position])
    }

    override fun getItemCount(): Int = friendsList.size

    fun updateList(newFriends: List<UserProfile>) {
        friendsList = newFriends
        notifyDataSetChanged()
    }
}