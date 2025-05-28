package wot.core.net.sentry_net.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
import wot.core.net.sentry_net.model.db.NetLogEntity
import wot.core.net.sentry_net.model.repository.NetLogRepository

/**
 * ViewModel
 *
 * @author : yangsn
 * @date : 2025/5/28
 */
class NetLogViewModel(private val repository: NetLogRepository) : ViewModel() {
    val pagingDataFlow: Flow<PagingData<NetLogEntity>> =
        repository.getPagedLogs()
            .cachedIn(viewModelScope)
}

class NetLogViewModelFactory(private val repository: NetLogRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NetLogViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NetLogViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
