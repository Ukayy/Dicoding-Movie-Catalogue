package com.example.moviecatalogue.ui.tvshow


import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.annotation.Nullable
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.moviecatalogue.R
import com.example.moviecatalogue.adapter.TVShowAdapter
import com.example.moviecatalogue.model.TVShow
import com.example.moviecatalogue.viewmodel.TvViewModel
import kotlinx.android.synthetic.main.fragment_tvshow.*
import java.util.*
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 */class TVShowFragment : Fragment() {

    private lateinit var tvViewModel: TvViewModel
    private lateinit var adapter: TVShowAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tvshow, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        tvViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(TvViewModel::class.java)

        rv_tv_show.layoutManager = LinearLayoutManager(activity)

        tvViewModel.setData().observe(viewLifecycleOwner, Observer {t->
            t?.result.let {
                showLoading(false)
                if(it != null){
                    showData(it as ArrayList<TVShow>)
                }
            }
        })
    }

    private fun showData(data: ArrayList<TVShow>) {
        adapter = TVShowAdapter(data) {
            val intent = Intent(activity, TvShowDetailActivity::class.java)
            intent.putExtra(TvShowDetailActivity.EXTRA_TV_SHOW, it.id)
            startActivity(intent)
        }
        adapter.notifyDataSetChanged()
        rv_tv_show.adapter = adapter
    }

    private fun showLoading(loading: Boolean) {
        if (loading) {
            sm_tv.startShimmer()
            sm_tv.visibility = View.VISIBLE
        } else {
            sm_tv.stopShimmer()
            sm_tv.visibility = View.GONE
        }
    }

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        //val search: MenuItem = menu.findItem(R.id.search)
        val searchManager = Objects.requireNonNull(
            Objects.requireNonNull(activity)?.getSystemService(
                Context.SEARCH_SERVICE)
        ) as SearchManager
        val searchView: SearchView = menu.findItem(R.id.search).getActionView() as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(activity!!.componentName))
        searchView.setQueryHint(resources.getString(R.string.search_movie))
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val moveIntent = Intent(activity, SearchTvActivity::class.java)
                moveIntent.putExtra(SearchTvActivity.EXTRA_INTENT, query)
                startActivity(moveIntent)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
    }
}

