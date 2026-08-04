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
- **Kapsam (ilk tur):** domain + ui katmanları. data'yı sonraki tura bırak.

---

## 7. Test Hedefleri (Öncelik Sırası)

### Öncelik 1 — Saf mantık (mock'suz, en yüksek değer)
- `CalculateScoreUseCase` — integer division, `totalQuestions<=0` guard, sınır değerleri.
- `UpdateOptionsUseCase` — 4 dallı `when`, `selectedOption==null` (cevap gösterme) vs seçilmiş doğru/yanlış.
- `resolveSummaryState` (§5.2 sonrası) — 3 dal: CORRECT / EQUAL / WRONG.

### Öncelik 2 — Yüksek değerli ViewModel'ler (mock + dispatcher + Turbine)
- `QuizViewModel` — soru ilerletme state makinesi, skor+submit, NavigateSummary effect.
- `RegisterViewModel` — `checkButtonEnabled`, dialog dallanması.
- `LoginViewModel` — login success/fail effect, reset-password.
- `SearchViewModel` — query>2 dallanması.
- `DetailViewModel` — favorite toggle (ekle/sil).

### Öncelik 3 — Orta (data-yükle + fold + effect)
- Home, Profile, Favorites, Category, Leaderboard, Welcome, Summary, Splash.

### Öncelik 3 — Delege use case'ler (düşük değer, kolay coverage)
- 23 pass-through use case — MockK ile repo mock + `Result.success/failure` + argüman iletimi doğrulaması.
  (İsteğe bağlı; asıl değer Öncelik 1-2'de.)

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
Bu spec onaylandıktan sonra `writing-plans` ile detaylı implementasyon planı üretilecek:
1. `resolveSummaryState` refactor'ı (tek dosya).
2. `libs.versions.toml`'a test kütüphaneleri.
3. `quiz.test` convention plugin'i.
4. `MainDispatcherRule` ortak yardımcısı.
5. Öncelik 1 testleri → Öncelik 2 → devamı.
