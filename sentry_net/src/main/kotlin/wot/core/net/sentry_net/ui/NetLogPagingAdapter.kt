package wot.core.net.sentry_net.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import wot.core.net.sentry_net.R
import wot.core.net.sentry_net.model.db.NetLogEntity

/**
 * Adapter
 *
 * @author : yangsn
 * @date : 2025/5/28
 */
class NetLogPagingAdapter :
    PagingDataAdapter<NetLogEntity, NetLogPagingAdapter.VH>(NetLogDiffCallback()) {

    class VH(view: View) : RecyclerView.ViewHolder(view) {
        val url: TextView = view.findViewById(R.id.textUrl)
        val code: TextView = view.findViewById(R.id.textCode)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_net_log, parent, false)
        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val log = getItem(position)
        if (log != null) {
            holder.url.text = log.url
            holder.code.text = log.responseCode.toString()
        }
    }
}

class NetLogDiffCallback : DiffUtil.ItemCallback<NetLogEntity>() {
    override fun areItemsTheSame(oldItem: NetLogEntity, newItem: NetLogEntity): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: NetLogEntity, newItem: NetLogEntity): Boolean =
        oldItem == newItem
}
