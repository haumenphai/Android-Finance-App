package promax.dohaumen.financeapp.dialogs

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import promax.dohaumen.financeapp.R
import promax.dohaumen.financeapp.databinding.LayoutAddMoneyIoBinding

object DialogAddMoneyIO {

    private var bgAlpha = 0.5f

    fun setBgAlpha(alpha: Float): DialogAddMoneyIO {
        this.bgAlpha = alpha
        return this
    }

    fun show(rootLayout: ViewGroup) {
        val view = LayoutInflater.from(rootLayout.context)
            .inflate(R.layout.layout_add_money_io, rootLayout, false)
        val b = LayoutAddMoneyIoBinding.bind(view)

        b.imgBg.alpha = bgAlpha
        rootLayout.addView(view)


        val anim = AnimationUtils.loadAnimation(view.context, R.anim.anim_show_dialog)
        val animBg = AnimationUtils.loadAnimation(view.context, R.anim.anim_show_bg_dialog)
        b.layoutContent.startAnimation(anim)
        b.imgBg.startAnimation(animBg)

        b.imgBg.setOnClickListener {}
        b.layoutContent.setOnClickListener {}
        b.btnCancel.setOnClickListener {
            rootLayout.removeView(view)
        }
    }
}
