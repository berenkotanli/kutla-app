package com.example.kutlaapp.ui.contacts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kutlaapp.data.model.Contact
import com.example.kutlaapp.databinding.ItemContactBinding

class ContactsAdapter(
    private var contactList: List<Contact>,
    private val onAddClicked: (Contact) -> Unit,
    private val onInviteClicked: (Contact) -> Unit
) : RecyclerView.Adapter<ContactsAdapter.ContactViewHolder>() {

    inner class ContactViewHolder(private val binding: ItemContactBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(contact: Contact) {
            binding.tvName.text = contact.name

            with(binding){
                if (contact.isKutlaUser){
                    tvInvite.visibility = View.GONE
                    btnAction.visibility = View.VISIBLE
                } else {
                    btnAction.visibility = View.GONE
                    tvInvite.visibility = View.VISIBLE
                }
            }

            binding.btnAction.setOnClickListener {
                onAddClicked(contact)
            }
            binding.tvInvite.setOnClickListener {
                onInviteClicked(contact)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val binding = ItemContactBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ContactViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bind(contactList[position])
    }

    override fun getItemCount(): Int = contactList.size

    fun updateList(newList: List<Contact>) {
        contactList = newList
        notifyDataSetChanged()
    }
}
