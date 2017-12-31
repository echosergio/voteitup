package upm.dam.voteitup.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import upm.dam.voteitup.R

class AnswerListAdapter(private val context: Context, private val listData: MutableList<TextView>) : BaseAdapter() {
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
            convertView = layoutInflater.inflate(R.layout.listview_answers, null)

            holder = ViewHolder()
            holder.editAnswerView = convertView!!.findViewById<TextView>(R.id.text_view_list_item)
            holder.deleteBtnAnswer = convertView.findViewById<Button>(R.id.btn_delete)

            convertView.setTag(holder)
        } else {
            holder = convertView.getTag() as ViewHolder
        }

        val answer = this.listData[position].text
        if(!answer.isNullOrBlank())
            holder.editAnswerView!!.setText(answer)
        Log.d("Debug","Answer is: " + answer + "in position:"+ position)

        holder.editAnswerView!!.onFocusChangeListener = View.OnFocusChangeListener{ view, hasFocus ->
            if (!hasFocus) {
                if (!holder.editAnswerView!!.isInEditMode) {
                    val text = holder.editAnswerView!!.text
                    this.listData[position].setText(text.toString(),TextView.BufferType.EDITABLE)
                    Log.d("Debug", "Text $position is: $text")
                }
            }


        }
        holder.deleteBtnAnswer!!.setOnClickListener {
            this.listData.removeAt(position)
            this.notifyDataSetChanged()
        }
        return convertView
    }

    internal class ViewHolder {
        var editAnswerView: TextView? = null
        var deleteBtnAnswer: Button? = null
    }


}