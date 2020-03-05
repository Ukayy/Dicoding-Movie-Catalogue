package com.example.moviecatalogue.ui.tvshow

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviecatalogue.R
import com.example.moviecatalogue.adapter.TVShowAdapter
import com.example.moviecatalogue.model.TVShow
import com.example.moviecatalogue.viewmodel.SearchTvViewModel
import kotlinx.android.synthetic.main.activity_search_tv.*

class SearchTvActivity : AppCompatActivity() {

    private lateinit var viewModel: SearchTvViewModel
    private lateinit var adapter: TVShowAdapter

    companion object {
        const val EXTRA_INTENT = "extra_intent"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_tv)

        val query = intent.getStringExtra(EXTRA_INTENT)
        Toast.makeText(this, query, Toast.LENGTH_SHORT).show()

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(SearchTvViewModel::class.java)

        rv_search_tv.layoutManager = LinearLayoutManager(this)


        viewModel.setDataSearch(query).observe(this, Observer { t ->
            t?.result.let {
                if (it != null) {
                    showLoading(false)
                    showData(it as ArrayList<TVShow>)
                }
            }
        })
    }

    private fun showData(data: ArrayList<TVShow>) {
        adapter = TVShowAdapter(data) {
            val intent = Intent(this, TvShowDetailActivity::class.java)
            intent.putExtra(TvShowDetailActivity.EXTRA_TV_SHOW, it.id)
            startActivity(intent)
        }
        adapter.notifyDataSetChanged()
        rv_search_tv.adapter = adapter
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
}
