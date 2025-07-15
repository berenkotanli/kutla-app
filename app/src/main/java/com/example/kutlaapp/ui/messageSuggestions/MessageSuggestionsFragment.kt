package com.example.kutlaapp.ui.messageSuggestions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kutlaapp.data.model.UserProfile
import com.example.kutlaapp.databinding.FragmentMessageSuggestionsBinding
import com.example.kutlaapp.ui.dialogs.MessageTypeDialog
import com.example.kutlaapp.utils.MessageSuggestionHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MessageSuggestionsFragment : Fragment() {

    private var _binding: FragmentMessageSuggestionsBinding? = null
    private val binding get() = _binding!!
    private lateinit var selectedFriend: UserProfile
    private var selectedMessageType: String = "Romantik"
    private var selectedSalutation: String = "Sen"
    private lateinit var messageAdapter: MessageAdapter
    private val messageList = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMessageSuggestionsBinding.inflate(inflater, container, false)

        selectedFriend = arguments?.getSerializable("friend") as? UserProfile ?: UserProfile()

        showMessageTypeDialog()

        binding.swipeRefreshLayout.setOnRefreshListener {
            fetchMessageSuggestions(selectedFriend, selectedMessageType, selectedSalutation)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupRecyclerView() {
        messageAdapter = MessageAdapter(
            messages = messageList,
            onLikeClick = { message ->
            },
            onDislikeClick = { message ->
                messageList.remove(message)
                messageAdapter.notifyDataSetChanged()
            },
            onCopyClick = { message ->
                copyToClipboard(message)
                Toast.makeText(requireContext(), "Mesaj panoya kopyalandı", Toast.LENGTH_SHORT).show()
            }
        )

        binding.rvMessages.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = messageAdapter
        }
    }

    private fun showMessageTypeDialog() {
        MessageTypeDialog(requireContext()) { messageType, salutation ->
            fetchMessageSuggestions(selectedFriend, messageType, salutation)
            selectedMessageType = messageType
            selectedSalutation = salutation
        }.show()
    }

    private fun fetchMessageSuggestions(person: UserProfile, messageType: String, salutation: String) {
        lifecycleScope.launch {
            val messages = MessageSuggestionHelper.getMessageSuggestionsFromAI(person, messageType, salutation)

            if (messageList.isEmpty()) {
                setupRecyclerView()
            }

            messageList.clear()
            messageList.addAll(messages)

            messageAdapter.notifyDataSetChanged()
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun copyToClipboard(text: String) {
        val clipboard = requireContext().getSystemService(android.content.ClipboardManager::class.java)
        val clip = android.content.ClipData.newPlainText("Mesaj", text)
        clipboard.setPrimaryClip(clip)
    }
}