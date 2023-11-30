package com.example.dbproject.fragment

import android.os.Bundle
import android.provider.ContactsContract.Data
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dbproject.R
import com.example.dbproject.databinding.FragmentReviewBinding
import com.example.dbproject.databinding.ItemReviewDetailBinding

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



        binding.reviewRecyclerview.adapter = ReviewRecyclerViewAdapter()
        binding.reviewRecyclerview.layoutManager = LinearLayoutManager(activity)
        return binding.root
    }

    inner class ReviewHolder(var binding: ItemReviewDetailBinding) : RecyclerView.ViewHolder(binding.root)
    inner class ReviewRecyclerViewAdapter(): RecyclerView.Adapter<ReviewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewHolder {
            var view = ItemReviewDetailBinding.inflate(LayoutInflater.from(parent.context),parent,false)

            return ReviewHolder(view)
        }

        override fun getItemCount(): Int {
            return 2
        }

        override fun onBindViewHolder(holder: ReviewHolder, position: Int) {
            var examplelist = arrayListOf<String>()
            var examplelistacc = arrayListOf<String>()
            var viewHolder = holder.binding

            examplelist.add("식당1")
            examplelist.add("식당2")
            examplelistacc.add("aaa@naver.com")
            examplelistacc.add("bbb@naver.com")
            viewHolder.reviewListResname.text = examplelist[position]
            viewHolder.reviewListAccname.text = examplelistacc[position]


        }


    }
}