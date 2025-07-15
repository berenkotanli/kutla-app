package com.example.kutlaapp.data.repository

import android.content.Context
import com.example.kutlaapp.data.model.Contact
import com.example.kutlaapp.utils.ContactsHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ContactsRepository @Inject constructor(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth
) {
    // **Rehberi Okuyup Firebase Kullanıcıları ile Karşılaştır**
    suspend fun getContacts(context: Context): List<Contact> {
        val localContacts = ContactsHelper.getContacts(context)
        val kutlaUsersSnapshot = db.collection("users").get().await()

        val kutlaUsers = kutlaUsersSnapshot.documents.mapNotNull { doc ->
            val phone = doc.getString("phone")?.replace(" ", "")?.replace("-", "")
            phone?.let { Contact(doc.getString("name") ?: "", it, true, doc.id) }
        }

        return localContacts.map { contact ->
            kutlaUsers.find { it.phoneNumber == contact.phoneNumber }?.let {
                contact.copy(isKutlaUser = true, userId = it.userId)
            } ?: contact
        }.sortedByDescending { it.isKutlaUser }
    }

    // **Arkadaş Ekleme İşlemi**
    fun addFriend(friendId: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val currentUserId = auth.currentUser?.uid ?: return

        val friendRef = db.collection("friends").document(currentUserId)

        friendRef.get().addOnSuccessListener { document ->
            if (document.exists()) {
                friendRef.update("friendList", FieldValue.arrayUnion(friendId))
                    .addOnSuccessListener { onSuccess() }
                    .addOnFailureListener { onFailure(it) }
            } else {
                val newFriendList = hashMapOf("friendList" to listOf(friendId))
                friendRef.set(newFriendList)
                    .addOnSuccessListener { onSuccess() }
                    .addOnFailureListener { onFailure(it) }
            }
        }.addOnFailureListener { onFailure(it) }
    }
}