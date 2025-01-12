package com.canerture.quiz.ui

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.canerture.quiz.domain.model.OptionModel
import com.canerture.quiz.domain.model.OptionState
import com.canerture.quiz.domain.model.QuestionModel

class QuizPreviewProvider : PreviewParameterProvider<QuizContract.UiState> {
    override val values: Sequence<QuizContract.UiState>
        get() = sequenceOf(
            QuizContract.UiState(
                isLoading = false,
                id = 1,
                categoryId = 1,
                questions = listOf(
                    QuestionModel(
                        question = "What is the capital of Turkey?",
                        options = listOf(
                            OptionModel("Ankara", OptionState.UNSELECTED),
                            OptionModel("Istanbul", OptionState.UNSELECTED),
                            OptionModel("Izmir", OptionState.UNSELECTED),
                            OptionModel("Bursa", OptionState.UNSELECTED),
                        ),
                        answer = "Ankara",
                    ),
                    QuestionModel(
                        question = "What is the capital of France?",
                        options = listOf(
                            OptionModel("Ankara", OptionState.UNSELECTED),
                            OptionModel("Paris", OptionState.UNSELECTED),
                            OptionModel("Izmir", OptionState.UNSELECTED),
                            OptionModel("Bursa", OptionState.UNSELECTED),
                        ),
                        answer = "Paris",
                    ),
                    QuestionModel(
                        question = "What is the capital of Germany?",
                        options = listOf(
                            OptionModel("Ankara", OptionState.UNSELECTED),
                            OptionModel("Berlin", OptionState.UNSELECTED),
                            OptionModel("Izmir", OptionState.UNSELECTED),
                            OptionModel("Bursa", OptionState.UNSELECTED),
                        ),
                        answer = "Berlin",
                    ),
                    QuestionModel(
                        question = "What is the capital of Italy?",
                        options = listOf(
                            OptionModel("Ankara", OptionState.UNSELECTED),
                            OptionModel("Istanbul", OptionState.UNSELECTED),
                            OptionModel("Rome", OptionState.UNSELECTED),
                            OptionModel("Bursa", OptionState.UNSELECTED),
                        ),
                        answer = "Rome",
                    ),
                ),
                question = QuestionModel(
                    question = "What is the capital of Turkey?",
                    options = listOf(
                        OptionModel("Ankara", OptionState.CORRECT),
                        OptionModel("Istanbul", OptionState.INCORRECT),
                        OptionModel("Izmir", OptionState.UNSELECTED),
                        OptionModel("Bursa", OptionState.UNSELECTED),
                    ),
                    answer = "Ankara",
                ),
                options = listOf(
                    OptionModel("Ankara", OptionState.CORRECT),
                    OptionModel("Istanbul", OptionState.INCORRECT),
                    OptionModel("Izmir", OptionState.UNSELECTED),
                    OptionModel("Bursa", OptionState.UNSELECTED),
                ),
            ),
            QuizContract.UiState(
                isLoading = true,
                id = 1,
                categoryId = 1,
                questions = listOf(
                    QuestionModel(
                        question = "What is the capital of Turkey?",
                        options = listOf(
                            OptionModel("Ankara", OptionState.UNSELECTED),
                            OptionModel("Istanbul", OptionState.UNSELECTED),
                            OptionModel("Izmir", OptionState.UNSELECTED),
                            OptionModel("Bursa", OptionState.UNSELECTED),
                        ),
                        answer = "Ankara",
                    ),
                    QuestionModel(
                        question = "What is the capital of France?",
                        options = listOf(
                            OptionModel("Ankara", OptionState.UNSELECTED),
                            OptionModel("Paris", OptionState.UNSELECTED),
                            OptionModel("Izmir", OptionState.UNSELECTED),
                            OptionModel("Bursa", OptionState.UNSELECTED),
                        ),
                        answer = "Paris",
                    ),
                    QuestionModel(
                        question = "What is the capital of Germany?",
                        options = listOf(
                            OptionModel("Ankara", OptionState.UNSELECTED),
                            OptionModel("Berlin", OptionState.UNSELECTED),
                            OptionModel("Izmir", OptionState.UNSELECTED),
                            OptionModel("Bursa", OptionState.UNSELECTED),
                        ),
                        answer = "Berlin",
                    ),
                    QuestionModel(
                        question = "What is the capital of Italy?",
                        options = listOf(
                            OptionModel("Ankara", OptionState.UNSELECTED),
                            OptionModel("Istanbul", OptionState.UNSELECTED),
                            OptionModel("Rome", OptionState.UNSELECTED),
                            OptionModel("Bursa", OptionState.UNSELECTED),
                        ),
                        answer = "Rome",
                    ),
                ),
                question = QuestionModel(
                    question = "What is the capital of Turkey?",
                    options = listOf(
                        OptionModel("Ankara", OptionState.UNSELECTED),
                        OptionModel("Istanbul", OptionState.UNSELECTED),
                        OptionModel("Izmir", OptionState.UNSELECTED),
                        OptionModel("Bursa", OptionState.UNSELECTED),
                    ),
                    answer = "Ankara",
                ),
                options = listOf(
                    OptionModel("Ankara", OptionState.CORRECT),
                    OptionModel("Istanbul", OptionState.INCORRECT),
                    OptionModel("Izmir", OptionState.UNSELECTED),
                    OptionModel("Bursa", OptionState.UNSELECTED),
                ),
            ),
        )
}