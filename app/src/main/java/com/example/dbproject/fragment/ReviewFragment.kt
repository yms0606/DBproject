package com.example.dbproject.fragment

import android.os.Bundle
import android.provider.ContactsContract.Data
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dbproject.R
import com.example.dbproject.data.RestaurantData
import com.example.dbproject.data.ReviewData
import com.example.dbproject.data.UserData
import com.example.dbproject.databinding.FragmentReviewBinding
import com.example.dbproject.databinding.ItemReviewDetailBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.FirebaseStorage
import kotlin.math.round

class ReviewFragment : Fragment() {

    lateinit var binding : FragmentReviewBinding
    lateinit var auth: FirebaseAuth
    lateinit var firestore: FirebaseFirestore
    lateinit var firebaseStorage: FirebaseStorage
    // 리뷰 리스트가 있을 프레그먼트
    // 리스트 뷰 디자인은 미정
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_review,container,false)
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        firebaseStorage = FirebaseStorage.getInstance()

        binding.reviewRecyclerview.adapter = ReviewRecyclerViewAdapter()
        binding.reviewRecyclerview.layoutManager = LinearLayoutManager(activity)


        return binding.root
    }

    inner class ReviewHolder(var binding: ItemReviewDetailBinding) : RecyclerView.ViewHolder(binding.root)
    inner class ReviewRecyclerViewAdapter(): RecyclerView.Adapter<ReviewHolder>(){

        var reviewDatas = arrayListOf<ReviewData>()

        init {
            firestore.collection("Reviews").addSnapshotListener { value, error ->

                reviewDatas.clear()

                for(item in value!!.documents){
                    var reviewData = item.toObject(ReviewData::class.java)
                    reviewDatas.add(reviewData!!)
                }
                reviewDatas.reverse()
                notifyDataSetChanged()
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewHolder {
            var view = ItemReviewDetailBinding.inflate(LayoutInflater.from(parent.context),parent,false)
            return ReviewHolder(view)
        }

        override fun getItemCount(): Int {
            return reviewDatas.size
        }

        override fun onBindViewHolder(holder: ReviewHolder, position: Int) {

            var viewHolder = holder.binding
            var path = "doc"+reviewDatas[position].resId.toString()


            var restaurantData = firestore.collection("Restaurants").document(path)
            restaurantData.get().addOnCompleteListener {
                task->
                val res = task.result.toObject(RestaurantData::class.java)

                viewHolder.menu1Text.text = res!!.menu[0]
                viewHolder.menu2Text.text = res!!.menu[1]
                viewHolder.menu3Text.text = res!!.menu[2]

            }

            if(reviewDatas[position].favoriteList.contains(auth.currentUser!!.email)){
                viewHolder.reviewListFavbtn.setImageResource(R.drawable.icon_heart)
            }

            viewHolder.reviewListResname.text = reviewDatas[position].resName
            viewHolder.reviewListAccname.text = reviewDatas[position].userName
            viewHolder.likeCountText.text = reviewDatas[position].favorite.toString()
            viewHolder.menu1Rating.text = (round(reviewDatas[position].menuRating[0]*100)/100).toString()
            viewHolder.menu2Rating.text = (round(reviewDatas[position].menuRating[1]*100)/100).toString()
            viewHolder.menu3Rating.text = (round(reviewDatas[position].menuRating[2]*100)/100).toString()
            viewHolder.commentText.text = reviewDatas[position].comment
            viewHolder.listResRating.text = (round(reviewDatas[position].resRating!! * 100)/100) .toString()

            Glide.with(holder.itemView.context).load(reviewDatas[position].imageUrl)
                .into(viewHolder.reivewListImage)

            viewHolder.root.setOnClickListener {
                if(viewHolder.hidedView.visibility == View.VISIBLE){
                    viewHolder.hidedView.visibility = View.GONE
                    notifyDataSetChanged()
                }else{
                    viewHolder.hidedView.visibility = View.VISIBLE
                }
            }

            viewHolder.reviewListFavbtn.setOnClickListener {
                if(reviewDatas[position].favoriteList.contains(auth.currentUser!!.email)){

                    viewHolder.reviewListFavbtn.setImageResource(R.drawable.icon_base_heart)
                    reviewDatas[position].favoriteList.remove(auth.currentUser!!.email)
                    reviewDatas[position].favorite = reviewDatas[position].favorite?.minus(1)

                    val userData = firestore.collection("users").document(reviewDatas[position].userUid!!)
                    userData.get().addOnCompleteListener {
                            task->
                        var user = task.result.toObject(UserData::class.java)

                        val userUpdates = hashMapOf<String,Any>(
                            "favoriteNumber" to user?.favoriteNumber?.minus(1)!!
                        )

                        userData.update(userUpdates)
                    }

                }else{
                    viewHolder.reviewListFavbtn.setImageResource(R.drawable.icon_heart)
                    reviewDatas[position].favoriteList.add(auth.currentUser!!.email!!)
                    reviewDatas[position].favorite = reviewDatas[position].favorite?.plus(1)

                    val userData = firestore.collection("users").document(reviewDatas[position].userUid!!)
                    userData.get().addOnCompleteListener {
                            task->
                        var user = task.result.toObject(UserData::class.java)

                        val userUpdates = hashMapOf<String,Any>(
                            "favoriteNumber" to user?.favoriteNumber?.plus(1)!!
                        )

                        userData.update(userUpdates)
                    }
                }

                val updates = hashMapOf<String,Any>(
                    "favoriteList" to reviewDatas[position].favoriteList,
                    "favorite" to reviewDatas[position].favorite!!,
                )
                val data = firestore.collection("Reviews").document(reviewDatas[position].uid!!)
                data.get().addOnCompleteListener {
                    data.update(updates)
                }

            }


        }


    }
}