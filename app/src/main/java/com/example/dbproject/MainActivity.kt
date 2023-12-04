package com.example.dbproject

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RatingBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import com.example.dbproject.databinding.ActivityMainBinding
import com.example.dbproject.databinding.DialogDetailBinding
import com.example.dbproject.fragment.AccountFragment
import com.example.dbproject.fragment.DialogFragment
import com.example.dbproject.fragment.HomeFragment
import com.example.dbproject.fragment.RankFragment
import com.example.dbproject.fragment.ReviewFragment
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)

        var f = HomeFragment()
        supportFragmentManager.beginTransaction().add(R.id.main_content,f).commit() // 메인 최초 화면 설정

        MotionToast.createColorToast(this,
            "SUCCESS",
            "로그인 성공!",
            MotionToastStyle.SUCCESS,
            MotionToast.GRAVITY_BOTTOM,
            MotionToast.LONG_DURATION,
            ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))

        binding.expandableBottomBar.onItemSelectedListener = {view, menuItem, isSelect->
            // https://github.com/st235/ExpandableBottomBar
            // 네비게이션 바 클릭 시 이벤트

            when(menuItem.id){
                R.id.nav_home_btn->{
                    println("home")

                    var f = HomeFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.main_content,f).commit()

                }
                R.id.nav_review_btn->{
                    println("review")

                    var f = ReviewFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.main_content,f).commit()

                }
                R.id.nav_rank_btn->{
                    println("rank")

                    var f = RankFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.main_content,f).commit()
                }
                R.id.nav_account_btn->{
                    println("account")

                    var f = AccountFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.main_content,f).commit()
                }

            }

        }
    }
}