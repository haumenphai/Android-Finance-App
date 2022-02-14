package promax.dohaumen.financeapp.fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import promax.dohaumen.financeapp.MainActivity
import promax.dohaumen.financeapp.R
import promax.dohaumen.financeapp.databinding.DialogMoneyTypeAddAndEditBinding
import promax.dohaumen.financeapp.databinding.FragmentSettingBinding
import promax.dohaumen.financeapp.datas.AppData
import promax.dohaumen.financeapp.dialogs.DialogInputOneValue
import promax.dohaumen.financeapp.dialogs.DialogShowMoneyHistory
import promax.dohaumen.financeapp.helper.formatNumber
import promax.dohaumen.financeapp.helper.getStr
import promax.dohaumen.financeapp.helper.isNumeric
import promax.dohaumen.financeapp.models.*

@SuppressLint("SetTextI18n")
class SettingFragment: Fragment() {
    private lateinit var b: FragmentSettingBinding
    private val mainActivity: MainActivity by lazy { activity as MainActivity }

    private var currencyAdapter = CurrencyAdapter()
    private var moneyTypeAdapter = MoneyTypeAdapter()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        b = FragmentSettingBinding.inflate(inflater, container, false)
        setupViewTotalMoney()
        setupViewCurrency()
        setUpViewMoneyType()
        return b.root
    }

    private fun loadDataMoneyToTextView() {
        // tính case money thay đổi ở homefragment nhưng ở đây ko cập nhật

        AppData.getTotalMoneyFormatedLiveData().observeForever {
            b.tvTotalMoneyValue.text = it
        }
        AppData.getTotalMoneyInBanksFormatedLiveData().observeForever {
            b.tvTotalMoneyInBanksValue.text = it
        }
        AppData.getTotalCashFormatedLiveData().observeForever {
            b.tvTotalCashValue.text = it
        }
        AppData.getMoneyUnitLiveData().observeForever {
            b.tvMoneyUnitValue.text = it
        }
        AppData.getMoneyFormatLiveData().observeForever {
            b.tvMoneyFormatValue.text = it
        }
    }

    private fun setupViewTotalMoney() {
        loadDataMoneyToTextView()

        b.btnSetTotalMoneyInBanks.setOnClickListener {
            DialogInputOneValue(mainActivity)
                .setTitle(getStr(R.string.set_total_money_in_banks_title))
                .setTextBtnSave(getStr(R.string.save))
                .setInputTypeEditName(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)
                .setMaxLengthEditName(18)
                .onchangeEditName { text, dialog ->
                    dialog.setSubTitle("${text.formatNumber(AppData.getMoneyFormat())}  ${AppData.getMoneyUnit()}")
                }
                .setBtnSaveClick { text, dialog ->
                    if (!text.trim().isNumeric()) {
                        Toast.makeText(context, R.string.please_enter_a_number, Toast.LENGTH_SHORT).show()
                        return@setBtnSaveClick
                    }
                    if (text.trim() != "") {
                        AppData.setTotalMoneyInBanks(text)
                        TotalMoneyInBanksHistoryDB.get.dao()
                            .insert(TotalMoneyInBanksHistory(AppData.getTotalMoneyInBanksFormated()))
                        dialog.dialog.cancel()
                    }
                }.show()
        }
        b.btnSetTotalCash.setOnClickListener {
            DialogInputOneValue(mainActivity)
                .setTitle(getStr(R.string.set_total_cash_title))
                .setTextBtnSave(getStr(R.string.save))
                .setMaxLengthEditName(18)
                .setInputTypeEditName(InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)
                .onchangeEditName { text, dialog ->
                    dialog.setSubTitle("${text.formatNumber(AppData.getMoneyFormat())}  ${AppData.getMoneyUnit()}")
                }
                .setBtnSaveClick { text, dialog ->
                    if (!text.trim().isNumeric()) {
                        Toast.makeText(context, R.string.please_enter_a_number, Toast.LENGTH_SHORT).show()
                        return@setBtnSaveClick
                    }
                    if (text.trim() != "") {
                        AppData.setTotalCash(text)
                        TotalCashHistoryDB.get.dao().insert(TotalCashHistory(AppData.getTotalCashFormated()))
                        dialog.dialog.cancel()
                    }
                }.show()
        }
        b.btnSetMoneyUnit.setOnClickListener {
            DialogInputOneValue(mainActivity)
                .setTitle(getStr(R.string.set_money_unit_title))
                .setTextBtnSave(getStr(R.string.save))
                .setMaxLengthEditName(10)
                .setInputTypeEditName(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS)
                .setTextEditName(AppData.getMoneyUnit())
                .setSelectionEditName(0, AppData.getMoneyUnit().length)
                .setBtnSaveClick { text, dialog ->
                    if (text.trim() != "") {
                        AppData.setMoneyUnit(text)
                        dialog.dialog.cancel()
                        mainActivity.homeFragment.notifyMoneyUnitOrMoneyFormatChanged()
                    }
                }
                .show()
        }
        b.btnSetMoneyFormat.setOnClickListener {
            DialogInputOneValue(mainActivity)
                .setTitle(getStr(R.string.set_money_format))
                .setTextBtnSave(getStr(R.string.save))
                .setMaxLengthEditName(1)
                .setInputTypeEditName(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS)
                .setTextEditName(AppData.getMoneyFormat().toString())
                .setSelectionEditName(0, AppData.getMoneyFormat().toString().length)
                .setBtnSaveClick { text, dialog ->
                    if (text != "") {
                        AppData.setMoneyFormat(text[0])
                        dialog.dialog.cancel()
                        mainActivity.homeFragment.notifyMoneyUnitOrMoneyFormatChanged()
                    }
                }
                .show()
        }
        b.btnViewHistory.setOnClickListener {
            DialogShowMoneyHistory().show(b.root)
        }
    }

    private fun setupViewCurrency() {
        val dialog = DialogInputOneValue(mainActivity)

        b.btnAddCurrecy.setOnClickListener {
            dialog.setTextEditName("")
                .setTitle(mainActivity.getString(R.string.add_currency))
                .setTextBtnSave(mainActivity.getString(R.string.add))
                .setBtnSaveClick { text, dialog ->
                    val name = text.trim()
                    if (name == "") {
                        Toast.makeText(context, R.string.message_name_is_empty, Toast.LENGTH_SHORT).show()
                    } else {
                        CurrencyDB.get.dao().insert(Currency(name))
                        dialog.cancel()
                    }
                }.show()
        }


        b.recyclerViewCurrecy.layoutManager = LinearLayoutManager(mainActivity)
        b.recyclerViewCurrecy.adapter = currencyAdapter
        CurrencyDB.get.dao().getLiveData().observeForever {
            currencyAdapter.setList(it)
        }

        // edit
        currencyAdapter.onClickItem = { currency ->
            dialog.setTitle(mainActivity.getString(R.string.edit))
                .setTextBtnSave(mainActivity.getString(R.string.save))
                .setTextEditName(currency.name)
                .setSelectionEditName(0, currency.name.length)
                .setBtnSaveClick { text, dialog ->
                    val name = text.trim()
                    if (name == "") {
                        Toast.makeText(context, R.string.message_name_is_empty, Toast.LENGTH_SHORT).show()
                    } else {
                        currency.name = name
                        CurrencyDB.get.dao().update(currency)
                        dialog.cancel()
                    }

                }.show()

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
