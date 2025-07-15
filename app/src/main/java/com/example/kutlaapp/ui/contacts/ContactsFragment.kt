package com.example.kutlaapp.ui.contacts

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kutlaapp.data.model.Contact
import com.example.kutlaapp.databinding.FragmentContactsBinding
import com.example.kutlaapp.viewmodel.ContactsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContactsFragment : Fragment() {

    private var _binding: FragmentContactsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ContactsViewModel by viewModels()
    private lateinit var adapter: ContactsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContactsBinding.inflate(inflater, container, false)

        checkPermissions()
        setupRecyclerView()
        observeViewModel()

        return binding.root
    }

    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CONTACTS)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                arrayOf(Manifest.permission.READ_CONTACTS), 100)
        } else {
            viewModel.loadContacts()
        }
    }

    private fun setupRecyclerView() {
        adapter = ContactsAdapter(
            listOf(),
            { contact -> addFriend(contact) },
            { contact -> inviteFriend(contact) }
        )
        binding.rvContacts.layoutManager = LinearLayoutManager(requireContext())
        binding.rvContacts.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.contacts.observe(viewLifecycleOwner) { contacts ->
            adapter.updateList(contacts)
        }
    }

    private fun addFriend(contact: Contact) {
        contact.userId?.let { userId ->
            viewModel.addFriend(
                userId,
                onSuccess = {
                    Toast.makeText(requireContext(), "Arkadaş eklendi!", Toast.LENGTH_SHORT).show()
                },
                onFailure = { error ->
                    Toast.makeText(requireContext(), "Hata: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }

    private fun inviteFriend(contact: Contact) {
        Toast.makeText(requireContext(), "${contact.name} davet edildi!", Toast.LENGTH_SHORT).show()
    }
}