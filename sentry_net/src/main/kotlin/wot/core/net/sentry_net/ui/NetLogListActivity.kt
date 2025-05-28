package wot.core.net.sentry_net.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import wot.core.net.sentry_net.R
import wot.core.net.sentry_net.model.db.NetLogDatabase
import wot.core.net.sentry_net.model.repository.NetLogRepository

/**
 * 功能说明
 *
 * @author : yangsn
 * @date : 2025/5/28
 */
class NetLogListActivity : AppCompatActivity() {

    private lateinit var adapter: NetLogPagingAdapter
    private lateinit var viewModel: NetLogViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_net_log_list)

        val dao = NetLogDatabase.getInstance(applicationContext).logDao()
        val repository = NetLogRepository(dao)
        viewModel =
            ViewModelProvider(this, NetLogViewModelFactory(repository))[NetLogViewModel::class.java]

        val recycler = findViewById<RecyclerView>(R.id.recyclerView)
        recycler.layoutManager = LinearLayoutManager(this)
        adapter = NetLogPagingAdapter()
        recycler.adapter = adapter

        lifecycleScope.launch {
            viewModel.pagingDataFlow.collectLatest { pagingData ->
                adapter.submitData(pagingData)
            }
        }
    }

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, NetLogListActivity::class.java)
        }
    }
}
