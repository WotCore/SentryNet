package wot.core.net.sentry_net.model.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * 数据库
 *
 * @author : yangsn
 * @date : 2025/5/28
 */
@Database(entities = [NetLogEntity::class], version = 1, exportSchema = false)
abstract class NetLogDatabase : RoomDatabase() {
    abstract fun logDao(): NetLogDao

    companion object {
        @Volatile
        private var INSTANCE: NetLogDatabase? = null

        fun getInstance(context: Context): NetLogDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    NetLogDatabase::class.java,
                    "net_log.db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}