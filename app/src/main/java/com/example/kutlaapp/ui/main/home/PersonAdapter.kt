package com.example.kutlaapp.ui.main.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kutlaapp.R
import com.example.kutlaapp.data.model.UserProfile
import com.example.kutlaapp.databinding.ItemPersonBinding
import com.example.kutlaapp.ui.extensions.toFormattedBirthDate

class PersonAdapter(
    private var friendsList: List<UserProfile>,
    private val onFriendClick: (UserProfile) -> Unit
) : RecyclerView.Adapter<PersonAdapter.FriendViewHolder>() {

    inner class FriendViewHolder(private val binding: ItemPersonBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(friend: UserProfile) {
            binding.tvName.text = friend.name
            binding.tvBirthday.text = friend.birthDate.toFormattedBirthDate()

            val defaultAvatar = if (friend.gender == "Kadın") R.drawable.avatar_women else R.drawable.avatar_man

            Glide.with(binding.root.context)
                .load(defaultAvatar)
                .error(defaultAvatar)
                .into(binding.ivProfile)

            binding.root.setOnClickListener { onFriendClick(friend) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        val binding = ItemPersonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FriendViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        holder.bind(friendsList[position])
    }

    override fun getItemCount(): Int = friendsList.size

    fun updateList(newFriends: List<UserProfile>) {
        friendsList = newFriends
        notifyDataSetChanged()
    }
}
