package cha.n.googlemap.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import cha.n.googlemap.R
import cha.n.googlemap.data.model.keyword.Document
import cha.n.googlemap.databinding.ActivityMapsBinding
import cha.n.googlemap.util.DisplayUtils
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private val TAG = MapsActivity::class.java.simpleName

    private lateinit var viewModel: MapsViewModel

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var sheetBehavior: BottomSheetBehavior<View>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,
            R.layout.activity_maps
        )

        viewModel = ViewModelProvider(this@MapsActivity, MapsViewModelFactory()).get(MapsViewModel::class.java)
        binding.lifecycleOwner = this@MapsActivity
        binding.vm = viewModel

        setupMapFragment()
        setupBottomSheet()
        setupListAdapter()

        binding.etSearch.apply {
            setOnEditorActionListener { textView, i, keyEvent ->
                viewModel.getKeywordResults(textView.text.toString())
                sheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
                true
            }

            setOnFocusChangeListener { view, hasFocus ->
                if (hasFocus) {
                    sheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                }
            }

            setOnClickListener {
                if (sheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
                    sheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                }
            }
        }

        viewModel.documents.observe(this@MapsActivity, Observer { documents ->
            if (!documents.isNullOrEmpty()) {
                val firstItem = documents[0]
                val latLng = LatLng((firstItem.y).toDouble(), (firstItem.x).toDouble())
                val marker1 = MarkerOptions()
                    .position(latLng)
                    .title(firstItem.place_name)
                mMap?.let {
                    it.addMarker(marker1).showInfoWindow()
                    it.moveCamera(
                        CameraUpdateFactory
                            .newLatLngZoom(latLng, 17F)
                    )
                }
            }
        })
    }

    private fun setupMapFragment() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun setupBottomSheet() {
//        binding.bottomSheet.maxHeight = DisplayUtils.getScreenHeightPx(this@MapsActivity)*3/4
        sheetBehavior = BottomSheetBehavior.from(binding.bottomSheet)
        sheetBehavior.apply {
            peekHeight = DisplayUtils.floatToDip(this@MapsActivity, 96f)
            isHideable = false
            addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onSlide(bottomSheet: View, slideOffset: Float) { }
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    when (newState) {
                        BottomSheetBehavior.STATE_HIDDEN -> {
                        }
                        BottomSheetBehavior.STATE_EXPANDED -> {
                        }
                        BottomSheetBehavior.STATE_HALF_EXPANDED -> {
                            (this@MapsActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(bottomSheet.windowToken, 0)
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
        }
    }

    private fun setupListAdapter() {
        binding.rvSearchResult.adapter = RecyclerAdapter().apply {
            setOnItemClickListener(object : OnItemClickListener {
                override fun onItemClick(v: View, item: Any, position: Int) {
                    val item = item as Document
                    when(v.id) {
                        R.id.clSearchResultItem -> {
                            sheetBehavior.peekHeight = DisplayUtils.floatToDip(this@MapsActivity, 96f)
                            sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                            val smoothScroller: RecyclerView.SmoothScroller by lazy {
                                object : LinearSmoothScroller(this@MapsActivity) {
                                    override fun getVerticalSnapPreference() = SNAP_TO_START
                                }
                            }
                            smoothScroller.targetPosition = position
                            binding.rvSearchResult.layoutManager?.startSmoothScroll(smoothScroller)

                            val latLng = LatLng((item.y).toDouble(), (item.x).toDouble())
                            val marker1 = MarkerOptions()
                                .position(latLng)
                                .title(item.place_name)
                            mMap?.let {
                                it.addMarker(marker1).showInfoWindow()
                                it.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(latLng, 17F))
                            }
                        }
                    }
                }
            })
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

        val seoul = LatLng(37.555149, 126.970759)
        val marker1 = MarkerOptions()
            .position(seoul)
            .title("서울역")
        mMap.addMarker(marker1).showInfoWindow()
        mMap.moveCamera(CameraUpdateFactory
                .newLatLngZoom(seoul, 17F))
    }

    override fun onBackPressed() {
        if (sheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        } else {
            super.onBackPressed()
        }
    }



}