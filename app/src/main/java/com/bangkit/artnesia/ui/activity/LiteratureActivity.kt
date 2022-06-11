package com.bangkit.artnesia.ui.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.artnesia.databinding.ActivityLiteratureBinding
import com.bangkit.artnesia.ui.adapter.LitAdapter
import com.bangkit.artnesia.ui.utils.LiteratureViewModel
import com.bangkit.artnesia.ui.utils.ViewModelFactory

class LiteratureActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLiteratureBinding
    private lateinit var litAdapter: LitAdapter
    private lateinit var viewModelFactory: ViewModelFactory
    private val literatureViewModel: LiteratureViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLiteratureBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        viewModelFactory = ViewModelFactory.getInstance(this)
        resource()

        binding.rvListLiterature.layoutManager = LinearLayoutManager(this)
        litAdapter = LitAdapter()
        literatureViewModel.dataLiterature.observe(this) {
            litAdapter.setData(it)
        }
        binding.rvListLiterature.adapter = litAdapter

        literatureViewModel.message.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }

        val resultStr = intent.getStringExtra("imageDetection")
        binding.search.setQuery(resultStr, false)

        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                litAdapter.filter.filter(newText)
                return false
            }

        })

        getListData()

    }

    private fun getListData() {
        literatureViewModel.getLiteratures()
    }

    private fun resource() {
        literatureViewModel.loading.observe(this) { event ->
            event.getContentIfNotHandled()?.let {
                isLoading(it)
            }
        }
    }

    private fun isLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar3.visibility = View.VISIBLE
        } else {
            binding.progressBar3.visibility = View.GONE
        }
    }
}