package upm.dam.voteitup.adapters

import android.content.Context
import android.support.v4.content.res.ResourcesCompat
import android.widget.TextView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.BaseAdapter
import upm.dam.voteitup.R
import upm.dam.voteitup.entities.UserActivity

class UserActivityListAdapter(private val context: Context, private val listData: List<UserActivity>) : BaseAdapter() {
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
            convertView = layoutInflater.inflate(R.layout.listview_useractivity, null)

            holder = ViewHolder()
            holder.userActivityTextView = convertView!!.findViewById<TextView>(R.id.userActivityTextView)

            convertView.setTag(holder)
        } else {
            holder = convertView.getTag() as ViewHolder
        }

        val userActivity = this.listData[position]
        holder.userActivityTextView!!.text = "Votó " + userActivity.choice + " en " + userActivity.text
        holder.userActivityTextView!!.typeface = ResourcesCompat.getFont(context, R.font.roboto_light)

        return convertView
    }

    internal class ViewHolder {
        var userActivityTextView: TextView? = null
    }
}
