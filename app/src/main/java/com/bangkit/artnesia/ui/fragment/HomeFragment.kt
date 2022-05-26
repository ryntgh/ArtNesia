package com.bangkit.artnesia.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.artnesia.R
import com.bangkit.artnesia.data.local.ArticleData
import com.bangkit.artnesia.data.local.LiteratureData
import com.bangkit.artnesia.data.model.ArticleModel
import com.bangkit.artnesia.data.model.LiteratureModel
import com.bangkit.artnesia.databinding.FragmentHomeBinding
import com.bangkit.artnesia.ui.adapter.ArticleAdapter
import com.bangkit.artnesia.ui.adapter.LiteratureAdapter
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel

class HomeFragment : Fragment() {

    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding as FragmentHomeBinding

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
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater , container , false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val slideModel : ArrayList<SlideModel> = arrayListOf()

        (activity as AppCompatActivity).supportActionBar?.hide()

        slideModel.add(SlideModel("https://s3-kemenparekraf.s3.ap-southeast-1.amazonaws.com/EKONOMI_KREATIF_SENI_PERTUNJUKAN_shutterstock_1446792446_oki_cahyo_nugroho_d60c5c608e.jpg"))
        slideModel.add(SlideModel("https://akcdn.detik.net.id/community/media/visual/2019/11/02/2472af9a-055d-46dc-bff3-630d5ed510e5_43.jpeg?w=250&q="))
        slideModel.add(SlideModel("https://cdnaz.cekaja.com/media/2020/06/1505_Artikel-CA20_-kesenian-tradisional-khas-jawa-barat.jpg"))
        slideModel.add(SlideModel("https://cdn-2.tstatic.net/manado/foto/bank/images/pesta-kesenian-bali-1.jpg"))

        binding.isHome.setImageList(slideModel , ScaleTypes.CENTER_CROP)

        literatureAdapter = LiteratureAdapter(requireActivity())
        literatureAdapter.setData(getLiterature())
        articleAdapter = ArticleAdapter(requireActivity())
        articleAdapter.setData(getArticle())

        binding.exploreRecView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.exploreRecView.setHasFixedSize(true)
        binding.exploreRecView.adapter = literatureAdapter

        binding.articleRecView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.articleRecView.setHasFixedSize(true)
        binding.articleRecView.adapter = articleAdapter
    }
}