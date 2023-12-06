package com.example.dbproject.fragment

import android.app.Activity
import android.content.Intent
import android.content.res.AssetManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.dbproject.LoginActivity
import com.example.dbproject.MainActivity
import com.example.dbproject.R
import com.example.dbproject.data.RestaurantData
import com.example.dbproject.data.ReviewData
import com.example.dbproject.data.UserData
import com.example.dbproject.databinding.FragmentAccountBinding
import com.example.dbproject.databinding.FragmentReviewBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.FirebaseStorage
import com.opencsv.CSVReader
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import java.io.InputStream
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Date

class AccountFragment : Fragment() {
    lateinit var binding : FragmentAccountBinding
    lateinit var auth: FirebaseAuth
    lateinit var firestore: FirebaseFirestore
    lateinit var storage : FirebaseStorage
    var photoUrl : Uri? = null
    var photoResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result->
        photoUrl = result.data?.data
    }
    // 내 계정 정보 보여주는 페이지
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_account,container,false)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()

        var i = Intent(Intent.ACTION_PICK)
        i.type = "image/*"
        photoResult.launch(i)

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


        /*binding.inputReview.setOnClickListener {

            val assetManager: AssetManager = requireContext().assets
            val inputStream : InputStream = assetManager.open("reviewdata.csv")

            val reader = CSVReader(InputStreamReader(inputStream))

            val allContent = reader.readAll()

            for(content in allContent){
                var review = content.toList()
                //var reviewData = ReviewData()


                val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                val imageFileName = "IMAGE_"+review[1].toString()+"_"+timestamp+".png"
                var rating = review[2].toDouble()
                var me1_rating = review[3].toDouble()
                var me2_rating = review[4].toDouble()
                var me3_rating = review[5].toDouble()


                var storagePath = storage.reference?.child("images")?.child(imageFileName)
                storagePath?.putFile(photoUrl!!)?.continueWithTask {
                    return@continueWithTask storagePath?.downloadUrl
                }?.addOnCompleteListener {
                        downloadUrl->

                    var d = firestore.collection("users").whereEqualTo("email",auth.currentUser?.email)
                    d.get().addOnCompleteListener { task ->

                        var fd = task.result.documents.first().toObject(UserData::class.java)

                        var reviewData = ReviewData()

                        reviewData.resName = review[0]
                        reviewData.resId = review[1].toInt()
                        reviewData.userName = auth?.currentUser?.email
                        reviewData.favorite = 0
                        reviewData.favoriteList = ArrayList<String>()
                        reviewData.resRating = rating
                        reviewData.imageUrl = downloadUrl.result.toString()
                        reviewData.menuRating.add(me1_rating)
                        reviewData.menuRating.add(me2_rating)
                        reviewData.menuRating.add(me3_rating)
                        reviewData.comment = review[6]

                        reviewData.userUid = fd?.uid

                        var uid = firestore.collection("Reviews").document().id
                        reviewData.uid = uid

                        firestore.collection("Reviews").document(uid).set(reviewData)
                    }


                }
                var path = "doc"+review[1].toString()
                val data = firestore.collection("Restaurants").document(path)
                data.get().addOnCompleteListener {
                        task->


                    val restaurantData = task.result.toObject(RestaurantData::class.java)

                    var count = restaurantData?.reviewCount
                    var total = restaurantData?.rating?.times(count!!)
                    var add_count = count?.plus(1)

                    var add_total = ( total?.plus(rating) )?.div(add_count!!)

                    println(count.toString())
                    println(restaurantData!!.menuRating.get(0).toString())

                    total = restaurantData!!.menuRating[0].times(count!!)
                    var me1_total = ( total?.plus(me1_rating) )?.div(add_count!!)
                    total = restaurantData!!.menuRating[1].times(count!!)
                    var me2_total = ( total?.plus(me2_rating) )?.div(add_count!!)
                    total = restaurantData!!.menuRating[2].times(count!!)
                    var me3_total = ( total?.plus(me3_rating) )?.div(add_count!!)

                    var menuRating : ArrayList<Double> = arrayListOf<Double>()
                    menuRating.add(me1_total!!)
                    menuRating.add(me2_total!!)
                    menuRating.add(me3_total!!)

                    val updates = hashMapOf<String,Any>(
                        "reviewCount" to add_count!!,
                        "rating" to add_total!!,
                        "menuRating" to menuRating,
                    )

                    data.update(updates)
                }

            }


        }*/


        return binding.root
    }
}