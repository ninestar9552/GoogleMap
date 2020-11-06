package cha.n.googlemap.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import cha.n.googlemap.MyApplication
import cha.n.googlemap.R
import cha.n.googlemap.databinding.FragmentBottomSheetBinding
import cha.n.googlemap.util.DisplayUtils
import com.google.android.material.bottomsheet.BottomSheetBehavior


class BottomSheetFragment : Fragment() {

    private val viewModel: MapsViewModel by lazy {
        ViewModelProvider(requireActivity(), MapsViewModelFactory((requireActivity().application as MyApplication).taskRepository)).get(MapsViewModel::class.java)
    }

    private lateinit var binding: FragmentBottomSheetBinding
    lateinit var sheetBehavior: BottomSheetBehavior<View>   // Activity의 onBackPressed에서 접근할 수 있도록 public 선언

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bottom_sheet, container, false)
        binding.lifecycleOwner = this@BottomSheetFragment
        binding.vm = viewModel
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupBottomSheet()
        setupListAdapter()

        binding.etKeywordSearch.apply {
            setOnEditorActionListener { textView, i, keyEvent ->
                viewModel.getKeywordResults(textView.text.toString())
                sheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
                true
            }

            // focus 상태가 되거나 또는 click 이벤트 시 bottom sheet 확장
            // (edit text 는 focus가 없을 시 click 이벤트가 발생되지 않으므로)
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

        viewModel.inputKeywordText.observe(this@BottomSheetFragment, Observer { inputText ->
            if (inputText.length > 2) {
                viewModel.getKeywordResults(inputText)
            }
        })

        // 반환된 Event class의 peekContent() 메서드 사용 시 기존 이벤트 소비 여부에 상관없이 이벤트 발생됨
        // (Event class 사용 시 Observer와 EventObserver의 차이)
        viewModel.itemSelctedEvent.observe(this@BottomSheetFragment, Observer { event ->
            event.peekContent().apply {
                sheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
            }
        })

    }

    private fun setupBottomSheet() {
        sheetBehavior = BottomSheetBehavior.from(binding.clBottomSheet)
        sheetBehavior.apply {
            peekHeight = DisplayUtils.floatToDip(requireContext(), 126f)
            isHideable = false
            addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onSlide(bottomSheet: View, slideOffset: Float) {

                }
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    when (newState) {
                        BottomSheetBehavior.STATE_HALF_EXPANDED -> {
                            (requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(bottomSheet.windowToken, 0)
                        }
                        BottomSheetBehavior.STATE_COLLAPSED -> {
                            (requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(bottomSheet.windowToken, 0)
                            binding.etKeywordSearch.clearFocus()
                        }
                    }
                }
            })
        }
    }

    private fun setupListAdapter() {
        viewModel?.let { viewModel ->
            binding.rvSearchResult.adapter = RecyclerAdapter(viewModel = viewModel)
        }
    }
}