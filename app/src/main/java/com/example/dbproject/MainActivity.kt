package com.example.dbproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.dbproject.databinding.ActivityMainBinding
import com.example.dbproject.fragment.HomeFragment

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)

        var f = HomeFragment()
        supportFragmentManager.beginTransaction().add(R.id.main_content,f).commit()



        binding.expandableBottomBar.onItemSelectedListener = {view, menuItem, isSelect->
            // https://github.com/st235/ExpandableBottomBar

            when(menuItem.id){
                R.id.nav_home_btn->{
                    println("home")

                    var f = HomeFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.main_content,f).commit()

                }
                R.id.nav_review_btn->{
                    println("review")
                }
                R.id.nav_rank_btn->{
                    println("rank")
                }
                R.id.nav_account_btn->{
                    println("account")
                }

            }

        }
    }
}