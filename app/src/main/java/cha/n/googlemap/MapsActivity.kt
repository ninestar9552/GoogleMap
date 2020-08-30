package cha.n.googlemap

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import cha.n.googlemap.data.*
import cha.n.googlemap.databinding.ActivityMapsBinding
import cha.n.googlemap.utils.DisplayUtils
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    val TAG = MapsActivity::class.java.simpleName

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var sheetBehavior: BottomSheetBehavior<View>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_maps)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        sheetBehavior = BottomSheetBehavior.from(binding.bottomSheet)
        sheetBehavior.peekHeight = DisplayUtils.floatToDip(this@MapsActivity, 96f)
        sheetBehavior.isHideable = false
        sheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {
                    }
                    BottomSheetBehavior.STATE_HALF_EXPANDED -> {
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        (this@MapsActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(bottomSheet.windowToken, 0)
                        binding.etSearch.clearFocus()
                    }
                    BottomSheetBehavior.STATE_DRAGGING -> {
                    }
                    BottomSheetBehavior.STATE_SETTLING -> {
                    }
                }
            }

        })


        binding.etSearch.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                sheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
        binding.etSearch.setOnClickListener {
            if (sheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
                sheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }

        binding.etSearch.addTextChangedListener { editable ->
            val documents = ArrayList<Document>()
            val meta = Meta(is_end = true, pageable_count = 1, total_count = 1)

            val address = Address(
                address_name = "서울 영등포구 영등포동 618-496",
                b_code = "1156010100",
                h_code = "1156051500",
                main_address_no = "618",
                mountain_yn = "N",
                region_1depth_name = "서울",
                region_2depth_name = "영등포구",
                region_3depth_h_name = "영등포본동",
                region_3depth_name = "영등포동",
                sub_address_no = "496",
                x = "126.907665995469",
                y = "37.5156680341198"
            )
            val road_address = RoadAddress(
                address_name = "서울 영등포구 경인로 846",
                building_name = "영등포 민자역사",
                main_building_no = "846",
                region_1depth_name = "서울",
                region_2depth_name = "영등포구",
                region_3depth_name = "영등포동",
                road_name = "경인로",
                sub_building_no = "",
                underground_yn = "N",
                x = "126.907665995469",
                y = "37.5156680341198",
                zone_no = "07306"
            )
            val document = Document(
                address = address,
//                address_name = "서울 영등포구 경인로 846",
                address_name = editable.toString(),
                address_type = "ROAD_ADDR",
                road_address = road_address,
                x = "126.907665995469",
                y = "37.5156680341198"
            )
            documents.add(document)

            val searchResult = SearchResult(
                documents = documents,
                meta = meta
            )
            binding.vm = searchResult

            val yeongdeungpo = LatLng((binding.vm!!.documents[0].y).toDouble(), (binding.vm!!.documents[0].x).toDouble())
            val marker1 = MarkerOptions()
                .position(yeongdeungpo)
                .title("Marker in Yeongdeungpo")
//            mMap?.let {
//                it.addMarker(marker1)
//                it.moveCamera(CameraUpdateFactory
//                    .newLatLngZoom(yeongdeungpo, 15F))
//            }
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val seoul = LatLng(37.555149, 126.970759)
        val marker1 = MarkerOptions()
            .position(seoul)
            .title("Marker in Seoul")
        mMap.addMarker(marker1)
        mMap.moveCamera(CameraUpdateFactory
                .newLatLngZoom(seoul, 15F))

        mMap.setOnMarkerClickListener {
            LocationSearchDialogFragment.newInstance(3).show(supportFragmentManager, TAG)
            false
        }
    }

    override fun onBackPressed() {
        if (sheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        } else {
            super.onBackPressed()
        }
    }
}