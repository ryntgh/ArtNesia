package com.bangkit.artnesia.ui.utils

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.artnesia.data.remote.response.LitResponse
import com.bangkit.artnesia.data.remote.response.LiteratureItem
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LiteratureViewModel(private val repository: Repository) : ViewModel() {

    private var _dataLiterature = MutableLiveData<ArrayList<LiteratureItem>>()
    val dataLiterature: LiveData<ArrayList<LiteratureItem>> = _dataLiterature

    private var _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private var _loading = MutableLiveData<Event<Boolean>>()
    val loading: LiveData<Event<Boolean>> = _loading

    private var _error = MutableLiveData<Event<Boolean>>()
    val error: LiveData<Event<Boolean>> = _error

    fun getLiteratures() {
        val client = repository.getLiteratureData()
        client.enqueue(object : Callback<LitResponse> {
            override fun onResponse(call: Call<LitResponse>, response: Response<LitResponse>) {
                if (response.isSuccessful) {
                    val userResponse = response.body()?.literature
                    repository.appExecutors.networkIO.execute {
                        _dataLiterature.postValue(userResponse!!)
                    }
                    _error.value = Event(false)
                } else {
                    val jObjError = JSONObject(response.errorBody()!!.string())
                    val msg = jObjError.getString("message")
                    Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                    _message.value = Event(msg).toString()
                    _error.value = Event(true)
                }
            }

            override fun onFailure(call: Call<LitResponse>, t: Throwable) {
                Log.e(ContentValues.TAG, "onFailure: " + t.message)
                _loading.value = Event(false)
                _message.value = Event(t.message.toString()).toString()
                _error.value = Event(true)
            }
        })
    }

}