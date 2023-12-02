package com.example.dbproject.fragment
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.DialogFragment
import com.example.dbproject.databinding.DialogDetailBinding

class DialogFragment(): DialogFragment() {

    lateinit var binding: DialogDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DialogDetailBinding.inflate(inflater,container,false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.reviewCancelBtn.setOnClickListener{
            dismiss()
        }
        binding.reviewAddBtn.setOnClickListener {
            Toast.makeText(context,binding.reviewResRating.getReviewScore().toString()
                ,Toast.LENGTH_SHORT).show()

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