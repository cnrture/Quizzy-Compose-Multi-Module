package com.canerture.search.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canerture.search.domain.model.QuizModel
import com.canerture.search.domain.usecase.GetQuizzesUseCase
import com.canerture.search.ui.SearchContract.UiAction
import com.canerture.search.ui.SearchContract.UiEffect
import com.canerture.search.ui.SearchContract.UiState
import com.canerture.ui.delegate.mvi.MVI
import com.canerture.ui.delegate.mvi.mvi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@OptIn(FlowPreview::class)
@HiltViewModel
internal class SearchViewModel @Inject constructor(
    private val getQuizzesUseCase: GetQuizzesUseCase,
) : ViewModel(),
    MVI<UiState, UiAction, UiEffect> by mvi(UiState()) {

    private val queryFlow = MutableStateFlow("")

    init {
        getInitialQuizList()
        observeQuery()
    }

    override fun onAction(uiAction: UiAction) {
        when (uiAction) {
            UiAction.OnBackClick -> viewModelScope.launch { emitUiEffect(UiEffect.NavigateBack) }
            is UiAction.OnQuizClick -> viewModelScope.launch { emitUiEffect(UiEffect.NavigateDetail(uiAction.id)) }
            is UiAction.OnQueryChange -> {
                updateUiState { copy(query = uiAction.query) }
                queryFlow.value = uiAction.query
            }
        }
    }

    private fun getInitialQuizList() = viewModelScope.launch {
        updateUiState { copy(isLoading = true) }
        getQuizzesUseCase().fold(
            onSuccess = { updateUiState { copy(initialQuizList = it, quizList = it, isLoading = false) } },
            onFailure = { updateUiState { copy(initialQuizList = emptyList(), quizList = emptyList(), isLoading = false) } },
        )
    }

    private fun observeQuery() = viewModelScope.launch {
        queryFlow
            .debounce(DEBOUNCE_MILLIS.milliseconds)
            .distinctUntilChanged()
            .collect { query -> updateUiState { copy(quizList = filterQuizzes(query)) } }
    }

    private fun filterQuizzes(query: String): List<QuizModel> {
        val trimmed = query.trim()
        if (trimmed.isEmpty()) return currentUiState.initialQuizList
        return currentUiState.initialQuizList.filter {
            it.name.contains(trimmed, ignoreCase = true) ||
                it.category.contains(trimmed, ignoreCase = true)
        }
    }

    companion object {
        private const val DEBOUNCE_MILLIS = 300L
    }
}
