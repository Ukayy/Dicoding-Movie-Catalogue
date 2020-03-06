package com.example.moviecatalogue.ui.movie


import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviecatalogue.R
import com.example.moviecatalogue.adapter.MovieAdapter
import com.example.moviecatalogue.model.Movie
import com.example.moviecatalogue.viewmodel.MovieViewModel
import kotlinx.android.synthetic.main.fragment_movie.*
import java.util.*
import kotlin.collections.ArrayList


/**
 * A simple [Fragment] subclass.
 */
class MovieFragment : Fragment() {
    private lateinit var movieViewModel: MovieViewModel
    private lateinit var adapter: MovieAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_movie, container, false)
    }

    override fun onActivityCreated(
        savedInstanceState: Bundle?
    ) {
        super.onActivityCreated(savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.show()
        movieViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(MovieViewModel::class.java)

        rv_movie.layoutManager = LinearLayoutManager(activity)

        movieViewModel.setData().observe(viewLifecycleOwner, Observer { t ->
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
            val intent = Intent(activity, MovieDetailActivity::class.java)
            intent.putExtra(MovieDetailActivity.EXTRA_MOVIE, it.id)
            startActivity(intent)
        }
        adapter.notifyDataSetChanged()
        rv_movie.adapter = adapter
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
                val moveIntent = Intent(activity, SearchMovieActivity::class.java)
                moveIntent.putExtra(SearchMovieActivity.EXTRA_INTENT, query)
                startActivity(moveIntent)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
    }

}
