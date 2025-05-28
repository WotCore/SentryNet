package wot.core.net.sentry_net.model.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * Dao 接口
 *
 * @author : yangsn
 * @date : 2025/5/28
 */
@Dao
interface NetLogDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(log: NetLogEntity)

    @Query("SELECT COUNT(*) FROM net_logs")
    suspend fun count(): Int

    @Query("DELETE FROM net_logs WHERE id IN (SELECT id FROM net_logs ORDER BY timestamp ASC LIMIT :limit)")
    suspend fun deleteOldest(limit: Int)

    @Query("SELECT * FROM net_logs ORDER BY timestamp DESC")
    fun pagingSource(): PagingSource<Int, NetLogEntity>

    // 可扩展其他查询方法，比如筛选等
}