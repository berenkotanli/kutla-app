package com.example.kutlaapp.data.repository

import com.example.kutlaapp.data.model.UserProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class HomeRepository @Inject constructor(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth
) {

    suspend fun getFriends(): List<UserProfile> {
        val currentUserId = auth.currentUser?.uid ?: throw Exception("Kullanıcı oturumu açık değil.")

        val friendDoc = db.collection("friends").document(currentUserId).get().await()

        val friendIds = friendDoc.get("friendList") as? List<String> ?: emptyList()

        if (friendIds.isEmpty()) return emptyList()

        val friendsSnapshot = db.collection("users")
            .whereIn("userId", friendIds)
            .get()
            .await()

        return friendsSnapshot.documents.mapNotNull { doc ->
            doc.toObject(UserProfile::class.java)
        }
    }
}
