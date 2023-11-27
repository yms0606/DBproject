package com.example.dbproject.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.dbproject.R
import com.example.dbproject.databinding.FragmentRankBinding
import com.example.dbproject.databinding.FragmentReviewBinding

class RankFragment : Fragment() {
    lateinit var binding : FragmentRankBinding
    // 가장 좋아요를 많이 받은 유저 1,2,3를 신기한 리스트 써서 만들면 어떨까 싶어서 만들어 둠
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_rank,container,false)
        return binding.root
    }
}