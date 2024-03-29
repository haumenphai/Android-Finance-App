package promax.dohaumen.financeapp.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import promax.dohaumen.financeapp.R
import promax.dohaumen.financeapp.databinding.ItemMoneyInOutBinding
import promax.dohaumen.financeapp.models.MoneyInOut
import promax.dohaumen.financeapp.models.MoneyInOut.MoneyInOutType
import kotlin.math.roundToInt

class MoneyInOutAdapter : RecyclerView.Adapter<MoneyInOutAdapter.MoneyInoutHolder>() {
    private var list: List<MoneyInOut> = mutableListOf()
    private lateinit var context: Context

    lateinit var onClickItem: (moneyInOut: MoneyInOut) -> Unit
    lateinit var onLongClickItem: (moneyInOut: MoneyInOut) -> Unit

    fun setList(list: List<MoneyInOut>) {
        this.list = list
        this.notifyDataSetChanged()
    }

    fun getList() = list

    fun setCheckAll() {
        list.forEach { it.isChecked = true }
        notifyDataSetChanged()
    }

    fun unCheckAll() {
        list.forEach { it.isChecked = false }
        notifyDataSetChanged()
    }

    fun getListChecked() = list.filter { it.isChecked }

    fun setSwapCheckItem(moneyIO: MoneyInOut) {
        moneyIO.isChecked = !moneyIO.isChecked
        notifyItemChanged(list.indexOf(moneyIO))
    }

    inner class MoneyInoutHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val b = ItemMoneyInOutBinding.bind(itemView)
        init {
            b.bgItem.setOnClickListener {
                onClickItem(list[layoutPosition])
            }
            b.bgItem.setOnLongClickListener {
                onLongClickItem(list[layoutPosition])
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoneyInoutHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_money_in_out, parent, false)
        this.context = parent.context
        return MoneyInoutHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MoneyInoutHolder, position: Int) {
        val currentMoneyInOut = list[position]

        when {
            currentMoneyInOut.isChecked -> {
                holder.b.bgItem.setBackgroundColor(ContextCompat.getColor(context, R.color.yellow_500))
                holder.b.imgIschecked.visibility = View.VISIBLE
            }
            currentMoneyInOut.type == MoneyInOutType.IN -> {
                holder.b.bgItem.setBackgroundResource(R.drawable.ripple_item_money_in)
                holder.b.imgIschecked.visibility = View.INVISIBLE
            }
            else -> {
                holder.b.bgItem.setBackgroundResource(R.drawable.ripple_item_money_out)
                holder.b.imgIschecked.visibility = View.INVISIBLE
            }
        }

        holder.b.tvSequence.text = "${position+1}"
        holder.b.tvName.text = currentMoneyInOut.name
        holder.b.tvAmount.text = "${context.getString(R.string.amount)}: ${currentMoneyInOut.amount.roundToInt()}"
        holder.b.tvTime.text = currentMoneyInOut.datetime
    }

    override fun getItemCount(): Int = list.size


}
