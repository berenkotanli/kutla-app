package com.example.kutlaapp.ui.dialogs

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.widget.PopupMenu
import androidx.core.widget.doAfterTextChanged
import com.example.kutlaapp.R

class MessageTypeDialog(
    context: Context,
    private val onMessageSelected: (String, String) -> Unit
) {
    private val dialog: Dialog = Dialog(context)
    private var selectedMessageType: String = ""
    private var selectedSalutation: String = ""

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_message_type, null)
        dialog.setContentView(view)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)

        val edtMessageType = view.findViewById<EditText>(R.id.edtMessageType)
        val etSalutation = view.findViewById<EditText>(R.id.etSalutation)
        val btnConfirm = view.findViewById<Button>(R.id.btnConfirm)

        val messageTypes = listOf("Romantik", "Arkadaşça", "Samimi", "Komik", "Resmi")

        edtMessageType.setOnClickListener {
            showPopupMenu(context, it, messageTypes) { selectedType ->
                edtMessageType.setText(selectedType)
                selectedMessageType = selectedType
            }
        }

        etSalutation.doAfterTextChanged {
            selectedSalutation = it.toString().trim()
        }

        btnConfirm.setOnClickListener {
            if (selectedMessageType.isEmpty() || selectedSalutation.isEmpty()) {
                Toast.makeText(context, "Lütfen tüm alanları doldurun!", Toast.LENGTH_SHORT).show()
            } else {
                onMessageSelected(selectedMessageType, selectedSalutation)
                dialog.dismiss()
            }
        }

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    fun show() {
        dialog.show()
    }

    private fun showPopupMenu(context: Context, anchor: android.view.View, items: List<String>, onItemSelected: (String) -> Unit) {
        val popupMenu = PopupMenu(context, anchor)
        items.forEachIndexed { index, item ->
            popupMenu.menu.add(0, index, 0, item)
        }
        popupMenu.setOnMenuItemClickListener { menuItem: MenuItem ->
            onItemSelected(menuItem.title.toString())
            true
        }
        popupMenu.show()
    }
}