package com.example.kutlaapp.services

import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar

class CheckBirthdaysFunction {

    private val db = FirebaseFirestore.getInstance()

    fun checkAndSendBirthdayNotifications(onComplete: () -> Unit) {
        val calendar = Calendar.getInstance()
        val todayDay = calendar.get(Calendar.DAY_OF_MONTH)
        val todayMonth = calendar.get(Calendar.MONTH) + 1

        db.collection("users")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val birthDate = document.getLong("birthDate") ?: continue
                    val birthCalendar = Calendar.getInstance().apply { timeInMillis = birthDate }

                    val birthDay = birthCalendar.get(Calendar.DAY_OF_MONTH)
                    val birthMonth = birthCalendar.get(Calendar.MONTH) + 1

                    if (birthDay == todayDay && birthMonth == todayMonth) {
                        val name = document.getString("name") ?: "Bir arkadaşın"
                        sendBirthdayNotification(name)
                    }
                }
                onComplete()
            }
    }

    private fun sendBirthdayNotification(name: String) {
        val title = "Bugün Doğum Günü!"
        val message = "$name'in doğum günü! Kutlamayı unutma 🎉"

        // Firebase Cloud Messaging ile Bildirim Gönder
        val notificationData = mapOf(
            "title" to title,
            "message" to message
        )

        FirebaseFirestore.getInstance().collection("notifications")
            .add(notificationData)
    }
}