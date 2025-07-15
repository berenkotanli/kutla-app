package com.example.kutlaapp.utils

import com.example.kutlaapp.BuildConfig
import com.example.kutlaapp.data.model.UserProfile
import com.example.kutlaapp.ui.extensions.calculateAge
import com.example.kutlaapp.ui.extensions.parseResponseToListMessage
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object MessageSuggestionHelper {

    private const val GEMINI_API_KEY = BuildConfig.GEMINI_API_KEY

    suspend fun getMessageSuggestionsFromAI(person: UserProfile, messageType: String, salutation: String): List<String> = withContext(Dispatchers.IO) {
        val generativeModel = GenerativeModel(
            modelName = "gemini-1.5-flash",
            apiKey = GEMINI_API_KEY
        )

        val prompt = """
            Kişi detayları aşağıdadır:
            İsim: ${person.name}
            Yaşı: ${person.birthDate.calculateAge()}
            Cinsiyeti: ${person.gender}
            Mesaj türü: $messageType
            Hitap Şekli: $salutation
            
            Bu kişiye özel, $messageType tarzında ve "$salutation" şeklinde hitap içeren doğum günü mesajları öner.
            Cevabını 5 madde halinde liste olarak yaz ve sadece mesajları içersin.
        """.trimIndent()

        val response = generativeModel.generateContent(prompt)
        println(response.text)
        response.text?.let { it.parseResponseToListMessage() } ?: emptyList()
    }
}