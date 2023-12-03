package com.example.dbproject.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.dbproject.R
import com.example.dbproject.databinding.FragmentHomeBinding
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.NaverMapOptions
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.overlay.OverlayImage

class HomeFragment : Fragment(), OnMapReadyCallback {

    lateinit var binding: FragmentHomeBinding

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

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home,container,false)

        return binding.root
    }

    override fun onMapReady(p0: NaverMap) {
        //37.2791667!4d127.0430556    겐코
        //37.27848!4d127.04279   한식이야기
        // 지도 laod 시 실행, 현재 지도 위에 임의로 2개의 핀을 꼽아둠



        p0.moveCamera(CameraUpdate.scrollTo(LatLng(37.2800147,127.0436415))) // 초기좌표 설정
        p0.moveCamera(CameraUpdate.zoomTo(16.0)) // 지도 줌인 계수


        var places = ArrayList<ArrayList<Double>>()
        places.add(arrayListOf(37.2791667,127.0430556))
        places.add(arrayListOf(37.27848,127.04279))

        context?.let {

            places.forEach { place->
                // 다중 마커 추가하는 방법, 이차원 배열을 사용해서 + 각 마커마다 클릭 이벤트 부여
                //https://kimcoder.tistory.com/351
                //https://navermaps.github.io/android-map-sdk/guide-ko/5-1.html

                val infoWindow = InfoWindow()
                infoWindow.adapter = object : InfoWindow.DefaultTextAdapter(it) {
                    override fun getText(infoWindow: InfoWindow): CharSequence {
                        return place[0].toString() + "/" + place[1].toString()
                    }
                } // 정보창 선언

                val marker = Marker()
                marker.position = LatLng(place[0],place[1])
                marker.icon = OverlayImage.fromResource(com.naver.maps.map.R.drawable.navermap_default_marker_icon_lightblue)
                marker.isIconPerspectiveEnabled = true

                marker.setOnClickListener {
                    Toast.makeText(context,place[0].toString(),Toast.LENGTH_SHORT).show()

                    if(marker.infoWindow == null) { // 정보창 띄우기
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
