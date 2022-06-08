package com.bangkit.artnesia.ui.fragment

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
import com.bangkit.artnesia.data.local.ArticleData
import com.bangkit.artnesia.data.local.CategoryData
import com.bangkit.artnesia.data.local.LiteratureData
import com.bangkit.artnesia.data.model.ArticleModel
import com.bangkit.artnesia.data.model.CategoryModel
import com.bangkit.artnesia.data.model.Literature
import com.bangkit.artnesia.data.model.LiteratureModel
import com.bangkit.artnesia.databinding.FragmentExploreBinding
import com.bangkit.artnesia.ui.activity.LiteratureActivity
import com.bangkit.artnesia.ui.activity.MyProductActivity
import com.bangkit.artnesia.ui.adapter.ArticleAdapter
import com.bangkit.artnesia.ui.adapter.CategoryAdapter
import com.bangkit.artnesia.ui.adapter.ExploreLiteratureAdapter
import com.google.firebase.firestore.*

class ExploreFragment : Fragment() {

    private var _binding: FragmentExploreBinding? = null
    private val binding get() = _binding as FragmentExploreBinding

    private lateinit var articleAdapter: ArticleAdapter
    private lateinit var categoryAdapter: CategoryAdapter

    private val mFireStore = FirebaseFirestore.getInstance()

    private lateinit var literatureAdapter: ExploreLiteratureAdapter
    private lateinit var literatureList: ArrayList<Literature>
    private lateinit var literatureRV: RecyclerView

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

        getLiterature()

        articleAdapter = ArticleAdapter(requireActivity())
        articleAdapter.setData(getArticle())
        categoryAdapter = CategoryAdapter(requireActivity())
        categoryAdapter.setData(getCategory())

        binding.rvArticle.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvArticle.setHasFixedSize(true)
        binding.rvArticle.adapter = articleAdapter

        binding.rvCategory.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvCategory.setHasFixedSize(true)
        binding.rvCategory.adapter = categoryAdapter

        binding.tvViewAll.setOnClickListener {
            activity?.let {
                it.startActivity(Intent(it, LiteratureActivity::class.java))
            }
        }
    }

    private fun getLiterature(){
        literatureRV = binding.rvLiterature
        literatureRV.layoutManager = LinearLayoutManager(requireContext())
        literatureRV.setHasFixedSize(true)

        literatureList = arrayListOf()

        literatureAdapter  = ExploreLiteratureAdapter(requireActivity() , literatureList)

        literatureRV.adapter = literatureAdapter

        getLiteratureData()
    }

    private fun getLiteratureData() {
        mFireStore.collection("literature")
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(
                    value: QuerySnapshot?,
                    error: FirebaseFirestoreException?
                ) {
                    if (error!=null){
                        Log.e("Firestore error", error.message.toString())
                        return
                    }

                    for (dc: DocumentChange in value?.documentChanges!!){
                        if (dc.type == DocumentChange.Type.ADDED){
                            //productArrayList.add(dc.document.toObject(Product::class.java))
                            val lit = dc.document.toObject(Literature::class.java)
                            lit.literature_id = dc.document.id

                            literatureList.add(lit)
                        }
                    }
                    //randomize list
                    literatureList.shuffle()

                    literatureAdapter.notifyDataSetChanged()
                }
            })
    }
}