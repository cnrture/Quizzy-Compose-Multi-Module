# Route Patterns (Quizzy)

Real navigation patterns from the codebase. Copy these shapes rather than inventing new ones.

## Package placement

A route + extension lives in `feature/<name>/ui/.../navigation/`. Match the module's existing package — note the historical exceptions:

- Splash route → `com.canerture.ui.navigation` (`splashScreen`, `Splash`)
- Summary route → `com.canerture.result.ui.navigation` (`summaryScreen`, `Summary`)

Do not normalize these; follow the package already in the file.

## No-argument route

```kotlin
@Serializable
data object Login : Screen

fun NavGraphBuilder.loginScreen(
    onNavigateBack: () -> Unit,
    onNavigateRegister: () -> Unit,
    onNavigateHome: () -> Unit,
) {
    composable<Login> {
        val viewModel = hiltViewModel<LoginViewModel>()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        val uiEffect = viewModel.uiEffect
        LoginScreen(uiState, uiEffect, viewModel::onAction, onNavigateBack, onNavigateRegister, onNavigateHome)
    }
}
```

## Single-argument route

```kotlin
@Serializable
data class Detail(val id: Int) : Screen

fun NavGraphBuilder.detailScreen(
    onNavigateBack: () -> Unit,
    onNavigateQuiz: (Int) -> Unit,
) {
    composable<Detail> { /* ... */ }
}
```

## Multi-argument route

```kotlin
@Serializable
data class Category(val id: Int, val name: String, val imageUrl: String) : Screen

@Serializable
data class Summary(val quizId: Int, val correctAnswers: Int, val wrongAnswers: Int, val score: Int) : Screen
```

Navigated as `navController.navigate(Category(id, name, imageUrl))`.

## Reading arguments in the ViewModel

```kotlin
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute

@HiltViewModel
internal class DetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getQuizDetailUseCase: GetQuizDetailUseCase,
) : ViewModel(), MVI<UiState, UiAction, UiEffect> by mvi(UiState()) {
    init {
        val args: Detail = savedStateHandle.toRoute()
        getQuizDetail(args.id)
    }
}
```

## Flow graphs

```kotlin
// LoginFlow — auth screens
@Serializable
object LoginFlow : Screen

internal fun NavGraphBuilder.loginFlowNavigation(navController: NavHostController) {
    navigation<LoginFlow>(Welcome) {
        welcomeScreen(
            onNavigateLogin = { navController.navigate(Login) },
            onNavigateRegister = { navController.navigate(Register) },
            onNavigateHome = { navController.navigateWithPopUpTo(MainFlow, LoginFlow) },
        )
        loginScreen(
            onNavigateBack = { navController.popBackStack() },
            onNavigateRegister = { navController.navigate(Register) },
            onNavigateHome = { navController.navigateWithPopUpTo(MainFlow, LoginFlow) },
        )
        registerScreen(
            onNavigateBack = { navController.popBackStack() },
            onNavigateLogin = { navController.navigateWithPopUpTo(Login, Register) },
        )
    }
}
```

## Top-level host

```kotlin
@Composable
fun QuizAppNavGraph(modifier: Modifier = Modifier, navController: NavHostController) {
    NavHost(navController = navController, startDestination = Splash, modifier = modifier) {
        splashScreen(
            onNavigateWelcome = { navController.navigateWithPopUpTo(LoginFlow, Splash) },
            onNavigateHome = { navController.navigateWithPopUpTo(MainFlow, Splash) },
        )
        loginFlowNavigation(navController)
        mainFlowNavigation(navController)
    }
}
```

## Back-stack rules

- Plain forward: `navController.navigate(Route(args))`
- Back: `navController.popBackStack()`
- Replace/clear across a boundary: `navController.navigateWithPopUpTo(target, popUpInclusiveTarget)`

There is no `savedStateHandle`-based result-passing pattern wired in the project; if two screens must share data, use a shared `core:datasource/*` source instead of passing a result up the back stack.
