package wot.core.net.sentry_net

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.RequestBody
import okhttp3.Response
import okhttp3.ResponseBody
import okio.Buffer
import wot.core.net.sentry_net.common.encryptor.DefaultLogEncryptor
import wot.core.net.sentry_net.common.encryptor.LogEncryptor
import wot.core.net.sentry_net.model.db.NetLogDatabase
import wot.core.net.sentry_net.model.db.NetLogEntity

/**
 * 网络日志拦截器
 * 1. NetLogInterceptor 添加到你的 OkHttpClient 中即可自动拦截并记录网络请求日志
 * 2. 支持自定义加密器（LogEncryptor接口）
 * 3. 支持自动日志清理（超出 maxLogCount 会自动删除最老日志）
 *
 * @author : yangsn
 * @date : 2025/5/28
 */
class NetLogInterceptor(
    private val context: Context,
    private val encryptor: LogEncryptor = DefaultLogEncryptor,
    private val coroutineScope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO),
    private val maxLogCount: Int = 500
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val requestBodyStr = request.body?.let { bodyToString(it) }
        val startTime = System.currentTimeMillis()
        val response = chain.proceed(request)
        val duration = System.currentTimeMillis() - startTime

        val responseBodyStr = response.body?.string()

        val entity = NetLogEntity(
            url = request.url.toString(),
            method = request.method,
            requestHeaders = request.headers.toString(),
            requestBody = requestBodyStr?.let { encryptor.encrypt(it) },
            responseCode = response.code,
            responseBody = responseBodyStr?.let { encryptor.encrypt(it) },
            responseHeaders = response.headers.toString(),
            timestamp = System.currentTimeMillis()
        )

        coroutineScope.launch {
            val dao = NetLogDatabase.getInstance(context).logDao()
            if (dao.count() >= maxLogCount) dao.deleteOldest(1)
            dao.insert(entity)
        }

        // 重新构建response，避免body被消费问题
        return response.newBuilder()
            .body(ResponseBody.create(response.body?.contentType(), responseBodyStr ?: ""))
            .build()
    }

    private fun bodyToString(body: RequestBody): String {
        return try {
            val buffer = Buffer()
            body.writeTo(buffer)
            buffer.readUtf8()
        } catch (e: Exception) {
            "[body parse error]"
        }
    }
}