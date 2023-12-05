package com.example.dbproject.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.dbproject.LoginActivity
import com.example.dbproject.MainActivity
import com.example.dbproject.R
import com.example.dbproject.data.UserData
import com.example.dbproject.databinding.FragmentAccountBinding
import com.example.dbproject.databinding.FragmentReviewBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle

class AccountFragment : Fragment() {
    lateinit var binding : FragmentAccountBinding
    lateinit var auth: FirebaseAuth
    lateinit var firestore: FirebaseFirestore
    // 내 계정 정보 보여주는 페이지
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_account,container,false)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        firestore.collection("users").whereEqualTo("email",auth.currentUser!!.email).get().addOnCompleteListener {
            task->

            var user = task.result.documents.first().toObject(UserData::class.java)
            binding.userNameAcc.text = user?.email
            binding.likeCountAcc.text = user?.favoriteNumber.toString()
        }

        binding.signOutBtn.setOnClickListener {
            MotionToast.createColorToast(context as Activity,
                "SUCCESS",
                "로그아웃",
                MotionToastStyle.SUCCESS,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.SHORT_DURATION,
                ResourcesCompat.getFont(requireContext(), www.sanju.motiontoast.R.font.helvetica_regular))

            auth.signOut()
            activity?.finish()
            activity?.startActivity(Intent(context,LoginActivity::class.java))
        }


        return binding.root
    }
}