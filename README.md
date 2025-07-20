#  Kutla - Sevdiklerin İçin Akıllı Hatırlatıcı ve Öneri Asistanı

**Kutla**, sevdiklerinin özel günlerini hatırlamanı kolaylaştıran ve onlara en uygun hediye ve mesajları öneren kişisel bir asistan uygulamasıdır.

Uygulamada kullanıcılar telefon rehberinden kişiler ekleyebilir, doğum günü veya diğer özel günleri takip edebilir, yapay zeka destekli hediye ve mesaj önerileri alabilir.

<img src="https://github.com/user-attachments/assets/47f215ab-2d22-4bdb-b236-13af2e454f22" width="500" />

---

##  Özellikler

###  Giriş/Kayıt Sistemi
- Gmail ile giriş (Firebase Authentication)
- E-posta ve şifre ile giriş/kayıt

###  Kişi Yönetimi
- Telefon rehberinden kişi ekleme
- Eğer kişi **Kutla kullanıcısıysa**: ➕ `Ekle` butonu
- Eğer değilse: ✉️ `Davet Et` butonu

###  Ana Sayfa
- Kişi listesi
- Doğum gününe kaç gün kaldığını gösterme
- Kişi detay ekranı (Ad, soyad, doğum tarihi, burç, hobiler vs.)

###  Hediye Önerileri
- Google Gemini API ile **kişiye özel** öneriler
- Amazon, Trendyol, Hepsiburada linkleri
- "Beğendim / Beğenmedim" seçeneği ile öneri listesi güncellenir  
  *(Pull to Refresh destekli)*

###  Mesaj Önerileri
- Doğum günü mesajı üretme
- Ton (samimi, resmi, romantik) ve hitap (sen, siz) seçimi
- Beğen / Beğenmeme ile öneri sistemi güncellenir
- Kopyala butonu ile mesajı kolayca paylaş

###  Profil Ekranı
- Kullanıcının adı, hobileri, doğum tarihi vs. bilgileri
- Bilgileri Firebase Firestore ve Storage ile güncelleyebilme

---

##  Kullanılan Teknolojiler

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

##  Ekran Görüntüleri ve Video

<p align="center">
  <img src="https://github.com/user-attachments/assets/0c3e4b2a-61b2-483e-85ab-6af05fc29dbc" width="270"/>
  <img src="https://github.com/user-attachments/assets/8339662e-32c8-485e-a967-7a7ece387e30" width="270"/>
  <img src="https://github.com/user-attachments/assets/aa2f256d-2119-4102-a054-abfd261b3f26" width="270"/>
</p>
<p align="center">
  <img src="https://github.com/user-attachments/assets/a8bcec2e-a95d-40a0-b03d-60c869b59d2e" width="270"/>
  <img src="https://github.com/user-attachments/assets/c545a043-d99e-4cc1-9594-a9c2c4bb11bf" width="270"/>
  <img src="https://github.com/user-attachments/assets/3ed13391-9c75-4bec-936e-c35484c4fa53" width="270"/>
</p>
<p align="center">
  <img src="https://github.com/user-attachments/assets/ccc636d4-0abc-4339-a7ad-3c1ffe56df74" width="270"/>
  <img src="https://github.com/user-attachments/assets/4052d833-194c-4249-8f39-75e40440bcf3" width="270"/>
  <img src="https://github.com/user-attachments/assets/b3720588-1c90-4ec8-a5ab-38c35663e34b" width="270"/>
</p>

