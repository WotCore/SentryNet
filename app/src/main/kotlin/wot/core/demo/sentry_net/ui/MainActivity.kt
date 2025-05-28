package wot.core.demo.sentry_net.ui

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import wot.core.demo.sentry_net.R
import wot.core.demo.sentry_net.databinding.MainActivityBinding
import wot.core.net.sentry_net.NetLogInterceptor
import wot.core.net.sentry_net.model.db.NetLogDatabase
import wot.core.net.sentry_net.ui.NetLogListActivity

/**
 * 功能说明
 *
 * @author : yangsn
 * @date : 2025/5/27
 */
class MainActivity : AppCompatActivity() {

    private val client by lazy {
        OkHttpClient.Builder()
            .addInterceptor(
                NetLogInterceptor(context = this)
            )
            .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding =
            DataBindingUtil.setContentView<MainActivityBinding>(this, R.layout.main_activity)
        setContentView(binding.root)

        binding.addData.setOnClickListener { testRequest() }

        binding.goNetLogListActivity.setOnClickListener {
            startActivity(NetLogListActivity.createIntent(this))
        }
    }

    private fun testRequest() {
        lifecycleScope.launch {
            val request = Request.Builder()
                .url("https://httpbin.org/get")
                .get()
                .build()

            withContext(Dispatchers.IO) {
                val response = client.newCall(request).execute()
                Log.d("NetLogTest", "Response: ${response.code}")
            }

            delay(1000) // 等待写入数据库

            val logs = NetLogDatabase.getInstance(this@MainActivity)
                .logDao()
                .getAll()

            logs.forEach {
                Log.d("NetLogTest", "Logged: ${it.url} code=${it.responseCode}")
            }

            Toast.makeText(this@MainActivity, "日志条数：${logs.size}", Toast.LENGTH_SHORT).show()
        }
    }
}