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
import cha.n.googlemap.data.model.FavoritesDatabase
import cha.n.googlemap.databinding.FragmentBottomSheetBinding
import cha.n.googlemap.util.DisplayUtils
import com.google.android.material.bottomsheet.BottomSheetBehavior


class BottomSheetFragment : Fragment() {

    private val viewModel: MapsViewModel by lazy {
        ViewModelProvider(requireActivity(),
            MapsViewModelFactory((requireActivity().application as MyApplication).service,
                FavoritesDatabase.getInstance(requireContext().applicationContext)?.favoritesDao()!!
            )
        ).get(MapsViewModel::class.java)
    }

    private lateinit var binding: FragmentBottomSheetBinding
    lateinit var sheetBehavior: BottomSheetBehavior<View>

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

        viewModel.inputKeywordText.observe(this@BottomSheetFragment, Observer { inputText ->
            if (inputText.length > 2) {
                viewModel.getKeywordResults(inputText)
            }
        })

        viewModel.itemSelctedEvent.observe(this@BottomSheetFragment, Observer { event ->
            event.peekContent().apply {
                sheetBehavior.peekHeight = DisplayUtils.floatToDip(requireContext(), 96f)
                sheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
            }
        })

    }

    private fun setupBottomSheet() {
        sheetBehavior = BottomSheetBehavior.from(binding.bottomSheet)
        sheetBehavior.apply {
            peekHeight = DisplayUtils.floatToDip(requireContext(), 96f)
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
                            (requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(bottomSheet.windowToken, 0)
                        }
                        BottomSheetBehavior.STATE_COLLAPSED -> {
                            (requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(bottomSheet.windowToken, 0)
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
        viewModel?.let { viewModel ->
            binding.rvSearchResult.adapter = RecyclerAdapter(viewModel = viewModel)
        }
    }
}