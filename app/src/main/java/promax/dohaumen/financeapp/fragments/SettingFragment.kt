package promax.dohaumen.financeapp.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import promax.dohaumen.financeapp.MainActivity
import promax.dohaumen.financeapp.R
import promax.dohaumen.financeapp.databinding.DialogAddCurrencyBinding
import promax.dohaumen.financeapp.databinding.DialogMoneyTypeAddAndEditBinding
import promax.dohaumen.financeapp.databinding.FragmentSettingBinding
import promax.dohaumen.financeapp.models.*

class SettingFragment: Fragment() {
    private lateinit var b: FragmentSettingBinding
    private val mainActivity: MainActivity by lazy { activity as MainActivity }

    private var currencyAdapter = CurrencyAdapter()
    private var moneyTypeAdapter = MoneyTypeAdapter()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        b = FragmentSettingBinding.inflate(inflater, container, false)
        setupViewTotalMoney()
        setupViewCurrency()
        setUpViewMoneyType()
        return b.root
    }

    private fun setupViewTotalMoney() {
        b.btnSetTotalMoneyInBanks.setOnClickListener {

        }
        b.btnSetTotalCash.setOnClickListener {

        }
        b.btnSetMoneyUnit.setOnClickListener {

        }
        b.btnSetMoneyFormat.setOnClickListener {

        }
        b.btnViewHistory.setOnClickListener {

        }
    }

    private fun setupViewCurrency() {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_add_currency, null)
        val b1 = DialogAddCurrencyBinding.bind(view)
        val dialog = Dialog(mainActivity)
        dialog.setContentView(view)

        b.btnAddCurrecy.setOnClickListener {
            b1.editName.setText("")
            b1.tvTitle.text = mainActivity.getString(R.string.add_currency)
            b1.btnSave.text = mainActivity.getString(R.string.add)

            b1.btnSave.setOnClickListener {
                val name = b1.editName.text.toString().trim()
                if (name == "") {
                    Toast.makeText(context, R.string.message_name_is_empty, Toast.LENGTH_SHORT).show()
                } else {
                    CurrencyDB.get.dao().insert(Currency(name))
                    dialog.cancel()
                }
            }
            dialog.show()
        }


        b.recyclerViewCurrecy.layoutManager = LinearLayoutManager(mainActivity)
        b.recyclerViewCurrecy.adapter = currencyAdapter
        CurrencyDB.get.dao().getLiveData().observeForever {
            currencyAdapter.setList(it)
        }

        // edit
        currencyAdapter.onClickItem = { currency ->
            b1.tvTitle.text = mainActivity.getString(R.string.edit)
            b1.btnSave.text = mainActivity.getString(R.string.save)
            b1.editName.setText(currency.name)
            b1.editName.setSelection(currency.name.length)

            b1.btnSave.setOnClickListener {
                val name = b1.editName.text.toString().trim()
                if (name == "") {
                    Toast.makeText(context, R.string.message_name_is_empty, Toast.LENGTH_SHORT).show()
                } else {
                    currency.name = name
                    CurrencyDB.get.dao().update(currency)
                    dialog.cancel()
                }
            }
            dialog.show()
        }



    }

    private fun setUpViewMoneyType() {
        val b1 = DialogMoneyTypeAddAndEditBinding.bind(
            LayoutInflater.from(context).inflate(R.layout.dialog_money_type_add_and_edit, null)
        )
        val dialog = Dialog(mainActivity)
        dialog.setContentView(b1.root)

        b.btnAddMoneyType.setOnClickListener {
            b1.apply {
                this.editName.setText("")
                this.radioTypeMoneyIn.isChecked = true
                this.tvTitle.text = mainActivity.getString(R.string.add_money_type_lable)
                this.btnSave.text = mainActivity.getString(R.string.add)
            }

            b1.btnSave.setOnClickListener {
                val name = b1.editName.text.toString().trim()
                val typeInOut = if (b1.radioTypeMoneyIn.isChecked) {
                    MoneyInOut.MoneyInOutType.IN
                } else {
                    MoneyInOut.MoneyInOutType.OUT
                }

                if (name == "") {
                    Toast.makeText(context, R.string.message_name_is_empty, Toast.LENGTH_SHORT).show()
                } else {
                    MoneyTypeDB.get.dao().insert(MoneyType(name, typeInOut))
                    dialog.cancel()
                }
            }

            dialog.show()
        }


        b.recyclerViewMoneyType.layoutManager = LinearLayoutManager(mainActivity)
        b.recyclerViewMoneyType.adapter = moneyTypeAdapter
        MoneyTypeDB.get.dao().getLiveData().observeForever {
            moneyTypeAdapter.setList(it.toMutableList())
        }

        // edit
        moneyTypeAdapter.onClickItem = { moneyType ->
            b1.apply {
                this.tvTitle.text = mainActivity.getString(R.string.edit)
                this.btnSave.text = mainActivity.getString(R.string.save)
                this.editName.setText(moneyType.name)
                this.editName.setSelection(moneyType.name.length)
            }

            if (moneyType.type == MoneyInOut.MoneyInOutType.IN) {
                b1.radioTypeMoneyIn.isChecked = true
            } else {
                b1.radioTypeMoneyOut.isChecked = true
            }

            b1.btnSave.setOnClickListener {
                val name = b1.editName.text.toString().trim()
                val typeInOut = if (b1.radioTypeMoneyIn.isChecked) {
                    MoneyInOut.MoneyInOutType.IN
                } else {
                    MoneyInOut.MoneyInOutType.OUT
                }

                if (name == "") {
                    Toast.makeText(context, R.string.message_name_is_empty, Toast.LENGTH_SHORT).show()
                } else {
                    moneyType.apply {
                        this.name = name
                        this.type = typeInOut
                    }
                    MoneyTypeDB.get.dao().update(moneyType)
                    dialog.cancel()
                }
            }
            dialog.show()

        }
    }
}
