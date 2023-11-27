package com.example.dbproject.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.naver.maps.map.overlay.Marker
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


        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home,container,false)

        return binding.root
    }

    override fun onMapReady(p0: NaverMap) {
        //37.2791667!4d127.0430556    겐코
        //37.27848!4d127.04279   한식이야기

        p0.moveCamera(CameraUpdate.scrollTo(LatLng(37.2800147,127.0436415))) // 초기좌표 설정
        p0.moveCamera(CameraUpdate.zoomTo(16.0)) // 지도 줌인 계수

        val marker = Marker()
        marker.icon = OverlayImage.fromResource(com.naver.maps.map.R.drawable.navermap_default_marker_icon_lightblue)
        marker.position = LatLng(37.2791667,127.0430556)
        marker.isIconPerspectiveEnabled = true
        marker.map = p0

        val marker2 = Marker()
        marker2.icon = OverlayImage.fromResource(com.naver.maps.map.R.drawable.navermap_default_marker_icon_lightblue)
        marker2.position = LatLng(37.27848,127.04279)
        marker2.isIconPerspectiveEnabled = true
        marker2.map = p0
    }
}