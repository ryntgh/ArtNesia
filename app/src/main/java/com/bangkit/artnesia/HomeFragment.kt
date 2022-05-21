package com.bangkit.artnesia

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.artnesia.data.local.ArticleData
import com.bangkit.artnesia.data.local.LiteratureData
import com.bangkit.artnesia.data.model.ArticleModel
import com.bangkit.artnesia.data.model.LiteratureModel
import com.bangkit.artnesia.ui.adapter.ArticleAdapter
import com.bangkit.artnesia.ui.adapter.LiteratureAdapter

class HomeFragment : Fragment() {

    lateinit var rvLiterature: RecyclerView
    lateinit var rvArticle: RecyclerView

    private lateinit var literatureAdapter: LiteratureAdapter
    private lateinit var articleAdapter: ArticleAdapter

    private fun getLiterature(): List<LiteratureModel> {
        return LiteratureData.generateLiterature()
    }

    private fun getArticle(): List<ArticleModel>{
        return ArticleData.generateArticle()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        rvLiterature = view.findViewById(R.id.exploreRecView)
        rvArticle = view.findViewById(R.id.articleRecView)

        return view
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.hide()

        literatureAdapter = LiteratureAdapter(requireActivity())
        literatureAdapter.setData(getLiterature())
        articleAdapter = ArticleAdapter(requireActivity())
        articleAdapter.setData(getArticle())

        rvLiterature.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        rvLiterature.setHasFixedSize(true)
        rvLiterature.adapter = literatureAdapter

        rvArticle.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        rvArticle.setHasFixedSize(true)
        rvArticle.adapter = articleAdapter
    }
}