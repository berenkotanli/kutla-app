package com.example.kutlaapp.utils

import com.example.kutlaapp.BuildConfig
import com.example.kutlaapp.data.model.UserProfile
import com.example.kutlaapp.ui.extensions.calculateAge
import com.example.kutlaapp.ui.extensions.parseResponseToList
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object GiftSuggestionHelper {

    private const val GEMINI_API_KEY = BuildConfig.GEMINI_API_KEY

    suspend fun getProductSuggestionsFromAI(person: UserProfile): List<String> = withContext(Dispatchers.IO) {
        val generativeModel = GenerativeModel(
            modelName = "gemini-1.5-flash",
            apiKey = GEMINI_API_KEY
        )

        val prompt = """
            Kişi detayları aşağıdadır:
            İsim: ${person.name}
            Hobiler: ${person.hobbies}
            Yaşı: ${person.birthDate.calculateAge()}
            Cinsiyeti: ${person.gender}
            
            Bu detaylara göre benzersiz ve bu kişiye özel uygun hediye öner. Cevabını 5 madde halinde liste olarak yaz ve sadece isimleri olsun hediye önerilerinin.
        """.trimIndent()

        val response = generativeModel.generateContent(prompt)
        println(response.text)
        response.text?.let { it.parseResponseToList() } ?: emptyList()
    }

    suspend fun getFilteredProductSuggestionsFromAI(person: UserProfile, likedProducts: List<String>, dislikedProducts: List<String>): List<String> = withContext(Dispatchers.IO) {
        val generativeModel = GenerativeModel(
            modelName = "gemini-1.5-flash",
            apiKey = GEMINI_API_KEY
        )

        val prompt = """
        Kişi detayları:
        İsim: ${person.name}
        Hobiler: ${person.hobbies}
        Yaşı: ${person.birthDate.calculateAge()}
        Cinsiyeti: ${person.gender}

        Daha önce beğenilen ürünler: ${likedProducts.joinToString()}
        Beğenilmeyen ürünler: ${dislikedProducts.joinToString()}

        Yeni öneriler oluştur: Beğenilmeyen ürünleri önermemelisin, beğenilenlere benzer yeni öneriler eklemelisin.
        Cevabı 5 madde halinde liste olarak ver ve sadece ürün isimleri olsun.
    """.trimIndent()

        val response = generativeModel.generateContent(prompt)
        println(response.text)
        response.text?.let { it.parseResponseToList() } ?: emptyList()
    }

}
