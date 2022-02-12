package promax.dohaumen.financeapp.dialogs

import android.app.Dialog
import android.content.Context
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.view.LayoutInflater
import androidx.core.widget.addTextChangedListener
import promax.dohaumen.financeapp.R
import promax.dohaumen.financeapp.databinding.DialogInputOneValueCommonBinding


class DialogInputOneValue(context: Context) {
    val dialog: Dialog
    val b: DialogInputOneValueCommonBinding

    init {
        val view = LayoutInflater.from(context).inflate(
            R.layout.dialog_input_one_value_common,
            null
        )
        b = DialogInputOneValueCommonBinding.bind(view)
        dialog = Dialog(context)
        dialog.setContentView(view)
    }

    fun setTitle(title: String): DialogInputOneValue {
        b.tvTitle.text = title
        return this
    }

    fun setTextBtnSave(text: String): DialogInputOneValue {
        b.btnSave.text = text
        return this
    }

    fun setTextEditName(text: String): DialogInputOneValue {
        b.editName.setText(text)
        return this
    }

    fun setMaxLengthEditName(length: Int): DialogInputOneValue {
        b.editName.filters = arrayOf<InputFilter>(LengthFilter(length))
        return this
    }

    fun setSelectionEditName(i1: Int, i2: Int): DialogInputOneValue {
        b.editName.setSelection(i1, i2)
        return this
    }

    fun setCancelOutSide(bool: Boolean = true): DialogInputOneValue {
        dialog.setCanceledOnTouchOutside(bool)
        return this
    }

    fun setHintEditName(hint: String): DialogInputOneValue {
        b.editName.hint = hint
        return this
    }

    fun setSubTitle(text: String): DialogInputOneValue {
        b.tvSubTitle.text = text
        return this
    }


    fun onchangeEditName(onchange: (text: String, dialog: DialogInputOneValue) -> Unit): DialogInputOneValue {
        b.editName.addTextChangedListener {
            onchange(it.toString(), this)
        }
        return this
    }

    fun setInputTypeEditName(inputType: Int): DialogInputOneValue {
        b.editName.inputType = inputType
        return this
    }

    fun setBtnSaveClick(onClick: (editValue: String, dialog: DialogInputOneValue) -> Unit): DialogInputOneValue {
        b.btnSave.setOnClickListener {
            onClick(b.editName.text.toString(), this)
        }
        return this
    }

    fun show(): DialogInputOneValue {
        dialog.show()
        return this
    }

    fun cancel(): DialogInputOneValue {
        dialog.cancel()
        return this
    }

}