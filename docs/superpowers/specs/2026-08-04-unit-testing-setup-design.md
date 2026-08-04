# Unit Test Kurulumu — Tasarım & Karar Dokümanı

**Tarih:** 2026-08-04
**Kapsam:** Projeye ilk unit test altyapısını kurmak; test öncesi testability denetimi ve hedeflenen mimari iyileştirmeler.

---

## 1. Amaç

Quizzy multi-module projesine ilk kez unit test eklemek. Kullanıcının isteği: test yazmadan önce
(a) her class'ı testability açısından gözden geçirmek, (b) teste uygun olmayan yerleri tespit edip
gerekirse temizlemek, (c) sonra doğru katmanlara test yazmak.

---

## 2. Mevcut Durum (Keşif Bulguları)

### 2.1 Test altyapısı — YOK
- Projede yazılmış **hiçbir test yok** (`src/test/`, `src/androidTest/` hiçbir modülde mevcut değil).
- `libs.versions.toml`'da yalnızca ölü JUnit4 + androidx.test girişleri var (hiçbir modülde kullanılmıyor).
- **Eksik:** MockK, `kotlinx-coroutines-test`, Turbine, Truth, `hilt-android-testing`, `arch-core-testing`.
- Convention plugin'lerin **hiçbiri** test source set'ine bağımlılık eklemiyor, test task'ı yapılandırmıyor.

### 2.2 Toolchain
- Kotlin 2.4.10, AGP 9.2.1, KSP 2.3.2, Hilt 2.60.1, coroutines 1.11.0.
- compile/target SDK 37, min SDK 24, Java 21.
- `domain` katmanları saf JVM (`quiz.jvm.library`) → JUnit + MockK + coroutines-test için ideal.
- Hata yönetimi Kotlin stdlib `Result<T>` üzerinden (özel `Resource` YOK); `core:common/BaseException` hiyerarşisi failure senaryolarında kullanılır.

---

## 3. Testability Audit Sonucu (99 class tarandı)

**Doğrulanmış testability engeli: 0.** Adversarial doğrulama turu 10 ham "şüpheyi" inceledi, **10'u da yanlış pozitif** çıktı. Kod mevcut haliyle JUnit4 + MockK + Truth ile test edilebilir.

Elenen yanlış pozitiflerin özeti (hiçbiri zorunlu refactor değil):
- ViewModel `init{}` use case çağrıları → `coEvery` ile VM kurulmadan stub'lanır (standart).
- `SavedStateHandle.toRoute()` → testte `SavedStateHandle(mapOf(...))` ile beslenir.
- `SplashViewModel` `delay(2000ms)` → `TestDispatcher` sanal zamanda `advanceUntilIdle()` ile geçer.
- MVI effect buffer'sız `Channel` → Turbine collector'ı action'dan önce başlatılır (one-shot event idiomu). **Not:** Channel elemanı tamponlar, düşmez (SharedFlow olsaydı düşerdi).

**Sonuç:** "Test öncesi temizlik" için zorunlu bir refactor bulunmadı. Kod test-ready.

---

## 4. Domain Katmanı Mimari Değerlendirmesi (25 use case, hakem onaylı)

Best-practice merceğinden sınıflandırma:

| Kategori | Sayı | Anlamı |
|---|---|---|
| A — Gerçek koordinasyon use case | 0 | Tek dosyada koordinasyon eden use case yok |
| B — Anemik pass-through | 23 | Tek satır `repository.x()` delegasyonu |
| C — Saf hesaplama | 2 | `CalculateScoreUseCase`, `UpdateOptionsUseCase` |

**Değerlendirme:** Katman mimarisi (ui→domain, data→domain, Hilt @Binds, type-safe nav) **sağlam**. Ancak domain
katmanı davranışsal olarak **neredeyse boş** — gerçek iş `:data` ve backend'de. "Yanlış mimari" değil,
**"doldurulmamış mimari."** Modeller anemik (davranışsız veri sınıfları).

