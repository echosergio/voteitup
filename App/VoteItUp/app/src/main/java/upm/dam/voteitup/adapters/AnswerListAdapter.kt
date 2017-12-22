package upm.dam.voteitup.adapters

import android.content.Context
import android.support.v4.content.res.ResourcesCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_create_poll.*
import upm.dam.voteitup.R
import upm.dam.voteitup.R.id.answerList
import upm.dam.voteitup.entities.Poll

class AnswerListAdapter(private val context: Context, private val listData: MutableList<EditText>) : BaseAdapter() {
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
            convertView = layoutInflater.inflate(R.layout.list_view_answers, null)

            holder = ViewHolder()
            holder.editAnswerView = convertView!!.findViewById<EditText>(R.id.editText1)
            convertView!!.setTag(holder)
        } else {
            holder = convertView!!.getTag() as ViewHolder
        }

        val answer = this.listData[position].text
        holder.editAnswerView!!.setText(answer)


        holder.editAnswerView!!.onFocusChangeListener = View.OnFocusChangeListener{ view, hasFocus ->
            if (!hasFocus) {
                if (!holder.editAnswerView!!.isInEditMode) {
                    var text = holder.editAnswerView!!.text
                    this.listData[position].setText(text.toString(),TextView.BufferType.EDITABLE)
                }
            }

        }
        return convertView
    }

    internal class ViewHolder {
        var editAnswerView: EditText? = null
    }


}