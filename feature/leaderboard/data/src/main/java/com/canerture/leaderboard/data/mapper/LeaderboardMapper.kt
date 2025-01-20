package com.canerture.leaderboard.data.mapper

import com.canerture.core.common.orFalse
import com.canerture.core.common.orZero
import com.canerture.leaderboard.data.model.LeaderboardResponse
import com.canerture.leaderboard.domain.model.BoardModel
import com.canerture.leaderboard.domain.model.LeaderboardModel

internal fun List<LeaderboardResponse>?.toModel(): LeaderboardModel {
    val currentUser = this?.find { it.isYourself.orFalse() }
    val currentList = this?.toMutableList()
    val firstUser = currentList?.getOrNull(0)
    val secondUser = currentList?.getOrNull(1)
    val thirdUser = currentList?.getOrNull(2)
    currentList?.removeAll(listOf(firstUser, secondUser, thirdUser).toSet())
    if (currentUser != null) currentList?.remove(currentUser)
    return LeaderboardModel(
        userList = currentList.toModelList(),
        currentUser = currentUser?.toModel(),
        firstUser = firstUser.toModel(),
        secondUser = secondUser.toModel(),
        thirdUser = thirdUser.toModel(),
    )
}

internal fun List<LeaderboardResponse>?.toModelList(): List<BoardModel> {
    return this?.map { it.toModel() }.orEmpty()
}

internal fun LeaderboardResponse?.toModel(): BoardModel {
    return BoardModel(
        username = this?.username.orEmpty(),
        avatarUrl = this?.avatarUrl.orEmpty(),
        score = this?.score.orZero().toString(),
        rank = this?.rank.orZero().toString(),
    )
}