**KARAR: 23 pass-through use case'e DOKUNULMAYACAK.** Tutarlı bir konvansiyonu bozmak davranış değiştirmez,
gelecekteki genişleme noktalarını (validasyon, koordinasyon) yok eder. Katmanın değeri doldurulmasıyla ölçülür;
bu ayrı bir gelecek işidir.

### Best-practice notu (referans için)
Saf karar kuralları (SummaryState sınıflandırması, skor hesabı) ideal olarak **domain entity davranışı** olmalı
(ne ViewModel, ne ayrı use case). Tek satırlık saf kuralı Hilt use case'ine sarmak "anemic use case" eğilimidir.

---

## 5. Refactor Kararları (Kullanıcı Onaylı)

Etki alanı analizi iki hedeflenen entity-taşımasının beklenenden dallı olduğunu gösterdi. Kullanıcı kararları:

### 5.1 CalculateScoreUseCase → QuizModel davranışı: **ERTELENDİ**
- **Engel:** `QuizViewModel`, `QuizModel` instance'ını state'te tutmuyor; alanları `UiState`'e dağıtıyor
  (`QuizViewModel.kt:52-64`). Skor `currentUiState.score/correctAnswers/questions.size`'dan hesaplanıyor
  (`QuizViewModel.kt:75-79`). Entity davranışına taşımak `UiState`'e `quizModel` alanı eklemeyi + `getQuiz`
  yeniden yazımını gerektirir — yani test edilecek ViewModel'i refactor etmek.
- **Karar:** Ertele. `CalculateScoreUseCase` şu an use case olarak gayet test edilebilir. Entity taşımasını
  sonraki tura bırak. **Bu turda dokunulmayacak.**

### 5.2 SummaryState mantığı: **Sadece saf fonksiyona çıkar (modül açma)**
- **Reddedilen ağır yol:** `summary:domain` modülünü açmak 6 adım gerektiriyor (settings.gradle include —
  modül şu an include bile edilmemiş, `src/` yok — + boş domain doldur + enum public + ui→domain bağımlılık +
  4 import + build doğrula). Build riski + şu anki hedefin dışında.
- **Karar (uygulanacak):** `SummaryState`'i UI'da bırak. `SummaryViewModel.kt:31-35`'teki sınıflandırma
  `when` bloğunu ViewModel içinden **top-level saf `internal fun`'a** çıkar. Sıfır build riski, modül zinciri
  açılmaz, test ViewModel kurmadan saf fonksiyon olarak yazılabilir.

**Uygulanacak değişiklik (tek dosya, davranış birebir aynı):**
```kotlin
// feature/summary/ui/.../SummaryViewModel.kt (dosya düzeyinde, top-level)
internal fun resolveSummaryState(correctAnswers: Int, wrongAnswers: Int): SummaryState = when {
    correctAnswers > wrongAnswers -> SummaryState.CORRECT
    correctAnswers == wrongAnswers -> SummaryState.EQUAL
    else -> SummaryState.WRONG
}
```
init bloğunda `state = when { ... }` yerine `state = resolveSummaryState(args.correctAnswers, args.wrongAnswers)`.

---

## 6. Test Altyapısı Kararları (Kullanıcı Onaylı)

- **Framework:** JUnit4 + MockK + Truth.
- **Altyapı:** Yeni `quiz.test` convention plugin'i (test bağımlılıklarını tek yerde topla, CLAUDE.md'nin
  convention-plugin felsefesine uygun).
- **Ek kütüphaneler:** `kotlinx-coroutines-test`, Turbine (Flow/effect testi için).
- **Ortak test yardımcıları:** `MainDispatcherRule` (JUnit4 TestWatcher) — her ViewModel testi için
  `Dispatchers.setMain(StandardTestDispatcher())`.
- **Kapsam (KULLANICI GÜNCELLEMESİ):** SADECE ui katmanı — **tüm 14 ViewModel'a test.** Domain use case
  testleri (saf mantık + delege) bu turda YAZILMAYACAK. `resolveSummaryState` refactor'ı yine yapılır ama
  ayrı saf-fonksiyon testi olmaz; SummaryViewModel testi içinde `state` alanı üzerinden dolaylı doğrulanır.

