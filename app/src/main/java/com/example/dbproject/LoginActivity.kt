package com.example.dbproject

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import com.example.dbproject.databinding.ActivityLoginBinding
import com.example.dbproject.fragment.LoginFragment
import com.google.android.gms.dynamic.SupportFragmentWrapper
import com.google.firebase.auth.FirebaseAuth
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_login)
        auth = FirebaseAuth.getInstance()

        binding.loginBtn.setOnClickListener{
            binding.loginBtn.startAnimation()
            signIn()
        }
        binding.createBtn.setOnClickListener {
            LoginFragment().show(supportFragmentManager,"add account")
        }

    }


    fun signIn(){

        var id : String? = binding.EmailEdit.text.toString()
        var password : String? = binding.passwordEdit.text.toString()

        if(id?.length == 0 || password?.length == 0) {
            MotionToast.createColorToast(
                this,
                "Failed ☹️",
                "아이디와 비밀번호를 입력해주세요",
                MotionToastStyle.ERROR,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
            binding.loginBtn.revertAnimation()
            binding.loginBtn.background = resources.getDrawable(R.drawable.button_background)
            return
        }

        auth.signInWithEmailAndPassword(id!!,password!!).addOnCompleteListener {
            task->

            if(task.isSuccessful){
                if(task.result?.user!!.isEmailVerified){ // 로그인 성공 & 이메일 인증 완료
                    finish()
                    startActivity(Intent(this,MainActivity::class.java))
                }else{                                   // 로그인 성공 & 이메일 인증 안됨
                    binding.loginBtn.revertAnimation()
                    binding.loginBtn.background = resources.getDrawable(R.drawable.button_background)
                    task.result?.user!!.sendEmailVerification()
                    MotionToast.createColorToast(this,
                        "INFO",
                        "인증 메일을 전송했습니다. 확인해주세요",
                        MotionToastStyle.INFO,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
                }
            }else{                                       // 비밀번호가 잘못되었거나 아이디가 존재하지 않음
                binding.loginBtn.revertAnimation()
                binding.loginBtn.background = resources.getDrawable(R.drawable.button_background)
                MotionToast.createColorToast(this,
                    "Failed ☹️",
                    "비밀번호와 아이디를 확인해주세요",
                    MotionToastStyle.ERROR,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
            }
        }
    }
}