package com.canerture.detail.data.repository

import com.canerture.datastore.DataStoreHelper
import com.canerture.detail.domain.repository.DetailRepository
import javax.inject.Inject

class DetailRepositoryImpl @Inject constructor(
    private val dataStore: DataStoreHelper,
) : DetailRepository {

}