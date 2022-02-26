package promax.dohaumen.financeapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import promax.dohaumen.financeapp.databinding.ActivityMainBinding
import promax.dohaumen.financeapp.fragments.HomeFragment
import promax.dohaumen.financeapp.fragments.SettingFragment


class MainActivity : AppCompatActivity() {
    lateinit var b: ActivityMainBinding
    lateinit var homeFragment: HomeFragment
    lateinit var settingFragment: SettingFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)
        controlFragmentWithSaveState()

//        MoneyInOutDB.get.dao().deleteAll()
//        MoneyInOut.getListDemoTest().forEach {
//            MoneyInOutDB.get.dao().insert(it)
//        }
//        for (i in 1..206) {
//            val m = MoneyInOut(i.toString(), MoneyInOut.MoneyInOutType.IN, "1", Currency.BANK)
//            MoneyInOutDB.get.dao().insert(m)
//        }
//        FilterMoneyIODB.get.dao().deleteAll()
//        FilterMoneyIO.getListItemFilter().forEach { FilterMoneyIODB.get.dao().insert(it) }
//        TestFilterMoneyIO.testAll()
    }

    private fun controlFragmentWithSaveState() {
        homeFragment = HomeFragment()
        settingFragment = SettingFragment()

        var activeFragment: Fragment = homeFragment
        supportFragmentManager.beginTransaction().apply {
            add(R.id.container_fragment, homeFragment, "home")
            add(R.id.container_fragment, settingFragment, "list").hide(settingFragment)
        }.commit()

        b.bottomNav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    supportFragmentManager.beginTransaction().hide(activeFragment).show(homeFragment).commit()
                    activeFragment = homeFragment
                }
                R.id.setting -> {
                    supportFragmentManager.beginTransaction().hide(activeFragment).show(settingFragment).commit()
                    activeFragment = settingFragment
                }
            }
            true
        }
    }

}
