package com.example.kutlaapp.ui.messageSuggestions

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kutlaapp.R
import com.example.kutlaapp.databinding.ItemMessageBinding

class MessageAdapter(
    private val messages: List<String>,
    private val onLikeClick: (String) -> Unit,
    private val onDislikeClick: (String) -> Unit,
    private val onCopyClick: (String) -> Unit
) : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    private val likedMessages = mutableSetOf<String>()
    private val dislikedMessages = mutableSetOf<String>()

    inner class MessageViewHolder(private val binding: ItemMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(message: String) {
            binding.tvMessage.text = message
            updateButtonStates(message)

            binding.ivLike.setOnClickListener {
                if (likedMessages.contains(message)) {
                    likedMessages.remove(message)
                } else {
                    likedMessages.add(message)
                    dislikedMessages.remove(message)
                }
                updateButtonStates(message)
                onLikeClick(message)
            }

            binding.ivDislike.setOnClickListener {
                if (dislikedMessages.contains(message)) {
                    dislikedMessages.remove(message)
                } else {
                    dislikedMessages.add(message)
                    likedMessages.remove(message)
                }
                updateButtonStates(message)
                onDislikeClick(message)
            }

            binding.ivCopy.setOnClickListener {
                onCopyClick(message)
            }
        }

        private fun updateButtonStates(message: String) {
            if (likedMessages.contains(message)) {
                binding.ivLike.setImageResource(R.drawable.ic_thumb_up_filled)
                binding.ivDislike.setImageResource(R.drawable.ic_thumb_down)
            } else if (dislikedMessages.contains(message)) {
                binding.ivLike.setImageResource(R.drawable.ic_thumb_up)
                binding.ivDislike.setImageResource(R.drawable.ic_thumb_down_filled)
            } else {
                binding.ivLike.setImageResource(R.drawable.ic_thumb_up)
                binding.ivDislike.setImageResource(R.drawable.ic_thumb_down)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val binding = ItemMessageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MessageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind(messages[position])
    }

    override fun getItemCount(): Int = messages.size
}
