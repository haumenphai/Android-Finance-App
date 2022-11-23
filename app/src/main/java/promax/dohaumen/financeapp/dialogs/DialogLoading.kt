package promax.dohaumen.financeapp.dialogs

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import promax.dohaumen.financeapp.R
import promax.dohaumen.financeapp.databinding.DialogLoadingBinding

class DialogLoading(context: Context) {
    val dialog = Dialog(context)
    val b = DialogLoadingBinding.bind(LayoutInflater.from(context).inflate(R.layout.dialog_loading, null))

    private var onClickBtnCancel: () -> Unit = {}

    fun setOnClickBtnCancel(callback: () -> Unit): DialogLoading {
        this.onClickBtnCancel = callback
        return this
    }

    init {
        dialog.setContentView(b.root)
        dialog.setCanceledOnTouchOutside(false)
        b.tvCancel.setOnClickListener {
            onClickBtnCancel()
            dialog.cancel()
        }
    }

    fun disableCancel(): DialogLoading {
        b.tvCancel.visibility = View.GONE
        return this
    }

    fun show(): DialogLoading {
        dialog.show()
        return this
    }

    fun cancel(): DialogLoading {
        dialog.cancel()
        return this
    }
}