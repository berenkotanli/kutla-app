package com.example.kutlaapp.data.repository

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.kutlaapp.data.model.UserProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import javax.inject.Inject

class ProfileRepository @Inject constructor(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val storage: FirebaseStorage
) {

    fun getUserId(): String? {
        return auth.currentUser?.uid
    }

    fun getUserProfile(userId: String): LiveData<UserProfile?> {
        val liveData = MutableLiveData<UserProfile?>()
        db.collection("users").document(userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    liveData.value = null
                    return@addSnapshotListener
                }
                if (snapshot != null && snapshot.exists()) {
                    liveData.value = snapshot.toObject(UserProfile::class.java)
                } else {
                    liveData.value = null
                }
            }
        return liveData
    }

    fun updateUserProfile(userProfile: UserProfile, callback: (Boolean) -> Unit) {
        db.collection("users").document(userProfile.userId)
            .set(userProfile)
            .addOnSuccessListener { callback(true) }
            .addOnFailureListener { callback(false) }
    }

    fun uploadProfileImage(userId: String, imageUri: Uri, callback: (String?) -> Unit) {
        val imageRef = storage.reference.child("profile_images/$userId.jpg")

        imageRef.putFile(imageUri)
            .continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let { throw it }
                }
                imageRef.downloadUrl
            }
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(task.result.toString())
                } else {
                    callback(null)
                    task.exception?.printStackTrace()
                }
            }
    }
}