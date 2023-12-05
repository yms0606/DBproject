package com.example.dbproject.fragment

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.dbproject.R
import com.example.dbproject.data.RestaurantData
import com.example.dbproject.databinding.FragmentHomeBinding
import com.example.dbproject.databinding.InfowindowDetailBinding
import com.example.dbproject.databinding.ItemReviewDetailBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.NaverMapOptions
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.InfoWindow.DefaultViewAdapter
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.overlay.OverlayImage
import kotlinx.coroutines.NonDisposableHandle.parent
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import kotlin.math.*
class HomeFragment : Fragment(), OnMapReadyCallback {

    lateinit var binding: FragmentHomeBinding
    lateinit var auth: FirebaseAuth
    var firestore: FirebaseFirestore
    var RestaurantDatas  = ArrayList<RestaurantData>()

    init {
        firestore = FirebaseFirestore.getInstance()
        firestore.collection("Restaurants").addSnapshotListener { value, error ->

            for(item in value!!.documents){
                var res : RestaurantData? = item.toObject(RestaurantData::class.java)
                RestaurantDatas.add(res!!)
            }
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val fm = childFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map_fragment)
                as MapFragment??: MapFragment.newInstance().also {
            fm.beginTransaction().add(R.id.map_fragment,it).commit() }
        mapFragment.getMapAsync(this)  // 지도 객체 설정
        // ============== 네이버 지도 api

        auth = FirebaseAuth.getInstance()
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home,container,false)


        return binding.root
    }

    override fun onMapReady(p0: NaverMap) {
        //37.2791667!4d127.0430556    겐코
        //37.27848!4d127.04279   한식이야기
        // 지도 laod 시 실행, 현재 지도 위에 임의로 2개의 핀을 꼽아둠



        p0.moveCamera(CameraUpdate.scrollTo(LatLng(37.2800147,127.0436415))) // 초기좌표 설정
        p0.moveCamera(CameraUpdate.zoomTo(16.0)) // 지도 줌인 계수

        firestore.collection("Restaurants").addSnapshotListener { value, error ->

            RestaurantDatas.clear()

            for(item in value!!.documents){
                var res : RestaurantData? = item.toObject(RestaurantData::class.java)
                RestaurantDatas.add(res!!)
            }

            context?.let {

                RestaurantDatas.forEach {place->
                    // 다중 마커 추가하는 방법, 이차원 배열을 사용해서 + 각 마커마다 클릭 이벤트 부여
                    //https://kimcoder.tistory.com/351
                    //https://navermaps.github.io/android-map-sdk/guide-ko/5-1.html



                    val infoWindow = InfoWindow()
                    infoWindow.adapter = object : InfoWindow.DefaultViewAdapter(it){
                        override fun getContentView(p0: InfoWindow): View {
                            var views: InfowindowDetailBinding = InfowindowDetailBinding.inflate(
                                LayoutInflater.from(context),LinearLayout(context),false)
                            // binding의 객체화
                            views.resName.text = place.name.toString()
                            views.resRating.text = (round((place.rating?.times(100)!!)).div(100)!!).toString()
                            views.m1Name.text = place.menu.get(0)
                            views.m2Name.text = place.menu.get(1)
                            views.m3Name.text = place.menu.get(2)
                            views.m1Rating.text = (round(place.menuRating.get(0)*100)/100).toString()
                            views.m2Rating.text = (round(place.menuRating.get(1)*100)/100).toString()
                            views.m3Rating.text = (round(place.menuRating.get(2)*100)/100).toString()
                            return views.root
                        }

                    } // 정보창 선언

                    infoWindow.setOnClickListener {

                        var isAlready = false

                        firestore.collection("Reviews").whereEqualTo("resId",place.id)
                            .whereEqualTo("userName",auth.currentUser?.email).get().addOnCompleteListener {
                                task->
                                println("in")
                                if( task.result.documents.size == 0){
                                    isAlready = true
                                }
                                println(isAlready.toString())

                                if(isAlready){
                                    var dialogFragment = DialogFragment()
                                    var bundle = Bundle()
                                    bundle.putStringArrayList("menu",place.menu)
                                    bundle.putInt("id",place.id!!)
                                    bundle.putString("name",place.name)
                                    dialogFragment.arguments = bundle
                                    dialogFragment.show(requireActivity().supportFragmentManager,"Add Review")

                                    infoWindow.close()
                                }else{
                                    MotionToast.createColorToast(context as Activity,
                                        "INFO",
                                        "이미 리뷰를 작성한 식당입니다.",
                                        MotionToastStyle.ERROR,
                                        MotionToast.GRAVITY_BOTTOM,
                                        MotionToast.SHORT_DURATION,
                                        ResourcesCompat.getFont(requireContext(), www.sanju.motiontoast.R.font.helvetica_regular))
                                }
                            }

                        true
                    }
                    val marker = Marker()
                    marker.position = LatLng(place.latitude!!.toDouble(),place.longitude!!.toDouble())
                    marker.icon = OverlayImage.fromResource(com.naver.maps.map.R.drawable.navermap_default_marker_icon_lightblue)
                    marker.isIconPerspectiveEnabled = true

                    marker.setOnClickListener {

                        if(marker.infoWindow == null) { // 정보창 띄우기
                            MotionToast.createColorToast(context as Activity,
                                "INFO",
                                "해당 식당은 " +place.name.toString()+" 입니다.",
                                MotionToastStyle.INFO,
                                MotionToast.GRAVITY_BOTTOM,
                                MotionToast.SHORT_DURATION,
                                ResourcesCompat.getFont(requireContext(), www.sanju.motiontoast.R.font.helvetica_regular))
                            infoWindow.open(marker)
                        } else{
                            infoWindow.close()
                        }

                        true
                    }

                    marker.map = p0
                }
            }
        }


    }
}
