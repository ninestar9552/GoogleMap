package cha.n.googlemap

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import cha.n.googlemap.data.address.*
import cha.n.googlemap.data.keyword.KeywordResults
import cha.n.googlemap.databinding.ActivityMapsBinding
import cha.n.googlemap.util.DisplayUtils
import cha.n.googlemap.util.getRetrofitService
import cha.n.googlemap.util.showLog
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_list_search_result.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


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

//        binding.bottomSheet.maxHeight = DisplayUtils.getScreenHeightPx(this@MapsActivity)*3/4

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
            val meta = Meta(
                is_end = true,
                pageable_count = 1,
                total_count = 1
            )

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

            val searchResult = AddressResults(
                documents = documents,
                meta = meta
            )
            binding.vm = searchResult
        }

        this.showLog("걍걍", "힐힐힐")

        binding.etSearch.setOnEditorActionListener { textView, i, keyEvent ->
            getRetrofitService().getKeywordSearch(textView.text.toString()).enqueue(object : Callback<KeywordResults> {
                override fun onFailure(call: Call<KeywordResults>, t: Throwable) {
                    Log.d("호출실패!!!!", t.message.toString())
                }

                override fun onResponse(call: Call<KeywordResults>, response: Response<KeywordResults>) {
                    if (response.isSuccessful) {
                        Log.d("응답코드", response.code().toString())
                        Log.d("응답바디", response.body().toString())
                        val result = response.body()!!

                        sheetBehavior.peekHeight = DisplayUtils.floatToDip(this@MapsActivity, 180f)
                        if (!result.documents.isNullOrEmpty()) {
                            binding.rvSearchResult.isVisible = true
                            binding.tvEmptyMessage.isVisible = false

                            sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

                            val firstItem = result.documents[0]
                            val latLng = LatLng((firstItem.y).toDouble(), (firstItem.x).toDouble())
                            val marker1 = MarkerOptions()
                                .position(latLng)
                                .title(firstItem.place_name)
                            mMap?.let {
                                it.addMarker(marker1).showInfoWindow()
                                it.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(latLng, 17F))
                            }
                        } else {
                            binding.rvSearchResult.isVisible = false
                            binding.tvEmptyMessage.isVisible = true
                        }


                        val adapter = RecyclerAdapter(result.documents)
                        binding.rvSearchResult.adapter = adapter
                        adapter.setOnItemClickListener(object : OnItemClickListener {
                            override fun onItemClick(v: View, item: Any, position: Int) {
                                val item = item as cha.n.googlemap.data.keyword.Document
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

                    } else {
                        Log.d("응답실패", response.errorBody()!!.string())
                    }
                }
            })
            true
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
        mMap.addMarker(marker1).showInfoWindow()
        mMap.moveCamera(CameraUpdateFactory
                .newLatLngZoom(seoul, 17F))

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





    inner class RecyclerAdapter(private val list: List<cha.n.googlemap.data.keyword.Document>) : RecyclerView.Adapter<RecyclerAdapter.ItemViewHolder>() {

        var listener: OnItemClickListener? = null

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.ItemViewHolder
        {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list_search_result, parent, false)

            return ItemViewHolder(view)
        }

        override fun getItemCount(): Int = list.size

        override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

            val item = list[position]

            holder.containerView.tvItemPlaceName.text = item.place_name
            if (item.road_address_name != "") {
                holder.containerView.tvItemAddressType.text = "[도로]"
                holder.containerView.tvItemAddressName.text = item.road_address_name
            } else {
                holder.containerView.tvItemAddressType.text = "[지번]"
                holder.containerView.tvItemAddressName.text = item.address_name
            }

            holder.containerView.clSearchResultItem.setOnClickListener {v ->
                listener?.let { it.onItemClick(v, item, position) }
            }
        }

        inner class ItemViewHolder(override val containerView: View): RecyclerView.ViewHolder(containerView),
            LayoutContainer

        fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
            listener = onItemClickListener
        }
    }

    interface OnItemClickListener {
        fun onItemClick(v: View, item: Any, position: Int)
    }


}