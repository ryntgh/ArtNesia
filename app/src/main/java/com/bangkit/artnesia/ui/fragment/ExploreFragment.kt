package com.bangkit.artnesia.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.artnesia.data.local.ArticleData
import com.bangkit.artnesia.data.local.CategoryData
import com.bangkit.artnesia.data.local.LiteratureData
import com.bangkit.artnesia.data.model.ArticleModel
import com.bangkit.artnesia.data.model.CategoryModel
import com.bangkit.artnesia.data.model.LiteratureModel
import com.bangkit.artnesia.databinding.FragmentExploreBinding
import com.bangkit.artnesia.ui.adapter.ArticleAdapter
import com.bangkit.artnesia.ui.adapter.CategoryAdapter
import com.bangkit.artnesia.ui.adapter.ExploreLiteratureAdapter

class ExploreFragment : Fragment() {

    private var _binding: FragmentExploreBinding? = null
    private val binding get() = _binding as FragmentExploreBinding

    private lateinit var literatureAdapter: ExploreLiteratureAdapter
    private lateinit var articleAdapter: ArticleAdapter
    private lateinit var categoryAdapter: CategoryAdapter

    private fun getLiterature(): List<LiteratureModel> {
        return LiteratureData.generateLiterature()
    }

    private fun getArticle(): List<ArticleModel> {
        return ArticleData.generateArticle()
    }

    private fun getCategory(): List<CategoryModel> {
        return CategoryData.generateCategory()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExploreBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.hide()

        literatureAdapter = ExploreLiteratureAdapter(requireActivity())
        literatureAdapter.setData(getLiterature())
        articleAdapter = ArticleAdapter(requireActivity())
        articleAdapter.setData(getArticle())
        categoryAdapter = CategoryAdapter(requireActivity())
        categoryAdapter.setData(getCategory())

        binding.rvLiterature.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvLiterature.setHasFixedSize(true)
        binding.rvLiterature.adapter = literatureAdapter

        binding.rvArticle.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvArticle.setHasFixedSize(true)
        binding.rvArticle.adapter = articleAdapter

        binding.rvCategory.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvCategory.setHasFixedSize(true)
        binding.rvCategory.adapter = categoryAdapter
    }
}