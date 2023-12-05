package com.example.dbproject.fragment

import android.content.res.AssetManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.dbproject.R
import com.example.dbproject.data.RestaurantData
import com.example.dbproject.data.UserData
import com.example.dbproject.databinding.FragmentRankBinding
import com.example.dbproject.databinding.FragmentReviewBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObject
import com.opencsv.CSVReader
import org.apache.commons.logging.Log
import java.io.InputStream
import java.io.InputStreamReader

class RankFragment : Fragment() {
    lateinit var binding : FragmentRankBinding
    lateinit var firestore: FirebaseFirestore
    // 가장 좋아요를 많이 받은 유저 1,2,3를 신기한 리스트 써서 만들면 어떨까 싶어서 만들어 둠
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_rank,container,false)
        firestore = FirebaseFirestore.getInstance()

        var userDatas = arrayListOf<UserData>()

        firestore.collection("users").orderBy("favoriteNumber", Query.Direction.DESCENDING).limit(3).get().addOnCompleteListener {
            task->

            for(item in task.result.documents){
                var userData = item.toObject(UserData::class.java)
                userDatas.add(userData!!)
            }

            binding.userName1st.text = userDatas[0].email
            binding.likeCount1st.text = userDatas[0].favoriteNumber.toString()
            binding.userName2nd.text = userDatas[1].email
            binding.likeCount2nd.text = userDatas[1].favoriteNumber.toString()
            binding.userName3rd.text = userDatas[2].email
            binding.likeCount3rd.text = userDatas[2].favoriteNumber.toString()

        }



        /*binding.inputDataBtn.setOnClickListener {
            // 데이터 삽입
            val assetManager: AssetManager = requireContext().assets
            val inputStream : InputStream = assetManager.open("data.csv")

            val reader = CSVReader(InputStreamReader(inputStream))

            val allContent = reader.readAll()
            var id: Int = 1

            for(content in allContent){
                var data = content.toList()
                var restaurantData = RestaurantData()
                restaurantData.name = data[0].toString()
                restaurantData.latitude = data[1].toDouble()
                restaurantData.longitude = data[2].toDouble()
                restaurantData.menu.add(data[3].toString())
                restaurantData.menu.add(data[4].toString())
                restaurantData.menu.add(data[5].toString())
                restaurantData.rating = 0.0
                restaurantData.reviewCount = 0
                restaurantData.menuRating.add(0.0)
                restaurantData.menuRating.add(0.0)
                restaurantData.menuRating.add(0.0)
                restaurantData.id = id

                firestore.collection("Restaurants").document("doc"+id.toString()).set(restaurantData)
                id += 1
                println("data: "+data[0].toString()+" "+data[1].toString())
            }
            Toast.makeText(context,"data input complete",Toast.LENGTH_SHORT).show()
        }*/




        return binding.root
    }
}