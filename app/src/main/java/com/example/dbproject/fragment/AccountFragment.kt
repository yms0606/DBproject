package com.example.dbproject.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.dbproject.R
import com.example.dbproject.databinding.FragmentAccountBinding
import com.example.dbproject.databinding.FragmentReviewBinding

class AccountFragment : Fragment() {
    lateinit var binding : FragmentAccountBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_account,container,false)
        return binding.root
    }
}