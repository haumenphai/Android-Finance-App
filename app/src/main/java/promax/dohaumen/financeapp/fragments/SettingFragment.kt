package promax.dohaumen.financeapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import promax.dohaumen.financeapp.MainActivity
import promax.dohaumen.financeapp.databinding.FragmentSettingBinding

class SettingFragment: Fragment() {
    private lateinit var b: FragmentSettingBinding
    private val mainActivity: MainActivity by lazy { activity as MainActivity }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        b = FragmentSettingBinding.inflate(inflater, container, false)
        return b.root
    }

}
