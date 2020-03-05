package com.example.moviecatalogue.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.moviecatalogue.model.MovieResponse
import com.example.moviecatalogue.network.NetworkConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchMovieViewModel : ViewModel() {

    private var data = MutableLiveData<MovieResponse>()



    private fun getSearchMovie(query : String) {
        GlobalScope.launch(Dispatchers.Main){
            NetworkConfig().create().getSearchMovie(query).enqueue(object :
                Callback<MovieResponse> {
                override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                    Log.e("Exception", t.message)
                    data.postValue(null)
                }

                override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                    if (response.isSuccessful) {
                        response.body().let {
                            data.postValue(it)
                        }
                    }
                }

            })
        }
    }

    fun setDataSearch(query: String): MutableLiveData<MovieResponse> {
        getSearchMovie(query)
        return data
    }

}