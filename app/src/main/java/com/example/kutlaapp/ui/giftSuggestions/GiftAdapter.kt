package com.example.kutlaapp.ui.giftSuggestions

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kutlaapp.R
import com.example.kutlaapp.databinding.ItemGiftBinding

class GiftAdapter(
    private val products: List<String>,
    private val onProductClick: (String) -> Unit
) : RecyclerView.Adapter<GiftAdapter.SuggestionsViewHolder>() {

    private val likeStatus = mutableMapOf<Int, Boolean?>()

    inner class SuggestionsViewHolder(private val binding: ItemGiftBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(product: String, position: Int) {
            binding.tvProductName.text = product

            val currentStatus = likeStatus[position]
            updateIcons(currentStatus)

            binding.ivLike.setOnClickListener {
                if (likeStatus[position] == true) {
                    likeStatus.remove(position)
                } else {
                    likeStatus[position] = true
                }
                notifyItemChanged(position)
            }

            binding.ivDislike.setOnClickListener {
                if (likeStatus[position] == false) {
                    likeStatus.remove(position)
                } else {
                    likeStatus[position] = false
                }
                notifyItemChanged(position)
            }

            binding.llAmazon.setOnClickListener { openUrl("https://www.amazon.com.tr/s?k=${product}") }
            binding.llHepsiburada.setOnClickListener { openUrl("https://www.hepsiburada.com/ara?q=${product}") }
            binding.llTrendyol.setOnClickListener { openUrl("https://www.trendyol.com/sr?q=${product}") }

            binding.root.setOnClickListener { onProductClick(product) }
        }

        private fun openUrl(url: String) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            binding.root.context.startActivity(intent)
        }

        private fun updateIcons(status: Boolean?) {
            when (status) {
                true -> {
                    binding.ivLike.setImageResource(R.drawable.ic_thumb_up_filled)
                    binding.ivDislike.setImageResource(R.drawable.ic_thumb_down)
                }
                false -> {
                    binding.ivLike.setImageResource(R.drawable.ic_thumb_up)
                    binding.ivDislike.setImageResource(R.drawable.ic_thumb_down_filled)
                }
                null -> {
                    binding.ivLike.setImageResource(R.drawable.ic_thumb_up)
                    binding.ivDislike.setImageResource(R.drawable.ic_thumb_down)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuggestionsViewHolder {
        val binding = ItemGiftBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SuggestionsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SuggestionsViewHolder, position: Int) {
        holder.bind(products[position], position)
    }

    override fun getItemCount(): Int = products.size

    fun getLikedProducts(): List<String> {
        return likeStatus.filterValues { it == true }.keys.mapNotNull { index ->
            products.getOrNull(index)
        }
    }

    fun getDislikedProducts(): List<String> {
        return likeStatus.filterValues { it == false }.keys.mapNotNull { index ->
            products.getOrNull(index)
        }
    }
}