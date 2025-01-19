package com.canerture.quiz.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.canerture.feature.quiz.ui.R
import com.canerture.quiz.domain.model.OptionModel
import com.canerture.quiz.domain.model.OptionState
import com.canerture.quiz.ui.common.rememberShake
import com.canerture.quiz.ui.common.shake
import com.canerture.quiz.ui.common.toBgColor
import com.canerture.quiz.ui.common.toIcon
import com.canerture.quiz.ui.common.toIconColor
import com.canerture.ui.components.QuizAppText
import com.canerture.ui.extensions.boldBorder
import com.canerture.ui.extensions.conditional
import com.canerture.ui.theme.QuizAppTheme

@Composable
fun AnswerButton(
    optionModel: OptionModel,
    isSelectable: Boolean,
    onOptionSelect: (OptionModel) -> Unit,
) {
    val shakeAnimation = rememberShake()

    LaunchedEffect(key1 = optionModel.state) {
        if (optionModel.state == OptionState.INCORRECT) {
            shakeAnimation.shake()
        }
    }

    val bgColor = optionModel.state.toBgColor()
    val icon = optionModel.state.toIcon()
    val iconColor = optionModel.state.toIconColor()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .boldBorder(
                radius = 100,
                color = iconColor,
            )
            .background(
                color = bgColor,
                shape = CircleShape,
            )
            .clip(CircleShape)
            .conditional(isSelectable) {
                clickable { onOptionSelect(optionModel) }
            }
            .conditional(optionModel.state == OptionState.INCORRECT) {
                shake(shakeAnimation)
            }
            .padding(
                vertical = 16.dp,
                horizontal = 24.dp,
            ),
    ) {
        QuizAppText(
            text = optionModel.option,
            style = QuizAppTheme.typography.heading5,
        )
        Spacer(modifier = Modifier.weight(1f))
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = stringResource(R.string.option),
                tint = iconColor,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AnswerButtonPreview() {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        AnswerButton(
            optionModel = OptionModel(
                option = "Option A",
                state = OptionState.UNSELECTED,
            ),
            isSelectable = true,
            onOptionSelect = {},
        )
        AnswerButton(
            optionModel = OptionModel(
                option = "Option A",
                state = OptionState.CORRECT,
            ),
            isSelectable = true,
            onOptionSelect = {},
        )
        AnswerButton(
            optionModel = OptionModel(
                option = "Option A",
                state = OptionState.INCORRECT,
            ),
            isSelectable = true,
            onOptionSelect = {},
        )
    }
}