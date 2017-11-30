package upm.dam.voteitup.adapters

import android.content.Context
import android.util.Log
import android.widget.TextView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.BaseAdapter
import android.widget.ImageView
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
            //holder.flagView = convertView!!.findViewById<ImageView>(R.id.imageView_flag)
            holder.countryNameView = convertView!!.findViewById<TextView>(R.id.textView_countryName)
            holder.populationView = convertView!!.findViewById(R.id.textView_population)
            convertView!!.setTag(holder)
        } else {
            holder = convertView!!.getTag() as ViewHolder
        }

        val poll = this.listData[position]
        holder.countryNameView!!.text = poll.text
        holder.populationView!!.text = poll.creationDate

        //val imageId = this.getMipmapResIdByName(poll.image)

        //holder.flagView!!.setImageResource(imageId)

        return convertView
    }

    // Find Image ID corresponding to the name of the image (in the directory mipmap).
    fun getMipmapResIdByName(resName: String): Int {
        val pkgName = context.getPackageName()
        // Return 0 if not found.
        val resID = context.getResources().getIdentifier(resName, "mipmap", pkgName)
        Log.i("CustomListView", "Res Name: $resName==> Res ID = $resID")
        return resID
    }

    internal class ViewHolder {
        var flagView: ImageView? = null
        var countryNameView: TextView? = null
        var populationView: TextView? = null
    }
}
