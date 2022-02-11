package promax.dohaumen.financeapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import promax.dohaumen.financeapp.databinding.ActivityMainBinding
import promax.dohaumen.financeapp.db.MoneyInOutDB
import promax.dohaumen.financeapp.fragments.HomeFragment
import promax.dohaumen.financeapp.fragments.SettingFragment

class MainActivity : AppCompatActivity() {
    lateinit var b: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)
        controlFragmentWithSaveState()

        // todo: remove test
        MoneyInOutDB.get.dao().deleteAll()
        MoneyInOutDB.insertDemoData()
    }

    private fun controlFragmentWithSaveState() {
        val homeFragment = HomeFragment()
        val settingFragment = SettingFragment()

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
