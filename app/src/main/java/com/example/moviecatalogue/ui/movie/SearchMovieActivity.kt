package com.example.moviecatalogue.ui.movie

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviecatalogue.R
import com.example.moviecatalogue.adapter.MovieAdapter
import com.example.moviecatalogue.model.Movie
import com.example.moviecatalogue.viewmodel.SearchMovieViewModel
import kotlinx.android.synthetic.main.activity_search_movie.*
import kotlin.collections.ArrayList


class SearchMovieActivity : AppCompatActivity() {

    private lateinit var movieViewModel: SearchMovieViewModel
    private lateinit var adapter: MovieAdapter

    companion object {
        const val EXTRA_INTENT = "extra_intent"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_movie)

        val query = intent.getStringExtra(EXTRA_INTENT)
        Toast.makeText(this, query, Toast.LENGTH_SHORT).show()

        movieViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(SearchMovieViewModel::class.java)

        rv_search_movie.layoutManager = LinearLayoutManager(this)


        movieViewModel.setDataSearch(query).observe(this, Observer { t ->
            t?.results.let {
                if (it != null) {
                    showLoading(false)
                    showData(it as ArrayList<Movie>)
                }
            }
        })
    }

    private fun showData(data: ArrayList<Movie>) {
        adapter = MovieAdapter(data) {
            val intent = Intent(this, MovieDetailActivity::class.java)
            intent.putExtra(MovieDetailActivity.EXTRA_MOVIE, it.id)
            startActivity(intent)
        }
        adapter.notifyDataSetChanged()
        rv_search_movie.adapter = adapter
    }

    private fun showLoading(loading: Boolean) {
        if (loading) {
            sm_movie.startShimmer()
            sm_movie.visibility = View.VISIBLE
        } else {
            sm_movie.stopShimmer()
            sm_movie.visibility = View.GONE
        }
    }
}
