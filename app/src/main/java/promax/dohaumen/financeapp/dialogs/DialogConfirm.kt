package promax.dohaumen.financeapp.dialogs

import android.app.AlertDialog
import android.content.Context
import promax.dohaumen.financeapp.R
import promax.dohaumen.financeapp.helper.getStr

class DialogConfirm {

    companion object {
        fun show(context: Context, title: String="", message: String="", onClickOK: () -> Unit) {
            AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK") { i1,i2 ->
                    onClickOK()
                }
                .setNegativeButton(getStr(R.string.cancel)) { i1, i2 ->

                }
                .show()
        }
    }
}