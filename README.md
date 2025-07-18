# 📱 Kutla - Özel Gün Hatırlatıcısı ve Kişiye Özel Hediye/Mesaj Önericisi

**Kutla**, sevdiklerinin özel günlerini hatırlamanı kolaylaştıran ve onlara en uygun hediye ve mesajları öneren kişisel bir asistan uygulamasıdır.

Uygulamada kullanıcılar telefon rehberinden kişiler ekleyebilir, doğum günü veya diğer özel günleri takip edebilir, yapay zeka destekli hediye ve mesaj önerileri alabilir.

---

## 🚀 Özellikler

### 🔐 Giriş/Kayıt Sistemi
- Gmail ile giriş (Firebase Authentication)
- E-posta ve şifre ile giriş/kayıt

### 👥 Kişi Yönetimi
- Telefon rehberinden kişi ekleme
- Eğer kişi **Kutla kullanıcısıysa**: ➕ `Ekle` butonu
- Eğer değilse: ✉️ `Davet Et` butonu

### 📅 Ana Sayfa
- Kişi listesi
- Doğum gününe kaç gün kaldığını gösterme
- Kişi detay ekranı (Ad, soyad, doğum tarihi, burç, hobiler vs.)

### 🎁 Hediye Önerileri
- Google Gemini API ile **kişiye özel** öneriler
- Amazon, Trendyol, Hepsiburada linkleri
- "Beğendim / Beğenmedim" seçeneği ile öneri listesi güncellenir  
  *(Pull to Refresh destekli)*

### ✉️ Mesaj Önerileri
- Doğum günü mesajı üretme
- Ton (samimi, resmi, romantik) ve hitap (sen, siz) seçimi
- Beğen / Beğenmeme ile öneri sistemi güncellenir
- Kopyala butonu ile mesajı kolayca paylaş

### 👤 Profil Ekranı
- Kullanıcının adı, hobileri, doğum tarihi vs. bilgileri
- Bilgileri Firebase Firestore ve Storage ile güncelleyebilme

---

## 🧰 Kullanılan Teknolojiler

| Alan                 | Teknoloji                                  |
|----------------------|---------------------------------------------|
| Programlama Dili     | Kotlin                                     |
| Mimarî               | MVVM (Model - View - ViewModel)            |
| Bağımlılık Enjeksiyonu | Hilt                                     |
| Veri Tabanı          | Firebase Firestore                         |
| Dosya Depolama       | Firebase Storage                           |
| Kimlik Doğrulama     | Firebase Auth (Email/Şifre, Gmail)         |
| Yapay Zeka API       | Google Gemini (hediye ve mesaj üretimi için) |
| Diğer                | ViewModel, LiveData, Room, Coroutines, Retrofit |


