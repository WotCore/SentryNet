package wot.core.net.sentry_net.model.db

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * 实体类
 *
 * @author : yangsn
 * @date : 2025/5/28
 */
@Entity(
    tableName = "net_logs",
    indices = [Index(value = ["url", "timestamp"])]
)
data class NetLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val url: String,
    val method: String,
    val requestHeaders: String?,
    val requestBody: String?,
    val responseCode: Int,
    val responseBody: String?,
    val responseHeaders: String?,
    val timestamp: Long = System.currentTimeMillis()
)
