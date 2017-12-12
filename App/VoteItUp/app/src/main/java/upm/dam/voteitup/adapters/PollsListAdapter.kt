package upm.dam.voteitup.adapters

import android.content.Context
import android.support.v4.content.res.ResourcesCompat
import android.widget.TextView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.BaseAdapter
import upm.dam.voteitup.R
import upm.dam.voteitup.entities.Poll

class PollsListAdapter(private val context: Context, private val listData: List<Poll>) : BaseAdapter() {
    private val layoutInflater: LayoutInflater

    init {
        layoutInflater = LayoutInflater.from(context)
    }

    override fun getCount(): Int {
        return listData.size
    }

    override fun getItem(position: Int): Any {
        return listData[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val holder: ViewHolder
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_polls_search, null)

            holder = ViewHolder()
            holder.pollTextView = convertView!!.findViewById<TextView>(R.id.textView_pollName)
            holder.pollVotesView = convertView!!.findViewById(R.id.textView_votes)

            convertView!!.setTag(holder)
        } else {
            holder = convertView!!.getTag() as ViewHolder
        }

        val poll = this.listData[position]
        holder.pollTextView!!.text = poll.text
        holder.pollVotesView!!.text = poll.Choices.orEmpty().sumBy { it.votes }.toString() + " votos"

        holder.pollTextView!!.typeface = ResourcesCompat.getFont(context, R.font.roboto_light)

        return convertView
    }

    internal class ViewHolder {
        var pollTextView: TextView? = null
        var pollVotesView: TextView? = null
    }
}
