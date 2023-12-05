package com.example.dbproject.fragment

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.DialogFragment
import com.example.dbproject.LoginActivity
import com.example.dbproject.R
import com.example.dbproject.data.UserData
import com.example.dbproject.databinding.DialogDetailBinding
import com.example.dbproject.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle

class LoginFragment(): DialogFragment() {


    lateinit var binding: FragmentLoginBinding
    lateinit var auth: FirebaseAuth
    lateinit var firestore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?{
        binding = FragmentLoginBinding.inflate(inflater,container,false)
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.createCancelBtn.setOnClickListener {
            dismiss()
        }
        binding.createAddBtn.setOnClickListener {
            val id = binding.createEmailEdit.text.toString()
            val password = binding.createPasswordEdit.text.toString()
            val phoneNumber = binding.createPhoneEdit.text.toString()

            auth.createUserWithEmailAndPassword(id,password).addOnCompleteListener {
                    task1->
                if(task1.isSuccessful){
                    var userData = UserData()
                    userData.email = id
                    userData.phoneNumber = phoneNumber
                    userData.favoriteNumber = 0

                    var uid = firestore.collection("users").document().id
                    userData.uid = uid

                    userData.userUid = task1.result.user?.uid
                    firestore.collection("users").document(uid).set(userData)

                    MotionToast.createColorToast(context as Activity,
                        "SUCCESS",
                        "계정 생성 완료",
                        MotionToastStyle.SUCCESS,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.SHORT_DURATION,
                        ResourcesCompat.getFont(requireContext(), www.sanju.motiontoast.R.font.helvetica_regular))

                    dismiss()
                }else{
                    MotionToast.createColorToast(
                        context as Activity,
                        "Failed ☹️",
                        "이미 존재하는 메일입니다.",
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(requireContext(), www.sanju.motiontoast.R.font.helvetica_regular))
                }
            }
        }



        return binding.root
    }

    override fun onResume() {
        super.onResume()
        dialog?.setCancelable(false)
        dialog?.setTitle("Create Account")
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }
}