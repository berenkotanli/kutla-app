package com.example.kutlaapp.utils

import android.content.Context
import android.provider.ContactsContract
import com.example.kutlaapp.data.model.Contact

object ContactsHelper {
    fun getContacts(context: Context): List<Contact> {
        val contactList = mutableListOf<Contact>()
        val contentResolver = context.contentResolver
        val cursor = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            arrayOf(
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER
            ),
            null, null, null
        )

        cursor?.use {
            while (it.moveToNext()) {
                val name = it.getString(0)
                val phone = it.getString(1).replace(" ", "").replace("-", "")
                contactList.add(Contact(name, phone))
            }
        }
        return contactList.distinctBy { it.phoneNumber }
    }
}
