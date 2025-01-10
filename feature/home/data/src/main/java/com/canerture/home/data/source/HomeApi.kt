package com.canerture.home.data.source

import com.canerture.home.data.common.Constants.CATEGORIES
import com.canerture.home.data.model.CategoryResponse
import com.canerture.network.model.BaseResponse
import retrofit2.http.GET

interface HomeApi {
    @GET(CATEGORIES)
    suspend fun getCategories(): BaseResponse<List<CategoryResponse>?>
}