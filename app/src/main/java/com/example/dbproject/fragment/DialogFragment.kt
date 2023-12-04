package com.example.dbproject.fragment
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.DialogFragment
import com.example.dbproject.data.RestaurantData
import com.example.dbproject.data.ReviewData
import com.example.dbproject.databinding.DialogDetailBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import com.google.firebase.storage.FirebaseStorage
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Date

class DialogFragment(): DialogFragment() {

    lateinit var binding: DialogDetailBinding
    lateinit var firestore: FirebaseFirestore
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var firebaseStorage: FirebaseStorage

    var photoUrl : Uri? = null
    var photoResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result->
        photoUrl = result.data?.data
        binding.reviewImage.setImageURI(photoUrl)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DialogDetailBinding.inflate(inflater,container,false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        firebaseStorage = FirebaseStorage.getInstance()


        var name = arguments?.getString("name")
        var id = arguments?.getInt("id")
        var menu = arguments?.getStringArrayList("menu")

        binding.reviewResName.text = name.toString() +" 는 어땠나요?"
        binding.reviewMenu1Name.text = menu?.get(0).toString()
        binding.reviewMenu2Name.text = menu?.get(1).toString()
        binding.reviewMenu3Name.text = menu?.get(2).toString()


        binding.reviewImage.setOnClickListener {
            var i = Intent(Intent.ACTION_PICK)
            i.type = "image/*"
            photoResult.launch(i)
        }

        binding.reviewCancelBtn.setOnClickListener{
            dismiss()
        }
        binding.reviewAddBtn.setOnClickListener {

            val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            val imageFileName = "IMAGE_"+id.toString()+"_"+timestamp+".png"
            var rating = binding.reviewResRating.getReviewScore()
            var me1_rating = binding.reviewMenu1Rating.getReviewScore()
            var me2_rating = binding.reviewMenu2Rating.getReviewScore()
            var me3_rating = binding.reviewMenu3Rating.getReviewScore()


            var storagePath = firebaseStorage.reference?.child("images")?.child(imageFileName)

            storagePath?.putFile(photoUrl!!)?.continueWithTask {
                return@continueWithTask storagePath?.downloadUrl
            }?.addOnCompleteListener {
                downloadUrl->

                var reviewData = ReviewData()

                reviewData.resName = name
                reviewData.resId = id
                reviewData.userName = firebaseAuth?.currentUser?.email
                reviewData.favorite = 0
                reviewData.favoriteList = ArrayList<String>()
                reviewData.resRating = rating.toDouble()
                reviewData.imageUrl = downloadUrl.result.toString()
                reviewData.menuRating.add(me1_rating.toDouble())
                reviewData.menuRating.add(me2_rating.toDouble())
                reviewData.menuRating.add(me3_rating.toDouble())
                reviewData.comment = binding.reviewComment.text.toString()

                firestore.collection("Reviews").document().set(reviewData)

            }
            var path = "doc"+id.toString()
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



            MotionToast.createColorToast(context as Activity,
                "SUCCESS",
                "게시물 등록 완료",
                MotionToastStyle.SUCCESS,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                ResourcesCompat.getFont(requireContext(), www.sanju.motiontoast.R.font.helvetica_regular))

            dismiss()
        }


        return binding.root
    }

    override fun onResume() {
        super.onResume()
        dialog?.setCancelable(false)
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }
}