---

## 7. Test Hedefleri — TÜM 14 ViewModel (Kullanıcı Onaylı Kapsam)

Her ViewModel'ın kendi `:ui` modülünde `src/test/` altında testi yazılacak. Grup içi sıra değer/karmaşıklığa göre:

### Grup A — Gerçek state mantığı (en zengin testler)
- `QuizViewModel` — soru ilerletme state makinesi, skor+submit, NavigateSummary effect, doğru cevap sayacı.
- `RegisterViewModel` — `checkButtonEnabled`, success/error dialog dallanması, dismiss→navigasyon.
- `LoginViewModel` — login success→NavigateHome / fail→dialog, reset-password mail, bottom sheet toggle.
- `SearchViewModel` — query>2 arama vs ≤2 initial listeye dönüş dallanması.
- `DetailViewModel` — favorite toggle (isFavorite'e göre ekle/sil) + toast effect.
- `EditProfileViewModel` — form alan güncellemeleri, avatar seçimi, saveProfile avatar-id çözümleme.

### Grup B — Data-yükle + fold(success/error) + effect
- `HomeViewModel` — 3 paralel init yükleme, fold→state / ShowError effect, navigasyon effect'leri.
- `ProfileViewModel` — getProfile Flow fold, getRank, logout→Logout effect.
- `FavoritesViewModel` — getFavorites, deleteFavorite→reload.
- `CategoryViewModel` — init route arg (title/imageUrl) + kategori yükleme.
- `LeaderboardViewModel` — init tek success mapping (onAction yok).

### Grup C — Basit (init/effect ağırlıklı)
- `WelcomeViewModel` — loginWithGoogle success/fail, navigasyon effect'leri.
- `SummaryViewModel` — init'te SavedStateHandle→state; `state` alanı 3 senaryo (CORRECT/EQUAL/WRONG) doğrulanır
  (resolveSummaryState dolaylı kapsanır). onAction navigasyon effect'leri.
- `SplashViewModel` — `delay` + advanceUntilIdle, checkUserLoggedIn success→NavigateHome / fail→NavigateWelcome.

---

## 8. Teknik Notlar (Test Yazarken)
- **MVI effect testi:** Turbine `uiEffect.test { }` collector'ı, `onAction`'dan ÖNCE (veya `backgroundScope`'ta)
  başlatılmalı. Effect `Channel` üzerinden aktığı için eleman tamponlanır.
- **State testi:** `viewModel.currentUiState` / `uiState.value` senkron okunur; emisyon akışı için Turbine.
- **SavedStateHandle:** route arg'ları `SavedStateHandle(mapOf("id" to 5))` ile önceden konur.
- **Coroutine:** Tüm ViewModel'ler `viewModelScope.launch` → `MainDispatcherRule` zorunlu.
  `SplashViewModel` `delay` → `advanceUntilIdle()`.

---

## 9. Sıradaki Adım
Bu spec onaylandıktan sonra `writing-plans` ile detaylı implementasyon planı üretilecek.

**Plan kapsamı (kullanıcı onaylı):** Altyapı + **TÜM 14 ViewModel testi** — uçtan uca, tek planda.
Domain use case testleri bu turda YOK.

1. `resolveSummaryState` refactor'ı (tek dosya, §5.2) — onaylandı.
2. `libs.versions.toml`'a test kütüphaneleri (MockK, Truth, coroutines-test, Turbine).
3. `quiz.test` convention plugin'i (test bağımlılıklarını feature `:ui` modüllerine uygulayan).
4. `MainDispatcherRule` ortak yardımcısı (nereye konacağı planda netleşecek — paylaşımlı test yardımcısı,
   muhtemelen core:ui veya ayrı bir test-fixtures modülü; her `:ui` test'inden erişilebilir olmalı).
5. Her feature `:ui` modülüne `quiz.test` plugin uygulaması + `src/test/` kurulumu.
6. **TÜM 14 ViewModel testi** (§7 Grup A → B → C sırasıyla).
