package com.example.dbproject.fragment

import android.os.Bundle
import android.provider.ContactsContract.Data
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.dbproject.R
import com.example.dbproject.databinding.FragmentReviewBinding

class ReviewFragment : Fragment() {

    lateinit var binding : FragmentReviewBinding
    // 리뷰 리스트가 있을 프레그먼트
    // 리스트 뷰 디자인은 미정
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_review,container,false)
        return binding.root
    }
}