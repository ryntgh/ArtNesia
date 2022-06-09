package com.bangkit.artnesia.ui.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.artnesia.data.local.CategoryData
import com.bangkit.artnesia.data.model.Article
import com.bangkit.artnesia.data.model.CategoryModel
import com.bangkit.artnesia.data.model.Literature
import com.bangkit.artnesia.databinding.FragmentExploreBinding
import com.bangkit.artnesia.ui.activity.LiteratureActivity
import com.bangkit.artnesia.ui.adapter.ArticleAdapter
import com.bangkit.artnesia.ui.adapter.CategoryAdapter
import com.bangkit.artnesia.ui.adapter.ExploreLiteratureAdapter
import com.google.firebase.firestore.*

class ExploreFragment : Fragment() {

    private var _binding: FragmentExploreBinding? = null
    private val binding get() = _binding as FragmentExploreBinding
    private val mFireStore = FirebaseFirestore.getInstance()

    private lateinit var categoryAdapter: CategoryAdapter

    private lateinit var literatureAdapter: ExploreLiteratureAdapter
    private lateinit var literatureList: ArrayList<Literature>
    private lateinit var literatureRV: RecyclerView

    private lateinit var articleAdapter: ArticleAdapter
    private lateinit var articleList: ArrayList<Article>
    private lateinit var articleRV: RecyclerView

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

        binding.tvViewAll.setOnClickListener {
            activity?.let {
                it.startActivity(Intent(it, LiteratureActivity::class.java))
            }
        }

        categoryAdapter = CategoryAdapter(requireActivity())
        categoryAdapter.setData(getCategory())

        binding.rvCategory.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvCategory.setHasFixedSize(true)
        binding.rvCategory.adapter = categoryAdapter

        getLiterature()
        getArticle()
    }

    private fun getLiterature() {
        literatureRV = binding.rvLiterature
        literatureRV.layoutManager = LinearLayoutManager(requireContext())
        literatureRV.setHasFixedSize(true)

        literatureList = arrayListOf()

        literatureAdapter = ExploreLiteratureAdapter(requireActivity(), literatureList)

        literatureRV.adapter = literatureAdapter

        getLiteratureData()
    }

    private fun getArticle() {
        articleRV = binding.rvArticle
        articleRV.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        articleRV.setHasFixedSize(true)

        articleList = arrayListOf()

        articleAdapter = ArticleAdapter(requireActivity(), articleList)

        articleRV.adapter = articleAdapter

        getArticleData()
    }

    private fun getLiteratureData() {
        mFireStore.collection("literature")
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onEvent(
                    value: QuerySnapshot?,
                    error: FirebaseFirestoreException?
                ) {
                    if (error != null) {
                        Log.e("Firestore error", error.message.toString())
                        return
                    }

                    for (dc: DocumentChange in value?.documentChanges!!) {
                        if (dc.type == DocumentChange.Type.ADDED) {
                            val lit = dc.document.toObject(Literature::class.java)
                            lit.literature_id = dc.document.id

                            literatureList.add(lit)
                        }
                    }
                    literatureList.shuffle()

                    literatureAdapter.notifyDataSetChanged()
                }
            })
    }

    private fun getArticleData() {
        mFireStore.collection("article")
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onEvent(
                    value: QuerySnapshot?,
                    error: FirebaseFirestoreException?
                ) {
                    if (error != null) {
                        Log.e("Firestore error", error.message.toString())
                        return
                    }

                    for (dc: DocumentChange in value?.documentChanges!!) {
                        if (dc.type == DocumentChange.Type.ADDED) {
                            val artc = dc.document.toObject(Article::class.java)
                            artc.article_id = dc.document.id

                            articleList.add(artc)
                        }
                    }
                    articleList.shuffle()

                    articleAdapter.notifyDataSetChanged()
                }
            })
    }
}