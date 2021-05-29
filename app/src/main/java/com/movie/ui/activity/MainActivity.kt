package com.movie.ui.activity

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.movie.R
import com.movie.data.network.API
import com.movie.data.network.Resource
import com.movie.data.network.RetrofitClient
import com.movie.data.repo.MovieRepo
import com.movie.databinding.ActivityMainBinding
import com.movie.ui.adapter.MovieAdapter
import com.movie.ui.adapter.MoviePagerAdapter
import com.movie.ui.viewmodel.MovieViewModel
import com.movie.ui.viewmodel.MovieViewModelFactory
import com.movie.utils.MyLoader
import com.movie.utils.Utility
import com.movie.utils.Utility.toPx
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
        const val page = 1
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MovieViewModel
    private lateinit var loader: MyLoader
    private lateinit var adapter: MovieAdapter
    private lateinit var viewPagerAdapter: MoviePagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        init()
    }

    private fun init() {
        loader = MyLoader(this)
        initializeViewModel()
        changeToolbarTitle()
        setViewPager()
        setRecyclerView()
        if (Utility.isNetworkAvailable(this)) {
            viewModel.getMovies(page)
        } else {
            binding.tvNoInternet.visibility = View.VISIBLE
        }
        viewModel.movieResponse.observe(this, {
            when (it) {
                is Resource.Loading -> loader.show()
                is Resource.Success -> {
                    binding.tvNoInternet.visibility = View.GONE
                    binding.scrollView.visibility = View.VISIBLE
                    loader.dismiss()
                    Log.d(TAG, "init: $it")
                    adapter.setMovies(it.value.results)
                    viewPagerAdapter.setMovies(it.value.results)
                }
                is Resource.Failure -> {
                    binding.tvNoInternet.visibility = View.VISIBLE
                    binding.tvNoInternet.text = getString(R.string.something_went_wrong)
                    loader.dismiss()
                    Utility.showSnackBar(
                        this,
                        binding.root,
                        getString(R.string.something_went_wrong)
                    )
                }
            }
        })
    }

    private fun changeToolbarTitle() {
        binding.toolbarTitle.text = getString(R.string.movies)
        val scrollBounds = Rect()
        binding.scrollView.getHitRect(scrollBounds)

        binding.scrollView.setOnScrollChangeListener { _: NestedScrollView, i: Int, i1: Int, i2: Int, i3: Int ->
            if (binding.textView.getLocalVisibleRect(scrollBounds)) {
                if (!binding.textView.getLocalVisibleRect(scrollBounds)
                    || scrollBounds.height() < binding.textView.height
                ) binding.toolbarTitle.text = getString(R.string.movies)
                else binding.toolbarTitle.text = getString(R.string.movies)
            } else binding.toolbarTitle.text = getString(R.string.now_showing)
        }
    }

    private fun initializeViewModel() {
        val repo = MovieRepo(RetrofitClient.buildApi(API::class.java))
        viewModel = ViewModelProvider(
            this,
            MovieViewModelFactory(repo)
        ).get(MovieViewModel::class.java)
    }

    private fun setViewPager() {
        viewPagerAdapter = MoviePagerAdapter(this)
        binding.viewPager2.adapter = viewPagerAdapter
        binding.viewPager2.offscreenPageLimit = 1
        val nextItemVisiblePx = resources.getDimension(R.dimen.viewpager_next_item_visible)
        val currentItemHorizontalMarginPx =
            resources.getDimension(R.dimen.viewpager_current_item_horizontal_margin)
        val pageTranslationX = nextItemVisiblePx + currentItemHorizontalMarginPx
        val pageTransformer = ViewPager2.PageTransformer { page: View, position: Float ->
            page.translationX = -pageTranslationX * position
            // Next line scales the item's height. You can remove it if you don't want this effect
            page.scaleY = 1 - (0.25f * kotlin.math.abs(position))
            // If you want a fading effect uncomment the next line:
            // page.alpha = 0.25f + (1 - abs(position))
        }
        viewPager2.setPageTransformer(pageTransformer)


        val recyclerViewInstance =
            binding.viewPager2.children.first { it is RecyclerView } as RecyclerView
        recyclerViewInstance.also {
            it.clipToPadding = false
            it.addItemDecoration(ItemDecoration())
        }
    }

    class ItemDecoration : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            super.getItemOffsets(outRect, view, parent, state)
            outRect.right = 42.toPx()
            outRect.left = 42.toPx()
        }
    }

    private fun setRecyclerView() {
        binding.rv.layoutManager = GridLayoutManager(this, 3)
        binding.rv.setHasFixedSize(true)
        adapter = MovieAdapter(this)
        binding.rv.adapter = adapter
    }
}