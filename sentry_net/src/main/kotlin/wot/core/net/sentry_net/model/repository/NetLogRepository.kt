package wot.core.net.sentry_net.model.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import wot.core.net.sentry_net.model.db.NetLogDao
import wot.core.net.sentry_net.model.db.NetLogEntity

/**
 * Repository
 *
 * @author : yangsn
 * @date : 2025/5/28
 */
class NetLogRepository(private val dao: NetLogDao) {

    fun getPagedLogs(pageSize: Int = 20): Flow<PagingData<NetLogEntity>> {
        return Pager(
            config = PagingConfig(pageSize = pageSize, enablePlaceholders = false),
            pagingSourceFactory = { dao.pagingSource() }
        ).flow
    }
}